package org.lrf.stock.controller;

import java.util.Date;
import java.util.Map;

import org.lrf.stock.service.StockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StockController {
	
	@Autowired
	private StockServiceImpl stockService;
	
	@RequestMapping("stocks")
	@ResponseBody
	public Map<String, Object> getAllStock(@RequestParam("keyWord")String keyWord) {
		return stockService.getStocksByKeyword(keyWord);
	} 
	

	@RequestMapping("period_stocks")
	@ResponseBody
	public Map<String, Object> getPeriodStock(@RequestParam("keyWord")String keyWord,@RequestParam("startDate")Date startDate,@RequestParam("endDate")Date endDate) {
		return stockService.getStocksByKeyWordAndPeriod(keyWord,startDate,endDate);
	}
}
