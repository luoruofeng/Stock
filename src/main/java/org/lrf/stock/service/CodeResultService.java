package org.lrf.stock.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.lrf.stock.entity.CodeResult;

public interface CodeResultService {
	public Map<String, Object> getCodeResultByKeyWordAndPeriod(String keyWord,Date startDate,Date endDate);
}
