package com.rafo.chess.resources.config.build;

import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rafo.chess.resources.DataContainer;
import com.rafo.chess.resources.config.build.interfaces.IBuilder;
import com.rafo.chess.resources.config.build.interfaces.IInitBean;
import com.rafo.chess.resources.define.BaseBean;
import com.rafo.chess.resources.define.DataConfigBean;
import com.rafo.chess.resources.define.DataConfigContainer;

public class DefaultBuilder implements IBuilder {

	@Override
	public <B extends BaseBean> void initBuild(String[][] data,
			Class<B> beanClass) throws Exception {
		List<B> list = new ArrayList<B>();
		String[] row;
		B b;
		for (int i = 7; i < data.length; i++) {
			row = data[i];
			b = beanClass.newInstance();
			if (b instanceof IInitBean) {
				((IInitBean) b).initBean(row);
				list.add(b);
			} else {
				throw new IllegalClassFormatException(String.format(
						"[%s] must implement IInitBean", beanClass.getName()));
			}
		}

		// 添加数据到容器中
		DataConfigBean dataConfigBean = DataConfigContainer.getInstance()
				.getDataConfigBean(beanClass.getName());
		if (dataConfigBean != null) {
			// 添加list数据
			DataContainer.getInstance().addListData(dataConfigBean.getName(),
					list);

			// 查找设置键值方法
			String getMethod = "get" + dataConfigBean.getIdColunm();
			Method method = null;
			Method[] methods = beanClass.getMethods();
			for (Method _tmp : methods) {
				if (_tmp.getName().equalsIgnoreCase(getMethod)) {
					method = _tmp;
					break;
				}
			}

			// 获取键值
			if (method != null) {
				// 循环获取键值，并设置
				String key;
				Map<String, Object> beans = new HashMap<String, Object>();
				for (Object bean : list) {
					key = String.valueOf(method.invoke(bean, new Object[0]));
					if (beans.containsKey(key)) {
						throw new IllegalArgumentException(String.format(
								"Bean[%s] ->row's key[%s] exsit!",
								beanClass.getName(), key));
					} else {
						beans.put(key, bean);
					}
				}

				DataContainer.getInstance().addMapData(dataConfigBean.getName(), beans);
			} else {
				throw new IllegalArgumentException(String.format(
						"Method[%s]'s not fount in class[%s]", getMethod,
						beanClass.getName()));
			}
		}

	}
	@Override
	public <B extends BaseBean> void initBuildGm(String[][] data,
			Class<B> beanClass) throws Exception {
		List<B> list = new ArrayList<B>();
		String[] row;
		B b;
		for (int i = 7; i < data.length; i++) {
			row = data[i];
			b = beanClass.newInstance();
			if (b instanceof IInitBean) {
				((IInitBean) b).initBean(row);
				list.add(b);
			} else {
				throw new IllegalClassFormatException(String.format(
						"[%s] must implement IInitBean", beanClass.getName()));
			}
		}

		// 添加数据到容器中
		DataConfigBean dataConfigBean = DataConfigContainer.getInstance()
				.getDataConfigBean(beanClass.getName());
		if (dataConfigBean != null) {
			// 添加list数据

			DataContainer.getInstance().removeMapData(dataConfigBean.getName());
			DataContainer.getInstance().addListData(dataConfigBean.getName(),
					list);

			// 查找设置键值方法
			String getMethod = "get" + dataConfigBean.getIdColunm();
			Method method = null;
			Method[] methods = beanClass.getMethods();
			for (Method _tmp : methods) {
				if (_tmp.getName().equalsIgnoreCase(getMethod)) {
					method = _tmp;
					break;
				}
			}

			// 获取键值
			if (method != null) {
				// 循环获取键值，并设置
				String key;
				Map<String, Object> beans = new HashMap<String, Object>();
				for (Object bean : list) {
					key = String.valueOf(method.invoke(bean, new Object[0]));
					if (beans.containsKey(key)) {
						throw new IllegalArgumentException(String.format(
								"Bean[%s] ->row's key[%s] exsit!",
								beanClass.getName(), key));
					} else {
						beans.put(key, bean);
					}
				}

				DataContainer.getInstance().addMapDataGm(
						dataConfigBean.getName(), beans);
			} else {
				throw new IllegalArgumentException(String.format(
						"Method[%s]'s not fount in class[%s]", getMethod,
						beanClass.getName()));
			}
		}

	}

	@Override
	public void lastBuild() throws Exception {

	}
}
