package cryolite.progress;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class IOProgress extends Progress {

	private static HashMap<String, IOProgress> ioProgressMap = new HashMap<String, IOProgress>();

	/**
	 * Factory to get an IOProgress
	 * 
	 * @param groupName
	 * @return
	 */
	public static IOProgress getInstance(String groupName) {
		IOProgress p = ioProgressMap.get(groupName);
		if (p == null || (p != null && p.stop)) {
			p = new IOProgress(groupName);
			ioProgressMap.put(groupName, p);
		}
		return p;
	}

	private AtomicLong acc = new AtomicLong(0), sum = new AtomicLong(0);

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

	/**
	 * Set an incremental progress
	 * 
	 * @param acc
	 *            The bytes being read or written
	 */
	public synchronized void setProgress(long progress) {
		this.sum.getAndAdd(progress);
		this.acc.getAndAdd(progress);
	}

	protected void cleanup() {
		LOG.info(String.format(
				"%s:\tavg. %3.2fMB/s with total bytes %d in %d ms", name,
				transform(sum.get(), now - start + 1), sum.get(), (now - start)));
	}

	@Override
	/**
	 * Output the progress
	 */
	protected void output() {
		LOG.info(String.format("%s:\t%3.2fMB/s, avg. %3.2fMB/s", name,
				transform(acc.get(), now - last + 1),
				transform(sum.get(), now - start + 1)));
		acc.set(0);
	}

	// change byte & time to MB/s
	private double transform(long i, long ms) {
		return 1000.0 / 1024 / 1024 * i / ms;
	}
}