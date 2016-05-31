package org.summoners.cache;

/**
 * Representation of a Riot file directory.
 * @author Brittan Thomas
 */
public class RiotDirectory {
	
	/**
	 * Instantiates a new riot directory.
	 *
	 * @param name
	 *            the name of the directory
	 */
	public RiotDirectory(String name) {
		this.name = name;
		this.path = name.length() == 0 ? "" : name + "/";
	}

	/**
	 * The directories this directory contains.
	 */
	private RiotDirectory[] subdirs;
	
	/**
	 * Gets the directories this directory contains.
	 *
	 * @return the directories this directory contains
	 */
	public RiotDirectory[] getSubdirs() {
		return subdirs;
	}

	/**
	 * Sets the directories this directory contains.
	 *
	 * @param subdirs
	 *            the new directories this directory contains
	 */
	public void setSubdirs(RiotDirectory[] subdirs) {
		this.subdirs = subdirs;
	}
	
	/**
	 * The manifest files this directory contains.
	 */
	private RiotFileManifest[] files;

	/**
	 * Gets the manifest files this directory contains.
	 *
	 * @return the manifest files this directory contains
	 */
	public RiotFileManifest[] getFiles() {
		return files;
	}

	/**
	 * Sets the manifest files this directory contains.
	 *
	 * @param files
	 *            the new manifest files this directory contains
	 */
	public void setFiles(RiotFileManifest[] files) {
		this.files = files;
	}
	
	/**
	 * The name of the directory.
	 */
	private String name;

	/**
	 * Gets the name of the directory.
	 *
	 * @return the name of the directory
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the directory.
	 *
	 * @param name
	 *            the new name of the directory
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * The file path of the directory.
	 */
	private String path;

	/**
	 * Gets the file path of the directory.
	 *
	 * @return the file path of the directory
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the file path of the directory.
	 *
	 * @param path
	 *            the new file path of the directory
	 */
	public void setPath(String path) {
		this.path = path;
	}
}
