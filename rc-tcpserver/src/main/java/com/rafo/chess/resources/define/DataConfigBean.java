package com.rafo.chess.resources.define;

import com.rafo.chess.resources.config.build.DefaultBuilder;
import com.rafo.chess.resources.config.build.interfaces.IBuilder;
import com.rafo.chess.resources.config.check.DefaultChecker;
import com.rafo.chess.resources.config.check.inerfaces.IChecker;
import com.rafo.chess.resources.config.load.ExcelLoader;
import com.rafo.chess.resources.config.load.interfaces.ILoader;
import com.rafo.chess.resources.define.BaseBean;

public class DataConfigBean {
	// 配置文件的名字
	private String configFileName = null;

	// 配置文件的sheet，如果使用CVS，留空
	private String sheetFileName = null;

	// 数据名字，即sheet名
	private String name = null;

	// 作为id的列名
	private String idColunm = null;

	// 默认加载器
	@SuppressWarnings("rawtypes")
	Class<? extends ILoader> loaderClass = ExcelLoader.class;

	// 默认构建器
	private Class<? extends IBuilder> buildClass = DefaultBuilder.class;

	// 默认校验器
	private Class<? extends IChecker> checkClass = DefaultChecker.class;

	// Bean
	private Class<? extends BaseBean> beanClass = null;

	// 是否启用
	private boolean canUse = true;

	public String getConfigFileName() {
		return configFileName;
	}

	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	public String getSheetFileName() {
		return sheetFileName;
	}

	public void setSheetFileName(String sheetFileName) {
		this.sheetFileName = sheetFileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdColunm() {
		return idColunm;
	}

	public void setIdColunm(String idColunm) {
		this.idColunm = idColunm;
	}

	public Class<? extends IBuilder> getBuildClass() {
		return buildClass;
	}

	public void setBuildClass(Class<? extends IBuilder> buildClass) {
		this.buildClass = buildClass;
	}

	public Class<? extends IChecker> getCheckClass() {
		return checkClass;
	}

	public void setCheckClass(Class<? extends IChecker> checkClass) {
		this.checkClass = checkClass;
	}

	public Class<? extends BaseBean> getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class<? extends BaseBean> beanClass) {
		this.beanClass = beanClass;
	}

	public boolean isCanUse() {
		return canUse;
	}

	public void setCanUse(boolean canUse) {
		this.canUse = canUse;
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends ILoader> getLoaderClass() {
		return loaderClass;
	}

	@SuppressWarnings("rawtypes")
	public void setLoaderClass(Class<? extends ILoader> loaderClass) {
		this.loaderClass = loaderClass;
	}

}
