package com.rafo.chess.calculate.majiang.operation.config;

import com.rafo.chess.calculate.majiang.operation.condition.ICondition;
import com.rafo.chess.calculate.majiang.operation.effect.IEffect;
import com.rafo.chess.resources.impl.PluginTemplateGen;
@SuppressWarnings("unchecked")
public class PluginConfig implements IPluginConfig {
	PluginTemplateGen gen;

	public PluginConfig(PluginTemplateGen gen) {
		this.gen = gen;
	}

	@Override
	public int pluginId() {
		return gen.getTempId();
	}

	@Override
	public String pluginName() {
		return gen.getPluginName();
	}

	@Override
	public int getPlayerType() {
		return gen.getPlayType();
	}

	@Override
	public boolean inTheOperationFlag(int flag) {
		String[] flags = gen.getPluginOfFlag().split(",");
		for(String str: flags){
			if(Integer.parseInt(str)==flag)
				return true;
		}
		return false;
	}

	@Override
	public String getConditionStr() {
		return gen.getConditionStr();
	}

	@Override
	public String getIEffectStr() {
		return gen.getEffectStr();
	}

	@Override
	public PluginTemplateGen getPluginTemplateGen() {
		return gen;
	}

}
