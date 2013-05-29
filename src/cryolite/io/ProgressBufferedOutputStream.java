package cryolite.io;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;

public class ProgressBufferedOutputStream extends BufferedOutputStream {

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
		super(new ProgressOutputStream(out, groupName));
	}
	
	public ProgressBufferedOutputStream(OutputStream out, int size, String groupName) {
		super(new ProgressOutputStream(out, groupName), size);
	}
}
