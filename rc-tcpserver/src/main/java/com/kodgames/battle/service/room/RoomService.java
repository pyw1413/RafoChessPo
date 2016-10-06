package com.kodgames.battle.service.room;

import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.kodgames.battle.common.constants.GlobalConstants;
import com.kodgames.battle.entity.room.*;
import com.kodgames.battle.service.server.KodgamesExtension;
import com.rafo.chess.exception.PersistException;
import com.rafo.chess.manager.RedisManager;
import com.rafo.chess.model.LoginRoom;
import com.rafo.chess.model.LoginUser;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.match.BoolMatch;
import com.smartfoxserver.v2.entities.match.MatchExpression;
import com.smartfoxserver.v2.entities.match.RoomProperties;
import com.smartfoxserver.v2.entities.match.StringMatch;
import com.smartfoxserver.v2.extensions.SFSExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.kodgames.battle.common.Player;
import com.kodgames.battle.common.Room;
import com.kodgames.battle.common.VoteResultType;
import com.kodgames.battle.common.Player.PlayState;
import com.kodgames.battle.service.record.RecordService;
import com.kodgames.battle.util.DateTimeUtil;


public class RoomService
{
	private static final long serialVersionUID = -95924175253120279L;
	private final Logger logger = LoggerFactory.getLogger(RoomService.class);
	private Room room ;
	private ConcurrentHashMap<String, Integer> accountRooms = new ConcurrentHashMap<String, Integer>();
	private ConcurrentHashMap<Integer, Long> room2VoteStartTimes = new ConcurrentHashMap<>();
	private static Long ONE_MINS = 60 * 1000L;
	private SFSExtension roomExt;

	public SFSExtension getRoomExt() {
		return roomExt;
	}

	public void setRoomExt(SFSExtension roomExt) {
		this.roomExt = roomExt;
	}


	public Room getRoom()
	{
		return room;
	}

	public ConcurrentHashMap<String, Integer> getAccountRooms() {
		return accountRooms;
	}

	public BGRoomCreateRES createRoom(GBRoomCreateREQ message)
	{
		//TODO: IF ALREADY CREATED
		Room room = new Room(message);
		this.room = room;
		accountRooms.put(message.getAccountID(), message.getRoomID());

		room.addPlayer( message.getAccountID(), message.getID(), message.getIp(), "", message.getServerID()); // 创建房间时，将房主加入，即使其再次申请进入房间，由Enter控制
		
		BGRoomCreateRES builder = new BGRoomCreateRES();
		builder.setAccountID(message.getAccountID());
		builder.setRoomID(message.getRoomID());
		builder.setCount(message.getCount());
		builder.setType(message.getType());
		builder.setResult(GlobalConstants.ROOM_CREATE_SUCCESS);
		builder.setPlayerID(message.getID());

	    return builder;
	}
	
	public BGRoomEnterRES enterFailed(GBRoomEnterREQ message, int errorCode)
	{
		BGRoomEnterRES res = new BGRoomEnterRES();
		res.setResult(errorCode);
		res.setRoomID(message.getRoomID());
		res.setApplierAccountID(message.getAccountID());
		res.setApplierID(message.getID());
		return res;
	}
	
