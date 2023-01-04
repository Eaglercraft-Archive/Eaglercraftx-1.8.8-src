package net.lax1dude.eaglercraft.v1_8.internal.buffer;

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
public class EaglerBufferInputStream extends InputStream {
	
	private final ByteBuffer buffer;
	
	public EaglerBufferInputStream(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public int read() throws IOException {
		if(buffer.remaining() <= 0) {
			return -1;
		}
		return (int)buffer.get() & 0xFF;
	}

	@Override
	public int read(byte b[], int off, int len) throws IOException {
		int p = buffer.position();
		int l = buffer.limit();
		int r = l - p;
		if(r < len) {
			len = r;
		}
		if(len > 0) {
			buffer.get(b, off, len);
		}
		return len;
	}

	@Override
	public long skip(long n) throws IOException {
		int p = buffer.position();
		int l = buffer.limit();
		int r = l - p;
		if(r < n) {
			n = r;
		}
		if(n > 0) {
			buffer.position(p + (int)n);
		}
		return n;
	}

	@Override
	public int available() throws IOException {
		return buffer.remaining();
	}

}
