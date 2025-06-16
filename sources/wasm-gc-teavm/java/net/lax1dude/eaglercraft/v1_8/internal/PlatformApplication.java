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

package net.lax1dude.eaglercraft.v1_8.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.teavm.interop.Import;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.browser.Storage;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.canvas.ImageData;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;
import org.teavm.jso.typedarrays.Uint8ClampedArray;

import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.MemoryStack;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCBufferAllocator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCDirectArrayConverter;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.BetterJSStringConverter;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.ClientMain;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.TeaVMUtils;

public class PlatformApplication {

	public static void openLink(String url) {
		if(url.indexOf(':') == -1) {
			url = "http://" + url;
		}
		URI parsedURL;
		try {
			parsedURL = new URI(url);
		}catch(URISyntaxException ex) {
			PlatformRuntime.logger.error("Refusing to open invalid URL: {}", url);
			return;
		}
		try {
			PlatformRuntime.win.open(parsedURL.toString(), "_blank", "noopener,noreferrer");
		}catch(Throwable t) {
			PlatformRuntime.logger.error("Exception opening link!");
		}
	}

	public static void setClipboard(String text) {
		long start = PlatformRuntime.steadyTimeMillis();
		boolean b = false;
		try {
			b = setClipboard0(BetterJSStringConverter.stringToJS(text));
		}catch(Throwable t) {
			PlatformRuntime.logger.error("Exception setting clipboard data");
		}
		if(!b) {
			try {
				Window.prompt("Here is the text you're trying to copy:", text);
			}catch(Throwable t2) {
			}
		}
		if(PlatformRuntime.steadyTimeMillis() - start > 500l) {
			PlatformInput.unpressCTRL = true;
		}
	}

	@Import(module = "platformApplication", name = "setClipboard")
	private static native boolean setClipboard0(JSString str);

	public static String getClipboard() {
		long start = PlatformRuntime.steadyTimeMillis();
		String ret = null;
		try {
			ret = BetterJSStringConverter.stringFromJS(getClipboard0());
		}catch(Throwable t) {
			PlatformRuntime.logger.error("Exception getting clipboard data");
		}
		if(ret == null) {
			try {
				ret = Window.prompt("Please enter the text to paste:");
			}catch(Throwable t2) {
			}
		}
		if(PlatformRuntime.steadyTimeMillis() - start > 500l) {
			PlatformInput.unpressCTRL = true;
		}
		return ret != null ? ret : "";
	}

	@Import(module = "platformApplication", name = "getClipboard")
	private static native JSString getClipboard0();

	public static void setLocalStorage(String name, byte[] data) {
		setLocalStorage(name, data, true);
	}

	public static void setLocalStorage(String name, byte[] data, boolean hooks) {
		IClientConfigAdapter adapter = PlatformRuntime.getClientConfigAdapter();
		String eagName = adapter.getLocalStorageNamespace() + "." + name;
		String b64 = data != null ? Base64.encodeBase64String(data) : null;
		try {
			Storage s = Window.current().getLocalStorage();
			if(s != null) {
				if(b64 != null) {
					s.setItem(eagName, b64);
				}else {
					s.removeItem(eagName);
				}
			}
		}catch(Throwable t) {
		}
		if(hooks) {
			adapter.getHooks().callLocalStorageSavedHook(name, b64);
		}
	}

	public static byte[] getLocalStorage(String name) {
		return getLocalStorage(name, true);
	}

	public static byte[] getLocalStorage(String name, boolean hooks) {
		IClientConfigAdapter adapter = PlatformRuntime.getClientConfigAdapter();
		String eagName = adapter.getLocalStorageNamespace() + "." + name;
		byte[] hooked = null;
		if(hooks) {
			String hookedStr = adapter.getHooks().callLocalStorageLoadHook(eagName);
			if(hookedStr != null) {
				try {
					hooked = Base64.decodeBase64(hookedStr);
				}catch(Throwable t) {
					PlatformRuntime.logger.error("Invalid Base64 recieved from local storage hook!");
					hooked = null;
				}
			}
		}
		if(hooked == null) {
			try {
				Storage s = Window.current().getLocalStorage();
				if(s != null) {
					String str = s.getItem(eagName);
					if(str != null) {
						return Base64.decodeBase64(str);
					}else {
						return null;
					}
				}else {
					return null;
				}
			}catch(Throwable t) {
				return null;
			}
		}else {
			return hooked;
		}
	}

	public static void setResetSettingsCallbackWASM() {
		setResetSettingsCallbackWASM0().call(ClientMain::resetSettings);
	}

	@JSFunctor
	private static interface JSWASMResetSettingsCallback extends JSObject {
		void callback();
	}

	private static interface JSWASMResetSettingsCallbackInterface extends JSObject {
		void call(JSWASMResetSettingsCallback callback);
	}

	@Import(module = "platformApplication", name = "setResetSettingsCallback")
	private static native JSWASMResetSettingsCallbackInterface setResetSettingsCallbackWASM0();

	public static void displayFileChooser(String mime, String ext) {
		displayFileChooser0(BetterJSStringConverter.stringToJS(mime), BetterJSStringConverter.stringToJS(ext));
	}

	@Import(module = "platformApplication", name = "displayFileChooser")
	private static native void displayFileChooser0(JSString mime, JSString ext);

	@Import(module = "platformApplication", name = "fileChooserHasResult")
	public static native boolean fileChooserHasResult();

