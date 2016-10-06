package com.rafo.chess.calculate.majiang.operation.plugin.impl;

import java.util.ArrayList;

import com.rafo.chess.calculate.majiang.operation.IOperation;
import com.rafo.chess.calculate.majiang.operation.PayDetailed;
import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;
import com.rafo.chess.calculate.majiang.operation.impl.DaOperation;
import com.rafo.chess.calculate.majiang.operation.impl.GangOperation;
import com.rafo.chess.calculate.majiang.operation.impl.HuOperation;
import com.rafo.chess.calculate.majiang.operation.impl.ZhuaOperation;
import com.rafo.chess.model.Action;

/***
 * 杠后炮胡
 * 
 * @author Administrator
 * 
 */
public class GangHouPaoHuPlugin extends AbstractPlugin<HuOperation> {
	@Override
	public void doOperation(HuOperation opt) {
		if (analysis(opt)) {
			payment(opt);

			@SuppressWarnings("unchecked")
			ArrayList<PayDetailed> list = opt.getOperationManager()
					.getPayDetailedList();
			for (PayDetailed pd : list) {
				if (pd.getToUid() != opt.getFireder().getUid())
					continue;
				if (pd.getPluginId() == 11 || pd.getPluginId() == 12
						|| pd.getPluginId() == 13 || pd.getPluginId() == 15) {
					pd.setValid(false);
				}
			}

		}
	}

	@Override
	public IPluginConfig getConfig() {
		return config;
	}

	@Override
	public boolean analysis(HuOperation opt) {
		int step = opt.getStep();
		IOperation last1 = opt.getOperationManager().getOperationByStep(
				step - 1);
		IOperation last2 = opt.getOperationManager().getOperationByStep(
				step - 2);
		if (!(last1 instanceof DaOperation)
				|| last1.getFireder().getUid() != opt.getFireder().getUid()) {
			return false;
		}
		if (!(last2 instanceof GangOperation)
				|| last2.getFireder().getUid() != opt.getFireder().getUid())
			return false;

		return true;
	}
}
