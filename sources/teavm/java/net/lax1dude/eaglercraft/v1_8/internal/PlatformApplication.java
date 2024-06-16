package net.lax1dude.eaglercraft.v1_8.internal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Storage;
import org.teavm.jso.browser.TimerHandler;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.typedarrays.ArrayBuffer;

import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.DebugConsoleWindow;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;

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
public class PlatformApplication {

	public static void openLink(String url) {
		if(url.indexOf(':') == -1) {
			url = "http://" + url;
		}
		Window.current().open(url, "_blank", "noopener,noreferrer");
	}

	public static void setClipboard(String text) {
		try {
			setClipboard0(text);
		}catch(Throwable t) {
			PlatformRuntime.logger.error("Exception setting clipboard data");
		}
	}
	
	public static String getClipboard() {
		try {
			return getClipboard0();
		}catch(Throwable t) {
			PlatformRuntime.logger.error("Exception getting clipboard data");
			return "";
		}
	}

	@JSFunctor
	private static interface StupidFunctionResolveString extends JSObject {
		void resolveStr(String s);
	}
	
	@Async
	private static native String getClipboard0();
	
	private static void getClipboard0(final AsyncCallback<String> cb) {
		final long start = System.currentTimeMillis();
		getClipboard1(new StupidFunctionResolveString() {
			@Override
			public void resolveStr(String s) {
				if(System.currentTimeMillis() - start > 500l) {
					PlatformInput.unpressCTRL = true;
				}
				cb.complete(s);
			}
		});
	}
	
	@JSBody(params = { "cb" }, script = "if(!window.navigator.clipboard || !window.navigator.clipboard.readText) cb(\"\"); else window.navigator.clipboard.readText().then(function(s) { cb(s); }, function(s) { cb(\"\"); });")
	private static native void getClipboard1(StupidFunctionResolveString cb);
	
	@JSBody(params = { "str" }, script = "if(window.navigator.clipboard) window.navigator.clipboard.writeText(str);")
	private static native void setClipboard0(String str);
	
	public static void setLocalStorage(String name, byte[] data) {
		setLocalStorage(name, data, true);
	}
	
