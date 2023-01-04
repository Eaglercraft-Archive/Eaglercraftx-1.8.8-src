package net.lax1dude.eaglercraft.v1_8.internal;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLImageElement;
import org.teavm.jso.dom.xml.Document;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.DataView;
import org.teavm.jso.typedarrays.Uint8Array;
import org.teavm.jso.typedarrays.Uint8ClampedArray;

import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.MainClass;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

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
public class PlatformAssets {
	
	private static final byte[] MISSING_FILE = new byte[0];
	
	static final Map<String,byte[]> assets = new HashMap();
	
	public static final byte[] getResourceBytes(String path) {
		if(path.startsWith("/")) {
			path = path.substring(1);
		}
		byte[] data = assets.get(path);
		if(data == null && path.startsWith("assets/minecraft/lang/") && !path.endsWith(".mcmeta")) {
			ArrayBuffer file = PlatformRuntime.downloadRemoteURI(
					MainClass.configLocalesFolder + "/" + path.substring(22));
			if(file != null && file.getByteLength() > 0) {
				data = TeaVMUtils.arrayBufferToBytes(file);
				assets.put(path, data);
				return data;
			}else {
				assets.put(path, MISSING_FILE);
				return null;
			}
		}else {
			return data == MISSING_FILE ? null : data;
		}
	}
	
	public static final ImageData loadImageFile(InputStream data) {
		byte[] b = EaglerInputStream.inputStreamToBytesQuiet(data);
		if(b != null) {
			return loadImageFile(b);
		}else {
			return null;
		}
	}
	
	private static HTMLCanvasElement imageLoadCanvas = null;
	private static CanvasRenderingContext2D imageLoadContext = null;
	
	public static ImageData loadImageFile(byte[] data) {
		Uint8Array buf = Uint8Array.create(data.length);
		buf.set(data);
		return loadImageFile(buf.getBuffer());
	}
	
	@JSBody(params = { }, script = "return { willReadFrequently: true };")
	public static native JSObject youEagler();
	
	@Async
	private static native ImageData loadImageFile(ArrayBuffer data);
	
	private static void loadImageFile(ArrayBuffer data, final AsyncCallback<ImageData> ret) {
		final Document doc = Window.current().getDocument();
		final HTMLImageElement toLoad = (HTMLImageElement) doc.createElement("img");
		toLoad.addEventListener("load", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				if(imageLoadCanvas == null) {
					imageLoadCanvas = (HTMLCanvasElement) doc.createElement("canvas");
				}
				if(imageLoadCanvas.getWidth() < toLoad.getWidth()) {
					imageLoadCanvas.setWidth(toLoad.getWidth());
				}
				if(imageLoadCanvas.getHeight() < toLoad.getHeight()) {
					imageLoadCanvas.setHeight(toLoad.getHeight());
				}
				if(imageLoadContext == null) {
					imageLoadContext = (CanvasRenderingContext2D) imageLoadCanvas.getContext("2d", youEagler());
				}
				imageLoadContext.clearRect(0, 0, toLoad.getWidth(), toLoad.getHeight());
				imageLoadContext.drawImage(toLoad, 0, 0, toLoad.getWidth(), toLoad.getHeight());
				org.teavm.jso.canvas.ImageData pxlsDat = imageLoadContext.getImageData(0, 0, toLoad.getWidth(), toLoad.getHeight());
				Uint8ClampedArray pxls = pxlsDat.getData();
				int totalPixels = pxlsDat.getWidth() * pxlsDat.getHeight();
				TeaVMUtils.freeDataURL(toLoad.getSrc());
				if(pxls.getByteLength() < totalPixels << 2) {
					ret.complete(null);
					return;
				}
				DataView view = DataView.create(pxls.getBuffer());
				int[] pixels = new int[totalPixels];
				for(int i = 0; i < pixels.length; ++i) {
					pixels[i] = view.getUint32(i << 2, true);
				}
				ret.complete(new ImageData(pxlsDat.getWidth(), pxlsDat.getHeight(), pixels, true));
			}
		});
		toLoad.addEventListener("error", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				TeaVMUtils.freeDataURL(toLoad.getSrc());
				ret.complete(null);
			}
		});
		String src = TeaVMUtils.getDataURL(data, "image/png");
		if(src != null) {
			toLoad.setSrc(src);
		}else {
			ret.complete(null);
		}
	}
	
}
