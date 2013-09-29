package cryolite.util;

import java.util.Stack;

import org.apache.commons.logging.Log;

public class TimeLog {

	Log LOG;

	public TimeLog(Log LOG) {
		this.LOG = LOG;
	}

	private Stack<Long> startTime = new Stack<Long>();
	private Stack<String> message = new Stack<String>();

	public void tic(String msg) {
		startTime.push(System.currentTimeMillis());
		message.push(msg);
		LOG.info("Tic: " + msg);
	}

	public void tic() {
		tic("");
	}

	public void tac() {
		tac("");
	}

	public void tac(String msg) {
		long start = startTime.pop();
		String startMsg = message.pop();
		long now = System.currentTimeMillis();

		LOG.info(String.format("Tac: (%s) ~ (%s) in %d ms.", startMsg, msg, now
				- start));
	}
}
