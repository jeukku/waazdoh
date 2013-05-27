package cmusic.client;

import junit.framework.TestCase;
import waazdoh.common.waves.WaveOutput;
import waazdoh.cutils.xml.JBean;

public class TestWaveOutput extends TestCase {
	public void testSin() {
		WaveOutput o = new WaveOutput();
		o.setType(WaveOutput.TYPE_SIN);
		JBean b = o.getBean();
		assertNotNull(b);
		//
		WaveOutput bwave = new WaveOutput(b);
		assertTrue(bwave.getType() == WaveOutput.TYPE_SIN);
	}
}
