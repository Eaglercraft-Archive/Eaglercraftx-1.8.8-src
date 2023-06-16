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
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.dom.xml.Document;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Int8Array;

import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.MainClass;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info)
 * 
 */
public class PlatformApplication {

	public static void openLink(String url) {
		Window.current().open(url, "_blank");
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
	
	@JSBody(params = { "cb" }, script = "if(!window.navigator.clipboard) cb(\"\"); else window.navigator.clipboard.readText().then(function(s) { cb(s); }, function(s) { cb(\"\"); });")
	private static native void getClipboard1(StupidFunctionResolveString cb);
	
	@JSBody(params = { "str" }, script = "if(window.navigator.clipboard) window.navigator.clipboard.writeText(str);")
	private static native void setClipboard0(String str);
	
	public static void setLocalStorage(String name, byte[] data) {
		try {
			Storage s = Window.current().getLocalStorage();
			if(s != null) {
				s.setItem("_eaglercraftX." + name, Base64.encodeBase64String(data));
			}
		}catch(Throwable t) {
		}
	}
	
	public static byte[] getLocalStorage(String name) {
		try {
			Storage s = Window.current().getLocalStorage();
			if(s != null) {
				String str = s.getItem("_eaglercraftX." + name);
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
	}
	
	private static final DateFormat dateFormatSS = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
	
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
				Int8Array typedArray = Int8Array.create(buffer);
				byte[] bytes = new byte[typedArray.getByteLength()];
				for(int i = 0; i < bytes.length; ++i) {
					bytes[i] = typedArray.get(i);
				}
				fileChooserResultObject = new FileChooserResult(name, bytes);
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

	@JSBody(params = { "doc", "str" }, script = "doc.write(str);")
	private static native void documentWrite(Document doc, String str);

	public static void openCreditsPopup(String text) {
		Window currentWin = Window.current();
		
		int w = (int)(850 * currentWin.getDevicePixelRatio());
		int h = (int)(700 * currentWin.getDevicePixelRatio());
		
		int x = (currentWin.getScreen().getWidth() - w) / 2;
		int y = (currentWin.getScreen().getHeight() - h) / 2;
		
		Window newWin = Window.current().open("", "_blank", "top=" + y + ",left=" + x + ",width=" + w + ",height=" + h + ",menubar=0,status=0,titlebar=0,toolbar=0");
		
		newWin.focus();
		documentWrite(newWin.getDocument(), "<html><head><title>EaglercraftX 1.8 Credits</title></head><body><pre>" + text + "</pre></body></html>");
	}

}
