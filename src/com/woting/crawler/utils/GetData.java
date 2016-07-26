package com.woting.crawler.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class GetData {

	public static int getWeekDayNum() {
		Date data = new Date();
		Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        int w = cal.get(Calendar.DAY_OF_WEEK);
        return w;
	}
	
	public static String getDayNum() {
		Date date = new Date();
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
		String str = dateFm.format(date);
		return str;
	}
}
