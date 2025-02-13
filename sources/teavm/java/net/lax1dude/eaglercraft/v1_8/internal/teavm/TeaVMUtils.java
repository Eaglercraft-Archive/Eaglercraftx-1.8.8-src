/*
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

package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.teavm.backend.javascript.spi.GeneratedBy;
import org.teavm.backend.javascript.spi.InjectedBy;
import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLScriptElement;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.ArrayBufferView;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Int16Array;
import org.teavm.jso.typedarrays.Int32Array;
import org.teavm.jso.typedarrays.Int8Array;
import org.teavm.jso.typedarrays.Uint8Array;

import net.lax1dude.eaglercraft.v1_8.internal.teavm.generators.TeaVMUtilsUnwrapGenerator;

public class TeaVMUtils {

	@JSBody(params = { "url" }, script = "URL.revokeObjectURL(url);")
	public static native void freeDataURL(String url);
	
	@JSBody(params = { "buf", "mime" }, script = "return URL.createObjectURL(new Blob([buf], {type: mime}));")
	public static native String getDataURL(ArrayBuffer buf, String mime);
	
	@JSBody(params = { "blob" }, script = "return URL.createObjectURL(blob);")
	public static native String getDataURL(JSObject blob);
	
	@JSBody(params = { "obj", "name", "handler" }, script = "obj.addEventListener(name, handler);")
	public static native void addEventListener(JSObject obj, String name, JSObject handler);
	
	@JSBody(params = { "obj", "name", "handler" }, script = "obj.removeEventListener(name, handler);")
	public static native void removeEventListener(JSObject obj, String name, JSObject handler);
	
	@JSBody(params = {}, script = "return (new Error()).stack;")
	public static native String dumpJSStackTrace();

	@InjectedBy(TeaVMUtilsUnwrapGenerator.UnwrapTypedArray.class)
	public static native Int8Array unwrapByteArray(byte[] buf);

	@InjectedBy(TeaVMUtilsUnwrapGenerator.UnwrapArrayBuffer.class)
	public static native ArrayBuffer unwrapArrayBuffer(byte[] buf);

	@InjectedBy(TeaVMUtilsUnwrapGenerator.UnwrapTypedArray.class)
	public static native ArrayBufferView unwrapArrayBufferView(byte[] buf);

	@GeneratedBy(TeaVMUtilsUnwrapGenerator.WrapTypedArray.class)
	public static native byte[] wrapByteArray(Int8Array buf);

	@GeneratedBy(TeaVMUtilsUnwrapGenerator.WrapArrayBuffer.class)
	public static native byte[] wrapByteArrayBuffer(ArrayBuffer buf);

	@GeneratedBy(TeaVMUtilsUnwrapGenerator.WrapArrayBufferView.class)
	public static native byte[] wrapByteArrayBufferView(ArrayBufferView buf);

	@InjectedBy(TeaVMUtilsUnwrapGenerator.UnwrapUnsignedTypedArray.class)
	public static native Uint8Array unwrapUnsignedByteArray(byte[] buf);

	@GeneratedBy(TeaVMUtilsUnwrapGenerator.WrapArrayBufferView.class)
	public static native byte[] wrapUnsignedByteArray(Uint8Array buf);

	@InjectedBy(TeaVMUtilsUnwrapGenerator.UnwrapTypedArray.class)
	public static native Int32Array unwrapIntArray(int[] buf);

	@InjectedBy(TeaVMUtilsUnwrapGenerator.UnwrapArrayBuffer.class)
	public static native ArrayBuffer unwrapArrayBuffer(int[] buf);

	@InjectedBy(TeaVMUtilsUnwrapGenerator.UnwrapTypedArray.class)
	public static native ArrayBufferView unwrapArrayBufferView(int[] buf);

	@GeneratedBy(TeaVMUtilsUnwrapGenerator.WrapTypedArray.class)
	public static native int[] wrapIntArray(Int32Array buf);

	@GeneratedBy(TeaVMUtilsUnwrapGenerator.WrapArrayBuffer.class)
	public static native int[] wrapIntArrayBuffer(ArrayBuffer buf);

	@GeneratedBy(TeaVMUtilsUnwrapGenerator.WrapArrayBufferView.class)
	public static native int[] wrapIntArrayBufferView(ArrayBufferView buf);

	@InjectedBy(TeaVMUtilsUnwrapGenerator.UnwrapTypedArray.class)
	public static native Float32Array unwrapFloatArray(float[] buf);

	@InjectedBy(TeaVMUtilsUnwrapGenerator.UnwrapArrayBuffer.class)
	public static native ArrayBuffer unwrapArrayBuffer(float[] buf);

	@InjectedBy(TeaVMUtilsUnwrapGenerator.UnwrapTypedArray.class)
	public static native ArrayBufferView unwrapArrayBufferView(float[] buf);

	@GeneratedBy(TeaVMUtilsUnwrapGenerator.WrapTypedArray.class)
	public static native float[] wrapFloatArray(Float32Array buf);

	@GeneratedBy(TeaVMUtilsUnwrapGenerator.WrapArrayBuffer.class)
	public static native float[] wrapFloatArrayBuffer(ArrayBuffer buf);

	@GeneratedBy(TeaVMUtilsUnwrapGenerator.WrapArrayBufferView.class)
	public static native float[] wrapFloatArrayBufferView(ArrayBufferView buf);

	@InjectedBy(TeaVMUtilsUnwrapGenerator.UnwrapTypedArray.class)
	public static native Int16Array unwrapShortArray(short[] buf);

	@InjectedBy(TeaVMUtilsUnwrapGenerator.UnwrapArrayBuffer.class)
	public static native ArrayBuffer unwrapArrayBuffer(short[] buf);

	@InjectedBy(TeaVMUtilsUnwrapGenerator.UnwrapTypedArray.class)
	public static native ArrayBufferView unwrapArrayBufferView(short[] buf);

	@GeneratedBy(TeaVMUtilsUnwrapGenerator.WrapTypedArray.class)
	public static native short[] wrapShortArray(Int16Array buf);

	@GeneratedBy(TeaVMUtilsUnwrapGenerator.WrapArrayBuffer.class)
	public static native short[] wrapShortArrayBuffer(ArrayBuffer buf);

	@GeneratedBy(TeaVMUtilsUnwrapGenerator.WrapArrayBufferView.class)
	public static native short[] wrapShortArrayBuffer(ArrayBufferView buf);

	@Async
	public static native void sleepSetTimeout(int millis);

	private static void sleepSetTimeout(int millis, AsyncCallback<Void> cb) {
		Window.setTimeout(() -> cb.complete(null), millis);
	}

	public static final Comparator<OffsetTouch> touchSortingComparator2 = (t1, t2) -> {
		return t1.eventUID - t2.eventUID;
	};

	public static List<OffsetTouch> toSortedTouchList(TouchList touchList, SortedTouchEvent.ITouchUIDMapper mapper,
			int originX, int originY) {
		int l = touchList.getLength();
		List<OffsetTouch> ret = new ArrayList<>(l);
		for(int i = 0; i < l; ++i) {
			ret.add(OffsetTouch.create(touchList.item(i), mapper, originX, originY));
		}
		Collections.sort(ret, touchSortingComparator2);
		return ret;
	}

	public static String tryResolveClassesSource() {
		return ClassesJSLocator.resolveClassesJSFromThrowable();
	}

	public static HTMLScriptElement tryResolveClassesSourceInline() {
		return ClassesJSLocator.resolveClassesJSFromInline();
	}

	@JSBody(params = { "obj" }, script = "console.log(obj);")
	public static native void objDump(JSObject obj);

	@JSBody(params = { "obj" }, script = "return \"\" + obj;")
	public static native String safeToString(JSObject obj);

	@JSBody(params = { "obj" }, script = "return (!!obj && (typeof obj.message === \"string\")) ? obj.message : (\"\" + obj);")
	public static native String safeErrorMsgToString(JSObject obj);

	@JSBody(params = { "obj" }, script = "return !!obj;")
	public static native boolean isTruthy(JSObject object);

	@JSBody(params = { "obj" }, script = "return !obj;")
	public static native boolean isNotTruthy(JSObject object);

	@JSBody(params = { "obj" }, script = "return obj === undefined;")
	public static native boolean isUndefined(JSObject object);

	public static <T extends JSObject> T ensureDefined(T valIn) {
		return isUndefined((JSObject)valIn) ? null : valIn;
	}

	@JSBody(params = { "obj" }, script = "return obj.stack||null;")
	public static native String getStackSafe(JSObject object);

}