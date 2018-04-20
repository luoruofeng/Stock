package org.lrf.stock.comparable;

import java.util.Comparator;

import org.lrf.stock.entity.Stock;

public class StockDateComparable implements Comparator<Stock> {
	@Override
	public int compare(Stock o1, Stock o2) {
		if (o1.getDate().after(o2.getDate()))
			return -1;
		else if(o1.getDate().equals(o2.getDate()))
			return 0;
		else if(o1.getDate().before(o2.getDate()))
			return 1;
		return 0;
	}
}
