package org.summoners.cache.util;

import java.io.*;
import java.nio.*;

/**
 * A utility class for manipulating and creating ByteBuffers.
 * @author Joseph Robert Melsha (jrmelsha@olivet.edu)
 */
public class BufferUtil {
	
	public static String getString(ByteBuffer buffer, int len) {
		byte[] buf = new byte[len];
		buffer.get(buf);
		
		String name = new String(buf);
		int index = name.indexOf('\0');
		if (index != -1)
			name = name.substring(0, index);
		
		name = name.toLowerCase();
		return name;
	}
	
	public static ByteBuffer getBufferFromInputStream(InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int read; byte[] data = new byte[16384];
		while ((read = is.read(data, 0, data.length)) != -1)
			buffer.write(data, 0, read);
		
		buffer.flush();
		return ByteBuffer.wrap(buffer.toByteArray());
	}
	
	/**
	 * Writes the specified byte buffer to the specified IO output stream.
	 *
	 * @param buf
	 *            the data to be written to the output stream
	 * @param out
	 *            the output stream to be written to
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void writeTo(ByteBuffer buf, OutputStream out) throws IOException {
		int i = buf.arrayOffset() + buf.position();
		int n = buf.remaining();
		byte[] array = buf.array();
		buf.position(buf.position() + n);
		out.write(array, i, n);
	}

	/**
	 * Gets the specified byte buffer as a byte array.
	 *
	 * @param buf
	 *            the byte buffer source
	 * @return the array of bytes the buffer contained
	 */
	public static byte[] asByteArray(ByteBuffer buf) {
		return toByteArray(buf, false);
	}

	/**
	 * Copies the specified byte buffer into a byte array.
	 *
	 * @param buf
	 *            the byte buffer source
	 * @return the array (copy) of bytes the buffer contained
	 */
	public static byte[] toByteArray(ByteBuffer buf) {
		return toByteArray(buf, true);
	}

	/**
	 * Converts a byte buffer into a byte array
	 *
	 * @param buf
	 *            the byte buffer source
	 * @param copy
	 *            whether the data should be copied
	 * @return the array of bytes the buffer contained
	 */
	public static byte[] toByteArray(ByteBuffer buf, boolean copy) {
		if (buf.hasArray()) {
			int i = buf.arrayOffset() + buf.position();
			int n = buf.remaining();
			byte[] array = buf.array();
			if (!copy && i == 0 && n == array.length)
				return array;
			byte[] tmp = new byte[n];
			System.arraycopy(array, i, tmp, 0, n);
			return tmp;
		}
		byte[] tmp = new byte[buf.remaining()];
		buf.duplicate().get(tmp);
		return tmp;
	}

	/**
	 * Creates a directly-wrapped byte buffer.
	 *
	 * @param data
	 *            the byte[] of data
	 * @return the directly-wrapped byte buffer
	 */
	public static ByteBuffer wrapDirect(byte[] data) {
		return wrapDirect(data, 0, data.length);
	}

	/**
	 * Creates a directly-wrapped byte buffer.
	 *
	 * @param data
	 *            the byte[] of data
	 * @param offset
	 *            the offset of the data to collect.
	 * @param length
	 *            the length of the desired byte buffer.
	 * @return the directly-wrapped byte buffer.
	 */
	public static ByteBuffer wrapDirect(byte[] data, int offset, int length) {
        if ((offset | length | (offset + length) | (data.length - (offset + length))) < 0)
            throw new IndexOutOfBoundsException();
        ByteBuffer buf = ByteBuffer.allocateDirect(length);
        buf.put(data, offset, length);
        buf.flip();
		return buf;
	}

	/**
	 * Creates an allocated byte buffer.
	 *
	 * @param capacity
	 *            the capacity of the byte buffer
	 * @param direct
	 *            whether the byte buffer should be direct or not
	 * @return the byte buffer
	 */
	public static ByteBuffer allocate(int capacity, boolean direct) {
		return direct ? ByteBuffer.allocateDirect(capacity) : ByteBuffer.allocate(capacity);
	}

	/**
	 * Creates an allocated byte buffer.
	 *
	 * @param capacity
	 *            the capacity of the byte buffer
	 * @param direct
	 *            whether the byte buffer should be direct or not
	 * @param little
	 *            whether the byte buffer should be in little-endian order or not
	 * @return the byte buffer
	 */
	public static ByteBuffer allocate(int capacity, boolean direct, boolean little) {
		return allocate(capacity, direct).order(little ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
	}
}
