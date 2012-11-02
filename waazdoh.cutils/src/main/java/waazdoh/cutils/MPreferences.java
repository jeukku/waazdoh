package waazdoh.cutils;

import java.util.Set;

public interface MPreferences {
	static final String SERVICE_URL = "service.url";
	static final String LOCAL_PATH = "local.path";
	static final String SERVERLIST = "server.list";
	static final String NETWORK_MAX_DOWNLOADS = "network.downloads.max";
	static final String MEMORY_MAX_USAGE = "memory.max";
	static final String SERVICE_MOCK = "service.mock";

	String get(String string);

	String get(String name, String defaultvalue);

	boolean getBoolean(String name);

	void set(String name, String value);

	void set(String name, boolean b);

	int getInteger(String string, int i);

	Set<String> getNames();
}
