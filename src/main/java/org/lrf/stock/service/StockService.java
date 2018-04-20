package org.lrf.stock.service;

public interface StockService {
	
	public void downloadRestExcelFilesAndSaveExcel();
	
	public void downloadAllExcelFilesAndSaveExcel();
	
	public void dropAllStock();
	
	public void setAllStockStartTime();
	
	public void saveAllCSVToDB();
	
	public void saveTempCSVToDB();
	
	public void createStockIndex();
	
	public void saveNoRecordStock();
}
