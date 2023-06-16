package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.DataView;
import org.teavm.jso.typedarrays.Float32Array;
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
public class EaglerArrayBufferAllocator {
	
	public static class WrongBufferClassType extends RuntimeException {
		public WrongBufferClassType(String msg) {
			super(msg);
		}
	}

	public static ByteBuffer allocateByteBuffer(int size) {
		return new EaglerArrayByteBuffer(DataView.create(ArrayBuffer.create(size)));
	}

	public static IntBuffer allocateIntBuffer(int size) {
		return new EaglerArrayIntBuffer(DataView.create(ArrayBuffer.create(size << 2)));
	}

	public static FloatBuffer allocateFloatBuffer(int size) {
		return new EaglerArrayFloatBuffer(DataView.create(ArrayBuffer.create(size << 2)));
	}
	
	public static DataView getDataView(ByteBuffer buffer) {
		if(buffer instanceof EaglerArrayByteBuffer) {
			EaglerArrayByteBuffer b = (EaglerArrayByteBuffer)buffer;
			DataView d = b.dataView;
			int p = b.position;
			int l = b.limit;
			if(p == 0 && l == b.capacity) {
				return d;
			}else {
				int i = d.getByteOffset();
				return DataView.create(d.getBuffer(), i + p, l - p);
			}
		}else {
			throw notEagler(buffer);
		}
	}
	
	public static Uint8Array getDataViewStupid(ByteBuffer buffer) {
		if(buffer instanceof EaglerArrayByteBuffer) {
			EaglerArrayByteBuffer b = (EaglerArrayByteBuffer)buffer;
			DataView d = b.dataView;
			int p = b.position;
			int l = b.limit;
			int i = d.getByteOffset();
			return Uint8Array.create(d.getBuffer(), i + p, l - p);
		}else {
			throw notEagler(buffer);
		}
	}
	
	public static DataView getDataView(IntBuffer buffer) {
		if(buffer instanceof EaglerArrayIntBuffer) {
			EaglerArrayIntBuffer b = (EaglerArrayIntBuffer)buffer;
			DataView d = b.dataView;
			int p = b.position;
			int l = b.limit;
			if(p == 0 && l == b.capacity) {
				return d;
			}else {
				int i = d.getByteOffset();
				return DataView.create(d.getBuffer(), i + (p << 2), (l - p) << 2);
			}
		}else {
			throw notEagler(buffer);
		}
	}
	
	public static Uint8Array getDataViewStupid(IntBuffer buffer) {
		if(buffer instanceof EaglerArrayIntBuffer) {
			EaglerArrayIntBuffer b = (EaglerArrayIntBuffer)buffer;
			DataView d = b.dataView;
			int p = b.position;
			int l = b.limit;
			int i = d.getByteOffset();
			return Uint8Array.create(d.getBuffer(), i + (p << 2), (l - p) << 2);
		}else {
			throw notEagler(buffer);
		}
	}
	
	public static DataView getDataView(FloatBuffer buffer) {
		if(buffer instanceof EaglerArrayFloatBuffer) {
			EaglerArrayFloatBuffer b = (EaglerArrayFloatBuffer)buffer;
			DataView d = b.dataView;
			int p = b.position;
			int l = b.limit;
			if(p == 0 && l == b.capacity) {
				return d;
			}else {
				int i = d.getByteOffset();
				return DataView.create(d.getBuffer(), i + (p << 2), (l - p) << 2);
			}
		}else {
			throw notEagler(buffer);
		}
	}
	
	public static Uint8Array getDataViewStupid(FloatBuffer buffer) {
		if(buffer instanceof EaglerArrayFloatBuffer) {
			EaglerArrayFloatBuffer b = (EaglerArrayFloatBuffer)buffer;
			DataView d = b.dataView;
			int p = b.position;
			int l = b.limit;
			int i = d.getByteOffset();
			return Uint8Array.create(d.getBuffer(), i + (p << 2), (l - p) << 2);
		}else {
			throw notEagler(buffer);
		}
	}
	
	public static Float32Array getFloatArrayStupid(FloatBuffer buffer) {
		if(buffer instanceof EaglerArrayFloatBuffer) {
			EaglerArrayFloatBuffer b = (EaglerArrayFloatBuffer)buffer;
			DataView d = b.dataView;
			int p = b.position;
			int l = b.limit;
			int i = d.getByteOffset();
			return Float32Array.create(d.getBuffer(), i + p, l - p);
		}else {
			throw notEagler(buffer);
		}
	}
	
	private static WrongBufferClassType notEagler(Object clazz) {
		return new WrongBufferClassType("Tried to pass a " + clazz.getClass().getSimpleName() + " which is not a native eagler buffer");
	}

}
