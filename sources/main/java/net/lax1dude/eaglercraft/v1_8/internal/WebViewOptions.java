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

package net.lax1dude.eaglercraft.v1_8.internal;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

public class WebViewOptions {

	public EnumWebViewContentMode contentMode = EnumWebViewContentMode.BLOB_BASED;
	public String fallbackTitle = "WebView";
	public boolean scriptEnabled = false;
	public boolean strictCSPEnable = true;
	public boolean serverMessageAPIEnabled = false;
	public URI url = null;
	public byte[] blob = null;
	public EaglercraftUUID permissionsOriginUUID = null;

	public WebViewOptions() {
	}

	public WebViewOptions(boolean script, boolean serverMessageAPIEnabled, boolean strictCSPEnable, URI url) {
		this.contentMode = EnumWebViewContentMode.URL_BASED;
		this.scriptEnabled = script;
		this.strictCSPEnable = strictCSPEnable;
		this.serverMessageAPIEnabled = serverMessageAPIEnabled;
		this.url = url;
		this.permissionsOriginUUID = getURLOriginUUID(url);
	}

	public WebViewOptions(boolean script, boolean serverMessageAPIEnabled, boolean strictCSPEnable, byte[] data, EaglercraftUUID permissionsOriginUUID) {
		this.contentMode = EnumWebViewContentMode.BLOB_BASED;
		this.scriptEnabled = script;
		this.strictCSPEnable = strictCSPEnable;
		this.serverMessageAPIEnabled = serverMessageAPIEnabled;
		this.blob = data;
		this.permissionsOriginUUID = permissionsOriginUUID;
	}

	public static EaglercraftUUID getURLOriginUUID(URI url) {
		return EaglercraftUUID.nameUUIDFromBytes(("URLOrigin:" + url.toString()).getBytes(StandardCharsets.UTF_8));
	}

	public static EaglercraftUUID getEmbedOriginUUID(byte[] sha256) {
		byte[] vigg = "BlobOrigin:".getBytes(StandardCharsets.UTF_8);
		byte[] eagler = new byte[sha256.length + vigg.length];
		System.arraycopy(vigg, 0, eagler, 0, vigg.length);
		System.arraycopy(sha256, 0, eagler, vigg.length, sha256.length);
		return EaglercraftUUID.nameUUIDFromBytes(eagler);
	}

}