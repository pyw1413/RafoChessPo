package com.rafo.chess.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.rafo.chess.core.ROCHExtension;
import com.rafo.chess.resources.define.BaseBean;
import com.rafo.chess.resources.define.DataConfigManager;


public class DataContainer {
	// 日志
	private Logger log = Logger.getLogger(this.getClass());
	/**
	 * 统一的数据Map容器
	 * key:对象名
	 * value：map<id,BaseBean>
	 */
	private Map<String, Map<String, Object>> mapDatas = null;

	/**
	 * 统一的数据list容器
	 */
	private Map<String, List<? extends BaseBean>> listDatas = null;

	private static DataContainer dataContainer = null;

	public static DataContainer getInstance() {
		if (dataContainer == null) {
			dataContainer = new DataContainer();
		}
		return dataContainer;
	}

	public void init(String dataPath,String resourcePath){
		DataConfigManager.getInstance().dataPath = dataPath;
		DataConfigManager.getInstance().resourcePath=resourcePath;
		log.info("Loading App's  data config...");
		DataConfigManager.getInstance().loadDataConfig();
		// 加载基础数据
		log.info("Loading App's data...");
		DataConfigManager.getInstance().loadData();
	}
	
	
	public void init(ROCHExtension extension){
		Properties props = extension.getConfigProperties();
		// 加载基础数据配置
		String path = props.getProperty("");
		String resourcePath = props.getProperty("");
		init(path,resourcePath);
	}
	
	private DataContainer() {
		if (mapDatas == null) {
			mapDatas = new HashMap<String, Map<String, Object>>();
		}

		if (listDatas == null) {
			listDatas = new HashMap<String, List<? extends BaseBean>>();
		}
	}

	public Map<String, Object> getMapDataByName(String name) {
		return this.mapDatas.get(name.toString());
	}

	public List<? extends BaseBean> getListDataByName(String name) {
		return this.listDatas.get(name.toString());
	}

	public void addMapData(String name, Map<String, Object> data) {
		if (this.mapDatas.containsKey(name)) {
			throw new IllegalArgumentException(String.format("Same name:[%s]",
					name));
		}

		this.mapDatas.put(name, data);
	}
	public void addMapDataGm(String name, Map<String, Object> data) {
		this.mapDatas.put(name, data);
	}

	public void addListData(String name, List<? extends BaseBean> data) {
		if (this.mapDatas.containsKey(name)) {
			throw new IllegalArgumentException(String.format("Same name:[%s]",
					name));
		}

		this.listDatas.put(name, data);
	}
	
	public void removeMapData(String name) {
		this.mapDatas.remove(name);
	}
}
