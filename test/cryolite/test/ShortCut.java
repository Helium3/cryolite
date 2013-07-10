package cryolite.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import cryolite.progress.*;
import cryolite.util.TimeLog;

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
	public void testAll() throws InterruptedException {
		cp().close();
		cp("abc").close();
		
		iop().close();
		iop("abc").close();
		
		IOProgress iop = iop("same");
		iop = iop("same");
		iop.setProgress(1);
		Thread.sleep(10000);
		iop.setProgress(1);
		iop.close();
		iop.setProgress(1);
		Thread.sleep(9000);
		iop.setProgress(1);
		iop.close();
		
		CounterProgress cp = cp("same");
		cp = cp("same");
		cp.setProgress(1);
		Thread.sleep(10000);
		cp.setProgress(1);
		cp.close();
		cp.setProgress(1);
		Thread.sleep(9000);
		cp.setProgress(1);
		cp.close();
		
		TimeLog tl = tl();
		tl.tic();
		tl.tac();
	}
	
	public static void main(String args[]) throws InterruptedException {
		new ShortCut().testAll();
	}
}
