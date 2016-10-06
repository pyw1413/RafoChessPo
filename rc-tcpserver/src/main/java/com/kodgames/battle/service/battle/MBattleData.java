package com.kodgames.battle.service.battle;

import java.util.ArrayList;

public class MBattleData 
{
	private ArrayList<MBattleStep> battleSteps = new ArrayList<MBattleStep>();

	public ArrayList<MBattleStep> getBattleSteps() {
		return battleSteps;
	}

	public void setBattleSteps(ArrayList<MBattleStep> battleSteps) {
		this.battleSteps = battleSteps;
	}
	
	
}
