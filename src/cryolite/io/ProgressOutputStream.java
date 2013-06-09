package cryolite.io;

import java.io.FileNotFoundException;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cryolite.progress.IOProgress;

/**
 * OutputStream with a 5 seconds progress output
 * For small write use ProgressBufferedOutputStream instead
 * @author jds
 * 
 */
public class ProgressOutputStream extends FilterOutputStream {

	// percentage of data set has been processed
	private final byte[] oneByte = new byte[1];
	
  protected IOProgress ioProgress;

	/**
	 * OutputStream that keep a progress monitor Print progress every 5 seconds
	 * 
	 * @param out
	 *            The underlying OutputStream
	 * @param groupName
	 *            Progress from same group share the progress
	 * @throws FileNotFoundException
	 */
	public ProgressOutputStream(OutputStream out, String groupName) {
		super(out);
		ioProgress = IOProgress.getInstance(groupName);
	}

	public synchronized void write(int c) throws IOException {
		oneByte[0] = (byte) c;
		write(oneByte, 0, 1);
	}

	public synchronized void write(byte b[], int off, int len) throws IOException {
		out.write(b, off, len);
		ioProgress.setProgress(len);
	}

	/**
	 * Forget to call the close method will NOT cause the monitor thread keep
	 * running
	 */
	public synchronized void close() throws IOException {
		super.close();
		ioProgress.close();
	}
}
