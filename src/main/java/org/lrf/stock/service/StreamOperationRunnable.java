package org.lrf.stock.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import cn.hutool.core.io.file.FileWrapper;

public class StreamOperationRunnable implements Runnable {

	public StreamOperationRunnable(String code, String excelDir, String excelSuffix, InputStream inputStream) {
		this.code = code;
		this.excelDir = excelDir;
		this.excelSuffix = excelSuffix;
		this.inputStream = inputStream;
	}

	private InputStream inputStream;
	private String code;

	private String excelDir;
	private String excelSuffix;
	public static boolean STATUS = false;

	@Override
	public void run() {
		try {
			writeInputStreamToDisk();
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static int count = 0;

	private void writeInputStreamToDisk() throws IOException {
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			File file = new File(excelDir + code + "." + excelSuffix);

			br = new BufferedReader(new InputStreamReader(inputStream, "GB18030"));
			bw = new BufferedWriter(new FileWriter(file));

			String bufferString = null;
			while ((bufferString = br.readLine()) != null) {
				bw.write(bufferString);
				bw.newLine();
				bw.flush();
			}
			System.out.println("download excel :" + code + ".csv");
		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			br.close();
			bw.close();
		}
	}
}
