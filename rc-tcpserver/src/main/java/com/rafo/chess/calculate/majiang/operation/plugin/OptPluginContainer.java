package com.rafo.chess.calculate.majiang.operation.plugin;

import java.util.ArrayList;

import com.rafo.chess.calculate.majiang.operation.IOperation;

/**
 * 插件容器
 * @author Administrator
 */
public class OptPluginContainer{
	
	protected ArrayList<IOptPlugin> pluginList = new ArrayList<IOptPlugin>();
	
	public OptPluginContainer(){
		
	}
	public ArrayList<IOptPlugin> getPluginList(){
		return pluginList;
	}
}
