package cmusic.client;

import org.utils.xml.JBean;

public class WBookmark {
	private String id;
	private String oid;
	private String created;

	public WBookmark(JBean bbookmark) {
		id = bbookmark.getAttribute("bookmarkid");
		oid = bbookmark.getAttribute("objectid");
		created = bbookmark.getAttribute("created");
	}

	public String getObjectID() {
		return oid;
	}
}
