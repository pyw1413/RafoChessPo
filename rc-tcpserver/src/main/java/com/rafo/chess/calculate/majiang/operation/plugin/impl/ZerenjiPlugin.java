package com.rafo.chess.calculate.majiang.operation.plugin.impl;

import java.util.ArrayList;
import com.rafo.chess.calculate.majiang.card.MJCard;
import com.rafo.chess.calculate.majiang.operation.IOperation;
import com.rafo.chess.calculate.majiang.operation.PayDetailed;
import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;
import com.rafo.chess.calculate.majiang.operation.impl.DaOperation;
import com.rafo.chess.calculate.majiang.operation.plugin.IOptPlugin;

/***
 * 抓到指定牌触发执行
 * 
 * @author Administrator
 * 
 */
public class ZerenjiPlugin extends AbstractPlugin {
	@Override
	public void doOperation(IOperation opt) {
		if (analysis(opt)) {
			// 取出上一步操作
			int stepTemp = -1;
			// 反向便利，找到当前牌的最近的打牌处理
			while ((stepTemp = (opt.getStep() - 1)) >= 0) {
				IOperation lastOpt = opt.getOperationManager()
						.getOperationByStep(stepTemp);
				if ((lastOpt instanceof DaOperation)
						&& opt.getRecorderInfo().getCard() == lastOpt
								.getRecorderInfo().getCard()) {
					ArrayList<PayDetailed> pdList = opt.getOperationManager()
							.getPayDetailedByStep(stepTemp);
//					ArrayList<PayDetailed> pdList = opt.getFireder().getList();
					
					// 如果有冲锋鸡，取消
					for (PayDetailed pd : pdList) {
						if (pd.getPluginId() == 2) {
							pd.setValid(false);
						}
					}
				}
			}
			//责任鸡流程
			String str = config.getIEffectStr();
			if (str == null || str.equals(""))
				return;
			int rate = Integer.parseInt(str);
			PayDetailed ratePay = new PayDetailed();
			ratePay.setCard(opt.getRecorderInfo().getCard());
			ratePay.setPluginId(config.pluginId());
			ratePay.setStep(opt.getStep());
			ratePay.setRate(rate);
			if(opt.getOperator().isJiaozui()){
				ratePay.setToUid(opt.getOperator().getGamePlayer().getUid());
				ratePay.setFromUid(new int[]{opt.getFireder().getGamePlayer().getUid()});
//				opt.getOperator().addPayDetailed(ratePay);
				opt.getOperationManager().addPayDetailed(ratePay);
			}else{
				ratePay.setToUid(opt.getFireder().getGamePlayer().getUid());
				ratePay.setFromUid(new int[]{opt.getOperator().getGamePlayer().getUid()});
//				opt.getOperator().addPayDetailed(ratePay);
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
		boolean res = false;
		String conditionStr = config.getConditionStr();
		String[] condArr = conditionStr.split(",");
		for(String s : condArr){
			if(s==null||s.equals(""))
				continue;
			if(Integer.parseInt(s)!=opt.getRecorderInfo().getCard())
				continue;
			res = true;
			break;
		}
		return res;
	}

}
