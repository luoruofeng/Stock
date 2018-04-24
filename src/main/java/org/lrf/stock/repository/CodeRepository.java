package org.lrf.stock.repository;

import java.util.Date;
import java.util.List;

import org.lrf.stock.entity.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class CodeRepository{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<Code> findAll(){
		return mongoTemplate.findAll(Code.class);
	}
	
	public void saveCodes(List<Code> codes) {
		mongoTemplate.insertAll(codes);
	}
	
	public void dropCollection() {
		mongoTemplate.dropCollection(Code.class);
	}
	
	public Code getCodeEntityByCode(String code) {
		return mongoTemplate.findOne(Query.query(Criteria.where("code").is(code)), Code.class);
	}
	
	public void saveOrUpdateCode(Code code) {
		mongoTemplate.save(code);
	}
	
	public void update(String code,Date startDate) {
		mongoTemplate.updateFirst(Query.query(Criteria.where("code").is(code)), Update.update("startDate", startDate), Code.class);
	}
}
