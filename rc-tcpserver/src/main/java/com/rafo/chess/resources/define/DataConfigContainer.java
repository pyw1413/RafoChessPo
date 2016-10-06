package com.rafo.chess.resources.define;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.rafo.chess.resources.define.DataConfigBean;
import com.rafo.chess.resources.define.DataConfigContainer;

public class DataConfigContainer {
	private static DataConfigContainer dataConfigContainer = null;

	// 消息容器
	private Map<String, DataConfigBean> beansConfig = new HashMap<String, DataConfigBean>();

	private DataConfigContainer() {
	}

	public static DataConfigContainer getInstance() {
		if (dataConfigContainer == null) {
			dataConfigContainer = new DataConfigContainer();
		}
		return dataConfigContainer;
	}

	/**
	 * 添加数据配置
	 * 
	 * @param dataConfigBean
	 *            数据配置bean
	 */
	public void addDataConfig(DataConfigBean dataConfigBean) {
		beansConfig
				.put(dataConfigBean.getBeanClass().getName(), dataConfigBean);
	}

	/**
	 * 根据消息id获取配置bean
	 * 
	 * @param beanClassName
	 *            bean的配置信息
	 * 
	 * @return 数据配置bean
	 */
	public DataConfigBean getDataConfigBean(String beanClassName) {
		return beansConfig.get(beanClassName);
	}

	/**
	 * 获取所有需要加载数据的配置名
	 * 
	 * @return 返回所有数据的配置名
	 */
	public Set<String> getAllDataConfigBean() {
		return beansConfig.keySet();
	}

	public void clear() {
		beansConfig.clear();
	}
}
