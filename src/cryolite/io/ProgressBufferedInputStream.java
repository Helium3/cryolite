package cryolite.io;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * InputStream with a 5 seconds progress output
 * 
 * @author jds
 * 
 */
public class ProgressBufferedInputStream extends BufferedInputStream {

	/**
	 * InputStream that keep a progress monitor Print progress every 5 seconds
	 * 
	 * @param in
	 *            The underlying InputStream
	 * @param groupName
	 *            Progress from same group share the progress
	 * @throws FileNotFoundException
	 */
	public ProgressBufferedInputStream(InputStream in, String groupName) {
		super(new ProgressInputStream(in, groupName));
	}

	public ProgressBufferedInputStream(InputStream in, int size, String groupName) {
		super(new ProgressInputStream(in, groupName), size);
	}
}
