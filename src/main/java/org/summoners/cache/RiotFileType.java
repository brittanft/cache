package org.summoners.cache;

import org.summoners.util.*;

/**
 * Representation of Riot's file types.
 */
public enum RiotFileType {
	NORMAL(),
	UNKNOWN_1(),
	COMPRESSED(),
	UNKNOWN_3(),
	COPY_TO_SLN(),
	MANAGED(),
	UNCOMPRESSED_ARCHIVE(false),
	UNKNOWN_7(),
	UNKNOWN_8(),
	UNKNOWN_9(),
	UNKNOWN_10(),
	UNKNOWN_11(),
	UNKNOWN_12(),
	UNKNOWN_13(),
	UNKNOWN_14(),
	UNKNOWN_15(),
	UNKNOWN_16(),
	UNKNOWN_17(),
	UNKNOWN_18(),
	UNKNOWN_19(),
	UNKNOWN_20(),
	UNKNOWN_21(),
	COMPRESSED_ARCHIVE();
	
	/**
	 * Instantiates a new Riot file type.
	 */
	private RiotFileType() {
		this(true);
	}
	
	/**
	 * Instantiates a new Riot file type.
	 *
	 * @param compressed
	 *            if this file type is compressed
	 */
	private RiotFileType(boolean compressed) {
		this.compressed = compressed;
	}
	
	/**
	 * If this file type is compressed.
	 */
	private boolean compressed;
	
	/**
	 * Checks if this file type is compressed.
	 *
	 * @return if this file type is compressed
	 */
	public boolean isCompressed() {
		return compressed;
	}
	
	/**
	 * Checks if this file type is an archive file type.
	 *
	 * @return true, if this file type is an archive file type
	 */
	public boolean isArchive() {
		return this == UNCOMPRESSED_ARCHIVE || this == COMPRESSED_ARCHIVE;
	}
	
	/**
	 * The static, final singletoned array of file types.
	 */
	public final static RiotFileType[] VALUES = values();
	
	/**
	 * Gets the file type from the file type id.
	 *
	 * @param id
	 *            the file type id
	 * @return the riot file type
	 */
	public static RiotFileType fromId(int id) {
		Validate.requireFalse(id > 22, "Unhandled ID: " + id, IllegalStateException.class);
		return VALUES[id];
	}
}
