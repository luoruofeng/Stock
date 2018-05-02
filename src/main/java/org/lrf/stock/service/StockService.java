package org.lrf.stock.service;

import java.util.Date;
import java.util.Map;

public interface StockService {
	
	public Map<String, Object> getStocksByKeyWordAndPeriod(String keyWord,Date startDate,Date endDate);
	
	public void downloadRestExcelFilesAndSaveExcel();
	
	public void downloadAllExcelFilesAndSaveExcel();
	
	public void dropAllStock();
	
	public void setAllStockStartTime();
	
	public void saveAllCSVToDB();
	
	public void saveTempCSVToDB();
	
	public void createStockIndex();
	
	public void saveNoRecordStock();
	
	public Map<String, Object> getStocksByKeyword(String keyWord);
}
