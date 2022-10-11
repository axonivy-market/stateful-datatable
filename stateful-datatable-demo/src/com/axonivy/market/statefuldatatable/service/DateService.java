package com.axonivy.market.statefuldatatable.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class DateService {
	public static Date setTimeZero(Date originalDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(originalDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime();
	}
	
	public static Date setTimeMidnight(Date originalDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(originalDate);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 99);
		
		return calendar.getTime();
	}
	
	public static Date setTimeZero(LocalDate originalDate) {
		return Date.from(originalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static Date setTimeMidnight(LocalDate originalDate) {	
		return Date.from(originalDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static String dateAsString(Date date) {
		if(date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");	
		return sdf.format(date);
	}
	
	public static Date stringAsDate(String date) throws ParseException {
		if(StringUtils.isEmpty(date)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");	
		return sdf.parse(date);
	}
}
