package com.kodgames.battle.service.battle;

public class MajhongHelper 
{
	public enum CardType
	{
		Wan(11),
		Tiao(21),
		Tong(31),
		Hua(41);
		
		private int value;
		private CardType(int value)
		{
			this.value = value;
		}
		
		public int Value()
		{
			return this.value;
		}
	}
	
	public enum HuType
	{
		Unknow, // 未叫嘴
		Ting,   // 叫嘴
		PingHU,
		DaDuiZiHu,
		QiDuiHu,
		LongQiDuiHu,
		QingYiSeHu,
		QingDaduiHU,
		QingQiDuiHu,
		QingLongBeiHu,
	}
	
	public static final int ONE_CARD_MAX = 4;
	public static final int ONE_TYPE_MAX = 9;
	public static final int PLAY_CARD_COUNT = 13;
	private boolean jiangSingle = false;
	private int[] temp = new int[CardType.Tong.Value() + MajhongHelper.ONE_TYPE_MAX];
	private int[] temp4other = new int[CardType.Tong.Value() + MajhongHelper.ONE_TYPE_MAX];
	
	public int checkPong(int[] checkPai, int card)
	{
		if(checkPai[card] >= 2)
			return card;
		else
			return -1;
	}
	
	public HuType checkTing(int[] checkPai, int[] replace, boolean fullCards)
	{
		int replaceIndex = 0;
		HuType tingType = HuType.Unknow;
		HuType tempType = HuType.Unknow;
		for(int i = 0; i< checkPai.length; i++)
		{
			if(checkPai[i] <= 0)
				continue;
			if(fullCards)
				checkPai[i] -= 1;
			for(int j = CardType.Wan.Value(); j < CardType.Wan.Value() + ONE_TYPE_MAX; j++)
			{
				checkPai[j] += 1;
				System.arraycopy(checkPai, 0, temp, 0, checkPai.length);
				if(temp[j] < 0)
					temp[j] = 1;
				tempType = checkHu(temp, j, true);
				if(tempType != HuType.Unknow && (replaceIndex == 0 || replace[replaceIndex - 1] != i))
				{
					replace[replaceIndex++] = i;
					if(tingType.ordinal() < tempType.ordinal())
						tingType = tempType;
				}
					
				checkPai[j] -= 1;
			}
			
			for(int j = CardType.Tiao.Value(); j < CardType.Tiao.Value() + ONE_TYPE_MAX; j++)
			{
				checkPai[j] += 1;
				System.arraycopy(checkPai, 0, temp, 0, checkPai.length);
				if(temp[j] < 0)
					temp[j] = 1;
				tempType = checkHu(temp, j, true);
				if(tempType != HuType.Unknow && (replaceIndex == 0 || replace[replaceIndex - 1] != i))
				{
					replace[replaceIndex++] = i;
					if(tingType.ordinal() < tempType.ordinal())
						tingType = tempType;
				}
				checkPai[j] -= 1;
			}
			for(int j = CardType.Tong.Value(); j < CardType.Tong.Value() + ONE_TYPE_MAX; j++)
			{
				checkPai[j] += 1;
				System.arraycopy(checkPai, 0, temp, 0, checkPai.length);
				if(temp[j] < 0)
					temp[j] = 1;
				tempType = checkHu(temp, j, true);
				if(tempType != HuType.Unknow && (replaceIndex == 0 || replace[replaceIndex - 1] != i))
				{
					replace[replaceIndex++] = i;
					if(tingType.ordinal() < tempType.ordinal())
						tingType = tempType;
				}
				checkPai[j] -= 1;
			}
			if(fullCards)
				checkPai[i] += 1;
			else
				break;
		}
		
		return tingType;
	}
	
	public HuType checkHu(int[] checkPai, int drawCard, boolean checkPinghu)
	{
		jiangSingle = false;
		HuType huType = HuType.Unknow;
		
		boolean qing1Se = check1Se(checkPai);
		if(checkLong7Dui(checkPai))
		{
			// 龙七对，只能胡4个相同的那张牌
			if(checkPai[drawCard] == 4)
				huType = HuType.LongQiDuiHu;
			else
				huType = HuType.QiDuiHu;
		}
		else if(check7Dui(checkPai))
			huType = HuType.QiDuiHu;
		else if(checkDaDuiZi(checkPai))
			huType = HuType.DaDuiZiHu;
		
		if(huType == HuType.Unknow)
		{
			if(checkPinghu || qing1Se)
				huType = checkingHu(checkPai) ? (qing1Se ? HuType.QingYiSeHu : HuType.PingHU ): huType;
		}
		else if(qing1Se)
			huType = HuType.values()[huType.ordinal() + (HuType.QingYiSeHu.ordinal() - HuType.DaDuiZiHu.ordinal() + 1)];
		
		// 大对子单吊算清一色
		if(huType == HuType.DaDuiZiHu && checkPaiCount(checkPai) == 2 && checkPai[drawCard] == 2)
			huType = HuType.QingYiSeHu;
		
		return huType;
	}
	
