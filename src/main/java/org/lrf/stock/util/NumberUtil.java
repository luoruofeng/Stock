package org.lrf.stock.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {

	public static boolean isNumericzidai(String str) {
		Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
}
