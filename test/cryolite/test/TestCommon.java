package cryolite.test;

import cryolite.progress.CounterProgress;
import cryolite.progress.IOProgress;

public abstract class TestCommon {
	protected CounterProgress cp = null;
	protected IOProgress iop = null;
	
	protected int LOOP = 100;
	protected int LOOPSIZE = 1000000;

	protected ShortCut s = new ShortCut();
}
