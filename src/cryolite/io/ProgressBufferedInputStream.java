package cryolite.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import cryolite.progress.IOProgress;

public class ProgressBufferedInputStream extends FilterInputStream {

	private static int defaultBufferSize = 8192;

	protected byte buf[];

	protected int count;

	protected int pos;

	protected int markpos = -1;

	protected int marklimit;

	private final IOProgress ioProgress;

	public ProgressBufferedInputStream(InputStream in, String groupName) {
		this(in, defaultBufferSize, groupName);
	}

	public ProgressBufferedInputStream(InputStream in, int size,
			String groupName) {
		super(in);
		if (size <= 0) {
			throw new IllegalArgumentException("Buffer size <= 0");
		}
		buf = new byte[size];
		ioProgress = IOProgress.getInstance(groupName);
	}

	private void fill() throws IOException {
		byte[] buffer = buf;
		if (markpos < 0)
			pos = 0; /* no mark: throw away the buffer */
		else if (pos >= buffer.length) /* no room left in buffer */
			if (markpos > 0) { /* can throw away early part of the buffer */
				int sz = pos - markpos;
				System.arraycopy(buffer, markpos, buffer, 0, sz);
				pos = sz;
				markpos = 0;
			} else if (buffer.length >= marklimit) {
				markpos = -1; /* buffer got too big, invalidate mark */
				pos = 0; /* drop buffer contents */
			} else { /* grow buffer */
				int nsz = pos * 2;
				if (nsz > marklimit)
					nsz = marklimit;
				byte nbuf[] = new byte[nsz];
				System.arraycopy(buffer, 0, nbuf, 0, pos);
				buffer = nbuf;
			}
		count = pos;
		int n = in.read(buffer, pos, buffer.length - pos);
		if (n > 0) {
			count = n + pos;
			ioProgress.setProgress(n);
		}
	}

	public int read() throws IOException {
		if (pos >= count) {
			fill();
			if (pos >= count)
				return -1;
		}
		return buf[pos++] & 0xff;
	}

	private int read1(byte[] b, int off, int len) throws IOException {
		int avail = count - pos;
		if (avail <= 0) {
			/*
			 * If the requested length is at least as large as the buffer, and
			 * if there is no mark/reset activity, do not bother to copy the
			 * bytes into the local buffer. In this way buffered streams will
			 * cascade harmlessly.
			 */
			if (len >= buf.length && markpos < 0) {
				int n = in.read(b, off, len);
				if (n > 0)
					ioProgress.setProgress(n);
				return n;
			}
			fill();
			avail = count - pos;
			if (avail <= 0)
				return -1;
		}
		int cnt = (avail < len) ? avail : len;
		System.arraycopy(buf, pos, b, off, cnt);
		pos += cnt;
		return cnt;
	}

	public int read(byte b[], int off, int len) throws IOException {
		if ((off | len | (off + len) | (b.length - (off + len))) < 0) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return 0;
		}

		int n = 0;
		for (;;) {
			int nread = read1(b, off + n, len - n);
			if (nread <= 0)
				return (n == 0) ? nread : n;
			n += nread;
			if (n >= len)
				return n;
			// if not closed but no bytes available, return
			InputStream input = in;
			if (input != null && input.available() <= 0)
				return n;
		}
	}

	public long skip(long n) throws IOException {
		if (n <= 0) {
			return 0;
		}
		long avail = count - pos;

		if (avail <= 0) {
			// If no mark position set then don't keep in buffer
			if (markpos < 0)
				return in.skip(n);

			// Fill in buffer to save bytes for reset
			fill();
			avail = count - pos;
			if (avail <= 0)
				return 0;
		}

		long skipped = (avail < n) ? avail : n;
		pos += skipped;
		return skipped;
	}

	public int available() throws IOException {
		int n = count - pos;
		int avail = in.available();
		return n > (Integer.MAX_VALUE - avail) ? Integer.MAX_VALUE : n + avail;
	}

	public void mark(int readlimit) {
		marklimit = readlimit;
		markpos = pos;
	}

	public void reset() throws IOException {
		if (markpos < 0)
			throw new IOException("Resetting to invalid mark");
		pos = markpos;
	}

	public boolean markSupported() {
		return true;
	}

	public void close() throws IOException {
		in.close();
		buf = null;
	}
}
