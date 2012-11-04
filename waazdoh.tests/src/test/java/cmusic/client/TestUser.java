package cmusic.client;

import java.io.IOException;

import waazdoh.client.MClient;

public class TestUser extends CMusicTestCase {
	public void testRegister() throws IOException {
		MClient c = getNewClient();
		assertNotNull(c);
	}
	
}
