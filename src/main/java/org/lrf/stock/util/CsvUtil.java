package org.lrf.stock.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lrf.stock.csv_mapper.CsvBasicMapper;
import org.lrf.stock.util.csv.EntityFactory;
import org.lrf.stock.util.csv.XTableEntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.util.CharsetUtil;

public class CsvUtil {
	private Logger logger = LoggerFactory.getLogger(CsvUtil.class);
	
	private String filename = null;
	private BufferedReader br= null;
	private List<String> list = new ArrayList<>();
	
	public CsvUtil() {}
	
	public CsvUtil(String filename) throws Exception{
		this.filename = filename;
		br = new BufferedReader(new FileReader(filename));
		String step;
		while((step = br.readLine()) != null) {
			list.add(step);
		}
	}
	
	public List<String> getList(){
		return list;
	}
	
	public int getRowNum() {
		return list.size();
	}
	
	public int getColNum() {
		if(!list.toString().equals("[]")) {
			if(list.get(0).toString().contains(",")) {
				return list.get(0).toString().split(",").length;
			}else if(list.get(0).toString().trim().length()!=0) {
				return 1;
			}else {
				return 0;
			}
		}
		else {
			return 0;
		}
	}
	
	public String getRow(int index) {
		if(this.list.size()!=0) {
			return (String)list.get(index);
		}else {
			return null;
		}
	}
	public String getCol(int index) {
		if(this.getColNum() ==0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		String tmp = null;
		int colnum = this.getColNum();
		if(colnum > 1) {
			for(Iterator<String> it = list.iterator();it.hasNext();) {
				tmp = it.next().toString();
				sb = sb.append(tmp.split(",")[index]+",");		
				}			
		}else {
			for(Iterator<String> it = list.iterator();it.hasNext();) {
				tmp = it.next().toString();
				sb = sb.append(tmp+",");		
				}
		}
		
		String str = new String(sb.toString());
		str = str.substring(0,str.length()-1);
		return str;
	}
	
	public String getString(int row,int col) {
		String temp = null;
		int colnum = this.getColNum();
		if(colnum > 1) {
			temp = list.get(row).toString().split(",")[col];
			
		}else if(colnum ==1) {
			temp = list.get(row).toString();
		}else {
			temp = null;
		}
		return temp;
	}
	
	public void CsvClose() throws Exception{
		this.br.close();
	}
	
	public String removeHead(String str) {
		String[] str1 = str.split(",");
		String sb = new String();
		for(int i = 1;i<str1.length;i++) {
			sb = sb+str1[i]+",";
		}
		return sb;
	}
	
	public JSONArray readCsv(String path) {
		JSONArray array  = new JSONArray();
		CsvUtil util;
		try {
			util = new CsvUtil(path);
			int row = util.getRowNum();
			int col = util.getColNum();
			for(int i =0; i<col;i++) {
				JSONObject jsonObject = new JSONObject();
				String value = util.getCol(i);
				jsonObject.put(util.getString(0, i), util.removeHead(value));
				array.put(jsonObject);
			}
		} catch (Exception e) {
			logger.debug("Create CsvUtil  Exception"+e);
		}
		return array;		
	}
	
	
	/**-----------------------author luoruofeng-------------------------*/
	
	private static CsvData createCsvData(File csvFile) {
		CsvReader reader = new CsvReader();
		return reader.read(csvFile, CharsetUtil.CHARSET_UTF_8);
	}
	
	public static <T> List<T> readCSV(EntityFactory<T> entityFactory,File csvFile,int startX,int startY,T t) {
		CsvData csvData = CsvUtil.createCsvData(csvFile);
		return entityFactory.createListEntity(csvData, new CsvBasicMapper<>(csvData), startX, startY,t);
	}

}
