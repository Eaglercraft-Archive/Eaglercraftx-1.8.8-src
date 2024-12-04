package net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.opts;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

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
public interface JSEaglercraftXOptsHooks extends JSObject {

	@JSBody(script = "return (typeof this.localStorageSaved === \"function\") ? this.localStorageSaved : null;")
	JSObject getLocalStorageSavedHook();

	@JSBody(script = "return (typeof this.localStorageLoaded === \"function\") ? this.localStorageLoaded : null;")
	JSObject getLocalStorageLoadedHook();

	@JSBody(script = "return (typeof this.crashReportShow === \"function\") ? this.crashReportShow : null;")
	JSObject getCrashReportHook();

	@JSBody(script = "return (typeof this.screenChanged === \"function\") ? this.screenChanged : null;")
	JSObject getScreenChangedHook();

}
