package com.net128.app.album.persistence;

import com.net128.app.album.persistence.MongoDatastoreFactory;
import com.swisscom.io.extwc.sdk.helper.WebConnectorBotParameters;

public class RepositoryTest {
	private final static WebConnectorBotParameters webConnectorBotParameters=
		new WebConnectorBotParameters("/application-test.properties", "/credentials.json");
	static {
		MongoDatastoreFactory.init(webConnectorBotParameters.appProperties);
	}
}
