package cryolite.io;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class DeserializeBuffer extends InputStream implements DataInput {

  private byte[] buf = new byte[0];
  private int pos = 0;
  private int count = 0;

  public DeserializeBuffer() {}
  
  @Override
  public void readFully(byte[] b) throws IOException {
    read(b, 0, b.length);
  }

  @Override
  public void readFully(byte[] b, int off, int len) throws IOException {
    read(b, off, len);
  }

  @Override
  public int skipBytes(int n) throws IOException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean readBoolean() throws IOException {
    if (pos + 1 > count) 
      throw new EOFException();
    
    return (buf[pos++] != 0);
  }

  @Override
  public byte readByte() throws IOException {
    if (pos + 1 > count) 
      throw new EOFException();
    
    return (byte) (buf[pos++] & 0xff);
  }

  @Override
  public int readUnsignedByte() throws IOException {
    if (pos + 1 > count) 
      throw new EOFException();
    
    return (buf[pos++] & 0xff);
  }

  @Override
  public short readShort() throws IOException {
    if (pos + 2 > count) 
      throw new EOFException();
    
    return (short)(
        ((buf[pos++] & 0xff) <<  8) |
        ((buf[pos++] & 0xff) <<  0));
  }

  @Override
  public int readUnsignedShort() throws IOException {
    if (pos + 2 > count) 
      throw new EOFException();
    
    return 
        ((buf[pos++] & 0xff) <<  8) |
        ((buf[pos++] & 0xff) <<  0) ;
  }

  @Override
  public char readChar() throws IOException {
    if (pos + 2 > count) 
      throw new EOFException();
    
    return (char)(
        ((buf[pos++] & 0xff) <<  8) |
        ((buf[pos++] & 0xff) <<  0));
  }

  @Override
  public int readInt() throws IOException {
    if (pos + 4 > count)
      throw new EOFException();
    
    return 
        ((buf[pos++] & 0xff) << 24) |
        ((buf[pos++] & 0xff) << 16) |
        ((buf[pos++] & 0xff) <<  8) |
        ((buf[pos++] & 0xff) <<  0) ;
  }

  @Override
  public long readLong() throws IOException {
    if (pos + 8 > count)
      throw new EOFException();
    
    return 
        ((buf[pos++] & 0xff) << 56) |
        ((buf[pos++] & 0xff) << 48) |
        ((buf[pos++] & 0xff) << 40) |
        ((buf[pos++] & 0xff) << 32) |
        ((buf[pos++] & 0xff) << 24) |
        ((buf[pos++] & 0xff) << 16) |
        ((buf[pos++] & 0xff) <<  8) |
        ((buf[pos++] & 0xff) <<  0) ;
  }

  @Override
  public float readFloat() throws IOException {
    return Float.intBitsToFloat(readInt());
  }

  @Override
  public double readDouble() throws IOException {
    return Float.intBitsToFloat(readInt());
  }

  @Override
  @Deprecated
  public String readLine() throws IOException {
    return new DataInputStream(this).readLine();
  }

  @Override
  public String readUTF() throws IOException {
    return DataInputStream.readUTF(this);
  }

  @Override
  public int read() throws IOException {
    return (pos < count) ? (buf[pos++] & 0xff) : -1;
  }
  
  public int read(byte b[], int off, int len) {
    if (b == null) {
      throw new NullPointerException();
    } else if (off < 0 || len < 0 || len > b.length - off) {
      throw new IndexOutOfBoundsException();
    }

    if (pos >= count) {
      return -1;
    }

    int avail = count - pos;
    if (len > avail) {
      len = avail;
    }
    if (len <= 0) {
      return 0;
    }
    System.arraycopy(buf, pos, b, off, len);
    pos += len;
    return len;
  }

  public void reset(byte[] input, int length) {
    this.buf = input;
    this.pos = 0;
    this.count = length;
  }

  public void reset(byte[] input, int start, int length) {
    this.buf = input;
    this.pos = start;
    this.count = start + length;
  }
  
  public byte[] getData() { return buf; }
  public int getPosition() { return pos; }
  public int getLength() { return count; }
}
