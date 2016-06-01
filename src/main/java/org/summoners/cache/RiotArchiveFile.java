package org.summoners.cache;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;
import java.util.stream.*;

import org.summoners.cache.structure.*;
import org.summoners.util.*;

import static org.summoners.cache.RiotFileUtil.*;

/**
 * Representation of the Riot Archive File (RAF) indicated by a .raf file extension.
 * @author Riot Games
 * @author Brittan Thomas
 */
public class RiotArchiveFile extends Archive<RiotFile> implements AutoCloseable {
	
	/**
	 * The Constant MAGIC number Riot implemented.
	 */
	private static final int MAGIC = 0x18BE0EF0;
	
	/**
	 * The Constant VERSION number of the RAF format.
	 */
	private static final int VERSION = 1;
	
	/**
	 * The Constant MANAGER_IDX implemented by Riot.
	 */
	private static final int MANAGER_IDX = 0;
	
	/**
	 * Instantiates a new Riot archive file.
	 *
	 * @param path
	 *            the path
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public RiotArchiveFile(String path) throws IOException {
		this.raf = Paths.get(path);
		this.data = Paths.get(path + ".dat");
		this.data.toFile().createNewFile();
        dataFile = new RandomAccessFile(data.toFile(), "rw");
	}
	
	/**
	 * Instantiates a new Riot archive file and reads its data.
	 *
	 * @param raf
	 *            the raf
	 * @param data
	 *            the raf data
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unused")
	public RiotArchiveFile(Path raf, Path data) throws FileNotFoundException, IOException {
		this.raf = raf;
		this.data = data;
		
		ByteBuffer buf = loadLEFile(raf, false);
		Validate.require(buf.getInt() == MAGIC, "Magic number mismatch!", IllegalStateException.class);
		int version = buf.getInt();
		int managerIdx = buf.getInt();
		int fileIndex = buf.getInt();
		int pathIndex = buf.getInt();
		int fileCount = buf.getInt();
		for (int fileIdx = 0; fileIdx != fileCount; ++fileIdx) {
			int hash = buf.getInt();
			long offset = buf.getInt() & 0xFFFFFFFFL;
			int size = buf.getInt();
			int index = buf.getInt();
			files.add(new RiotFile(this, "", size, offset, index, hash));
		}
		
		int offset = buf.position();
		int pathSize = buf.getInt();
		int pathCount = buf.getInt();
		for (int i = 0; i != pathCount; ++i) {
			RiotFile file = files.get(i);
			
			buf.position(offset + 8 + i * 8);
			int po = buf.getInt();
			int pl = buf.getInt();
			buf.position(offset + po);
			
			byte[] name = new byte[pl - 1];
			buf.get(name);
			file.setName(new String(name));
			dictionary.put(file.getName(), file);
		}
        dataFile = new RandomAccessFile(data.toFile(), "rw");
	}
	
	/**
	 * The path of the Riot archive file.
	 */
	private Path raf;

	/**
	 * Gets the path of the Riot archive file.
	 *
	 * @return the path of the Riot archive file
	 */
	public Path getPath() {
		return raf;
	}
	
	/**
	 * The path of the Riot archive file data.
	 */
	private Path data;

	/**
	 * Gets the path of the Riot archive file data.
	 *
	 * @return the path of the Riot archive file data
	 */
	public Path getDataPath() {
		return data;
	}
	
	/**
	 * The indexed Riot archive file data random access file.
	 */
	private final RandomAccessFile dataFile;
	
	/**
	 * Gets the indexed Riot archive file data random access file.
	 *
	 * @return the indexed Riot archive file data random access file
	 */
	public RandomAccessFile getDataFile() {
		return dataFile;
	}
	
	/**
	 * If the archive has changed.
	 */
	private boolean changed = false;
	
	/**
	 * Checks if the archive has changed.
	 *
	 * @return if the archive has changed
	 */
	public boolean isChanged() {
		return changed;
	}
	
	/**
	 * Sets if the archive has changed.
	 *
	 * @param changed
	 *            if the archive has changed
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	
	/**
	 * If this Riot Archive data file is closed.
	 */
	private boolean closed = false;
	
	/**
	 * A map of paths to their corresponding files contained in this archive.
	 */
	private LinkedHashMap<String, RiotFile> dictionary = new LinkedHashMap<>();
	
	/**
	 * Gets the a map of paths to their corresponding files contained in this archive.
	 *
	 * @return the a map of paths to their corresponding files contained in this archive
	 */
	public LinkedHashMap<String, RiotFile> getDictionary() {
		return dictionary;
	}
	
