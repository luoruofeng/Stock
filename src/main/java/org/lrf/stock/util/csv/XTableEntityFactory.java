package org.lrf.stock.util.csv;

import java.util.ArrayList;
import java.util.List;

import org.lrf.stock.csv_mapper.CsvBasicMapper;

import cn.hutool.core.text.csv.CsvData;

public class XTableEntityFactory<T> implements EntityFactory<T>{
	
	@Override
	public List<T> createListEntity(CsvData csvData,CsvBasicMapper<T> mapper,int startX,int startY,T t) {
		List<T> result = new ArrayList<>();
		for (int x = startX; x < csvData.getRowCount(); x++) {
			List<Object> args = new ArrayList<>();
			for (int y = startY; y < csvData.getRow(0).getFieldCount(); y++) {
				args.add(mapper.getValue(x, y));
			}
			result.add(mapper.fromCsvDataToEntityFactory(args.toArray(), t));				
		}
		return result;
	}

}
