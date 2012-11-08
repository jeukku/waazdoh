package waazdoh.client.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import waazdoh.common.model.MBinarySource;
import waazdoh.cutils.JBeanResponse;
import waazdoh.cutils.MID;
import waazdoh.cutils.MURL;
import waazdoh.cutils.UserID;
import waazdoh.cutils.xml.JBean;
import waazdoh.cutils.xml.XML;
import waazdoh.service.CMService;

public class ServiceMock implements CMService {
	private String username;
	private String session;
	private UserID userid;
	private Map<String, JBean> groups = new HashMap<String, JBean>();
	private MBinarySource source;

	private static Map<MID, JBeanResponse> objects = new HashMap<MID, JBeanResponse>();

	public ServiceMock(MBinarySource source) {
		MID gusersid = new MID();
		this.source = source;
		//
		String gname = "users";
		addBGroup(gusersid.toString(), gname);
		addBGroup(new MID().toString(), "test");

	}

	private void addBGroup(String gid, String gname) {
		String sxml = "<response> <bookmarkgroup>		  <owner>1b32558c-827d-4f4c-83bf-b9ea4a313db6</owner>		  <name>users</name>"
				+ "  <groupid>"
				+ gid
				+ "</groupid>		  <created>2012-09-14T03:27:05.200Z</created> <bookmarks> <bookmark>"
				+ "		  <objectid>1b32558c-827d-4f4c-83bf-b9ea4a313db6</objectid>		  <created>Tue Sep 18 10:35:50 UTC 2012</created>		  <bookmarkid>8fbdff42-16f1-4619-9083-1624b8ed4ef4.141a553a-7664-4752-a176-3d19b8faf34e</bookmarkid> </bookmark> <bookmark>"
				+ "		  <objectid>1b32558c-827d-4f4c-83bf-b9ea4a313db6</objectid>"
				+ "		  <created>Thu Sep 20 07:25:19 UTC 2012</created>		  <bookmarkid>6b8f96a2-db16-452f-8038-df8d4c681d2d.15d64b78-4fb0-4948-8543-73cf49cdf627</bookmarkid> </bookmark> </bookmarks> </bookmarkgroup>"
				+ "		  </response>";

		JBean b = new JBean(new XML(sxml));
		groups.put(gid, b);
	}

	@Override
	public JBeanResponse getUser(UserID userid) {
		String sxml = "<response> <user> <uid>"
				+ userid
				+ "</uid><profile><pictureURL>https://twimg0-a.akamaihd.net/profile_images/2297908262/rhp37rm35mul5uf0zom6_reasonably_small.jpeg</pictureURL>	  <name>Juuso</name> <info>me!!!</info> </profile> <name>test"
				+ userid
				+ "</name>		  </user> <success>true</success> </response>";
		JBeanResponse r = JBeanResponse.getTrue();
		r.setBean(new JBean(new XML(sxml)));
		return r;
	}

	@Override
	public boolean setSession(String username, String session) {
		createSession(username);
		return true;
	}

	@Override
	public JBeanResponse read(MID id) {
		JBeanResponse resp = source.getBean(id.toString());
		if (resp == null) {
			resp = ServiceMock.objects.get(id);
		}
		return resp;
	}

	@Override
	public JBeanResponse write(MID id, JBean b) {
		JBeanResponse res = JBeanResponse.getTrue();
		res.setBean(b);
		source.addBean(id.toString(), res);
		ServiceMock.objects.put(id, res);
		return JBeanResponse.getTrue();
	}

	@Override
	public boolean publish(MID id) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public UserID getUserID() {
		return userid;
	}

	@Override
	public JBeanResponse search(String filter, int index, int count) {
		JBeanResponse ret = JBeanResponse.getTrue();
		HashSet<String> list = new HashSet<String>();
		for (int i = index; i < count; i++) {
			list.add("" + new MID());
		}
		ret.getBean().addList("items", list);
		return ret;
	}

	@Override
	public MURL getURL(String service, String method, MID id) {
		return new MURL("localhost");
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isLoggedIn() {
		return username != null && session != null && userid != null;
	}

	@Override
	public String requestAppLogin(String username, String appname, MID appid) {
		createSession(username);
		return session;
	}

	private void createSession(String username) {
		session = new MID().toString();
		userid = new UserID(new MID().toString());
		this.username = username;
	}

	@Override
	public String getSessionID() {
		return session;
	}

	@Override
	public String getInfoText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JBeanResponse reportDownload(MID id, boolean success) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JBeanResponse getBookmarkGroup(String id) {
		JBeanResponse ret = JBeanResponse.getTrue();
		ret.setBean(groups.get(id));
		return ret;
	}

	@Override
	public Map<String, String> getBookmarkGroups() {
		Map<String, String> ret = new HashMap<String, String>();
		for (String id : groups.keySet()) {
			JBean b = groups.get(id);
			ret.put(id, b.getAttribute("name"));
		}
		return ret;
	}
}
