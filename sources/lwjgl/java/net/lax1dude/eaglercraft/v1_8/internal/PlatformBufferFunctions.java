package net.lax1dude.eaglercraft.v1_8.internal;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;

public class PlatformBufferFunctions {

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