	public BGRoomEnterRES enterRoom(GBRoomEnterREQ message)
	{
		if (room == null)
		{
			logger.error("EnterRoom error, room not exist, roomID={}", message.getRoomID());
			return enterFailed( message, GlobalConstants.ROOM_ENTER_FAILED_NUMBER_ERROR);
		}
		if (accountRooms.get(message.getAccountID()) != null && accountRooms.get(message.getAccountID()) != message.getRoomID())
		{
			logger.error("EnterRoom error, player already in room, account={}, roomID={}", message.getAccountID(), message.getRoomID());
			return enterFailed(message, GlobalConstants.ROOM_ENTER_FAILED_HAS_IN_OTHER_ROOM);
		}
		if (!room.addPlayer(message.getAccountID(), message.getID(), message.getIp(), message.getSex(), message.getServerID()))
		{
			logger.error("EnterRoom error, room has enougth players, playerCount={}, accountID={}", 
				room.getPlayers().size(), message.getAccountID());
			return enterFailed( message, GlobalConstants.ROOM_ENTER_FAILED_ROOM_FULL);
		}
		
		Player applier = room.getPlayer(message.getAccountID());
		applier.setHead(message.getHead());
		applier.setName(message.getName());
		applier.setSex(message.getSex());
		accountRooms.put(message.getAccountID(), message.getRoomID());
		ConcurrentLinkedHashMap<String, Player> players = room.getPlayers();
		
		// 检测相同IP
		if(!room.isInBattle())
		{
			List<String> same_ips = new ArrayList<String>();
			for(Player player : players.values())
			{
				if(player.getAccountID().equals(applier.getAccountID()) || player.isOffline())
					continue;
				
				if(player.getIp().equals(applier.getIp()))
					same_ips.add(player.getAccountID());
			}
			
			if(same_ips.size() > 0 && same_ips.size() < 3)
			{
				if(applier.needResetSameIps(same_ips))
				{
					applier.setSameIp(same_ips);
					
					for(Player player : players.values())
					{
//						player.setPlayState(PlayState.Idle);
						
						if(same_ips.contains(player.getAccountID()))
							player.resetSameIp(applier.getIp(), applier.getAccountID());
					}
				}
			}
		}
		
		BGRoomEnterRES res = new BGRoomEnterRES();
		for (String accountID : players.keySet()) // 将房间内所有玩家的信息填充到协议中
		{
			Player player = players.get(accountID);
			PlayerInfoSSPROTO playerSSInfo = new PlayerInfoSSPROTO();
			playerSSInfo.setAccountID(accountID);
			playerSSInfo.setChair(player.getChair());
			playerSSInfo.setName(player.getName());
			playerSSInfo.setHead(player.getHead());
			playerSSInfo.setSex(player.getSex());
			playerSSInfo.setRoom(room.getRoomID()); 
			playerSSInfo.setPlayerID(player.getID());
			playerSSInfo.setIp(player.getIp());
			playerSSInfo.setStatus(player.getPlayState().ordinal());
			playerSSInfo.setOffline(player.isOffline());
			res.addPlayerInfo(playerSSInfo);
		}
		res.setApplierAccountID(message.getAccountID());
		res.setApplierID(message.getID());
		res.setRoomID(room.getRoomID());
		res.setRoomType(room.getRoomType());
		res.setCurrentBattleCount(room.getBattleTime()+1);
		res.setResult(GlobalConstants.ROOM_ENTER_SUCCESS);
		
		// 检查房间状态，通知进入房间的人房间状态
		if(room.hasVoteApply())
		{
			BGVoteDestroyRES builder = new BGVoteDestroyRES();
			setVoteBuilder(builder, room, GlobalConstants.WC_VOTE_DESTROY_VOTING);
			builder.setAccountID(message.getAccountID());
			res.setBgVoteDestroyRES(builder);
		}
		
		return res;
	}
	
	public List<BGRoomDestoryRES> destoryRoom( GBRoomDestoryREQ message)
	{
		List<BGRoomDestoryRES> results = new ArrayList<>();
		if(room == null)
		{
			logger.error("destoryRoom error, room number error, roomID={}, accountID={}", message.getRoomID(), message.getAccountID());
			results.add(destroyRoomError( message, GlobalConstants.ROOM_DESTORY_FAILED_ROOM_NUM_ERROR));
			return results;
		}
		if(accountRooms.get(message.getAccountID()) == null)
		{
			logger.error("destoryRoom error, player is not in room, roomID={}, accountID={}", message.getRoomID(), message.getAccountID());
			results.add(destroyRoomError( message, GlobalConstants.ROOM_DESTORY_FAILED_NOT_IN_ROOM));
			return results;
		}
		if (!room.getOwner().equals(message.getAccountID()))
		{
			logger.error("destoryRoom error, player is not owner, roomID={}, accountID={}", message.getRoomID(), message.getAccountID());
			results.add(destroyRoomError( message, GlobalConstants.ROOM_DESTORY_FAILED_NOT_OWNER));
			return results;
		}
		if (room.isInBattle())
		{
			logger.error("destoryRoom error, room is battling, roomID={}, accountID={}", message.getRoomID(), message.getAccountID());
			results.add(destroyRoomError( message, GlobalConstants.ROOM_DESTORY_FAILED_IN_BATTLE));
			return results;
		}
		
		destroyRoomData(message.getRoomID());
		
		ConcurrentLinkedHashMap<String, Player> players = room.getPlayers();

		for (String accountID : players.keySet())
		{
			BGRoomDestoryRES res = new BGRoomDestoryRES();
			res.setResult(GlobalConstants.ROOM_DESTORY_SUCCESS);
			res.setAccountID(accountID);
			res.setRoomID(room.getRoomID());
			results.add(res);
        }
        return results;
	}
	
