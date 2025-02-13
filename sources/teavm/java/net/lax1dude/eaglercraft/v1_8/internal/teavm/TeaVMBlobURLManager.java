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

package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBuffer;

import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

public class TeaVMBlobURLManager {

	private static final Logger logger = LogManager.getLogger("TeaVMBlobURLManager");

	private static boolean isSameOriginSupport = true;

	public static void initialize() {
		if(((TeaVMClientConfigAdapter)PlatformRuntime.getClientConfigAdapter()).isDisableBlobURLsTeaVM()) {
			isSameOriginSupport = false;
			logger.info("Note: Blob urls have been disabled, client will use data: urls instead");
		}else {
			try {
				isSameOriginSupport = checkSameOriginSupport();
			}catch(Throwable t) {
				isSameOriginSupport = false;
			}
			if(!isSameOriginSupport) {
				logger.warn("Warning: Same-origin fetch support detected as false, client will use data: urls instead of blob: urls");
			}
		}
	}

	@Async
	private static native Boolean checkSameOriginSupport();

	private static void checkSameOriginSupport(final AsyncCallback<Boolean> cb) {
		try {
			checkSameOriginSupport0((v) -> cb.complete(v));
		}catch(Throwable t) {
			cb.error(t);
		}
	}

	@JSFunctor
	private static interface SameOriginSupportCallback extends JSObject {
		void call(boolean support);
	}

	@JSBody(params = { "cb" }, script = "if((typeof URL === \"undefined\") || (typeof URL.createObjectURL !== \"function\")) { cb(false); }"
			+ "else { var objURL = URL.createObjectURL(new Blob([new Uint8Array([69, 69, 69, 69])]));"
			+ "if(!objURL) { cb(false); return; }"
			+ "var eag = function(theObjURL, theXHRObj) {"
			+ "theXHRObj.responseType = \"arraybuffer\";"
			+ "theXHRObj.addEventListener(\"load\", function(evt) { try { URL.revokeObjectURL(theObjURL); } catch(exx) { }"
			+ "var stat = theXHRObj.status;"
			+ "if(stat === 0 || (stat >= 200 && stat < 400)) {"
			+ "var typedArr = new Uint8Array(theXHRObj.response);"
			+ "if(typedArr.length === 4 && typedArr[0] === 69 && typedArr[1] === 69 && typedArr[2] === 69 && typedArr[3] === 69) {"
			+ "cb(true);"
			+ "} else { cb(false); } } else { cb(false); } });"
			+ "theXHRObj.addEventListener(\"error\", function(evt) { try { URL.revokeObjectURL(theObjURL); } catch(exx) { }  cb(false); });"
			+ "theXHRObj.open(\"GET\", theObjURL, true);"
			+ "theXHRObj.send();"
			+ "}; eag(objURL, new XMLHttpRequest()); }")
	private static native void checkSameOriginSupport0(SameOriginSupportCallback cb);

	private static class HandleRealBlobURL implements TeaVMBlobURLHandle {

		private final String blobURL;

		public HandleRealBlobURL(String blobURL) {
			this.blobURL = blobURL;
		}

		@Override
		public String toExternalForm() {
			return blobURL;
		}

		@Override
		public void release() {
			revokeBlobURL(blobURL);
		}

	}

	private static class HandleFakeBlobURL implements TeaVMBlobURLHandle {

		private final byte[] blobData;
		private final String blobMIME;

		public HandleFakeBlobURL(byte[] blobData, String blobMIME) {
			this.blobData = blobData;
			this.blobMIME = blobMIME;
		}

		@Override
		public String toExternalForm() {
			return "data:" + blobMIME + ";base64," + Base64.encodeBase64String(blobData);
		}

		@Override
		public void release() {
			
		}

	}

	public static TeaVMBlobURLHandle registerNewURLByte(byte[] objectData, String mimeType) {
		if(isSameOriginSupport) {
			return new HandleRealBlobURL(createBlobURL(TeaVMUtils.unwrapArrayBuffer(objectData), mimeType));
		}else {
			return new HandleFakeBlobURL(objectData, mimeType);
		}
	}

	public static TeaVMBlobURLHandle registerNewURLArrayBuffer(ArrayBuffer objectData, String mimeType) {
		return registerNewURLByte(TeaVMUtils.wrapByteArrayBuffer(objectData), mimeType);
	}

	public static TeaVMBlobURLHandle registerNewURLBlob(JSObject objectData) {
		if(isSameOriginSupport) {
			return new HandleRealBlobURL(createBlobURL(objectData));
		}else {
			return new HandleFakeBlobURL(TeaVMUtils.wrapByteArrayBuffer(blobToArrayBuffer(objectData)), getBlobMime(objectData));
		}
	}

	@JSBody(params = { "objectData" }, script = "return objectData.type || \"application/octet-stream\";")
	private static native String getBlobMime(JSObject objectData);

	@Async
	private static native ArrayBuffer blobToArrayBuffer(JSObject objectData);

	private static void blobToArrayBuffer(JSObject objectData, final AsyncCallback<ArrayBuffer> cb) {
		blobToArrayBuffer0(objectData, cb::complete);
	}

	@JSFunctor
	private static interface ArrayBufferCallback extends JSObject {
		void call(ArrayBuffer buf);
	}

	@JSBody(params = { "objectData", "callback" }, script = 
			"var eag = function(reader){"
			+ "reader.addEventListener(\"loadend\",function(evt){ callback(reader.result); });"
			+ "reader.addEventListener(\"error\",function(evt){ callback(null); });"
			+ "reader.readAsArrayBuffer(objectData);"
			+ "}; eag(new FileReader());")
	private static native ArrayBuffer blobToArrayBuffer0(JSObject objectData, ArrayBufferCallback callback);

	@JSBody(params = { "buf", "mime" }, script = "return URL.createObjectURL(new Blob([buf], {type: mime}));")
	private static native String createBlobURL(ArrayBuffer buf, String mime);

	@JSBody(params = { "objectBlob" }, script = "return URL.createObjectURL(objectBlob);")
	private static native String createBlobURL(JSObject objectBlob);

	@JSBody(params = { "url" }, script = "URL.revokeObjectURL(url);")
	private static native void revokeBlobURL(String url);

	public static String toExternalForm(TeaVMBlobURLHandle handle) {
		return handle.toExternalForm();
	}

	public static void releaseURL(TeaVMBlobURLHandle handle) {
		handle.release();
	}

}