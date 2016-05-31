package org.summoners.cache;

import java.io.*;

import org.summoners.util.*;

/**
 * An output stream for a Riot file to be appended to the RAF data file.
 * @author Xupwup
 */
public class RiotFileOutputStream extends OutputStream {
	
	/**
	 * Instantiates a new RAF output stream.
	 *
	 * @param raf
	 *            the RAF being written to
	 * @param manifest
	 *            the Riot manifest file for size comparison
	 * @param file
	 *            the Riot file which is being written
	 * @param buffer
	 *            the RAF data buffer
	 */
	public RiotFileOutputStream(RiotArchiveFile raf, RiotFileManifest manifest, RiotFile file) {
		this.raf = raf;
		this.manifest = manifest;
		this.file = file;
	}
	
	/**
	 * The Riot archive file being written to.
	 */
	private final RiotArchiveFile raf;
	
	/**
	 * The Riot manifest file for size comparison.
	 */
	private final RiotFileManifest manifest;
	
	/**
	 * The Riot file which is being written.
	 */
	private final RiotFile file;
	
	/**
	 * The current count of bytes written to the buffer.
	 */
	private int count = 0;
	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int i) throws IOException {
		write(new byte[] { (byte) i });
	}
	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] bytes) throws IOException {
		write(bytes, 0, bytes.length);
	}
	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] bytes, int off, int len) throws IOException {
		synchronized (raf.getDataFile()) {
			raf.getDataFile().seek(file.getOffset() + count);
			raf.getDataFile().write(bytes, off, len);
			count += len;
		}
		Validate.require(count > (manifest.getFileType().isCompressed() ? manifest.getSizeCompressed() : manifest.getSizeUncompressed()),
				"More bytes written than expected.", IOException.class);
		
		/*synchronized (buffer) {
			buffer.require((int) file.getOffset() + count + len);
			buffer.toByteBuffer().position((int) file.getOffset() + count);
			buffer.write(bytes, off, len);
			count += len;
		}
		if (count > (manifest.getFileType().isCompressed() ? manifest.getSizeCompressed() : manifest.getSizeUncompressed()))
			throw new IOException("More bytes written than expected.");*/
	}
	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		super.close();
        file.setSize(count);
        raf.setChanged(true);
	}
}