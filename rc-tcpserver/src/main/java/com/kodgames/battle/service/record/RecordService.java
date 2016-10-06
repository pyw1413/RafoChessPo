package com.kodgames.battle.service.record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rafo.chess.manager.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battle.common.Player;
import com.kodgames.battle.common.Room;
import com.kodgames.battle.common.constants.GlobalConstants;
import com.kodgames.battle.entity.battle.BattleStatisticBean;
import com.kodgames.battle.entity.record.BGRecordLogSYN;
import com.kodgames.battle.entity.record.BWRoomRecordRES;
import com.kodgames.battle.entity.record.BWRoundRecordRES;
import com.kodgames.battle.entity.record.RoomStatisticsPROTO;
import com.kodgames.battle.entity.record.RoundDatas;
import com.kodgames.battle.util.DateTimeUtil;
import com.kodgames.battle.util.RedisKeyConstants;


public class RecordService
{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1452350439730871003L;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	
	public BWRoomRecordRES getRoomRecord(int playerID, String accountID)
	{
		List<BattleStatistic> battleStatistics = new ArrayList<>(); // 玩家历史战斗数据
		List<BattleStatisticBean> beans = new ArrayList<BattleStatisticBean>();
		Map<BattleStatistic, BattleStatisticBean> data2Bean = new HashMap<BattleStatistic, BattleStatisticBean>();
		BWRoomRecordRES builder = new BWRoomRecordRES();
		builder.setAccountID(accountID);
		getStatisticsFromDBByPlayerID(playerID, battleStatistics, beans, data2Bean);
		
		if(beans.size() == 0)
		{
			logger.error("player has no record in db, AccountID={}, playerID={}", accountID, playerID);
			builder.setResult(GlobalConstants.ROOM_RECORD_FAILED_NODATA);
			return builder;
		}

		try
		{
			Date twoDaysAgo = DateTimeUtil.getTwoDayAgo(new Date());
			for (BattleStatisticBean bean : beans)
			{
				RoomStatisticsPROTO roomStatisticsPROTO = RoomStatisticsPROTO.parseFrom(bean.getRoom_statistic());
				RoomStatistic temp = new RoomStatistic(roomStatisticsPROTO);
				
				Date roomStartDate = new Date(temp.getStartTime());
				if(!roomStartDate.before(twoDaysAgo))
					builder.addRoomBattleStatistics(roomStatisticsPROTO);
			}
			builder.setResult(GlobalConstants.ROOM_RECORD_SUCCESS);
		}
		catch(Exception e)
		{
			logger.error("get room record error, playerID={}", playerID);
			builder.setResult(GlobalConstants.ROOM_RECORD_FAILED);
		}
		return builder;
	}
	
	public BWRoundRecordRES getRoundRecords(String accountID, int roomID, int playerID)
	{
		List<BattleStatistic> battleStatistics = new ArrayList<>(); // 玩家历史战斗数据
		List<BattleStatisticBean> beans = new ArrayList<BattleStatisticBean>();
		Map<BattleStatistic, BattleStatisticBean> data2Bean = new HashMap<BattleStatistic, BattleStatisticBean>();
		
		BWRoundRecordRES builder = new BWRoundRecordRES();
		builder.setAccountID(accountID);
		getStatisticsFromDBByPlayerID(playerID, battleStatistics, beans, data2Bean);
		if(beans.size() == 0)
		{
			logger.error("player has no record in db, AccountID={}, playerID={}", accountID, playerID);
			builder.setResult(GlobalConstants.ROUND_RECORD_FAILED_NODATA);
			return builder;
		}
		
		for (BattleStatistic battleStatistic : battleStatistics) // ROOMID与playerID联合做为标志
		{
			if(isStatisti4Player(roomID, playerID, battleStatistic))
			{
				List<RoundRecord> roundRecords = battleStatistic.getRoundRecordDatas();
				for (RoundRecord roundRecord : roundRecords)
				{
					builder.addRoundData(roundRecord.toRoundDataPROTO());
				}
			}
		}
		builder.setResult(GlobalConstants.ROUND_RECORD_SUCCESS);
		return builder;
	}
	

