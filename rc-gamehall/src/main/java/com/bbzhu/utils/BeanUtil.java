package com.bbzhu.utils;

import java.lang.reflect.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.bbzhu.system.InvokeCenter;
import com.bbzhu.system.LockCenter;
import javazoom.upload.MultipartFormDataRequest;

public class BeanUtil {
	//记录字段的类型
	private Map<String, Map<String, String>> tableMap = new HashMap<String, Map<String,String>>();
	private static BeanUtil instance = null;
	
	public static BeanUtil getInstance() {
		synchronized (LockCenter.getInstance().getLock("BeanUtil")) {
			if (instance == null) {
				instance = new BeanUtil();
			}
			return instance;
		}
	}
	
	private BeanUtil(){
		
	}
	
	public String getShortName(String longName){
		if(longName.equals("class java.lang.String")){
			return "String";
		}else if(longName.equals("class java.util.Date")){
			return "Date";
		}else if(longName.equals("class java.lang.Double")){
			return "Double";
		}else if(longName.equals("class java.lang.Long")){
			return "Long";
		}else{
			return "Int";
		}
	}
	
	public void init(Pojo pojo){
		synchronized (tableMap) {
			Map<String,String> fieldMap = tableMap.get(pojo.getTableName());
	    	if(fieldMap == null){
	    		fieldMap = new HashMap<String,String>();
	    		tableMap.put(pojo.getTableName(), fieldMap);
	    		try {
	    			Field f[] = Class.forName("com.www.model.base.Base"+pojo.getClassName()).getDeclaredFields();
		    		for(int i = 0 ; i < f.length ; i++){
		    			if(!Modifier.isTransient(f[i].getModifiers()) && !Modifier.isStatic(f[i].getModifiers())){
		    				fieldMap.put(f[i].getName(), getShortName(f[i].getType().toString()));
		    			}
		    		}
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
		}
	}
	
	public String getFieldType(Pojo pojo, String fieldName){
		init(pojo);
		return tableMap.get(pojo.getTableName()).get(fieldName);
	}
	
    public Object getProperty(Pojo pojo, String fieldName){
    	return InvokeCenter.getInstance().getPropertyInvoke(pojo, pojo.getClassName(), fieldName);
    }
    
    public String getValue(Pojo pojo, String fieldName){
    	String fieldType = this.getFieldType(pojo, fieldName);
    	Object fieldValue = this.getProperty(pojo, fieldName);
    	
    	return this.getValue(fieldValue, fieldType);
    }
    
    public String getValue(Object fieldValue, String fieldType){
    	if(fieldType.equals("Date")){
    		if(fieldValue == null)
    			return null;
    		else
    			return "'" + Common.FormatFullDate((Date)fieldValue) + "'";
    	}else if(fieldType.equals("String")){
    		if(fieldValue == null)
    			return "''";
    		else
    			return "'" + Common.format(fieldValue.toString()).replace("'", "''") + "'";
    	}else{
    		if(fieldValue == null)
    			return "0";
    		else
    			return Common.format(fieldValue.toString());
    	}
    }
    
    public Object getObject(String fieldValue, String fieldType){
    	if(fieldValue == null)
    		return null;
    	
    	try{
	    	if(fieldType.equals("Date")){
	    		if(Common.isDate(fieldValue))
	    			return Common.FormatDate(fieldValue);
	    		else
	    			return Common.FormatFullDate(fieldValue);
	    	}else if(fieldType.equals("Double")){
	    		return Double.parseDouble(fieldValue);
	    	}else if(fieldType.equals("Int")){
	    		return Integer.parseInt(fieldValue);
	    	}else if(fieldType.equals("Long")){
	    		return Long.parseLong(fieldValue);
	    	}else{
	    		return fieldValue;
	    	}
    	}catch(Exception ex){
    		return null;
    	}
    }
    
    public void setProperty(Pojo pojo, String fieldName, Object fieldValue){
    	InvokeCenter.getInstance().setPropertyInvoke(pojo, pojo.getClassName(), fieldName, fieldValue);
    }
    
    public String buildInsert(Pojo pojo){
    	init(pojo);
    	Map<String,String> fieldMap = tableMap.get(pojo.getTableName());
    	
    	String key = pojo.getKey();
    	boolean auto_increment = pojo.getAutoIncrement();
    	
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ").append(pojo.getTableName()).append("(");
		StringBuffer sb2 = new StringBuffer(" values(");
		
		for(String fieldName:fieldMap.keySet()){
			String value = this.getValue(pojo, fieldName);
			if(!(auto_increment && fieldName.equals(key) && value.equals("0"))){
				sb.append(fieldName).append(",");
				sb2.append(value).append(",");
			}
		}
		
		sb.setCharAt(sb.length() - 1, ')');
		sb2.setCharAt(sb2.length() - 1, ')');
		
		return sb.append(sb2).toString();
	}
    
    public String buildUpdate(Pojo pojo){
    	init(pojo);
    	Map<String,String> fieldMap = tableMap.get(pojo.getTableName());
    	String key = pojo.getKey();
    	
		StringBuffer sb = new StringBuffer();
		sb.append("update ").append(pojo.getTableName()).append(" set ");

		for(String fieldName:fieldMap.keySet()){
			if(!fieldName.equals(key)){
				Object fieldValue = this.getProperty(pojo, fieldName);
				if(fieldValue != null){
					sb.append(fieldName).append("=").append(this.getValue(fieldValue, this.getFieldType(pojo, fieldName))).append(",");
				}
			}
		}
		
		sb.setCharAt(sb.length()-1, ' ');
		
		sb.append("where ").append(key).append("=").append(this.getValue(pojo, key));
		
		return sb.toString();
	}
    
    public String buildDelete(Pojo pojo){
    	init(pojo);
    	String key = pojo.getKey();
    	
		StringBuffer sb = new StringBuffer();
		sb.append("delete from ").append(pojo.getTableName()).append(" where ").append(key).append("=").append(this.getValue(pojo, key));
		
		return sb.toString();
    }
    
    public Pojo getPojo(Pojo pojo, HttpServletRequest request, Integer itype){
    	init(pojo);
    	Map<String,String> fieldMap = tableMap.get(pojo.getTableName());
    	
    	for(String fieldName:fieldMap.keySet()){
    		String fieldValue = request.getParameter(fieldName);
    		if(fieldValue == null && itype == 0)
    			continue;
    		this.setProperty(pojo, fieldName, this.getObject(fieldValue, this.getFieldType(pojo, fieldName)));
    	}
    	
    	return pojo;
    }
    
    public Pojo getPojo(Pojo pojo, MultipartFormDataRequest request, Integer itype){
    	init(pojo);
    	Map<String,String> fieldMap = tableMap.get(pojo.getTableName());
    	
    	for(String fieldName:fieldMap.keySet()){
    		String fieldValue = request.getParameter(fieldName);
    		if(fieldValue == null && itype == 0)
    			continue;
    		this.setProperty(pojo, fieldName, this.getObject(fieldValue, this.getFieldType(pojo, fieldName)));
    	}
    	
    	return pojo;
    }
    
    public Pojo getPojo(Pojo pojo, Map<String,String> map){
    	init(pojo);
    	Map<String,String> fieldMap = tableMap.get(pojo.getTableName());

    	for(String fieldName:fieldMap.keySet()){
    		this.setProperty(pojo,fieldName, this.getObject(map.get(fieldName),fieldMap.get(fieldName)));
    	}
    	
    	return pojo;
    }
}
