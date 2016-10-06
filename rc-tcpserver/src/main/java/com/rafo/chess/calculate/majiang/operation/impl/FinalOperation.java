package com.rafo.chess.calculate.majiang.operation.impl;

import com.rafo.chess.calculate.majiang.card.MJCard;
import com.rafo.chess.calculate.majiang.operation.OptAction;

/***
 * 吃牌的操作
 * @author Administrator
 */
public class FinalOperation extends OptAction{
	public FinalOperation(Integer step, MJCard currentCard) {
		super(step, currentCard);
	}
}
