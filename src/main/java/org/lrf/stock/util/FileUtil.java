package org.lrf.stock.util;

import java.io.File;

public class FileUtil {
	public static File[] getChildrenFile(String dir,String suffix) {
		return new File(dir).listFiles((d, name) -> {
			return name.endsWith(suffix);
		});
	}
}
