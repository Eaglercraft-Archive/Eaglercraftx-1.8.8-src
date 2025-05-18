/*
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

package net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm;

import org.teavm.interop.Unmanaged;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSString;
import org.teavm.jso.impl.JS;

/**
 * Note: this is left over from when TeaVM JSO converted
 * strings using a horrifying concatenation loop, eagler
 * had a "better" implementation back then
 */
public class BetterJSStringConverter {

	@Unmanaged
	public static JSString stringToJS(String input) {
		return (JSString) JS.wrap(input);
	}

	@Unmanaged
	@SuppressWarnings("unchecked")
	public static JSArray<JSString> stringArrayToJS(String[] input) {
		return (JSArray<JSString>) JS.wrap(input);
	}

	@Unmanaged
	public static String stringFromJS(JSString input) {
		return JS.unwrapString(input);
	}

	@Unmanaged
	public static String[] stringArrayFromJS(JSArray<JSString> input) {
		return JS.unwrapStringArray(input);
	}

}