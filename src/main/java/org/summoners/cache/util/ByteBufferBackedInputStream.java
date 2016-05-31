package org.summoners.cache.util;

import java.io.*;
import java.nio.*;

/**
 * An InputStream implementation backed by a NIO ByteBuffer instance.
 * @author Brittan Thomas
 */
public class ByteBufferBackedInputStream extends InputStream {
	
	/**
	 * The byte buffer backing.
	 */
	ByteBuffer buf;

	/**
	 * Instantiates a new byte buffer backed input stream.
	 *
	 * @param buf
	 *            the byte buffer backing.
	 */
	public ByteBufferBackedInputStream(ByteBuffer buf) {
		this.buf = buf;
	}

	/* (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		if (!buf.hasRemaining()) {
			return -1;
		}
		return buf.get() & 0xFF;
	}

	/* (non-Javadoc)
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte[] bytes, int off, int len) throws IOException {
		if (!buf.hasRemaining()) {
			return -1;
		}
		len = Math.min(len, buf.remaining());
		buf.get(bytes, off, len);
		return len;
	}
}