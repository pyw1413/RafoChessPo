package com.kodgames.battle.common;

import com.kodgames.battle.service.battle.MBattleStep;

import java.util.ArrayList;
import java.util.List;


public class Player
{
	public enum PlayState
	{
		Idle, Ready, Battle,
	}

	private String accountID;
	private int ID;
	private String name;
	private String head;
	private int chair;
	private int points;
	private PlayState playState = PlayState.Idle;
	private boolean isOffline;
	private int serverID;
	private String ip;
	private String sex;
	
	// 相同ip判定数据
	private String sameIp;
	private List<String> sameIpAccIDs = new ArrayList<String>();
	private List<MBattleStep> steps;

	public Player(String accountID, int ID, String name, String head, int chair, String ip,
			String sex, int serverID)
	{
		this.accountID = accountID;
		this.ID = ID;
		this.name = name;
		this.head = head;
		this.chair = chair;
		this.points = 0;
		this.serverID = -1;
		this.ip = ip;
		this.sex = sex;
		this.serverID = serverID;
	}


	public int getServerID()
	{
		return serverID;
	}

	public void setServerID(int serverID)
	{
		this.serverID = serverID;
	}

	public void setHead(String head)
	{
		this.head = head;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/*
	 * 设置ServerID，如果不为-1则不会再设置
	 */
	// public void setServerID(int serverID)
	// {
	// this.serverID = serverID;
	// }
	public int getID()
	{
		return ID;
	}

	public String getAccountID()
	{
		return accountID;
	}

	public String getName()
	{
		return name;
	}

	public String getHead()
	{
		return head;
	}

	public int getChair()
	{
		return chair;
	}

	public int getPoints()
	{
		return points;
	}

	public String getSex()
	{
		return sex;
	}

	public void setSex(String sex)
	{
		this.sex = sex;
	}

	public void modifyPoints(int alterPoints)
	{
		this.points += alterPoints;
	}

	public PlayState getPlayState()
	{
		return playState;
	}

	public void setPlayState(PlayState playState)
	{
		this.playState = playState;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getIp()
	{
		return ip;
	}

	public boolean isOffline()
	{
		return isOffline;
	}

	public void setOffline(boolean isOffline)
	{
		this.isOffline = isOffline;
	}
	
	public boolean needResetSameIps(List<String> accountIDs)
	{
		if(sameIp == null || !sameIp.equals(ip))
			return true;
		
		if(accountIDs.size() > sameIpAccIDs.size())
			return true;
		
		return false;
	}
	
	public void setSameIp(List<String> accountIDs)
	{
		sameIp = ip;
		sameIpAccIDs.clear();
		for(String accountID : accountIDs)
			sameIpAccIDs.add(accountID);
	}
	
	public void resetSameIp(String ip, String accountID)
	{
		if(sameIp == null || !sameIp.equals(ip))
			sameIpAccIDs.clear();
		
		sameIp = ip;
		if(!sameIpAccIDs.contains(accountID))
			sameIpAccIDs.add(accountID);
	}
}