package com.bbzhu.system;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LockCenter {
	private Map<String, Object> map = new ConcurrentHashMap<String, Object>();
	private static LockCenter obj = new LockCenter();
	
	/**
	 * 全局锁对像
	 */
	public static LockCenter getInstance(){
		return obj;
	}
	
	/**
	 * 得到一个锁对像
	 */
	public Object getLock(String lockName){
		synchronized (map) {
			Object key = map.get(lockName);
			if(key != null){
				return key;
			}else{
				Object obj = new Object();
				map.put(lockName, obj);
				return obj;
			}
		}
	}
}
