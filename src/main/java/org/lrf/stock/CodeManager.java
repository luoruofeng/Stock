package org.lrf.stock;

import org.lrf.stock.service.CodeResultServiceImpl;
import org.lrf.stock.service.CodeService;
import org.lrf.stock.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CodeManager implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(CodeManager.class);

	@Autowired
	private CodeService codeService;

	@Autowired
	private StockService stockService;

	@Autowired
	private CodeResultServiceImpl codeResultService;

	@Override
	public void run(String... args) throws Exception {

		System.out.println(args.length+"****************************");
		
		if (args != null && args[0].equals("init")) {

			// 把所有的股票存起来
			logger.info("Command Line Runner: writeCodeToFile");
			codeService.writeCodeToFile();

			// 给刚才所有以保存的code设置上市日期
			logger.info("Command Line Runner: setAllStockStartTime");
			stockService.setAllStockStartTime();

			// 下载刚才excel历史记录
			logger.info("Command Line Runner: downloadAllExcelFilesAndSaveExcel");
			stockService.downloadAllExcelFilesAndSaveExcel();

			// 删除DB中的所有stock
			logger.info("Command Line Runner: dropAllStock");
			stockService.dropAllStock();

			// 把Excel转对象，进行保存
			logger.info("Command Line Runner: saveAllCSVToDB");
			stockService.saveAllCSVToDB();

			// 对stock对象里面的code建立索引
			logger.info("Command Line Runner: createStockIndex");
			stockService.createStockIndex();

			// 计算codeResult
			logger.info("Command Line Runner: saveAllCodeResult");
			codeResultService.saveAllCodeResult();

		}
	}

}