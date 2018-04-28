package org.lrf.stock.repository;

import java.util.Date;
import java.util.List;

import org.lrf.stock.entity.CodeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CodeResultRepository{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<CodeResult> findAll(){
		return mongoTemplate.findAll(CodeResult.class);

	}
	
	public void saveCodeResults(List<CodeResult> codeResults) {
		mongoTemplate.insertAll(codeResults);
	}
	
	public void dropCollection() {
		mongoTemplate.dropCollection(CodeResult.class);
	}
	
	public CodeResult getCodeResultByCode(String code) {
		return mongoTemplate.findOne(Query.query(Criteria.where("code").is(code)), CodeResult.class);
	}
	
	public void saveOrUpdateCode(CodeResult codeResult) {
		mongoTemplate.save(codeResult);
	}
	
	public List<CodeResult> getCodeResultByKeyWordAndPeriod(String code,Date startDate,Date endDate,List<Integer> days){
		
		Criteria criteria = Criteria.where("code").is(code).and("date").gte(startDate).lte(endDate).and("numberOfDays").in(days);
		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "date"));
		return mongoTemplate.find(Query.query(criteria).with(sort), CodeResult.class);
	}
	
}
