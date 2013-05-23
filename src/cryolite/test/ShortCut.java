package cryolite.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import cryolite.core.TimeLog;
import cryolite.progress.*;

/**
 * Convient class for writing test
 * @author jds
 *
 */
public class ShortCut {

	public CounterProgress cp() {
		String name = getFather();
		return CounterProgress.getInstance(name);
	}

	public CounterProgress cp(String name) {
		return CounterProgress.getInstance(name);
	}

	public IOProgress iop() {
		String name = getFather();
		return IOProgress.getInstance(name);
	}

	public IOProgress iop(String name) {
		return IOProgress.getInstance(name);
	}
	
	public TimeLog tl() {
		Log LOG = LogFactory.getLog(getFather());
		return new TimeLog(LOG);
	}
	
	private String getFather() {
		return Thread.currentThread().getStackTrace()[3].toString();
	}
	
	@Test
	public void testAll() {
		cp().cancel();
		cp("abc").cancel();
		
		iop().cancel();
		iop("abc").cancel();
		
		TimeLog tl = tl();
		tl.tic();
		tl.tac();
	}
	
	public static void main(String args[]) {
		new ShortCut().testAll();
	}
}
