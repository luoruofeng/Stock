package org.lrf.stock.controller;

import org.lrf.stock.service.StockServiceImpl;
import org.lrf.stock.util.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StockController {
	
	@Autowired
	private StockServiceImpl stockService;
	
	@RequestMapping("stocks")
	public ModelAndView getAllStock(String code) {
		return new ModelAndView("stocks", Keys.STOCKS, stockService.getStockByCode(code));
	} 
}
