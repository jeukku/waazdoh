package cmusic.client;

import org.cutils.MLogger;
import org.utils.xml.JBean;

public class WUser {

	private String name;
	private String img;

	public WUser(JBean bean) {
		/*
		 * <response> <user> <uid>1b32558c-827d-4f4c-83bf-b9ea4a313db6</uid>
		 * <profile>
		 * <pictureURL>https://twimg0-a.akamaihd.net/profile_images/2297908262
		 * /rhp37rm35mul5uf0zom6_reasonably_small.jpeg</pictureURL>
		 * <name>Juuso</name> <info>me!!!</info> </profile> <name>jeukku</name>
		 * </user> <success>true</success> </response>
		 */
		JBean buser = bean.get("user");
		MLogger.getLogger(this).info("user : " + buser);
		name = buser.getAttribute("name");
		JBean profile = buser.get("profile");
		img = profile.getAttribute("pictureURL");
	}

	public String getName() {
		return name;
	}

}
