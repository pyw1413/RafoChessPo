package com.bbzhu.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ListPage {
	private int intPage = 1;													//当前页数
	private int pageCount = 0;													//总页数
	private int pageSize = 1000000;												//每页记录数
	private int rsCount = 0;													//总记录数
	private String strOrder;													//排序字符
	private String strSelect;													//要查询的字段
	private List<Condition> conditionList = new ArrayList<Condition>();			//查询条件
	private Map<String, Object> paramMap = new HashMap<String, Object>(); 		//页面参数
	
	public ListPage(String strSelect, String strOrder){
		this.strSelect = strSelect;
		this.strOrder = strOrder;
	}
	
	public ListPage(){
		this.strSelect = "*";
		this.strOrder = "";
	}
	
	public ListPage(HttpServletRequest request,Integer pageSize){
		this.strSelect = "*";
		this.strOrder = "";
		this.intPage = Common.getPage(request);
		this.pageSize = pageSize;
	}
	
	/**
	 * 生成最终子句
	 */
	public String getString(){
		StringBuffer sb = new StringBuffer();
		
		sb.append(this.getWhereString());
		
		if(!this.strOrder.equals(""))
			sb.append(" order by ").append(this.strOrder);
		
		return sb.toString();
	}
	
	/**
	 * 生成Where子句
	 */
	public String getWhereString(){
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0; i < conditionList.size(); i++){
			Condition c = conditionList.get(i);
			if(i > 0)
				sb.append(" and ");
			else
				sb.append(" where ");
			sb.append(c.getString());
		}
		
		return sb.toString();
	}
	
	/**
	 * 生成分页用字符串
	 */
	public String getUrlParam(){
		StringBuffer sb = new StringBuffer();
		for(String key : paramMap.keySet()){
			if(sb.length() > 0)
				sb.append("&");
			else
				sb.append("?");
			sb.append(key).append("=").append(paramMap.get(key).toString());
		}
		return sb.toString();
	}
	
	/**
	 * 生成分页用字符串
	 */
	public String getUrl(String url,Integer intPage){
		StringBuffer sb = new StringBuffer(url);
		String strParam = this.getUrlParam();
		if(strParam.equals(""))
			sb.append("?intPage=").append(intPage);
		else
			sb.append(strParam).append("&intPage=").append(intPage);
		
		return sb.toString();
	}
	
	/**
	 * 格式化页码信息
	 */
	public void init(int rsCount){
		if(rsCount <= 0){
			return;
		}
		
		pageCount = (rsCount + pageSize - 1)/pageSize;
		if(intPage > pageCount){
			intPage = pageCount;
		}

		this.rsCount = rsCount;
	}
	
	/**
	 * 列出所有的页码
	 */
	public String buildAllPage(String url){
		StringBuffer sb = new StringBuffer();
		
//		if(this.intPage > 1){
//			sb.append("<a href='"+this.getUrl(url, 1)+"' class='page'>首页</a> ");
//			sb.append("<a href='"+this.getUrl(url, this.intPage - 1)+"' class='page'>上一页</a> ");
//		}
		
		for(int i = 1; i < this.pageCount+1; i++){
			if(i == this.intPage){
				sb.append("<a href='"+this.getUrl(url, i)+"' class='pagenow'>"+i+"</a> ");
			}else{
				sb.append("<a href='"+this.getUrl(url, i)+"' class='page'>"+i+"</a> ");
			}
		}
		
//		if(this.intPage < this.pageCount){
//			sb.append("<a href='"+this.getUrl(url, this.intPage+1)+"' class='page'>下一页</a> ");
//			sb.append("<a href='"+this.getUrl(url, this.pageCount)+"' class='page'>尾页</a> ");
//		}
		
		return sb.toString();
	}
	
	/**
	 * 列出count条页码
	 */
	public String buildPage(String url, int count){
		StringBuffer sb = new StringBuffer();
		
//		sb.append("<a href='"+this.getUrl(url, 1)+"' class='page'>首页</a> ");
//		sb.append("<a href='"+this.getUrl(url, this.intPage-1)+"' class='page'>上一页</a> ");
		
		int half = count/2;
		
		int n = 0;
		for(int i=(this.intPage-half); i < this.pageCount+1; i++){
			if(i > 0){
				n++;
				if(i == this.intPage){
					sb.append("<a href='"+this.getUrl(url, i)+"' class='pagenow'>"+i+"</a> ");
				}else{
					sb.append("<a href='"+this.getUrl(url, i)+"' class='page'>"+i+"</a> ");
				}
			}
			if(n >= count){
				break;
			}
		}
		
//		sb.append("<a href='"+this.getUrl(url, this.intPage+1)+"' class='page'>下一页</a> ");
//		sb.append("<a href='"+this.getUrl(url, this.pageCount)+"' class='page'>尾页</a> ");
		
		return sb.toString();
	}
	
	/**
	 * 后台使用，通过select框进行跳转
	 */
	public String buildSelPage(String url){
		StringBuffer sb = new StringBuffer();
		
		sb.append("共有 <font color='red'>").append(this.getRsCount()).append("</font> 条记录，");
		sb.append("当前 <font color='red'>").append(this.getIntPage()).append("</font> / <font color='red'>").append(this.getPageCount()).append("</font> 页，");
		sb.append("跳转到 <select name='intPage' onchange=\"changePage(this.options[this.selectedIndex].value,'"+url+this.getUrlParam()+"')\">");
		for(int i = 1; i < this.pageCount+1; i++){
			if(i == this.intPage){
				sb.append("<option value='"+i+"' selected>"+i+"</option> ");
			}else{
				sb.append("<option value='"+i+"'>"+i+"</option> ");
			}
		}
		sb.append("</select> 页");
		
		return sb.toString();
	}
	
	/**
	 * 同时增加一个查询条件和页面跳转条件
	 */
	public void addConditonAndParam(String name,Object value){
		this.addCondition(name, value);
		this.addParam(name, value);
	}
	
	/**
	 * 增加一个查询条件
	 */
	public void addCondition(String name,Object value,String operat){
		Condition c = new Condition(name,value,operat);
		conditionList.add(c);
	}
	
	public void addCondition(String name,Object value){
		this.addCondition(name, value, "=");
	}
	
	/**
	 * 增加一个页面跳转条件
	 */
	public void addParam(String name,Object value){
		paramMap.put(name, value);
	}
	
	public Object getParam(String name){
		return paramMap.get(name);
	}

	public int getIntPage() {
		return intPage;
	}

	public void setIntPage(int intPage) {
		if(intPage < 1)
			intPage = 1;
		this.intPage = intPage;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if(pageSize < 0)
			pageSize = 10;
		this.pageSize = pageSize;
	}

	public int getRsCount() {
		return rsCount;
	}
	
	public String getStrOrder() {
		return strOrder;
	}

	public void setStrOrder(String strOrder) {
		this.strOrder = strOrder;
	}
	
	public String getStrSelect() {
		return strSelect;
	}

	public void setStrSelect(String strSelect) {
		this.strSelect = strSelect;
	}

	class Condition {
		private String key;
		private Object value;
		private String operat;
		
		public Condition(String key, Object value, String operat){
			this.key = key;
			this.value = value;
			this.operat = operat;
		}
		
		public String getString(){
			String keyType = BeanUtil.getInstance().getShortName(value.getClass().toString());
			return key + operat + BeanUtil.getInstance().getValue(this.value, keyType);
		}
	}
}
