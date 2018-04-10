package org.lrf.stock;

import org.lrf.stock.service.CodeResultService;
import org.lrf.stock.service.CodeService;
import org.lrf.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class CodeManager implements CommandLineRunner {

	@Autowired
	private CodeService codeService;
	
	@Autowired
	private StockService stockService;
	
	@Autowired
	private CodeResultService codeResultService;
	
	@Override
	public void run(String... args) throws Exception {
		//把所有的股票存起来
		//codeService.writeCodeToFile();
		
		//给刚才所有以保存的code设置上市日期
		//stockService.setAllStockStartTime();
		
		//下载刚才excel历史记录
		//stockService.downloadExcelFilesAndSaveExcel();
		
		//把Excel转对象，进行保存
		stockService.saveStockCSVToDB();
		
		//对stock对象里面的code建立索引
		stockService.createStockIndex();
		
		//计算codeResult
		//codeResultService.setAllCodeResult();
	}
}