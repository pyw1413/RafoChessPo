package com.rafo.chess.calculate.majiang.operation.condition;

import java.util.ArrayList;

import com.rafo.chess.calculate.majiang.card.MJCard;

public interface ICondition {
	/**判定方法*/
	public boolean beFireable(String conditionStr,ArrayList<MJCard> card);
}
