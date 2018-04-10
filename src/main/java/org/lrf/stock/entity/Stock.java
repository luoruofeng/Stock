package org.lrf.stock.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Comparator;

import org.springframework.data.annotation.Id;

public class Stock {

	private static final String NONE = "None";

	@Id
	private String id;

	// 日期
	private Date date;
	// 股票代码
	private String code;
	// 名称
	private String name;
	// 收盘价
	private Double close;
	// 最高价
	private Double high;
	// 最低价
	private Double low;
	// 开盘价
	private Double open;
	// 前收盘
	private Double previousClose;
	// 涨跌额
	private Double changeAmount;
	// 涨跌幅
	private Double changeRate;
	// 换手率
	private Double turnoverRate;
	// 成交量
	private Long vol;
	// 成交金额
	private Double GMV;
	// 总市值
	private Long totalValue;
	// 流通市值
	private Long circulationMarketValue;

	public Stock() {
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Double getLow() {
		return low;
	}

	public void setLow(Double low) {
		this.low = low;
	}

	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	public Double getPreviousClose() {
		return previousClose;
	}

	public void setPreviousClose(Double previousClose) {
		this.previousClose = previousClose;
	}

	public Double getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(Double changeAmount) {
		this.changeAmount = changeAmount;
	}

	public Double getChangeRate() {
		return changeRate;
	}

	public void setChangeRate(Double changeRate) {
		this.changeRate = changeRate;
	}

	public Double getTurnoverRate() {
		return turnoverRate;
	}

	public void setTurnoverRate(Double turnoverRate) {
		this.turnoverRate = turnoverRate;
	}

	public Long getVol() {
		return vol;
	}

	public void setVol(Long vol) {
		this.vol = vol;
	}

	public Double getGMV() {
		return GMV;
	}

	public void setGMV(Double gMV) {
		GMV = gMV;
	}

	public Long getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Long totalValue) {
		this.totalValue = totalValue;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getCirculationMarketValue() {
		return circulationMarketValue;
	}

	public void setCirculationMarketValue(Long circulationMarketValue) {
		this.circulationMarketValue = circulationMarketValue;
	}

	public Stock(String date, String code, String name, String close, String high, String low, String open,
			String previousClose, String changeAmount, String changeRate, String turnoverRate, String vol, String gMV,
			String totalValue, String circulationMarketValue) {
		super();
		setDate(date);
		this.code = (code != null) ? code.substring(1) : "";
		this.name = name;
		this.close = new Double(checkNone(close));
		this.high = new Double(checkNone(high));
		this.low = new Double(checkNone(low));
		this.open = new Double(checkNone(open));
		this.previousClose = new Double(checkNone(previousClose));
		this.changeAmount = new Double(checkNone(changeAmount));
		this.changeRate = new Double(checkNone(changeRate));
		this.turnoverRate = new Double(checkNone(turnoverRate));
		this.vol = new Long(checkNone(vol));
		this.GMV = new Double(checkNone(gMV));
		this.totalValue = stringToLong(checkNone(totalValue));
		this.circulationMarketValue = stringToLong(checkNone(circulationMarketValue));
	}

	private void setDate(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			setDate(sdf.parse(dateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private String checkNone(String arg) {
		if (arg.equals(NONE))
			return "0";
		else
			return arg;
	}

	@Override
	public String toString() {
		return "Stock [DATE=" + date + ", STOCKSYMBOL=" + code + ", STOCKNAME=" + name + ", TCLOSE=" + close + ", HIGH="
				+ high + ", LOW=" + low + ", TOPEN=" + open + ", LCLOSE=" + previousClose + ", CHG=" + changeAmount
				+ ", PCHG=" + changeRate + ", TURNOVER=" + turnoverRate + ", VOTURNOVER=" + vol + ", VATURNOVER=" + GMV
				+ ", TCAP=" + totalValue + ", MCAP=" + circulationMarketValue + ", toString()=" + super.toString()
				+ "]";
	}

	private long stringToLong(String str) {
		double d = new Double(str);
		return (long) d;
	}

	public class StockCloseComparable implements Comparator<Stock> {
		@Override
		public int compare(Stock o1, Stock o2) {
			if (o1.getClose() > o2.getClose())
				return 1;
			else if(o1.getClose() == o2.getClose())
				return 0;
			else if(o1.getClose() < o2.getClose())
				return -1;
			return 0;
		}
	}
	
}
