package org.lrf.stock.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.lrf.stock.csv_mapper.CsvBasicMapper;
import org.lrf.stock.csv_mapper.StockMapper;
import org.lrf.stock.entity.Code;
import org.lrf.stock.entity.Stock;
import org.lrf.stock.repository.CodeRepository;
import org.lrf.stock.repository.StockRepository;
import org.lrf.stock.service.countdownlatch_proxy.CountDownLatchUser;
import org.lrf.stock.service.countdownlatch_proxy.CountDownLockProxy;
import org.lrf.stock.util.CsvUtil;
import org.lrf.stock.util.FileUtil;
import org.lrf.stock.util.csv.XTableEntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.util.CharsetUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

@Component("stockService")
public class StockServiceImpl implements PageProcessor, StockService, CountDownLatchUser {

	private Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

	@Autowired
	private CountDownLockProxy countDownLockProxy;

	@Value("${stock.excel.dir}")
	private String excelDir;

	@Value("${stock.excel.http.maxTotalConnections}")
	private int maxTotalConnections;

	@Value("${stock.excel.http.maxHostConnections}")
	private int maxHostConnections;

	@Value("${stock.excel.size}")
	private int excelSize;

	@Value("${stock.excel.suffix}")
	private String excelSuffix;

	private ExecutorService writeExcelFixedThreadPool;

	private final static String DATA_FORMAT = "YYYYMMdd";

	@Value("${stock.excel.pool.size}")
	private int excelPoolSize;

	@Autowired
	private CodeRepository codeRepository;

	@Autowired
	private StockRepository stockRepository;

	private final static int THREAD_TOTLE = 299;

	private final static int REQUEST_TIME_OUT = 100000;

	private final static int RETYR_TIMES = 10;

	private Site site = Site.me().setTimeOut(REQUEST_TIME_OUT).setRetryTimes(RETYR_TIMES);

	@Override
	public void process(Page page) {
		String startTime = page.getHtml().xpath("//input[@name=\"date_start_type\"]/@value").toString();
		String code = page.getHtml().xpath("//td[@class=\"col_1\"]/h1/span/a/text()").toString();

		codeRepository.update(code, StringToDate(startTime));
	}

	private static Date StringToDate(String date) {
		if (date.length() < 10)
			return null;
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDate ld = LocalDate.of(new Integer(date.substring(0, 4)), new Integer(date.substring(5, 7)),
				new Integer(date.substring(8, 10)));
		if (ld == null)
			return null;
		ZonedDateTime zdt = ld.atStartOfDay(zoneId);
		return Date.from(zdt.toInstant());
	}

	@Override
	public Site getSite() {
		return site;
	}

	public void setStockStartTimeUrl(Spider spider, Code code) {
		if (code != null && code.getStartDate() != null)
			return;
		// code2或者5或者1开头的是基金
		if (code != null && (code.getCode().substring(0, 1).equals("2") || code.getCode().substring(0, 1).equals("5")
				|| code.getCode().substring(0, 1).equals("1")))
			return;
		spider.addUrl("http://quotes.money.163.com/trade/lsjysj_" + code.getCode() + ".html");
	}

	public void setAllStockStartTime() {
		Spider spider = Spider.create(this).thread(THREAD_TOTLE);
		codeRepository.findAll().forEach(code -> {
			setStockStartTimeUrl(spider, code);
		});
		spider.run();
	}

	public List<Stock> getStockByCode(String code) {
		return stockRepository.getStocksEntityByStock(code);
	}

	private void createExcelDir() {
		File dir = new File(excelDir);
		System.out.println(dir.getAbsolutePath());
		if (dir.exists() && dir.isDirectory()) {
			deleteDir(dir);
		}
		System.out.println(dir.mkdirs() ? "create excel Directory success" : "create excel Directory failed");
	}

	private HttpRequestBase createHttpUriRequestWithOutUrl() {
		RequestConfig config = RequestConfig.custom().build();
		HttpGet httpGet = new HttpGet();
		httpGet.setConfig(config);

		return httpGet;
	}

