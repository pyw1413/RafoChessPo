package com.rafo.chess.calculate.majiang.operation.config;

import com.rafo.chess.resources.impl.PluginTemplateGen;


/**
 * 插件的配置对象
 * 
 * @author Administrator
 */
public interface IPluginConfig {
	
	public static final String PLUGIN_CONDITION_CLASS_PATH = "com.rafo.chess.calculate.majiang.operation.config";
	public static final String PLUGIN_EFFECT_CLASS_PATH = "com.rafo.chess.calculate.majiang.operation.effect";
	public static final String PLUGIN_CLASS_PATH = "com.rafo.chess.calculate.majiang.operation.plugin.impl";

	public PluginTemplateGen getPluginTemplateGen();
	
	public int pluginId();
	
	public String pluginName();
	
	public int getPlayerType();
	
	public boolean inTheOperationFlag(int flag);

	public String getConditionStr();

	public String getIEffectStr();

}
