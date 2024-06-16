package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.browser.Window;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.ArrayBufferView;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Int16Array;
import org.teavm.jso.typedarrays.Int32Array;
import org.teavm.jso.typedarrays.Int8Array;
import org.teavm.jso.typedarrays.Uint8Array;

import net.lax1dude.eaglercraft.v1_8.EagUtils;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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
public class TeaVMUtils {

	@JSBody(params = { "url" }, script = "URL.revokeObjectURL(url);")
	public static native void freeDataURL(String url);
	
	@JSBody(params = { "buf", "mime" }, script = "return URL.createObjectURL(new Blob([buf], {type: mime}));")
	public static native String getDataURL(ArrayBuffer buf, String mime);
	
	@JSBody(params = { "obj", "name", "handler" }, script = "obj.addEventListener(name, handler);")
	public static native void addEventListener(JSObject obj, String name, JSObject handler);
	
	@JSBody(params = {}, script = "return (new Error()).stack;")
	public static native String dumpJSStackTrace();

	private static abstract class TeaVMArrayObject implements JSObject {
		@JSProperty
	    public abstract ArrayBufferView getData();
	}

	public static Int8Array unwrapByteArray(byte[] buf) {
		if(buf == null) {
			return null;
		}
		return Int8Array.create(((TeaVMArrayObject)(Object)buf).getData().getBuffer());
	}

	public static ArrayBuffer unwrapArrayBuffer(byte[] buf) {
		if(buf == null) {
			return null;
		}
		return ((TeaVMArrayObject)(Object)buf).getData().getBuffer();
	}

	public static ArrayBufferView unwrapArrayBufferView(byte[] buf) {
		if(buf == null) {
			return null;
		}
		return ((TeaVMArrayObject)(Object)buf).getData();
	}

	@JSBody(params = { "buf" }, script = "return $rt_createByteArray(buf)")
	private static native JSObject wrapByteArray0(JSObject buf);

	public static byte[] wrapByteArray(Int8Array buf) {
		if(buf == null) {
			return null;
		}
		return (byte[])(Object)wrapByteArray0(buf.getBuffer());
	}

	public static byte[] wrapByteArrayBuffer(ArrayBuffer buf) {
		if(buf == null) {
			return null;
		}
		return (byte[])(Object)wrapByteArray0(buf);
	}

	public static byte[] wrapByteArrayBufferView(ArrayBufferView buf) {
		if(buf == null) {
			return null;
		}
		return (byte[])(Object)wrapByteArray0(buf.getBuffer());
	}

	public static Uint8Array unwrapUnsignedByteArray(byte[] buf) {
		if(buf == null) {
			return null;
		}
		return Uint8Array.create(((TeaVMArrayObject)(Object)buf).getData().getBuffer());
	}

	public static byte[] wrapUnsignedByteArray(Uint8Array buf) {
		if(buf == null) {
			return null;
		}
		return (byte[])(Object)wrapByteArray0(buf.getBuffer());
	}

	public static Int32Array unwrapIntArray(int[] buf) {
		if(buf == null) {
			return null;
		}
		return Int32Array.create(((TeaVMArrayObject)(Object)buf).getData().getBuffer());
	}

	public static ArrayBuffer unwrapArrayBuffer(int[] buf) {
		if(buf == null) {
			return null;
		}
		return ((TeaVMArrayObject)(Object)buf).getData().getBuffer();
	}

	public static ArrayBufferView unwrapArrayBufferView(int[] buf) {
		if(buf == null) {
			return null;
		}
		return ((TeaVMArrayObject)(Object)buf).getData();
	}

	@JSBody(params = { "buf" }, script = "return $rt_createIntArray(buf)")
	private static native JSObject wrapIntArray0(JSObject buf);

	public static int[] wrapIntArray(Int32Array buf) {
		if(buf == null) {
			return null;
		}
		return (int[])(Object)wrapIntArray0(buf.getBuffer());
	}

	public static int[] wrapIntArrayBuffer(ArrayBuffer buf) {
		if(buf == null) {
			return null;
		}
		return (int[])(Object)wrapIntArray0(buf);
	}

	public static int[] wrapIntArrayBufferView(ArrayBufferView buf) {
		if(buf == null) {
			return null;
		}
		return (int[])(Object)wrapIntArray0(buf.getBuffer());
	}

