package com.kodgames.battle.common;

import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.kodgames.battle.common.constants.GlobalConstants;
import com.kodgames.battle.entity.room.GBRoomCreateREQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Room
{
	Logger logger = LoggerFactory.getLogger(Room.class);
	private ConcurrentLinkedHashMap<String, Player> players =
		new ConcurrentLinkedHashMap.Builder<String, Player>().maximumWeightedCapacity(4).build();
	private ConcurrentHashMap<String, VoteResultType> voteDestroyResult = new ConcurrentHashMap<String, VoteResultType>();  // <accountID, VoteResult>
	private int roomID;
	private String ownerAccountID;
	private int owerID;
	private int roundTotal; //总局数
	private int battleTime; // 已经战斗局数
	private int playType; // 玩法类型
	private int status;
	private boolean hasSubCard;
	private long createTime;
	private int roomType; // 房间的类型，0或者1
	private static final int ROOMFULLPLAYER = 4;
	private boolean isInBattle; // 是否处于打牌中
	private TreeSet<Integer> unusedChair = new TreeSet<Integer>();
	private Lock lock = new ReentrantLock();
	
	public Room(GBRoomCreateREQ req)
	{
		hasSubCard = false;
		this.owerID = req.getID();
		this.roomID = req.getRoomID();
		this.ownerAccountID = req.getAccountID();
		this.roundTotal = GlobalConstants.COUNT2ROUNDMAP.get(req.getCount());
		this.setRoomType(req.getCount());
		this.playType = req.getType();
		createTime = new Date().getTime();
		unusedChair.add(0);
		unusedChair.add(1);
		unusedChair.add(2);
		unusedChair.add(3);
	}
	
	public void setStatus(int status)
	{
		this.status = status;
	}
	
	public int getStatus()
	{
		return status;
	}
	
	public int getOwerID()
	{
		return owerID;
	}

	public void setOwerID(int owerID)
	{
		this.owerID = owerID;
	}

	public int getRoomID()
	{
		return roomID;
	}
	
	public String getOwner()
	{
		return ownerAccountID;
	}
	
	public int getBattleCount()
	{
		return roundTotal;
	}
	
	public int getBattleTime()
	{
		return battleTime;
	}
	
	public void addBattleTime()
	{
		battleTime++;
	}
	
	public int getType()
	{
		return playType;
	}
	
	public boolean isFullNumber()
	{
		return players.size() == 4;
	}
	
	public boolean isFinishBattle()
	{
		return battleTime > roundTotal;
	}
	
	public Lock getRoomLock()
	{
		return lock;
	}
	/**
	 * 
	 * 将玩家添加至房间
	 * 
	 * @param outAccountID 玩家对应的AccountID
	 * @param playerID 客户端需要的，标识玩家的ID
	 * @param serverID 玩家持有ServeID
	 * @return
	 * @see
	 */
	public Boolean addPlayer( String outAccountID, int playerID, String ip, String sex, int serverID)
	{
		String accountID = outAccountID;
		Player player = players.get(accountID);
		if (players.size() >= 4 && player == null) // 房间满且该玩家并不在房间之中
			return false;
		if (player == null)
		{
			// 需要创建玩家
			int chair;
			try
			{
				chair = unusedChair.first();
			}
			catch (NoSuchElementException e) 
			{
				logger.error("addPlayer error, no chair could be used");
				return false;
			}
			
			player = new Player(accountID, playerID, accountID, "head", chair, ip, sex, serverID);
			unusedChair.remove(chair); // 座位号使用后，移除
		}
		else
		{
			player.setSex(sex);
			player.setIp(ip);
			player.setServerID(serverID);
			player.setOffline(false);
		}
		
		players.put(accountID, player);
		return true;
	}
	
	public Player getPlayer(String accountID)
	{
		return players.get(accountID);
	}
	
	public Player getPlayer(int playerId)
	{
		for(Map.Entry<String, Player> entry : players.entrySet())
		{
			if(entry.getValue().getID() == playerId)
				return entry.getValue();
		}
		return null;
	}
	public void removePlayer(String accountID)
	{
		unusedChair.add(players.get(accountID).getChair());
		players.remove(accountID);
	}
	
	public ConcurrentLinkedHashMap<String, Player> getPlayers()
	{
		return players;
	}
	
	/*
	 * 是否第一次进行投票，如果是，表示玩家是发起者
	 */
	public boolean isFirstApplyDestroy()
	{
		return voteDestroyResult.size() == 0 ? true : false;
	}
	
	public boolean hasVoteApply()
	{
		return voteDestroyResult.size() == 0 ? false : true;
	}

	/*
	 * 如果里面存储的结果达到了房间人数，并且没有拒绝，则可以解散
	 */
	public boolean isCouldDestroy()
	{
		if(voteDestroyResult.size() != ROOMFULLPLAYER) 
			return false;
		for (Map.Entry<String, VoteResultType> player2Vote : voteDestroyResult.entrySet())
		{
			if(player2Vote.getValue() == VoteResultType.REFUSE)
				return false;
		}
		
		return true;
	}
	
	public void addVoteResult(String accountID, VoteResultType voteResult)
	{		
		voteDestroyResult.put(accountID, voteResult);
	}
	
	public boolean hasRefuse()
	{
		for (Map.Entry<String, VoteResultType> player2Vote : voteDestroyResult.entrySet())
		{
			if(player2Vote.getValue() == VoteResultType.REFUSE)
				return true;
		}
		return false;
	}
	
	/*
	 * 是否已经投过票
	 */
	public boolean hasVoted(String accountID)
	{
		if(voteDestroyResult.containsKey(accountID))
			return true;
		return false;
	}
	
	public void canceDestroy()
	{
		voteDestroyResult.clear();
	}
	
	public final Map<String, VoteResultType> getVoteRecord()
	{
		return voteDestroyResult;
	}

	public boolean isSlient() // 所有人是否离线
	{
		synchronized (players)
		{
			int offlinePlayer = 0;
			for (Player player : players.values())
			{
				if(player.isOffline())
					++offlinePlayer;
			}
			if(offlinePlayer == players.size())
				return true;
		}
		return false;
	}
	
	public boolean isPostive() // 是否扣过房卡
	{
		return this.hasSubCard;
	}
	
	public long getCreateTime()
	{
		return createTime;
	}

	public int getRoomType()
	{
		return roomType;
	}

	public void setRoomType(int roomType)
	{
		this.roomType = roomType;
	}

	public boolean isInBattle()
	{
		return isInBattle;
	}

	public void setInBattle(boolean isInBattle)
	{
		this.isInBattle = isInBattle;
	}

	public boolean isHasSubCard()
	{
		return hasSubCard;
	}

	public void setHasSubCard(boolean hasSubCard)
	{
		this.hasSubCard = hasSubCard;
	}
}