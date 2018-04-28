package org.lrf.stock.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CodeResultServiceImpl implements CodeResultService {

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

		System.out.println("------------caculate code result and save --------------------");

		List<Code> codes = codeRepository.findAll();
		for (Code code : codes) {
			Stock startDateStock = stockRepository.getFirstStock(code.getCode());
			Stock lastDateStock = stockRepository.getLastStock(code.getCode());

			for (Date currentDate = startDateStock.getDate(); !currentDate
					.after(lastDateStock.getDate()); currentDate = DateUtil.addOneDay(currentDate)) {
				NUMBER_OF_CODE_RESULT += 1;
				System.out.println("\n\n\n calculate code result completed code:" + code.getCode() + " date:"
						+ DateUtil.getStr(currentDate));
				this.saveCodeResult(currentDate, code);

			}
		}

		System.out.println(NUMBER_OF_CODE_RESULT + " finish!!!!!");
	}

	private List<Stock> setStockListByCalculateDate(List<Stock> stocks, Date calculateDate) {
		stocks.sort(new StockDateComparable());

		try {
			Date halfOfBetweenDays = DateUtil.getMiddleDate(stocks.get(stocks.size() - 1).getDate(),
					stocks.get(0).getDate());

			Order order = null;
			System.out.println(halfOfBetweenDays + "    " + calculateDate.after(halfOfBetweenDays));
			if (calculateDate.after(halfOfBetweenDays)) {
				order = Order.DESC;
			} else {
				Collections.reverse(stocks);
				order = Order.ASC;
			}
			return subListByOrder(stocks, order, calculateDate);

		} catch (Exception e) {
			throw e;
		}
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

	public static boolean isNumericzidai(String str) {
		Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	@Override
	public Map<String, Object> getCodeResultByKeyWordAndPeriod(String keyWord, Date startDate, Date endDate) {
		
		Map<String, Object> result = new HashMap<>();
		
		String code = null;
		
		if(!isNumericzidai(keyWord)) {
			Code codeEntity = null;
			if((codeEntity = codeRepository.getCodeByName(keyWord)) == null) {
				result.put(Keys.MESSAGE, Keys.NO_STOCK);
				return result;
			}else {
				code = codeEntity.getCode();
			}
		}else {
			if(codeRepository.getCodeEntityByCode(keyWord) == null) {
				result.put(Keys.MESSAGE, Keys.NO_STOCK);
				return result;
			}else {
				code = keyWord;
			}
		}
		
		result.put(Keys.CODE_RESULTS,codeResultRepository.getCodeResultByKeyWordAndPeriod(code, startDate, endDate, Day.getDays()));
		
		return result;
	}

}
