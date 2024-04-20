package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import org.teavm.jso.typedarrays.DataView;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Int32Array;
import org.teavm.jso.typedarrays.Int8Array;
import org.teavm.jso.typedarrays.Uint16Array;
import org.teavm.jso.typedarrays.Uint8Array;

/**
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
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
public class EaglerArrayBufferAllocator {
	
	public static class WrongBufferClassType extends RuntimeException {
		public WrongBufferClassType(String msg) {
			super(msg);
		}
	}

	public static ByteBuffer allocateByteBuffer(int size) {
		return new EaglerArrayByteBuffer(Int8Array.create(size));
	}

	public static ByteBuffer wrapByteBufferTeaVM(DataView dv) {
		return new EaglerArrayByteBuffer(dv);
	}

	public static ByteBuffer wrapByteBufferTeaVM(Int8Array typedArray) {
		return new EaglerArrayByteBuffer(typedArray);
	}

	public static IntBuffer allocateIntBuffer(int size) {
		return new EaglerArrayIntBuffer(Int32Array.create(size));
	}

	public static IntBuffer wrapIntBufferTeaVM(Int32Array typedArray) {
		return new EaglerArrayIntBuffer(typedArray);
	}

	public static FloatBuffer allocateFloatBuffer(int size) {
		return new EaglerArrayFloatBuffer(Float32Array.create(size));
	}

	public static FloatBuffer wrapFloatBufferTeaVM(Float32Array typedArray) {
		return new EaglerArrayFloatBuffer(typedArray);
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
				return DataView.create(d.getBuffer(), d.getByteOffset() + p, l - p);
			}
		}else {
			throw notEagler(buffer);
		}
	}
	
	public static Int8Array getDataView8(ByteBuffer buffer) {
		if(buffer instanceof EaglerArrayByteBuffer) {
			EaglerArrayByteBuffer b = (EaglerArrayByteBuffer)buffer;
			Int8Array d = b.typedArray;
			int p = b.position;
			int l = b.limit;
			if(p == 0 && l == b.capacity) {
				return d;
			}else {
				int i = d.getByteOffset();
				return Int8Array.create(d.getBuffer(), d.getByteOffset() + p, l - p);
			}
		}else {
			throw notEagler(buffer);
		}
	}
	
	public static Uint8Array getDataView8Unsigned(ByteBuffer buffer) {
		if(buffer instanceof EaglerArrayByteBuffer) {
			EaglerArrayByteBuffer b = (EaglerArrayByteBuffer)buffer;
			Int8Array d = b.typedArray;
			int p = b.position;
			int i = d.getByteOffset();
			return Uint8Array.create(d.getBuffer(), i + p, b.limit - p);
		}else {
			throw notEagler(buffer);
		}
	}
	
	public static Uint16Array getDataView16Unsigned(ByteBuffer buffer) {
		if(buffer instanceof EaglerArrayByteBuffer) {
			EaglerArrayByteBuffer b = (EaglerArrayByteBuffer)buffer;
			Int8Array d = b.typedArray;
			int p = b.position;
			return Uint16Array.create(d.getBuffer(), d.getByteOffset() + p, (b.limit - p) >> 1);
		}else {
			throw notEagler(buffer);
		}
	}
	
	public static Float32Array getDataView32F(ByteBuffer buffer) {
		if(buffer instanceof EaglerArrayByteBuffer) {
			EaglerArrayByteBuffer b = (EaglerArrayByteBuffer)buffer;
			Int8Array d = b.typedArray;
			int p = b.position;
			return Float32Array.create(d.getBuffer(), d.getByteOffset() + p, (b.limit - p) >> 2);
		}else {
			throw notEagler(buffer);
		}
	}
	
	public static Int32Array getDataView32(IntBuffer buffer) {
		if(buffer instanceof EaglerArrayIntBuffer) {
			EaglerArrayIntBuffer b = (EaglerArrayIntBuffer)buffer;
			Int32Array d = b.typedArray;
			int p = b.position;
			int l = b.limit;
			if(p == 0 && l == b.capacity) {
				return d;
			}else {
				return Int32Array.create(d.getBuffer(), d.getByteOffset() + (p << 2), l - p);
			}
		}else {
			throw notEagler(buffer);
		}
	}
	
	public static Uint8Array getDataView8Unsigned(IntBuffer buffer) {
		if(buffer instanceof EaglerArrayIntBuffer) {
			EaglerArrayIntBuffer b = (EaglerArrayIntBuffer)buffer;
			Int32Array d = b.typedArray;
			int p = b.position;
			int l = b.limit;
			return Uint8Array.create(d.getBuffer(), d.getByteOffset() + (p << 2), (l - p) << 2);
		}else {
			throw notEagler(buffer);
		}
	}
	
	public static Float32Array getDataView32F(FloatBuffer buffer) {
		if(buffer instanceof EaglerArrayFloatBuffer) {
			EaglerArrayFloatBuffer b = (EaglerArrayFloatBuffer)buffer;
			Float32Array d = b.typedArray;
			int p = b.position;
			int l = b.limit;
			if(p == 0 && l == b.capacity) {
				return d;
			}else {
				return Float32Array.create(d.getBuffer(), d.getByteOffset() + (p << 2), l - p);
			}
		}else {
			throw notEagler(buffer);
		}
	}
	
	public static Uint8Array getDataView8Unsigned(FloatBuffer buffer) {
		if(buffer instanceof EaglerArrayFloatBuffer) {
			EaglerArrayFloatBuffer b = (EaglerArrayFloatBuffer)buffer;
			Float32Array d = b.typedArray;
			int p = b.position;
			int l = b.limit;
			return Uint8Array.create(d.getBuffer(), d.getByteOffset() + (p << 2), (l - p) << 2);
		}else {
			throw notEagler(buffer);
		}
	}
	
	private static WrongBufferClassType notEagler(Object clazz) {
		return new WrongBufferClassType("Tried to pass a " + clazz.getClass().getSimpleName() + " which is not a native eagler buffer");
	}

}
