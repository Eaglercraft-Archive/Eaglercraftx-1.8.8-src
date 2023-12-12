package net.lax1dude.eaglercraft.v1_8.sp.server.export;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.zip.CRC32;

import net.lax1dude.eaglercraft.v1_8.EaglerZLIB;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class EPKDecompiler {

	public static class FileEntry {
		public final String type;
		public final String name;
		public final byte[] data;
		protected FileEntry(String type, String name, byte[] data) {
			this.type = type;
			this.name = name;
			this.data = data;
		}
	}
	
	private ByteArrayInputStream in2;
	private InputStream zis;
	private CRC32 crc32;
	private int numFiles;
	private boolean isFinished = false;
	
	public EPKDecompiler(byte[] data) throws IOException {
		in2 = new ByteArrayInputStream(data);
		
		byte[] header = new byte[8];
		in2.read(header);
		
		if(Arrays.equals(header, new byte[]{(byte)69,(byte)65,(byte)71,(byte)80,(byte)75,(byte)71,(byte)36,(byte)36})) {
			byte[] endCode = new byte[] { (byte)':', (byte)':', (byte)':', (byte)'Y',
					(byte)'E', (byte)'E', (byte)':', (byte)'>' };
			for(int i = 0; i < 8; ++i) {
				if(data[data.length - 8 + i] != endCode[i]) {
					throw new IOException("EPK file is missing EOF code (:::YEE:>)");
				}
			}
			in2 = new ByteArrayInputStream(data, 8, data.length - 16);
			InputStream is = in2;
			
			String vers = readASCII(is);
			if(!vers.startsWith("ver2.")) {
				throw new IOException("Unknown or invalid EPK version: " + vers);
			}
			
			is.skip(is.read()); // skip filename
			is.skip(loadShort(is)); // skip comment
			is.skip(8); // skip millis date
			
			numFiles = loadInt(is);
			
			char compressionType = (char)is.read();
			
			switch(compressionType) {
			case 'G':
				zis = EaglerZLIB.newGZIPInputStream(is);
				break;
			case 'Z':
				zis = EaglerZLIB.newInflaterInputStream(is);
				break;
			case '0':
				zis = is;
				break;
			default:
				throw new IOException("Invalid or unsupported EPK compression: " + compressionType);
			}
			
			crc32 = new CRC32();
		}else if(Arrays.equals(header, new byte[]{(byte)69,(byte)65,(byte)71,(byte)80,(byte)75,(byte)71,(byte)33,(byte)33})) {
			throw new IOException("FILE IS AN UNSUPPORTED LEGACY FORMAT!");
		}else {
			throw new IOException("FILE IS NOT AN EPK FILE!");
		}
		
	}

	public FileEntry readFile() throws IOException {
		if(isFinished) {
			return null;
		}
		
		byte[] typeBytes = new byte[4];
		zis.read(typeBytes);
		String type = readASCII(typeBytes);
		
		if(numFiles == 0) {
			if(!"END$".equals(type)) {
				throw new IOException("EPK file is missing END code (END$)");
			}
			isFinished = true;
			return null;
		}else {
			if("END$".equals(type)) {
				throw new IOException("Unexpected END when there are still " + numFiles + " files remaining");
			}else {
				String name = readASCII(zis);
				int len = loadInt(zis);
				byte[] data;
				
				if("FILE".equals(type)) {
					if(len < 5) {
						throw new IOException("File '" + name + "' is incomplete (no crc)");
					}
					
					int loadedCrc = loadInt(zis);
					
					data = new byte[len - 5];
					zis.read(data);
					
					crc32.reset();
					crc32.update(data, 0, data.length);
					if((int)crc32.getValue() != loadedCrc) {
						throw new IOException("File '" + name + "' has an invalid checksum");
					}
					
					if(zis.read() != ':') {
						throw new IOException("File '" + name + "' is incomplete");
					}
				}else {
					data = new byte[len];
					zis.read(data);
				}
				
				if(zis.read() != '>') {
					throw new IOException("Object '" + name + "' is incomplete");
				}
				
				--numFiles;
				return new FileEntry(type, name, data);
			}
		}
	}
	
	private static final int loadShort(InputStream is) throws IOException {
		return (is.read() << 8) | is.read();
	}
	
	private static final int loadInt(InputStream is) throws IOException {
		return (is.read() << 24) | (is.read() << 16) | (is.read() << 8) | is.read();
	}
	
	public static final String readASCII(byte[] bytesIn) throws IOException {
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
