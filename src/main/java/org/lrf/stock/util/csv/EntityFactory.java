package org.lrf.stock.util.csv;

import java.util.List;

import org.lrf.stock.csv_mapper.CsvBasicMapper;

import cn.hutool.core.text.csv.CsvData;

public interface EntityFactory<T> {
	public List<T> createListEntity(CsvData csvData,CsvBasicMapper<T> mapper,int startX,int startY,T t); 
}
