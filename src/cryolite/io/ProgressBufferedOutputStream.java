package cryolite.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cryolite.progress.IOProgress;

public class ProgressBufferedOutputStream extends FilterOutputStream {

	protected byte buf[];

	protected int count;

	private final IOProgress ioProgress;

	public ProgressBufferedOutputStream(OutputStream out, String groupName) {
		this(out, 8192, groupName);
	}

	public ProgressBufferedOutputStream(OutputStream out, int size,
			String groupName) {
		super(out);
		if (size <= 0) {
			throw new IllegalArgumentException("Buffer size <= 0");
		}
		buf = new byte[size];
		ioProgress = IOProgress.getInstance(groupName);
	}

	private void flushBuffer() throws IOException {
		if (count > 0) {
			out.write(buf, 0, count);
			ioProgress.setProgress(count);
			count = 0;
		}
	}

	public void write(int b) throws IOException {
		if (count >= buf.length) {
			flushBuffer();
		}
		buf[count++] = (byte) b;
	}

	public void write(byte b[], int off, int len) throws IOException {
		if (len >= buf.length) {
			/*
			 * If the request length exceeds the size of the output buffer,
			 * flush the output buffer and then write the data directly. In this
			 * way buffered streams will cascade harmlessly.
			 */
			flushBuffer();
			out.write(b, off, len);
			ioProgress.setProgress(len);
			return;
		}
		if (len > buf.length - count) {
			flushBuffer();
		}
		System.arraycopy(b, off, buf, count, len);
		count += len;
	}

	public void flush() throws IOException {
		flushBuffer();
		out.flush();
	}

	public void close() throws IOException {
		super.close();
		ioProgress.close();
	}
}
