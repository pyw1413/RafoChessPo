package com.kodgames.battle.service.battle;


import com.kodgames.battle.entity.battle.BattleCensus;

public class MBattleCensus
{
    private int playerId; 
	private int winSelf; // 自摸次数
	private int winOther; // 接炮次数
	private int discardOther; // 点炮次数
	private int kong; // 暗杠次数
	private int cealedKong; // 明杠次数
	private int point;      // 分数
	
	public MBattleCensus(int playerId)
	{
		this.playerId = playerId;
	}
	
	public void addPoint(int point)
	{
		this.point += point;
	}
	
	public void addWinSelf()
	{
		winSelf++;
	}
	
	public void addWinOther()
	{
		winOther++;
	}
	
	public void addDiscardOther()
	{
		discardOther++;
	}
	
	public void addKong()
	{
		kong++;
	}
	
	public void addCealedKong()
	{
		cealedKong++;
	}
	
	public BattleCensus toBattleCensus()
	{
		BattleCensus builder = new BattleCensus();
		builder.setPlayerId(playerId);
		builder.setWinSelf(winSelf);
		builder.setWinOther(winOther);
		builder.setDiscardOther(discardOther);
		builder.setKong(kong);
		builder.setCealedKong(cealedKong);
		builder.setPoint(point);
		return builder;
	}
}
