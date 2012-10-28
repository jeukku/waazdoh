package cmusic.client.app;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.cutils.MLogger;
import org.cutils.MPreferences;

public class CommandLinePreferences implements MPreferences {
	private MLogger log = MLogger.getLogger(this);

	@Override
	public String get(String string) {
		Properties p = getProperties();
		return p.getProperty(string);
	}

	@Override
	public Set<String> getNames() {
		Set<Object> keys = getProperties().keySet();
		Set<String> ret = new HashSet<String>();
		for (Object object : keys) {
			ret.add("" + object);
		}
		return ret;
	}

	private Properties getProperties() {
		Properties p = new Properties();
		try {
			p.load(new FileReader("cc.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	@Override
	public int getInteger(String string, int i) {
		String sint = get(string, "" + i);
		return Integer.parseInt(sint);
	}

	@Override
	public String get(String name, String defaultvalue) {
		String value = get(name);
		log.info("getting " + name + " got " + value + " default: "
				+ defaultvalue);
		if (value == null) {
			set(name, defaultvalue);
			return defaultvalue;
		} else {
			return value;
		}
	}

	@Override
	public boolean getBoolean(String name) {
		return "true".equals(get(name));
	}

	@Override
	public void set(String name, String value) {
		log.info("setting " + name + " -> " + value);
		Properties p = getProperties();
		if (value != null) {
			p.setProperty(name, value);
			saveProperties(p);
		}
	}

	private void saveProperties(Properties p) {
		try {
			p.store(new FileOutputStream("cc.properties"), "no comment");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void set(String name, boolean b) {
		set(name, "" + b);
	}

}
