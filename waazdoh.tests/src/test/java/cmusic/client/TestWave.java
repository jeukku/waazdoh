package cmusic.client;

import junit.framework.TestCase;
import waazdoh.CMJobs;
import waazdoh.client.test.TestPWaveSource;
import waazdoh.common.model.MBinarySource;
import waazdoh.common.model.MEnvironment;
import waazdoh.common.model.MObjectFactory;
import waazdoh.common.model.MWave;
import waazdoh.common.model.StaticObjectFactory;
import waazdoh.common.model.StaticService;
import waazdoh.cutils.MCRC;
import waazdoh.cutils.MLogger;
import waazdoh.cutils.UserID;
import waazdoh.service.CMService;


public class TestWave extends TestCase {
	private MLogger log = MLogger.getLogger(this);
	
	public void testWave() {
		final TestPWaveSource storage = new TestPWaveSource(new TestPreferences(
				"testwave"));
		final StaticService service = new StaticService();
		storage.setService(service);
				
		MEnvironment env = new MEnvironment() {
			
			private MObjectFactory objectfactory = new StaticObjectFactory();

			@Override
			public UserID getUserID() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public CMService getService() {
				return service;
			}
			
			@Override
			public CMJobs getJobs() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public MBinarySource getBinarySource() {
				return storage;
			}
			
			@Override
			public MObjectFactory getObjectFactory() {
				return objectfactory ;
			}
		};
		
		MWave w = env.getObjectFactory().newWave(0, env);
		//
		for (int i = 0; i < 100000; i++) {
			w.addSamples(new float[] { i }, 1);
		}
		w.setReady();
		w.save();
		w.publish();
		//
		MCRC crc = w.getCRC();
		assertNotNull(crc);
		assertFalse(crc.isError());
		//
		assertNotNull(w.getBinary());
	}
}
