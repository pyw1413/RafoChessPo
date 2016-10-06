package com.rafo.chess.calculate.majiang.card;

import com.rafo.chess.calculate.majiang.operation.OperationEnum;

/***
 * //花色: 1万 条 筒 1xx 2xx 3xx   东南西北 10 11 12 13 中发白 14 15 16
 * @author Administrator
 *
 */
public class MJCard {
	private int number;
	private OperationEnum opt;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public OperationEnum getOpt() {
		return opt;
	}

	public void setOpt(OperationEnum opt) {
		this.opt = opt;
	}
	
	
	
}
