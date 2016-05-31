package org.summoners.cache.pkg;

import java.io.*;
import java.util.*;

import org.summoners.cache.*;

/**
 * A package of files stored in the cache.
 * @author Riot Games
 * @author Brittan Thomas
 */
public class Package {
	
	/**
	 * Instantiates a new package.
	 *
	 * @param name
	 *            the name of this package
	 */
	public Package(String name) {
		this.name = name;
	}
	
	/**
	 * The name of this package.
	 */
	private String name;
	
	/**
	 * Gets the name of this package.
	 *
	 * @return the name of this package
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * The list of files in this package.
	 */
	private LinkedList<PackageFile> files = new LinkedList<>();
	
	/**
	 * Gets the next packaged file.
	 *
	 * @return the next packaged file in this list
	 */
	public PackageFile next() {
		return files.get(index);
	}
	
	/**
	 * Gets the list of files in this package.
	 *
	 * @return the list of files in this package
	 */
	public LinkedList<PackageFile> getFiles() {
		return files;
	}
	
	/**
	 * The list of unpacked files from this package.
	 */
	private LinkedList<PackagedData> packagedData = new LinkedList<>();
	
	/**
	 * Opens the specified packaged file from the specified archive file.
	 *
	 * @param packageFile
	 *            the package file to be opened
	 * @param archive
	 *            the archive to retrieve the data from
	 * @param directory
	 *            the directory the file exists in
	 * @return the opened packaged data
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public PackagedData open(PackageFile packageFile, RiotArchiveFile archive, String directory) throws IOException {
		PackagedData openFile = new PackagedData(packageFile, archive, directory);
		packagedData.add(openFile);
		return openFile;
	}
	
	/**
	 * Gets the list of unpacked files from this package.
	 *
	 * @return the list of unpacked files from this package
	 */
	public LinkedList<PackagedData> getPackagedData() {
		return packagedData;
	}
	
	/**
	 * The current index in the list.
	 */
	private int index = 0;
	
	/**
	 * Gets the current index in the list.
	 *
	 * @return the current index in the list
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Sets the current index in the list.
	 *
	 * @param index
	 *            the new current index in the list
	 */
	public void setIndex(int index) {
		this.index = index;
	}
}
