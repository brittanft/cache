package org.summoners.cache.pkg;

import java.io.*;
import java.util.zip.*;

import org.summoners.cache.*;

/**
 * Representation of an opened, packaged file.
 * @author Riot Games
 * @author Brittan Thomas
 */
public class PackagedData {
	
	/**
	 * Instantiates a new open file.
	 *
	 * @param packageFile
	 *            the package file
	 * @param patcher
	 *            the patcher
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public PackagedData(PackageFile packageFile, RiotArchiveFile archive, String directory) throws IOException {
		this.packageFile = packageFile;
		
		RiotFileManifest manifest = packageFile.getManifest();
		if (manifest.getFileType().isArchive())
			outputStream = archive.write(manifest.getPath() + manifest.getName(), manifest);
		else {
			File targetDir = new File(directory);
			File target = new File(targetDir, manifest.getName());
			targetDir.mkdirs();
			outputStream = new BufferedOutputStream(new FileOutputStream(target));
		}
		
		if (manifest.getFileType().ordinal() > 0 && manifest.getFileType() != RiotFileType.COMPRESSED_ARCHIVE)
			outputStream = new InflaterOutputStream(outputStream);
	}
	
	/**
	 * The packaged file being opened.
	 */
	private PackageFile packageFile;
	
	/**
	 * Gets the packaged file being opened.
	 *
	 * @return the packaged file being opened
	 */
	public PackageFile getPackageFile() {
		return packageFile;
	}
	
	/**
	 * The output stream of the packaged file.
	 */
	private OutputStream outputStream;
	
	/**
	 * Gets the output stream of the packaged file.
	 *
	 * @return the output stream of the packaged file
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}
}
