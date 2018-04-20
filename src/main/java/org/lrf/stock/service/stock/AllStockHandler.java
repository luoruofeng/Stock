package org.lrf.stock.service.stock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lrf.stock.entity.Stock;
import org.lrf.stock.repository.CodeRepository;
import org.lrf.stock.repository.StockRepository;

/**
 * 废弃
 * 内存放不下all stock
 * @author luoruofeng
 *
 */
@Deprecated
public class AllStockHandler {
	private AllStockHandler() {
	}

	private static AllStockHandler handler = new AllStockHandler();

	private static Map<String, List<Stock>> codeStockMap = new HashMap<>();

	public static AllStockHandler instance() {
		return handler;
	}

	public static List<Stock> getAllStockByCode(StockRepository stockRepository, String code,
			CodeRepository codeRepository) {
		return getAllStock(stockRepository, codeRepository).get(code);
	}

	static int i = 0;
	public static Map<String, List<Stock>> getAllStock(StockRepository stockRepository, CodeRepository codeRepository) {
		if (codeStockMap.isEmpty()) {
			synchronized (codeStockMap) {
				if (codeStockMap.isEmpty()) {

					codeRepository.findAll().forEach((code) -> {
						List<Stock> stocks = stockRepository.getStocksEntityByStock(code.getCode());
						codeStockMap.put(code.getCode(), stocks);

					});
					return codeStockMap;
					/**
					 * List<Stock> stocks = stockRepository.findAll(); return
					 * stocks.stream().collect( Collectors.toMap( Stock::getCode, (s)->{ List<Stock>
					 * itemStocks = new ArrayList<>(); itemStocks.add(s); return itemStocks; },
					 * (current,other)->{ current.addAll(other); return current; } ) );
					 */
				}
			}
		}
		return codeStockMap;
	}

	public static boolean isKeyExists(StockRepository stockRepository, String code, CodeRepository codeRepository) {
		return AllStockHandler.getAllStock(stockRepository, codeRepository).keySet().contains(code);
	}

	public static Set<String> getAllCodes(StockRepository stockRepository, CodeRepository codeRepository) {
		return AllStockHandler.getAllStock(stockRepository, codeRepository).keySet();
	}

	public void cleanStock() {
		codeStockMap = new HashMap<>();
	}
}
