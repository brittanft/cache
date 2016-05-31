package org.summoners.cache.util;

import java.io.*;
import java.nio.*;

import org.summoners.util.*;

/**
 * An expanding, no-capacity byte buffer wrapper with an output stream.
 * @author Joseph Robert Melsha (jrmelsha@olivet.edu)
 * @author Brittan Thomas
 */
public class ExpandingByteBufferOutputStream extends OutputStream {
	
	/**
	 * If this byte buffer should be direct or not.
	 */
	protected final boolean direct;
	
	/**
	 * If this byte buffer should be in little-endian order.
	 */
	protected final boolean little;
	
	/**
	 * The initial size of this expanding byte buffer.
	 */
	protected final int initialSize;
	
	/**
	 * The handle of this byte buffer wrapper.
	 */
	protected ByteBuffer buffer;
	
	/**
	 * The task to be ran on this stream's close.
	 */
	protected Runnable closeTask;
	
	/**
	 * Sets the task to be ran on this stream's close.
	 *
	 * @param closeTask
	 *            the new task to be ran on this stream's close
	 */
	public void setCloseTask(Runnable closeTask) {
		this.closeTask = closeTask;
	}

	/**
	 * Instantiates a new expanding byte buffer output stream.
	 *
	 * @param direct
	 *            if this byte buffer should be direct or not
	 */
	public ExpandingByteBufferOutputStream(boolean direct) {
		this(direct, false, 16);
	}

	/**
	 * Instantiates a new expanding byte buffer output stream.
	 *
	 * @param direct
	 *            if this byte buffer should be direct or not
	 * @param little
	 *            if this byte buffer should be in little-endian order or not
	 */
	public ExpandingByteBufferOutputStream(boolean direct, boolean little) {
		this(direct, little, 16);
	}

	/**
	 * Instantiates a new expanding byte buffer output stream.
	 *
	 * @param direct
	 *            if this byte buffer should be direct or not
	 * @param little
	 *            if this byte buffer should be in little-endian order or not
	 * @param initialSize
	 *            the initial size of this byte buffer
	 */
	public ExpandingByteBufferOutputStream(boolean direct, boolean little, int initialSize) {
		this.direct = direct;
		this.little = little;
		this.initialSize = initialSize;
		clear(true);
	}

	/**
	 * Writes the specified byte buffer onto this buffer.
	 *
	 * @param buf
	 *            the buffer to be written
	 */
	public void write(ByteBuffer buf) {
		if (buf.hasRemaining())
			require(buf.remaining()).put(buf);
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		write(new byte[] { (byte) b }, 0, 1);
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		write(ByteBuffer.wrap(b, off, len));
	}

	/**
	 * Ensures the specified expansion can be handled.
	 *
	 * @param expansion
	 *            the amount of bytes this buffer will expand on
	 * @return the byte buffer
	 */
	public ByteBuffer require(int expansion) {
		Validate.require(expansion >= 0);
		if (buffer != null) {
			int rem = buffer.remaining();
			if (expansion > rem) {
				buffer.flip();
				int size = buffer.remaining();
				expansion += size;
				expansion = Math.max(expansion, buffer.capacity() << 1);
				expansion = Math.max(expansion, initialSize);
				ByteBuffer b = BufferUtil.allocate(expansion, direct, little);
				b.put(buffer);
				buffer = b;
			}
		} else {
			expansion = Math.max(expansion, initialSize);
			buffer = BufferUtil.allocate(expansion, direct, little);
		}
		return buffer;
	}

	/**
	 * Clears (unsafely) the byte buffer.
	 */
	public void clear() {
		clear(false);
	}

	/**
	 * Clears the byte buffer.
	 *
	 * @param safe
	 *            whether the clear should be safe or not
	 */
	public void clear(boolean safe) {
		if (safe) {
			buffer = null;
			if (initialSize > 0)
				buffer = BufferUtil.allocate(initialSize, direct, little);
		} else
			buffer.clear();
	}
	
	/**
	 * Returns this buffer's position.
	 *
	 * @return the position of this buffer
	 */
	public int position() {
		if (buffer == null)
			return 0;
		return buffer.position();
	}

	/**
	 * Turns this expandable byte buffer into a byte buffer instance.
	 *
	 * @return the resulting (non-copied) byte buffer
	 */
	public ByteBuffer toByteBuffer() {
		return toByteBuffer(false);
	}

	/**
	 * Turns this expandable byte buffer into a byte buffer instance.
	 *
	 * @param copy
	 *            whether the result should be a copy or not
	 * @return the byte buffer
	 */
	public ByteBuffer toByteBuffer(boolean copy) {
		ByteBuffer buf = buffer;
		if (buf == null)
			buf = BufferUtil.allocate(0, direct, little);
		else {
			buf = buf.duplicate();
			buf.flip();
			if (copy) {
				ByteBuffer tmp = BufferUtil.allocate(buf.remaining(), direct, little);
				tmp.put(buf);
				tmp.flip();
				buf = tmp;
			}
		}
		return buf;
	}

	/**
	 * Turns this expandable byte buffer into a byte array.
	 *
	 * @return the resulting, copied byte array.
	 */
	public byte[] toByteArray() {
		return BufferUtil.toByteArray(toByteBuffer(false), true);
	}
	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		super.close();
		if (closeTask != null)
			closeTask.run();
	}
}