package com.rafo.chess.calculate.majiang.operation;

import java.util.ArrayList;

import com.rafo.chess.calculate.majiang.OperationManager;
import com.rafo.chess.calculate.majiang.card.MJCard;
import com.rafo.chess.calculate.majiang.operation.plugin.IOptPlugin;
import com.rafo.chess.model.GameAction;
import com.rafo.chess.model.GamePlayer;
import com.rafo.chess.model.RecorderInfo;

public abstract class OptAction implements IOperation{
	/**实施操作对象*/
	protected OptPlayer operator;
	/**点炮的对象,自己就是自摸*/
	protected OptPlayer fireder;
	/**操作的牌*/
	protected MJCard currentCard ;
	/**当前牌局的步数*/
	protected int step;
	
	protected OperationManager optManager;
	
	protected RecorderInfo recorderInfo;
	
	public OptAction(Integer step,MJCard currentCard){
		this.step = step;
		this.currentCard = currentCard;
	}
	

	public OperationManager getOptManager() {
		return optManager;
	}


	public void setOptManager(OperationManager optManager) {
		this.optManager = optManager;
	}



	public RecorderInfo getRecorderInfo() {
		return recorderInfo;
	}


	public void setRecorderInfo(RecorderInfo recorderInfo) {
		this.recorderInfo = recorderInfo;
	}


	public void setOperationManager(OperationManager optManager){
		this.optManager = optManager;
	}
	public  OperationManager getOperationManager(){
		return optManager;
	}
	

	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public OptPlayer getOperator() {
		return operator;
	}
	public void setOperator(OptPlayer operator) {
		this.operator = operator;
	}
	public OptPlayer getFireder() {
		return fireder;
	}
	public void setFireder(OptPlayer fireder) {
		this.fireder = fireder;
	}
	public MJCard getCurrentCard() {
		return currentCard;
	}
	public void setCurrentCard(MJCard currentCard) {
		this.currentCard = currentCard;
	}
	
	@SuppressWarnings({ "rawtypes,unchecked,rawtypes", "unchecked", "rawtypes" })
	@Override
	public void process() {
		ArrayList<IOptPlugin> pluginList = optManager.getOperationPluginList(this);
		if(pluginList==null)
			return;
		for(IOptPlugin plugin : pluginList){
			plugin.doOperation(this);
		}
	}
	
}
