package org.lrf.stock.util;

import java.util.ArrayList;
import java.util.List;

public enum Day {

	FIVE(5),TEN(10),TWENTY(20),THIRTY(30),SIXTY(60),NINETY(90),HALF_YEAR(180),YEAR(360);
	
	public Integer value;
	
	private Day(Integer value) {
		this.value = value;
	}
	
	public static List<Integer> getDays(){
		List<Integer> result = new ArrayList<>();
		for (Day day : Day.values()) {
			result.add(day.value);
		}
		return result;
	}
	
	
}
