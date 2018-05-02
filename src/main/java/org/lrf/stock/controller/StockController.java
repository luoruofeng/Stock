package org.lrf.stock.controller;

import java.util.Date;
import java.util.Map;

import org.lrf.stock.service.StockServiceImpl;
import org.lrf.stock.util.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
