package org.lrf.stock.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamOperationRunnable implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(StreamOperationRunnable.class);
	
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
		} catch (Exception e) {
			logger.debug("Have  Exception" +e);
		}
	}

	static int count = 0;

	private void writeInputStreamToDisk()   {
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
			logger.info("Download csv :"+file.getPath());
		} catch (IOException e) {
			logger.debug("Read Or Write Exception"+e);
		} finally {
			try {
				br.close();
				bw.close();
			} catch (IOException e) {
				logger.debug("BufferedReader or bufferedWriter close exception"+e);
			}
		}
	}
}
