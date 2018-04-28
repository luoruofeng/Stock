package org.lrf.stock.service.calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import javax.management.RuntimeErrorException;

import org.lrf.stock.comparable.StockCloseComparable;
import org.lrf.stock.entity.Code;
import org.lrf.stock.entity.CodeResult;
import org.lrf.stock.entity.Stock;
import org.lrf.stock.util.DateUtil;
import org.lrf.stock.util.Day;

public class CodeResultCalculator {
	private List<Stock> stocks;
	private Stock currentStock;
	private Day day;
	private Code code;
	private Date calculatorDate;
	private Date startDate;
	private CodeResult codeResult;
	private Double avg;
	private Stock highestStock;
	private Stock lowestStock;
	private List<Stock> aboveAvgMaxs;
	private List<Stock> belowAvgMins;
	private Predicate<Stock> afterStartDatePredicate;

	public CodeResultCalculator(List<Stock> allStocks, Day day, Code code, Date startDate,Date date) {
		
		/**
		 * 给参数赋值
		 */
		this.day = day;
		this.code = code;
		this.startDate = startDate;
		this.calculatorDate = date;


		//排序后 给stocks赋值
		this.stocks = getStockesFromStartDate(allStocks);
		
		//获取并设置当天的stock
		setCurrentStock();
		
		//创建 code result
		this.codeResult = createCodeResult();

		 //计算
		caculateCodeResultContent();
		
		//code result 赋值
		setCodeResult();
		
		printAllStockesByCodeAndStartDate();
		System.out.println(codeResult);
		System.out.println("-----------------------------");
		System.out.println("\n");
	}
	
	private void setCurrentStock() {
		List<Stock> oneStockList =  this.stocks.stream().filter(s->{return s.getDate().equals(calculatorDate);}).collect(Collectors.toList());
		if(oneStockList == null || oneStockList.isEmpty()) {
			throw new RuntimeException("current day is not in stock list :"+calculatorDate.toString());
		}
		this.currentStock = oneStockList.get(0);
	}
	
	public CodeResult getCodeResult() {
		return this.codeResult;
	}

	private CodeResult createCodeResult() {
		return new CodeResult(code.getCode() , day.value ,calculatorDate,this.currentStock.getClose());
	}

	private List<Stock> getStockesFromStartDate(List<Stock> allStocks) {
		return allStocks.stream().filter(getAfterStartDatePredicate()).sorted(new StockCloseComparable())
		.collect(Collectors.toList());
	}
	
	
	public DoubleStream getFilterStockCloseStream(Predicate<Stock> predicate) {
		return stocks.stream().filter(predicate).mapToDouble((stock) -> stock.getClose());
	}

	private Predicate<Stock> getAfterStartDatePredicate() {
		if (afterStartDatePredicate == null) {
			afterStartDatePredicate = (item) -> {
				if (startDate == null)
					return false;
				return !item.getDate().before(startDate);
			};
		}
		return afterStartDatePredicate;
	}

	/**
	 * 获取低于平均价的stock
	 * 
	 * @param codeStr
	 * @param startDate
	 * @param avg
	 * @return
	 */
	public List<Stock> getBelowAvg() {
		return stocks.stream().filter(getAfterStartDatePredicate().and((stock) -> {
			return stock.getClose() < avg;
		})).collect(Collectors.toList());
	}

	/**
	 * 获取高于平均价的stock
	 * 
	 * @param codeStr
	 * @param startDate
	 * @param avg
	 * @return
	 */
	public List<Stock> getAboveAvg() {
		return stocks.stream().filter(getAfterStartDatePredicate().and((stock) -> {
			return stock.getClose() >= avg;
		})).collect(Collectors.toList());
	}

	/**
	 * 获取最低价格stock list
	 * 
	 * @param codeStr
	 * @param startDate
	 * @param avg
	 * @return
	 */
	public List<Stock> getBelowAvgMins() {
		return getBelowAvg().stream().sorted(new StockCloseComparable()).collect(Collectors.toList());
	}

	/**
	 * 获取最高价格stock list
	 * 
	 * @param codeStr
	 * @param startDate
	 * @param avg
	 * @return
	 */
	public List<Stock> getAboveAvgMaxs() {
		List<Stock> stocks = getAboveAvg().stream().sorted(new StockCloseComparable()).collect(Collectors.toList());
		Collections.reverse(stocks);
		return stocks;
	}

	/**
	 * 获取最低收盘价的stock
	 * 
	 * @param codeStr
	 * @param startDate
	 * @param avg
	 * @param listTotal
	 * @return
	 */
	public Stock getLowestClose() {
		if (stocks != null && stocks.size() > 0)
			return stocks.get(0);
		else
			return null;
	}

	/**
	 * 获取最高收盘价的stock
	 * 
	 * @param codeStr
	 * @param startDate
	 * @param avg
	 * @param listTotal
	 * @return
	 */
	public Stock getHighestClose() {
		if (stocks != null && stocks.size() > 0)
			return stocks.get(stocks.size() - 1);
		else
			return null;
	}

	private void printAllStockesByCodeAndStartDate() {
		StringBuffer sb = new StringBuffer(((Integer) stocks.size()).toString()).append(" : ");
		stocks.forEach(s -> {
			sb.append(DateUtil.getStr(s.getDate())).append(" ").append(s.getClose()).append(" ");
		});
		System.out.println(sb.toString());
	}

	private void setAvg() {
		Double test =stocks.stream().mapToDouble(s->{return s.getClose();}).summaryStatistics().getAverage();
		this.avg = getFilterStockCloseStream(getAfterStartDatePredicate()).summaryStatistics().getAverage();
	}
	
	private void setCodeResult() {
		if (avg != null)
			codeResult.setAvg(avg);
		
		if (highestStock != null)
			codeResult.setMax(highestStock.getClose());
		
		if (lowestStock != null)
			codeResult.setMin(lowestStock.getClose());
		
		if (aboveAvgMaxs != null)
			codeResult.setAboveAvgMaxs(aboveAvgMaxs);
		
		if (belowAvgMins != null)
			codeResult.setBelowAvgMins(belowAvgMins);
	}

	private void setMax() {
		this.highestStock = getHighestClose();
	}

	private void setMin() {
		lowestStock = getLowestClose();
	}

	private void setAboveAvgMaxs() {
		aboveAvgMaxs = getAboveAvgMaxs();
	}

	private void setBelowAvgMins() {
		belowAvgMins = getBelowAvgMins();
	}

	public void caculateCodeResultContent() {
		
		setAvg();
		setMax();
		setMin();
		setAboveAvgMaxs();
		setBelowAvgMins();
	}


	
}
