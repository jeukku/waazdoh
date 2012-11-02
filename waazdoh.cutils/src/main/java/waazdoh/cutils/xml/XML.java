package waazdoh.cutils.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class XML {
	private String string;
	
	public XML(String string) {
		this.string = string;
	}
	
	public String getString() {
		return string;
	}
	
	public void setString(String string) {
		this.string = string;
	}
	
	public XML(Reader stringReader) throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(stringReader);
		while (true) {
			String line = br.readLine();
			if (line == null) {
				break;
			}
			sb.append(line);
			sb.append("\n");
		}
		string = sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof XML) {
			XML b = (XML) obj;
			return b.string.equals(string);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return string.hashCode();
	}
	
	@Override
	public String toString() {
		return "" + string;
	}
}
