package org.lrf.stock.repository;

import java.util.List;

import org.lrf.stock.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class StockRepository{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<Stock> findAll(){
		return mongoTemplate.findAll(Stock.class);
	}
	
	public void saveStocks(List<Stock> stocks) {
		mongoTemplate.insertAll(stocks);
	}
	
	public void saveOrUpdateStock(Stock stock) {
		mongoTemplate.save(stock);
	}
	
	public void dropCollection() {
		mongoTemplate.dropCollection(Stock.class);
	}
	
	public List<Stock> getStocksEntityByStock(String code) {
		return mongoTemplate.find(Query.query(Criteria.where("code").is(code)), Stock.class);
	}
	
	public void createIndex(String field) {
		mongoTemplate.getCollection("stock").createIndex(field);
	}
	
}
