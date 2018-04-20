package org.lrf.stock.util;

import java.io.File;

public class FileUtil {
	public static File[] getChildrenFile(String dir, String suffix) {
		return new File(dir).listFiles((d, name) -> {
			return name.endsWith(suffix);
		});
	}

	public static void createDir(String dirPath) {
		File dir = new File(dirPath);
		System.out.println(dir.getAbsolutePath());
		if (dir.exists() && dir.isDirectory()) {
			FileUtil.deleteDir(dir);
		}
		System.out.println(dir.mkdirs() ? "create excel Directory success" : "create excel Directory failed");
	}

	// 递归删除文件夹
	public static void deleteDir(File dir) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteDir(files[i]);
			}
		}
		dir.delete();
	}
}
