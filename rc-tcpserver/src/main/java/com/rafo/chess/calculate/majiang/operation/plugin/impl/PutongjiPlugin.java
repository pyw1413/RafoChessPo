package com.rafo.chess.calculate.majiang.operation.plugin.impl;

import java.util.ArrayList;
import java.util.List;

import com.rafo.chess.calculate.majiang.card.MJCard;
import com.rafo.chess.calculate.majiang.operation.IOperation;
import com.rafo.chess.calculate.majiang.operation.OptPlayer;
import com.rafo.chess.calculate.majiang.operation.PayDetailed;
import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;
import com.rafo.chess.calculate.majiang.operation.effect.IEffect;
import com.rafo.chess.calculate.majiang.operation.impl.ZhuaOperation;
import com.rafo.chess.calculate.majiang.operation.plugin.IOptPlugin;
import com.rafo.chess.model.AbstractDealer;
import com.rafo.chess.model.GameAction;
import com.rafo.chess.model.GamePlayer;

/***
 * 抓到指定牌触发执行
 * 
 * @author Administrator
 * 
 */
public class PutongjiPlugin extends AbstractPlugin {
	@Override
	public void doOperation(IOperation opt) {
		if (analysis(opt)) {
			// 判断是否有鸡,有多少明鸡
			int ji = 0;
			int mingji = 0;
			Integer[] handCards = opt.getOperator().getGamePlayer()
					.getHandCards();
			List<GameAction> openCards = opt.getOperator().getGamePlayer()
					.getOpenCards();
			for (int i = 0; i < handCards.length; i++) {
				// 手牌
				if (handCards[i] == Integer.parseInt(config.getConditionStr())) {
					ji++;
				}
				// 名牌
				for (GameAction ga : openCards) {
					Integer[] group = ga.getCards().get(0);
					for (int j = 0; j < group.length; j++) {
						if (group[j] == Integer.parseInt(config
								.getConditionStr())) {
							//该鸡是否是有效的冲锋鸡,如果是不算普通鸡
							ArrayList<PayDetailed> list = opt.getOperationManager().getPayDetailedList();
							for(PayDetailed pd:list){
								if(pd.getPluginId()==10&&pd.isValid()){
									continue;
								}
							}
							ji++;
							mingji++;
						}
					}
				}
				// 打出的牌
				Integer[] outCards = opt.getOperator().getGamePlayer().getOutCards();
				if(outCards!=null){
					for (int out : outCards) {
						if (out == Integer.parseInt(config.getConditionStr())) {
							ji++;
							mingji++;
						}
					}
				}

			}
			// 叫嘴，收钱
			int rate = Integer.parseInt(config.getIEffectStr());
			if (opt.getOperator().isJiaozui() && ji > 0) {
				PayDetailed ratePay = new PayDetailed();
				ratePay.setCard(opt.getRecorderInfo().getCard());
				ratePay.setPluginId(config.pluginId());
				ratePay.setStep(opt.getStep());
				ratePay.setRate(rate);
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
					fromUids[index++] = op.getGamePlayer().getUid();
				}
				ratePay.setFromUid(fromUids);
				opt.getOperationManager().addPayDetailed(ratePay);
			}
			// 亮牌未叫嘴，倒赔
			else if (mingji > 0) {
				ArrayList<OptPlayer> list = opt.getOperationManager()
						.getOptPlayers();

				for (OptPlayer op : list) {
					if (op.getGamePlayer().getUid() == opt.getOperator()
							.getGamePlayer().getUid())
						continue;
					if(!op.isJiaozui())
						continue;
					PayDetailed ratePay = new PayDetailed();
					ratePay.setCard(opt.getRecorderInfo().getCard());
					ratePay.setPluginId(config.pluginId());
					ratePay.setStep(opt.getStep());
					ratePay.setRate(rate);
					ratePay.setFromUid(new int[] { opt.getOperator()
							.getGamePlayer().getUid() });
					ratePay.setToUid(op.getGamePlayer().getUid());
					opt.getOperationManager().addPayDetailed(ratePay);
				}
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
