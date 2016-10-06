package com.bbzhu.utils;

import java.util.ArrayList;
import java.util.List;

import com.bbzhu.database.Row;
import com.bbzhu.database.RowSet;
import com.bbzhu.database.Table;
import com.bbzhu.system.InvokeCenter;
import com.bbzhu.system.LockCenter;

public class Dao {
	private static Dao instance = null;
	private Table table;
	
	public static Dao getInstance(){
		synchronized (LockCenter.getInstance().getLock("BaseDao")) {
			if(instance == null){
				instance = new Dao();
			}
		}
		return instance;
	}
	
	private Dao(){
		table = new Table();
	}
	
	/**
	 * 基础查询代码
	 */
	public RowSet select(String sql){
		RowSet rs = null;
		try {
			rs = table.select(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return rs;
	}
	
	public Row getRow(String sql){
		RowSet rs = this.select(sql);
		if(rs.length() > 0)
			return rs.get(0);
		return null;
	}
	
	public int edit(String sql){
		int n = 0;
		try {
			n = table.edit(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}
	
	public boolean batchedit(List<String> list){
		boolean b = true;
		try {
			b = table.batchEdit(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	/**
	 * Model类3大基本操作
	 */
	public int save(Pojo pojo){
		return this.edit(pojo.buildInsert());
	}
	
	public int update(Pojo pojo){
		return this.edit(pojo.buildUpdate());
	}
	
	public int delete(Pojo pojo){
		return this.edit(pojo.buildDelete());
	}
	
	/**
	 * 获取单条记录
	 */
	public Pojo find(Pojo pojo, Object id){
		String sql = "select * from " + pojo.getTableName() + " where " + pojo.getKey() + "=" + id;
		if(BeanUtil.getInstance().getShortName(id.getClass().toString()).equals("String"))
			sql = "select * from " + pojo.getTableName() + " where " + pojo.getKey() + "='" + id + "'";
		
		RowSet rs = this.select(sql);
		try {
			if(rs.length() > 0)
				return rs.get(0).getPojo(pojo.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;	
	}
	
	public Pojo find(String className, Object id){
		return this.find(InvokeCenter.getInstance().getPojo(className), id);
	}
	
	/**
	 * 完全查询操作
	 */
	public List<Pojo> findAll(Pojo pojo){
		String sql = "select * from " + pojo.getTableName();
		return this.rsToList(this.select(sql), pojo.getClass());
	}
	
	public List<Pojo> findAll(String className){
		return this.findAll(InvokeCenter.getInstance().getPojo(className));
	}
	
	/**
	 * 条件查询操作
	 */
	public List<Pojo> findList(String sql,Pojo pojo){
		return this.rsToList(this.select(sql), pojo.getClass());
	}
	
	public List<Pojo> findList(String sql,String className){
		return this.findList(sql, InvokeCenter.getInstance().getPojo(className));
	}
	
	/**
	 * 获取最Top的几条记录
	 */
	public List<Pojo> getTop(Pojo pojo, Integer n){
		String sql = "select * from " + pojo.getTableName();
		sql = this.getPageSQL(sql, 1, n);
		return this.rsToList(this.select(sql), pojo.getClass());
	}
	
	public List<Pojo> getTop(String className, Integer n){
		return this.getTop(InvokeCenter.getInstance().getPojo(className), n);
	}
	
	/**
	 * 获取总记录数
	 */
	public int getCount(Pojo pojo){
		String sql = "select count("+pojo.getKey()+") as n from " + pojo.getTableName();
		return this.select(sql).get(0).getInt("n");
	}
	
	public int getCount(String className){
		return this.getCount(InvokeCenter.getInstance().getPojo(className));
	}
	
	/**
	 * 获取最大ID值
	 */
	public int getMax(Pojo pojo){
		String sql = "select max("+pojo.getKey()+") as n from " + pojo.getTableName();
		Row rw = this.select(sql).get(0);
		
		if(rw.get("n") == null){
			return 0;
		}else{
			return rw.getInt("n");
		}
	}
	
	public int getMax(String className){
		return this.getMax(InvokeCenter.getInstance().getPojo(className));
	}
	
	/**
	 * 分页查询
	 */
	public RowSet getPageList(ListPage listPage,String countSql,String sql){
		int rsCount = Integer.parseInt(this.select(countSql).get(0).get(0).toString());
		listPage.init(rsCount);
		
		sql = getPageSQL(sql, listPage.getIntPage(), listPage.getPageSize());
		return this.select(sql);
	}
	
	public List<Pojo> getPageList(ListPage listPage,String countSql,String sql,Pojo pojo){
		return this.rsToList(this.getPageList(listPage, countSql, sql), pojo.getClass());
	}
	
	public List<Pojo> getPageList(ListPage listPage,String countSql,String sql,String className){
		return this.getPageList(listPage, countSql, sql, InvokeCenter.getInstance().getPojo(className));
	}
	
	public List<Pojo> getPageList(ListPage listPage,Pojo pojo){
		String countSql = "select count(" + pojo.getKey() + ") from " + pojo.getTableName() + listPage.getWhereString();
		String sql = "select " + listPage.getStrSelect() + " from " + pojo.getTableName() + listPage.getString();
		return this.getPageList(listPage, countSql, sql, pojo);
	}
	
	public List<Pojo> getPageList(ListPage listPage,String className){
		return this.getPageList(listPage, InvokeCenter.getInstance().getPojo(className));
	}
	
	/**
	 * 基础转换代码
	 */
	public List<Pojo> rsToList(RowSet rs,Class c){
		List<Pojo> list = new ArrayList<Pojo>();
		
		try {
			for(int i = 0;i < rs.length();i++){
				Row row = rs.get(i);
				list.add(row.getPojo(c));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public String getPageSQL(String sql,int intPage,int pageSize){
		sql = sql + " limit " + (intPage-1)*pageSize + "," + pageSize;
		return sql;
	}
}
