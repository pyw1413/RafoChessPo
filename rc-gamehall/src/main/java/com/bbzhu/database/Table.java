/**
 *本类主要处理数据库表的逻辑
 *修改日期:2006-01-03
 */
package com.bbzhu.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;

public class Table{
	public Table(){
	}

	public RowSet select(String SQL) throws Exception{
		Connection conn = null;
		PreparedStatement st = null;
		RowSet rows = new RowSet();
		try{
			conn = DatabaseConn.getInstance().getConnection();
			st = conn.prepareStatement(SQL);
			if(Param.getDebug()){
				System.out.println("HSQL:" + SQL);
			}
			ResultSet rs = st.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			if (cols == 0){
				return null;
			}
			while (rs.next()){
				Row row = new Row();
				for (int i = 1; i <= cols; i++){
					String name = rsmd.getColumnName(i);
					row.put(name, rs.getString(i));
				}
				rows.add(row);
			}
			rs.close();
			st.close();
		}catch (Exception ex){
			ex.printStackTrace();
			System.out.println("执行["+SQL+"]查询时出错！");
		}finally{
			if(st != null){
				st.close();
			}
			disConnect(conn);
		}
		return rows;
	}
	
	private void disConnect(Connection conn){
		try{
			if(conn != null){
				conn.close();
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
    /**
     * 执行更新操作
     */
	public int edit(String SQL) throws Exception{
		int result = 0;
		Connection conn = null;
		PreparedStatement st = null;
		try{
			conn = DatabaseConn.getInstance().getConnection();
			st = conn.prepareStatement(SQL);
			if(Param.getDebug()){
				System.out.println("HSQL:" + SQL);
			}
			result = st.executeUpdate();
			st.close();
		}catch (Exception ex){
			ex.printStackTrace();
			System.out.println("执行SQL["+SQL+"]更新时出错！");
		}finally{
			if(st != null){
				st.close();
			}
			disConnect(conn);
		}
		return result;
	}
	
	/**
     * 执行批量更新操作
     */
	public boolean batchEdit(List<String> list) throws Exception{
		boolean bresult = false;								//批量执行的结果
		Connection conn = null;
		Statement st = null;
		try{
			conn = DatabaseConn.getInstance().getConnection();
			st = conn.createStatement();
			
			conn.setAutoCommit(false);

			for(int i = 0; i < list.size(); i++){
				String sql = (String)list.get(i);
				if(Param.getDebug()){
					System.out.println("HSQL:" + sql);
				}
				st.execute(sql);
			}
			
			conn.commit();
			st.close();
			
			bresult = true;
		}catch (Exception ex){
			conn.rollback();
			ex.printStackTrace();
		}finally{
			if(st != null){
				st.close();
			}
			disConnect(conn);
		}
		return bresult;
	}
}
