package com.rafo.chess.calculate.majiang.operation.plugin.impl;

import java.util.ArrayList;

import com.rafo.chess.calculate.majiang.operation.IOperation;
import com.rafo.chess.calculate.majiang.operation.PayDetailed;
import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;
import com.rafo.chess.calculate.majiang.operation.impl.DaOperation;
import com.rafo.chess.calculate.majiang.operation.impl.GangOperation;
import com.rafo.chess.calculate.majiang.operation.impl.HuOperation;
import com.rafo.chess.calculate.majiang.operation.impl.PengOperation;
import com.rafo.chess.calculate.majiang.operation.impl.TingOperation;
import com.rafo.chess.calculate.majiang.operation.impl.ZhuaOperation;
import com.rafo.chess.model.Action;

/***
 * 杠后炮胡
 * 
 * @author Administrator
 * 
 */
public class TingPlugin extends AbstractPlugin<TingOperation> {
	@Override
	public void doOperation(TingOperation opt) {
		if (analysis(opt)) {
			payment(opt);
		}
	}

	@Override
	public IPluginConfig getConfig() {
		return config;
	}

	@Override
	public boolean analysis(TingOperation opt) {
		//记录玩家是否有抓拍的动作
		int count = 0;
		ArrayList<IOperation> list = opt.getOperationManager().getOperationList();
		for(int i = 0;i<opt.getStep();i++){
			IOperation optTemp = list.get(i);
			if(optTemp.getOperator().getUid()!=opt.getOperator().getUid())
				continue;
			if((optTemp instanceof ZhuaOperation))
				continue;
			count++;
		}
		int limit = Integer.parseInt(config.getConditionStr());
		return count==limit;
	}
}
