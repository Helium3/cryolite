package cryolite.util;

public class Utils {

	public static int readInt(byte[] b, int offset) {
		return 
			((b[offset++] & 0xff) << 24) +
			((b[offset++] & 0xff) << 16) +
			((b[offset++] & 0xff) <<  8) +
			((b[offset++] & 0xff) <<  0) ;
	}

	public static int readInt(byte[] b) {
		return 
			(b[0] & 0xff) << 24 |
			(b[1] & 0xff) << 16 |
			(b[2] & 0xff) <<  8 |
			(b[3] & 0xff) <<  0;
	}
  
  public static void writeInt(byte[] b, int c) {
    b[0] = (byte) (c >>> 24);
    b[1] = (byte) (c >>> 16);
    b[2] = (byte) (c >>>  8);
    b[3] = (byte) (c >>>  0);
  }
  
  public static void writeInt(byte[] b, int c, int offset) {
    b[offset + 0] = (byte) (c >>> 24);
    b[offset + 1] = (byte) (c >>> 16);
    b[offset + 2] = (byte) (c >>>  8);
    b[offset + 3] = (byte) (c >>>  0);
  }
  
  public static void writeInts(byte[] b, int offset, int ... ints) {
    for (int c : ints) {
      b[offset++] = (byte) (c >>> 24);
      b[offset++] = (byte) (c >>> 16);
      b[offset++] = (byte) (c >>>  8);
      b[offset++] = (byte) (c >>>  0);
    }
  }
  
  public static void writeLong(byte[] b, long c) {
    b[0] = (byte) (c >>> 56);
    b[1] = (byte) (c >>> 48);
    b[2] = (byte) (c >>> 40);
    b[3] = (byte) (c >>> 32);
    b[4] = (byte) (c >>> 24);
    b[5] = (byte) (c >>> 16);
    b[6] = (byte) (c >>>  8);
    b[7] = (byte) (c >>>  0);
  }
  
  public static void writeLong(byte[] b, long c, int offset) {
    b[offset + 0] = (byte) (c >>> 56);
    b[offset + 1] = (byte) (c >>> 48);
    b[offset + 2] = (byte) (c >>> 40);
    b[offset + 3] = (byte) (c >>> 32);
    b[offset + 4] = (byte) (c >>> 24);
    b[offset + 5] = (byte) (c >>> 16);
    b[offset + 6] = (byte) (c >>>  8);
    b[offset + 7] = (byte) (c >>>  0);
  }
  
  public static void writeLongs(byte[] b, int offset, long ... longs) {
    for (long c : longs) {
      b[offset++] = (byte) (c >>> 56);
      b[offset++] = (byte) (c >>> 48);
      b[offset++] = (byte) (c >>> 40);
      b[offset++] = (byte) (c >>> 32);
      b[offset++] = (byte) (c >>> 24);
      b[offset++] = (byte) (c >>> 16);
      b[offset++] = (byte) (c >>>  8);
      b[offset++] = (byte) (c >>>  0);
    }
  }
}
