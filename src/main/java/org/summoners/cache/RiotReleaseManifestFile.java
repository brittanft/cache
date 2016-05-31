package org.summoners.cache;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;

/**
 * Representation of the Riot Release Manifest File (RRMF).
 * @author Riot Games
 * @author Brittan Thomas
 */
public class RiotReleaseManifestFile {
	
	/**
	 * The magic number read from this file.
	 */
	private final int magic;

	/**
	 * Gets the magic number read from this file.
	 *
	 * @return the magic number read from this file
	 */
	public int getMagic() {
		return magic;
	}
	
	/**
	 * The file type of this file.
	 */
	private final int fileType;
	
	/**
	 * Gets the file type of this file.
	 *
	 * @return the file type of this file
	 */
	public int getFileType() {
		return fileType;
	}
	
	/**
	 * The amount of items described in this file.
	 */
	private final int itemCount;

	/**
	 * Gets the amount of items described in this file.
	 *
	 * @return the amount of items described in this file
	 */
	public int getItemCount() {
		return itemCount;
	}
	
	/**
	 * The release version.
	 */
	private final int releaseVersion;
	
	/**
	 * Gets the release version.
	 *
	 * @return the release version
	 */
	public int getReleaseVersion() {
		return releaseVersion;
	}
	
	/**
	 * The release name.
	 */
	private final String releaseName;
	
	/**
	 * Gets the release name.
	 *
	 * @return the release name
	 */
	public String getReleaseName() {
		return releaseName;
	}
	
	/**
	 * The directories.
	 */
	private RiotDirectory[] directories;
	
	/**
	 * Gets the directories.
	 *
	 * @return the directories
	 */
	public RiotDirectory[] getDirectories() {
		return directories;
	}
	
	public RiotDirectory getDirectory(String path) {
		for (RiotDirectory directory : directories)
			if (directory.getPath().equals(path))
				return directory;
		
		return null;
	}
	
	/**
	 * The manifest files.
	 */
	private RiotFileManifest[] files;
	
	/**
	 * Gets the manifest files.
	 *
	 * @return the manifest files
	 */
	public RiotFileManifest[] getFiles() {
		return files;
	}
	
	/**
	 * The path to manifest file hashmap.
	 */
	private final LinkedHashMap<String, RiotFileManifest> dictionary = new LinkedHashMap<>();

	/**
	 * Gets the manifest file from the specified path.
	 *
	 * @param path
	 *            the path of the manifest file
	 * @return the specified manifest file
	 */
	public RiotFileManifest getFile(String path) {
		return dictionary.get(path);
	}
	
	/**
	 * Instantiates a new Riot release manifest file.
	 *
	 * @param file
	 *            the Riot release manifest file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public RiotReleaseManifestFile(Path file) throws IOException {
		ByteBuffer buf = RiotFileUtil.loadLEFile(file, false);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		magic = buf.getInt();
		System.out.println("Magic: " + magic);
		fileType = buf.getInt();
		System.out.println("File Type: " + fileType);
		itemCount = buf.getInt();
		System.out.println("Item Count: " + itemCount);
		releaseVersion = buf.getInt();
		releaseName = RiotFileUtil.getReleaseName(releaseVersion);
		System.out.println("Release Version: " + releaseVersion + " (" + releaseName + ")");
		
		RiotDirectoryMetadata[] directoryMetadata = new RiotDirectoryMetadata[buf.getInt()];
		for (int directoryIdx = 0; directoryIdx != directoryMetadata.length; ++directoryIdx)
			directoryMetadata[directoryIdx] = new RiotDirectoryMetadata(buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt());
		
		files = new RiotFileManifest[buf.getInt()];
		for (int metadataIdx = 0; metadataIdx != files.length; ++metadataIdx) {
			int nameIndex = buf.getInt(), release = buf.getInt();
			byte[] checksum = new byte[16];
			buf.get(checksum);
			files[metadataIdx] = new RiotFileManifest(RiotFileUtil.getReleaseName(release), release, nameIndex, checksum, 
								buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt());
		}
		
		/*
		 * Begin parsing the names of the directories and files.
		 */
		
