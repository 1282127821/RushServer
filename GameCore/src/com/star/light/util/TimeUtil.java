package com.star.light.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class TimeUtil {
	//TODO:LZGLZG考虑JDK新的时间函数来重新编写
	/**
	 * 一个小时的秒数
	 */
	public static final int SECONDOFPERHOUR = 3600;
	
	/**
	 * 一天的秒数
	 */
	public static final int SECONDOFPERDAY = 86400;
	
	/**
	 * 一天的毫秒数
	 */
	public static final int MILSECOFPERDAY = 86400000;

	/**
	 * 获取系统距1970年1月1日总毫秒
	 */
	public static long getSysCurTimeMillis() {
		return getCalendar().getTimeInMillis();
	}

	/**
	 * 获取系统距1970年1月1日总秒
	 */
	public static int getSysCurSeconds() {
		return (int) (getSysCurTimeMillis() / 1000);
	}

	/**
	 * 获取系统距1970年1月1日总秒
	 */
	public static int getSysCurDay() {
		return (int) (getSysCurTimeMillis() / MILSECOFPERDAY);
	}

	/**
	 * 获取系统当前时间
	 */
	public static Timestamp getSysteCurTime() {
		Timestamp ts = new Timestamp(getCalendar().getTimeInMillis());
		return ts;
	}

	public static Timestamp getSysMonth() {
		Calendar now = getCalendar();
		now.set(Calendar.DAY_OF_MONTH, 1);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.format(now.getTime());
		return new Timestamp(now.getTimeInMillis());
	}

	/**
	 * 获取指定日期距1970年1月1日总秒
	 */
	public static long getDateToSeconds(Date date) {
		return getCalendar(date).getTimeInMillis() / 1000;
	}

	public static int getSecondsFromDate(Date date) {
		return (int) (getCalendar(date).getTimeInMillis() / 1000);
	}

	/**
	 * 获取当前时间的秒
	 */
	public static int getSysTimeSeconds() {
		Calendar cal = getCalendar();
		return cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
	}

	/**
	 * 获取指定日期距1970年1月1日总毫秒
	 */
	public static long getDateToMillis(Date date) {
		return getCalendar(date).getTimeInMillis();
	}

	/**
	 * 获取当前小时
	 */
	public static int getCurrentHour() {
		return getCalendar().get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取指定时间小时
	 */
	public static int getHourByDate(Date date) {
		return getCalendar(date).get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获 取当前分钟
	 */
	public static int getCurrentMinute() {
		return getCalendar().get(Calendar.MINUTE);
	}

	/**
	 * 获取当前秒钟
	 */
	public static int getCurrentSecond() {
		return getCalendar().get(Calendar.SECOND);
	}

	/**
	 * 获取当前天
	 */
	public static int getCurrentDay() {
		return getCalendar().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 指定的毫秒long值转成Timestamp类型
	 */
	public static Timestamp getMillisToDate(long value) {
		return new Timestamp(value);
	}

	/**
	 * 当前系统时间增加值
	 */
	public static Date addSystemCurTime(int type, int value) {
		Calendar cal = getCalendar();
		switch (type) {
		case Calendar.DATE:// 增加天数
			cal.add(Calendar.DATE, value);
			break;

		case Calendar.HOUR:// 增加小时
			cal.add(Calendar.HOUR, value);
			break;

		case Calendar.MINUTE:// 增加分钟
			cal.add(Calendar.MINUTE, value);
			break;

		case Calendar.SECOND:// 增加秒
			cal.add(Calendar.SECOND, value);
			break;

		case Calendar.MILLISECOND:// 增加毫秒
			cal.add(Calendar.MILLISECOND, value);
			break;

		default:
			break;
		}
		return new Date(cal.getTimeInMillis());
	}

	public static String getSimpleDateFormat(Date date, String format, int type, int value) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);// 格式化对象
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		switch (type) {
		case Calendar.MONTH:// 增加月数 负数即为减
			calendar.add(Calendar.MONTH, value);
			break;

		case Calendar.DATE:// 增加天数
			calendar.add(Calendar.DATE, value);
			break;

		case Calendar.HOUR:// 增加小时
			calendar.add(Calendar.HOUR, value);
			break;

		case Calendar.MINUTE:// 增加分钟
			calendar.add(Calendar.MINUTE, value);
			break;

		case Calendar.SECOND:// 增加秒
			calendar.add(Calendar.SECOND, value);
			break;

		case Calendar.MILLISECOND:// 增加毫秒
			calendar.add(Calendar.MILLISECOND, value);
			break;

		default:
			break;
		}
		return sdf.format(calendar.getTime());
	}

	/**
	 * 当前系统时间增加值
	 *
	 * @param type
	 * @param value
	 * @return
	 */
	public static Date setDateByType(Date date, int type, int value) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		switch (type) {
		case Calendar.DATE:// 增加天数
			cal.set(Calendar.DATE, value);
			break;

		case Calendar.HOUR:// 增加小时
			cal.set(Calendar.HOUR, value);
			break;

		case Calendar.MINUTE:// 增加分钟
			cal.set(Calendar.MINUTE, value);
			break;

		case Calendar.SECOND:// 增加秒
			cal.set(Calendar.SECOND, value);
			break;

		case Calendar.MILLISECOND:// 增加毫秒
			cal.set(Calendar.MILLISECOND, value);
			break;

		default:
			break;
		}
		return new Date(cal.getTimeInMillis());
	}

	public static Date getNextDate() {
		Calendar cal = getCalendar();
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 * 格式化日期
	 */
	public static String getDateFormat(Date date) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = formatter.format(date);
		return ctime;
	}
	
	public static String getDateFormat(Date date, String format) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat(format);
		String ctime = formatter.format(date);
		return ctime;
	}

	/**
	 * 比较日期是否同一天
	 */
	public static boolean dateCompare(Date date) {
		if (date == null)
			return false;
		Calendar now = getCalendar();
		Calendar other = getCalendar(date);
		return dateCompare(now, other) == 0;
	}

	public static boolean dateCompare(long date) {
		Calendar now = getCalendar();
		Calendar other = getCalendar(getMillisToDate(date));
		return dateCompare(now, other) == 0;
	}

	public static boolean dataCompare5(Date date) {
		if (date == null)
			return false;
		Calendar now = getCalendar();
		now.add(Calendar.HOUR_OF_DAY, -5);
		Calendar other = getCalendar(date);
		other.add(Calendar.HOUR_OF_DAY, -5);
		return dateCompare(now, other) == 0;
	}

	/**
	 * 返回两个日期相差天数,凌晨5点算一天
	 */
	public static int dataCompare5(Date date1, Date date2) {
		Calendar c1 = getCalendar(date1);
		Calendar c2 = getCalendar(date2);
		c1.add(Calendar.HOUR_OF_DAY, -5);
		c2.add(Calendar.HOUR_OF_DAY, -5);
		return dateCompare(c1, c2);
	}

	public static boolean dataCompare(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return false;
		Calendar c1 = getCalendar(date1);
		Calendar c2 = getCalendar(date2);
		return dateCompare(c1, c2) == 0;
	}

	/**
	 * 返回两个日期相差天数
	 */
	public static int dataCompare(Date date1, Date date2, int hours) {
		Calendar c1 = getCalendar(date1);
		Calendar c2 = getCalendar(date2);
		if (hours > 0) {
			c1.add(Calendar.HOUR_OF_DAY, hours);
			c2.add(Calendar.HOUR_OF_DAY, hours);
		}
		return dateCompare(c1, c2);
	}


	public static int getToday5TimeSecond() {
		Calendar calendar = Calendar.getInstance();  //得到日历
		calendar.setTime( new Date());//把当前时间赋给日历
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour < 5) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
		calendar.set(Calendar.HOUR_OF_DAY, 5);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return (int)(calendar.getTimeInMillis()/1000);
	}

	public static Date parseDateHours(long timeMillis, int hours) {
		Calendar now = getCalendar(new Date(timeMillis));
		now.set(Calendar.HOUR_OF_DAY, hours);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		return now.getTime();
	}

	/**
	 * 返回两个日期相差天数
	 *
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @return
	 */
	public static int dateCompare(Calendar startDate, Calendar endDate) {
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.MILLISECOND, 0);

		endDate.set(Calendar.HOUR_OF_DAY, 0);
		endDate.set(Calendar.MINUTE, 0);
		endDate.set(Calendar.SECOND, 0);
		endDate.set(Calendar.MILLISECOND, 0);

		int day = (int) (endDate.getTimeInMillis() / MILSECOFPERDAY - startDate.getTimeInMillis() / MILSECOFPERDAY);
		return day;
	}

	public static int dateCompare(Date startDate, Date endDate) throws Exception {
		if (startDate == null || endDate == null) {
			throw new Exception("startDate is null or endDate is null");
		}
		Calendar c1 = getCalendar(startDate);
		Calendar c2 = getCalendar(endDate);
		return dateCompare(c1, c2);
	}

	/**
	 * 比较日期是否是同一个月份
	 *
	 * @param date 被比较的日期
	 * @return
	 */
	public static boolean monthCompare(Date date) {// 一年之内是否是同一个月
		if (date == null)
			return false;
		Calendar now = getCalendar();
		Calendar other = getCalendar(date);
		int nowMonth = now.get(Calendar.MONTH) + 1;
		int otherMonth = other.get(Calendar.MONTH) + 1;
		return (otherMonth - nowMonth) == 0;
	}

	/**
	 * 比较日期是否相隔一个小时
	 *
	 * @param date 被检测日期
	 * @return
	 */
	public static boolean hourCompare(Date date) {// 一天之内是否是同一小时
		if (date == null)
			return false;
		Calendar now = getCalendar();
		Calendar other = getCalendar(date);
		long nowMillis = now.getTimeInMillis();
		long otherMillis = other.getTimeInMillis();
		return Math.abs(nowMillis - otherMillis) < 30 * 1000;
	}

	/**
	 * 获取该月的天数
	 *
	 * @return
	 */
	public static int monthDays() {// 返回当前月份的天数
		Calendar now = getCalendar();
		return now.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取当前是该月的第几天
	 *
	 * @return
	 */
	public static int monthDay() {
		Calendar now = getCalendar();
		return now.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 重置防沉迷刷新时间
	 *
	 * @param hour        刷新时间点
	 * @param refreshTime 刷新时间引用
	 */
	public static void setAASRefreshTime(int hour, Calendar refreshTime) {
		refreshTime.setTime(getSysteCurTime());
		refreshTime.set(Calendar.HOUR_OF_DAY, hour);
		refreshTime.set(Calendar.MINUTE, 0);
		refreshTime.set(Calendar.SECOND, 0);
	}

	public static long calcDistanceMillis(Date startTime, Date endTime) {
		long startSecond = getDateToSeconds(startTime);
		long endSecond = getDateToSeconds(endTime);
		return (endSecond - startSecond) * 1000;
	}

	/**
	 * 间隔时间以小时为单位 
	 */
	public static boolean isInterval(Date startDate) {
		return dataCompare5(startDate);
	}

	/**
	 * 获取系统时间
	 */
	private static Calendar getCalendar() {
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(new Date());
		return nowCalendar;
	}

	/**
	 * 获取指定的时间
	 */
	public static Calendar getCalendar(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}

	public static Timestamp getCalendarToDate(Calendar calendar) {
		return calendar != null ? new Timestamp(getCalendar().getTimeInMillis()) : null;
	}

	public static Date addDate(Date date, long value) {
		long time = date.getTime() + value;
		return new Date(time);
	}

	/**
	 * 获取特定日期（凌晨0点） 例：2014-04-09 00:00:00
	 *
	 * @param date 特定日期
	 * @param days 增减天数
	 * @return
	 */
	public static Date addDateByDay(Date date, int days) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 * 获取当前日期（凌晨0点） 例：2014-04-09 00:00:00
	 *
	 * @return
	 */
	public static Date getSysCurDate() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 * 把日期类型转换为字节数组
	 *
	 * @param date
	 * @return
	 */
	public static byte[] dateToBytes(Date date) {
		Calendar calendar = Calendar.getInstance();
		byte[] byteArray = new byte[7];
		calendar.setTime(date);
		short year = (short) calendar.get(Calendar.YEAR);
		byteArray[0] = (byte) ((year >>> 8) & 0xFF);
		byteArray[1] = (byte) (year & 0xFF);
		byteArray[2] = (byte) (calendar.get(Calendar.MONTH) + 1);
		byteArray[3] = (byte) calendar.get(Calendar.DATE);
		byteArray[4] = (byte) calendar.get(Calendar.HOUR_OF_DAY);
		byteArray[5] = (byte) calendar.get(Calendar.MINUTE);
		byteArray[6] = (byte) calendar.get(Calendar.SECOND);
		return byteArray;
	}

	public static Date getSunday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		Date monday = currentDate.getTime();
		return monday;
	}

	public static Date getNextMonday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		Date monday = currentDate.getTime();
		return monday;
	}

	private static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期一是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}

	public static int getDayOfWeekIndex(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (index == 0) {
			index = 7;
		}
		return index;
	}

	/**
	 * 获取本周的第几天5点
	 *
	 * @param i    第几天（1~7 星期一为第一天）
	 * @return
	 */
	public static Date getDayOfCurrentWeek(int i) {
		Calendar calendar = Calendar.getInstance();
		int index = calendar.get(Calendar.DAY_OF_WEEK) - (i + 1);
		calendar.add(Calendar.DATE, index * -1);
		calendar.set(Calendar.HOUR_OF_DAY, 5);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取上周的第几天5点
	 *
	 * @param i    第几天（1~7 星期一为第一天）
	 * @return
	 */
	public static Date getDayOfLastWeek(int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getFirstDayOfWeek(new Date()));
		calendar.add(Calendar.DATE, -1);
		int index = calendar.get(Calendar.DAY_OF_WEEK) - (i + 1);
		calendar.add(Calendar.DATE, index * -1);
		calendar.set(Calendar.HOUR_OF_DAY, 5);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取某周的第一天
	 *
	 * @param date 该周的第一天
	 * @return yyyyMMdd
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		calendar.add(Calendar.DATE, index * -1);
		return calendar.getTime();
	}

	public static Date getLastDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int index = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DATE, 7 - index);
		return calendar.getTime();
	}

	/**
	 * 将日期转为 yyyy-MM-dd HH:mm:ss 格式
	 */
	public static String parseDateToStr(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	public static boolean isTimeOut(Date expDate) {
		Calendar curentDate = Calendar.getInstance();
		Calendar expirtDate = Calendar.getInstance();
		expirtDate.setTime(expDate);
		return expirtDate.getTimeInMillis() - curentDate.getTimeInMillis() <= 0;
	}

	public static Date getSaturday(int nextWeek) {
		int mondayPlus = getMondayPlus();
		if (nextWeek != 0) {
			mondayPlus = mondayPlus + (nextWeek * 7);
		}

		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 5);
		currentDate.set(Calendar.HOUR_OF_DAY, 5);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		Date saturday = currentDate.getTime();
		return saturday;
	}

	/**
	 * 判断是否为周六
	 */
	public static boolean isSaturday(Date date) {
		return getDayOfWeekIndex(date) == 6;
	}

	/**
	 * 判断是否为周日
	 */
	public static boolean isSunday() {
		return getDayOfWeekIndex(new Date()) == 7;
	}
	
	public static Date parseStrToDate(String dateStr) {
		return parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 字符串转换from String
	 */
	public static Date parseDate(String dateStr, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date;
		try {
			date = df.parse(dateStr);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取两个日期相隔天数
	 */
	public static Date getAddHourDate(Date date, int addHour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, addHour);
		return calendar.getTime();
	}

	/**
	 * 获取增加分钟数
	 */
	public static Date getAddMiniteDate(Date date, int addMinute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, addMinute);
		return calendar.getTime();
	}

	/**
	 * 获取两个日期相隔天数
	 */
	public static int getDaysBetweenDate(Date firstDate, Date secondDate) {
		Calendar firstCalendar = Calendar.getInstance();
		firstCalendar.setTime(firstDate);
		firstCalendar.set(Calendar.HOUR_OF_DAY, 0);
		firstCalendar.set(Calendar.MINUTE, 0);
		firstCalendar.set(Calendar.SECOND, 0);

		Calendar secondCalendar = Calendar.getInstance();
		secondCalendar.setTime(secondDate);
		secondCalendar.set(Calendar.HOUR_OF_DAY, 0);
		secondCalendar.set(Calendar.MINUTE, 0);
		secondCalendar.set(Calendar.SECOND, 0);

		long firstMillis = firstCalendar.getTimeInMillis();
		long seconMillis = secondCalendar.getTimeInMillis();

		if (firstMillis >= seconMillis) {
			return (int) ((firstMillis - seconMillis) / (24 * 3600 * 1000));
		} else {
			return (int) ((seconMillis - firstMillis) / (24 * 3600 * 1000));
		}
	}

	/**
	 * 检测当前日期与检测日期是否处于同一(天数)周期内
	 *
	 * @param begDate   周期开始时间
	 * @param checkDate 检测日期
	 * @param cycleDays 周期长度(单位:天)
	 * @return
	 */
	public static boolean isSameCycle(Date begDate, Date checkDate, int cycleDays) {
		if (cycleDays <= 0) {
			return false;
		}

		long checkCycleCount = getDaysBetweenDate(begDate, checkDate) / cycleDays;
		long cycleCount = getDaysBetweenDate(begDate, new Date()) / cycleDays;
		return checkCycleCount == cycleCount;
	}

	/**
	 * 计算周期中的第几天
	 *
	 * @param begDate   起始日期
	 * @param endDate   目标日期
	 * @param cycleDays
	 * @return
	 */
	public static int cycleDay(Date begDate, Date endDate, int cycleDays) {
		if (cycleDays <= 0) {
			return 0;
		}

		int cycleCount = getDaysBetweenDate(begDate, endDate) % cycleDays + 1;
		return cycleCount;
	}

	/**
	 * 获取两个时间间隔秒数
	 */
	public static long getIntervalSecond(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}
		long intervalSecond = (endDate.getTime() - startDate.getTime()) / 1000;
		if (intervalSecond < 0) {
			return 0;
		}

		return intervalSecond;
	}

	/**
	 * 是否是本月最后一天
	 */
	public static boolean isLastDayOfMonth() {
		Calendar cDay1 = Calendar.getInstance();
		int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
		int curreDay = monthDay();
		return lastDay == curreDay;
	}
	
	/**
	 * 获取系统今天0时0分0秒距1970年1月1日总秒
	 */
	public static int getNowDaySecond() {
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(new Date());
		nowCalendar.set(Calendar.HOUR_OF_DAY, 0);                  
		nowCalendar.set(Calendar.MINUTE, 0);
		nowCalendar.set(Calendar.SECOND, 0);
		nowCalendar.set(Calendar.MILLISECOND, 0);
		return (int)(nowCalendar.getTimeInMillis() / 1000);
	}
	
	/**
	 * 获取现在是星期几和客户端匹配
	 * @return
	 */
	public static int getNowWeek(){
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DAY_OF_WEEK) - 1;
	}
	
	public static void main(String[] str) {
		
	}
}
