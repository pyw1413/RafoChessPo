package com.rafo.chess.calculate.majiang.operation.plugin.impl;

import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;
import com.rafo.chess.calculate.majiang.operation.impl.HuOperation;
import com.rafo.chess.model.Action;

/***
 * 抓到指定牌触发执行
 * 
 * @author Administrator
 * 
 */
public class HUPlugin extends AbstractPlugin<HuOperation> {
	@Override
	public void doOperation(HuOperation opt) {
		if (analysis(opt)) {
			payment(opt);
		}
	}

	@Override
	public IPluginConfig getConfig() {
		return config;
	}

	@Override
	public boolean analysis(HuOperation opt) {
		int index = opt.getRecorderInfo().getPlayType();
		int configIndex = Integer.parseInt(config.getConditionStr());
		return index == configIndex;
	}
}
