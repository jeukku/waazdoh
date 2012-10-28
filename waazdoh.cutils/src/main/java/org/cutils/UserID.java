package org.cutils;

public class UserID {
	private String id;
	
	public UserID(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
}