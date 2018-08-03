package com.techwells.teammission.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class DateUtil {
	private static final long ONE_MINUTE = 60;
	private static final long ONE_HOUR = 3600;
	private static final long ONE_DAY = 86400;
	private static final long ONE_MONTH = 2592000;
	private static final long ONE_YEAR = 31104000;

	public static Calendar calendar = Calendar.getInstance();
	
	/**
	 * 将指定的string类型的日期，转换成Date对象
	 * 格式：yyyy-MM-dd HH:mm:ss
	 * @param time
	 * @return  Date对象
	 * @throws ParseException  异常
	 */
	public static Date getDateFromString(String time) throws ParseException{
		String pattern = "yyyy-MM-dd HH:mm:ss";  //按照上面的日期格式定义模板，这个一定要完全和上面的一样，否则转换不正确
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.parse(time);
	}
	
	/**
	 * 将指定的string类型的日期转换成Date对象
	 * @param time  字符串日期
	 * @param pattern  模版  比如 ：yyyy-MM-dd HH:mm:ss
	 * @return Date类型的对象
	 * @throws ParseException  异常
	 */
	public static Date getDateFromString(String time,String pattern) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.parse(time);
	}
	
	/**
	 * 将指定的Date对象转换成指定格式的字符串输出
	 * 输出格式：yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String getDateForFormat(Date date){
		String format="yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}
	
	/**
	 * 比较日期的大小，如果d1大于d2，那么返回true
	 * @param d1  Date类型日期
	 * @param d2 Date类型日期
	 * @return 
	 */
	public static boolean compareTime(Date d1,Date d2){
		return d1.getTime()>d2.getTime();
	}
	
	
	/**
	 * 判断指定日期date是否是未来时间
	 * @param date  日期对象
	 * @return   如果为未来时间，那么返回true
	 */
	public static boolean isFutureTime(Date date){
		return date.getTime()>new Date().getTime();
	}
	
	
	/**
	 * 判断当前日期是否是过去时间
	 * @param date  Date对象
	 * @return  如果为过去时间返回true
	 */
	public static boolean isPastTime(Date date){
		return date.getTime()<new Date().getTime();
	}
	
	
	/**
	 * 判断输入的日期字符串是否符合格式要求  只判断年月日  格式为   yyyy-MM-dd
	 * @param date 日期字符串
	 * @return  如果匹配返回true
	 */
	public static boolean matchDateFormatByY4M2D2(String date){
		String pattern="^\\d{4}\\-\\d{2}\\-\\d{2}$";   //符合正则的表达式
		return Pattern.matches(pattern, date);
	}
	
	/**
	 * 判断输入的日期字符串是否符合格式要求，判断年月日时分秒  格式为：yyyy-MM-dd HH:mm:ss
	 * @param date 日期字符串
	 * @return  如果匹配成功返回true
	 */
	public static boolean matchDateFormatByY4M2D2H2m2s2(String date){
		String pattern="^\\d{4}\\-\\d{2}\\-\\d{2}\\s\\d{2}\\:\\d{2}\\:\\d{2}$";
		return Pattern.matches(pattern, date);
	}
	
	
	/**
	 * 根据给定的开始时间，结束时间，时间间隔获取全部的时间片段
	 * @param startDate  开始时间，格式为  HH:mm
	 * @param endDate    结束时间    格式：HH:mm
	 * @param interval   时间间隔
	 * @return
	 */
	public static List<String> getTimeQuantum(String startDate,String endDate,int interval){
		List<String> list=new ArrayList<String>();
		int shr=Integer.parseInt(startDate.substring(0,2));   //获取开始时间的小时
		int sm=Integer.parseInt(startDate.substring(3));    //获取开始时间的分钟
		int ehr=Integer.parseInt(endDate.substring(0,2));   //获取结束时间的小时
		int em=Integer.parseInt(endDate.substring(3));    //获取结束时间的分钟
		list.add(startDate);
		
		int startH=shr;
		int startM=sm;
		
		for (int i = 1; i <=((ehr-startH)*60+em-startM)/interval; i++) {
			int total=sm+interval;
			//如果时间间隔+开始时间的分钟数小于60
			if(total<60){
				if (total<10) {
					if (shr<10) {
						list.add("0"+shr+":"+"0"+total); 
					}else {
						list.add(shr+":"+"0"+total); 
					}
					
				}else {
					if (shr<10) {
						list.add("0"+shr+":"+total); 
					}else {
						list.add(shr+":"+total); 
					}
				}
				sm=total;  //此时的sm将是total
				//shr不用改变
			}else{   //大于等于60
				int h=total/60;  //获取小时
				int m=total%60;  //获取分钟
				if (m<10) {
					if ((shr+h)<10) {
						list.add("0"+(shr+h)+":0"+m);
					}else {
						list.add((shr+h)+":0"+m);   
					}
					
				}else {
					if ((shr+h)<10) {
						list.add("0"+(shr+h)+":"+m);
					}else {
						list.add((shr+h)+":"+m); 
					}
					   
				}
				sm=m;  //此时的sm将是最新的m
				shr=shr+h;  //此时的shr将是最新的
			}
		}
		return list;
	}
	
	
	/**
	 * 判断两个时间段是否相交
	 * @param d1Start  时间段字符串  比如  06:22
	 * @param d1End    时间段字符串  比如  07:22
	 * @param d2Start  时间段字符串  比如  05:22
	 * @param d2End    时间段字符串  比如  07:22
	 * @return 如果相交返回true
	 */
	public static boolean isIntersect(String d1Start,String d1End,String d2Start,String d2End){
		int d1sh=Integer.parseInt(d1Start.substring(0,2));  //d1开始时间的hour
		int d1sm=Integer.parseInt(d1Start.substring(3));   //d1开始时间的minute
		
		int d1eh=Integer.parseInt(d1End.substring(0,2));  //d1结束时间的hour
		int d1em=Integer.parseInt(d1End.substring(3));   //d1结束时间的minute
		
		int d2sh=Integer.parseInt(d2Start.substring(0,2));  //d2开始时间的hour
		int d2sm=Integer.parseInt(d2Start.substring(3));   //d2结束时间的minute
		
		int d2eh=Integer.parseInt(d2End.substring(0,2));  //d2开始时间的hour
		int d2em=Integer.parseInt(d2End.substring(3));   //d2结束时间的minute
//		System.out.println((d2sh-d1sh)*60+d2sm-d1sm);
//		System.out.println((d2sh-d1eh)*60+d2sm-d1em);
		
		if ((((d2eh-d1sh)*60+d2em-d1sm)>0&&((d2eh-d1eh)*60+d2em-d1em)<0)
				||(((d2sh-d1sh)*60+d2sm-d1sm)>0&&((d2sh-d1eh)*60+d2sm-d1em)<0)
				||(((d1eh-d2sh)*60+d1em-d2sm)>0&&((d1eh-d2eh)*60+d1em-d2em)<0)
				||(((d1sh-d2sh)*60+d1sm-d2sm)>0&&((d1sh-d2eh)*60+d1sm-d2em)<0)) {
			return true;
		}else {
			return false;
		}
		
	}
	
	
	/**
	 * 
	 * @return yyyy-mm-dd 2012-12-25
	 */
	public static String getDate() {
		String day;
		if (Integer.parseInt(getDay()) < 10) {
			day = "0" + getDay();
		} else {
			day = getDay();
		}
		String data = getYear() + "-" + getMonth() + "-" + day;

		return data;
	}

	public static String getMM() {
		return getMonth();
	}

	/**
	 * @param format
	 * @return yyyy年MM月dd HH:mm MM-dd HH:mm 2012-12-25
	 * 
	 */
	public static String getDate(String format) {
		SimpleDateFormat simple = new SimpleDateFormat(format);
		return simple.format(calendar.getTime());
	}

	/**
	 * 
	 * @return yyyy-MM-dd HH:mm 2012-12-29 23:47
	 */
	public static String getDateAndMinute() {
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return simple.format(calendar.getTime());
	}

	/**
	 * 
	 * @return yyyy-MM-dd HH:mm:ss 2012-12-29 23:47:36
	 */
	public static String getFullDate() {
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simple.format(calendar.getTime());
	}

	/**
	 * 距离今天多久
	 * 
	 * @param date
	 * @return
	 * 
	 */
	public static String fromToday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		long time = date.getTime() / 1000;
		long now = new Date().getTime() / 1000;
		long ago = now - time;
		if (ago <= ONE_HOUR)
			return ago / ONE_MINUTE + "分钟前";
		else if (ago <= ONE_DAY)
			return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE)
					+ "分钟前";
		else if (ago <= ONE_DAY * 2)
			return "昨天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
					+ calendar.get(Calendar.MINUTE) + "分";
		else if (ago <= ONE_DAY * 3)
			return "前天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
					+ calendar.get(Calendar.MINUTE) + "分";
		else if (ago <= ONE_MONTH) {
			long day = ago / ONE_DAY;
			return day + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
					+ calendar.get(Calendar.MINUTE) + "分";
		} else if (ago <= ONE_YEAR) {
			long month = ago / ONE_MONTH;
			long day = ago % ONE_MONTH / ONE_DAY;
			return month + "个月" + day + "天前"
					+ calendar.get(Calendar.HOUR_OF_DAY) + "点"
					+ calendar.get(Calendar.MINUTE) + "分";
		} else {
			long year = ago / ONE_YEAR;
			int month = calendar.get(Calendar.MONTH) + 1;// JANUARY which is 0
															// so month+1
			return year + "年前" + month + "月" + calendar.get(Calendar.DATE)
					+ "日";
		}

	}

	/**
	 * 距离截止日期还有多长时间
	 * 
	 * @param date
	 * @return
	 */
	public static String fromDeadline(Date date) {
		long deadline = date.getTime() / 1000;
		long now = (new Date().getTime()) / 1000;
		long remain = deadline - now;
		if (remain <= ONE_HOUR)
			return "只剩下" + remain / ONE_MINUTE + "分钟";
		else if (remain <= ONE_DAY)
			return "只剩下" + remain / ONE_HOUR + "小时"
					+ (remain % ONE_HOUR / ONE_MINUTE) + "分钟";
		else {
			long day = remain / ONE_DAY;
			long hour = remain % ONE_DAY / ONE_HOUR;
			long minute = remain % ONE_DAY % ONE_HOUR / ONE_MINUTE;
			return "只剩下" + day + "天" + hour + "小时" + minute + "分钟";
		}

	}

	/**
	 * 距离今天的绝对时间
	 * 
	 * @param date
	 * @return
	 */
	public static String toToday(Date date) {
		long time = date.getTime() / 1000;
		long now = (new Date().getTime()) / 1000;
		long ago = now - time;
		if (ago <= ONE_HOUR)
			return ago / ONE_MINUTE + "分钟";
		else if (ago <= ONE_DAY)
			return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE) + "分钟";
		else if (ago <= ONE_DAY * 2)
			return "昨天" + (ago - ONE_DAY) / ONE_HOUR + "点" + (ago - ONE_DAY)
					% ONE_HOUR / ONE_MINUTE + "分";
		else if (ago <= ONE_DAY * 3) {
			long hour = ago - ONE_DAY * 2;
			return "前天" + hour / ONE_HOUR + "点" + hour % ONE_HOUR / ONE_MINUTE
					+ "分";
		} else if (ago <= ONE_MONTH) {
			long day = ago / ONE_DAY;
			long hour = ago % ONE_DAY / ONE_HOUR;
			long minute = ago % ONE_DAY % ONE_HOUR / ONE_MINUTE;
			return day + "天前" + hour + "点" + minute + "分";
		} else if (ago <= ONE_YEAR) {
			long month = ago / ONE_MONTH;
			long day = ago % ONE_MONTH / ONE_DAY;
			long hour = ago % ONE_MONTH % ONE_DAY / ONE_HOUR;
			long minute = ago % ONE_MONTH % ONE_DAY % ONE_HOUR / ONE_MINUTE;
			return month + "个月" + day + "天" + hour + "点" + minute + "分前";
		} else {
			long year = ago / ONE_YEAR;
			long month = ago % ONE_YEAR / ONE_MONTH;
			long day = ago % ONE_YEAR % ONE_MONTH / ONE_DAY;
			return year + "年前" + month + "月" + day + "天";
		}

	}

	public static String getYear() {
		return calendar.get(Calendar.YEAR) + "";
	}

	public static String getMonth() {
		int month = calendar.get(Calendar.MONTH) + 1;
		return month + "";
	}

	public static String getDay() {
		return calendar.get(Calendar.DATE) + "";
	}

	public static String get24Hour() {
		return calendar.get(Calendar.HOUR_OF_DAY) + "";
	}

	public static String getMinute() {
		return calendar.get(Calendar.MINUTE) + "";
	}

	public static String getSecond() {
		return calendar.get(Calendar.SECOND) + "";
	}

	public static void main(String[] args) throws ParseException {
		String deadline = "2012-12-30 12:45:59";
		Date date = new Date();
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date = simple.parse(deadline);
		System.out.println(DateUtil.fromDeadline(date));

		String before = "2012-12-12 0:0:59";
		date = simple.parse(before);
		System.out.println(DateUtil.fromToday(date));

		System.out.println(DateUtil.getFullDate());
		System.out.println(DateUtil.getDate());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd EEE");

		Date currentDate = new Date();

		// 比如今天是2012-12-25
		System.out.println("今天的日期: " + sdf.format(currentDate));

		List<String> dayList = new ArrayList<String>();
		System.out.println(dayList);

		System.out.println((Integer.parseInt(DateUtil.getYear()) + 1) + "-"
				+ DateUtil.getMonth() + "-" + DateUtil.getDay() + " "
				+ DateUtil.get24Hour() + ":" + DateUtil.getMinute() + ":"
				+ DateUtil.getSecond());
		String osName = System.getProperty("os.name");

		String user = System.getProperty("user.name");

		System.out.println("当前操作系统是：" + osName);

		System.out.println("当前用户是：" + user);
		/*
		 * System类代表系统，系统级的很多属性和控制方法都放置在该类的内部。该类位于java.lang包。
		 * currentTimeMillis方法public static long
		 * currentTimeMillis()该方法的作用是返回当前的计算机时间，
		 * 时间的表达格式为当前计算机时间和GMT时间(格林威治时间)1970年1月1号0时0分0秒所差的毫秒数。
		 */
		long currentTime = System.currentTimeMillis();

		System.out.println(currentTime);

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年-MM月dd日-HH时mm分ss秒");

		Date d = new Date(currentTime);

		System.out.println(formatter.format(d));

		System.out.println("********************");

		System.out.println(getWeek("2016-01-03", -3, -2));

		System.out.println(getWeek("2016-01-03", -2, -1));

		System.out.println(getWeek("2016-01-03", -1, 0));

		System.out.println("2018-03-18 11:00  30天之后的日期是："
				+ DateUtil.getStringRelativeDate("2018-03-18 11:00", -30));

		System.out.println(simple.format(DateUtil.getDateRelativeDate(
				new Date(), -30)));
	}

	// 获取周时间（根据参数获取上上周s=-3e=-2,上周s=-2e=-1,本周s=-1e=0）
	public static List<String> getWeek(String date, int s, int e) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date da = null;
		try {
			da = sdf.parse(date);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		List<String> strList = new ArrayList<String>();
		Calendar cal1 = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		c.setTime(da);
		cal1.setTime(da);
		int n = s;
		cal1.add(Calendar.DATE, n * 7);// 想周几，这里就传几Calendar.MONDAY(TUESDAY...)
		cal1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		c.add(Calendar.WEEK_OF_MONTH, e);
		String monday = new SimpleDateFormat("yyyy-MM-dd").format(cal1
				.getTime());
		strList.add(monday);
		String sunday = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		strList.add(sunday);
		return strList;
	}

	public static String getTime(String user_time) {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d;
		try {

			d = sdf.parse(user_time);
			long l = d.getTime();
			String str = String.valueOf(l);
			re_time = str.substring(0, 10);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return re_time;
	}

	public static Date getTimeDate(String user_time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(user_time);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	public static String convertDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = null;
		try {
			Date datever = sdf.parse(date);
			int i = datever.compareTo(new Date());
			if (i == 1) {
				System.out.println("输入日期超过了当今日期");
				return null;
			} else if (i == -1) {
				datever.setTime(datever.getTime() - 24 * 60 * 60 * 1000);
				str = sdf.format(datever);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * @description 获取某天之前或者之后的日期
	 * 
	 * @param someDay
	 *            某天
	 * @param duration
	 *            时长(天)
	 * */

	public static String getStringRelativeDate(String someDay, int duration) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar calendar = Calendar.getInstance();// 获取日历实例
		try {
			calendar.setTime(sdf.parse(someDay));
		} catch (ParseException e) {
			e.printStackTrace();
			return "格式化日期失败";
		}
		calendar.add(Calendar.DAY_OF_MONTH, duration); // 设置为后一天
		String relativeDate = sdf.format(calendar.getTime());// 获得后一天
		return relativeDate;
	}

	/**
	 * @description 获取某天之前或者之后的日期
	 * 
	 * @param someDay
	 *            某天
	 * @param duration
	 *            时长(天)
	 * */

	public static Date getDateRelativeDate(Date someDay, int duration)
			throws ParseException {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar calendar = Calendar.getInstance();// 获取日历实例
		calendar.setTime(someDay);
		calendar.add(Calendar.DAY_OF_MONTH, duration); // 设置为后一天
		// String relativeDate = sdf.format(calendar.getTime());// 获得后一天
		return calendar.getTime();
	}

	/**
	 * @description 计算两个时间点之间的绝对时间
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * */

	public static int getAbsoluteTime(Date startTime, Date endTime)
			throws ParseException {
		long deadline = startTime.getTime() / 1000;
		long now = endTime.getTime() / 1000;
		long remain = now - deadline;
		if (remain < 0) {
			return -1;
		}
		return (int) Math.ceil(remain / 60);
	}
}