	private BGRoomDestoryRES destroyRoomError( GBRoomDestoryREQ message, int errorCode)
	{
		BGRoomDestoryRES res = new BGRoomDestoryRES();
		res.setResult(errorCode);
		res.setAccountID(message.getAccountID());
		res.setRoomID(message.getRoomID());
		return res;
	}
	
	/*
	 * 服务器主动销毁房间，只有在房间总结算的时候会调用
	 */
	public BGAutoDestroySYN autoDestroyRoom(int roomID)
	{
		BGAutoDestroySYN builder = new BGAutoDestroySYN();

		if (room.getStatus() != 0)
		{
			logger.error("autoDestroyRoom room is not normal, roomID={}", roomID);
			builder.setResult(GlobalConstants.BG_AUTO_DESTROY_FAILED_NO_IN_ROOM);
			return builder;
		}
		
		Set<String> accountIDs = new HashSet<>(room.getPlayers().keySet());
		destroyRoomData(roomID);
		
		builder.setOwnerAccountID(room.getOwner());
		builder.setRoomID(roomID);
		builder.setRoomCount(room.getRoomType());
		builder.setRoomType(room.getType());
		builder.setIp(room.getPlayer(room.getOwner()).getIp());

		for (String accountID : accountIDs)
		{
			builder.addAccountIDs(accountID);
        }
		builder.setResult(GlobalConstants.BG_AUTO_DESTROY_SUCCESS);

		try {
			SFSExtension extension = this.getRoomExt();
			com.smartfoxserver.v2.entities.Room sfsRoom = extension.getParentRoom();
			RoomHelper.destroyRedisRoom(sfsRoom);
			RoomHelper.clearUserRoomInfo(sfsRoom);
			sfsRoom.destroy();
		} catch (PersistException e) {
			e.printStackTrace();
		}


		
		return builder;

	}
	
	//GM工具强行清除房间
	public List<BGForceRoomDestoryRES> forceRoomDestory(int roomId,int callback)
	{
		if(room == null)
		{
			logger.error("destoryRoom error, room number error, roomID={}",roomId);
			return null;
		}
		//由于属于特殊使用，所以不管房间状态如何，直接删除房间
		destroyRoomData(roomId);
		
		ConcurrentLinkedHashMap<String, Player> players = room.getPlayers();
		List<BGForceRoomDestoryRES> results = new ArrayList<>();
		for (String accountID : players.keySet())
		{
			BGForceRoomDestoryRES res = new BGForceRoomDestoryRES();
			res.setResult(GlobalConstants.ROOM_DESTORY_SUCCESS);
			res.setAccountID(accountID);
			res.setRoomID(roomId);
			results.add(res);
        }

        return results;
	}
	
