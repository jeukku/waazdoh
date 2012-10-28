package org.cutils;

import java.util.UUID;


public class Version {
	private String id;
	
	public Version() {
		this.id = UUID.randomUUID().toString();
	}
	
	public Version(String substring) {
		MID.check(substring);
		this.id = substring;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Version) {
			Version b = (Version) obj;
			return id.equals(b.id);
		} else {
			return true;
		}
	}
	
	@Override
	public String toString() {
		return "" + id;
	}
}
