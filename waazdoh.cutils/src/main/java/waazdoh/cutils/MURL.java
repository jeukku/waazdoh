package waazdoh.cutils;

import java.net.MalformedURLException;
import java.net.URL;

public class MURL {
	private String append = "";
	private int port;
	private String host;
	
	public MURL(String host, int port) {
		this.host = host;
		this.port = port;
		checkURL();
	}
	
	public MURL(String host) {
		this.host = host;
		checkURL();
	}
	
	@Override
	public String toString() {
		String url = "http://" + host;
		if (port > 0) {
			url += ":" + port;
		}
		url += append;
		return url;
	}
	
	public MURL append(String string) {
		append = append + string;
		checkURL();
		return this;
	}
	
	private void checkURL() {
		append = append.replace("//", "/");
	}
	
	public int getPort() {
		return port;
	}
	
	public String getHost() {
		return host;
	}
	
	public URL getURL() throws MalformedURLException {
		return new URL(toString());
	}
}
