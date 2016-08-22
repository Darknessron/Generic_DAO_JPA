
package com.rontseng.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Describe the type here.
 *
 * @author Ron
 * @date Mar 24, 2016 5:31:00 PM
 */
public class DateUtil {

	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String TW_DATE_PATTERN = "yyy.MM.dd";

	/**
	 * Convert Date to XMLGregorianCalendar
	 * 
	 * @param date
	 * @return
	 */
	public static XMLGregorianCalendar toXMLDate(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		XMLGregorianCalendar gc = null;
		try {
			gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gc;
	}

	/**
	 * Convert XMLGregorianCalendar to Date
	 * 
	 * @param calendar
	 * @return
	 */
	public static Date toDate(XMLGregorianCalendar calendar) {
		return calendar.toGregorianCalendar().getTime();
	}

	/**
	 * Display date in Chinese format
	 * 
	 * @param date
	 * @param isShowWeek
	 *            Is display week day.
	 * @return
	 */
	public static String getDisplayDate(Date date, boolean isShowWeek) {
		String result = null;
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(date);
		int year = currentDate.get(Calendar.YEAR) - 1911;
		int month = currentDate.get(Calendar.MONTH) + 1;
		int day = currentDate.get(Calendar.DAY_OF_MONTH);
		if (isShowWeek) {
			String weekDay = currentDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.TAIWAN);
			result = String.format("中華民國%1$03d年%2$02d月%3$02d日 %4$s", year, month, day, weekDay);
		} else {
			result = String.format("中華民國%1$03d年%2$02d月%3$02d日", year, month, day);
		}
		return result;
	}

	/**
	 * Display current Date
	 * 
	 * @return
	 */
	public static String getStatusDate() {
		return getDisplayDate(getCurrentDate(), true);
	}

	/**
	 * Get current Timestamp
	 * 
	 * @return
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}

	public static String getCurrentDateTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(new Date());
	}

	public static String getCurrentDateYear() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		return formatter.format(new Date());
	}

	/**
	 * Get current date
	 * 
	 * @return
	 */
	public static Date getCurrentDate() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * Format date String to Date object
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static Date parse(String dateStr, String format) throws Exception {
		// Taiwanese Date pattern
		if (TW_DATE_PATTERN.equals(format)) {
			String[] dateArray = dateStr.split("\\.");
			Calendar date = Calendar.getInstance();
			date.set(Calendar.YEAR, Integer.parseInt(dateArray[0]) + 1911);
			date.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
			date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]));

			return date.getTime();
		}

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		return sdf.parse(dateStr);
	}

	/**
	 * Convert Date Object to Date String
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String parse(Date date, String format) throws Exception {
		// Taiwanese Date pattern
		if (TW_DATE_PATTERN.equals(format)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			String year = (calendar.get(Calendar.YEAR) - 1911) + "";
			String month = (calendar.get(Calendar.MONTH) + 1) + "";
			String day = calendar.get(Calendar.DAY_OF_MONTH) + "";

			return String.format("%1$03d.%2$02d.%3$02d", Integer.parseInt(year), Integer.parseInt(month),
					Integer.parseInt(day));
		}
		return new SimpleDateFormat(format).format(date);
	}

	public static String convertIntoTWFormat(String _date, String symbol) {
		if (_date.length() != 8) {
			return _date;
		}
		try {
			int _year = Integer.parseInt(_date.substring(0, 4)) - 1911;
			_date = _year + symbol + _date.substring(4, 6) + symbol + _date.substring(6, 8);
		} catch (Exception ex) {
		}
		return _date;
	}

	/**
	 * Convert java.util.Date to java.sql.Date
	 * 
	 * @param date
	 * @return
	 */
	public static java.sql.Date convertToSQLDate(Date date) {
		return new java.sql.Date(date.getTime());
	}

	public static String getTWSystemYear() {
		String year = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		year = formatter.format(new Date());
		year = String.valueOf(Integer.parseInt(formatter.format(new Date())) - 1911);
		return year;
	}
}
