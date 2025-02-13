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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.browser.Window;

import net.lax1dude.eaglercraft.v1_8.Base64;

public class TeaVMDataURLManager {

	private static void checkDataURLSupport0(boolean fetchBased, final AsyncCallback<Boolean> callback) {
		final byte[] testData = new byte[1024];
		for(int i = 0; i < 1024; ++i) {
			testData[i] = (byte)i;
		}
		String testURL = "data:application/octet-stream;base64," + Base64.encodeBase64String(testData);
		TeaVMFetchJS.FetchHandler cb = (data) -> {
			if(data != null && TeaVMUtils.isTruthy(data) && data.getByteLength() == 1024) {
				byte[] bb = TeaVMUtils.wrapByteArrayBuffer(data);
				callback.complete(Arrays.equals(bb, testData));
			}else {
				callback.complete(false);
			}
		};
		try {
			if(fetchBased) {
				TeaVMFetchJS.doFetchDownload(testURL, "force-cache", cb);
			}else {
				TeaVMFetchJS.doXHRDownload(testURL, cb);
			}
		}catch(Throwable t) {
			callback.complete(false);
		}
	}

	@Async
	private static native Boolean checkDataURLSupport0(boolean fetchBased);

	public static boolean checkDataURLSupport(boolean fetchBased) {
		Boolean b = null;
		try {
			b = checkDataURLSupport0(fetchBased);
		}catch(Throwable t) {
		}
		return b != null && b.booleanValue();
	}

	public static byte[] decodeDataURLFallback(String dataURL) {
		if(dataURL.length() < 6 || !dataURL.substring(0, 5).equalsIgnoreCase("data:")) {
			return null;
		}
		int i = dataURL.indexOf(',');
		if(i == -1 || i >= dataURL.length() - 1) {
			return null;
		}
		String mime = dataURL.substring(0, i).toLowerCase();
		String str = dataURL.substring(i + 1);
		try {
			if(mime.endsWith(";base64")) {
				return Base64.decodeBase64(str);
			}else {
				return Window.decodeURIComponent(str).getBytes(StandardCharsets.UTF_8);
			}
		}catch(Throwable t) {
			return null;
		}
	}

}