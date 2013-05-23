package cryolite.test;

import org.junit.Test;

import cryolite.core.Utils;

public class TestUtils extends TestCommon {
	
	@Test
	public void testPerformance() {
		test_readInt();
		test_writeInt();
	}

	/**
	 * 1608.22MB/s
	 */
	private void test_writeInt() {
		byte[] b = new byte[4];
		int offset = 0;
		int c = 1;
		
		iop = s.iop("writeInt with offset");
		for(int i = 0; i < LOOP; i++) {
			for(int j = 0; j < LOOPSIZE; j++) {
				Utils.writeInt(b, offset, c++);
			}
			iop.setProgress(4*LOOPSIZE);
		}
		iop.cancel();		
	}

	/**
	 * 381469.73MB/s
	 */
	private void test_readInt() {
		byte[] b = new byte[4];
		int offset = 0;
		
		iop = s.iop("readInt with offset");
		for(int i = 0; i < LOOP; i++) {
			for(int j = 0; j < LOOPSIZE; j++) {
				Utils.readInt(b, offset);
			}
			iop.setProgress(4*LOOPSIZE);
		}
		iop.cancel();		
	}

	@Test
	public void testFunctional() {
		int[] arr = new int[] {-1, Integer.MAX_VALUE, Integer.MIN_VALUE, 0, 99, -99};
		
		byte[] buf = new byte[4];
		for(int i = 0; i < arr.length; i++) {
			Utils.writeInt(buf, 0, arr[i]);
			assert Utils.readInt(buf, 0) == arr[i];
		}
		
	}
	
	static public void main(String args[]) {
		new TestUtils().testPerformance();
	}
}
