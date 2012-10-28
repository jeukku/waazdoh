package org.cp2p.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.cutils.JBeanResponse;
import org.cutils.MID;
import org.cutils.MLogger;
import org.cutils.MPreferences;
import org.utils.xml.JBean;
import org.utils.xml.XML;

public class MBeanStorage {
	private MLogger log = MLogger.getLogger(this);
	private String path;

	public MBeanStorage(MPreferences preferences) {
		this.path = preferences.get(MPreferences.LOCAL_PATH) + File.separator
				+ "beans";
		File file = new File(path);
		file.mkdirs();
	}

	public JBeanResponse getBean(MID id) {
		try {
			File f = getFile(id);
			if (f.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(f));
				StringBuffer sb = new StringBuffer();
				while (true) {
					String line = br.readLine();
					if (line == null) {
						break;
					}
					sb.append(line);
				}
				br.close();
				//
				XML xml = new XML(sb.toString());
				JBeanResponse resp = new JBeanResponse();
				resp.setBean(new JBean(xml));
				//
				return resp;
			} else {
				return null;
			}
		} catch (IOException e) {
			log.error(e);
			return null;
		}
	}

	private File getFile(MID id) {
		File f = new File(path + File.separator + id);
		return f;
	}

	public void addBean(MID id, JBeanResponse response) {
		try {
			File f = getFile(id);
			FileWriter fw;
			fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(response.getBean().toXML().toString());
			bw.close();
		} catch (IOException e) {
			log.error(e);
		}
	}

}