	private void addPlayerRoundCount(RoomStatistic roomStatistic)
	{
		List<Integer> playerIDs = get4Players(roomStatistic); 
		for (Integer playerID : playerIDs)
		{
			String totalStr = RedisManager.getInstance().get(RedisKeyConstants.PLAYER_ROUND_INFO + playerID);
			if(totalStr != null)
			{
				RedisManager.getInstance().incr(RedisKeyConstants.PLAYER_ROUND_INFO + playerID);
			}
		}
	}
	
	public void saveRoundToDB(int roomID, int ownerID, int roomType, Boolean isFinish, RoomStatistic roomStatistic, RoundRecord roundRecord)
	{
		addPlayerRoundCount(roomStatistic);
		int currRoundCount = 0;
		List<BattleStatistic> battleStatistics = new ArrayList<>(); // 玩家历史战斗数据
		List<BattleStatisticBean> beans = new ArrayList<BattleStatisticBean>();
		Map<BattleStatistic, BattleStatisticBean> data2Bean = new HashMap<BattleStatistic, BattleStatisticBean>();
		
		boolean hasRecord = false;
		getStatisticsFromDBByRoomID(roomID, battleStatistics, beans, data2Bean); // 获得所有房间号为roomID的相关数据，再行筛选
		if(battleStatistics.size() == 0) // 该房间中的某玩家没有写过，则代表该房间没有被写过
		{	
			currRoundCount = createBean(roomID, ownerID, roomType, isFinish, roomStatistic, roundRecord);
		}
		
		else 
		{
			for (BattleStatistic battleStatistic : battleStatistics)
			{
				if(battleStatistic.getRoomID() != roomID)
				{
					continue;
				}
				
				if(battleStatistic.getRoomStatisticsData().getStartTime() != roomStatistic.getStartTime())
				{
					continue;
				}
				List<Integer> dbPlayerIDs = get4Players(battleStatistic.getRoomStatisticsData());
				List<Integer> playerIDs = get4Players(roomStatistic); 
				if(!isSamePlayers(dbPlayerIDs, playerIDs))
				{
					continue;
				}
				
				hasRecord = true;
				// 至此，房间号，四个人，以及房间创建时间一致，可以认为是指定的记录
				currRoundCount = updateBean(roomID, ownerID, roomType, isFinish, roomStatistic, roundRecord, battleStatistic, data2Bean);
			}
			if(hasRecord == false)
			{
				currRoundCount = createBean(roomID, ownerID, roomType, isFinish, roomStatistic, roundRecord);
			}
		}
		
		sendRecordLog(roomID, roomStatistic, ownerID, isFinish,  roomType, roundRecord, currRoundCount);
	}
	
