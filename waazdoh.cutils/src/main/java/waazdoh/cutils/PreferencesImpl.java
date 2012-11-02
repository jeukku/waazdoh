package waazdoh.cutils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PreferencesImpl implements MPreferences {
	private Map<String, String> values = new HashMap<String, String>();

	@Override
	public String get(String string) {
		return values.get(string);
	}

	@Override
	public Set<String> getNames() {
		return values.keySet();
	}
	
	@Override
	public String get(String name, String defaultvalue) {
		String v = get(name);
		if (v == null) {
			set(name, defaultvalue);
		}
		return defaultvalue;
	}

	@Override
	public int getInteger(String string, int i) {
		String sint = get(string, "" + i);
		return Integer.parseInt(sint);
	}

	@Override
	public boolean getBoolean(String name) {
		return Boolean.parseBoolean(get(name));
	}

	@Override
	public void set(String name, String value) {
		values.put(name, value);
	}

	@Override
	public void set(String name, boolean b) {
		set(name, "" + b);
	}
}
