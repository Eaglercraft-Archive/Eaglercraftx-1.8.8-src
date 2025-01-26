package net.lax1dude.eaglercraft.v1_8.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.teavm.interop.Address;
import org.teavm.interop.Import;
import org.teavm.interop.Unmanaged;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Uint8Array;
import org.teavm.jso.typedarrays.Uint8ClampedArray;

import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.MemoryStack;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCBufferAllocator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCDirectArrayConverter;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.BetterJSStringConverter;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.ClientMain;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.EPKLoader;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

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
public class PlatformAssets {

	static final Logger logger = LogManager.getLogger("PlatformAssets");

	private static final byte[] MISSING_FILE = new byte[0];

	static Map<String,byte[]> assets = new HashMap<>();

	public static boolean getResourceExists(String path) {
		if(path.startsWith("/")) {
			path = path.substring(1);
		}
		byte[] ret = assets.get(path);
		if(ret != null && ret != MISSING_FILE) {
			return true;
		}else {
			if(path.startsWith("assets/minecraft/lang/") && !path.endsWith(".mcmeta")) {
				byte[] file = PlatformRuntime.downloadRemoteURIByteArray(
						ClientMain.configLocalesFolder + "/" + path.substring(22));
				if(file != null) {
					assets.put(path, file);
					return true;
				}else {
					assets.put(path, MISSING_FILE);
					return false;
				}
			}else {
				return false;
			}
		}
	}
	
	public static byte[] getResourceBytes(String path) {
		if(path.startsWith("/")) {
			path = path.substring(1);
		}
		byte[] data = assets.get(path);
		if(data == null && path.startsWith("assets/minecraft/lang/") && !path.endsWith(".mcmeta")) {
			byte[] file = PlatformRuntime.downloadRemoteURIByteArray(
					ClientMain.configLocalesFolder + "/" + path.substring(22));
			if(file != null) {
				assets.put(path, file);
				return data;
			}else {
				assets.put(path, MISSING_FILE);
				return null;
			}
		}else {
			return data == MISSING_FILE ? null : data;
		}
	}

	static void readAssetsTeaVM() {
		if(!assets.isEmpty()) {
			assets = new HashMap<>();
		}

		int epkCount = getEPKFileCount();

		logger.info("Reading {} EPK files", epkCount);

		for(int i = 0; i < epkCount; ++i) {
			JSEPKFileEntry etr = getEPKFileData(i);
			String name = etr.getName();
			String path = etr.getPath();
			Uint8Array data = etr.getData();
			int dataLen = data.getLength();
			
			logger.info("Reading: \"{}\" @ {}", name, path.startsWith("/") ? path : ("/" + path));
			
			ByteBuffer buf = PlatformRuntime.allocateByteBuffer(dataLen);
			try {
				WASMGCBufferAllocator.getUnsignedByteBufferView(buf).set(data);
				EPKLoader.loadEPK(buf, path, assets);
			}catch(IOException e) {
				logger.error("Failed to load the EPK file!");
				logger.error(e);
				throw new RuntimeInitializationFailureException("Failed to read EPK file \"" + name + "\"!");
			}finally {
				PlatformRuntime.freeByteBuffer(buf);
			}
		}

		logger.info("Loaded {} assets from EPK(s)", assets.size());
	}

	private interface JSEPKFileEntry extends JSObject {
		
		@JSProperty
		String getName();
		
		@JSProperty
		String getPath();
		
		@JSProperty
		Uint8Array getData();
		
	}

	@Import(module = "platformAssets", name = "getEPKFileData")
	private static native JSEPKFileEntry getEPKFileData(int idx);

	@Import(module = "platformAssets", name = "getEPKFileCount")
	private static native int getEPKFileCount();

	public static ImageData loadImageFile(InputStream data) {
		return loadImageFile(data, "image/png");
	}

	public static ImageData loadImageFile(InputStream data, String mime) {
		byte[] b = EaglerInputStream.inputStreamToBytesQuiet(data);
		if(b != null) {
			return loadImageFile(b, mime);
		}else {
			return null;
		}
	}

	public static ImageData loadImageFile(byte[] data) {
		return loadImageFile(data, "image/png");
	}

	public static ImageData loadImageFile(byte[] data, String mime) {
		JSImageLoadResult asyncResult;
		MemoryStack.push();
		try {
			asyncResult = loadImageFile0(WASMGCDirectArrayConverter.byteArrayToStackU8Array(data),
					BetterJSStringConverter.stringToJS(mime));
		}finally {
			MemoryStack.pop();
		}

		if(asyncResult == null) {
			return null;
		}

		int w = asyncResult.getWidth();
		int h = asyncResult.getHeight();
		int len = w * h;
		int len2 = len << 2;
		
		MemoryStack.push();
		try {
			Address dataDest = MemoryStack.malloc(len2);
			loadImageFile1(asyncResult, WASMGCBufferAllocator.getUnsignedClampedByteBufferView0(dataDest, len2));
			int[] pixelsArray = new int[len];
			copyPixelArrayFast(pixelsArray, dataDest, len2);
			return new ImageData(w, h, pixelsArray, true);
		}finally {
			MemoryStack.pop();
		}
	}

	private interface JSImageLoadResult extends JSObject {

		@JSProperty
		int getWidth();

		@JSProperty
		int getHeight();

	}

	@Import(module = "platformAssets", name = "loadImageFile0")
	private static native JSImageLoadResult loadImageFile0(Uint8Array bufferData, JSString mime);

	@Import(module = "platformAssets", name = "loadImageFile1")
	private static native void loadImageFile1(JSImageLoadResult imageLoadResult, Uint8ClampedArray dataDest);

	@Unmanaged
	private static void copyPixelArrayFast(int[] pixelsArray, Address addr, int count) {
		Address addrEnd = addr.add(count);
		int dstOffset = 0;
		while(addr.isLessThan(addrEnd)) {
			pixelsArray[dstOffset] = addr.getInt();
			++dstOffset;
			addr = addr.add(4);
		}
	}

}