	public void changeRoomByVote(Room room)
	{
		List<BattleStatistic> battleStatistics = new ArrayList<>(); // 玩家历史战斗数据
		List<BattleStatisticBean> beans = new ArrayList<BattleStatisticBean>();
		Map<BattleStatistic, BattleStatisticBean> data2Bean = new HashMap<BattleStatistic, BattleStatisticBean>();

		getStatisticsFromDBByRoomID(room.getRoomID(), battleStatistics, beans, data2Bean); // 获得所有房间号为roomID的相关数据，再行筛选
		if(battleStatistics.size() == 0) // 该房间中的某玩家没有写过，则代表该房间没有被写过
		{	
			return; // 无记录需要更改
		}
		else
		{
			BattleStatistic latestStatic = null;
			int i = 0;
			for (BattleStatistic battleStatistic : battleStatistics)
			{
				if(battleStatistic.getRoomID() != room.getRoomID())
				{
					continue;
				}
				List<Integer> dbPlayerIDs = get4Players(battleStatistic.getRoomStatisticsData());
				List<Integer> playerIDs = new ArrayList<>();
				Map<String, Player> account2Player = room.getPlayers();
				for (Player player : account2Player.values())
				{
					playerIDs.add(player.getID());
				}
				
				if(!isSamePlayers(dbPlayerIDs, playerIDs))
				{
					continue;
				}
				
				if(i == 0)
				{
					latestStatic = battleStatistics.get(0);
					++i;
					continue;
				}
				if(latestStatic.getRoomStatisticsData().getStartTime() <= battleStatistic.getRoomStatisticsData().getStartTime())
				{
					latestStatic = battleStatistic;
				}
			}
			
			if(latestStatic == null)
				return;

			// 至此，房间号，四个人，以及房间创建时间一致，可以认为是指定的记录
/*			IBattleStatisticBeanDao dao = DBCS.getExector(IBattleStatisticBeanDao.class);
			BattleStatisticBean bean = data2Bean.get(latestStatic);
			bean.setIsFinish((byte)2);
			dao.updateBattleStatisticBean(bean);*/
		}
	}
	
	private BGRecordLogSYN sendRecordLog(Integer roomID, RoomStatistic roomStatistic, int ownerID, Boolean isFinish, int roomType, RoundRecord roundRecord, int currRoundCount)
	{
		BGRecordLogSYN builder = new BGRecordLogSYN();
		builder.setRoomID(roomID);
		builder.setRoomStartTime(roomStatistic.getStartTime());
		builder.setRoomType(roomType);
		builder.addAllPlayerIDs(get4Players(roomStatistic));
		builder.setOwnerID(ownerID);
		builder.setFinished(isFinish);
		builder.setRoundRecordStartTime(roundRecord.getStartTime());
		builder.setCurrRoundCount(currRoundCount);
		
/*		ServerService serverService = ServiceContainer.getInstance().getPublicService(ServerService.class);
		Transmitter.getInstance().write(serverService.getGameNode(), GlobalConstants.DEFAULT_CALLBACK, builder.build());*/
		return builder;
	}
	
	private int updateBean(int roomID, int ownerID, int roomType, 
		Boolean isFinish, RoomStatistic roomStatistic, RoundRecord roundRecord, 
		BattleStatistic battleStatistic, Map<BattleStatistic, BattleStatisticBean> data2Bean)
	{
		BattleStatisticBean bean = data2Bean.get(battleStatistic);
		bean.setRoomID(roomID);
		List<Integer> playerIDs = get4Players(roomStatistic);
		bean.setPlayerID1(playerIDs.get(0));
		bean.setPlayerID2(playerIDs.get(1));
		bean.setPlayerID3(playerIDs.get(2));
		bean.setPlayerID4(playerIDs.get(3));
		bean.setOwnerID(ownerID);
		bean.setRoomType(roomType);
		bean.setIsFinish(isFinish == true ? (byte)1 : (byte)0);
		bean.setRoom_statistic(roomStatistic.toRoomStatisticsPROTO().toByteArray());
		
/*		List<RoundRecord> roundRecords = battleStatistic.getRoundRecordDatas();
		roundRecords.add(roundRecord);
		RoundDatas round_datas = this.getRoundDataProto(roundRecords);
		bean.setRound_datas(round_datas.toByteArray());
		
		IBattleStatisticBeanDao dao = DBCS.getExector(IBattleStatisticBeanDao.class);
		dao.updateBattleStatisticBean(bean);

		return roundRecords.size();*/
		return 0;
	}
	
