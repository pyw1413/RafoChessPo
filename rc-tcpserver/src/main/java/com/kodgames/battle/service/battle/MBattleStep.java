package com.kodgames.battle.service.battle;

import com.kodgames.battle.entity.battle.BattleStep;

import java.util.ArrayList;


public class MBattleStep
{



	public class PlayType
	{
		public static final int Idle = 0;
		public static final int Deal = 1 << 1; // 发牌
		public static final int Draw = 1 << 2; // 摸牌
		public static final int Discard = 1 << 3; // 打牌
		public static final int CanPong = 1 << 4; // 可碰
		public static final int Pong = 1 << 5; // 碰
		public static final int CanKong = 1 << 6; // 可明杠
		public static final int Kong = 1 << 7; // 明杠
		public static final int CanCealedKong = 1 << 8; // 可暗杠
		public static final int CealedKong = 1 << 9; // 暗杠
		public static final int CanDotKong = 1 << 10; // 可点杠
		public static final int DotKong = 1 << 11; // 点杠
		public static final int CanHu = 1 << 12; // 可胡
		public static final int Hu = 1 << 13; // 胡牌
		public static final int He = 1 << 14;
		public static final int CanReadyHand = 1 << 15; // 可听牌
		public static final int ReadyHand = 1 << 16; // 听牌(软听)
		public static final int OffLine = 1 << 17; // 离线
		public static final int Pass = 1 << 18; // 过
		public static final int ChargeChicken = 1 << 19; // 冲锋鸡
		public static final int DutyChicken = 1 << 20; // 责任鸡
		
		// 以下的不参与出牌操作，用于结算
		public static final int NormalChicken = 1 << 21; // 普通鸡
		public static final int FlopChicken = 1 << 22; // 翻牌鸡
		public static final int KillReadyHand = 1 << 23; // 杀胡
		public static final int QiangGang = 1 << 24; // 抢杠
		public static final int GangShangHu = 1 << 25; // 杠上胡
		public static final int GangHouPao = 1 << 26; // 杠后炮
		public static final int WinSelf = 1 << 27; // 自摸
		public static final int DiscardOther = 1 << 28; // 点炮
		public static final int DotKongOther = 1 << 29; // 点明杠
		public static final int HardReadHand = 1 << 30; // 硬听
		public static final int BeKilledReadHand = (1 << 30) + 1; // 被杀报
	}

	private String owernAccId;
	private String targetAccId;
	private int playType;
	private int winType;
	private boolean isGangDraw; // 是否是杠后第一次摸牌
	private boolean isGangDiscard; // 是否是杠后第一次打牌
	private String gangAccId;
	private ArrayList<Integer> cards = new ArrayList<Integer>();
	private ArrayList<String> ignoreAccIds = new ArrayList<String>();
	private ArrayList<String> huLists = new ArrayList<>();
	private boolean ignoreOther;

	public MBattleStep()
	{
		this.cards.clear();
		this.playType = PlayType.Idle;
	}

	public String getOwernAccId()
	{
		return owernAccId;
	}

	public void setOwernAccId(String owernAccId)
	{
		this.owernAccId = owernAccId;
	}

	public boolean isTargetValid()
	{
		return targetAccId != null && targetAccId.equals("") == false;
	}

	public String getTargetAccId()
	{
		return targetAccId;
	}

	public void setTargetAccId(String targetAccId)
	{
		this.targetAccId = targetAccId;
	}

	public int getPlayType()
	{
		return playType;
	}

	public void setPlayType(int playType)
	{
		if (this.playType == 0)
			this.playType = playType;
		else
			this.playType |= playType;
	}

	public int cardSize()
	{
		return cards.size();
	}

	public int getCard(int index)
	{
		if (cards.size() <= 0)
			return -1;
		else
			return cards.get(index);
	}

	public void setCard(int card)
	{
		cards.add(card);
	}

	public void setCard(int index, int card)
	{
		if (cards.size() > index)
			cards.set(index, card);
		else
			cards.add(card);
	}

	public void setCards(ArrayList<Integer> cards)
	{
		if(cards == null || cards.size() <= 0)
			return;
		
		this.cards.clear();
		for(int card : cards)
			this.cards.add(card);
	}
	
