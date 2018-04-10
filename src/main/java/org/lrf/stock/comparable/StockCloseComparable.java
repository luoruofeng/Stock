package org.lrf.stock.comparable;

import java.util.Comparator;

import org.lrf.stock.entity.Stock;

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
