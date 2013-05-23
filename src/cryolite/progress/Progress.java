package cryolite.progress;

import cryolite.core.Stoppable;

public abstract class Progress extends Stoppable {

	protected String name;
	private int delay;
	protected long start, last, now;

	protected Progress(String name, int delay) {
		this.name = name;
		this.delay = delay;

		new Thread(this).start();
	}

	/**
	 * Set a progress
	 * 
	 * @param progress
	 *            the progress to be accumulated
	 */
	public abstract void setProgress(long progress);

	// wait a certain delay before output progress
	private void waitDelay() {
		try {
			synchronized (this) {
				wait(delay);
			}
		} catch (InterruptedException e) {
			LOG.debug("Interrupted");
			stop = true;
		}
	}

	protected void setup() {
		last = start = now = System.currentTimeMillis();
	}

	/**
	 * 3 steps: 1- wait a certain delay 2- determine whether to stop 3- output
	 * progress
	 */
	protected void runTask() {
		waitDelay();

		if (kill()) {
			stop = true;
			return;
		}

		now = System.currentTimeMillis();
		output();
		last = now;
	}

	/**
	 * Sub-class should specify how to output the progress
	 */
	protected abstract void output();

	/**
	 * The default behavior will cause Progress never stops until the user
	 * cancel it
	 * 
	 * @return whether the Progress are done
	 */
	protected boolean kill() {
		return false;
	}
}
