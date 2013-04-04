package waazdoh.app;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import waazdoh.cutils.MLogger;
import waazdoh.cutils.MPreferences;

public class AppPreferences implements MPreferences {
	private Preferences p;
	private MLogger log = MLogger.getLogger(this);
	private String path;
	
	public AppPreferences(String path) {
		this.path = path;
		Properties props = new Properties();
		
		try {
			props.load(new FileReader(path + File.separator + "default.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e);
		}
		p = Preferences.userNodeForPackage(getClass()).node(
				"" + props.getProperty("preferences.node"));
		for (Object okey : props.keySet()) {
			String key = "" + okey;
			String value = "" + props.getProperty(key);
			log.info("default preference setting " + key + "->" + value);
			get(key, value);
		}

	}

	@Override
	public Set<String> getNames() {
		Set<String> ret = new HashSet<String>();
		String[] keys;
		try {
			keys = p.keys();
			for (String string : keys) {
				ret.add(string);
			}
			return ret;
		} catch (BackingStoreException e) {
			log.error(e);
			return null;
		}
	}

	@Override
	public String get(String name, String defaultvalue) {
		if (p.get(name, null) == null && defaultvalue != null) {
			set(name, defaultvalue);
		}
		String parsed = parse(name, p.get(name, defaultvalue));
		log.info("get " + name + " = " + parsed);
		return parsed;
	}

	@Override
	public int getInteger(String string, int i) {
		String sint = get(string, "" + i);
		return Integer.parseInt(sint);
	}

	@Override
	public String get(String string) {
		String value = p.get(string, null);
		if (value != null) {
			String parsed = parse(string, value);
			log.info("get " + string + " = " + parsed);
			return parsed;
		} else {
			log.info("no setting called \"" + string + "\"");
			return null;
		}
	}

	private String parse(String name, String value) {
		if (name.indexOf(".path") > 0) {
			if (value != null && value.indexOf("/") != 0) {
				value = System.getProperty("user.home") + File.separator
						+ value;
			}
		}
		return value;
	}

	@Override
	public void set(String name, String value) {
		p.put(name, value);
	}

	@Override
	public void set(String name, boolean b) {
		set(name, "" + b);
	}

	@Override
	public boolean getBoolean(String valuename) {
		return "true".equals("" + get(valuename));
	}
}
