package waazdoh.cutils;

public class UserID {
	private String id;

	public UserID(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UserID && obj != null) {
			UserID nid = (UserID) obj;
			return id.equals(nid.id);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return id;
	}
}