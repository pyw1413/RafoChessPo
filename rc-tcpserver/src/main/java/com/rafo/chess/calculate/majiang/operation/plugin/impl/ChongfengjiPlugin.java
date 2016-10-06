package com.rafo.chess.calculate.majiang.operation.plugin.impl;

import java.util.ArrayList;

import com.rafo.chess.calculate.ResultFlag;
import com.rafo.chess.calculate.majiang.card.MJCard;
import com.rafo.chess.calculate.majiang.operation.OptPlayer;
import com.rafo.chess.calculate.majiang.operation.PayDetailed;
import com.rafo.chess.calculate.majiang.operation.condition.ICondition;
import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;
import com.rafo.chess.calculate.majiang.operation.effect.IEffect;
import com.rafo.chess.calculate.majiang.operation.impl.DaOperation;
import com.rafo.chess.calculate.majiang.operation.impl.GangOperation;
import com.rafo.chess.calculate.majiang.operation.plugin.IOptPlugin;
import com.rafo.chess.model.AbstractDealer;
import com.rafo.chess.model.GamePlayer;

/***
 * 
 * @author Administrator
 * 
 */
public class ChongfengjiPlugin extends AbstractPlugin<DaOperation> {
	@Override
	public void doOperation(DaOperation opt) {
		if (analysis(opt)) {
			String str = config.getIEffectStr();
			if (str == null || str.equals(""))
				return;
			int rate = Integer.parseInt(str);
			if (opt.getOperator().isJiaozui()) {
				PayDetailed ratePay = new PayDetailed();
				ratePay.setPluginId(config.pluginId());
				ratePay.setStep(opt.getStep());
				ratePay.setCard(opt.getRecorderInfo().getCard());
				int[] others = new int[3];
				AbstractDealer dealer = opt.getOperationManager()
						.getDealer();
				int index = 0;
				for (GamePlayer player : dealer.getPlayers()) {
					if (player.getUid() == opt.getOperator().getGamePlayer().getUid())
						continue;
					others[index++] = player.getUid();
				}
				int toUid = opt.getOperator().getGamePlayer().getUid();
				ratePay.setToUid(toUid);
				ratePay.setFromUid(others);
				opt.getOperationManager().addPayDetailed(ratePay);
				ratePay.setRate(rate);
				ratePay.setToUid(toUid);
				ratePay.setFromUid(others);
				opt.getOperationManager().addPayDetailed(ratePay);
			}
			else{
				ArrayList<OptPlayer> players = opt.getOptManager().getOptPlayers();
				for(OptPlayer op:players){
					if(op.getGamePlayer().getUid()==opt.getOperator().getGamePlayer().getUid())
						continue;
					if(!op.isJiaozui())
						continue;
					int toUid = op.getUid();
					PayDetailed ratePay = new PayDetailed();
					ratePay.setCard(opt.getRecorderInfo().getCard());
					ratePay.setPluginId(config.pluginId());
					ratePay.setStep(opt.getStep());
					ratePay.setToUid(toUid);
					ratePay.setFromUid(new int[]{opt.getOperator().getUid()});
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
	public boolean analysis(DaOperation opt) {
		boolean res = false;
		String conditionStr = config.getConditionStr();
		String[] condArr = conditionStr.split(",");
		for (String s : condArr) {
			if (s == null || s.equals(""))
				continue;
			if (Integer.parseInt(s) != opt.getRecorderInfo().getCard())
				continue;
			res = true;
			break;
		}
		return res;
	}

}
