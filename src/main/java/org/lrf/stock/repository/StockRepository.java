package org.lrf.stock.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.lrf.stock.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Component
public class StockRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	public List<Stock> findAll() {
		// return mongoTemplate.findAll(Stock.class);
		DBCursor dbCursor = mongoTemplate.getCollection("stock").find();
		List<Stock> result = new ArrayList<>();
		while (dbCursor.hasNext()) {
			result.add(createStock(dbCursor.next()));
		}
		return result;

	}

	public Stock createStock(DBObject object) {
		Stock stock = new Stock();
		stock.setId(object.get("_id").toString());
		stock.setDate((Date)object.get("date"));
		stock.setCode(object.get("code").toString());
		stock.setName(object.get("name").toString());
		stock.setClose(Double.parseDouble(object.get("close").toString()));
		stock.setHigh(Double.parseDouble(object.get("high").toString()));
		stock.setLow(Double.parseDouble(object.get("low").toString()));
		stock.setOpen(Double.parseDouble(object.get("open").toString()));
		stock.setPreviousClose(Double.parseDouble(object.get("previousClose").toString()));
		stock.setChangeAmount(Double.parseDouble(object.get("changeAmount").toString()));
		stock.setChangeRate(Double.parseDouble(object.get("changeRate").toString()));
		stock.setTurnoverRate(Double.parseDouble(object.get("turnoverRate").toString()));
		stock.setVol(Long.parseLong(object.get("vol").toString()));
		stock.setGMV(Double.parseDouble(object.get("GMV").toString()));
		stock.setTotalValue(Long.parseLong(object.get("totalValue").toString()));
		stock.setCirculationMarketValue(Long.parseLong(object.get("circulationMarketValue").toString()));
		stock.setCaculateAmplitude();
		return stock;
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
		DBObject dbObject = new BasicDBObject();
		dbObject.put("code", code); 
		DBCursor dbCursor = (DBCursor) mongoTemplate.getCollection("stock").find(dbObject).maxTime(8, TimeUnit.HOURS);
		List<Stock> result = new ArrayList<>();
		while (dbCursor.hasNext()) {
			result.add(createStock(dbCursor.next()));
		}
		return result;
		//return mongoTemplate.find(Query.query(Criteria.where("code").is(code)), Stock.class);
	}

	public void createIndex(String field) {
		mongoTemplate.getCollection("stock").createIndex(field);
	}

	public Stock getLastStock(String code) {
	return mongoTemplate.findOne(Query.query(Criteria.where("code").is(code))
				.with(new Sort(new Sort.Order(Sort.Direction.DESC, "date"))).limit(1), Stock.class);
	}
	
	public Stock getFirstStock(String code) {
		return mongoTemplate.findOne(Query.query(Criteria.where("code").is(code))
					.with(new Sort(new Sort.Order(Sort.Direction.ASC, "date"))).limit(1), Stock.class);
		}

}
