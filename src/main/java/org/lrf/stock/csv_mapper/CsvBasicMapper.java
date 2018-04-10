package org.lrf.stock.csv_mapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lrf.stock.util.csv.EntityFactory;
import org.lrf.stock.util.csv.XTableEntityFactory;
import org.lrf.stock.util.csv.YTableEntityFactory;

import cn.hutool.core.text.csv.CsvData;

public class CsvBasicMapper<T> {

	
	
	private CsvData csvData;

	public CsvBasicMapper() {}
	
	public CsvBasicMapper(CsvData csvData) {
		this.csvData = csvData;
	}
	
	public T fromCsvDataToEntityFactory(Object[] args ,T t) {
		
		List<Class> argsClassList = new ArrayList<>();
		
		
		for (int i = 0; i < args.length; i++) {
			argsClassList.add(String.class);
		}

		Class[] argsClass = argsClassList.toArray(new Class[args.length]);
		
		
		try {
			T result = (T) t.getClass().getConstructor(argsClass).newInstance(args);
			System.out.println("create stock ----- "+result.toString());
			return result;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getValue(int x, int y) {
		return csvData.getRow(x).get(y);
	}
}
