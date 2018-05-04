package org.lrf.stock;

import java.util.Date;

import org.lrf.stock.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfig {

	private static Logger logger = LoggerFactory.getLogger(SchedulingConfig.class);
	
	
	@Autowired
	private StockService stockService;
	
	/**
	 * 定时获取新stocks  每天3点59和7点59执行
	 */
	@Scheduled(cron = "0 59 15,19 * * ?")
	public void downloadAndSaveNewStocks() {	
		
		logger.info("scheduled of download new csv and save new stock to DB,execute time : "+new Date());
		
		stockService.downloadRestExcelFilesAndSaveExcel();
		stockService.saveTempCSVToDB();
	}
}
