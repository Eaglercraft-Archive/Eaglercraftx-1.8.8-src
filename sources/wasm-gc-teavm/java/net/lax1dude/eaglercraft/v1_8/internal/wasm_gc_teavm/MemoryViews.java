/*
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm;

import org.teavm.interop.Import;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Int16Array;
import org.teavm.jso.typedarrays.Int32Array;
import org.teavm.jso.typedarrays.Int8Array;
import org.teavm.jso.typedarrays.Uint16Array;
import org.teavm.jso.typedarrays.Uint32Array;
import org.teavm.jso.typedarrays.Uint8Array;

public class MemoryViews {

	public static Int8Array i8 = null;
	public static Uint8Array u8 = null;
	public static Int16Array i16 = null;
	public static Uint16Array u16 = null;
	public static Int32Array i32 = null;
	public static Uint32Array u32 = null;
	public static Float32Array f32 = null;

	static void setupCallback() {
		setHeapViewCallback(jso(MemoryViews::updateHeapViews));
	}

	@JSBody(params = { "obj" }, script = "return obj;")
	private static native JSObject jso(IHeapViewUpdate obj);

	@Import(module = "WASMGCBufferAllocator", name = "setHeapViewCallback")
	private static native void setHeapViewCallback(JSObject callback);

	@JSFunctor
	private interface IHeapViewUpdate extends JSObject {
		void call(Int8Array i8, Uint8Array u8, Int16Array i16, Uint16Array u16, Int32Array i32,
			Uint32Array u32, Float32Array f32);
	}

	private static void updateHeapViews(Int8Array i8, Uint8Array u8, Int16Array i16, Uint16Array u16, Int32Array i32,
			Uint32Array u32, Float32Array f32) {
		MemoryViews.i8 = i8;
		MemoryViews.u8 = u8;
		MemoryViews.i16 = i16;
		MemoryViews.u16 = u16;
		MemoryViews.i32 = i32;
		MemoryViews.u32 = u32;
		MemoryViews.f32 = f32;
	}

}
