package cryolite.io;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class SerializeBuffer extends OutputStream implements DataOutput {

  private byte[] buf;
  private int written = 0;

  public SerializeBuffer() {
    this(64);
  }
  
  public SerializeBuffer(int size) {
    buf = new byte[size];
  }
  
  private void grow(int incr) {
    final int oldCapacity = buf.length;
    if (written + incr <= oldCapacity) return;
    
    int minCapacity = written + incr;
    // overflow-conscious code
    int newCapacity = oldCapacity << 1;
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity < 0) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        newCapacity = Integer.MAX_VALUE;
    }
    buf = Arrays.copyOf(buf, newCapacity);    
  }
  
  @Override
  public void writeBoolean(boolean v) throws IOException {
    grow(1);
    buf[written++] = v ? (byte) 1 : (byte) 0;
  }

  @Override
  public void writeByte(int v) throws IOException {
    grow(1);
    buf[written++] = (byte) v;
  }

  @Override
  public void writeShort(int v) throws IOException {
    grow(2);
    buf[written++] = (byte) (v >>> 8);
    buf[written++] = (byte) (v >>> 0);
  }

  @Override
  public void writeChar(int v) throws IOException {
    grow(2);
    buf[written++] = (byte) (v >>> 8);
    buf[written++] = (byte) (v >>> 0);
  }

  @Override
  public void writeInt(int v) throws IOException {
    grow(4);
    buf[written++] = (byte) (v >>> 24);
    buf[written++] = (byte) (v >>> 16);
    buf[written++] = (byte) (v >>>  8);
    buf[written++] = (byte) (v >>>  0);
  }

  @Override
  public void writeLong(long v) throws IOException {
    grow(8);
    buf[written++] = (byte) (v >>> 56);
    buf[written++] = (byte) (v >>> 48);
    buf[written++] = (byte) (v >>> 40);
    buf[written++] = (byte) (v >>> 32);
    buf[written++] = (byte) (v >>> 24);
    buf[written++] = (byte) (v >>> 16);
    buf[written++] = (byte) (v >>>  8);
    buf[written++] = (byte) (v >>>  0);
  }

  @Override
  public void writeFloat(float v) throws IOException {
    writeInt(Float.floatToIntBits(v));
  }

  @Override
  public void writeDouble(double v) throws IOException {
    writeLong(Double.doubleToLongBits(v));
  }

  @Override
  public void writeBytes(String s) throws IOException {
    int len = s.length();
    grow(len);
    for (int i = 0 ; i < len ; i++) {
        buf[written++] = (byte) s.charAt(i);
    }
  }

  @Override
  public void writeChars(String s) throws IOException {
    int len = s.length();
    grow(len << 1);
    for (int i = 0 ; i < len ; i++) {
        int v = s.charAt(i);
        buf[written++] = (byte) (v >>> 8);
        buf[written++] = (byte) (v >>> 0);
    }
  }

  @Override
  public void writeUTF(String s) throws IOException {
    new DataOutputStream(this).writeUTF(s);
  }

  @Override
  public void write(int b) throws IOException {
    grow(1);
    buf[written++] = (byte) b;
  }

  @Override
  public void write(byte b[], int off, int len) {
    grow(len);
    System.arraycopy(b, off, buf, written, len);
    written += len;
  }

  public byte[] getData() { return buf; }
  public int getLength() { return written; }
  
  public void reset() { 
    written = 0; 
  }
}
