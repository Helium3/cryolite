package cryolite.io;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import cryolite.progress.IOProgress;

public class ProgressBufferedOutputStream extends BufferedOutputStream {
	
	/**
	 * we can't touch the InputStream as we are FilterInputStream
	 */
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
	public ProgressBufferedOutputStream(OutputStream out, String groupName) {
		super(out);
		ioProgress = IOProgress.getInstance(groupName);		
	}
	
	public ProgressBufferedOutputStream(OutputStream out, int size, String groupName) {
		super(out, size);
		ioProgress = IOProgress.getInstance(groupName);
	}
	
	public void write(int c) throws IOException {
		out.write(c);
		ioProgress.setProgress(1);
	}

	public void write(byte b[], int off, int len) throws IOException {
		out.write(b, off, len);
		ioProgress.setProgress(len);
	}

	/**
	 * Forget to call the close method will NOT cause the monitor thread keep
	 * running
	 */
	public void close() throws IOException {
		super.close();
		ioProgress.close();
	}
}
