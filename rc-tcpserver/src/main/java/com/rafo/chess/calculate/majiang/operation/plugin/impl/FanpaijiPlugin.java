package com.rafo.chess.calculate.majiang.operation.plugin.impl;

import java.util.ArrayList;
import java.util.List;

import com.rafo.chess.calculate.majiang.operation.IOperation;
import com.rafo.chess.calculate.majiang.operation.OptPlayer;
import com.rafo.chess.calculate.majiang.operation.PayDetailed;
import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;
import com.rafo.chess.model.GameAction;

/***
 * 翻牌机
 * 
 * @author Administrator
 * 
 */
public class FanpaijiPlugin extends AbstractPlugin {
	@Override
	public void doOperation(IOperation opt) {
		if (analysis(opt)) {
			// 判断是否有鸡,有多少明鸡
			int fpjCard = opt.getOperationManager().getDealer().getFPJCard()+1;
			if(fpjCard%100==10){
				fpjCard -=9;
			}
			int ji = 0;
			int rateParam = 1;
			if(fpjCard==301)
				rateParam = 2;
			
			Integer[] handCards = opt.getOperator().getGamePlayer()
					.getHandCards();
			List<GameAction> openCards = opt.getOperator().getGamePlayer()
					.getOpenCards();
			for (int i = 0; i < handCards.length; i++) {
				// 手牌
				if (handCards[i] == fpjCard) {
					ji++;
				}

			}

			// 名牌
			for (GameAction ga : openCards) {
				Integer[] group = ga.getCards().get(0);
				for (int j = 0; j < group.length; j++) {
					if (group[j] == fpjCard) {
						//该鸡是否是有效的冲锋鸡,如果是不算普通鸡
						ArrayList<PayDetailed> list = opt.getOperationManager().getPayDetailedList();
						for(PayDetailed pd:list){
							if(pd.getPluginId()==10&&pd.isValid()){
								continue;
							}
						}
						ji++;
					}
				}
			}
			// 打出的牌
			Integer[] openCardsArr = opt.getOperator().getGamePlayer().getOutCards();
			if(openCardsArr!=null){
				for (int out : opt.getOperator().getGamePlayer().getOutCards()) {
					if (out == fpjCard) {
						ji++;
					}
				}
			}


			// 叫嘴，收钱
			int rate = Integer.parseInt(config.getIEffectStr());
			if (opt.getOperator().isJiaozui() && ji > 0) {
				PayDetailed ratePay = new PayDetailed();
				ratePay.setCard(fpjCard);
				ratePay.setPluginId(config.pluginId());
				ratePay.setStep(opt.getStep());
				ratePay.setRate(rate*rateParam*ji);
				ratePay.setToUid(opt.getOperator().getGamePlayer().getUid());
				@SuppressWarnings("unchecked")
				ArrayList<OptPlayer> list = opt.getOperationManager()
						.getOptPlayers();
				int index = 0;
				int[] fromUids = new int[3];
				for (OptPlayer op : list) {
					if (op.getGamePlayer().getUid() == opt.getOperator()
							.getGamePlayer().getUid())
						continue;
					if(op.isJiaozui())
						continue;
					fromUids[index++] = op.getGamePlayer().getUid();
				}
				ratePay.setFromUid(fromUids);
				opt.getOperationManager().addPayDetailed(ratePay);
			}
		}
	}

	@Override
	public IPluginConfig getConfig() {
		return config;
	}

	@Override
	public boolean analysis(IOperation opt) {
		return true;
	}

}
