package cryolite.io;

import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import cryolite.progress.IOProgress;

/**
 * InputStream with a 5 seconds progress output
 * For small read use ProgressBufferedInputStream instead
 * @author jds
 * 
 */
public class ProgressInputStream extends FilterInputStream {

	// percentage of data set has been processed
	protected IOProgress ioProgress;
	private final byte[] oneByte = new byte[1];

	/**
	 * InputStream that keep a progress monitor Print progress every 5 seconds
	 * 
	 * @param in
	 *            The underlying InputStream
	 * @param groupName
	 *            Progress from same group share the progress
	 * @throws FileNotFoundException
	 */
	public ProgressInputStream(InputStream in, String groupName) {
		super(in);
		ioProgress = IOProgress.getInstance(groupName);
	}

	public synchronized int read() throws IOException {
		in.read(oneByte, 0, 1);
		return oneByte[0];
	}

	public synchronized int read(byte b[], int off, int len) throws IOException {
		int c = in.read(b, off, len);
		if (c != -1) {
			ioProgress.setProgress(c);
		}
		return c;
	}

	/**
	 * Forget to call the close method will NOT cause the progress monitor
	 * thread keep running
	 */
	public synchronized void close() throws IOException {
		super.close();
		ioProgress.close();
	}
}