	/**
	 * Gets the Riot file matching the specified path.
	 *
	 * @param path
	 *            the path being queried for.
	 * @return the Riot file
	 */
	public RiotFile get(String path) {
		return dictionary.get(path);
	}
	
	/**
	 * Reads the file at the specified path.
	 *
	 * @param path
	 *            the path of the file to be read.
	 * @return the loaded byte buffer of the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public ByteBuffer read(String path) throws IOException {
		RiotFile file = dictionary.get(path);
		if (file == null)
			throw new FileNotFoundException("\"" + path + "\" was not found in archive " + raf);
		
		return file.getData();
	}
	
	/**
	 * Writes a Riot file to this archive.
	 *
	 * @param path
	 *            the path of the Riot file
	 * @param manifest
	 *            the manifest description of the Riot file
	 * @return the output stream
	 */
	public OutputStream write(String path, RiotFileManifest manifest) throws IOException {
		RiotFile file = new RiotFile(this, path, dataFile.length());
		file.setIndex(0);
		dictionary.put(file.getName(), file);
		files.add(file);
		synchronized (dataFile) {
			dataFile.setLength(dataFile.length() + (manifest.getFileType().isCompressed() ? manifest.getSizeCompressed() : manifest.getSizeUncompressed()));
		}
		return new BufferedOutputStream(new RiotFileOutputStream(this, manifest, file));
	}
	
	/** Checks if the specified file is compressed.
	 *
	 * @param file
	 *            the file being checked
	 * @return true, if the file header indicates compression
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public boolean isCompressed(RiotFile file) throws IOException {
		try {
			ByteBuffer buffer = loadLEFile(data, false);
			buffer.position((int) file.getOffset());
			return buffer.get() == 120 && buffer.get() == -100;
		} catch (FileNotFoundException ex) {
			Logger.getLogger(RiotArchiveFile.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	/**
	 * Synchronizes the changes and writes the updated RAF file.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void sync() throws IOException {
		if (!changed)
			return;
		
		raf.toFile().createNewFile();
		LinkedHashSet<RiotFile> finished = files.stream().filter(f -> f.getSize() != -1)
				.sorted().collect(Collectors.toCollection(() -> new LinkedHashSet<>()));
		int pathSum = finished.stream().mapToInt(f -> f.getName().getBytes().length + 1).sum();
		
		/*
		 * Allocate a buffer with a capacity of the raf file size.
		 */
		ByteBuffer buf = ByteBuffer.allocate(32 + (finished.size() * 24) + pathSum).order(ByteOrder.LITTLE_ENDIAN);
		
		/*
		 * First we must write the file header.
		 */
		
		/* Insert the magic number to prevent corruption. */
		buf.putInt(MAGIC);
		/* Insert the version number of the RAF format. */
		buf.putInt(VERSION);
		/* Insert the manager index. */
		buf.putInt(MANAGER_IDX);
		/* Insert the file list starting offset. */
		buf.putInt(buf.position() + 8);
		/* Insert the path list starting offset. */
		buf.putInt(buf.position() + 4 + (finished.size() * 16));
		
		/*
		 * Start the file list. 
		 */
	
		/* Insert the amount of files to be stored. */
		buf.putInt(finished.size());
		
		/*
		 * Insert each file entry with an auto incrementing index.
		 */
		int pathIdx = 0;
		for (RiotFile file : finished) {
			buf.putInt(file.getHash());
			buf.putInt((int) file.getOffset());
			buf.putInt(file.getSize());
			buf.putInt(pathIdx++);
		}
		
		/*
		 * Start the path list.
		 */
		
		/* Insert the amount of bytes contained in the path list. */
		buf.putInt(pathSum);
		/* Insert the amount of path entries being stored. */
		buf.putInt(finished.size());
		
		/*
		 * Insert the offsets for each file's path data.
		 */
		
		/* Offset is relative to the start of the path list. */
		int offset = 8 + finished.size() * 8;
		for (RiotFile file : finished) {
			buf.putInt(offset);
			offset += file.getNameLength();
			buf.putInt(file.getNameLength());
		}
		
		/* Insert the null-terminated file paths. */
		for (RiotFile file : finished) {
			buf.put(file.getName().getBytes());
			buf.put((byte) 0x00);
		}
		
		changed = false;
	}

	/* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws IOException {
		dataFile.close();
        sync();
        closed = true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Validate.require(closed, "RAF was not closed!", IllegalStateException.class);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		try {
			for (RiotFile file : files)
				builder.append(file.getName().replace("\0", "\\0")).append(" hash=").append(file.getHash())
					.append(" -- ").append(isCompressed(file)).append("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
}
