package com.rafo.chess.calculate.majiang.operation;

import com.rafo.chess.calculate.majiang.OperationManager;
import com.rafo.chess.calculate.majiang.card.MJCard;
import com.rafo.chess.model.GameAction;
import com.rafo.chess.model.GamePlayer;
import com.rafo.chess.model.RecorderInfo;

/***
 * 麻将的操作对象
 * 
 * @author Administrator
 * 
 */
public interface IOperation {

	public void process();

	public void setOperationManager(OperationManager optManager);

	public OperationManager getOperationManager();

	public int getStep();

	public void setStep(int step);

	public OptPlayer getOperator();

	public void setOperator(OptPlayer operator);

	public OptPlayer getFireder();

	public void setFireder(OptPlayer fireder);

	public MJCard getCurrentCard();

	public void setCurrentCard(MJCard currentCard);
	public RecorderInfo getRecorderInfo() ;

	public void setRecorderInfo(RecorderInfo recorder) ;
}
