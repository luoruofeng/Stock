package org.lrf.stock.service;

import java.util.Arrays;
import java.util.List;

import org.lrf.stock.entity.Code;
import org.lrf.stock.repository.CodeRepository;
import org.lrf.stock.service.pageprocessor.EastMoneyPageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.codecraft.webmagic.Spider;

@Component
public class CodeService{

	@Autowired
	private CodeRepository codeRepository;
	
    public void writeCodeToFile() {
    	System.out.println("-------开始下载-------");
    		List<Code> codes =codeRepository.findAll();

    		if(codes == null || codes.size() == 0) {
    			EastMoneyPageProcessor empp = new EastMoneyPageProcessor(codeRepository);
    			Spider.create(empp).addUrl("http://quote.eastmoney.com/stocklist.html").setSpiderListeners(Arrays.asList(empp)).thread(1).run();
    		}
    }
}
