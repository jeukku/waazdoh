package cmusic.client.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cutils.JBeanResponse;
import org.cutils.MID;
import org.cutils.MURL;
import org.cutils.UserID;
import org.utils.xml.JBean;

import waazdoh.service.CMService;

public class ServiceMock implements CMService {
	private String username;
	private String session;
	private MID userid = new MID();
	private Map<String, JBean> groups = new HashMap<String, JBean>();

	private static Map<MID, JBeanResponse> objects = new HashMap<MID, JBeanResponse>();

	public ServiceMock() {
		MID gusersid = new MID();
		String gname = "users";
		addBGroup(gusersid.toString(), gname);
		addBGroup(new MID().toString(), "test");
		
	}

	private void addBGroup(String gid, String gname) {
		JBean b = new JBean("bookmarkgroup");
		b.addAttribute("id", gid);
		b.addAttribute("name", gname);
		groups.put(gid, b);
	}

	@Override
	public JBeanResponse getUser(UserID userid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean setSession(String username, String session) {
		this.username = username;
		this.session = session;
		return true;
	}

	@Override
	public JBeanResponse read(MID id) {
		return ServiceMock.objects.get(id);
	}

	@Override
	public JBeanResponse write(MID id, JBean b) {
		JBeanResponse res = JBeanResponse.getTrue();
		res.setBean(b);
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
		return new UserID(this.userid.toString());
	}

	@Override
	public JBeanResponse search(String filter, int index, int i) {
		// TODO Auto-generated method stub
		return null;
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
		return username != null;
	}

	@Override
	public String requestAppLogin(String username, String appname, MID appid) {
		return new MID().toString();
	}

	@Override
	public String getSessionID() {
		return userid.toString();
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
