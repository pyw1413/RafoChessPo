package com.rafo.chess.resources.impl;

import com.rafo.chess.resources.config.ann.DataDefine;
import com.rafo.chess.resources.config.build.interfaces.IInitBean;
import com.rafo.chess.resources.define.BaseBean;

//public class PluginTemplateGen implements ITemplateGen{
@DataDefine(configFileName = "/pluginTemplateGen.xls", idColunm = "tempId", name = "pluginTemplateGen", sheetFileName = "pluginTemplateGen")
public class PluginTemplateGen extends BaseBean implements IInitBean {
	private int tempId = 0;
	private String pluginName;
	/** 哪个操作的插件 */
	private String pluginOfFlag;
	/** 插件执行的判定条件 */
	private String conditionStr;
	/** 插件执行的判定条件的类 */
	private String pluginClass;
	/** 插件执行的效果 */
	private String effectStr;
	/** 玩法 0贵阳麻将 */
	private int playType = 0;

	private int zimoFlag = 0;
	private int dianedFlag = 0;
	private int zimoedFlag = 0;
	private int dianFlag = 0;

	public int getZimoFlag() {
		return zimoFlag;
	}

	public void setZimoFlag(int zimoFlag) {
		this.zimoFlag = zimoFlag;
	}

	public int getDianedFlag() {
		return dianedFlag;
	}

	public void setDianedFlag(int dianedFlag) {
		this.dianedFlag = dianedFlag;
	}

	public int getZimoedFlag() {
		return zimoedFlag;
	}

	public void setZimoedFlag(int zimoedFlag) {
		this.zimoedFlag = zimoedFlag;
	}

	public int getDianFlag() {
		return dianFlag;
	}

	public void setDianFlag(int dianFlag) {
		this.dianFlag = dianFlag;
	}

	public int getPlayType() {
		return playType;
	}

	public void setPlayType(int playType) {
		this.playType = playType;
	}

	public int getTempId() {
		return tempId;
	}

	public void setTempId(int tempId) {
		this.tempId = tempId;
	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public String getPluginOfFlag() {
		return pluginOfFlag;
	}

	public void setPluginOfFlag(String pluginOfFlag) {
		this.pluginOfFlag = pluginOfFlag;
	}

	public String getConditionStr() {
		return conditionStr;
	}

	public void setConditionStr(String conditionStr) {
		this.conditionStr = conditionStr;
	}

	public String getEffectStr() {
		return effectStr;
	}

	public void setEffectStr(String effectStr) {
		this.effectStr = effectStr;
	}

	public String getPluginClass() {
		return pluginClass;
	}

	public void setPluginClass(String pluginClass) {
		this.pluginClass = pluginClass;
	}

	@Override
	public void initBean(String[] data) {
		if (data[0] != null && !"".equals(data[0].trim())) {
			this.tempId = Integer.parseInt(data[0]);
		}
		pluginName = data[1];
		/** 哪个操作的插件 */
		this.pluginOfFlag = data[2];
		pluginClass = data[3];
		/** 插件执行的判定条件 */
		conditionStr = data[4];
		/** 插件执行的效果 */
		effectStr = data[5];
		if (data[6] != null && !"".equals(data[6].trim())) {
			this.playType = Integer.parseInt(data[6]);
		}
		if (data[7] != null && !"".equals(data[7].trim())) {
			this.zimoFlag = Integer.parseInt(data[7]);
		}
		if (data[8] != null && !"".equals(data[8].trim())) {
			this.dianedFlag = Integer.parseInt(data[8]);
		}
		if (data[9] != null && !"".equals(data[9].trim())) {
			this.zimoedFlag = Integer.parseInt(data[9]);
		}
		if (data[10] != null && !"".equals(data[10].trim())) {
			this.dianFlag = Integer.parseInt(data[10]);
		}
	}

	@Override
	public byte[] serialization() {
		return null;
	}

	@Override
	public <T> T unSerialization(byte[] data) {
		return null;
	}
}
