package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;

import com.jcraft.jzlib.CRC32;
import com.jcraft.jzlib.GZIPInputStream;
import com.jcraft.jzlib.InflaterInputStream;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info) 
 * 
 */
public class EPKLoader {

	public static final void loadEPK(ArrayBuffer epkFile, Map<String, byte[]> loadedFiles) throws IOException {
		loadEPK(epkFile, "", loadedFiles);
	}

	public static final void loadEPK(ArrayBuffer epkFile, String path, Map<String, byte[]> loadedFiles) throws IOException {
		int byteLength = epkFile.getByteLength();
		int l = byteLength - 16;
		if(l < 1) {
			throw new IOException("EPK file is incomplete");
		}
		
		ArrayBufferInputStream is = new ArrayBufferInputStream(epkFile, 0, byteLength - 8);
		
		byte[] header = new byte[8];
		is.read(header);
		String type = readASCII(header);
		
		if(!"EAGPKG$$".equals(type)) {
			throw new IOException("Invalid EPK file type '" + type + "'");
		}
		
		Uint8Array readEndCode = Uint8Array.create(epkFile, byteLength - 8, 8);
		
		byte[] endCode = new byte[] { (byte)':', (byte)':', (byte)':', (byte)'Y',
				(byte)'E', (byte)'E', (byte)':', (byte)'>' };
		for(int i = 0; i < 8; ++i) {
			if(readEndCode.get(i) != endCode[i]) {
				throw new IOException("EPK file is missing EOF code (:::YEE:>)");
			}
		}
		
		String vers = readASCII(is);
		if(!vers.startsWith("ver2.")) {
			throw new IOException("Unknown or invalid EPK version: " + vers);
		}
		
		is.skip(is.read()); // skip filename
		is.skip(loadShort(is)); // skip comment
		is.skip(8); // skip millis date
		
		int numFiles = loadInt(is);
		
		char compressionType = (char)is.read();
		
		InputStream zis;
		switch(compressionType) {
		case 'G':
			zis = new GZIPInputStream(is);
			break;
		case 'Z':
			zis = new InflaterInputStream(is);
			break;
		case '0':
			zis = is;
			break;
		default:
			throw new IOException("Invalid or unsupported EPK compression: " + compressionType);
		}

		int blockFile = ('F' << 24) | ('I' << 16) | ('L' << 8) | 'E';
		int blockEnd = ('E' << 24) | ('N' << 16) | ('D' << 8) | '$';
		int blockHead = ('H' << 24) | ('E' << 16) | ('A' << 8) | 'D';
		
		if(path.length() > 0 && !path.endsWith("/")) {
			path = path + "/";
		}
		
		CRC32 crc32 = new CRC32();
		int blockType;
		for(int i = 0; i < numFiles; ++i) {
			
			blockType = loadInt(zis);
			
			if(blockType == blockEnd) {
				throw new IOException("Unexpected END when there are still " + (numFiles - i) + " files remaining");
			}
			
			String name = readASCII(zis);
			int len = loadInt(zis);
			
			if(i == 0) {
				if(blockType == blockHead) {
					byte[] readType = new byte[len];
					zis.read(readType);
					if(!"file-type".equals(name) || !"epk/resources".equals(readASCII(readType))) {
						throw new IOException("EPK is not of file-type 'epk/resources'!");
					}
					if(zis.read() != '>') {
						throw new IOException("Object '" + name + "' is incomplete");
					}
					continue;
				}else {
					throw new IOException("File '" + name + "' did not have a file-type block as the first entry in the file");
				}
			}
			
			if(blockType == blockFile) {
				if(len < 5) {
					throw new IOException("File '" + name + "' is incomplete");
				}
				
				int expectedCRC = loadInt(zis);
				
				byte[] load = new byte[len - 5];
				zis.read(load);

				if(len > 5) {
					crc32.reset();
					crc32.update(load, 0, load.length);
					if(expectedCRC != (int)crc32.getValue()) {
						throw new IOException("File '" + name + "' has an invalid checksum");
					}
				}
				
				if(zis.read() != ':') {
					throw new IOException("File '" + name + "' is incomplete");
				}
				
				loadedFiles.put(path + name, load);
			}else {
				zis.skip(len);
			}

			if(zis.read() != '>') {
				throw new IOException("Object '" + name + "' is incomplete");
			}
		}
		
		if(loadInt(zis) != blockEnd) {
			throw new IOException("EPK missing END$ object");
		}
		
		zis.close();
	}
	
	private static final int loadShort(InputStream is) throws IOException {
		return (is.read() << 8) | is.read();
	}
	
	private static final int loadInt(InputStream is) throws IOException {
		return (is.read() << 24) | (is.read() << 16) | (is.read() << 8) | is.read();
	}
	
	private static final String readASCII(byte[] bytesIn) throws IOException {
		char[] charIn = new char[bytesIn.length];
		for(int i = 0; i < bytesIn.length; ++i) {
			charIn[i] = (char)((int)bytesIn[i] & 0xFF);
		}
		return new String(charIn);
	}
	
	private static final String readASCII(InputStream bytesIn) throws IOException {
		int len = bytesIn.read();
		char[] charIn = new char[len];
		for(int i = 0; i < len; ++i) {
			charIn[i] = (char)(bytesIn.read() & 0xFF);
		}
		return new String(charIn);
	}

}
