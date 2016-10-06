package com.rafo.chess.calculate.majiang.operation.condition;

import java.util.ArrayList;

import com.rafo.chess.calculate.majiang.card.MJCard;

/***
 * {10,11,12}
 * @author Administrator
 *
 */
public class IsCardCondition implements ICondition{
	/***
	 * 是否是指定牌
	 * @param conditionStr
	 * @param cards
	 * @return
	 */
	public boolean beFireable(String conditionStr,ArrayList<MJCard> cards){
		MJCard card = cards.get(0);
		boolean res = false;
		String[] condArr = conditionStr.split(",");
		for(String s : condArr){
			if(s==null||s.equals(""))
				continue;
			if(Integer.parseInt(s)!=card.getNumber())
				continue;
			res = true;
			break;
		}
		return res;
	}
}