	private boolean checkDaDuiZi(int[] checkPai)
	{
		return checkOther(checkPai, new int[]{1, 4, 0}, false);
	}
	
	private boolean check7Dui(int[] checkPai)
	{
		return checkOther(checkPai, new int[]{7, 0, 0}, true);
	}
	
	private boolean checkLong7Dui(int[] checkPai)
	{
		return checkOther(checkPai, new int[]{5, 0, 1}, true) || 
			   checkOther(checkPai, new int[]{3, 0, 2}, true) ||
			   checkOther(checkPai, new int[]{1, 0, 3}, true);
	}
	
	private boolean checkOther(int[] checkPai, int[] parttern, boolean checkCount)
	{
		System.arraycopy(checkPai, 0, temp4other, 0, checkPai.length);
		int sumCount = 0;
		for(int i = CardType.Wan.Value(); i < temp4other.length; i++)
		{
			switch (temp4other[i]) {
			case 1:
				return false;
			case 2:
				if(parttern[0] > 0)
				{
					parttern[0] -= 1;
					temp4other[i] = 0;
					sumCount += 2;
				}
				break;
			case 3:
				if(parttern[1] > 0)
				{
					parttern[1] -= 1;
					temp4other[i] = 0;
					sumCount += 3;
				}
				break;
			case -3 :
				if(parttern[1] < 0)
					return false;
				break;
			case 4:
				if(parttern[2] > 0)
				{
					parttern[2] -= 1;
					temp4other[i] = 0;
					sumCount += 4;
				}
				break;
			case -4:
				if(parttern[2] < 0)
					return false;
				break;
			}
		}
		
		return checkPaiCount(temp4other) == 0 &&
			  (checkCount ? sumCount == PLAY_CARD_COUNT + 1 : true) &&
			  ((parttern[0] <= 0 && parttern[1] <= 0) ||
			  (parttern[0] <= 0 && parttern[2] <= 0) ||
			  (parttern[1] <= 0 && parttern[2] <= 0));
	}
	
	private boolean check1Se(int[] checkPai)
	{
		int wCount = 0;
		int tCount = 0;
		int bCount = 0;
		
		for(int i = CardType.Wan.Value(); i < CardType.Wan.Value() + ONE_TYPE_MAX; i++)
			wCount += Math.abs(checkPai[i]);
		for(int i = CardType.Tiao.Value(); i < CardType.Tiao.Value() + ONE_TYPE_MAX; i++)
			tCount += Math.abs(checkPai[i]);
		for(int i = CardType.Tong.Value(); i < CardType.Tong.Value() + ONE_TYPE_MAX; i++)
			bCount += Math.abs(checkPai[i]);
		
		return (wCount == 0 && tCount == 0) || (wCount == 0 && bCount == 0) || (tCount == 0 && bCount == 0);
	}
	
	private boolean checkingHu(int[] checkPai)
	{
		for(int i = 0; i < checkPai.length; i++)
		{
			if(checkPai[i] < 0 && checkPai[i] != -3 && checkPai[i] != -4)
				checkPai[i] = 1;
		}
		
		return checkHuAfterFixed(checkPai);
	}
	
	private boolean checkHuAfterFixed(int[] checkPai)
	{
		if(checkPaiCount(checkPai) <= 0)
			return true;
		
		for(int i = 0; i < checkPai.length; i++)
		{
			if(checkPai[i] < 0 && checkPai[i] != -3 && checkPai[i] != -4)
				checkPai[i] = 1;
			
			if(checkPai[i] <= 0)
				continue;
			
			if(checkPai[i] == 4)
			{
				checkPai[i] = 0;
				if(checkingHu(checkPai))
					return true;
				checkPai[i] = 4;
			}
			
			if(checkPai[i] >= 3)
			{
				checkPai[i] -= 3;
				if(checkingHu(checkPai))
					return true;
				checkPai[i] += 3;
			}
			
			if(!jiangSingle && checkPai[i] >= 2)
			{
				jiangSingle = true;
				checkPai[i] -= 2;
				if(checkingHu(checkPai))
					return true;
				jiangSingle = false;
				checkPai[i] += 2;
			}
			
			if(i % (ONE_TYPE_MAX + 1) < ONE_TYPE_MAX - 1 && 
			  checkPai[i + 1] > 0 &&
			  checkPai[i + 2] > 0)
			{
				checkPai[i] -= 1;
				checkPai[i + 1] -= 1;
				checkPai[i + 2] -= 1;
				if(checkingHu(checkPai))
					return true;
				checkPai[i] += 1; 
				checkPai[i + 1] += 1;
				checkPai[i + 2] += 1;
			}
		}
		
		return false;
	}
	
	private int checkPaiCount(int[] checkPai)
	{
		int count = 0;
		for(int i = 0; i < checkPai.length; i++)
		{
			if(checkPai[i] <= 0)
				continue;
			count += checkPai[i];
		}
			
		return count;
	}
}
