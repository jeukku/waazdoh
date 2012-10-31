package waazdoh.common.model;

import java.util.HashMap;
import java.util.Map;

import org.cutils.JBeanResponse;
import org.cutils.MID;
import org.cutils.MURL;
import org.cutils.UserID;
import org.utils.xml.JBean;

import waazdoh.service.CMService;

public class StaticService implements CMService {
	private UserID userid;
	private static Map<MID, JBean> data = new HashMap<MID, JBean>();

	@Override
	public boolean setSession(String username, String session) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getInfoText() {
		return "staticservice:" + data;
	}

	@Override
	public JBeanResponse reportDownload(MID id, boolean success) {
		// TODO Auto-generated method stub
		return null;
	}

	public JBeanResponse getUser(UserID userid) {
		return null;
	};

	@Override
	public String getSessionID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String requestAppLogin(String username, String appname, MID appid) {
		// TODO Auto-generated method stub
		return null;
	}

	public StaticService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean publish(MID id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JBeanResponse read(MID id) {
		JBean bean = data.get(id);
		if (bean != null) {
			JBeanResponse resp = JBeanResponse.getTrue();
			resp.setBean(bean);
			return resp;
		} else {
			return JBeanResponse.getFalse();
		}
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MURL getURL(String service, String method, MID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JBeanResponse write(MID id, JBean b) {
		data.put(id, b);
		return JBeanResponse.getTrue();
	}

	@Override
	public UserID getUserID() {
		if (userid == null) {
			userid = new UserID((new MID()).toString());
		}
		return userid;
	}

	@Override
	public JBeanResponse search(String filter, int index, int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JBeanResponse getBookmarkGroup(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getBookmarkGroups() {
		// TODO Auto-generated method stub
		return null;
	}

}