	public List<BGVoteDestroyRES> voteDestoryRoom(GBVoteDestroyREQ message)
	{
		BGVoteDestroyRES builder = new BGVoteDestroyRES();
		
		String accountID = message.getAccountID();
		int roomID = message.getRoomID();
		builder.setRoomID(roomID);
		List<BGVoteDestroyRES> results = new ArrayList<>();
		
		// 处理投票的逻辑
		if(!accountRooms.containsKey(message.getAccountID())) // 玩家不在房间中
		{
			logger.error("player not in room, player " + accountID);
			builder.setAccountID(accountID);
			builder.setResult(GlobalConstants.WC_VOTE_DESTROY_FAILED_NOT_IN_ROOM);
			results.add(builder);
			return results;
		}

		if(room == null) // 玩家不在房间中
		{
			logger.error("player not in room, player " + accountID);
			builder.setAccountID(accountID);
			builder.setResult(GlobalConstants.WC_VOTE_DESTROY_FAILED_NOT_IN_ROOM);
			results.add(builder);
			return results;
		}
		
		if(room.hasVoted(accountID)) // 玩家已经投过票
		{
			logger.error("Player has already voted " + accountID);
			builder.setAccountID(accountID);
			builder = setVoteBuilder(builder, room, GlobalConstants.WC_VOTE_DESTROY_FAILED_HAS_VOTED);
			results.add(builder);
			return results;
		}
		
		VoteResultType voteResult = VoteResultType.valueOf(message.getVoteResult());
		if(voteResult == VoteResultType.START && !room.isFirstApplyDestroy()) // 该房间已经存在一次申请
		{
			logger.error("Apply has already existed " + accountID);
			builder.setAccountID(accountID);
			builder = setVoteBuilder(builder, room, GlobalConstants.WC_VOTE_DESTROY_FAILED_EXISTED);
			results.add(builder);
			return results;
		}
		if(voteResult == VoteResultType.START)
		{
			room2VoteStartTimes.put(roomID, System.currentTimeMillis()); // 用于记录申请解散
			builder.setRemainTime(ONE_MINS + "");
		}
		// 这种条件，表示两个人同时点击，拒绝的比同意的先到达。已经清空了map。
		if(voteResult != VoteResultType.START && room.isFirstApplyDestroy()) 
		{
			return null;
		}

		room.addVoteResult(accountID, voteResult); // 添加投票记录之后，需要进行结果检查
		
		Set<String> accountIDs = new HashSet<>(room.getPlayers().keySet());
		
		if (voteResult == VoteResultType.REFUSE) // 有一人拒绝，则取消
		{
			builder = setVoteBuilder(builder, room, GlobalConstants.WC_VOTE_DESTROY_FAILED_REFUSED);
			room.canceDestroy();
			room2VoteStartTimes.remove(roomID);
		}

		else if (room.isCouldDestroy()) // 可以解散房间，之后房间会被垃圾回收，所以不用清空房间的投票记录
		{
			builder = setVoteBuilder(builder, room, GlobalConstants.WC_VOTE_DESTROY_SUCCESS);
			RecordService recordService = new RecordService();
			recordService.changeRoomByVote(room);
			
			destroyRoomData(roomID);
			room2VoteStartTimes.remove(roomID);
			
			// 通知Game，写解散房间Log
			sendVoteDestroyOKLog(roomID);
		}

		else
		{
			builder = setVoteBuilder(builder, room, GlobalConstants.WC_VOTE_DESTROY_VOTING);
		}
		
		for (String reciverAccountID : accountIDs){
			builder.setAccountID(reciverAccountID);
			try {
				results.add((BGVoteDestroyRES)builder.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		return results;
	}
	
	/*
	 * 定时检查投票解散房间，对于超时的，自动投票为同意
	 */
	public List<BGVoteDestroyRES> checkVoteTime(Long nowTime)
	{
		Set<Integer> roomIDS = room2VoteStartTimes.keySet();
		List<BGVoteDestroyRES> results = new ArrayList<>();
		for (Integer roomID : roomIDS)
		{
			if(nowTime - room2VoteStartTimes.get(roomID) >= ONE_MINS)
			{
				// 超时，直接销毁房间，然后发送给各个人。
				room2VoteStartTimes.remove(roomID);
				if(room == null)
					continue;
				
				Set<String> accountIDs = new HashSet<>(room.getPlayers().keySet());
				destroyRoomData(roomID);				
				
				sendVoteDestroyOKLog(roomID);

				for (String reciverAccountID : accountIDs){
					BGVoteDestroyRES builder = new BGVoteDestroyRES();
					builder = setVoteBuilder(builder, room, GlobalConstants.WC_VOTE_DESTROY_SUCCESS);
					builder.setAccountID(reciverAccountID);
					results.add(builder);
				}	
			}
		}
		return results;
	}
	
	private BGVoteDestroyOKLogSYN sendVoteDestroyOKLog(int roomID)
	{
		BGVoteDestroyOKLogSYN builder = new BGVoteDestroyOKLogSYN();
		builder.setRoomID(roomID);
		return builder;
	}
	
	public List<BGRoomQuitRES> roomQuit(GBRoomQuitREQ req)
	{
		List<BGRoomQuitRES> results = new ArrayList<>();
		if(accountRooms.get(req.getAccountID()) == null)
		{
			logger.error("roomQuit error, not in room, accountID={}, roomID={}", req.getAccountID(), req.getRoomID());
			results.add(quitRoomError(req,GlobalConstants.ROOM_QUIT_FAILED_NOT_IN_ROOM));
			return results;
		}

		if(room == null)
		{
			logger.error("roomQuit error, room ID is error,  accountID={}, roomID={}", req.getAccountID(), req.getRoomID());
			results.add(quitRoomError(req, GlobalConstants.ROOM_QUIT_FAILED_ERROR_ROOM));
			return results;
		}
		if(room.getOwner().equals(req.getAccountID()))
		{
			logger.error("roomQuit error, owner can not exit! accountID={}, roomID={}", req.getAccountID(), req.getRoomID());
			results.add(quitRoomError(req, GlobalConstants.ROOM_QUIT_FAILED_IS_OWNER));
			return results;
		}
		if(room.isInBattle())
		{
			logger.error("roomQuit error, player in battle! accountID={}, roomID={}", req.getAccountID(), req.getRoomID());
			results.add(quitRoomError(req, GlobalConstants.ROOM_QUIT_FAILED_IN_BATTLE));
			return results;
		}
		
		// 处理退出房间问题
		Set<String> accountIDs = new HashSet<String>(room.getPlayers().keySet());
		Player quiter = room.getPlayer(req.getAccountID());
		accountRooms.remove(req.getAccountID());
		room.removePlayer(req.getAccountID());
		
		BGRoomQuitRES builder = new BGRoomQuitRES();
		
		builder.setResult(GlobalConstants.ROOM_QUIT_SUCCESS);
		builder.setRoomID(req.getRoomID());
		builder.setQuitterID(quiter.getID());
		builder.setQuitterAccountID(quiter.getAccountID());

		for (String  accountID : accountIDs)
		{
			builder.setAccountID(accountID);
			try {
				results.add((BGRoomQuitRES)builder.clone());
			} catch (CloneNotSupportedException e) {
			}
		}
		return results;
	}
	
	private BGRoomQuitRES quitRoomError(GBRoomQuitREQ req, int errorCode)
	{
		BGRoomQuitRES builder = new BGRoomQuitRES();
		builder.setResult(errorCode);
		builder.setAccountID(req.getAccountID());
		builder.setRoomID(req.getRoomID());
		builder.setQuitterID(-1);
		builder.setQuitterAccountID(req.getAccountID());
		return builder;
	}
	
	/*
	 * 销毁房间后，清除房间内的玩家以及当前房间
	 */
	private void destroyRoomData(int roomID)
	{
/*		Room room = rooms.get(roomID);
		
		Map<String, Player> accountID2Player = room.getPlayers();
		for (String accountID: accountID2Player.keySet())
		{
			accountRooms.remove(accountID);
		}
		rooms.remove(roomID);
		
		BattleService battleService = new BattleService();
		battleService.destroyRoom(roomID);*/
	}
	
	/*
	 * 设置投票返回信息的值
	 */
	private BGVoteDestroyRES setVoteBuilder(BGVoteDestroyRES builder, Room room, int result)
	{

		Map<String, VoteResultType> voteResults = room.getVoteRecord();
		for (Map.Entry<String, VoteResultType> voteRecord : voteResults.entrySet())
		{
			Player player = room.getPlayer(voteRecord.getKey());
			VoteInfoPROTO voteInfo = new VoteInfoPROTO();
			voteInfo.setPlayerID(player.getID());
			voteInfo.setVoteResult(voteRecord.getValue().value());
			builder.addPlayerVoteInfo(voteInfo);
		}
		
		Long remainTime;
		Long startTime = room2VoteStartTimes.get(room.getRoomID());
		if(startTime == null)
		{
			remainTime = 0L;
		}
		else
		{
			remainTime = ONE_MINS - DateTimeUtil.getDateDiff(new Date().getTime(), startTime);
			if(remainTime < 0)
				remainTime = 0L;
		}
		
		builder.setRemainTime(remainTime.toString());
		builder.setRoomID(room.getRoomID());
		builder.setResult(result);
		
		return builder;
	}
	
	/*
	 * index 0 slientRoomCount
	 * index 1 positiveRoomCount
	 */
	public void getRoomStates(List<Integer> roomState)
	{
		int slientCount = 0;
		int postiveCount = 0;


		if(room.isSlient())
			++slientCount;
		if(room.isPostive())
			++postiveCount;

		roomState.add(0, slientCount);
		roomState.add(1, postiveCount);
	}

}