package cryolite.test;

import cryolite.progress.IOProgress;

public class TestIOProgress {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ShortCut s = new ShortCut();
		IOProgress iop = s.iop();
		for(int i = 0; i < 100000000; i++) {
			iop.setProgress(1);
		}
		iop.close();
	}

}