	private String createExcelUrl(Code code) {
		return "http://quotes.money.163.com/service/chddata.html?code=" + firstChar(code.getCode()) + code.getCode()
				+ "&start=" + getTimeStr(code.getStartDate())
				+ "&fields=TCLOSE;HIGH;LOW;TOPEN;LCLOSE;CHG;PCHG;TURNOVER;VOTURNOVER;VATURNOVER;TCAP;MCAP";
	}

	private HttpRequestBase setUrlToHttpRequestBase(HttpRequestBase request, Code code) {
		try {
			request.setURI(new URI(createExcelUrl(code)));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return request;
	}

	public void downloadExcelFilesAndSaveExcel() {
		System.out.println("_________________downloadExcelFilesAndSaveExcel___________________");
		Date startDate = new Date();
		createExcelDir();

		CloseableHttpClient client = HttpClients.createDefault();
		HttpRequestBase request = createHttpUriRequestWithOutUrl();

		codeRepository.findAll().forEach(code -> {
			if (code == null || code.getStartDate() == null)
				return;
			saveExcel(code.getCode(), downloadFile(client, setUrlToHttpRequestBase(request, code)));
		});
		try {
			Thread.sleep(2 * 1000);
			request.releaseConnection();
			client.close();
			System.out.println((startDate.getTime() - new Date().getTime()) / 1000 / 60);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getTimeStr(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT);
		return sdf.format(date);
	}

	// 递归删除文件夹
	public void deleteDir(File dir) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteDir(files[i]);
			}
		}
		dir.delete();
	}

	private String firstChar(String code) {
		String firstCode = code.substring(0, 1);

		Integer result = 0;
		if (firstCode.equals("0")) {
			result = 1;
		} else if (firstCode.equals("3")) {
			result = 1;
		} else if (firstCode.equals("6")) {
			result = 0;
		}
		return result.toString();
	}

	private InputStream downloadFile(HttpClient client, HttpUriRequest httpUriRequest) {
		try {
			HttpResponse httpResponse = client.execute(httpUriRequest);
			return httpResponse.getEntity().getContent();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void saveExcel(String code, InputStream inputStream) {
		if (writeExcelFixedThreadPool == null)
			writeExcelFixedThreadPool = Executors.newFixedThreadPool(this.excelPoolSize);

		writeExcelFixedThreadPool.execute(new StreamOperationRunnable(code, excelDir, excelSuffix, inputStream));
	}

	public void saveStockCSVToDB() {
		
		stockRepository.dropCollection();
		transformCSVToEntity();
	}

	private final static String CSV_SUFFIX = ".csv";
	private final static int READ_CSV_NUMBER_THREAD = 99;
	private File[] csvFiles;

	private void transformCSVToEntity() {

		csvFiles = FileUtil.getChildrenFile(excelDir, CSV_SUFFIX);

		if (csvFiles == null || csvFiles.length < 1) {
			System.out.println("have no any csv file!!!");
			return;
		}

		try {
			countDownLockProxy.procced(csvFiles.length, this);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void saveAllStocksFromCsv(CountDownLatch countDownLatch) {
		if(csvFiles == null) {
			throw new RuntimeException("csvFiles is null !!!!!!!!!!!!!! \n"+this);
		}
		
		ExecutorService es = Executors.newFixedThreadPool(READ_CSV_NUMBER_THREAD);

		for (int i = 0; i < csvFiles.length; i++) {
			final int temp = i;
			es.execute(() -> {
				try {
					stockRepository.saveStocks(
							CsvUtil.readCSV(new XTableEntityFactory<>(), csvFiles[temp], 1, 0, new Stock()));
				} catch (Exception e) {
					System.out.println(e);
				}

				if (countDownLatch != null)
					countDownLatch.countDown();
			});
		}
		
		es.shutdown();
	}

	public void createStockIndex() {
		System.out.println("-----------------------create code index in stock collection-----------------------");
		stockRepository.createIndex("code");
	}

	@Override
	public void useCountDownLatch(CountDownLatch countDownLatch) {
		saveAllStocksFromCsv(countDownLatch);
	}

}
