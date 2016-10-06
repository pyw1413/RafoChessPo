package com.bbzhu.utils;

import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.*;
import javazoom.upload.MultipartFormDataRequest;

public class Common {
	private static String[] denys = {"select","update","insert","delete","where","union","database","into","file"};
	private static String[] emonth = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	/**
	 * 防注入String格式化
	 */
	public static String format(String s){
		for(int i = 0; i < denys.length; i++){
			while(s.toLowerCase().indexOf(denys[i]) >= 0){
				s = s.replaceAll("(?i)"+denys[i], "");
			}
		}
		return s;
	}
	
	/**
	 * 获取String类型的参数值
	 */
	public static String formatString(String s){
		if(s == null){
			return "";
		}else{
			return s;
		}
	}
	
	public static String getValue(String strName,HttpServletRequest request){
		return formatString(request.getParameter(strName));
	}
	
	public static String getValue(String strName,MultipartFormDataRequest request){
		return formatString(request.getParameter(strName));
	}
	
	/**
	 * 获取Integer类型的参数值
	 */
	public static Integer formatInteger(String s){
		if(s == null){
			return 0;
		}else{
			try{
				Integer i = new Integer(s);
				return i;
			}catch(Exception ex){
				return 0;
			}
		}
	}
	
	public static Integer getInteger(String strName,HttpServletRequest request){
		return formatInteger(request.getParameter(strName));
	}
	
	public static Integer getInteger(String strName,MultipartFormDataRequest mrequest){
		return formatInteger(mrequest.getParameter(strName));
	}
	
	public static int getPage(HttpServletRequest request){
		Integer i = getInteger("intPage",request);
		if(i > 1){
			return i.intValue();
		}else{
			return 1;
		}
	}
	
	public static String formatNumber(long n){
		return NumberFormat.getInstance().format(n);
	}
	
	public static String formatNumber(double d,int n){
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(n);
		format.setMinimumFractionDigits(n);
		return format.format(d);
	}
	
	/**
	 * 传递UTF8编码信息
	 */
	public static String encode(String s) throws Exception{
		return URLEncoder.encode(s,"utf-8");
	}
	
	public static String decode(String s){
		try{
			s = new String(s.getBytes("ISO-8859-1"),"utf-8");
		}catch(Exception ex){
			s = "";
		}
		return s;
	}
	
	public static String formatUTF8(String s){
		 StringBuffer sb = new StringBuffer();
		 for(int i = 0; i < s.length(); i++){
			 char c = s.charAt(i);
			 if(c >= 0 && c <= '\377'){
				 sb.append(c);
			 }else{
				 byte b[];
				 try{
					 b = Character.toString(c).getBytes("utf-8");
				 }catch(Exception ex){
					 System.out.println(ex);
					 b = new byte[0];
				 }
				 for(int j = 0; j < b.length; j++){
					 int k = b[j];
					 if(k < 0)
						 k += 256;
					 sb.append("%" + Integer.toHexString(k).toUpperCase());
				 }
			 }
		 }
		 return sb.toString();
	 }
	
	public static String getDecodeValue(String strName,HttpServletRequest request){
		return decode(getValue(strName,request));
	}
	
	public static String getDecodeValue(String strName,MultipartFormDataRequest request){
		return decode(getValue(strName,request));
	}
	
	/**
	 * 获取多选框的值
	 */
	public static String getValues(String strName,HttpServletRequest request){
		String strValue[] = request.getParameterValues(strName);
		String s = "";
		if(strValue == null){
			return "";
		}else{
			for(int i = 0; i < strValue.length; i++){
				if(!strValue[i].trim().equals("")){
					if(s.equals("")){
						s = strValue[i].trim();
					}else{
						s = s + "," + strValue[i].trim();
					}
				}
			}
			return s;
		}
	}
	
	/**
	 * 将类型为 a,b,c 的字符串返回为 'a','b','c' 的字符串
	 */
	public static String getListStr(String s){
		if(s == null || s.equals("")){
			return "";
		}
		String[] s1 = s.split(",");
		String rStr = "";
		for(int i = 0; i < s1.length; i++){
			if(!s1[i].equals("")){
				rStr = append(rStr,"'"+s1[i]+"'");
			}
		}
		return rStr;
	}
	
	/**
	 * 连接字符串,s为总字符,s1为要连接字符,s2连接串
	 */
	public static String append(String s,String s1,String s2){
		if(s.equals("")){
			s = s1;
		}else{
			s = s + s2 + s1;
		}
		return s;
	}
	
	public static String append(String s,String s1){
		return append(s,s1,",");
	}
	
	/**
	 * 获取session中传递的message信息
	 */
	public static String getMsg(HttpServletRequest request){
		return getMsg(request,"");
	}
	
	public static String getMsg(HttpServletRequest request,String defaultValue){
		String s = "";
		if(request.getSession().getAttribute("msg") != null){
			s = request.getSession().getAttribute("msg").toString();
			request.getSession().removeAttribute("msg");
		}
		if(s.equals("")){
			s = defaultValue;
		}
		return s;
	}
	
