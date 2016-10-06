package com.kodgames.battle.service.record;


import com.kodgames.battle.entity.record.PlayerPointInfoPROTO;

/*
 * 记录玩家的用户ID，昵称与得分情况
 */
public class PlayerPointInfo
{
	private int playerID;
	private String nickName;
	private int chair;
	private int point;
	
	public PlayerPointInfo(PlayerPointInfoPROTO proto)
	{
		this.fromProto(proto);
	}
	public PlayerPointInfo(int playerID, String nickName, int chair, int point)
	{
		this.playerID = playerID;
		this.nickName = nickName;
		this.chair = chair;
		this.point = point;
	}
	public PlayerPointInfoPROTO toPlayerPointInfoPROTO()
	{
		PlayerPointInfoPROTO builder = new PlayerPointInfoPROTO();
		builder.setPlayerID(playerID);
		builder.setNickName(nickName);
		builder.setPoint(point);
		builder.setChair(chair);
		return builder;
	}
	private void fromProto(PlayerPointInfoPROTO proto)
	{
		this.playerID = proto.getPlayerID();
		this.nickName = proto.getNickName();
		this.chair = proto.getChair();
		this.point = proto.getPoint();
	}
	public int getPlayerID()
	{
		return playerID;
	}
	public void setPlayerID(int playerID)
	{
		this.playerID = playerID;
	}
	public String getNickName()
	{
		return nickName;
	}
	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}
	public int getPoint()
	{
		return point;
	}
	public void setPoint(int point)
	{
		this.point = point;
	}
	public int getChair()
	{
		return chair;
	}
	public void setChair(int chair)
	{
		this.chair = chair;
	}
}
