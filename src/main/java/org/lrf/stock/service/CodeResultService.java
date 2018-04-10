package org.lrf.stock.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.lrf.stock.comparable.StockCloseComparable;
import org.lrf.stock.entity.Code;
import org.lrf.stock.entity.CodeResult;
import org.lrf.stock.entity.Stock;
import org.lrf.stock.repository.CodeRepository;
import org.lrf.stock.repository.StockRepository;
import org.lrf.stock.util.DateUtil;
import org.lrf.stock.util.Day;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CodeResultService {
	
	@Autowired
	private CodeRepository codeRepository;
	
	@Autowired
	private StockRepository stockRepository;
	
	private static List<Stock> allStock;
	
	private List<Stock> getAllStockesByCode(String codeStr){
		if(allStock == null)
			allStock = stockRepository.getStocksEntityByStock(codeStr);
		
		return allStock;
	}
	
	private List<Stock> getAllStockesByCodeAndStartDate(String codeStr,Date startDate){
		return getFilterStockStream(codeStr,getAfterStartDatePredicate(startDate)).sorted(new StockCloseComparable()).collect(Collectors.toList());
	}
	
	private Stream<Stock> getFilterStockStream(String codeStr,Predicate<Stock> predicate) {
		return getAllStockesByCode(codeStr).stream().filter(predicate);
	}
	
	public DoubleStream getFilterStockCloseStream(String codeStr,Predicate<Stock> predicate) {	
		return getFilterStockStream(codeStr,predicate).mapToDouble((stock)->stock.getClose());
	}
	
	private Predicate<Stock> getAfterStartDatePredicate(Date startDate){
		return (item)->{
			return item.getDate().after(startDate);
		};
	}
	
	/**
	 * 获取评价收盘价
	 * @param codeStr
	 * @param startDate
	 * @return
	 */
	private Double getAvgPrice(String codeStr,Date startDate) {	
		return getFilterStockCloseStream(codeStr,getAfterStartDatePredicate(startDate)).summaryStatistics().getAverage();
	}
	
	/**
	 * 获取低于平均价的stock
	 * @param codeStr
	 * @param startDate
	 * @param avg
	 * @return
	 */
	public List<Stock> getBelowAvg(String codeStr,Date startDate,Double avg){
		return getFilterStockStream(codeStr,getAfterStartDatePredicate(startDate).and((stock)->{return stock.getClose() < avg;})).collect(Collectors.toList());
	}
	
	/**
	 * 获取高于平均价的stock
	 * @param codeStr
	 * @param startDate
	 * @param avg
	 * @return
	 */
	public List<Stock> getAboveAvg(String codeStr,Date startDate,Double avg){
		return getFilterStockStream(codeStr,getAfterStartDatePredicate(startDate).and((stock)->{return stock.getClose() >= avg;})).collect(Collectors.toList());
	}

	/**
	 * 获取最低价格stock list
	 * @param codeStr
	 * @param startDate
	 * @param avg
	 * @return
	 */
	public List<Stock> getBelowAvgMins(String codeStr,Date startDate,Double avg) {
		return getBelowAvg(codeStr,startDate,avg).stream().sorted(new StockCloseComparable()).collect(Collectors.toList());
	}
	
	/**
	 * 获取最高价格stock list
	 * @param codeStr
	 * @param startDate
	 * @param avg
	 * @return
	 */
	public List<Stock> getAboveAvgMaxs(String codeStr,Date startDate,Double avg) {
		List<Stock> stocks = getAboveAvg(codeStr,startDate,avg).stream().sorted(new StockCloseComparable()).collect(Collectors.toList());
		Collections.reverse(stocks);
		return stocks;
	}
	
	/**
	 * 获取最低收盘价的stock
	 * @param codeStr
	 * @param startDate
	 * @param avg
	 * @param listTotal
	 * @return
	 */
	public Stock getLowestClose(String codeStr,Date startDate,Double avg) {
		List<Stock> stocks = getAllStockesByCodeAndStartDate(codeStr, startDate);
		if(stocks != null && stocks.size() > 0)
			return stocks.get(0);
		else
			return null;
	}
	
	/**
	 * 获取最高收盘价的stock
	 * @param codeStr
	 * @param startDate
	 * @param avg
	 * @param listTotal
	 * @return
	 */
	public Stock getHighestClose(String codeStr,Date startDate,Double avg) {
		List<Stock> stocks = getAllStockesByCodeAndStartDate(codeStr, startDate);
		if(stocks != null && stocks.size() > 0)
			return stocks.get(stocks.size()-1);
		else
			return null;
	}
	
	private CodeResult createCodeResult(String code, int numberOfDays) {
		return new CodeResult(code,numberOfDays);
	}
	
	private static int NUMBER_OF_CODE_RESULT = 0;
	
	public void setCodeResult(Code code) {
		for(Day day : Day.values()) {
			NUMBER_OF_CODE_RESULT+=1;
			code.getCodeResults().put(day.value, caculateCodeResultContent(code,createCodeResult(code.getCode(),day.value),day.value));
		}
	}
	
	private double setAvg(Code code,CodeResult cr,Date startDate) {
		Double avg = getAvgPrice(code.getCode(),startDate);
		if (avg != null) {
			cr.setAvg(avg);
		}
		return avg;
	}
	
	private void setMax(Code code,CodeResult cr,Date startDate,Double avg) {
		Stock highestStock = getHighestClose(code.getCode(), startDate, avg);
		if(highestStock != null)
			cr.setMax(highestStock.getClose());
	}
	
	private void setMin(Code code,CodeResult cr,Date startDate,Double avg) {
		Stock LowestStock = getLowestClose(code.getCode(), startDate, avg);
		if(LowestStock != null)
			cr.setMin(LowestStock.getClose());
	}
	
	private void setAboveAvgMaxs(Code code,CodeResult cr,Date startDate,Double avg) {
		List<Stock> aboveAvgMaxs = getAboveAvgMaxs(code.getCode(), startDate, avg);
		if(aboveAvgMaxs != null)
			cr.setAboveAvgMaxs(aboveAvgMaxs);
	}
	
	private void setBelowAvgMins(Code code,CodeResult cr,Date startDate,Double avg) {
		List<Stock> belowAvgMins = getBelowAvgMins(code.getCode(), startDate, avg);
		if(belowAvgMins != null)
			cr.setBelowAvgMins(belowAvgMins);
	}
	
	public CodeResult caculateCodeResultContent(Code code,CodeResult cr,int day) {
		Date startDate = DateUtil.getDateByDayNumber(day);
		
		Double avg = setAvg(code,cr,startDate);
		setMax(code,cr,startDate,avg);
		setMin(code, cr, startDate, avg);
		setAboveAvgMaxs(code, cr, startDate, avg);
		setBelowAvgMins(code, cr, startDate, avg);
		
		printAllStockesByCodeAndStartDate(code.getCode(),startDate);
		System.out.println(cr);
		System.out.println("-----------------------------");
		System.out.println("\n");
		return cr;
	}
	
	
	private void printAllStockesByCodeAndStartDate(String codeStr,Date startDate) {
		List<Stock> stocks = getAllStockesByCodeAndStartDate(codeStr,startDate);
		StringBuffer sb = new StringBuffer(((Integer)stocks.size()).toString()).append(" : ");
		stocks.forEach(s->{
			sb.append(DateUtil.getStr(s.getDate())).append(" ").append(s.getClose()).append(" ");
		});
		System.out.println(sb.toString());
	}
	
	/**
	 * 清除并设置code的codeResult
	 */
	public void setAllCodeResult() {
		List<Code> codes = codeRepository.findAll();
		for (Code code : codes) {
			code.cleanCodeResults();
			setCodeResult(code);
		}
		System.out.println(NUMBER_OF_CODE_RESULT+" finish!!!!!");
		codeRepository.saveCodes(codes);
	}
}
