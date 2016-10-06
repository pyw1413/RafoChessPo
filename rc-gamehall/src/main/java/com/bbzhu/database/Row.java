/**
  *行的处理
  */
package com.bbzhu.database;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.bbzhu.utils.BeanUtil;
import com.bbzhu.utils.Pojo;

public class Row{
	public Row(){
	}
	
	/**
	 * Vector中加入的对象是有序的，即按加入的顺序排列并且能够根据索引访问，用于储存key
	 */
	private Vector<String> ordering = new Vector<String>();
	private HashMap<String,String> map = new HashMap<String,String>();		//存放键值对（表中字段名称与字段值）
	
	public void put(String name, String value){								//向HashMap中追加键值对，即字段名称与字段值
		if(!map.containsKey(name)){											//判断是否与行中的字段名重复，如过不重复则添加
			ordering.addElement(name);										//将键保存起来
			map.put(name, value);											//将键值对添加进去
		}
	}

	/**
	 * 得到行对象中字段的个数
	 */
	public int length(){
		return map.size();
	}

	/**
	 * 根据字段名称取得字段值
	 */
	public Object get(String name){
		return map.get(name);
	}
	
	/**
	 * 根据字段名称取得字段值
	 */
	public String getString(String name){
		if(this.get(name) == null){
			return "";
		}
		return this.get(name).toString();		
	}
	
	/**
	 * 根据字段名称取得字段值
	 */
	public Integer getInteger(String name){
		return new Integer(this.getString(name));
	}
	
	/**
	 * 根据字段名称取得字段值
	 */
	public int getInt(String name){
		return this.getInteger(name).intValue();
	}
	
	/**
	 * 根据字段名称取得字段值
	 */
	public Date getDate(String name){
		return (Date)(this.get(name));
	}

	/**
	 * 根据字段在HashMap中的编号取得字段值
	 */
	public Object get(int which){
		String key = getKey(which);
		return map.get(key);
	}

	/**
	 * 根据字段序号取得字段名称
	 */
	public String getKey(int which){
		return (String)ordering.elementAt(which);
	}

	/**
	 * 获取所有字段名
	 */
	public Row getFieldNames(){
		Row fieldName = new Row();
		for (int i = 0; i < this.length(); i++){
			fieldName.put(new Integer(i).toString(), this.getKey(i).toString());
		}
		return fieldName;
	}

	/**
	 * 将行对象转换成Model对象
	 */
	public Pojo getPojo(Class c) throws Exception{
		Pojo pojo = (Pojo)c.newInstance();
		return BeanUtil.getInstance().getPojo(pojo, this.map);
	}
}