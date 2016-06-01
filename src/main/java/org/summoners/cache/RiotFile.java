package org.summoners.cache;

import java.io.*;
import java.nio.*;
import java.util.zip.*;

import org.summoners.nio.*;

/**
 * Representation of a file in an RAF (Riot Archive File).
 * @author Riot Games
 * @author Brittan Thomas
 */
public class RiotFile implements Comparable<RiotFile> {
	
	/**
	 * Instantiates a new RAF using the name and offset.
	 *
	 * @param raf
	 * 			  the RAF containing this file
	 * @param name
	 *            the name description of the file
	 * @param offset
	 *            the cache offset where this file was found
	 */
	public RiotFile(RiotArchiveFile raf, String name, long offset) {
		this.raf = raf;
		this.name = name;
		this.offset = offset;
		this.size = -1;
		this.hash = hash(name);
	}

	/**
	 * Instantiates a new RAF.
	 *
	 * @param raf
	 * 			  the RAF containing this file
	 * @param name
	 *            the name description of this file
	 * @param size
	 *            the size of this file
	 * @param offset
	 *            the cache offset where this file was found
	 * @param index
	 *            the index of this file for referencing purposes
	 * @param hash
	 *            the hash of this file for updating purposes
	 */
	public RiotFile(RiotArchiveFile raf, String name, int size, long offset, int index, int hash) {
		this.raf = raf;
		this.name = name;
		this.size = size;
		this.offset = offset;
		this.index = index;
		this.hash = hash;
	}
	
	/**
	 * The RAF containing this file.
	 */
	private RiotArchiveFile raf;

	/**
	 * The name description of this file.
	 */
	private String name;

	/**
	 * Gets the name description of this file.
	 * 
	 * @return the name description of this file.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the name of this file (excludes path)
	 *
	 * @return the name of this file
	 */
	public String getFileName() {
		return name.substring(name.lastIndexOf('/'));
	}
	
	/**
	 * Gets the path of this file.
	 *
	 * @return the path of this file
	 */
	public String getDirectory() {
		return name.substring(0, name.lastIndexOf('/') + 1);
	}
	
	/**
	 * Gets the name length (in bytes).
	 *
	 * @return the name length (in bytes)
	 */
	public int getNameLength() {
		return name.getBytes().length + 1;
	}

	/**
	 * Sets the name description of this file.
	 *
	 * @param name
	 *            the new name description of this file
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The size of this file.
	 */
	private int size;

	/**
	 * Gets the size of this file.
	 * 
	 * @return the size of this file
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the size of this file.
	 * 
	 * @param size
	 *            the new size of this file
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * The cache offset where this file was found.
	 */
	private long offset;

	/**
	 * Gets the cache offset where this file was found.
	 *
	 * @return the cache offset where this file was found
	 */
	public long getOffset() {
		return offset;
	}

	/**
	 * Sets the cache offset where this file was found.
	 *
	 * @param offset
	 *            the new cache offset where this file was found
	 */
	public void setOffset(long offset) {
		this.offset = offset;
	}

	/**
	 * The index of this file for referencing purposes.
	 */
	private int index;

	/**
	 * Gets the index of this file for referencing purposes.
	 *
	 * @return the index of this file for referencing purposes
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index of this file for referencing purposes.
	 *
	 * @param index
	 *            the new index of this file for referencing purposes
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * The hash of this file for updating purposes.
	 */
	private int hash;

	/**
	 * Gets the hash of this file for updating purposes.
	 *
	 * @return the hash of this file for updating purposes
	 */
	public int getHash() {
		return hash;
	}

	/**
	 * Sets the hash of this file for updating purposes.
	 *
	 * @param hash
	 *            the new hash of this file for updating purposes
	 */
	public void setHash(int hash) {
		this.hash = hash;
	}
	
	/**
	 * Gets a byte buffer for this file's data.
	 *
	 * @return the loaded byte buffer for this file.
	 */
	public ByteBuffer getData() throws IOException {
		ByteBuffer dataBuf = RiotFileUtil.loadLEFile(raf.getDataPath(), false);
		byte[] buffer = new byte[getSize()];
		dataBuf.position((int) getOffset());
		dataBuf.get(buffer);
		return ByteBuffer.wrap(buffer);
	}
	
	public ByteBuffer getRealData() throws IOException {
		ByteBuffer buffer = BufferUtil.getBufferFromInputStream(getInputStream());
		return buffer.order(ByteOrder.LITTLE_ENDIAN);
	}

	/**
	 * Gets an input stream for this file's data and handles compression.
	 *
	 * @return the input stream for this file
	 */
	public InputStream getInputStream() throws IOException {
		InputStream inputStream = getInputStream0();
		if (raf.isCompressed(this))
			inputStream = new InflaterInputStream(inputStream);
		
		return inputStream;
	}

	/**
	 * Gets an input stream for this file's data.
	 *
	 * @return the input stream for this file
	 */
	private InputStream getInputStream0() throws IOException {
		ByteBuffer buffer = getData();
		if (buffer.hasArray())
			return new ByteArrayInputStream(buffer.array());
		
		return new ByteBufferBackedInputStream(buffer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name + " (s: " + size + " o: " + offset + " i: " + index + ")";
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RiotFile o) {
		int compare = Integer.compareUnsigned(hash, o.hash);
		if (compare == 0)
			return name.compareToIgnoreCase(o.name);
		return compare;
	}

	/**
	 * Calculates a hash for a given file path. This is the hash that is part of each FileEntry in a .raf. 
	 * Note that this is the file path to which RAFUnpacker will unpack the file.
	 *
	 * @param filePath
	 *            the file path from which to construct the hash
	 * @return the hash that needs to be set in a FileEntry
	 * @author ArcadeStorm
	 */
	public static int hash(String filePath) {
		long hash = 0;
		long temp;
		for (int i = 0; i < filePath.length(); i++) {
			hash = ((hash << 4) + Character.toLowerCase(filePath.charAt(i))) & 0xffffffff;
			temp = hash & 0xf0000000;
			hash ^= (temp >>> 24);
			hash ^= temp;
		}
		return (int) hash;
	}
}
