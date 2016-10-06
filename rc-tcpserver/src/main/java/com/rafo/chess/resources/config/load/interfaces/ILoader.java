package com.rafo.chess.resources.config.load.interfaces;

public interface ILoader<K,V> {

	public String[][] loadConfig(String configPath, String sheetName)
			throws Exception;
}
