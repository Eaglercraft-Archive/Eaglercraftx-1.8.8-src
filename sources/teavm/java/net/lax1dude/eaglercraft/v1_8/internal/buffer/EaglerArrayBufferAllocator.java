package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.DataView;
import org.teavm.jso.typedarrays.Float32Array;
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
		return new EaglerArrayByteBuffer(DataView.create(ArrayBuffer.create(size)));
	}

	public static ByteBuffer wrapByteBufferTeaVM(DataView dv) {
		return new EaglerArrayByteBuffer(dv);
	}

	public static IntBuffer allocateIntBuffer(int size) {
		return new EaglerArrayIntBuffer(DataView.create(ArrayBuffer.create(size << 2)));
	}

	public static IntBuffer wrapIntBufferTeaVM(DataView dv) {
		return new EaglerArrayIntBuffer(dv);
	}

	public static FloatBuffer allocateFloatBuffer(int size) {
		return new EaglerArrayFloatBuffer(DataView.create(ArrayBuffer.create(size << 2)));
	}

	public static FloatBuffer wrapFloatBufferTeaVM(DataView dv) {
		return new EaglerArrayFloatBuffer(dv);
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
	
	public static Uint16Array getDataViewStupid16(ByteBuffer buffer) {
		if(buffer instanceof EaglerArrayByteBuffer) {
			EaglerArrayByteBuffer b = (EaglerArrayByteBuffer)buffer;
			DataView d = b.dataView;
			int p = b.position;
			int l = b.limit;
			int i = d.getByteOffset();
			return Uint16Array.create(d.getBuffer(), i + p, (l - p) >> 1);
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
