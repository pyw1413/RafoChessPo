package com.rafo.chess.calculate.majiang.operation.plugin.impl;

import com.rafo.chess.calculate.majiang.operation.IOperation;
import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;
import com.rafo.chess.calculate.majiang.operation.impl.GangOperation;
import com.rafo.chess.calculate.majiang.operation.impl.HuOperation;
import com.rafo.chess.calculate.majiang.operation.impl.ZhuaOperation;

/***
 * 抓到指定牌触发执行
 * 
 * @author Administrator
 * 
 */
public class GangShangHuPlugin extends AbstractPlugin<HuOperation> {
	@Override
	public void doOperation(HuOperation opt) {
		if (analysis(opt)) {
			if(opt.getOperator().isJiaozui())
				payment(opt);
		}
	}

	@Override
	public IPluginConfig getConfig() {
		return config;
	}

	@Override
	public boolean analysis(HuOperation opt) {
		int step = opt.getStep();
		IOperation last1 = opt.getOperationManager().getOperationByStep(step-1);
		IOperation last2 = opt.getOperationManager().getOperationByStep(step-2);
		if(!(last1 instanceof ZhuaOperation)
				||last1.getOperator().getUid()!=opt.getOperator().getUid()){
			return false;
		}
		if(!(last2 instanceof GangOperation)||last2.getOperator().getUid()!=opt.getOperator().getUid())
			return false;
		
		return true;
	}
}
