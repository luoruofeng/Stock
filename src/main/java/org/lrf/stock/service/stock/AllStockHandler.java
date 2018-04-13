package org.lrf.stock.service.stock;

import java.util.ArrayList;
import java.util.List;

import org.lrf.stock.entity.Stock;
import org.lrf.stock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AllStockHandler {
	private AllStockHandler() {}
	
	private static AllStockHandler handler = new AllStockHandler();
	
	private static List<Stock> stocks = new ArrayList<>();
	
	public static AllStockHandler instance() {
		return handler;
	}
	
	public static List<Stock> getAllStock(StockRepository stockRepository){
		if (stocks.isEmpty()) {
			synchronized(stocks) {
				if (stocks.isEmpty()) {
					stocks = stockRepository.findAll();
				}
			}
		}
		return stocks;
	}
	
	public void cleanStock() {
		stocks = null;
	}
}
