package com.kodgames.battle.service.record;

import java.io.Serializable;
import java.util.List;

public class BattleStatistic implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int UID;
	private int roomID;
	private int playerID1;
	private int playerID2;
	private int playerID3;
	private int playerID4;
	private int ownerID;
	private int roomType;
	private boolean isFinish;
	private RoomStatistic roomStatistics;
	private List<RoundRecord> roundRecords;
	
	public BattleStatistic(
		int UID, int roomID, int playerID1, int playerID2, int playerID3, int playerID4, 
		int ownerID, int roomType, boolean isFinish,
		RoomStatistic roomStatistics, List<RoundRecord> roundRecords)
	{
		this.UID = UID;
		this.roomID = roomID;
		this.playerID1 = playerID1;
		this.playerID2 = playerID2;
		this.playerID3 = playerID3;
		this.playerID4 = playerID4;
		this.setOwnerID(ownerID);
		this.setRoomType(roomType);
		this.setFinish(isFinish);
		this.roomStatistics = roomStatistics;
		this.roundRecords = roundRecords;
	}
	public int getUID()
	{
		return UID;
	}
	public int getRoomID()
	{
		return roomID;
	}
	public void setRoomID(int roomID)
	{
		this.roomID = roomID;
	}
	public int getPlayerID1()
	{
		return playerID1;
	}
	public void setPlayerID1(int playerID1)
	{
		this.playerID1 = playerID1;
	}
	public int getPlayerID2()
	{
		return playerID2;
	}
	public void setPlayerID2(int playerID2)
	{
		this.playerID2 = playerID2;
	}
	public int getPlayerID3()
	{
		return playerID3;
	}
	public void setPlayerID3(int playerID3)
	{
		this.playerID3 = playerID3;
	}
	public int getPlayerID4()
	{
		return playerID4;
	}
	public void setPlayerID4(int playerID4)
	{
		this.playerID4 = playerID4;
	}
	public RoomStatistic getRoomStatisticsData()
	{
		return roomStatistics;
	}
	public void setRoomStatisticsData(RoomStatistic roomStatisticsData)
	{
		this.roomStatistics = roomStatisticsData;
	}
	public List<RoundRecord> getRoundRecordDatas()
	{
		return roundRecords;
	}
	public void setRoundRecordDatas(List<RoundRecord> roundRecordDatas)
	{
		this.roundRecords = roundRecordDatas;
	}
	public int getOwnerID()
	{
		return ownerID;
	}
	public void setOwnerID(int ownerID)
	{
		this.ownerID = ownerID;
	}
	public int getRoomType()
	{
		return roomType;
	}
	public void setRoomType(int roomType)
	{
		this.roomType = roomType;
	}
	public boolean isFinish()
	{
		return isFinish;
	}
	public void setFinish(boolean isFinish)
	{
		this.isFinish = isFinish;
	}
}
