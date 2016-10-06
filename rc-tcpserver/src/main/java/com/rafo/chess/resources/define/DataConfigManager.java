package com.rafo.chess.resources.define;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import com.rafo.chess.resources.config.ann.DataDefine;
import com.rafo.chess.resources.config.build.interfaces.IBuilder;
import com.rafo.chess.resources.config.check.inerfaces.IChecker;
import com.rafo.chess.resources.config.load.interfaces.ILoader;


public class DataConfigManager {
	// 日志
	private Logger log = Logger.getLogger(DataConfigManager.class);
	private static DataConfigManager dataConfigManager = null;
	private Map<String, HSSFSheet> xlsMap;
	public static String dataPath = null;
	public static String resourcePath = null;
	

	public static DataConfigManager getInstance() {
		if (dataConfigManager == null) {
			synchronized (DataConfigManager.class) {
				dataConfigManager = new DataConfigManager();
			}
		}
		return dataConfigManager;
	}
	
	public Map<String, HSSFSheet> getXlsMap() {
		return xlsMap;
	}

	public void setXlsMap(Map<String, HSSFSheet> xlsMap) {
		this.xlsMap = xlsMap;
	}

	private DataConfigManager() {
		this.xlsMap = new HashMap<String, HSSFSheet>();
	}

	/**
	 * 加载基础数据配置
	 */
	public void loadDataConfig() {
		DataConfigContainer.getInstance().clear();
		File msgPath = new File(dataPath);
		this.initPath(msgPath);
	}

	/**
	 * 加载所有基础数据
	 */
	public void loadData() {
		Set<String> beanClassNames = DataConfigContainer.getInstance()
				.getAllDataConfigBean();
		for (String beanClassName : beanClassNames) {
			this.loadData(beanClassName);
		}
	}

