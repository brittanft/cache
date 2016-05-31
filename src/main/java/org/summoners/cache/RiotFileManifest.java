package org.summoners.cache;

/**
 * Representation of the Riot File Manifest (RFM).
 * 	This file describes the attributes of another file.
 * @author Riot Games
 * @author Brittan Thomas
 */
public class RiotFileManifest implements Comparable<RiotFileManifest> {

	/**
	 * Instantiates a new Riot file manifest.
	 *
	 * @param release
	 *            the release version as a String
	 * @param releaseInt
	 *            the release version as an integer
	 * @param nameIndex
	 *            the index of this file's name
	 * @param checksum
	 *            the checksum of the file being described
	 * @param fileType
	 *            the file type of the file being described
	 * @param sizeUncompressed
	 *            the uncompressed size of the file being described
	 * @param sizeCompressed
	 *            the compressed size of the file being described
	 * @param unknown1
	 *            the unknown1
	 * @param unknown2
	 *            the unknown2
	 */
	public RiotFileManifest(String release, int releaseInt, int nameIndex, byte[] checksum, int fileType, 
					int sizeUncompressed, int sizeCompressed, int unknown1, int unknown2) {
		this.release = release;
		this.releaseInt = releaseInt;
		this.nameIndex = nameIndex;
		this.checksum = checksum;
		this.fileType = RiotFileType.fromId(fileType);
		this.sizeCompressed = sizeCompressed;
		this.sizeUncompressed = sizeUncompressed;
		this.unknown1 = unknown1;
		this.unknown2 = unknown2;
	}

	/**
	 * The release version as a String.
	 */
	private final String release;
	
	/**
	 * Gets the release version as a String.
	 *
	 * @return the release version as a String
	 */
	public String getRelease() {
		return release;
	}
	
	/**
	 * The release version as an integer.
	 */
	private final int releaseInt;

	/**
	 * Gets the release version as an integer.
	 *
	 * @return the release version as an integer
	 */
	public int getReleaseInt() {
		return releaseInt;
	}
	
	/**
	 * The index of this file's name.
	 */
	private final int nameIndex;

	/**
	 * Gets the index of this file's name.
	 *
	 * @return the index of this file's name
	 */
	public int getNameIndex() {
		return nameIndex;
	}
	
	/**
	 * The name of the file being described.
	 */
	private String name;
	
	/**
	 * Gets the name of the file being described.
	 *
	 * @return the name of the file being described
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the file being described.
	 *
	 * @param name
	 *            the new name of the file being described
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * The path of the file being described.
	 */
	private String path;

	/**
	 * Gets the path of the file being described.
	 *
	 * @return the path of the file being described
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Sets the path of the file being described.
	 *
	 * @param path
	 *            the new path of the file being described
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * The checksum of the file being described.
	 */
	private final byte[] checksum;

	/**
	 * Gets the checksum of the file being described.
	 *
	 * @return the checksum of the file being described
	 */
	public byte[] getChecksum() {
		return checksum;
	}
	
	/**
	 * The compressed size of the file being described.
	 */
	private final int sizeCompressed;

	/**
	 * Gets the compressed size of the file being described.
	 *
	 * @return the compressed size of the file being described
	 */
	public int getSizeCompressed() {
		return sizeCompressed;
	}
	
	/**
	 * The uncompressed size of the file being described.
	 */
	private final int sizeUncompressed;

	/**
	 * Gets the uncompressed size of the file being described.
	 *
	 * @return the uncompressed size of the file being described
	 */
	public int getSizeUncompressed() {
		return sizeUncompressed;
	}
	
	/**
	 * The type of the file being described.
	 */
	private final RiotFileType fileType;

	/**
	 * Gets the type of the file being described.
	 *
	 * @return the type of the file being described
	 */
	public RiotFileType getFileType() {
		return fileType;
	}
	
	/**
	 * The unknown1 attribute of the file being described.
	 */
	private final int unknown1;

	/**
	 * Gets the unknown1 attribute of the file being described.
	 *
	 * @return the unknown1 attribute of the file being described
	 */
	public int getUnknown1() {
		return unknown1;
	}
	
	/**
	 * The unknown2 attribute of the file being described.
	 */
	private final int unknown2;

	/**
	 * Gets the unknown2 attribute of the file being described.
	 *
	 * @return the unknown2 attribute of the file being described
	 */
	public int getUnknown2() {
		return unknown2;
	}
	
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return path + name + " " + release + " type:" + fileType + " un:" + sizeUncompressed + " u1:" + unknown1 + " u2:" + unknown2;
    }

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RiotFileManifest o) {
		return Integer.compare(releaseInt, o.releaseInt);
	}
}
