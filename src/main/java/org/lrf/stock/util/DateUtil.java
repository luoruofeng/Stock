package org.lrf.stock.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.lrf.stock.comparable.StockDateComparable;
import org.lrf.stock.entity.Stock;
import org.lrf.stock.repository.CodeRepository;
import org.lrf.stock.repository.StockRepository;
import org.lrf.stock.service.stock.AllStockHandler;

public class DateUtil {

	public static Date getDateByDayNumber(int dayNumber,StockRepository stockRepository,List<Stock> stocks,CodeRepository codeRepository
			) {
		/**
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -dayNumber);
		return cal.getTime();
		*/
		
		stocks.sort(new StockDateComparable());
		
		if(stocks.size() < dayNumber)
			return stocks.get(stocks.size()-1).getDate();
		return stocks.get(dayNumber-1).getDate();
	}
	
	public static String getStr(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public static Date addOneDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}
	
	public static LocalDate toLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public static Date  localDatetoDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	
	public static Date getMiddleDate(Date startDate,Date endDate) {
		LocalDate startLocalDate = toLocalDate(startDate);
		LocalDate endLocalDate = toLocalDate(endDate);
		
		return localDatetoDate(startLocalDate.plusDays(startLocalDate.until(endLocalDate, ChronoUnit.DAYS)/2));
	}
}
