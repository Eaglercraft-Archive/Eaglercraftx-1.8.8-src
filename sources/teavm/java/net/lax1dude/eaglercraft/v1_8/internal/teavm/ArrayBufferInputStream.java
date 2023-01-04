package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.io.InputStream;

import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;

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
public class ArrayBufferInputStream extends InputStream {
	
	private int position;
	private int limit;
	private final ArrayBuffer buffer;
	private final Uint8Array typed;
	
	public ArrayBufferInputStream(ArrayBuffer bufferIn) {
		this(bufferIn, 0, bufferIn.getByteLength());
	}
	
	public ArrayBufferInputStream(ArrayBuffer bufferIn, int off, int len) {
		if(off + len > bufferIn.getByteLength()) {
			throw new IllegalArgumentException("offset " + off + " and length " + len + " are out of bounds for a "
					+ bufferIn.getByteLength() + " long arraybuffer");
		}
		buffer = bufferIn;
		typed = Uint8Array.create(bufferIn);
		position = off;
		limit = off + len;
	}

	@Override
	public int read() {
		if(position >= limit) {
			return -1;
		}
		return typed.get(position++);
	}

	@Override
	public int read(byte b[], int off, int len) {
		if(off + len > b.length) {
			throw new ArrayIndexOutOfBoundsException("offset " + off + " and length " + len
					+ " are out of bounds for a " + b.length + " array");
		}
		
		int avail = limit - position;
		if(len > avail) {
			len = avail;
		}
		
		if(len <= 0) {
			return -1;
		}
		
		for(int i = 0; i < len; ++i) {
			b[off + i] = (byte)typed.get(position + i);
		}
		
		position += len;
		
		return len;
	}
	
	public long skip(long n) {
		int avail = limit - position;
		if(n > avail) {
			n = avail;
		}
		position += (int)n;
		return n;
	}
	
	@Override
	public int available() {
		return limit - position;
	}

	public int getPosition() {
		return position;
	}

	public int getLimit() {
		return limit;
	}

	public ArrayBuffer getBuffer() {
		return buffer;
	}

}
