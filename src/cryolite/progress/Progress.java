package cryolite.progress;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Progress {

	protected String name;
	private int delay;
	private int factor;
	protected long start, last;
	protected Log LOG = LogFactory.getLog(this.getClass());
	private volatile boolean stop = false;

	protected AtomicLong acc = new AtomicLong(0), sum = new AtomicLong(0);

	protected Progress(String name, int delay) {
		this.name = name;
		this.delay = delay;
		this.last = this.start = System.currentTimeMillis();
		Monitor._.addEvent(this);
	}
	
	public int getDelay() { return delay; }

	/**
	 * Set a progress
	 * 
	 * @param progress
	 *            the progress to be accumulated
	 */
	public void setProgress(long progress) {
		this.sum.getAndAdd(progress);
		this.acc.getAndAdd(progress);
	}

	/**
	 * wait a certain delay before output progress
	 * @param delay
	 */
	protected void waitDelay(long delay) {
		try {
			synchronized (this) {
				if(stop) return;
				wait(delay);
			}
		} catch (InterruptedException e) {
			LOG.debug("Interrupted");
		}
	}
	
	protected void slowDown() {
		if(factor < 300000)
			factor <<= 1;
	}

	protected void speedUp() {
		factor >>>= 1;
		if(factor == 0) factor = 1;
	}

	/**
	 * Sub-class should specify how to output the progress
	 */
	public void progress() {
		if(acc.get() == 0) {
			slowDown();
		} else {
			speedUp();
		}
		long now = System.currentTimeMillis();
		LOG.info(format(now));
		acc.set(0);
		last = now;
	}

	protected abstract String format(long now);
	
	public void close() {
		Monitor._.eventDone(this);
		synchronized (this) {
			notifyAll();
			stop = true;
		}
	}
}
