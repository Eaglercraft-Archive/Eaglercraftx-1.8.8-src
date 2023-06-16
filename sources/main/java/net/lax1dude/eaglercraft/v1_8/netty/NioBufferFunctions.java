package net.lax1dude.eaglercraft.v1_8.netty;

import java.nio.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;

public class NioBufferFunctions {
	
	public static final void get(ByteBuffer src, int index, byte[] dst, int dstOffset, int length) {
		for(int i = 0; i < length; ++i) {
			dst[i + dstOffset] = src.get(i + index);
		}
	}
	
	public static final void put(ByteBuffer dst, int dstIndex, ByteBuffer src, int srcOffset, int length) {
		for(int i = 0; i < length; ++i) {
			dst.put(i + dstIndex, src.get(i + srcOffset));
		}
	}
	
	public static final void put(ByteBuffer dst, int dstIndex, byte[] src, int srcOffset, int length) {
		for(int i = 0; i < length; ++i) {
			dst.put(i + dstIndex, src[i + srcOffset]);
		}
	}

	public static final void get(ByteBuffer src, int index, byte[] dst) {
		get(src, index, dst, 0, dst.length);
	}

	public static void put(ByteBuffer newBuffer, ByteBuffer flip) {
		int len = flip.remaining();
		for(int i = 0; i < len; ++i) {
			newBuffer.put(flip.get());
		}
	}

	public static void put(IntBuffer intBuffer, int index, int[] data) {
		int p = intBuffer.position();
		intBuffer.position(index);
		intBuffer.put(data);
		intBuffer.position(p);
	}
	
}
