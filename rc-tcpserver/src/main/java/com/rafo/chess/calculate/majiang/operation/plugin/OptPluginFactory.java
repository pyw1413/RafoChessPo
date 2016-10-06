package com.rafo.chess.calculate.majiang.operation.plugin;

import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;
import com.rafo.chess.resources.impl.PluginTemplateGen;

@SuppressWarnings("rawtypes")
public class OptPluginFactory {

	/***
	 * 实例化插件
	 * @param pluginConf
	 * @return
	 */
	public static IOptPlugin createOptPlugin(IPluginConfig pluginConf) {
		IOptPlugin plugin = null;
		PluginTemplateGen gen = pluginConf.getPluginTemplateGen();
		try {
			Class clazz = Class.forName(IPluginConfig.PLUGIN_CLASS_PATH + "."
					+ gen.getPluginClass());
			plugin = (IOptPlugin) clazz.newInstance();
			plugin.setConfig(pluginConf);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plugin;
	}
}
