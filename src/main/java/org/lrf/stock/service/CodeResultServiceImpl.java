package org.lrf.stock.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lrf.stock.comparable.StockDateComparable;
import org.lrf.stock.entity.Code;
import org.lrf.stock.entity.CodeResult;
import org.lrf.stock.entity.Stock;
import org.lrf.stock.repository.CodeRepository;
import org.lrf.stock.repository.CodeResultRepository;
import org.lrf.stock.repository.StockRepository;
import org.lrf.stock.service.calculator.CodeResultCalculator;
import org.lrf.stock.util.DateUtil;
import org.lrf.stock.util.Day;
import org.lrf.stock.util.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CodeResultServiceImpl implements CodeResultService {

	private static Logger logger = LoggerFactory.getLogger(CodeResultServiceImpl.class);
	
	@Autowired
	private CodeRepository codeRepository;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private CodeResultRepository codeResultRepository;

	private static int NUMBER_OF_CODE_RESULT = 0;

	public void setCodeResult(Code code) {

	}

	public void saveAllCodeResult() {

		logger.info("------------ Caculate codeResult and Save to Mongo DB --------------------");
		Date startDate = new Date();
		List<Code> codes = codeRepository.findAll();
		for (Code code : codes) {
			Stock startDateStock = stockRepository.getFirstStock(code.getCode());
			Stock lastDateStock = stockRepository.getLastStock(code.getCode());

			for (Date currentDate = startDateStock.getDate(); !currentDate
					.after(lastDateStock.getDate()); currentDate = DateUtil.addOneDay(currentDate)) {
				NUMBER_OF_CODE_RESULT += 1;
				
				this.saveCodeResult(currentDate, code);
				
				logger.info("Calculate codeResult and save to mongo DB. "+" code:"+code+" date:"+currentDate+" Start Date of calculate codeResult :"+startDateStock +" End Date of calculate codeResult :"+lastDateStock );

			}
		}

		logger.info("Calculate codeResult. Total of"+NUMBER_OF_CODE_RESULT + " codeResult calculated !!!!! spend " +((new Date().getTime()-startDate.getTime())/1000/60)+" min");
	}

	private List<Stock> setStockListByCalculateDate(List<Stock> stocks, Date calculateDate) {
		stocks.sort(new StockDateComparable());

		try {
			Date halfOfBetweenDays = DateUtil.getMiddleDate(stocks.get(stocks.size() - 1).getDate(),
					stocks.get(0).getDate());

			Order order = null;
			if (calculateDate.after(halfOfBetweenDays)) {
				order = Order.DESC;
			} else {
				Collections.reverse(stocks);
				order = Order.ASC;
			}

			return subListByOrder(stocks, order, calculateDate);
		} catch (Exception e) {
			 logger.debug("Get HalfOfBetweenDays Exception:"+e);
		}
		return null;
	}

	enum Order {
		ASC, DESC;
	}

	private List<Stock> subListByOrder(List<Stock> stocks, CodeResultServiceImpl.Order order, Date calculateDate) {

		List<Stock> result = null;

		int currentIndex = 0;
		while (currentIndex < stocks.size()) {
			if (stocks.get(currentIndex).getDate().equals(calculateDate) && calculateDate != null) {
				if (order.equals(Order.ASC)) {
					result = stocks.subList(0, currentIndex + 1);
					Collections.reverse(result);
				}

				if (order.equals(Order.DESC)) {
					result = stocks.subList(currentIndex, stocks.size());
				}
				break;
			} else {
				if (order.equals(Order.ASC)) {
					if (stocks.get(currentIndex).getDate().after(calculateDate)) {
						break;
					}
				}

				if (order.equals(Order.DESC)) {
					if (stocks.get(currentIndex).getDate().before(calculateDate)) {
						break;
					}
				}
			}
			currentIndex += 1;
		}
		return result;
	}

	/**
	 * 清除并设置code的codeResult
	 */
	public void saveCodeResult(Date calculateDate, Code code) {

		List<Stock> stocks = stockRepository.getStocksEntityByStock(code.getCode());
		stocks = this.setStockListByCalculateDate(stocks, calculateDate);
		if (stocks == null)
			return;

		List<CodeResult> codeResults = new ArrayList<>();

		for (Day day : Day.values()) {
			Date startDate = DateUtil.getDateByDayNumber(day.value, stockRepository, stocks, codeRepository);
			codeResults.add(new CodeResultCalculator(stocks, day, code, startDate, calculateDate).getCodeResult());
		}

		codeResultRepository.saveCodeResults(codeResults);

	}

	@Autowired
	private KeywordService keywordService;

	@Override
	public Map<String, Object> getCodeResultByKeyWordAndPeriod(String keyWord, Date startDate, Date endDate) {
		
		Map<String, Object> result = new HashMap<>();
		
		String code = null;
		
		try {
			code = keywordService.getCodeFromKeyword(keyWord);
		} catch (Exception e) {
			logger.debug("Get Code Exception "+e);
			result.put(Keys.MESSAGE, e.toString());
			return result;
		}
		
		result.put(Keys.CODE_RESULTS,codeResultRepository.getCodeResultByKeyWordAndPeriod(code, startDate, endDate, Day.getDays()));
		
		return result;
	}

}
