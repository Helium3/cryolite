package cryolite.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A thread that can be stopped
 * 
 * @author jds
 * 
 */
public abstract class Stoppable implements Runnable {

	protected volatile boolean stop = false;

	protected Log LOG = LogFactory.getLog(this.getClass());

	/**
	 * Stop the thread through a flag If multi thread try to stop the monitor,
	 * only the first takes effect
	 */
	public void cancel() {
      stop = true;
	  synchronized(this) {
		this.notify();
	  }
	}

	public void run() {
		setup();
		while (!stop) {
			runTask();
		}
		cleanup();
	}

	/**
	 * Anything before the thread loop
	 */
	protected void setup() {
	}

	/**
	 * Anything after the thread loop
	 */
	protected void cleanup() {
	}

	/**
	 * Where actual work take place
	 */
	protected abstract void runTask();
}
