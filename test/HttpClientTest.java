import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;


import com.google.gson.annotations.Until;


import play.Logger;
import play.test.UnitTest;
import util.connection.ConnectionException;
import util.connection.HttpClient;


public class HttpClientTest extends UnitTest{

	@Test
	public void availableServer() throws ConnectionException {
		HttpClient client = new HttpClient();
		String response = client.get("http://www.google.fr");
		assertNotNull(response);
		assertTrue(response.length() > 0);
	}

	@Test
	public void unavailabeServerError() {
		HttpClient client = new HttpClient();
		try {
			client.get("http://www.iamnotgoogleidonotexist.fr");
			fail();
		} catch (ConnectionException e) {
			assertTrue(e.getErrorCode() ==0);
		}
	}
	
	@Test
	public void invalidURLError() {
		HttpClient client = new HttpClient();
		try {
			client.get("htssddddksls:/ss");
			fail();
		} catch (ConnectionException e) {
			assertTrue(e.getErrorCode() ==0);
		}
	}
	
	@Test
	public void notFoundError() {
		HttpClient client = new HttpClient();
		try {
			client.get("http://www.google.fr/mchv");
			fail();
		} catch (ConnectionException e) {
			assertTrue(e.getErrorCode() == 404);
		}
	}	
}
