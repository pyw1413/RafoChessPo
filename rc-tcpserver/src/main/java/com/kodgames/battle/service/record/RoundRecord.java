package com.kodgames.battle.service.record;

import com.kodgames.battle.entity.battle.BattleData;
import com.kodgames.battle.entity.record.PlayerPointInfoPROTO;
import com.kodgames.battle.entity.record.RoundDataPROTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 一局需要存储的信息
 * 
 * @author  姓名
 * @version
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class RoundRecord implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static int count = 0;
	private int id;
	private long startTime;
	private List<PlayerPointInfo> playerPointInfos = new ArrayList<>();
	// 由于MBattleData类并未提供toProto方法，所以该类无法正确知道如何序列化BattleData，所以直接存储BattleData消息
	private BattleData battleData;
	
	public RoundRecord(RoundDataPROTO proto)
	{
		this.fromProto(proto);
		id = proto.getId(); 
	}

	public RoundRecord(long startTime, List<PlayerPointInfo> playerPointInfos, BattleData battleData)
	{
		this.startTime = startTime;
		this.playerPointInfos = playerPointInfos;
		this.battleData = battleData;
		id = count++;
	}
	public RoundDataPROTO toRoundDataPROTO()
	{
		RoundDataPROTO builder = new RoundDataPROTO();
		builder.setId(id);
		builder.setStartTime(startTime);
		for (PlayerPointInfo playerPointInfo : playerPointInfos)
		{
			PlayerPointInfoPROTO playerInfoPROTO = playerPointInfo.toPlayerPointInfoPROTO();
			builder.addPlayerInfo(playerInfoPROTO);
		}
		
		builder.setBattleData(battleData);
		
		return builder;
	}
	
	private void fromProto(RoundDataPROTO proto)
	{
		this.startTime = proto.getStartTime();
		for (PlayerPointInfoPROTO playProto : proto.getPlayerInfo())
		{
			PlayerPointInfo playerPointInfo = new PlayerPointInfo(playProto);
			playerPointInfos.add(playerPointInfo);
		}
		 battleData = proto.getBattleData();
	}
	public int getId()
	{
		return id;
	}

	public long getStartTime()
	{
		return startTime;
	}
	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}
	public List<PlayerPointInfo> getPlayerPointInfos()
	{
		return playerPointInfos;
	}
	public void setPlayerPointInfos(List<PlayerPointInfo> playerPointInfos)
	{
		this.playerPointInfos = playerPointInfos;
	}
	public BattleData getBattleDatas()
	{
		return battleData;
	}
	public void setBattleDatas(BattleData battleData)
	{
		this.battleData = battleData;
	} 
}
