package org.lrf.stock.service.pageprocessor;

import java.util.ArrayList;
import java.util.List;

import org.lrf.stock.entity.Stock;
import org.lrf.stock.repository.CodeRepository;
import org.lrf.stock.repository.StockRepository;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 * 废弃 爬取历史记录
 * @author luoruofeng
 *
 */
@Deprecated
public class NewStockPageProcessor implements PageProcessor{

	private final static int REQUEST_TIME_OUT = 20000;

	private final static int RETYR_TIMES = 1;
	
	private StockRepository stockRepository;
	private CodeRepository codeRepository;
	
	public NewStockPageProcessor(String code,StockRepository stockRepository) {
		this.code = code;
		this.stockRepository = stockRepository;
	}
	
	private String code;
	
	@Override
	public void process(Page page) {
		Selectable selectableTR = page.getHtml().xpath("//div[@class='inner_box']/table/tbody/");

		selectableTR.nodes().forEach((trSelectable)->{
			
			
			if(trSelectable == null || stockRepository.getStocksEntityByStock(code).isEmpty())
				return;
			
			List<String> args = new ArrayList<>();
			trSelectable.xpath("//td/text()").all().forEach((tdText)->{
				args.add(tdText);
			});
			
			
			new Stock();
		});
		
	}

	@Override
	public Site getSite() {
		return Site.me().setTimeOut(REQUEST_TIME_OUT).setRetryTimes(RETYR_TIMES);
	}
}
