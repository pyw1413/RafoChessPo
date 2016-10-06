package com.rafo.chess.resources.config.check.inerfaces;

public interface IChecker {
	/**
	 * 第一个校验数据
	 * 
	 * @param data
	 *            要校验的数据
	 * 
	 * @return 成功返回true
	 * 
	 * @throws Exception
	 *             校验出错时抛出错误
	 */
	public boolean checkData(String[][] data) throws Exception;
}
