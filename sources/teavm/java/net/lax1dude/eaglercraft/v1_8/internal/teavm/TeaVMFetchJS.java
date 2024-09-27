package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBuffer;

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
public class TeaVMFetchJS {

	@JSFunctor
	public static interface FetchHandler extends JSObject {
		void onFetch(ArrayBuffer data);
	}

	@JSBody(params = { }, script = "return (typeof fetch === \"function\");")
	public static native boolean checkFetchSupport();

	@JSBody(params = { "uri", "forceCache", "callback" }, script = "fetch(uri, { cache: forceCache, mode: \"cors\" })"
			+ ".then(function(res) { return res.arrayBuffer(); }).then(function(arr) { callback(arr); })"
			+ ".catch(function(err) { console.error(err); callback(null); });")
	public static native void doFetchDownload(String uri, String forceCache, FetchHandler callback);

	@JSBody(params = { "uri", "callback" }, script = "var eag = function(xhrObj){xhrObj.responseType = \"arraybuffer\";"
			+ "xhrObj.addEventListener(\"load\", function(evt) { var stat = xhrObj.status; if(stat === 0 || (stat >= 200 && stat < 400)) { callback(xhrObj.response); } else { callback(null); } });"
			+ "xhrObj.addEventListener(\"error\", function(evt) { callback(null); });"
			+ "xhrObj.open(\"GET\", uri, true); xhrObj.send();}; eag(new XMLHttpRequest());")
	public static native void doXHRDownload(String uri, FetchHandler callback);

}
