package cryolite.progress;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class IOProgress extends Progress {

	private static Map<String, IOProgress> ioProgressMap = new HashMap<String, IOProgress>();

	private AtomicInteger refCount = new AtomicInteger(1);
	
	/**
	 * Factory to get an IOProgress
	 * 
	 * @param groupName
	 * @return
	 */
	public static synchronized IOProgress getInstance(String groupName) {
		IOProgress p = ioProgressMap.get(groupName);
		if (p == null) {
			p = new IOProgress(groupName);
			ioProgressMap.put(groupName, p);
		} else {
			p.refCount.incrementAndGet();
		}
		return p;
	}
	
	/**
	 * Initial a I/O progress monitor with default delay 5s
	 * 
	 * @param name
	 *            The group name of the progress monitor
	 */
	private IOProgress(String name) {
		this(name, 5000);
	}

	/**
	 * Initial a I/O progress monitor with a specified delay
	 * 
	 * @param name
	 *            The group name of the progress monitor
	 * @param delay
	 *            Delay between each progress output
	 */
	IOProgress(String name, int delay) {
		super(name, delay);
	}

	public synchronized void close() {		
		if(refCount.decrementAndGet() != 0) {
			return;
		}
		
		ioProgressMap.remove(name);
		super.close();
		long now = System.currentTimeMillis();
		LOG.info(String.format(
				"%s:\tavg. %3.2fMB/s with total bytes %d in %d ms", name,
				transform(sum.get(), now - start + 1), sum.get(), (now - start)));
	}

	@Override
	/**
	 * format the progress
	 */
	protected String format() {
		long now = System.currentTimeMillis();
		return String.format("%s:\t%3.2fMB/s, avg. %3.2fMB/s", name,
				transform(acc.get(), now - last + 1),
				transform(sum.get(), now - start + 1));
	}

	// change byte & time to MB/s
	private double transform(long i, long ms) {
		return 1000.0 / 1024 / 1024 * i / ms;
	}
}