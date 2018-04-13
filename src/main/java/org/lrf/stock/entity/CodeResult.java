package org.lrf.stock.entity;

import java.util.List;

import org.lrf.stock.util.DateUtil;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CodeResult {
	
	public CodeResult() {}
	
	private String code;
	private int numberOfDays;
	private Double avg;
	private Double max;
	private Double min;
	private List<Stock> aboveAvgMaxs;
	private List<Stock> belowAvgMins;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getNumberOfDays() {
		return numberOfDays;
	}
	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	public Double getAvg() {
		return avg;
	}
	public void setAvg(Double avg) {
		this.avg = avg;
	}
	public Double getMax() {
		return max;
	}
	public void setMax(Double max) {
		this.max = max;
	}
	public Double getMin() {
		return min;
	}
	public void setMin(Double min) {
		this.min = min;
	}

	public List<Stock> getAboveAvgMaxs() {
		return aboveAvgMaxs;
	}
	public void setAboveAvgMaxs(List<Stock> aboveAvgMaxs) {
		this.aboveAvgMaxs = aboveAvgMaxs;
	}
	public List<Stock> getBelowAvgMins() {
		return belowAvgMins;
	}
	public void setBelowAvgMins(List<Stock> belowAvgMins) {
		this.belowAvgMins = belowAvgMins;
	}
	public CodeResult(String code, int numberOfDays) {
		super();
		this.code = code;
		this.numberOfDays = numberOfDays;
	}
	
	public CodeResult(String code, int numberOfDays, Double avg, Double max, Double min, List<Stock> aboveAvgMaxs,
			List<Stock> belowAvgMins) {
		super();
		this.code = code;
		this.numberOfDays = numberOfDays;
		this.avg = avg;
		this.max = max;
		this.min = min;
		this.aboveAvgMaxs = aboveAvgMaxs;
		this.belowAvgMins = belowAvgMins;
	}
	

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		StringBuffer aboveAvgMaxsStr = new StringBuffer(((Integer)aboveAvgMaxs.size()).toString()).append(" : ");
		aboveAvgMaxs.forEach(s->{
			aboveAvgMaxsStr.append(DateUtil.getStr(s.getDate())).append(" ").append(s.getClose()).append(" ");
		});

		StringBuffer belowAvgMinsStr = new StringBuffer(((Integer)belowAvgMins.size()).toString()).append(" : ");
		belowAvgMins.forEach(s->{
			belowAvgMinsStr.append(DateUtil.getStr(s.getDate())).append(" ").append(s.getClose()).append(" ");
		});
		
		return sb.append("code:").append(code).append(" daysNumber:").append(numberOfDays).append(" avg:").append(avg).append(" max:").append(max).append(" min:").append(min).append(" aboveAvgMaxs").append(aboveAvgMaxs.size()).append(" belowAvgMins:").append(belowAvgMins.size()).append("\n").append(" aboveAvgMaxs:").append(aboveAvgMaxsStr.toString()).append("\n").append(" belowAvgMins:").append(belowAvgMinsStr.toString()).toString();
	}
}