	/**
	 * 加载单个数据，指定要加载bean Class
	 * 
	 * @param beanClassName
	 *            要加载bean class
	 */
	public void loadData(String beanClassName) {
		DataConfigBean dataConfigBean = DataConfigContainer.getInstance()
				.getDataConfigBean(beanClassName);
		if (dataConfigBean != null) {
			log.info(String
					.format("Load Data:File[%s] sheetName[%s] Bean[%s] Loader[%s] Builder[%s] Checker[%s]",
							dataConfigBean.getConfigFileName(), dataConfigBean
									.getSheetFileName(), dataConfigBean
									.getBeanClass().getName(), dataConfigBean
									.getLoaderClass().getName(), dataConfigBean
									.getBuildClass().getName(), dataConfigBean
									.getCheckClass().getName()));

			try {
				// 读取数据
				ILoader<?, ?> loader = dataConfigBean.getLoaderClass()
						.newInstance();
				 String path =resourcePath+dataConfigBean.getConfigFileName();
				// Instances.getAppConfigBean().getCurrentPath()+Instances.getAppConfigBean().getServerPath()+dataConfigBean.getConfigFileName();
//				String path = AppConfigBean.resourcePath
//						+ dataConfigBean.getConfigFileName();
				String[][] dataOriginal = loader.loadConfig(path,
						dataConfigBean.getSheetFileName());

				// 校验数据
				IChecker checker = dataConfigBean.getCheckClass().newInstance();
				boolean back = checker.checkData(dataOriginal);
				if (!back) {
					throw new IllegalArgumentException(
							String.format(
									"Check Data Error:File[%s] sheetName[%s] Bean[%s] Loader[%s] Builder[%s] Checker[%s]",
									dataConfigBean.getConfigFileName(),
									dataConfigBean.getSheetFileName(),
									dataConfigBean.getBeanClass().getName(),
									dataConfigBean.getLoaderClass().getName(),
									dataConfigBean.getBuildClass().getName(),
									dataConfigBean.getCheckClass().getName()));
				}

				// 构建数据
				IBuilder builder = dataConfigBean.getBuildClass().newInstance();
				builder.initBuild(dataOriginal, dataConfigBean.getBeanClass());
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Load data failure! beanClassName=" + beanClassName,
						e);
				System.exit(0);
			}
		}
	}

	/**
	 * 加载单个数据，指定要加载bean Class
	 * 
	 * @param beanClassName
	 *            要加载bean class
	 */
	public void loadDataGm(String beanClassName) {
		DataConfigBean dataConfigBean = DataConfigContainer.getInstance()
				.getDataConfigBean(beanClassName);
		if (dataConfigBean != null) {
			log.info(String
					.format("Load Data:File[%s] sheetName[%s] Bean[%s] Loader[%s] Builder[%s] Checker[%s]",
							dataConfigBean.getConfigFileName(), dataConfigBean
									.getSheetFileName(), dataConfigBean
									.getBeanClass().getName(), dataConfigBean
									.getLoaderClass().getName(), dataConfigBean
									.getBuildClass().getName(), dataConfigBean
									.getCheckClass().getName()));

			try {
				// 读取数据
				ILoader<?, ?> loader = dataConfigBean.getLoaderClass()
						.newInstance();
				String[][] dataOriginal = loader.loadConfig(
						dataConfigBean.getConfigFileName(),
						dataConfigBean.getSheetFileName());

				// 校验数据
				IChecker checker = dataConfigBean.getCheckClass().newInstance();
				boolean back = checker.checkData(dataOriginal);
				if (!back) {
					throw new IllegalArgumentException(
							String.format(
									"Check Data Error:File[%s] sheetName[%s] Bean[%s] Loader[%s] Builder[%s] Checker[%s]",
									dataConfigBean.getConfigFileName(),
									dataConfigBean.getSheetFileName(),
									dataConfigBean.getBeanClass().getName(),
									dataConfigBean.getLoaderClass().getName(),
									dataConfigBean.getBuildClass().getName(),
									dataConfigBean.getCheckClass().getName()));
				}

				// 构建数据
				IBuilder builder = dataConfigBean.getBuildClass().newInstance();
				builder.initBuildGm(dataOriginal, dataConfigBean.getBeanClass());
			} catch (Exception e) {
				log.error("  GM Load data failure! beanClassName="
						+ beanClassName, e);
			}
		}
	}

	/**
	 * 最后校验数据
	 * 
	 * @param beanClassName
	 *            要校验bean class
	 */
	public void lastBuild(String beanClassName) {
		// DataConfigBean dataConfigBean = Instances.getDataConfigContainer()
		// .getDataConfigBean(beanClassName);
		// if (dataConfigBean != null) {
		//
		// }
	}

	private void initPath(File path) {
		if (path.isDirectory()) {
			File[] files = path.listFiles();
			for (File file : files) {
				this.initPath(file);
			}
		} else {
			this.initFile(path);
		}
	}

	@SuppressWarnings("unchecked")
	private void initFile(File file) {
		String path = file.getAbsolutePath();
		int start = path.indexOf("com");
		if (start != -1) {
			String className = path.substring(start);
			if (className.endsWith(".class")) {
				className = className.substring(0, className.length() - 6);
			}
			className = className.replaceAll("/", ".");
			className = className.replaceAll("\\\\", ".");
			try {
				@SuppressWarnings("rawtypes")
				Class clazz = Class.forName(className);
				if (clazz.isAnnotationPresent(DataDefine.class)) {
					DataDefine dataDefine = (DataDefine) clazz
							.getAnnotation(DataDefine.class);
					// 初始化配置bean
					DataConfigBean dataConfigBean = new DataConfigBean();
					dataConfigBean.setBeanClass(clazz);
					dataConfigBean.setBuildClass(dataDefine.buildClass());
					dataConfigBean.setCanUse(dataDefine.canUse());
					dataConfigBean.setCheckClass(dataDefine.checkClass());
					dataConfigBean.setConfigFileName(dataDefine
							.configFileName());
					dataConfigBean.setIdColunm(dataDefine.idColunm());
					dataConfigBean.setName(dataDefine.name());
					dataConfigBean.setSheetFileName(dataDefine.sheetFileName());
					// 保存配置
					DataConfigContainer.getInstance().addDataConfig(
							dataConfigBean);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				log.error(String.format("Class[%s] not found", path), e);
				System.exit(1);
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void initClassBean(String name) {
		int start = name.indexOf("com");
		if (start != -1) {
			String className = name.substring(start);
			if (className.endsWith(".class")) {
				className = className.substring(0, className.length() - 6);
			}
			className = className.replaceAll("/", ".");
			className = className.replaceAll("\\\\", ".");
			try {
				@SuppressWarnings("rawtypes")
				Class clazz = Class.forName(className);
				if (clazz.isAnnotationPresent(DataDefine.class)) {
					DataDefine dataDefine = (DataDefine) clazz
							.getAnnotation(DataDefine.class);
					// 初始化配置bean
					DataConfigBean dataConfigBean = new DataConfigBean();
					dataConfigBean.setBeanClass(clazz);
					dataConfigBean.setBuildClass(dataDefine.buildClass());
					dataConfigBean.setCanUse(dataDefine.canUse());
					dataConfigBean.setCheckClass(dataDefine.checkClass());
					dataConfigBean.setConfigFileName(dataDefine
							.configFileName());
					dataConfigBean.setIdColunm(dataDefine.idColunm());
					dataConfigBean.setName(dataDefine.name());
					dataConfigBean.setSheetFileName(dataDefine.sheetFileName());
					// 保存配置
					DataConfigContainer.getInstance().addDataConfig(
							dataConfigBean);
				}
			} catch (ClassNotFoundException e) {
				log.error(String.format("Class[%s] not found", name), e);
				System.exit(1);
			}
		}
	}
}
