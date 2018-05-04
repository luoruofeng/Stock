package org.lrf.stock.service;

import java.util.Date;
import java.util.Map;

public interface CodeResultService {
	public Map<String, Object> getCodeResultByKeyWordAndPeriod(String keyWord,Date startDate,Date endDate);
}