	private int createBean(int roomID,int ownerID, int roomType, Boolean isFinish, RoomStatistic roomStatistic, RoundRecord roundRecord)
	{
		List<Integer> playerIDs = get4Players(roomStatistic);
		BattleStatisticBean bean = new BattleStatisticBean();
		bean.setRoomID(roomID);
		bean.setPlayerID1(playerIDs.get(0));
		bean.setPlayerID2(playerIDs.get(1));
		bean.setPlayerID3(playerIDs.get(2));
		bean.setPlayerID4(playerIDs.get(3));
		bean.setOwnerID(ownerID);
		bean.setRoomType(roomType);
		bean.setIsFinish(isFinish == true ? (byte)1 : (byte)0);
//		bean.setRoom_statistic(roomStatistic.toRoomStatisticsPROTO().toByteArray());
		
		List<RoundRecord> roundRecords = new ArrayList<>();
		roundRecords.add(roundRecord);
//		RoundDatas round_datas = this.getRoundDataProto(roundRecords);
//		bean.setRound_datas(round_datas.toByteArray());
		
//		IBattleStatisticBeanDao dao = DBCS.getExector(IBattleStatisticBeanDao.class);
//		dao.insertBattleStatisticBean(bean);*/
		//记录玩家对房间的信息
		RedisManager.getInstance().sadd(RedisKeyConstants.PLAYERID_TO_ROOMIDS + playerIDs.get(0), roomID + "");
		RedisManager.getInstance().sadd(RedisKeyConstants.PLAYERID_TO_ROOMIDS + playerIDs.get(1), roomID + "");
		RedisManager.getInstance().sadd(RedisKeyConstants.PLAYERID_TO_ROOMIDS + playerIDs.get(2), roomID + "");
		RedisManager.getInstance().sadd(RedisKeyConstants.PLAYERID_TO_ROOMIDS + playerIDs.get(3), roomID + "");
		//记录房间信息
		RedisManager.getInstance().lpush(RedisKeyConstants.ROOMID_TO_ROOM_INFO + roomID, bean);
		return 1;
	}
	
