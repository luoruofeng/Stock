package org.lrf.stock.service.stock;

import java.util.ArrayList;
import java.util.List;

import org.lrf.stock.entity.Stock;
import org.lrf.stock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AllStockHandler {
	private AllStockHandler() {}
	
	private static AllStockHandler handler = new AllStockHandler();
	
	private List<Stock> stocks = new ArrayList<>();

	@Autowired
	private StockRepository stockRepository;
	
	public static AllStockHandler instance() {
		return handler;
	}
	
	public List<Stock> getAllStock(){
		if (stocks == null) {
			synchronized(this) {
				if (stocks == null) {
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
