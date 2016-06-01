package org.summoners.cache.structure;

/**
 * Representation of a generic archive.
 * @author Brittan Thomas
 * 
 * @param <E>
 *            the element type that is being contained in this archive.
 */
public class VersionedArchive<E> extends Archive<E> {
	
	/**
	 * Instantiates a new versioned archive.
	 *
	 * @param version
	 *            the recorded version of this archive
	 */
	public VersionedArchive(String version) {
		this.version = version;
	}
	
	/**
	 * The recorded version of this archive.
	 */
	protected String version;
	
	/**
	 * Gets the recorded version of this archive.
	 *
	 * @return the recorded version of this archive
	 */
	public String getVersion() {
		return version;
	}
}
