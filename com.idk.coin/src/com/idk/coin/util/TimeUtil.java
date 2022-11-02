package com.idk.coin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author idwook <br>
 * @description
 * yyyyMMdd HH:mm:ss.SSS zzz 20000101 01:01:01.000 GMT	<br>
 * yyyy-MM-dd HH:mm:ss 2000-01-01 01-01-01	<br>
 * �⵵ yyyy,YYYY <br>
 * �� MM<br>
 * �� DD,dd<br>
 * �ð� hh<br>
 * �� mm<br>
 * �� ss<br>
 * 
 */
public class TimeUtil {
		public static String getCurrentTime(String form){
			Calendar today = Calendar.getInstance(Locale.KOREA);
			SimpleDateFormat form1 = new SimpleDateFormat(form);
			return form1.format(today.getTime());
		}
		public static String getNextMonth(String datetime) throws ParseException{
			Date date = getDate("yyyy-MM-dd", datetime);
			long now = date.getTime();
			long value = ((long)1000*60*60*24)*(long)33;
			long before = now + value;
			return getDateFormat("yyyy-MM-01",before);
		}
		public static String getLastDay(String datetime) throws ParseException{
			Date date = getDate("yyyy-MM-dd", getNextMonth(datetime));
			long now = date.getTime();
			long value = ((long)1000*60*60*24)*(long)1;
			long before = now - value;
			return getDateFormat("yyyy-MM-dd",before);
		}
		public static String getCurrentTimeBefore(String form,int day){
			Calendar today = Calendar.getInstance(Locale.KOREA);
			long now = System.currentTimeMillis();
			long value = ((long)1000*60*60*24)*(long)day;
			long before = now - value;
			return getDateFormat(form,before);
		}
		public static String getCurrentTimeAfter(String form,int day){
			long now = System.currentTimeMillis();
			long value = ((long)1000*60*60*24)*(long)day;
			long after = now + value;
			return getDateFormat(form,after);
		}
		public static String getDateFormat(String form,long mili){
			Date date = new Date(mili);
			return getDateFormat(form,date);
		}
		/**
		 * @return  HH:mm:ss
		 */
		public static String getTimeFormat(String form,String time){
			return getDateFormat(form, "2000-01-01 "+time);
		}
		public static String getDateFormat(String form,Date date){
			Calendar today = Calendar.getInstance(Locale.KOREA);
			SimpleDateFormat form1 = new SimpleDateFormat(form);
			return form1.format(date.getTime());
		}
		public static Date getDate(String form,String date) throws ParseException{
			SimpleDateFormat form1 = new SimpleDateFormat(form);
			return form1.parse(date);
		}
		public static Date getDefaultDate(String date) throws ParseException{
			return getDate("yyyy-MM-dd HH:mm:ss",date);
		}
		public static String getDate(String timestamp){
			return getDateFormat("yyyy-MM-dd", timestamp);
		}
		public static String getDateTimeFormat(String timestamp){
			return getDateFormat("yyyy-MM-dd HH:mm", timestamp);
		}
		public static String getDateFormat(Date date){
			return getDateFormat("yyyy-MM-dd HH:mm:ss", date);
		}
		public static String getDateFormat(String timestamp){
			return getDateFormat("yyyy-MM-dd HH:mm:ss", timestamp);
		}
		public static String getDateFormat(String form,String timestamp){
			if(timestamp == null || timestamp.length()==0 || timestamp.equals("")) return "";
			
			if(timestamp.trim().toUpperCase().equals("NOW()")) return timestamp;
			//2011-07-03 01:01:01.0
			
			if(timestamp.length()<13) return timestamp;
			Date date =  parsingTimeStamp(timestamp);
			SimpleDateFormat form1 = new SimpleDateFormat(form);
			return form1.format(date.getTime());
		}
		public static String getTimeFormat(String time){
			if(time == null || time.length() == 0) return "";
			if(time.trim().toUpperCase().equals("NOW()")) return time;
			
			return getDateFormat("HH:mm","1970-01-01 "+time);
		}
		
		public static Date parsingTimeStamp(String timestamp){
			//2011-07-03 01:01:01.0
			int year = Integer.parseInt(timestamp.substring(0, 4))-1900;
			int month = Integer.parseInt(timestamp.substring(5, 7))-1;
			int day = Integer.parseInt(timestamp.substring(8, 10));
			int hours = Integer.parseInt(timestamp.substring(11, 13));
			int minutes = Integer.parseInt(timestamp.substring(14, 16));
			int seconds = 0;
			if(timestamp.length()>18) {
				  seconds = (int)Double.parseDouble(timestamp.substring(17,19));
			} 
		
			Date date = new Date(year,month,day,hours,minutes,seconds);
			return date;
		}
		public static String passTime(long start,long end){
			Date sdate = new Date(start);
			Date edate = new Date(end);
			int shours = sdate.getHours();
			int smins = sdate.getMinutes();
			int sseconds = sdate.getSeconds();
			int ehours = edate.getHours();
			int emins = edate.getMinutes();
			int eseconds = edate.getSeconds();
			int hours= shours - ehours;
			int mins = smins - emins;
			int seconds = sseconds - eseconds;
			
			return hours +":"+mins+":"+seconds;
		}
	}