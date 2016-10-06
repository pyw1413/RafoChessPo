package com.www.model.base;

import com.bbzhu.utils.Pojo;

public class BaseServer extends Pojo {
	private Integer id;
	private String ip;
	private Integer port;
	private Integer maxuser;

	public BaseServer(){
		this.tableName = "tbl_server";
		this.className = "Server";
		this.autoIncrement = true;
		this.key = "id";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this. id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this. ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this. port = port;
	}

	public Integer getMaxuser() {
		return maxuser;
	}

	public void setMaxuser(Integer maxuser) {
		this. maxuser = maxuser;
	}
}