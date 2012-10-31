package waazdoh.service;

import java.util.HashMap;
import java.util.Map;

import org.cutils.JBeanResponse;
import org.cutils.MID;
import org.cutils.MURL;
import org.cutils.UserID;
import org.utils.xml.JBean;

public interface CMService {
	boolean setSession(String username, String session);
	
	JBeanResponse read(MID id);
	
	JBeanResponse write(MID id, JBean b);
	
	boolean publish(MID id);
	
	UserID getUserID();
	
	JBeanResponse search(
			String filter, int index, int i);
	
	MURL getURL(String service, String method, MID id);
	
	String getUsername();
	
	boolean isLoggedIn();
	
	String requestAppLogin(String username, String appname, MID appid);

	String getSessionID();

	String getInfoText();

	JBeanResponse reportDownload(MID id, boolean success);

	JBeanResponse getBookmarkGroup(String id);

	Map<String, String> getBookmarkGroups();

	JBeanResponse getUser(UserID userid);

}
