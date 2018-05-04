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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.text.csv.CsvData;

public class CsvBasicMapper<T> {

	private Logger logger = LoggerFactory.getLogger(CsvBasicMapper.class);
	
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
			//System.out.println("create stock ----- "+result.toString());
			return result;
		} catch (InstantiationException e) {
			logger.debug("Instantiation(实例化) T Exception "+e);
		} catch (IllegalAccessException e) {
			logger.debug("Illegal Access（安全权限）  Exception "+e);
		} catch (IllegalArgumentException e) {
			logger.debug("IllegalArgument（非法参数）  Exception "+e);
		} catch (InvocationTargetException e) {
			logger.debug("InvocationTarget（反射 ）  Exception "+e);
		} catch (NoSuchMethodException e) {
			logger.debug("NoSuchMethod  Exception "+e);
		} catch (SecurityException e) {
			logger.debug("Security（安全）  Exception "+e);
		}
		return null;
	}

	public String getValue(int x, int y) {
		return csvData.getRow(x).get(y);
	}
}
