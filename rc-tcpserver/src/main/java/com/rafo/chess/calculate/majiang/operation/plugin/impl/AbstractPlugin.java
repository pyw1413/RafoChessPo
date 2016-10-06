package com.rafo.chess.calculate.majiang.operation.plugin.impl;

import com.rafo.chess.calculate.majiang.operation.IOperation;
import com.rafo.chess.calculate.majiang.operation.PayDetailed;
import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;
import com.rafo.chess.calculate.majiang.operation.effect.IEffect;
import com.rafo.chess.calculate.majiang.operation.plugin.IOptPlugin;
import com.rafo.chess.model.AbstractDealer;
import com.rafo.chess.model.GamePlayer;

public abstract class AbstractPlugin<O extends IOperation> implements IOptPlugin<O>{
	IPluginConfig config;
	
	public void setConfig(IPluginConfig config) {
		this.config = config;
	}
	public PayDetailed payment(O opt) {
		// 0所有 1点牌的人2自动判断
		String str = config.getIEffectStr();
		if (str == null || str.equals(""))
			return null;
		String[] arr = str.split(",");
		int rate = Integer.parseInt(arr[1]);
		PayDetailed ratePay = new PayDetailed();
		if(opt.getRecorderInfo()!=null)
			ratePay.setCard(opt.getRecorderInfo().getCard());
		ratePay.setPluginId(config.pluginId());
		ratePay.setStep(opt.getStep());
		ratePay.setRate(rate);
		int payType = Integer.parseInt(arr[0]);
		if (payType == IEffect.AUTO_PAY_PLAYERS) {
			if (opt.getOperator().getGamePlayer().getUid() == opt.getFireder().getGamePlayer().getUid()) {
				payType = IEffect.ALL_PAY_PLAYERS;
			} else {
				payType = IEffect.DA_PAY_PLAYER;
			}
		}
		if (payType == IEffect.ALL_PAY_PLAYERS) {
			int[] fromIds = new int[3];
			int toUid = opt.getOperator().getGamePlayer().getUid();
			ratePay.setToUid(toUid);
			int index = 0;
			AbstractDealer dealer = opt.getOperationManager().getDealer();
			for (GamePlayer player : dealer.getPlayers()) {
				if (player.getUid() == toUid)
					continue;
				fromIds[index++] = player.getUid();
			}
			ratePay.setFromUid(fromIds);
		} else {
			int toUid = opt.getOperator().getGamePlayer().getUid();
			ratePay.setToUid(toUid);
			int[] fromIds = new int[1];
			fromIds[0] = opt.getFireder().getGamePlayer().getUid();
			ratePay.setFromUid(fromIds);
		}
		opt.getOperationManager().addPayDetailed(ratePay);
		return ratePay;
	}

}
