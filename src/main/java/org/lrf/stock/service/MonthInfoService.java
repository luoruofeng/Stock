package org.lrf.stock.service;

import java.util.ArrayList;
import java.util.List;

import org.lrf.stock.entity.Code;
import org.lrf.stock.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class MonthInfoService implements PageProcessor{
	
	private final static int TIME_OUT = 1000 * 10;
	private final static int RETRY_TIMES = 10;
	
	@Autowired
	private CodeRepository codeRepository;
	
	
	public void createAllMonthInfo() {

		List<Code> codesResult = new ArrayList<>();
		
		codeRepository.findAll().forEach(c->{
			createMonthInfoForCode(c);
		});

	}
	
	private void setMonthInfo(Code code) {
		//code.setMonthInfo(createMonthInfo(code));
	}
	
	private void createMonthInfoForCode(Code code) {
		Spider.create(this).addUrl("http://quotes.money.163.com/f10/cwbbzy_"+code.getCode()+".html").thread(1).run();
	}

	@Override
	public void process(Page page) {
		page.getHtml().xpath("//");
	}

	@Override
	public Site getSite() {
		return Site.me().setTimeOut(TIME_OUT).setRetryTimes(RETRY_TIMES);
	}
}
