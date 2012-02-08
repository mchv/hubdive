package util.connection;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache extends LinkedHashMap<String, String>{

	private int maxSize;
	private int ttl;
	private long lastResetTime;
	
	public LRUCache (int maxSize, int ttl) {
		super(maxSize+1, 1, true);
		this.maxSize = maxSize;
		this.ttl = ttl;
		flush();
	}

	@Override
	public boolean containsKey(Object key) {
		if(timeToFlush())
			flush();
		return super.containsKey(key);
	}

	private boolean timeToFlush() {
		long currentTime = System.currentTimeMillis();
		return ((currentTime - lastResetTime) >= ttl);
	}
	
	private void flush() {
		this.clear();
		lastResetTime = System.currentTimeMillis();
		
	}
	

	/* This method is called just after a new entry has been added*/
    public boolean removeEldestEntry(Map.Entry eldest) {
        return size() > maxSize;
    }

	
}
