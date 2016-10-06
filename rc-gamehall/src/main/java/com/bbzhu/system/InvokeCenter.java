package com.bbzhu.system;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bbzhu.utils.Pojo;

public class InvokeCenter {
	private Map<String, ConcurrentHashMap<String,Method>> mapMethod = new ConcurrentHashMap<String, ConcurrentHashMap<String,Method>>();
	private Map<String, Object> mapClass = new ConcurrentHashMap<String, Object>();
	private static InvokeCenter InvokeCenterInstance = null;
	
	public static InvokeCenter getInstance() {
		synchronized (LockCenter.getInstance().getLock("InvokeCenter")) {
			if (InvokeCenterInstance == null) {
				InvokeCenterInstance = new InvokeCenter();
			}
			return InvokeCenterInstance;
		}
	}
	
	private InvokeCenter(){
		
	}

	private Object getClass(String className) throws Exception{
		if(!className.startsWith("com"))
			className = "com.www.model." + className;
		
		if(mapMethod.get(className) == null){
			Object obj = Class.forName(className).newInstance();
			mapClass.put(className, obj);
			ConcurrentHashMap<String,Method> map = new ConcurrentHashMap<String,Method>();
			Method[] methods = obj.getClass().getMethods();
			for (int j = 0; j < methods.length; j++) {
				Method method = methods[j];
				map.put(method.getName(),method);
			}
			mapMethod.put(className, map);
		}
		
		return mapClass.get(className);
	}
	
	private Method getMethod(String className,String methodName){
		if(!className.startsWith("com"))
			className = "com.www.model." + className;
		
		return mapMethod.get(className).get(methodName);
	}
	
	public Pojo getPojo(String className){
		try {
			return (Pojo)this.getClass(className);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//用于处理Action的跳转
	public String ActionInvoke(String className,String methodName,HttpServletRequest req,HttpServletResponse rep){
		className = "com.www.action." + className.substring(0,1).toUpperCase() + className.substring(1) + "Action";
		try {
			Object obj = this.getClass(className);
			return (String)this.getMethod(className, methodName).invoke(obj, new Object[]{req,rep});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//用于获取Model的属性
	public Object getPropertyInvoke(Pojo pojo, String className, String fieldName){
		String methodName = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
		try{
			this.getClass(className);
			return this.getMethod(className, methodName).invoke(pojo);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//用户设置Model的属性
	public void setPropertyInvoke(Pojo pojo, String className, String fieldName, Object fieldValue){
		String methodName = "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
		try{
			this.getClass(className);
			this.getMethod(className, methodName).invoke(pojo, fieldValue);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
