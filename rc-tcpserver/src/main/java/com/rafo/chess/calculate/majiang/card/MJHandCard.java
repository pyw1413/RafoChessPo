package com.rafo.chess.calculate.majiang.card;

import java.util.ArrayList;

public class MJHandCard {
	public ArrayList<MJCard> handCards = new ArrayList<MJCard>();
	public ArrayList<MJGroupCard> groups = new ArrayList<MJGroupCard>();
	
	public boolean isHU(){
		boolean isHU = true;
		for(int i =0;i<handCards.size();i++){
			for(int j = 1;j<handCards.size();j++){
				if(handCards.get(i)==handCards.get(j)){
					for(MJGroupCard group :groups){
						
					}
				}
			}
		}
		return isHU;
	}
	
	
}
