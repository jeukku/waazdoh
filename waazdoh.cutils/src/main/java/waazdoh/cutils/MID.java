package waazdoh.cutils;

import java.util.UUID;

public class MID {
	private final String id;
	private Version version;
	
	public MID(String value) {
		int i = value.indexOf(".");
		id = value.substring(0, i);
		MID.check(id);
		version = new Version(value.substring(i + 1));
	}
	
	public MID() {
		id = UUID.randomUUID().toString();
		version = new Version();
	}
	
	@Override
	public int hashCode() {
		return id.hashCode() + version.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MID) {
			MID bid = (MID) obj;
			return bid.id.equals(id) && version.equals(bid.version);
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return id + "." + version;
	}
	
	public void updateVersion() {
		this.version = new Version();
	}

	public static void check(String substring) {
		if(substring.length()!=36) {
			throw new IllegalArgumentException("id value length " + substring.length());
		}		
	}

	public MID copy() {
		return new MID(this.toString());
	}
}
