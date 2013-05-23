package cryolite.progress;

import java.util.HashMap;

public class CounterProgress extends Progress {

	private static HashMap<String, CounterProgress> counterProgressMap = new HashMap<String, CounterProgress>();

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
		}
		return p;
	}

	/**
	 * Output a progress every 10 seconds
	 */
	private CounterProgress(String name) {
		super(name, 10000);
	}

	private long count = 0;

	@Override
	public synchronized void setProgress(long progress) {
		count += progress;
	}

	/**
	 * The accumulated number of the Progress
	 */
	public void output() {
		LOG.info(String.format("%s: Total %d processed, avg: %d/s", name,
				count, 1000 * count / (now - start + 1)));
	}
	
	protected void cleanup() {
		output();
	}
}
