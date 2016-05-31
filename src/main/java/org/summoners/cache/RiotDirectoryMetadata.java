package org.summoners.cache;

/**
 * Representation of Riot directory metadata.
 * @author Brittan Thomas
 */
public class RiotDirectoryMetadata {
	
	/**
	 * Instantiates a new Riot directory metadata.
	 *
	 * @param nameIndex
	 *            the index where the name is
	 * @param directoryIndex
	 *            the index where the sub directories are
	 * @param directoryCount
	 *            the directory count
	 * @param fileIndex
	 *            the index where the files are
	 * @param fileCount
	 *            the file count
	 */
	public RiotDirectoryMetadata(int nameIndex, int directoryIndex, int directoryCount, int fileIndex, int fileCount) {
		this.nameIndex = nameIndex;
		this.directoryIndex = directoryIndex;
		this.directoryCount = directoryCount;
		this.fileIndex = fileIndex;
		this.fileCount = fileCount;
	}

	/**
	 * The index where the name is.
	 */
	private int nameIndex;
	
	/**
	 * Gets the index where the name is.
	 *
	 * @return the index where the name is
	 */
	public int getNameIndex() {
		return nameIndex;
	}

	/**
	 * The index where the sub directories are.
	 */
	private int directoryIndex;

	/**
	 * Gets the index where the sub directories are.
	 *
	 * @return the index where the sub directories are
	 */
	public int getDirectoryIndex() {
		return directoryIndex;
	}
	
	/**
	 * The directory count.
	 */
	private int directoryCount;

	/**
	 * Gets the directory count.
	 *
	 * @return the directory count
	 */
	public int getDirectoryCount() {
		return directoryCount;
	}
	
	/**
	 * The index where the files are.
	 */
	private int fileIndex;

	/**
	 * Gets the index where the files are.
	 *
	 * @return the index where the files are
	 */
	public int getFileIndex() {
		return fileIndex;
	}
	
	/**
	 * The file count.
	 */
	private int fileCount;

	/**
	 * Gets the file count.
	 *
	 * @return the file count
	 */
	public int getFileCount() {
		return fileCount;
	}
}
