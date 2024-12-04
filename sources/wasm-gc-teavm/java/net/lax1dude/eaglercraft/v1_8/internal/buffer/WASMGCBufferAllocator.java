package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import org.teavm.interop.Address;
import org.teavm.interop.DirectMalloc;
import org.teavm.interop.Import;
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
public class WASMGCBufferAllocator {

	private static final boolean enableBufferOverflowCheck = false;

	public static Address malloc(int size) {
		if(size == 0) {
			throw new IllegalArgumentException("Tried to allocate 0 bytes!");
		}
		Address addr;
		if(enableBufferOverflowCheck) {
			addr = DirectMalloc.malloc(size + 12);
			if(addr.toInt() == 0) {
				throw new OutOfMemoryError("DirectMalloc returned null pointer!");
			}
			int tag = (int)(Math.random() * 2147483647.0);
			addr.putInt(size);
			addr.add(4).putInt(tag);
			addr.add(size + 8).putInt(tag);
			addr = addr.add(8);
		}else {
			addr = DirectMalloc.malloc(size);
			if(addr.toInt() == 0) {
				throw new OutOfMemoryError("DirectMalloc returned null pointer!");
			}
		}
		return addr;
	}

	public static Address calloc(int size) {
		Address addr;
		if(enableBufferOverflowCheck) {
			if(size == 0) {
				throw new OutOfMemoryError("Tried to allocate 0 bytes!");
			}
			addr = DirectMalloc.calloc(size + 12);
			if(addr.toInt() == 0) {
				throw new OutOfMemoryError("DirectMalloc returned null pointer!");
			}
			int tag = (int)(Math.random() * 2147483647.0);
			addr.putInt(size);
			addr.add(4).putInt(tag);
			addr.add(size + 8).putInt(tag);
			addr = addr.add(8);
		}else {
			addr = DirectMalloc.calloc(size);
			if(addr.toInt() == 0) {
				throw new OutOfMemoryError("DirectMalloc returned null pointer!");
			}
		}
		return addr;
	}

	public static void free(Address ptr) {
		if(ptr.toInt() != 0) {
			if(enableBufferOverflowCheck) {
				ptr = ptr.add(-8);
				int size = ptr.getInt();
				int tag = ptr.add(4).getInt();
				if(tag != ptr.add(size + 8).getInt()) {
					throw new RuntimeException("Detected a buffer write overflow");
				}
				DirectMalloc.free(ptr);
			}else {
				DirectMalloc.free(ptr);
			}
		}
	}

	public static ByteBuffer allocateByteBuffer(int size) {
		return new DirectMallocByteBuffer(malloc(size), size, true);
	}

	public static ShortBuffer allocateShortBuffer(int size) {
		return new DirectMallocShortBuffer(malloc(size << 1), size, true);
	}

	public static IntBuffer allocateIntBuffer(int size) {
		return new DirectMallocIntBuffer(malloc(size << 2), size, true);
	}

	public static FloatBuffer allocateFloatBuffer(int size) {
		return new DirectMallocFloatBuffer(malloc(size << 2), size, true);
	}

	public static void freeByteBuffer(ByteBuffer buffer) {
		DirectMallocByteBuffer buf = (DirectMallocByteBuffer)buffer;
		if(buf.original) {
			WASMGCBufferAllocator.free(buf.address);
		}else {
			throwNotOriginal(buf);
		}
	}

	public static void freeShortBuffer(ShortBuffer buffer) {
		DirectMallocShortBuffer buf = (DirectMallocShortBuffer)buffer;
		if(buf.original) {
			WASMGCBufferAllocator.free(buf.address);
		}else {
			throwNotOriginal(buf);
		}
	}

	public static void freeIntBuffer(IntBuffer buffer) {
		DirectMallocIntBuffer buf = (DirectMallocIntBuffer)buffer;
		if(buf.original) {
			WASMGCBufferAllocator.free(buf.address);
		}else {
			throwNotOriginal(buf);
		}
	}

	public static void freeFloatBuffer(FloatBuffer buffer) {
		DirectMallocFloatBuffer buf = (DirectMallocFloatBuffer)buffer;
		if(buf.original) {
			WASMGCBufferAllocator.free(buf.address);
		}else {
			throwNotOriginal(buf);
		}
	}

	public static Address getByteBufferAddress(ByteBuffer buffer) {
		DirectMallocByteBuffer buf = (DirectMallocByteBuffer)buffer;
		return buf.address.add(buf.position());
	}

	public static Address getShortBufferAddress(ShortBuffer buffer) {
		DirectMallocShortBuffer buf = (DirectMallocShortBuffer)buffer;
		return buf.address.add(buf.position() << 1);
	}

	public static Address getIntBufferAddress(IntBuffer buffer) {
		DirectMallocIntBuffer buf = (DirectMallocIntBuffer)buffer;
		return buf.address.add(buf.position() << 2);
	}

	public static Address getFloatBufferAddress(FloatBuffer buffer) {
		DirectMallocFloatBuffer buf = (DirectMallocFloatBuffer)buffer;
		return buf.address.add(buf.position() << 2);
	}

