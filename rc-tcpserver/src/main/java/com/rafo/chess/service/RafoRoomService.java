package com.rafo.chess.service;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.kodgames.battle.common.Player;
import com.kodgames.battle.common.Room;
import com.kodgames.battle.common.VoteResultType;
import com.kodgames.battle.common.constants.GlobalConstants;
import com.kodgames.battle.entity.room.*;
import com.kodgames.battle.service.room.RoomHelper;
import com.kodgames.battle.util.DateTimeUtil;
import com.rafo.chess.exception.PersistException;
import com.rafo.chess.model.GamePlayer;
import com.rafo.chess.model.GameRoomSettings;
import com.rafo.chess.model.RafoRoom;
import com.smartfoxserver.v2.extensions.SFSExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class RafoRoomService
{
	private static final long serialVersionUID = -95924175253120279L;
	private final Logger logger = LoggerFactory.getLogger(RafoRoomService.class);
	private RafoRoom room ;
	private ConcurrentHashMap<String, Integer> accountRooms = new ConcurrentHashMap<String, Integer>();
	private ConcurrentHashMap<Integer, Long> room2VoteStartTimes = new ConcurrentHashMap<>();
	private AtomicInteger chairs = new AtomicInteger(1); //座位号生成器，从1开始
	private static Long ONE_MINS = 60 * 1000L;
	private SFSExtension roomExt;

	public SFSExtension getRoomExt() {
		return roomExt;
	}

	public RafoRoomService(SFSExtension roomExt){
		this.roomExt = roomExt;
	}


	public RafoRoom getRoom()
	{
		return room;
	}

	public ConcurrentHashMap<String, Integer> getAccountRooms() {
		return accountRooms;
	}


	public void createRoom(GameRoomSettings gameSetting) {
		this.room = new RafoRoom(gameSetting);
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

	//1. enter room
	public BGRoomEnterRES enterRoom(String name, int playerID, String ip, String sex, String head){

		if(this.room.getRoomSize()  == room.getPlayerMapping().size()){
			return null;
		}
		boolean isBanker = room.getPlayers().size() == 0;

		int chair = chairs.get();
		if(chair > room.getRoomSize()){
			//TODO: XX
		}else{
			chairs.incrementAndGet();
		}

		GamePlayer player = room.getPlayerMapping().get(playerID);

		if(player == null) {
			player = new GamePlayer();
			player.setUid(playerID);
			player.setIp(ip);
			player.setSeatNo(chair);
			player.setGender(Integer.parseInt(sex));
			player.setNickName(name);
			player.setPic(head);
			player.setScore(0);
			player.setWuid(String.valueOf(playerID));
			player.setRoomId(String.valueOf(room.getRoomId()));
			player.setBanker(isBanker);
			room.addPlayer(player);
		}

		BGRoomEnterRES res = new BGRoomEnterRES();
		for (GamePlayer p : room.getPlayers()) // 将房间内所有玩家的信息填充到协议中
		{
			PlayerInfoSSPROTO playerSSInfo = new PlayerInfoSSPROTO();
			playerSSInfo.setAccountID(String.valueOf(p.getUid()));
			playerSSInfo.setChair(p.getSeatNo() -1);
			playerSSInfo.setName(p.getNickName());
			playerSSInfo.setHead(p.getPic());
			playerSSInfo.setSex(String.valueOf(p.getGender()));
			playerSSInfo.setRoom(room.getRoomId());
			playerSSInfo.setPlayerID(p.getUid());
			playerSSInfo.setIp(p.getIp());
			playerSSInfo.setStatus(p.getPlayState().ordinal());
			playerSSInfo.setOffline(p.isOffline());
			res.addPlayerInfo(playerSSInfo);
		}
		res.setApplierAccountID(String.valueOf(player.getUid()));
		res.setApplierID(player.getUid());
		res.setRoomID(room.getRoomId());
		res.setRoomType(room.getGameType());
		res.setCurrentBattleCount(room.getCurrRounds());
		res.setResult(GlobalConstants.ROOM_ENTER_SUCCESS);

		// 检查房间状态，通知进入房间的人房间状态
        if(room.hasVoteApply())
        {
            BGVoteDestroyRES builder = new BGVoteDestroyRES();
            setVoteBuilder(builder, room, GlobalConstants.WC_VOTE_DESTROY_VOTING);
            builder.setAccountID(String.valueOf(playerID));
            res.setBgVoteDestroyRES(builder);
        }

		return res;
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
		
		Set<Integer> accountIDs = new HashSet<>(room.getPlayerMapping().keySet());

		builder.setOwnerAccountID(String.valueOf(room.getOwnerId()));
		builder.setRoomID(roomID);
		builder.setRoomCount(room.getPlayType());
		builder.setRoomType(room.getGameType());
		builder.setIp(room.getPlayerMapping().get(room.getOwnerId()).getIp());

		for (Integer accountID : accountIDs)
		{
			builder.addAccountIDs(String.valueOf(accountID));
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
		
		Set<Integer> accountIDs = new HashSet<>(room.getPlayerMapping().keySet());
		
		if (voteResult == VoteResultType.REFUSE) // 有一人拒绝，则取消
		{
			builder = setVoteBuilder(builder, room, GlobalConstants.WC_VOTE_DESTROY_FAILED_REFUSED);
			room.canceDestroy();
			room2VoteStartTimes.remove(roomID);
		}

		else if (room.isCouldDestroy()) // 可以解散房间，之后房间会被垃圾回收，所以不用清空房间的投票记录
		{
			builder = setVoteBuilder(builder, room, GlobalConstants.WC_VOTE_DESTROY_SUCCESS);
			room2VoteStartTimes.remove(roomID);
			
			// 通知Game，写解散房间Log
			sendVoteDestroyOKLog(roomID);
		}

		else
		{
			builder = setVoteBuilder(builder, room, GlobalConstants.WC_VOTE_DESTROY_VOTING);
		}
		
		for (Integer reciverAccountID : accountIDs){
			builder.setAccountID(String.valueOf(reciverAccountID));
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
				
				Set<Integer> accountIDs = new HashSet<>(room.getPlayerMapping().keySet());

				sendVoteDestroyOKLog(roomID);

				for (Integer reciverAccountID : accountIDs){
					BGVoteDestroyRES builder = new BGVoteDestroyRES();
					builder = setVoteBuilder(builder, room, GlobalConstants.WC_VOTE_DESTROY_SUCCESS);
					builder.setAccountID(String.valueOf(reciverAccountID));
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
		Integer playerId = Integer.parseInt(req.getAccountID());
		List<BGRoomQuitRES> results = new ArrayList<>();
		if(room.getPlayerMapping().get(playerId) == null)
		{
			logger.error("roomQuit error, not in room, accountID={}, roomID={}", req.getAccountID(), req.getRoomID());
			results.add(quitRoomError(req,GlobalConstants.ROOM_QUIT_FAILED_NOT_IN_ROOM));
			return results;
		}

		if(room.getOwnerId() ==  playerId)
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
		Set<Integer> accountIDs = new HashSet<Integer>(room.getPlayerMapping().keySet());
		GamePlayer player = room.getPlayerMapping().get(playerId);
		room.removePlayer(playerId);

		BGRoomQuitRES builder = new BGRoomQuitRES();

		builder.setResult(GlobalConstants.ROOM_QUIT_SUCCESS);
		builder.setRoomID(req.getRoomID());
		builder.setQuitterID(player.getUid());
		builder.setQuitterAccountID(String.valueOf(player.getUid()));

		for (Integer  accountID : accountIDs)
		{
			builder.setAccountID(String.valueOf(accountID));
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
	 * 设置投票返回信息的值
	 */
	private BGVoteDestroyRES setVoteBuilder(BGVoteDestroyRES builder, RafoRoom room, int result)
	{

		Map<String, VoteResultType> voteResults = room.getVoteRecord();
		for (Map.Entry<String, VoteResultType> voteRecord : voteResults.entrySet())
		{
			GamePlayer player = room.getPlayerMapping().get(Integer.parseInt(voteRecord.getKey()));
			VoteInfoPROTO voteInfo = new VoteInfoPROTO();
			voteInfo.setPlayerID(player.getUid());
			voteInfo.setVoteResult(voteRecord.getValue().value());
			builder.addPlayerVoteInfo(voteInfo);
		}
		
		Long remainTime;
		Long startTime = room2VoteStartTimes.get(room.getRoomId());
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
		builder.setRoomID(room.getRoomId());
		builder.setResult(result);
		
		return builder;
	}

}