	public static FileChooserResult getFileChooserResult() {
		JSFileChooserResult jsResult = getFileChooserResult0();
		if(jsResult != null) {
			return new FileChooserResult(jsResult.getFileName(),
					WASMGCDirectArrayConverter.externU8ArrayToByteArray(new Uint8Array(jsResult.getFileData())));
		}else {
			return null;
		}
	}

	private interface JSFileChooserResult extends JSObject {

		@JSProperty
		String getFileName();

		@JSProperty
		ArrayBuffer getFileData();

	}

	@Import(module = "platformApplication", name = "getFileChooserResult")
	private static native JSFileChooserResult getFileChooserResult0();

	@Import(module = "platformApplication", name = "clearFileChooserResult")
	public static native void clearFileChooserResult();

	@Import(module = "platformApplication", name = "getFaviconURL")
	public static native JSString faviconURLTeaVM();

	public static void showPopup(String msg) {
		Window.alert(msg);
	}

	@JSBody(params = { "doc", "str" }, script = "doc.write(str);doc.close();")
	private static native void documentWrite(HTMLDocument doc, String str);

	public static void openCreditsPopup(String text) {
		Window currentWin = PlatformRuntime.win;
		
		int w = (int)(850 * PlatformInput.getDPI());
		int h = (int)(700 * PlatformInput.getDPI());
		
		int x = (currentWin.getScreen().getWidth() - w) / 2;
		int y = (currentWin.getScreen().getHeight() - h) / 2;
		
		Window newWin = Window.current().open("", "_blank", "top=" + y + ",left=" + x + ",width=" + w + ",height=" + h + ",menubar=0,status=0,titlebar=0,toolbar=0");
		if(newWin == null || TeaVMUtils.isNotTruthy(newWin)) {
			Window.alert("ERROR: Popup blocked!\n\nPlease make sure you have popups enabled for this site!");
			return;
		}

		newWin.focus();
		documentWrite(newWin.getDocument(), "<!DOCTYPE html><html><head><meta charset=\"UTF-8\" />"
				+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" /><title>EaglercraftX 1.8 Credits</title>"
				+ "<link type=\"image/png\" rel=\"shortcut icon\" href=\""
				+ BetterJSStringConverter.stringFromJS(PlatformApplication.faviconURLTeaVM()) + "\" />"
				+ "</head><body><pre style=\"font:15px Consolas,monospace;\">" + text + "</pre></body></html>");
	}

	public static void downloadFileWithName(String str, byte[] dat) {
		MemoryStack.push();
		try {
			downloadFileWithNameTeaVM(BetterJSStringConverter.stringToJS(str), WASMGCDirectArrayConverter.byteArrayToStackU8Array(dat));
		}finally {
			MemoryStack.pop();
		}
	}

	@Import(module = "platformApplication", name = "downloadFileWithNameU8")
	public static native void downloadFileWithNameTeaVM(JSString str, Uint8Array dat);

	@Import(module = "platformApplication", name = "downloadFileWithNameA")
	public static native void downloadFileWithNameTeaVM(JSString str, ArrayBuffer dat);

	private static final DateFormat dateFormatSS = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

	@JSBody(params = { }, script = "return { willReadFrequently: true };")
	static native JSObject youEagler();

	public static String saveScreenshot() {
		PlatformOpenGL._wglBindFramebuffer(0x8D40, null);
		int w = PlatformInput.getWindowWidth();
		int h = PlatformInput.getWindowHeight();
		ByteBuffer buf = PlatformRuntime.allocateByteBuffer(w * h * 4);
		PlatformOpenGL._wglReadPixels(0, 0, w, h, 6408, 5121, buf);
		for(int i = 3, l = buf.remaining(); i < l; i += 4) {
			buf.put(i, (byte)0xFF);
		}
		String name = "screenshot_" + dateFormatSS.format(new Date()).toString() + ".png";
		HTMLCanvasElement copyCanvas = (HTMLCanvasElement) Window.current().getDocument().createElement("canvas");
		copyCanvas.setWidth(w);
		copyCanvas.setHeight(h);
		CanvasRenderingContext2D ctx = (CanvasRenderingContext2D) copyCanvas.getContext("2d", youEagler());
		ImageData imgData = ctx.createImageData(w, h);
		Uint8ClampedArray dest = imgData.getData();
		int ww = w * 4;
		for(int i = 0, j; i < h; ++i) {
			j = (h - i - 1) * ww;
			buf.limit(j + ww);
			buf.position(j);
			dest.set(WASMGCBufferAllocator.getUnsignedClampedByteBufferView(buf), i * ww);
		}
		ctx.putImageData(imgData, 0, 0);
		PlatformRuntime.freeByteBuffer(buf);
		downloadScreenshotWithNameTeaVM(BetterJSStringConverter.stringToJS(name), copyCanvas);
		return name;
	}

	@Import(module = "platformApplication", name = "downloadScreenshot")
	private static native void downloadScreenshotWithNameTeaVM(JSString str, HTMLCanvasElement cvs);

	@Import(module = "platformApplication", name = "showDebugConsole")
	public static native void showDebugConsole();

	public static void addLogMessage(String text, boolean err) {
	}

	@Import(module = "platformApplication", name = "isShowingDebugConsole")
	public static native boolean isShowingDebugConsole();

	@JSBody(params = { "str" }, script = "window.minecraftServer = str;")
	public static native void setMCServerWindowGlobal(String str);

}