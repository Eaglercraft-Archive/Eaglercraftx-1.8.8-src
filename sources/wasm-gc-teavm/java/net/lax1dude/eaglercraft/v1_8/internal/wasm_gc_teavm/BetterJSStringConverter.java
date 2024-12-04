package net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm;

import org.teavm.interop.Address;
import org.teavm.interop.DirectMalloc;
import org.teavm.interop.Import;
import org.teavm.interop.Unmanaged;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSString;

import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCBufferAllocator;

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
public class BetterJSStringConverter {

	private static final TextDecoder textDecoder = new TextDecoder("utf-16");

	@Unmanaged
	public static JSString stringToJS(String input) {
		if(input == null) return null;
		int len = input.length();
		Address tmpAddr = WASMGCBufferAllocator.malloc(len << 1);
		for(int i = 0; i < len; ++i) {
			tmpAddr.add(i << 1).putChar(input.charAt(i));
		}
		JSString ret = textDecoder.decode(WASMGCBufferAllocator.getUnsignedByteBufferView0(tmpAddr, len << 1));
		WASMGCBufferAllocator.free(tmpAddr);
		return ret;
	}

	@Unmanaged
	public static JSArray<JSString> stringArrayToJS(String[] input) {
		if(input == null) return null;
		int len = input.length;
		JSArray<JSString> ret = new JSArray<>(len);
		for(int i = 0; i < len; ++i) {
			ret.set(i, stringToJS(input[i]));
		}
		return ret;
	}

	@Unmanaged
	public static String stringFromJS(JSString input) {
		if(input == null) return null;
		int len = input.getLength();
		char[] chars = new char[len];
		for(int i = 0; i < len; ++i) {
			chars[i] = charCodeAt(input, i);
		}
		return new String(chars);
	}

	@Import(module = "teavmJso", name = "charAt")
	private static native char charCodeAt(JSString str, int idx);

	@Unmanaged
	public static String[] stringArrayFromJS(JSArray<JSString> input) {
		if(input == null) return null;
		int len = input.getLength();
		String[] ret = new String[len];
		for(int i = 0; i < len; ++i) {
			ret[i] = stringFromJS(input.get(i));
		}
		return ret;
	}

}
