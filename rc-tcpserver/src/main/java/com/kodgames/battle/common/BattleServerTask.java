package com.kodgames.battle.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class BattleServerTask
{
	Logger Logger = LoggerFactory.getLogger(BattleServerTask.class);

	public void run(long currentTime, int state)
	{
		run(state);
	}

	/**
	 * @param state 状态， 一次性task该值为0
	 */
	public abstract void run(int state);
}