		/* Initialize a cache of x names. */
		String[] names = new String[buf.getInt()];
		/* Read the size of the remaining data. */
		buf.getInt();
		
		int nameIndex = 0;
		StringBuilder builder = new StringBuilder();
		while (buf.hasRemaining()) {
			int c = buf.get();
			if (c == '\0') {
				names[nameIndex++] = builder.toString();
				builder = new StringBuilder();
			} else
				builder.append((char) c);
		}
		
		directories = new RiotDirectory[directoryMetadata.length];
		for (int index = 0; index != directoryMetadata.length; ++index)
			directories[index] = new RiotDirectory(names[directoryMetadata[index].getNameIndex()]);
		
		for (int index = 0; index != directories.length; ++index) {
			RiotDirectory directory = directories[index];
			RiotDirectoryMetadata metadata = directoryMetadata[index];
			
			int start = metadata.getDirectoryIndex();
			if (start == index)
				start++;
			
			directory.setSubdirs(new RiotDirectory[metadata.getDirectoryCount()]);
			for (int subIndex = 0; subIndex != metadata.getDirectoryCount(); ++subIndex) {
				RiotDirectory subDirectory = directories[start + subIndex];
				directory.getSubdirs()[subIndex] = subDirectory;
				subDirectory.setPath(directory.getPath() + subDirectory.getPath());
			}
		}
		
		for (RiotFileManifest manifestFile : files)
			manifestFile.setName(names[manifestFile.getNameIndex()]);
		
		for (int index = 0; index != directories.length; ++index) {
			RiotDirectory directory = directories[index];
			RiotDirectoryMetadata metadata = directoryMetadata[index];
			
			int start = metadata.getFileIndex();
			directory.setFiles(new RiotFileManifest[metadata.getFileCount()]);
			
			for (int fileIndex = 0; fileIndex != metadata.getFileCount(); ++fileIndex) {
				directory.getFiles()[fileIndex] = files[start + fileIndex];
				files[start + fileIndex].setPath(directory.getPath());
			}
		}
		
		for (RiotFileManifest manifestFile : files)
			dictionary.put(manifestFile.getPath() + manifestFile.getName(), manifestFile);
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(RiotFileManifest manifestFile : files)
            sb.append(manifestFile.getPath()).append(manifestFile.getName()).append(" type:").append(manifestFile.getFileType()).append(" ")
            		.append(manifestFile.getRelease()).append("\n");
        
        return sb.toString();
    }
	
	/**
	 * Downloads the release manifest file from a URL.
	 *
	 * @param branch
	 *            the branch type
	 * @param type
	 *            the type (ex: downloads, projects, solutions, etc)
	 * @param component
	 *            the component type (ex: live, lol_air_client, lol_game_client, etc)
	 * @param version
	 *            the version name (ex: 0.0.1.91)
	 * @return the release manifest file instance
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static RiotReleaseManifestFile download(String branch, String type, String component, String version) throws IOException {
		URL url = new URL("http://l3cdn.riotgames.com/releases/" + branch + "/" + type + "/" + component + "/releases/" + version + "/releasemanifest");
		URLConnection connection = url.openConnection();
		File file = RiotFileUtil.getRADSFile(type + "/" + component + "/releases/" + version + "/releasemanifest");
		new File(file.getParent()).mkdirs();
		file.createNewFile();
		try (InputStream in = connection.getInputStream()) {
			try (OutputStream fo = new FileOutputStream(file)) {
				int read;
				byte[] buffer = new byte[2048];
				while ((read = in.read(buffer)) != -1) {
					fo.write(buffer, 0, read);
				}
			}
		}
		return new RiotReleaseManifestFile(file.toPath());
	}
}
