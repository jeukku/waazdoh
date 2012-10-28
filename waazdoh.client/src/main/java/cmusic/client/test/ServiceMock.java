package cmusic.client.test;

import java.util.HashMap;
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

	private static Map<MID, JBeanResponse> objects = new HashMap<MID, JBeanResponse>();

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

}
