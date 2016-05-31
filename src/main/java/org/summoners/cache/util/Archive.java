package org.summoners.cache.util;

import java.util.*;

/**
 * Representation of a generic archive.
 * @author Brittan Thomas
 * 
 * @param <E>
 *            the element type that is being contained in this archive.
 */
public abstract class Archive<E> {
	
	/**
	 * A set of files contained in this archive.
	 */
	protected ArrayList<E> files = new ArrayList<>();
	
	/**
	 * Gets the a set of files contained in this archive.
	 *
	 * @return the a set of files contained in this archive
	 */
	public ArrayList<E> getFiles() {
		return files;
	}
	
}
