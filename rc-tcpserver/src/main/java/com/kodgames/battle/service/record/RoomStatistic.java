package com.kodgames.battle.service.record;

import com.kodgames.battle.entity.record.PlayerPointInfoPROTO;
import com.kodgames.battle.entity.record.RoomStatisticsPROTO;

import java.util.ArrayList;
import java.util.List;


/**
 * 记录一个房间创建的时间，以及其中四个玩家的总积分与昵称ID
 **/
public class RoomStatistic
{
	private long startTime;
	private int roomID;
	private List<PlayerPointInfo> playerPointInfos = new ArrayList<>();
	
	public RoomStatistic(RoomStatisticsPROTO proto)
	{
		this.fromProto(proto);
	}
	
	public RoomStatistic(long startTime, int roomID, List<PlayerPointInfo> playerPointInfos)
	{
		this.startTime = startTime;
		this.roomID = roomID;
		this.playerPointInfos = playerPointInfos;
	}
	
	public RoomStatisticsPROTO toRoomStatisticsPROTO()
	{
		RoomStatisticsPROTO builder = new RoomStatisticsPROTO();
		builder.setStartTime(startTime);
		builder.setRoomID(roomID);
		for (PlayerPointInfo playerPointInfo : playerPointInfos)
		{
			builder.addPlayerInfo(playerPointInfo.toPlayerPointInfoPROTO());
		}
		return builder;
	}
	
	private void fromProto(RoomStatisticsPROTO proto)
	{
		this.startTime = proto.getStartTime();
		this.setRoomID(proto.getRoomID());
		List<PlayerPointInfoPROTO> playerPointInfoPROTOs = proto.getPlayerInfo();
		for (PlayerPointInfoPROTO playerPointInfoPROTO : playerPointInfoPROTOs)
		{
			PlayerPointInfo playerPointInfo = new PlayerPointInfo(playerPointInfoPROTO);
			playerPointInfos.add(playerPointInfo);
		}
	}
	public long getStartTime()
	{
		return startTime;
	}
	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}
	public  List<PlayerPointInfo> getPlayerPointInfo()
	{
		return playerPointInfos;
	}
	public void setPlayerPointInfo(List<PlayerPointInfo> playerPointInfos)
	{
		this.playerPointInfos = playerPointInfos;
	}

	public int getRoomID()
	{
		return roomID;
	}

	public void setRoomID(int roomID)
	{
		this.roomID = roomID;
	}
}
