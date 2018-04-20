package org.lrf.stock.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.lrf.stock.comparable.StockCloseComparable;
import org.lrf.stock.entity.Code;
import org.lrf.stock.entity.CodeResult;
import org.lrf.stock.entity.CodeResultCalculator;
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

	private List<Stock> getAllStockesByCode(final String codeStr) {
		return stockRepository.getStocksEntityByStock(codeStr);
	}

	

	private static int NUMBER_OF_CODE_RESULT = 0;

	public void setCodeResult(Code code) {
		
	}

	

	/**
	 * 清除并设置code的codeResult
	 */
	public void saveAllCodeResult() {
		
		System.out.println("------------caculate code result and save --------------------");
		
		List<Code> codes = codeRepository.findAll();
		for (Code code : codes) {
			List<Stock> stocks = stockRepository.getStocksEntityByStock(code.getCode());
			
			Map<Integer,CodeResult> codeResults = new HashMap<>();
			
			for (Day day : Day.values()) {
				
				Date startDate = DateUtil.getDateByDayNumber(day.value,stockRepository,code.getCode(), codeRepository);
				
				codeResults.put(day.value,new CodeResultCalculator(stocks, day, code,startDate).getCodeResult());
			}

			code.cleanCodeResults();
			code.setCodeResults(codeResults);
			codeRepository.saveOrUpdateCode(code);
		}
		
		System.out.println(NUMBER_OF_CODE_RESULT + " finish!!!!!");
	}


	
}
