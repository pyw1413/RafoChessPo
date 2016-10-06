package com.kodgames.battle.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class DateTimeUtil
{
	private static String timeZone = "GMT+8";
	private static ReentrantReadWriteLock timeLock = new ReentrantReadWriteLock();
	/*
	 * 获取Date的字符串格式，以北京时间为准
	 */
	public static String getGMT8String(Date date)
	{
		try
		{
			timeLock.readLock().lock();
			DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
			return dateFormat.format(date);
		}
		finally
		{
			timeLock.readLock().unlock();
		}
	}
	
	public static long getCurrentTimeMillis()
	{
		try
		{
			timeLock.readLock().lock();
			return System.currentTimeMillis();
		}
		finally
		{
			timeLock.readLock().unlock();
		}
	}
	
	public static Date getNowDate()
	{
		return new Date();
	}
	
	public static Date getDate(Long milliseconds)
	{
		return new Date(milliseconds);
	}
	
	/**
	 * 两天前的00点时间，例如：2016.06.06返回2016.06.04.0.0.0
	 */
	public static Date getTwoDayAgo(Date today)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String todayStr = sdf.format(today);
		Calendar c = Calendar.getInstance();
		Date todayZero = null;
		try {
			todayZero = sdf.parse(todayStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		c.setTime(todayZero);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 2);

		return c.getTime();
	}
	
	public static long getDateDiff(long date1, long date2)
	{
		return date1 - date2;
	}
}