	public static void setLocalStorage(String name, byte[] data, boolean hooks) {
		IClientConfigAdapter adapter = PlatformRuntime.getClientConfigAdapter();
		String eagName = adapter.getLocalStorageNamespace() + "." + name;
		String b64 = Base64.encodeBase64String(data);
		try {
			Storage s = Window.current().getLocalStorage();
			if(s != null) {
				if(data != null) {
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
	
	private static final DateFormat dateFormatSS = EagRuntime.fixDateFormat(new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss"));
	
	public static String saveScreenshot() {
		String name = "screenshot_" + dateFormatSS.format(new Date()).toString() + ".png";
		int w = PlatformRuntime.canvas.getWidth();
		int h = PlatformRuntime.canvas.getHeight();
		HTMLCanvasElement copyCanvas = (HTMLCanvasElement) Window.current().getDocument().createElement("canvas");
		copyCanvas.setWidth(w);
		copyCanvas.setHeight(h);
		CanvasRenderingContext2D ctx = (CanvasRenderingContext2D) copyCanvas.getContext("2d");
		ctx.drawImage(PlatformRuntime.canvas, 0, 0);
		saveScreenshot(name, copyCanvas);
		return name;
	}
	
	@JSBody(params = { "name", "cvs" }, script = "var a=document.createElement(\"a\");a.href=cvs.toDataURL(\"image/png\");a.download=name;a.click();")
	private static native void saveScreenshot(String name, HTMLCanvasElement cvs);
	
	public static void showPopup(final String msg) {
		Window.setTimeout(new TimerHandler() {
			@Override
			public void onTimer() {
				Window.alert(msg);
			}
		}, 1);
	}

	@JSFunctor
	private static interface FileChooserCallback extends JSObject {
		void accept(String name, ArrayBuffer buffer);
	}

	private static class FileChooserCallbackImpl implements FileChooserCallback {

		private static final FileChooserCallbackImpl instance = new FileChooserCallbackImpl();

		@Override
		public void accept(String name, ArrayBuffer buffer) {
			fileChooserHasResult = true;
			if(name == null) {
				fileChooserResultObject = null;
			}else {
				fileChooserResultObject = new FileChooserResult(name, TeaVMUtils.wrapByteArrayBuffer(buffer));
			}
		}

	}

	private static volatile boolean fileChooserHasResult = false;
	private static volatile FileChooserResult fileChooserResultObject = null;

	@JSBody(params = { "inputElement", "callback" }, script = 
			"if(inputElement.files.length > 0) {"
			+ "const value = inputElement.files[0];"
			+ "value.arrayBuffer().then(function(arr){ callback(value.name, arr); })"
			+ ".catch(function(){ callback(null, null); });"
			+ "} else callback(null, null);")
	private static native void getFileChooserResult(HTMLInputElement inputElement, FileChooserCallback callback);

	@JSBody(params = { "inputElement", "value" }, script = "inputElement.accept = value;")
	private static native void setAcceptSelection(HTMLInputElement inputElement, String value);

	@JSBody(params = { "inputElement", "enable" }, script = "inputElement.multiple = enable;")
	private static native void setMultipleSelection(HTMLInputElement inputElement, boolean enable);

	public static void displayFileChooser(String mime, String ext) {
		final HTMLInputElement inputElement = (HTMLInputElement) Window.current().getDocument().createElement("input");
		inputElement.setType("file");
		if(mime == null) {
			setAcceptSelection(inputElement, "." + ext);
		}else {
			setAcceptSelection(inputElement, mime);
		}
		setMultipleSelection(inputElement, false);
		inputElement.addEventListener("change", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				getFileChooserResult(inputElement, FileChooserCallbackImpl.instance);
			}
		});
		inputElement.click();
	}

	public static boolean fileChooserHasResult() {
		return fileChooserHasResult;
	}

	public static FileChooserResult getFileChooserResult() {
		fileChooserHasResult = false;
		FileChooserResult res = fileChooserResultObject;
		fileChooserResultObject = null;
		return res;
	}

	private static final String faviconURLString = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAIAAAD8GO2jAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAFiUAABYlAUlSJPAAAAR/SURBVEhLtZXZK3ZRFMYPcqXc+gv413DHxVuGIpIhkciQWaRccCNjSCkligwXSOZ5nmfv9zvn2e8+58V753sudmuvvdZ61l5r7XOc8H+GS/D19aUNkPz5+aktQH5/f//4+LBKZKuRkpUtQjCUYG5gD2T38vLy/PwsDfL9/f3Dw8PT05M0b29vnKLhCKCBT4L4gvBLBIei4//4+Hh1dUVEQutUuLu7E83FxQUGnKLBWKfQaA3S+AREVxaEOD8/Pzk50XpzcyMDcH19zdZG3N3d3dzc3Nvb01aX5pQUpQGGQJxcQpfNysoKhUIdHR1o1tbWbInYAgxIPDMzMy8vLzc3FxqOdMoRqwJK8G8ALUYIhHMiSEhIwI6CyIb0qQzC4eGhsXCc1tZWnZIEKzdQJQSXgKxfX18RCM3Z5eWlcfVAxKOjo+Pj49PTU88lTOk2NjbMsePc3t6SAfcgFdszOyMuAdeBg0CQi2lhYUHOeOLDCisN8FzcPFZXV3t7ezHY3t5GQ+6it+2xMASsKhEEWKsmRLRBBUpPvpJ/TpFKFBwKYAiITmicsbYhdHfJAltqhUCVsCQhwslmeXmZxiBQT9c0Ar9E2O3v72sYSE0N1yQArkKy0kBMXLqlZqIZHR3t6empqqqSDcBdhXEJSJ/bUc3q6uq+vj629GB9fR1WsLW1NTs7u7S0RN2locMjIyOEm5ubQ7+4uJienk4/+vv77Y1hwhLBEKhwWHitdVFfX9/Y2Gg2HuLi4owUAysrK8yCG97rh0+ApP5Q2ZycHFlPTExUVFRIBvn5+WhKSkp2dnaMKhptbW2426GgQ/rwuAQCZ1hwFayLiork9hMFBQV1dXVmE0BLS4vqw3QFB8kn4IAxoGPkYpxi4FeDmpqas7Mz4pClAgqGwD48rjY2NmacYqC0tJQ1KSlJWyE5OZkpUKkBAxZVIntAoZh04+Q48fHxPNGBgYHExMT29naj9cBodnZ2mo3jlJWVMeW2OGQck4B1amqqoaGhqamJjx2lGxwcpL0mUgR8fJhsWqJtSkoKU2SbHHUDpkhPBujd8xuQG6PJRM/Pz09PT7O1NNnZ2Tw3fgZkXVhYKCUlUhBATP+hCVyKZGky17RV0g04laayslJ6hlVeFHB4eFhKaogGd0LxtmTgE+hbhKDnPjMzgw8E3qGL2tpaBWpubjYqj2BoaEj6rq4uNATRZ0ZwCbiL6gXEzINk5vCBQJ9rMD4+rkA8QNK036uDg4Py8vLu7m680KjIBNR3zBDoWQM1g98snyB+VSoRW8C/UwR81/SvhgNj9JOTkwwVERUdRBEI0BAdLRVERkhLS8vIyEDQlrsTPTU1lVFhKxARvZgUlFLbegCf4BvIsbi4mIg4E5EogIHhiKCMtU0WUFiVy06j5fAJIDdSBDQw+PegDfBRcbOPwH4F9LuFWIIQdQNKwWqzIE0aoFUaBsw+SQuFw0uNtC9A+F4i3QNrbg3IDn+SAsHh+wYiEpeyBEMLv/cAO6KzAijxxB+Y4wisBhssJUhjEbPJf4Nw+B+JXqLW3bw+wQAAAABJRU5ErkJggg==";

	public static String faviconURLTeaVM() {
		return faviconURLString.substring(0);
	}

	@JSBody(params = { "doc", "str" }, script = "doc.write(str);doc.close();")
	private static native void documentWrite(HTMLDocument doc, String str);

	public static void openCreditsPopup(String text) {
		Window currentWin = Window.current();
		
		int w = (int)(850 * currentWin.getDevicePixelRatio());
		int h = (int)(700 * currentWin.getDevicePixelRatio());
		
		int x = (currentWin.getScreen().getWidth() - w) / 2;
		int y = (currentWin.getScreen().getHeight() - h) / 2;
		
		Window newWin = Window.current().open("", "_blank", "top=" + y + ",left=" + x + ",width=" + w + ",height=" + h + ",menubar=0,status=0,titlebar=0,toolbar=0");
		if(newWin == null) {
			Window.alert("ERROR: Popup blocked!\n\nPlease make sure you have popups enabled for this site!");
			return;
		}

		newWin.focus();
		documentWrite(newWin.getDocument(), "<!DOCTYPE html><html><head><meta charset=\"UTF-8\" />"
				+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" /><title>EaglercraftX 1.8 Credits</title>"
				+ "<link type=\"image/png\" rel=\"shortcut icon\" href=\"" + PlatformApplication.faviconURLTeaVM() + "\" />"
						+ "</head><body><pre style=\"font:15px Consolas,monospace;\">" + text + "</pre></body></html>");
	}

	public static void clearFileChooserResult() {
		fileChooserHasResult = false;
		fileChooserResultObject = null;
	}

	@JSBody(params = { "name", "buf" }, script =
			"var hr = window.URL.createObjectURL(new Blob([buf], {type: \"octet/stream\"}));" +
			"var a = document.createElement(\"a\");" +
			"a.href = hr; a.download = name; a.click();" +
			"window.URL.revokeObjectURL(hr);")
	private static final native void downloadBytesImpl(String str, ArrayBuffer buf);

	public static final void downloadFileWithName(String str, byte[] dat) {
		downloadBytesImpl(str, TeaVMUtils.unwrapArrayBuffer(dat));
	}

	public static void showDebugConsole() {
		DebugConsoleWindow.showDebugConsole();
	}

	public static void addLogMessage(String text, boolean err) {
		DebugConsoleWindow.addLogMessage(text, err);
	}

	public static boolean isShowingDebugConsole() {
		return DebugConsoleWindow.isShowingDebugConsole();
	}
}
