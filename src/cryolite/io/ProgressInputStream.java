package cryolite.io;

import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import cryolite.progress.IOProgress;

/**
 * InputStream with a 5 seconds progress output For small read use
 * ProgressBufferedInputStream instead
 * 
 * @author jds
 * 
 */
public class ProgressInputStream extends FilterInputStream {

	protected IOProgress ioProgress;

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

	public int read() throws IOException {
		return in.read();
	}

	public int read(byte b[], int off, int len) throws IOException {
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
	public void close() throws IOException {
		super.close();
		ioProgress.close();
	}
}
