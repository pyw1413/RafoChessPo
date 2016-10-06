package com.rafo.chess.calculate.majiang.operation.effect.impl;

import java.util.List;
import com.rafo.chess.calculate.majiang.OperationManager;
import com.rafo.chess.calculate.majiang.operation.PayDetailed;
import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;
import com.rafo.chess.calculate.majiang.operation.effect.IEffect;
import com.rafo.chess.model.GamePlayer;

/***
 * 支付效果
 * 
 * @author Administrator
 * 
 */
public class HarvestEffect implements IEffect {
	private IPluginConfig pluginConfig;
	GamePlayer dapai;
	GamePlayer owner;
	List<GamePlayer> allPlayers;
	OperationManager operationManager;
	int step;
	int pluginId;

	public HarvestEffect(IPluginConfig conf, GamePlayer dapai,
			GamePlayer owner, List<GamePlayer> allPlayers,int step,
			int pluginId,OperationManager operationManager) {
		this.pluginConfig = conf;
		this.dapai = dapai;
		this.owner = owner;
		this.allPlayers = allPlayers;
		this.operationManager = operationManager;
		this.step = step;
		this.pluginId = pluginId;
	}

	public IPluginConfig getIPluginConfig() {
		return pluginConfig;
	}

	@Override
	public void fireEffect() {
		// 0所有 1点牌的人
		String str = pluginConfig.getIEffectStr();
		if (str == null || str.equals(""))
			return;
		String[] arr = str.split(",");
		int rate = Integer.parseInt(arr[1]);
		PayDetailed ratePay = new PayDetailed();
		ratePay.setPluginId(pluginId);
		ratePay.setStep(step);
		ratePay.setRate(rate);
		int payType = Integer.parseInt(arr[0]);
		if (payType == IEffect.AUTO_PAY_PLAYERS) {
			if (dapai.getUid() == owner.getUid()) {
				payType = IEffect.ALL_PAY_PLAYERS;
			} else {
				payType = IEffect.DA_PAY_PLAYER;
			}
		}
		if (payType == IEffect.ALL_PAY_PLAYERS) {
			int[] fromIds = new int[3];
			int toUid = owner.getUid();
			ratePay.setToUid(toUid);
			int index = 0;
			for (GamePlayer player : allPlayers) {
				if (player.getUid() == toUid)
					continue;
				fromIds[index++] = player.getUid();
			}
			ratePay.setFromUid(fromIds);
		} else {
			int toUid = owner.getUid();
			ratePay.setToUid(toUid);
			int[] fromIds = new int[1];
			fromIds[0] = dapai.getUid();
			ratePay.setFromUid(fromIds);
		}
		operationManager.addPayDetailed(ratePay);
	}
}
