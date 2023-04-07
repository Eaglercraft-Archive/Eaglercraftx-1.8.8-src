package net.lax1dude.eaglercraft.v1_8;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
public class EaglerInputStream extends ByteArrayInputStream {

	public EaglerInputStream(byte[] buf) {
		super(buf);
	}

	public EaglerInputStream(byte[] buf, int off, int len) {
		super(buf, off, len);
	}
	
	public static byte[] inputStreamToBytesQuiet(InputStream is) {
		if(is == null) {
			return null;
		}
		try {
			return inputStreamToBytes(is);
		}catch(IOException ex) {
			return null;
		}
	}
	
	public static byte[] inputStreamToBytes(InputStream is) throws IOException {
		if(is instanceof EaglerInputStream) {
			return ((EaglerInputStream) is).getAsArray();
		}else if(is instanceof ByteArrayInputStream) {
			byte[] ret = new byte[is.available()];
			is.read(ret);
			return ret;
		}else {
			ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
			byte[] buf = new byte[1024];
			int i;
			while((i = is.read(buf)) != -1) {
				os.write(buf, 0, i);
			}
			return os.toByteArray();
		}
	}

	public byte[] getAsArray() {
		if(pos == 0 && count == buf.length) {
			return buf;
		}else {
			byte[] ret = new byte[count];
			System.arraycopy(buf, pos, ret, 0, count);
			return ret;
		}
	}
	
	public boolean canUseArrayDirectly() {
		return pos == 0 && count == buf.length;
	}
	
	public int getPosition() {
		return pos;
	}
	
	public int getMark() {
		return mark;
	}
	
	public int getCount() {
		return count;
	}

}
