package com.rafo.chess.calculate.majiang.operation.plugin.impl;

import java.util.ArrayList;

import com.rafo.chess.calculate.majiang.operation.IOperation;
import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;
import com.rafo.chess.calculate.majiang.operation.impl.GangOperation;
import com.rafo.chess.calculate.majiang.operation.impl.HuOperation;
import com.rafo.chess.model.Action;

/***
 * 鎶撳埌鎸囧畾鐗岃Е鍙戞墽琛�
 * 
 * @author Administrator
 * 
 */
public class GangPlugin extends AbstractPlugin<GangOperation> {
	@Override
	public void doOperation(GangOperation opt) {
		if (analysis(opt)) {
			if (opt.getOperator().isJiaozui()) {
				@SuppressWarnings("unchecked")
				ArrayList<IOperation> optList = opt.getOperationManager()
						.getOperationList();
				// 是否有过牌的操作
				for (IOperation oo : optList) {
					if(oo.getRecorderInfo()==null)
						continue;
					if (Action.getByCode(oo.getRecorderInfo().getPlayType()) == Action.GUO
							&& oo.getRecorderInfo().getCard() == opt.getRecorderInfo().getCard()) {
						return;
					}
				}
				payment(opt);
			}
		}
	}

	@Override
	public IPluginConfig getConfig() {
		return config;
	}

	@Override
	public boolean analysis(GangOperation opt) {
		int playType = opt.getRecorderInfo().getPlayType();
		int configIndex = Integer.parseInt(config.getConditionStr());
		return playType == configIndex;
	}
}
