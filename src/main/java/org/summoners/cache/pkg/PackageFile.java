package org.summoners.cache.pkg;

import org.summoners.cache.*;

/**
 * Representation of a packaged file.
 * @author Riot Games
 * @author Brittan Thomas
 */
public class PackageFile {
	
	/**
	 * Instantiates a new package file.
	 *
	 * @param description
	 *            the description of this file
	 */
	public PackageFile(String[] description) {
		this.name = description[0];
		this.bin = description[1];
		this.offset = Long.parseLong(description[2]);
		this.size = Integer.parseInt(description[3]);
		this.unknown1 = Integer.parseInt(description[4]);
		if (unknown1 != 0)
			System.out.println("PackageFile unknown1: " + unknown1);
	}
	
	/**
	 * The name of this package file.
	 */
	private final String name;
	
	/**
	 * Gets the name of this package file.
	 *
	 * @return the name of this package file
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * The name of this package's bin file.
	 */
	private final String bin;
	
	/**
	 * Gets the name of this package's bin file.
	 *
	 * @return the name of this package's bin file
	 */
	public String getBin() {
		return bin;
	}
	
	/**
	 * The offset of this file.
	 */
	private final long offset;
	
	/**
	 * Gets the offset of this file.
	 *
	 * @return the offset of this file
	 */
	public long getOffset() {
		return offset;
	}
	
	/**
	 * The size of this file.
	 */
	private final int size;
	
	/**
	 * Gets the size of this file.
	 *
	 * @return the size of this file
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * An unknown attribute of this file.
	 */
	private final int unknown1;
	
	/**
	 * The manifest for this file.
	 */
	private RiotFileManifest manifest;
	
	/**
	 * Gets the manifest for this file.
	 *
	 * @return the manifest for this file
	 */
	public RiotFileManifest getManifest() {
		return manifest;
	}

	/**
	 * Sets the manifest for this file.
	 *
	 * @param manifest
	 *            the new manifest for this file
	 */
	public void setManifest(RiotFileManifest manifest) {
		this.manifest = manifest;
	}
}
