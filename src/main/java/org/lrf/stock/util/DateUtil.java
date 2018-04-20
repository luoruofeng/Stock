package org.lrf.stock.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.lrf.stock.comparable.StockDateComparable;
import org.lrf.stock.entity.Stock;
import org.lrf.stock.repository.CodeRepository;
import org.lrf.stock.repository.StockRepository;
import org.lrf.stock.service.stock.AllStockHandler;

public class DateUtil {

	public static Date getDateByDayNumber(int dayNumber,StockRepository stockRepository,String code,CodeRepository codeRepository
			) {
		/**
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -dayNumber);
		return cal.getTime();
		*/
		
		List<Stock> stock = stockRepository.getStocksEntityByStock(code);
		stock.sort(new StockDateComparable());
		
		if(stock.size() < dayNumber)
			return null;
		return stock.get(dayNumber-1).getDate();
	}
	
	public static String getStr(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
		
	}
}
