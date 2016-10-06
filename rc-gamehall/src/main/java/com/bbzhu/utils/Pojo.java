package com.bbzhu.utils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import javazoom.upload.MultipartFormDataRequest;


public class Pojo{
    protected String key;
    protected String tableName;
    protected String className;
    protected boolean autoIncrement;
    
    public Pojo(){
    }
    
    public String getKey(){
        return this.key;
    }
    
    public String getTableName(){
        return this.tableName;
    }
    
    public String getClassName(){
    	return this.className;
    }
    
    public boolean getAutoIncrement(){
        return this.autoIncrement;
    }
    
    public String buildInsert(){
        return BeanUtil.getInstance().buildInsert(this);
    }
    
    public String buildUpdate(){
        return BeanUtil.getInstance().buildUpdate(this);
    }
    
    public String buildDelete(){
        return BeanUtil.getInstance().buildDelete(this);
    }
    
    public Pojo getPojo(HttpServletRequest request){
    	return BeanUtil.getInstance().getPojo(this, request, 0);
    }
    
    public Pojo getPojo(MultipartFormDataRequest request){
    	return BeanUtil.getInstance().getPojo(this, request, 0);
    }
    
    public Pojo updatePojo(HttpServletRequest request){
    	return BeanUtil.getInstance().getPojo(this, request, 1);
    }
    
    public Pojo updatePojo(MultipartFormDataRequest request){
    	return BeanUtil.getInstance().getPojo(this, request, 1);
    }
    
    public int save(){
    	return Dao.getInstance().save(this);
    }
    
    public int update(){
    	return Dao.getInstance().update(this);
    }
    
    public int delete(){
    	return Dao.getInstance().delete(this);
    }
    
    public Pojo find(Object id){
    	return Dao.getInstance().find(this, id);
    }
    
    public List<Pojo> findAll(){
    	return Dao.getInstance().findAll(this);
    }
    
    public List<Pojo> findList(String sql){
    	return Dao.getInstance().findList(sql,this);
    }
    
    public List<Pojo> getTop(Integer n){
    	return Dao.getInstance().getTop(this, n);
    }
    
    public List<Pojo> getPageList(ListPage listPage){
    	return Dao.getInstance().getPageList(listPage, this);
    }
    
    public int getCount(){
    	return Dao.getInstance().getCount(this);
    }
    
    public int getMax(){
    	return Dao.getInstance().getMax(this);
    }
}
