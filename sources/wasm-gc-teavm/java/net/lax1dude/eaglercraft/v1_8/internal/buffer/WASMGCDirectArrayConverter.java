package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import org.teavm.interop.Address;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Int16Array;
import org.teavm.jso.typedarrays.Int32Array;
import org.teavm.jso.typedarrays.Int8Array;
import org.teavm.jso.typedarrays.Uint16Array;
import org.teavm.jso.typedarrays.Uint8Array;
import org.teavm.jso.typedarrays.Uint8ClampedArray;

/**
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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
public class WASMGCDirectArrayConverter {

	public static ByteBuffer byteArrayToBuffer(byte[] byteArray) {
		int len = byteArray.length;
		Address ret = WASMGCBufferAllocator.malloc(len);
		WASMGCDirectArrayCopy.memcpy(ret, byteArray, 0, len);
		return new DirectMallocByteBuffer(ret, len, true);
	}

	public static ByteBuffer byteArrayToBuffer(byte[] byteArray, int offset, int length) {
		if(offset < 0) throw Buffer.makeIOOBE(offset);
		if(offset + length > byteArray.length) throw Buffer.makeIOOBE(offset + length - 1);
		Address ret = WASMGCBufferAllocator.malloc(length);
		WASMGCDirectArrayCopy.memcpy(ret, byteArray, offset, length);
		return new DirectMallocByteBuffer(ret, length, true);
	}

	public static ShortBuffer shortArrayToBuffer(short[] shortArray) {
		int len = shortArray.length;
		Address ret = WASMGCBufferAllocator.malloc(len << 1);
		WASMGCDirectArrayCopy.memcpy(ret, shortArray, 0, len);
		return new DirectMallocShortBuffer(ret, len, true);
	}

	public static ShortBuffer shortArrayToBuffer(short[] shortArray, int offset, int length) {
		if(offset < 0) throw Buffer.makeIOOBE(offset);
		if(offset + length > shortArray.length) throw Buffer.makeIOOBE(offset + length - 1);
		Address ret = WASMGCBufferAllocator.malloc(length << 1);
		WASMGCDirectArrayCopy.memcpy(ret, shortArray, offset, length);
		return new DirectMallocShortBuffer(ret, length, true);
	}

	public static IntBuffer intArrayToBuffer(int[] intArray) {
		int len = intArray.length;
		Address ret = WASMGCBufferAllocator.malloc(len << 2);
		WASMGCDirectArrayCopy.memcpy(ret, intArray, 0, len);
		return new DirectMallocIntBuffer(ret, len, true);
	}

	public static IntBuffer intArrayToBuffer(int[] intArray, int offset, int length) {
		if(offset < 0) throw Buffer.makeIOOBE(offset);
		if(offset + length > intArray.length) throw Buffer.makeIOOBE(offset + length - 1);
		Address ret = WASMGCBufferAllocator.malloc(length << 2);
		WASMGCDirectArrayCopy.memcpy(ret, intArray, offset, length);
		return new DirectMallocIntBuffer(ret, length, true);
	}

	public static FloatBuffer floatArrayToBuffer(float[] floatArray) {
		int len = floatArray.length;
		Address ret = WASMGCBufferAllocator.malloc(len << 2);
		WASMGCDirectArrayCopy.memcpy(ret, floatArray, 0, len);
		return new DirectMallocFloatBuffer(ret, len, true);
	}

	public static FloatBuffer floatArrayToBuffer(float[] floatArray, int offset, int length) {
		if(offset < 0) throw Buffer.makeIOOBE(offset);
		if(offset + length > floatArray.length) throw Buffer.makeIOOBE(offset + length - 1);
		Address ret = WASMGCBufferAllocator.malloc(length << 2);
		WASMGCDirectArrayCopy.memcpy(ret, floatArray, offset, length);
		return new DirectMallocFloatBuffer(ret, length, true);
	}

	private static final Uint8Array UINT8ZeroLength = new Uint8Array(0);
	private static final Uint8ClampedArray UINT8CZeroLength = new Uint8ClampedArray(0);
	private static final Int8Array INT8ZeroLength = new Int8Array(0);
	private static final Uint16Array UINT16ZeroLength = new Uint16Array(0);
	private static final Int16Array INT16ZeroLength = new Int16Array(0);
	private static final Int32Array INT32ZeroLength = new Int32Array(0);
	private static final Float32Array FLOAT32ZeroLength = new Float32Array(0);

	public static Uint8Array byteArrayToExternU8Array(byte[] byteArray) {
		int len = byteArray.length;
		if(len == 0) {
			return UINT8ZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len);
		WASMGCDirectArrayCopy.memcpy(addr, byteArray, 0, len);
		Uint8Array ret = new Uint8Array(len);
		ret.set(WASMGCBufferAllocator.getUnsignedByteBufferView0(addr, len));
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static Uint8ClampedArray byteArrayToExternU8CArray(byte[] byteArray) {
		int len = byteArray.length;
		if(len == 0) {
			return UINT8CZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len);
		WASMGCDirectArrayCopy.memcpy(addr, byteArray, 0, len);
		Uint8ClampedArray ret = new Uint8ClampedArray(len);
		ret.set(WASMGCBufferAllocator.getUnsignedClampedByteBufferView0(addr, len));
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static Int8Array byteArrayToExternI8Array(byte[] byteArray) {
		int len = byteArray.length;
		if(len == 0) {
			return INT8ZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len);
		WASMGCDirectArrayCopy.memcpy(addr, byteArray, 0, len);
		Int8Array ret = new Int8Array(len);
		ret.set(WASMGCBufferAllocator.getByteBufferView0(addr, len));
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static Uint16Array byteArrayToExternU16Array(byte[] byteArray) {
		int len = byteArray.length & 0xFFFFFFFE;
		if(len == 0) {
			return UINT16ZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len);
		WASMGCDirectArrayCopy.memcpy(addr, byteArray, 0, len);
		len >>= 1;
		Uint16Array ret = new Uint16Array(len);
		ret.set(WASMGCBufferAllocator.getUnsignedShortBufferView0(addr, len));
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static Int16Array byteArrayToExternI16Array(byte[] byteArray) {
		int len = byteArray.length & 0xFFFFFFFE;
		if(len == 0) {
			return INT16ZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len);
		WASMGCDirectArrayCopy.memcpy(addr, byteArray, 0, len);
		len >>= 1;
		Int16Array ret = new Int16Array(len);
		ret.set(WASMGCBufferAllocator.getShortBufferView0(addr, len));
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static Uint16Array shortArrayToExternU16Array(short[] shortArray) {
		int len = shortArray.length;
		if(len == 0) {
			return UINT16ZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len << 1);
		WASMGCDirectArrayCopy.memcpy(addr, shortArray, 0, len);
		Uint16Array ret = new Uint16Array(len);
		ret.set(WASMGCBufferAllocator.getUnsignedShortBufferView0(addr, len));
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static Int16Array shortArrayToExternI16Array(short[] shortArray) {
		int len = shortArray.length;
		if(len == 0) {
			return INT16ZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len << 1);
		WASMGCDirectArrayCopy.memcpy(addr, shortArray, 0, len);
		Int16Array ret = new Int16Array(len);
		ret.set(WASMGCBufferAllocator.getShortBufferView0(addr, len));
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static Int32Array byteArrayToExternI32Array(byte[] byteArray) {
		int len = byteArray.length & 0xFFFFFFFC;
		if(len == 0) {
			return INT32ZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len);
		WASMGCDirectArrayCopy.memcpy(addr, byteArray, 0, len);
		len >>= 2;
		Int32Array ret = new Int32Array(len);
		ret.set(WASMGCBufferAllocator.getIntBufferView0(addr, len));
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static Int32Array intArrayToExternI32Array(int[] intArray) {
		int len = intArray.length;
		if(len == 0) {
			return INT32ZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len << 2);
		WASMGCDirectArrayCopy.memcpy(addr, intArray, 0, len);
		Int32Array ret = new Int32Array(len);
		ret.set(WASMGCBufferAllocator.getIntBufferView0(addr, len));
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static Float32Array byteArrayToExternF32Array(byte[] byteArray) {
		int len = byteArray.length & 0xFFFFFFFC;
		if(len == 0) {
			return FLOAT32ZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len);
		WASMGCDirectArrayCopy.memcpy(addr, byteArray, 0, len);
		len >>= 2;
		Float32Array ret = new Float32Array(len);
		ret.set(WASMGCBufferAllocator.getFloatBufferView0(addr, len));
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static Float32Array floatArrayToExternF32Array(float[] floatArray) {
		int len = floatArray.length;
		if(len == 0) {
			return FLOAT32ZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len << 2);
		WASMGCDirectArrayCopy.memcpy(addr, floatArray, 0, len);
		Float32Array ret = new Float32Array(len);
		ret.set(WASMGCBufferAllocator.getFloatBufferView0(addr, len));
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	private static final byte[] byteZeroLength = new byte[0];
	private static final short[] shortZeroLength = new short[0];
	private static final int[] intZeroLength = new int[0];
	private static final float[] floatZeroLength = new float[0];

	public static byte[] externU8ArrayToByteArray(Uint8Array U8Array) {
		int len = U8Array.getLength();
		if(len == 0) {
			return byteZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len);
		WASMGCBufferAllocator.getUnsignedByteBufferView0(addr, len).set(U8Array);
		byte[] ret = new byte[len];
		WASMGCDirectArrayCopy.memcpy(ret, 0, addr, len);
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static byte[] externU8CArrayToByteArray(Uint8ClampedArray U8CArray) {
		int len = U8CArray.getLength();
		if(len == 0) {
			return byteZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len);
		WASMGCBufferAllocator.getUnsignedClampedByteBufferView0(addr, len).set(U8CArray);
		byte[] ret = new byte[len];
		WASMGCDirectArrayCopy.memcpy(ret, 0, addr, len);
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static byte[] externI8ArrayToByteArray(Int8Array I8Array) {
		int len = I8Array.getLength();
		if(len == 0) {
			return byteZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len);
		WASMGCBufferAllocator.getByteBufferView0(addr, len).set(I8Array);
		byte[] ret = new byte[len];
		WASMGCDirectArrayCopy.memcpy(ret, 0, addr, len);
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static byte[] externU16ArrayToByteArray(Uint16Array U16Array) {
		int len = U16Array.getLength();
		if(len == 0) {
			return byteZeroLength;
		}
		int len2 = len << 1;
		Address addr = WASMGCBufferAllocator.malloc(len2);
		WASMGCBufferAllocator.getUnsignedShortBufferView0(addr, len).set(U16Array);
		byte[] ret = new byte[len2];
		WASMGCDirectArrayCopy.memcpy(ret, 0, addr, len2);
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static byte[] externI16ArrayToByteArray(Int16Array I16Array) {
		int len = I16Array.getLength();
		if(len == 0) {
			return byteZeroLength;
		}
		int len2 = len << 1;
		Address addr = WASMGCBufferAllocator.malloc(len2);
		WASMGCBufferAllocator.getShortBufferView0(addr, len).set(I16Array);
		byte[] ret = new byte[len2];
		WASMGCDirectArrayCopy.memcpy(ret, 0, addr, len2);
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static short[] externU16ArrayToShortArray(Uint16Array U16Array) {
		int len = U16Array.getLength();
		if(len == 0) {
			return shortZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len << 1);
		WASMGCBufferAllocator.getUnsignedShortBufferView0(addr, len).set(U16Array);
		short[] ret = new short[len];
		WASMGCDirectArrayCopy.memcpy(ret, 0, addr, len);
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static short[] externI16ArrayToShortArray(Int16Array I16Array) {
		int len = I16Array.getLength();
		if(len == 0) {
			return shortZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len << 1);
		WASMGCBufferAllocator.getShortBufferView0(addr, len).set(I16Array);
		short[] ret = new short[len];
		WASMGCDirectArrayCopy.memcpy(ret, 0, addr, len);
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static byte[] externI32ArrayToByteArray(Int32Array I32Array) {
		int len = I32Array.getLength();
		if(len == 0) {
			return byteZeroLength;
		}
		int len2 = len << 2;
		Address addr = WASMGCBufferAllocator.malloc(len2);
		WASMGCBufferAllocator.getIntBufferView0(addr, len).set(I32Array);
		byte[] ret = new byte[len2];
		WASMGCDirectArrayCopy.memcpy(ret, 0, addr, len2);
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static int[] externI32ArrayToIntArray(Int32Array I32Array) {
		int len = I32Array.getLength();
		if(len == 0) {
			return intZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len << 2);
		WASMGCBufferAllocator.getIntBufferView0(addr, len).set(I32Array);
		int[] ret = new int[len];
		WASMGCDirectArrayCopy.memcpy(ret, 0, addr, len);
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static byte[] externF32ArrayToByteArray(Float32Array F32Array) {
		int len = F32Array.getLength();
		if(len == 0) {
			return byteZeroLength;
		}
		int len2 = len << 2;
		Address addr = WASMGCBufferAllocator.malloc(len2);
		WASMGCBufferAllocator.getFloatBufferView0(addr, len).set(F32Array);
		byte[] ret = new byte[len2];
		WASMGCDirectArrayCopy.memcpy(ret, 0, addr, len2);
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

	public static float[] externF32ArrayToFloatArray(Float32Array F32Array) {
		int len = F32Array.getLength();
		if(len == 0) {
			return floatZeroLength;
		}
		Address addr = WASMGCBufferAllocator.malloc(len << 2);
		WASMGCBufferAllocator.getFloatBufferView0(addr, len).set(F32Array);
		float[] ret = new float[len];
		WASMGCDirectArrayCopy.memcpy(ret, 0, addr, len);
		WASMGCBufferAllocator.free(addr);
		return ret;
	}

}
