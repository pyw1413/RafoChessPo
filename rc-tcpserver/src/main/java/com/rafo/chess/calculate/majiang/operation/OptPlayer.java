package com.rafo.chess.calculate.majiang.operation;

import java.util.ArrayList;

import com.rafo.chess.model.GamePlayer;

public class OptPlayer {
	private GamePlayer gamePlayer;
	
	private boolean isJiaozui = false;
	
	private boolean isZerenji = false;
	
//	private ArrayList<PayDetailed> list = new ArrayList<PayDetailed>();

	
	
//	public ArrayList<PayDetailed> getList() {
//		return list;
//	}
	public int getUid(){
		return gamePlayer.getUid();
	}
	
	public GamePlayer getGamePlayer() {
		return gamePlayer;
	}
	public void setGamePlayer(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}
	public boolean isJiaozui() {
		return isJiaozui;
	}
	public void setJiaozui(boolean isJiaozui) {
		this.isJiaozui = isJiaozui;
	}
//	public void addPayDetailed(PayDetailed pay){
//		list.add(pay);
//	}
}