	public static Int8Array getByteBufferView(ByteBuffer buffer) {
		DirectMallocByteBuffer buf = (DirectMallocByteBuffer)buffer;
		return getByteBufferView0(buf.address.add(buf.position()), buf.remaining());
	}

	@Import(module = "WASMGCBufferAllocator", name = "getByteBufferView")
	public static native Int8Array getByteBufferView0(Address addr, int length);

	public static Uint8Array getUnsignedByteBufferView(ByteBuffer buffer) {
		DirectMallocByteBuffer buf = (DirectMallocByteBuffer)buffer;
		return getUnsignedByteBufferView0(buf.address.add(buf.position()), buf.remaining());
	}

	public static Uint8Array getUnsignedByteBufferView(ShortBuffer buffer) {
		DirectMallocIntBuffer buf = (DirectMallocIntBuffer)buffer;
		return getUnsignedByteBufferView0(buf.address.add(buf.position()), buf.remaining() << 1);
	}

	public static Uint8Array getUnsignedByteBufferView(IntBuffer buffer) {
		DirectMallocIntBuffer buf = (DirectMallocIntBuffer)buffer;
		return getUnsignedByteBufferView0(buf.address.add(buf.position()), buf.remaining() << 2);
	}

	@Import(module = "WASMGCBufferAllocator", name = "getUnsignedByteBufferView")
	public static native Uint8Array getUnsignedByteBufferView0(Address addr, int length);

	public static Uint8ClampedArray getUnsignedClampedByteBufferView(ByteBuffer buffer) {
		DirectMallocByteBuffer buf = (DirectMallocByteBuffer)buffer;
		return getUnsignedClampedByteBufferView0(buf.address.add(buf.position()), buf.remaining());
	}

	@Import(module = "WASMGCBufferAllocator", name = "getUnsignedClampedByteBufferView")
	public static native Uint8ClampedArray getUnsignedClampedByteBufferView0(Address addr, int length);

	public static Int16Array getShortBufferView(ShortBuffer buffer) {
		DirectMallocShortBuffer buf = (DirectMallocShortBuffer)buffer;
		return getShortBufferView0(buf.address.add(buf.position() << 1), buf.remaining());
	}

	public static Int16Array getShortBufferView(ByteBuffer buffer) {
		DirectMallocByteBuffer buf = (DirectMallocByteBuffer)buffer;
		return getShortBufferView0(buf.address.add(buf.position()), buf.remaining() >> 1);
	}

	@Import(module = "WASMGCBufferAllocator", name = "getShortBufferView")
	public static native Int16Array getShortBufferView0(Address addr, int length);

	public static Uint16Array getUnsignedShortBufferView(ShortBuffer buffer) {
		DirectMallocShortBuffer buf = (DirectMallocShortBuffer)buffer;
		return getUnsignedShortBufferView0(buf.address.add(buf.position() << 1), buf.remaining());
	}

	public static Uint16Array getUnsignedShortBufferView(ByteBuffer buffer) {
		DirectMallocByteBuffer buf = (DirectMallocByteBuffer)buffer;
		return getUnsignedShortBufferView0(buf.address.add(buf.position()), buf.remaining() >> 1);
	}

	@Import(module = "WASMGCBufferAllocator", name = "getUnsignedShortBufferView")
	public static native Uint16Array getUnsignedShortBufferView0(Address addr, int length);

	public static Int32Array getIntBufferView(IntBuffer buffer) {
		DirectMallocIntBuffer buf = (DirectMallocIntBuffer)buffer;
		return getIntBufferView0(buf.address.add(buf.position() << 2), buf.remaining());
	}

	public static Int32Array getIntBufferView(ByteBuffer buffer) {
		DirectMallocByteBuffer buf = (DirectMallocByteBuffer)buffer;
		return getIntBufferView0(buf.address.add(buf.position()), buf.remaining() >> 2);
	}

	@Import(module = "WASMGCBufferAllocator", name = "getIntBufferView")
	public static native Int32Array getIntBufferView0(Address addr, int length);

	public static Float32Array getFloatBufferView(FloatBuffer buffer) {
		DirectMallocFloatBuffer buf = (DirectMallocFloatBuffer)buffer;
		return getFloatBufferView0(buf.address.add(buf.position() << 2), buf.remaining());
	}

	public static Float32Array getFloatBufferView(ByteBuffer buffer) {
		DirectMallocByteBuffer buf = (DirectMallocByteBuffer)buffer;
		return getFloatBufferView0(buf.address.add(buf.position()), buf.remaining() >> 2);
	}

	@Import(module = "WASMGCBufferAllocator", name = "getFloatBufferView")
	public static native Float32Array getFloatBufferView0(Address addr, int length);

	private static void throwNotOriginal(Object clazz) {
		throw notOriginal(clazz);
	}

	public static class WrongBufferClassType extends RuntimeException {
		public WrongBufferClassType(String msg) {
			super(msg);
		}
	}

	private static WrongBufferClassType notOriginal(Object clazz) {
		return new WrongBufferClassType("Tried to pass a " + clazz.getClass().getSimpleName() + " which was not the original buffer");
	}

}