	public static Float32Array unwrapFloatArray(float[] buf) {
		if(buf == null) {
			return null;
		}
		return Float32Array.create(((TeaVMArrayObject)(Object)buf).getData().getBuffer());
	}

	public static ArrayBuffer unwrapArrayBuffer(float[] buf) {
		if(buf == null) {
			return null;
		}
		return ((TeaVMArrayObject)(Object)buf).getData().getBuffer();
	}

	public static ArrayBufferView unwrapArrayBufferView(float[] buf) {
		if(buf == null) {
			return null;
		}
		return ((TeaVMArrayObject)(Object)buf).getData();
	}

	@JSBody(params = { "buf" }, script = "return $rt_createFloatArray(buf)")
	private static native JSObject wrapFloatArray0(JSObject buf);

	public static float[] wrapFloatArray(Float32Array buf) {
		if(buf == null) {
			return null;
		}
		return (float[])(Object)wrapFloatArray0(buf.getBuffer());
	}

	public static float[] wrapFloatArrayBuffer(ArrayBuffer buf) {
		if(buf == null) {
			return null;
		}
		return (float[])(Object)wrapFloatArray0(buf);
	}

	public static float[] wrapFloatArrayBufferView(ArrayBufferView buf) {
		if(buf == null) {
			return null;
		}
		return (float[])(Object)wrapFloatArray0(buf.getBuffer());
	}

	public static Int16Array unwrapShortArray(short[] buf) {
		if(buf == null) {
			return null;
		}
		return Int16Array.create(((TeaVMArrayObject)(Object)buf).getData().getBuffer());
	}

	public static ArrayBuffer unwrapArrayBuffer(short[] buf) {
		if(buf == null) {
			return null;
		}
		return ((TeaVMArrayObject)(Object)buf).getData().getBuffer();
	}

	public static ArrayBufferView unwrapArrayBufferView(short[] buf) {
		if(buf == null) {
			return null;
		}
		return ((TeaVMArrayObject)(Object)buf).getData();
	}

	@JSBody(params = { "buf" }, script = "return $rt_createShortArray(buf)")
	private static native JSObject wrapShortArray0(JSObject buf);

	public static short[] wrapShortArray(Int16Array buf) {
		if(buf == null) {
			return null;
		}
		return (short[])(Object)wrapShortArray0(buf.getBuffer());
	}

	public static short[] wrapShortArrayBuffer(ArrayBuffer buf) {
		if(buf == null) {
			return null;
		}
		return (short[])(Object)wrapShortArray0(buf);
	}

	public static short[] wrapShortArrayBuffer(ArrayBufferView buf) {
		if(buf == null) {
			return null;
		}
		return (short[])(Object)wrapShortArray0(buf.getBuffer());
	}

	@Async
	public static native void sleepSetTimeout(int millis);

	private static void sleepSetTimeout(int millis, AsyncCallback<Void> cb) {
		Window.setTimeout(() -> cb.complete(null), millis);
	}

	public static String tryResolveClassesSource() {
		String str = dumpJSStackTrace();
		String[] frames = EagUtils.splitPattern.split(str);
		if("Error".equals(frames[0])) {
			// V8 stack trace
			if(frames.length > 1) {
				String framesTrim = frames[1].trim();
				if(framesTrim.startsWith("at")) {
					//definitely V8
					int i = framesTrim.indexOf('(');
					int j = framesTrim.indexOf(')');
					if(i != -1 && j != -1 && i < j) {
						return tryResolveClassesSourceFromFrame(framesTrim.substring(i + 1, j));
					}
				}
			}
		}else {
			// Mozilla/WebKit stack trace
			String framesTrim = frames[0].trim();
			int i = framesTrim.indexOf('@');
			if(i != -1) {
				return tryResolveClassesSourceFromFrame(framesTrim.substring(i + 1));
			}
		}
		return null;
	}

	private static String tryResolveClassesSourceFromFrame(String fileLineCol) {
		int i = fileLineCol.lastIndexOf(':');
		if(i > 0) {
			i = fileLineCol.lastIndexOf(':', i - 1);
		}
		if(i != -1) {
			return fileLineCol.substring(0, i);
		}
		return null;
	}
}
