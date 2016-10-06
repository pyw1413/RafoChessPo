package com.bbzhu.database;

import java.util.Vector;

public class RowSet{
	private Vector<Row> rows = new Vector<Row>();								//用于存放行的集合
	public RowSet(){
	}
	
	//往记录集中添加一行
	public void add(Row row){
		rows.addElement(row);
	}
	
	//记录集的行数
	public int length(){
		return rows.size();
	}
	
	//第n行记录
	public Row get(int which){
		if (length() < 1){
			return null;
		}else{
			return (Row)rows.elementAt(which);
		}
	}
}