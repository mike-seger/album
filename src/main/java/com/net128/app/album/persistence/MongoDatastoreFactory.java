package com.net128.app.album.persistence;

import java.util.Properties;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.swisscom.io.extwc.sdk.helper.CfHelper;
import com.swisscom.io.extwc.sdk.helper.CfHelper.Service;

public class MongoDatastoreFactory {
	private final static Logger logger = LoggerFactory.getLogger(MongoDatastoreFactory.class);
	private static MongoClient client;
	private static String uri;
	private static String dbName;
	private static Morphia morphia;
	private static Datastore datastore;
	
	public synchronized static void init(Properties applicationProperties) {
		if(morphia!=null) {
			return;
		}
		morphia = new Morphia();
		Service service=CfHelper.findFirstService("mongodb");
		if(service!=null) {
			uri=service.credentials.uri;
			dbName=service.credentials.database;
		} else {
			uri=applicationProperties.getProperty("mongodb.uri");
			dbName=applicationProperties.getProperty("mongodb.dbname");
		}
		
		morphia.mapPackage(applicationProperties.getProperty("monphia.map.package"));
		
		datastore=morphia.createDatastore(createClient(), dbName);
		datastore.ensureIndexes();
		logger.info("Using mongodb.uri: "+uri);
	}
	
	public static String getDbName() {
		return dbName;
	}

	public static Datastore getDatastore() {
		return datastore;
	}

	public static MongoClient createClient() {
		if(uri==null) {
			throw new RuntimeException("mongodb uri is not initialized");
		}
		if(client==null) {
			client = new MongoClient(new MongoClientURI(uri));			
		}
		return client;
	}
}
