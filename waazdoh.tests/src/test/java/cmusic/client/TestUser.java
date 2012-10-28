package cmusic.client;

import java.io.IOException;

public class TestUser extends CMusicTestCase {
	public void testRegister() throws IOException {
		MClient c = getNewClient();
		assertNotNull(c);
	}
	
}
