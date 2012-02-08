package util.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import play.Logger;

public class HttpClient {


	private LRUCache cache = new LRUCache(200, 120000);

	private boolean allowEmptyResponse = false;
	
	/**
	 * By default the client does not accept empty response and will loop until the response is not empty or the server return an HTTP error code.
	 */
	public void allowEmptyResponse() {
		allowEmptyResponse = true;
	}
	
	public String get(String url) throws ConnectionException {
		if(cache.containsKey(url)) {
			return cache.get(url);
		}

		
		String response = getNotCached(url);
		
		if (!allowEmptyResponse) {
			/* we sometime get empty responses  -> need to set up a specific server to test that */
			int limit = 0;
			while(response.isEmpty() && limit <7) {
				response = getNotCached(url);
				limit++;
			}
		}
		
		cache.put(url, response);
		return response;
	}
		
	private String getNotCached(String url) throws ConnectionException {
		
		Logger.debug("[HttpClient] NOT cached access to:" + url);		
		Response response;
		
		try {
			response = connect(url);
		} catch (MalformedURLException e) {
			throw new ConnectionException(e);
		} catch (IOException e) {
			throw new ConnectionException(e);
		}

		//TODO handle differents other code
		if (response.getCode() == HttpURLConnection.HTTP_OK) {
			return response.getMessage();
		} else {
			throw new ConnectionException(response.getCode());
		}
		
		
	}
	
	private Response connect(String url) throws IOException {

		URL urlObject = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) urlObject.openConnection();
		conn.connect();

		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} catch (IOException e) {
			in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder content = new StringBuilder();
		for (String line = in.readLine(); line != null; line = in.readLine()) {
			content.append(line + '\n');
		}
		
		String nextURL = conn.getHeaderField("X-Next");
		String lastURL = conn.getHeaderField("X-Last");		
		
		Response response = new Response (conn.getResponseCode(), content.toString(), nextURL, lastURL);
		in.close();
		conn.disconnect();
		return response;		
	}
	
	
	private class Response {
		
		private final int code;
		private final String message;
		private String nextURL;
		private String lastURL;
		
		public Response(int code, String message, String nextUrl, String lastUrl) {
			this.code = code;
			this.message = message;
			this.nextURL = nextUrl;
			this.lastURL = lastUrl;
		}
		
		public int getCode() {
			return code;
		}
		
		public String getMessage() {
			return message;
		}
		
		/* those methods are not yet useful */
		
		public boolean hasNextUrl() {
			return nextURL != null;
		}
		
		
		public String getNextUrl() {
			return nextURL;
		}
		
		public String getLastUrl() {
			return lastURL;
		}
		
	}
	
	
	
	
}