	public boolean containsCard(int card)
	{
		return cards.contains(card);
	}

	public void clearIgnoreLists()
	{
		ignoreAccIds.clear();
	}

	public boolean containsIgoreID(String accountID)
	{
		return ignoreAccIds.contains(accountID);
	}

	public void addIgnoreId(String accountID)
	{
		if (containsIgoreID(accountID) == false)
			ignoreAccIds.add(accountID);
	}

	public int getWinType()
	{
		return winType;
	}

	public void setWinType(int winType)
	{
		this.winType = winType;
	}
	
	public boolean isGangDraw()
	{
		return isGangDraw;
	}

	public void setGangDraw(boolean isGangDraw)
	{
		this.isGangDraw = isGangDraw;
	}

	public boolean isGangDiscard()
	{
		return isGangDiscard;
	}

	public void setGangDiscard(boolean isGangDiscard)
	{
		this.isGangDiscard = isGangDiscard;
	}
 
	public boolean checkGangAccId()
	{
		return gangAccId != null && gangAccId.equals(owernAccId);
	}
	
	public String getGangAccId()
	{
		return this.gangAccId;
	}
	
	public void setGangAccId(String gangAccId)
	{
		this.gangAccId = gangAccId;
	}

	public ArrayList<String> getHuLists()
	{
		return huLists;
	}

	public void setHuLists(ArrayList<String> huLists)
	{
		if(huLists == null || huLists.size() <= 0)
			return;
		
		this.huLists.clear();
		for(String accountId : huLists)
			this.huLists.add(accountId);
	}

	public boolean isSelf()
	{
		return owernAccId != null && targetAccId != null && owernAccId.equals(targetAccId);
	}

	public static int getHuTypeScore(MajhongHelper.HuType huType)
	{
		switch (huType)
		{
		case PingHU:
			return 1;
		case DaDuiZiHu:
			return 5;
		case QiDuiHu:
			return 10;
		case LongQiDuiHu:
			return 20;
		case QingYiSeHu:
			return 10;
		case QingDaduiHU:
			return 15;
		case QingQiDuiHu:
			return 20;
		case QingLongBeiHu:
			return 30;
		default:
			return 0;
		}
	}

	public static int getObtypeScore(int playType)
	{
		switch (playType)
		{
		case PlayType.WinSelf:
			return 1;
		case PlayType.Kong:
			return 3;
		case PlayType.DotKong:
			return 3;
		case PlayType.CealedKong:
			return 3;
		case PlayType.ReadyHand:
			return 10;
		case PlayType.HardReadHand:
			return 20;
		case PlayType.ChargeChicken:
			return 2;
		case PlayType.DutyChicken:
			return 1;
		case PlayType.NormalChicken:
			return 1;
		case PlayType.FlopChicken:
			return 1;
		case PlayType.GangShangHu:
			return 3;
		case PlayType.GangHouPao:
			return 3;
		case PlayType.QiangGang:
			return 9;
		case PlayType.KillReadyHand:
			return 10;
		case PlayType.BeKilledReadHand:
			return -10;
		default:
			return 0;
		}
	}



	public BattleStep toBattleStep()
	{
		BattleStep builder = new BattleStep();
		builder.setPlayType(playType);
		for (int i = 0; i < cards.size(); i++)
			builder.addCard(cards.get(i));
		return builder;
	}
	
	public MBattleStep copyOf()
	{
		MBattleStep battleStep = new MBattleStep();
		battleStep.setOwernAccId(owernAccId);
		battleStep.setTargetAccId(targetAccId);
		battleStep.setWinType(winType);
		battleStep.setPlayType(playType);
		battleStep.setGangDiscard(isGangDiscard);
		battleStep.setGangDraw(isGangDraw);
		battleStep.setGangAccId(gangAccId);
		battleStep.setHuLists(huLists);
		battleStep.setCards(cards);
		return battleStep;
	}

	public void setIgnoreOther(boolean ignoreOther) {
		this.ignoreOther = ignoreOther;
	}

	public boolean isIgnoreOther() {
		return ignoreOther;
	}
}
