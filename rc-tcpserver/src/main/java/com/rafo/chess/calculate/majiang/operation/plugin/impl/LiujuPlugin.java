package com.rafo.chess.calculate.majiang.operation.plugin.impl;

import java.util.ArrayList;
import java.util.List;

import com.rafo.chess.calculate.majiang.card.MJCard;
import com.rafo.chess.calculate.majiang.operation.IOperation;
import com.rafo.chess.calculate.majiang.operation.OptPlayer;
import com.rafo.chess.calculate.majiang.operation.PayDetailed;
import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;
import com.rafo.chess.calculate.majiang.operation.effect.IEffect;
import com.rafo.chess.calculate.majiang.operation.impl.JiaozuiOperation;
import com.rafo.chess.calculate.majiang.operation.impl.ZhuaOperation;
import com.rafo.chess.calculate.majiang.operation.plugin.IOptPlugin;
import com.rafo.chess.model.AbstractDealer;
import com.rafo.chess.model.Action;
import com.rafo.chess.model.GameAction;
import com.rafo.chess.model.GamePlayer;
import com.rafo.chess.model.RecorderInfo;
import com.rafo.chess.resources.DataContainer;
import com.rafo.chess.resources.impl.PluginTemplateGen;

/***
 * 抓到指定牌触发执行
 * 
 * @author Administrator
 * 
 */
public class LiujuPlugin extends AbstractPlugin<IOperation> {
	@Override
	public void doOperation(IOperation opt) {
		if (analysis(opt)) {
			IOperation jiaozui = opt.getOperationManager().getOperationByStep(0);
			RecorderInfo recorderInfo = jiaozui.getRecorderInfo();
			List<PluginTemplateGen> list = (List<PluginTemplateGen>) DataContainer.getInstance().getListDataByName("pluginTemplateGen");
			for(PluginTemplateGen gen : list){
				int playType = opt.getRecorderInfo().getPlayType();
				int configIndex = Integer.parseInt(gen.getConditionStr());
				if(playType != configIndex)
					continue;
				
				//加番
				String[] strArr = gen.getEffectStr().split(",");
				int rate = Integer.parseInt(strArr[1]);
				PayDetailed pd = new PayDetailed();
				pd.setCard(opt.getRecorderInfo().getCard());
				pd.setPluginId(config.pluginId());
				pd.setRate(rate);
				pd.setStep(opt.getStep());
				pd.setToUid(opt.getOperator().getUid());
				ArrayList<Integer> fromsId = new ArrayList<Integer>();
				ArrayList<OptPlayer> players = opt.getOperationManager().getOptPlayers();
				for(OptPlayer op : players){
					if(op.getUid()==opt.getOperator().getUid())
						continue;
					if(op.isJiaozui())
						continue;
					fromsId.add(op.getUid());
				}
				int[] fromsIdInt = new int[fromsId.size()];
				for(int i=0;i<fromsIdInt.length;i++){
					fromsIdInt[i] = fromsId.get(i);
				}
				pd.setFromUid(fromsIdInt);
				opt.getOperationManager().addPayDetailed(pd);
			}
		}
			
	}

	@Override
	public IPluginConfig getConfig() {
		return config;
	}

	@Override
	public boolean analysis(IOperation opt) {
		// TODO 刘局判断.可以根据上游定义行为记录判断.也可以根据操作流有没有胡牌和判断
		boolean isLiuju = false;
		return opt.getOperator().isJiaozui()&&isLiuju;
	}

}
