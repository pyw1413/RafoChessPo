package com.rafo.chess.calculate.majiang.operation.impl;

import com.rafo.chess.calculate.majiang.card.MJCard;
import com.rafo.chess.calculate.majiang.operation.OptAction;
import com.rafo.chess.model.Action;
import com.rafo.chess.model.GameAction;

/***
 * 胡牌的操作
 * @author Administrator
 */
public class HuOperation extends OptAction{

	public HuOperation(Integer step, MJCard currentCard) {
		super(step, currentCard);
		
	}

	public static void main(String[] arge){
		System.out.println(Action.PING_HU.ordinal());
	} 
	
}