	private boolean isStatisti4Player(int roomID, int playerID, BattleStatistic battleStatistic)
	{
		if(battleStatistic.getRoomID() == roomID)
		{
			if(battleStatistic.getPlayerID1() == playerID || 
				battleStatistic.getPlayerID2() == playerID || 
				battleStatistic.getPlayerID3() == playerID || 
				battleStatistic.getPlayerID4() == playerID)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	/*
	 * 从数据库取出playerID对应的beans与BattleStatistic
	 */
	private int getStatisticsFromDBByPlayerID(int playerID, List<BattleStatistic> battleStatistics, 
		List<BattleStatisticBean> beans, Map<BattleStatistic, BattleStatisticBean> data2Bean)
	{
		beans.clear();
		Set<String> roomIds = RedisManager.getInstance().smembers(RedisKeyConstants.PLAYERID_TO_ROOMIDS + playerID);
		List<BattleStatisticBean> tempBeans = new ArrayList<BattleStatisticBean>();
		if(!roomIds.isEmpty()) {
			for(String roomID : roomIds) {
				tempBeans.addAll(getRoomStatisticBeanBy(Integer.valueOf(roomID)));
			}
		}
		// 数据库对象为null
		if (tempBeans.isEmpty())
		{
			return 0;
		}
		beans.addAll(tempBeans);
		unmarshal(battleStatistics, beans, data2Bean);
//		logger.error("----getStatisticsFromDBByPlayerID---");
//		logger.error("battleStatistics size : " + battleStatistics.size());
		logger.error("beans size : " + beans.size());
		logger.error("data2Bean size : " + data2Bean.size());
		return 0;
	}
	
	private int getStatisticsFromDBByRoomID(int roomID, List<BattleStatistic> battleStatistics,
		List<BattleStatisticBean> beans, Map<BattleStatistic, BattleStatisticBean> data2Bean)
	{
		beans.clear();
		//从redis取出记录
		List<BattleStatisticBean> tempBeans = getRoomStatisticBeanBy(roomID);
		// 数据库对象为空
		if (tempBeans.isEmpty())
		{
			return 0;
		}
		beans.addAll(tempBeans);
		unmarshal(battleStatistics, beans, data2Bean);
		
		return 0;
	}
	
	/**
	 * 根据房间id取出房间记录信息
	 * @param roomID
	 * @return
	 */
	public List<BattleStatisticBean> getRoomStatisticBeanBy(int roomID) {
		List<BattleStatisticBean> tempBeans = new ArrayList<BattleStatisticBean>();
		// 从redis取出记录
		int size = RedisManager.getInstance().llen(RedisKeyConstants.ROOMID_TO_ROOM_INFO + roomID)
				.intValue();
		for (int i = 0; i < size; i++) {
			BattleStatisticBean bsBean = (BattleStatisticBean) RedisManager
					.lpop(RedisKeyConstants.ROOMID_TO_ROOM_INFO + roomID);
			tempBeans.add(bsBean);
		}
		return tempBeans;
	}
	
	private int unmarshal(List<BattleStatistic> battleStatistics, 
		List<BattleStatisticBean> beans, Map<BattleStatistic, BattleStatisticBean> data2Bean)
	{
		battleStatistics.clear();
		data2Bean.clear();
		if(beans.size() == 0)
		{
			return -1;
		}
		try
		{
			for (BattleStatisticBean bean : beans)
			{
				BattleStatistic battleStatistic = new BattleStatistic(
					bean.getUID(), bean.getRoomID(), bean.getPlayerID1(), bean.getPlayerID2(), 
					bean.getPlayerID3(), bean.getPlayerID4(), 
					bean.getOwnerID(), bean.getRoomType(), bean.getIsFinish() == 0 ? false :true,
					getRoomStatistics(bean), getRoundRecords(bean));
				battleStatistics.add(battleStatistic);
				data2Bean.put(battleStatistic, bean);
			}
			return 0;
		}
		catch(Exception e)
		{
			logger.error("ummarshal exception");
		}
		return -1;
	}
	
	private  RoomStatistic getRoomStatistics(BattleStatisticBean bean)
	{
		try
		{
			byte[] roomStatisticsBuffer = bean.getRoom_statistic();
			RoomStatisticsPROTO proto = RoomStatisticsPROTO.parseFrom(roomStatisticsBuffer);
			return new RoomStatistic(proto);
			
		}
		catch(Exception e)
		{
			logger.error("ummarshal room statistics exception, roomID={}", bean.getRoomID());
		}
		return null;
	}

	private RoundDatas getRoundDataProto(List<RoundRecord> roundRecords)
	{
		
		RoundDatas builder = new RoundDatas();
		
		for (RoundRecord roundRecord : roundRecords)
		{
			builder.addRoundDatas(roundRecord.toRoundDataPROTO());
		}
		return builder;
	}
	
	private List<Integer> get4Players(RoomStatistic roomStatistic)
	{
		List<PlayerPointInfo> playerPointInfos = roomStatistic.getPlayerPointInfo();
		List<Integer> playerIDs = new ArrayList<>();
		for (PlayerPointInfo playerPointInfo : playerPointInfos)
		{
			playerIDs.add(playerPointInfo.getPlayerID());
		}
		
		Collections.sort(playerIDs);
		return playerIDs;
	}
	
	private List<RoundRecord> getRoundRecords(BattleStatisticBean bean)
	{
/*		try
		{
			List<RoundRecord> roundRecords = new ArrayList<>();
			byte[] roundDatasBuffer = bean.getRound_datas();
			RoundDatas roundDatas = RoundDatas.parseFrom(roundDatasBuffer);
			for (RoundDataPROTO proto : roundDatas.getRoundDatas())
			{
				RoundRecord roundRecord = new RoundRecord(proto);
				roundRecords.add(roundRecord);
			}
			return roundRecords;
		}
		catch(Exception e)
		{
			logger.error("ummarshal round data exception, roomID={}", bean.getRoomID());
		}*/
		return new ArrayList<>();
	}
	
	private boolean isSamePlayers(List<Integer> list1, List<Integer> list2)
	{
		return list1.containsAll(list2);
	}


}
