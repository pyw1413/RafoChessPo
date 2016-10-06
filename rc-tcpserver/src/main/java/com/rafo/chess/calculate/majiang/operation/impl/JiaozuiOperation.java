package com.rafo.chess.calculate.majiang.operation.impl;

import com.rafo.chess.calculate.majiang.card.MJCard;
import com.rafo.chess.calculate.majiang.operation.OptAction;
import com.rafo.chess.model.GameAction;

/***
 * 通用，判断叫嘴，普通级
 * @author Administrator
 */
public class JiaozuiOperation extends OptAction{
	GameAction action = null;
	
	
	public JiaozuiOperation(Integer step, MJCard currentCard) {
		super(step, currentCard);
		
	}
	
}