	/**
	 * 字符截取，双字节字符标示为2
	 */
	public static String getTitle(String strTitle,int n){
		Pattern p = Pattern.compile("[^\\x00-\\xff]");			//双字节字符正则
		int nlen = 0;
		String tmpStr = "";
		
		for(int i = 0; i < strTitle.length(); i++){
			String str = strTitle.substring(i, i+1);
			Matcher m = p.matcher(str);
			if (m.matches()){
				nlen += 2;
			} else {
				nlen += 1;
			}
			
			tmpStr += str;
			
			if (nlen >= n) {
				break;
			}
		}
		
		if(!tmpStr.equals(strTitle))
			return tmpStr + "..";
		else
			return tmpStr;
	}
	
	/**
	 * 获取月份
	 */
	public static int getMonth(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.MONTH)+1;
	}
	
	public static int getMonth(Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c.get(Calendar.MONTH)+1;
	}
	
	/**
	 * 获取英文月份名称
	 */
	public static String getEMonth(int i){
		return emonth[i-1];
	}
	
	/**
	 * 获取两个时间的天数差，d1-d2
	 */
	public static int getDateDiff(Date d1,Date d2){
		int i = (int)((d1.getTime() - d2.getTime())/1000/60/60/24);
		return i;
	}
	
	/**
	 * 获取两个时间的小时差，d1-d2
	 */
	public static int getHourDiff(Date d1,Date d2){
		int i = (int)((d1.getTime() - d2.getTime())/1000/60/60);
		return i;
	}
	
	/**
	 * 获取间隔一定天数的日期
	 */
	public static Date getDayDate(Date date, int day) {
		date.setTime(date.getTime() + day*1000*24*60*60);
		return date;
	}

	/**
	 * 获取启动时间，用于定时任务，一般指定整点
	 */
	public static Date getStartTime(int hour){
		Calendar c = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		
		c.set(now.get(Calendar.YEAR), now.get(Calendar.MONDAY), now.get(Calendar.DATE),hour,0,0);
		if(c.before(now)){
			c.set(Calendar.DATE, c.get(Calendar.DATE)+1);
		}

		return c.getTime();
	}
	
	/**
	 * 判断日期合法性
	 */
	public static boolean isDate(String s){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		df.setLenient(false);
		
		try{
			df.parse(s);
		}catch(Exception ex){
			return false;
		}
		
		return true;
	}
	
	public static boolean isFullDate(String s){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		df.setLenient(false);
		
		try{
			df.parse(s);
		}catch(Exception ex){
			return false;
		}
		
		return true;
	}
	
	/**
	 * 获取年龄

	 */
	public static int getAge(String birthday) {
		return getAge(Common.format(birthday));
	}

	public static int getAge(Date birthday) {
		Calendar now = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		c.setTime(birthday);
		
		if(c.after(now)){
			return 0;
		}
		
		int age = now.get(Calendar.YEAR) - c.get(Calendar.YEAR); 
		c.set(Calendar.YEAR, now.get(Calendar.YEAR ));
		
		if(c.after(now)){
			age--;
		}
		
        return age;
	}
	
	/**
	 * 一系列时间函数
	 */
	public static String getNow(){
		Date now = new Date();
		return FormatFullDate(now);
	}
	
	public static String getDate(){
		Date now = new Date();
		return FormatDate(now);
	}
	
	public static String FormatFullDate(Date date){
		if(date == null){
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
	
	public static String FormatDate(Date date){
		if(date == null){
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}
	
	public static Date FormatFullDate(String str){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			Date date = df.parse(str);
			return date;
		}catch(Exception ex){
			return null;
		}
	}
	
	public static Date FormatDate(String str){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Date date = df.parse(str);
			return date;
		}catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * 随机生成文件名
	 */
	public static String getFileName(){
		Calendar c = Calendar.getInstance();
		String str = getStr(c.get(Calendar.YEAR),4);
		str = str + getStr(c.get(Calendar.MONTH)+1,2);
		str = str + getStr(c.get(Calendar.DATE),2);
		str = str + getStr(c.get(Calendar.HOUR_OF_DAY),2);
		str = str + getStr(c.get(Calendar.MINUTE),2);
		str = str + getStr(c.get(Calendar.SECOND),2);
		str = str + getStr(c.get(Calendar.MILLISECOND),3);
		str = str + getRandomInt(5);
		return str;
	}
	
	/**
	 * 将n1转换成n2长度的字符串
	 */
	public static String getStr(int n1, int n2){
		String str = n1 + "";
		if(str.length() < n2){
			int nlen = n2 - str.length();
			for(int i = 0 ; i < nlen; i++){
				str = "0" + str;	
			}
		}
		return str;
	}
	
	/**
	 * 查看s1是否包含s2,s1为s3隔开的字符串
	 */
	public static boolean find(String s1,String s2,String s3){
		if(s1 == null){
			return false;
		}
		if(s2 == null){
			s2 = "";
		}
		
		String[] s = s1.split(s3);
		boolean b = false;
		for(int i = 0; i < s.length; i++){
			if(s[i].equals(s2)){
				b = true;
				break;
			}    		
		}
		return b;
	}
	
	public static boolean find(String s1,String s2){
		return find(s1,s2,",");
	}
	
	/**
	 * 判断是否为邮件地址
	 */
	public static boolean isEmail(String str) {
		Pattern p = Pattern.compile("^[\\w\\-\\.]+@[\\w\\-\\.]+(\\.\\w+)+$");		//正则表达式
		Matcher m = p.matcher(str);													//操作的字符串
		Boolean b = m.matches();
		return b;
	}
	
	/**
	 * 获取HTML编码
	 */
	public static String getHtml(String s){
		s = s.replace("  ","　");
		s = s.replace("<", "&lt;");
		s = s.replace(">", "&gt;");
		s = s.replace("\r\n","<br>");
		return s;
	}
	
	/**
	 * 获取长度为size的随机字符串
	 */
	public static String getRandomString(int size){
		char[] c ={ '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'Q',
				'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D',
				'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M' };
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			sb.append(c[Math.abs(random.nextInt()) % c.length]);
		}
		return sb.toString();
	}
	
	public static String getRandomInt(int size){
		char[] c ={ '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			sb.append(c[Math.abs(random.nextInt()) % c.length]);
		}
		return sb.toString();
	}
	
	/**
	 * 获取主机名
	 */
	public static String getHost(HttpServletRequest request){
		String url = request.getRequestURL().toString();
		String uri = request.getServletPath();
		url = url.replaceAll(uri,"");
		
		return url;
	}
	
	/**
	 * 获取当前访问的绝对路径
	 */
	public static String getRealPath(HttpServletRequest request){
		String url = request.getSession().getServletContext().getRealPath("").replaceAll("\\\\","/");
		String uri = request.getServletPath();
		uri = uri.substring(0, uri.lastIndexOf("/"));
		
		return url + uri;
	}
	
	/**
	 * 获取根路径
	 */
	public static String getRootPath(HttpServletRequest request){
		return request.getSession().getServletContext().getRealPath("").replaceAll("\\\\","/");
	}
	
	/**
	 * 获取浏览器类型
	 */
	public static String getExplorer(HttpServletRequest request){
		String agent = request.getHeader("User-Agent");
		StringTokenizer st = new StringTokenizer(agent,";");
		st.nextToken();
		return st.nextToken();
	}
	
	/**
	 * 设置cookie
	 */
	public static void setCookie(HttpServletResponse response,String name,String value,Integer t){
		Cookie cookie = new Cookie(name,value);
		cookie.setMaxAge(t);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	/**
	 * 获取cookie
	 */
	public static String getCookie(HttpServletRequest request,String name){
		Cookie[] cookies = request.getCookies();
		String value = null;
		
		if(cookies != null && cookies.length > 1){
			for(int i = 0 ; i < cookies.length; i++){
				if(cookies[i].getName().equals(name))
					value = cookies[i].getValue();
			}
		}
		
		return value;
	}
	
	/**
	 * 获取摘要，以第一段文字作为摘要
	 */
	public static String getScontent(String s){
		Pattern pattern = Pattern.compile("<p>.*?</p>");
		Matcher matcher = pattern.matcher(s);
		if(matcher.find()){
			return matcher.group();
		}
		return s;
	}
	
	/**
	 * 正则表达式获取某一段内容
	 */
	public static String getMatherGroupDoTall(String s,int i,String regex){
		Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
		Matcher matcher = pattern.matcher(s);
		while(matcher.find()){
			if(matcher.groupCount()>=i){
				return matcher.group(i);
			}
		}
		return null;
	}
	
	/**
	 * 生成Select语句
	 */
	public static String buildSelect(String key, Integer value, TreeMap<Integer,String> treeMap){
		StringBuffer sb = new StringBuffer();
		sb.append("<select name='"+key+"'>");
		for(Integer option:treeMap.keySet()){
			if(option.intValue() == value)
				sb.append("<option value='"+option+"' selected>"+treeMap.get(option)+"</option>");
			else
				sb.append("<option value='"+option+"'>"+treeMap.get(option)+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	/**
	 * 生成Select语句
	 */
	public static String buildSelect2(String key, String value, TreeMap<String,String> treeMap){
		StringBuffer sb = new StringBuffer();
		sb.append("<select name='"+key+"'>");
		for(String option:treeMap.keySet()){
			if(option.equals(value))
				sb.append("<option value='"+option+"' selected>"+treeMap.get(option)+"</option>");
			else
				sb.append("<option value='"+option+"'>"+treeMap.get(option)+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
}