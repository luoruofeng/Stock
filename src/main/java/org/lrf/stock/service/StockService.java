package org.lrf.stock.service;

public interface StockService {
	
	public void downloadExcelFilesAndSaveExcel();
	
	public void setAllStockStartTime();
	
	public void saveStockCSVToDB();
	
	public void createStockIndex();
}
