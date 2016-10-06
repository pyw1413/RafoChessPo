package com.rafo.chess.calculate.majiang.operation.plugin.impl;

import static com.rafo.chess.common.Avatas.OpenCards2Array;

import java.util.List;

import com.rafo.chess.calculate.CalculateManager;
import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;
import com.rafo.chess.calculate.majiang.operation.impl.DaOperation;
import com.rafo.chess.calculate.majiang.operation.impl.JiaozuiOperation;
import com.rafo.chess.calculate.majiang.operation.plugin.IOptPlugin;
import com.rafo.chess.common.Avatas;
import com.rafo.chess.model.ActionType;
import com.rafo.chess.model.GameAction;
import com.rafo.chess.utils.CardsUtils;
import com.rafo.chess.utils.GtypeUtils.PlayType;

/***
 * 结算时使用的插件
 * 
 * @author Administrator
 * 
 */
public class jiaozuiPlugin extends AbstractPlugin<JiaozuiOperation> {
	@Override
	public void doOperation(JiaozuiOperation opt) {
		if (analysis(opt)) {
		}
	}

	@Override
	public IPluginConfig getConfig() {
		return config;
	}

	@Override
	public boolean analysis(JiaozuiOperation opt) {
		Integer[] cards = CardsUtils.ChooseCardsPool(PlayType.GYMJ);
		opt.getOperator().getGamePlayer().getHandCards();
		//判断是否胡牌
		List<GameAction> gaList = opt.getOperator().getGamePlayer().getOpenCards();
		Integer[] openCards = Avatas.OpenCards2Array(gaList);
		
		GameAction jiaozui = CalculateManager.judgeHU(opt.getOperator()
				.getGamePlayer().getHandCards(), openCards, ActionType.ZIMO);
		if(jiaozui!=null){
			opt.getOperator().setJiaozui(true);
			return true;
		}
		for (Integer card : cards) {
			jiaozui = Avatas.judgeHU(opt.getOperator().getGamePlayer()
					.getHandCards(), openCards, card, null);
			if (jiaozui != null) {
				opt.getOperator().setJiaozui(true);
				return true;
			}
		}
		return false;
	}
}
