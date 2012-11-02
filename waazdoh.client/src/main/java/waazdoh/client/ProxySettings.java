package waazdoh.client;

import org.apache.commons.httpclient.HttpClient;

public interface ProxySettings {

	void handle(HttpClient httpClient);
}
