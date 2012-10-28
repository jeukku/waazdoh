package cmusic.client;

import junit.framework.TestCase;

import org.cutils.MCRC;
import org.cutils.MLogger;
import org.cutils.UserID;

import waazdoh.CMJobs;
import waazdoh.common.model.Binary;
import waazdoh.common.model.MBinarySource;
import waazdoh.common.model.MEnvironment;
import waazdoh.common.model.MObjectFactory;
import waazdoh.common.model.StaticService;
import waazdoh.service.CMService;


public class TestBinary extends TestCase {
	private MLogger log = MLogger.getLogger(this);

	public void testBinary() {

		MEnvironment env = new MEnvironment() {

			@Override
			public UserID getUserID() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CMService getService() {
				return new StaticService();
			}

			@Override
			public MObjectFactory getObjectFactory() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CMJobs getJobs() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public MBinarySource getBinarySource() {
				// TODO Auto-generated method stub
				return null;
			}
		};

		Binary binary = new Binary(env.getService(), "test");
		assertNotNull(binary.getCRC());
		//
		for (int i = 0; i < 100000; i++) {
			byte byt = (byte) (i & 0xff);
			binary.add(new Byte(byt));
		}
		//
		MCRC crc = binary.getCRC();
		assertNotNull(crc);
		//
		Binary binary2 = new Binary(env.getService(), "test2");
		binary2.add(binary.asByteBuffer());
		//
		assertEquals(crc, binary2.getCRC());
		//
		for (int i = 0; i < binary.length(); i++) {
			Byte b = binary2.get(i);
			Byte a = binary.get(i);
			if (a != b) {
				assertEquals(a, b);
			}
		}
	}
}
