package org.lrf.stock.service.pageprocessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lrf.stock.entity.Code;
import org.lrf.stock.repository.CodeRepository;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class EastMoneyPageProcessor implements PageProcessor, SpiderListener {

	private final static int REQUEST_TIME_OUT = 20000;

	private final static int RETYR_TIMES = 1;

	public EastMoneyPageProcessor(CodeRepository codeRepository) {
		this.codeRepository = codeRepository;
	}

	private CodeRepository codeRepository;

	@Override
	public void process(Page page) {
		codeRepository.dropCollection();
		Selectable selectable = page.getHtml().xpath("//div[@id='quotesearch']/ul/li/a[@href]/text()");
		List<String> allCodeData = selectable.all();
		createAndSaveAllCode(allCode -> {
			allCodeData.forEach(item -> {
				Code code = new Code(item.substring(0, item.indexOf("(")),
						item.substring(item.indexOf("(") + 1, item.length() - 1));
				// code2或者5或者1开头的是基金
				if (code != null && (code.getCode().substring(0, 1).equals("2")
						|| code.getCode().substring(0, 1).equals("5") || code.getCode().substring(0, 1).equals("1")))
					return;
				allCode.add(code);
			});
		});

		/**
		 * List<Code> allCode = new ArrayList<>(); allCodeData.forEach(item -> { Code
		 * code = new Code(item.substring(0, item.indexOf("(")),
		 * item.substring(item.indexOf("(") + 1, item.length() - 1)); //
		 * code2或者5或者1开头的是基金 if (code != null && (code.getCode().substring(0,
		 * 1).equals("2") || code.getCode().substring(0, 1).equals("5") ||
		 * code.getCode().substring(0, 1).equals("1"))) return; allCode.add(code); });
		 * codeRepository.saveCodes(allCode);
		 */
	}

	@FunctionalInterface
	public interface CodeOperation {
		void createCode(List<Code> allCode);
	}

	private void createAndSaveAllCode(CodeOperation codeOperation) {
		List<Code> allCode = new ArrayList<>();
		codeOperation.createCode(allCode);
		codeRepository.saveCodes(allCode);
	}

	@Override
	public Site getSite() {
		return Site.me().setTimeOut(REQUEST_TIME_OUT).setRetryTimes(RETYR_TIMES);
	}

	@Override
	public void onSuccess(Request request) {
		checkSuccess();
	}

	@Override
	public void onError(Request request) {
		checkSuccess();
	}

	private void checkSuccess() {
		List<Code> codes = codeRepository.findAll();
		if (codes == null || codes.size() < 1) {
			System.out.println("can not get data from east money page!");
			retrySpareUrl();
		}
	}

	private String getStringFromInputStream(InputStream contentInput) {

		InputStreamReader isr = new InputStreamReader(contentInput);
		StringBuffer resultSB = new StringBuffer();
		char[] chars = new char[1024];

		try {
			while (isr.read(chars) > 0) {
				resultSB.append(new String(chars));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSB.toString();
	}

	private void retrySpareUrl() {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet();
		httpGet.setURI(URI.create(
				"http://quotes.money.163.com/hs/service/diyrank.php?host=http%3A%2F%2Fquotes.money.163.com%2Fhs%2Fservice%2Fdiyrank.php&page=0&query=STYPE%3AEQA&fields=NO%2CSYMBOL%2CNAME%2CPRICE%2CPERCENT%2CUPDOWN%2CFIVE_MINUTE%2COPEN%2CYESTCLOSE%2CHIGH%2CLOW%2CVOLUME%2CTURNOVER%2CHS%2CLB%2CWB%2CZF%2CPE%2CMCAP%2CTCAP%2CMFSUM%2CMFRATIO.MFRATIO2%2CMFRATIO.MFRATIO10%2CSNAME%2CCODE%2CANNOUNMT%2CUVSNEWS&sort=PERCENT&order=desc&count=4444&type=query"));
		createAndSaveAllCode(allCode -> {
			try {
				parseJsonStr(getStringFromInputStream(client.execute(httpGet).getEntity().getContent()), allCode);
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

	private void parseJsonStr(String jsonStr, List<Code> allCode) {
		try {
			JSONObject jsonObjRoot = new JSONObject(jsonStr);

			JSONArray jsonArray = jsonObjRoot.getJSONArray("list");
			if (jsonArray == null) {
				System.out.println("jsonArray is null,can not get any code");
				return;
			}

			jsonArray.forEach(jsonObject -> {
				if (jsonObject instanceof JSONObject) {

					String symbol = ((JSONObject) jsonObject).getString("SYMBOL");
					String name = ((JSONObject) jsonObject).getString("SNAME");

					System.out.println(symbol + " " + name);

					if (symbol != null && !symbol.isEmpty() && name != null && !name.isEmpty()) {
						allCode.add(new Code(name, symbol));
					}
				}
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
