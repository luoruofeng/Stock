package org.lrf.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;

@Configuration
public class MongoConfig {
	
	@Autowired
	private MongoClient mongoClient;
	
	@Value("${mongo.maxWaitTime}")
	private int maxWaitTime;
	
	@Value("${mongo.maxConnectionLifeTime}")
	private int maxConnectionLifeTime;
	
	@Value("${mongo.connectTimeout}")
	private int connectTimeout;

	@Value("${spring.data.mongodb.database}")
	private String dbName;
	
	@Bean
	public MongoTemplate mongoTemplate() {
		System.out.println("*** change mongodb properties ***");
		MongoClientOptions mongoClientOptions = MongoClientOptions.builder(mongoClient.getMongoClientOptions()).maxWaitTime(maxWaitTime).maxConnectionLifeTime(maxConnectionLifeTime).connectTimeout(connectTimeout).build();
		MongoClient mongoClient = new MongoClient("127.0.0.1:27017", mongoClientOptions);
		return new MongoTemplate(mongoClient, dbName);
	}
}
