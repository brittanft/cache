package org.summoners.cache;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.stream.*;

import org.summoners.cache.util.*;
import org.summoners.util.*;

/**
 * A utility class for reading data from an RAF.
 * @author Xupwup
 */
public class RiotFileUtil {
	
	/**
	 * Gets the full path to a custom file from the specified sub-path.
	 *
	 * @param subPath
	 *            the path to a custom file relative to the custom directory
	 * @return the path to the custom file
	 */
	public static Path getCustom(String subPath) {
		return Paths.get(getCustomPath().toString(), subPath);
	}
	
	/**
	 * Gets the custom file from the specified sub-path.
	 *
	 * @param subPath
	 *            the path to a custom file relative to the custom directory
	 * @return the custom file
	 */
	public static File getCustomFile(String subPath) {
		return getCustom(subPath).toFile();
	}
	
	/**
	 * Gets the custom asset directory path.
	 *
	 * @return the custom asset directory path
	 */
	public static Path getCustomPath() { //TODO
		return null;
	}

	/**
	 * Gets the full path to an official asset file from the specified sub-path.
	 *
	 * @param subPath
	 *            the path to an official asset file relative to the official directory
	 * @return the path to the official asset file
	 */
	public static Path getRADS(String subPath) {
		return Paths.get(getRADSPath().toString(), subPath);
	}

	/**
	 * Gets the official asset file from the specified sub-path.
	 *
	 * @param subPath
	 *            the path to an official asset file relative to the custom directory
	 * @return the official asset file
	 */
	public static File getRADSFile(String subPath) {
		return getRADS(subPath).toFile();
	}
	
	/**
	 * Gets the RADS directory path.
	 *
	 * @return the RADS directory path
	 */
	public static Path getRADSPath() { //TODO
		return Paths.get("C:/Riot Games/League of Legends/RADS/");
	}

	/**
	 * Deletes all of a specified path's contents, recursively.
	 *
	 * @param file
	 *            the file or directory being deleted
	 */
	public static void deleteDir(File file) {
		if (file.isDirectory()) {
			for (File subFile : file.listFiles())
				deleteDir(subFile);
		}
		
		file.delete();
	}
	
	/**
	 * Gets the release name from an integer.
	 *
	 * @param release
	 *            the release version in integer format
	 * @return the release name
	 */
	public static String getReleaseName(int release) {
		return ((release >>> 24) & 0xFF) + "." + ((release >>> 16) & 0xFF) + "." + ((release >>> 8) & 0xFF) + "." + (release & 0xFF);
	}

	/**
	 * Gets the release as an integer.
	 *
	 * @param name
	 *            the release name
	 * @return the release as an integer
	 */
	public static int getReleaseInt(String name) {
		String[] st = name.split("\\.");
		int release = 0;
		for (int i = 0; i < st.length; i++)
			release = release << 8 | Integer.parseInt(st[i]);
		
		return release;
	}
    
	/**
	 * Gets the newest version in the specified directory.
	 *
	 * @param target
	 *            the target directory
	 * @return the newest version in the specified directory
	 */
	public static String getNewestVersionInDirectory(File target) {
		String[] list = target.list((dir, name) -> name.matches("((0|[1-9][0-9]{0,2})\\.){3}(0|[1-9][0-9]{0,2})"));
		return Stream.of(list).sorted((s1, s2) -> getReleaseInt(s2) - getReleaseInt(s1)).findFirst().orElse(null);
	}
	
	/**
	 * Loads the specified file into a little-endian byte buffer.
	 *
	 * @param path
	 *            the file to be loaded
	 * @param direct
	 *            whether the buffer should be loaded directly into memory
	 * @return the loaded little-endian byte buffer
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static ByteBuffer loadLEFile(Path path, boolean direct) throws IOException {
		return loadFile(path, direct).order(ByteOrder.LITTLE_ENDIAN);
	}
	
	/**
	 * Loads the specified file into a byte buffer.
	 *
	 * @param path
	 *            the file to be loaded
	 * @param direct
	 *            whether the buffer should be loaded directly into memory
	 * @return the loaded byte buffer
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static ByteBuffer loadFile(Path path, boolean direct) throws IOException {
		try (SeekableByteChannel ch = Files.newByteChannel(path, StandardOpenOption.READ)) {
			long size = ch.size();
			Validate.require(size < Integer.MAX_VALUE, "File too large", IOException.class);
			ByteBuffer buf = BufferUtil.allocate((int) size, direct);
			ch.read(buf);
			buf.flip();
			return buf;
		}
	}
	
	/**
	 * Saves the specified byte buffer into the specified path.
	 *
	 * @param path
	 *            the path for the data to be saved in.
	 * @param buffer
	 *            the loaded byte buffer
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void saveFile(Path path, ByteBuffer buffer) throws IOException {
		try (SeekableByteChannel ch = Files.newByteChannel(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.SYNC, StandardOpenOption.DSYNC)) {
			if (ch.size() > buffer.remaining())
				ch.truncate(buffer.remaining());
			
			ch.write(buffer);
		}
	}
}
