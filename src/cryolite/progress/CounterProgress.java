package cryolite.progress;

import java.util.HashMap;

public class CounterProgress extends Progress {

	private static HashMap<String, CounterProgress> counterProgressMap = new HashMap<String, CounterProgress>();
	private int refCount = 1;
	/**
	 * Factory to get an CounterProgress
	 * 
	 * @param groupName
	 * @return
	 */
	public static synchronized CounterProgress getInstance(String groupName) {
		CounterProgress p = counterProgressMap.get(groupName);
		if (p == null) {
			p = new CounterProgress(groupName);
			counterProgressMap.put(groupName, p);
		} else {
			p.refCount++;
		}
		return p;
	}

	/**
	 * Output a progress every 10 seconds
	 */
	private CounterProgress(String name) {
		super(name, 10000);
	}

	/**
	 * The accumulated number of the Progress
	 */
	public String format() {
		long now = System.currentTimeMillis();
		return String.format("%s: Total %d processed, avg: %d/s", name,
				sum.get(), 1000 * sum.get() / (now - start + 1));
	}
	
	public synchronized void close() {		
		if(--refCount != 0) {
			return;
		}
		counterProgressMap.remove(name);
		
		super.close();
		progress();
	}
}
