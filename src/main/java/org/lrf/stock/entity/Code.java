package org.lrf.stock.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Code {
	
	public Code() {}
	
	public Code(String name, String code) {
		super();
		this.name = name;
		this.code = code;
	}
	
	public Code(String id, String name, String code) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
	}
	@Id
	private String id;
	private String name;
	private String code;
	private Date startDate;
	private Map<Integer, CodeResult> codeResults; 
	private MonthInfo monthInfo;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Map<Integer, CodeResult> getCodeResults() {
		return codeResults;
	}

	public void setCodeResults(Map<Integer, CodeResult> codeResults) {
		this.codeResults = codeResults;
	}
	
	public void cleanCodeResults() {
		this.setCodeResults(new HashMap<Integer, CodeResult>());
	}

	public MonthInfo getMonthInfo() {
		return monthInfo;
	}

	public void setMonthInfo(MonthInfo monthInfo) {
		this.monthInfo = monthInfo;
	}
	

}
