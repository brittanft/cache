package org.summoners.cache.data;

import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.function.*;

import org.summoners.cache.*;
import org.summoners.cache.data.hash.*;
import org.summoners.util.*;

public class RiotInibin {
	
	private RiotFile file;
	
	public RiotFile getFile() {
		return file;
	}
	
	public String getDirectory() {
		return file.getName().substring(0, file.getName().lastIndexOf('/') + 1);
	}
	
	public int version;
	public int stringTableLength;
	public int bitMask;
	
	private HashMap<Long, Object> keys = new HashMap<>();
	
	public HashMap<Long, Object> getKeys() {
		return keys;
	}
	
	public boolean contains(Long key) {
		return keys.containsKey(key);
	}
	
	public Object get(Long key) {
		return keys.get(key);
	}
	
	@SuppressWarnings("unused")
	public RiotInibin(RiotFile file, ByteBuffer buf) throws IOException {
		this.file = file;
		
		version = buf.get();
		buf.order(ByteOrder.LITTLE_ENDIAN);
		Validate.require(version == 2, () -> "Unknown .inibin version: " + version, IOException.class);
		stringTableLength = buf.getShort() & 0xFFFF;
		bitMask = buf.getShort();
		System.out.println("Bit string: " + Integer.toBinaryString(bitMask) + ", " + stringTableLength + ", " + bitMask);
		System.out.println("Remaining: " + buf.remaining());
		
		LinkedList<Boolean> bits = new LinkedList<>();
		for (int i = 0; i < 16; ++i) {
			boolean e = ((bitMask >>> i) & 1) != 0;
			bits.add(e);
		}
		
		System.out.println(bits);
		
		Supplier<LinkedList<Long>> run = () -> {
			LinkedList<Long> keyList = new LinkedList<>();
			int count = buf.getShort() & 0xFFFF;
			for (int i = 0; i != count; ++i) {
				long key = buf.getInt() & 0xFFFFFFFFL;
				keyList.add(key);
			}
			return keyList;
		};

		int step = -1;
		step++;
		if (bits.pop()) { // 0x0001 0
			System.out.println("Step: " + step);
			for (Long key : run.get())
				keys.put(key, buf.getInt() & 0xFFFFFFFFL);
		}

		step++;
		if (bits.pop()) { // 0x0002 1
			System.out.println("Step: " + step);
			for (Long key : run.get())
				keys.put(key, buf.getFloat());
		}

		step++;
		if (bits.pop()) { // 0x0004 2
			System.out.println("Step: " + step);
			for (Long key : run.get()) {
				keys.put(key, buf.get() / 10);
			}
		}

		step++;
		if (bits.pop()) { // 0x0008 3
			System.out.println("Step: " + step);
			for (Long key : run.get())
				keys.put(key, buf.getShort() & 0xFFFF);
		}

		step++;
		if (bits.pop()) { // 0x0010 4
			System.out.println("Step: " + step);
			for (Long key : run.get())
				keys.put(key, 0xFF & buf.get());
		}

		byte boolByte = (byte) 0;
		
		step++;
		if (bits.pop()) { // 0x0020 5
			System.out.println("Step: " + step);
			LinkedList<Long> list = run.get();
			for (int i = 0; i != list.size(); ++i) {
				long key = list.get(i);
				boolByte = (byte) (i % 8 == 0 ? buf.get() : boolByte >> 1);
				keys.put(key, 0x1 & boolByte);
			}
		}

		step++;
		if (bits.pop()) { // 0x0040 6
			System.out.println("Step: " + step);
			for (Long key : run.get()) {
				keys.put(key, buf.get() + " " + buf.get() + " " + buf.get());
			}
		}

		step++;
		if (bits.pop()) { // 0x0080 7
			System.out.println("Step: " + step);
			for (Long key : run.get()) {
				keys.put(key, buf.getFloat() + " " + buf.getFloat() + " " + buf.getFloat());
			}
		}

		step++;
		if (bits.pop()) { // 0x0100 8
			System.out.println("Step: " + step);
			for (Long key : run.get()) {
				keys.put(key, (buf.get() & 0xFF / 10) + " " + (buf.get() & 0xFF / 10));
			}
		}

		step++;
		if (bits.pop()) { // 0x0200 9
			System.out.println("Step: " + step);
			for (Long key : run.get()) {
				keys.put(key, buf.getFloat() + " " + buf.getFloat());
			}
		}

		step++;
		if (bits.pop()) { // 0x0400 10
			System.out.println("Step: " + step);
			for (Long key : run.get()) {
				keys.put(key, buf.get() / 10 + " " + buf.get() / 10 + " " + buf.get() / 10 + " " + buf.get() / 10);
			}
		}

		step++;
		if (bits.pop()) { // 0x0800 11
			System.out.println("Step: " + step);
			for (Long key : run.get()) {
				String string = new String(buf.getFloat() + " " + buf.getFloat() + " " + buf.getFloat() + " " + buf.getFloat());
				System.out.println(string);
				keys.put(key, string);
			}
		}

		step++;
		if (bits.pop()) { // 0x1000 12
			System.out.println("Step: " + step);
			LinkedList<Long> list = run.get();
			LinkedList<Integer> offsets = new LinkedList<>();
			
			for (int i = 0; i != list.size(); ++i)
				offsets.add(buf.getShort() & 0xFFFF);
			
			byte[] buffer = new byte[stringTableLength];
			buf.get(buffer);
			
			for (int i = 0; i != list.size(); ++i)
				keys.put(list.get(i), scanForString(buffer, offsets.get(i)));
		}

		step++; 
		if (bits.pop()) { // 0x2000 13
			System.out.println("Step: " + step);
			for (Long key : run.get()) {
			}
		}

		step++;
		if (bits.pop()) { // 0x4000 14
			System.out.println("Step: " + step);
			for (Long key : run.get()) {
			}
		}

		step++;
		if (bits.pop()) { // 0x8000 15
			System.out.println("Step: " + step);
			for (Long key : run.get()) {
			}
		}
		
		System.out.println("Remaining: " + buf.remaining());
		
		int remain = buf.remaining();
		byte[] remaining = new byte[buf.remaining()];
		buf.get(remaining);
		System.out.println("Leftover: " + new String(remaining, 0, remain));
		
		keys.forEach((k, v) -> {
			AttributeMapping mapping = AttributeMapping.MAPPINGS.get(k);
			System.out.println((mapping == null ? k : "[" + mapping.getCategory() + "] " + mapping.getAttribute()) + ": " + v);
		});
	}
	
	/**
	 * Scan a byte buffer for a US-ASCII-encoded string.
	 * 
	 * @param buffer
	 *            The buffer to scan.
	 * @param start
	 *            The position in the buffer to start scanning.
	 * @return The scanned string.
	 */
	private static String scanForString(byte[] buffer, int start) {
		StringBuilder sb = new StringBuilder();
		if (start >= buffer.length || start < 0)
			throw new IllegalArgumentException("start out of range");
		while (buffer[start] != 0)
			sb.append((char) buffer[start++]);
		return sb.toString();
	}
}
