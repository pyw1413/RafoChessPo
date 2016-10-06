package com.bbzhu.database;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.bbzhu.system.LockCenter;
import com.mchange.v2.c3p0.DataSources;

public class DatabaseConn{
	private static Map<Integer, DataSource> map = new HashMap<Integer, DataSource>();
	private static DatabaseConn instance = null;
	
	public static DatabaseConn getInstance(){
		synchronized (LockCenter.getInstance().getLock("DatabaseConn")) {
			if(instance == null)
				instance = new DatabaseConn();
		}
		
		return instance;
	}
	
	private DatabaseConn(){
		
	}
	
	public synchronized Connection getConnection(Integer id){
		Connection conn=null;
		try{														
			if(map.get(id) != null)
				conn = map.get(id).getConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	
	public Connection getConnection(){
		return getConnection(0);
	}

	public synchronized void addDataSource(Integer id, String driver, String url, String uid,String pwd){
		if(map.get(id) == null){
			try{
				Class.forName(driver);
				DataSource ds_unpooled = DataSources.unpooledDataSource(url, uid, pwd);
				Map<String,String> overrides = new HashMap<String,String>();
				overrides.put("acquireIncrement", "2");					//当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。Default: 3
				overrides.put("acquireRetryAttempts", "30");			//定义在从数据库获取新连接失败后重复尝试的次数。Default: 30
				overrides.put("acquireRetryDelay", "1000");				//两次连接中间隔时间，单位毫秒。Default: 1000
				overrides.put("autoCommitOnClose", "false");			//连接关闭时默认将所有未提交的操作回滚。Default: false
				overrides.put("breakAfterAcquireFailure", "false");		//获取连接失败后该数据源将申明已断开并永久关闭。Default: false
				overrides.put("checkoutTimeout", "30000");				//当连接池用完时客户端调用getConnection()后等待获取新连接的时间，超时后将抛出SQLException,如设为0则无限期等待。单位毫秒。Default: 0
				overrides.put("idleConnectionTestPeriod", "30");		//每30秒检查所有连接池中的空闲连接。Default: 0
				overrides.put("initialPoolSize", "2");					//初始化时获取三个连接，取值应在minPoolSize与maxPoolSize之间。Default: 3
				overrides.put("maxIdleTime", "30");						//最大空闲时间,30秒内未使用则连接被丢弃。若为0则永不丢弃。Default: 0
				overrides.put("maxPoolSize", "10");						//连接池中保留的最大连接数。Default: 15
				overrides.put("minPoolSize", "2");						//连接池中保留的最小连接数。Default: 10
				overrides.put("maxStatements", "0");					//如果maxStatements与maxStatementsPerConnection均为0，则缓存被关闭。Default: 0
				overrides.put("maxStatementsPerConnection", "0");		//maxStatementsPerConnection定义了连接池内单个连接所拥有的最大缓存statements数。Default: 0
				overrides.put("numHelperThreads", "2");					//通过多线程实现多个操作同时被执行。Default: 3
				
				DataSource ds_pooled = DataSources.pooledDataSource(ds_unpooled, overrides);
				map.put(id, ds_pooled);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
}