package com.rafo.chess.calculate.majiang.operation.plugin;

import com.rafo.chess.calculate.majiang.operation.IOperation;
import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;

public interface IOptPlugin<O extends IOperation> {
	/**执行这个插件的操作*/
	public void doOperation(O opt);
	/**插件的配置*/
	public IPluginConfig getConfig();
	
	public void setConfig(IPluginConfig pluginConf);
	
	public boolean analysis(O opt);
	
}
