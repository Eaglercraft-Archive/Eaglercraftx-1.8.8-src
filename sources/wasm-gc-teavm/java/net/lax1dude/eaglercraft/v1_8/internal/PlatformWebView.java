package net.lax1dude.eaglercraft.v1_8.internal;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.teavm.interop.Import;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime.JSEagRuntimeEvent;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCBufferAllocator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCDirectArrayConverter;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.BetterJSStringConverter;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketWebViewMessageEnV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketWebViewMessageV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketWebViewMessageV4EAG;
import net.lax1dude.eaglercraft.v1_8.webview.PermissionsCache;
import net.lax1dude.eaglercraft.v1_8.webview.WebViewOverlayController.IPacketSendCallback;

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
public class PlatformWebView {

	private static final Logger logger = LogManager.getLogger("PlatformWebView");

	private static String currentMessageChannelName = null;

	private static IPacketSendCallback packetSendCallback = null;

	private static boolean supportedState = false;
	private static boolean showingState = false;
	private static boolean supportForce = false;
	private static boolean enableCSP = true;
	private static boolean cspSupport = false;

	private static final int EVENT_CHANNEL_OPEN = 0;
	private static final int EVENT_CHANNEL_CLOSE = 1;

	private interface JSWebViewChannelEvent extends JSObject {

		@JSProperty
		int getEventType();

		@JSProperty
		JSString getChannelName();

	}

	private static final int EVENT_MESSAGE_STRING = 0;
	private static final int EVENT_MESSAGE_BINARY = 1;

	private interface JSWebViewMessageEvent extends JSObject {

		@JSProperty
		int getEventType();

		@JSProperty
		JSString getChannelName();

		@JSProperty("eventData")
		JSString getEventDataString();

		@JSProperty("eventData")
		ArrayBuffer getEventDataBinary();

	}

	private static final int EVENT_WEBVIEW_CHANNEL = 0;
	private static final int EVENT_WEBVIEW_MESSAGE = 1;
	private static final int EVENT_WEBVIEW_PERMISSION_ALLOW = 2;
	private static final int EVENT_WEBVIEW_PERMISSION_BLOCK = 3;
	private static final int EVENT_WEBVIEW_PERMISSION_CLEAR = 4;

	private static final List<Runnable> messageQueue = new LinkedList<>();

	private static Runnable setJavaScriptAllowedCurrent = null;
	private static Runnable setJavaScriptBlockedCurrent = null;
	private static Runnable setJavaScriptClearedCurrent = null;

	private static int webviewResetSerial = 0;

	@Import(module = "platformWebView", name = "checkSupported")
	private static native boolean checkSupported();

	@Import(module = "platformWebView", name = "checkCSPSupported")
	private static native boolean checkCSPSupported();

	static void initialize() {
		currentMessageChannelName = null;
		packetSendCallback = null;
		setJavaScriptAllowedCurrent = null;
		setJavaScriptBlockedCurrent = null;
		setJavaScriptClearedCurrent = null;
		supportedState = checkSupported();
		cspSupport = checkCSPSupported();
		if(!supportedState) {
			logger.error("This browser does not meet the safety requirements for webview support, this feature will be disabled");
		}else if(!cspSupport) {
			logger.warn("This browser does not support CSP attribute on iframes! (try Chrome)");
		}
	}

	public static boolean supported() {
		return supportedState;
	}

	public static boolean isShowing() {
		return showingState;
	}

	public static void setPacketSendCallback(IPacketSendCallback callback) {
		packetSendCallback = callback;
	}

	public static void handleJSEvent(JSEagRuntimeEvent evt) {
		switch(evt.getEventType() & 31) {
			case EVENT_WEBVIEW_CHANNEL: {
				JSWebViewChannelEvent obj = evt.getEventObj();
				final String channel = BetterJSStringConverter.stringFromJS(obj.getChannelName());
				switch(obj.getEventType()) {
					case EVENT_CHANNEL_OPEN: {
						messageQueue.add(() -> {
							sendMessageEnToServer(true, channel);
						});
						break;
					}
					case EVENT_CHANNEL_CLOSE: {
						messageQueue.add(() -> {
							sendMessageEnToServer(false, channel);
						});
						break;
					}
				}
				break;
			}
			case EVENT_WEBVIEW_MESSAGE: {
				JSWebViewMessageEvent obj = evt.getEventObj();
				final String channel = BetterJSStringConverter.stringFromJS(obj.getChannelName());
				switch(obj.getEventType()) {
					case EVENT_MESSAGE_STRING: {
						final String data = BetterJSStringConverter.stringFromJS(obj.getEventDataString());
						messageQueue.add(() -> {
							sendMessageToServer(channel, CPacketWebViewMessageV4EAG.TYPE_STRING, data.getBytes(StandardCharsets.UTF_8));
						});
						break;
					}
					case EVENT_MESSAGE_BINARY: {
						final byte[] dataArr = WASMGCDirectArrayConverter
								.externU8ArrayToByteArray(new Uint8Array(obj.getEventDataBinary()));
						messageQueue.add(() -> {
							sendMessageToServer(channel, CPacketWebViewMessageV4EAG.TYPE_BINARY, dataArr);
						});
						break;
					}
				}
				break;
			}
			case EVENT_WEBVIEW_PERMISSION_ALLOW: {
				if(setJavaScriptAllowedCurrent != null) {
					messageQueue.add(setJavaScriptAllowedCurrent);
				}
				break;
			}
			case EVENT_WEBVIEW_PERMISSION_BLOCK: {
				if(setJavaScriptBlockedCurrent != null) {
					messageQueue.add(setJavaScriptBlockedCurrent);
				}
				break;
			}
			case EVENT_WEBVIEW_PERMISSION_CLEAR: {
				if(setJavaScriptClearedCurrent != null) {
					messageQueue.add(setJavaScriptClearedCurrent);
				}
				break;
			}
		}
	}

	public static void runTick() {
		if(!showingState) {
			return;
		}
		List<Runnable> lst = null;
		if(messageQueue.isEmpty()) {
			return;
		}
		lst = new ArrayList<>(messageQueue);
		messageQueue.clear();
		for(int i = 0, l = lst.size(); i < l; ++i) {
			try {
				lst.get(i).run();
			}catch(Throwable t) {
				logger.error("Caught exception processing webview message!");
				logger.error(t);
			}
		}
	}

	public static void handleMessageFromServer(SPacketWebViewMessageV4EAG packet) {
		if(showingState && currentMessageChannelName != null) {
			if(packet.type == SPacketWebViewMessageV4EAG.TYPE_STRING) {
				sendStringMessage(BetterJSStringConverter.stringToJS(currentMessageChannelName),
						BetterJSStringConverter.stringToJS(new String(packet.data, StandardCharsets.UTF_8)));
			}else if(packet.type == SPacketWebViewMessageV4EAG.TYPE_BINARY) {
				ByteBuffer buf = WASMGCDirectArrayConverter.byteArrayToBuffer(packet.data);
				try {
					sendBinaryMessage(BetterJSStringConverter.stringToJS(currentMessageChannelName),
							WASMGCBufferAllocator.getUnsignedByteBufferView(buf));
				}finally {
					PlatformRuntime.freeByteBuffer(buf);
				}
			}
		}else {
			logger.error("Server tried to send the WebView a message, but the message channel is not open!");
		}
	}

	@Import(module = "platformWebView", name = "sendStringMessage")
	private static native void sendStringMessage(JSString ch, JSString str);

	@Import(module = "platformWebView", name = "sendBinaryMessage")
	private static native void sendBinaryMessage(JSString ch, Uint8Array bin);

	private static void sendMessageToServer(String channelName, int type, byte[] data) {
		if(channelName.length() > 255) {
			logger.error("WebView tried to send a message packet, but channel name is too long, max is 255 characters!");
			return;
		}
		if(!channelName.equals(currentMessageChannelName)) {
			logger.error("WebView tried to send a message packet, but the channel is not open!");
			return;
		}
		if(packetSendCallback != null) {
			if(!packetSendCallback.sendPacket(new CPacketWebViewMessageV4EAG(type, data))) {
				logger.error("WebView tried to send a packet to the server, but the server does not support this protocol!");
			}
		}else {
			logger.error("WebView tried to send a message, but no callback for sending packets is set!");
		}
	}

	private static void sendMessageEnToServer(boolean messageChannelOpen, String channelName) {
		if(channelName.length() > 255) {
			logger.error("WebView tried to {} a channel, but channel name is too long, max is 255 characters!", messageChannelOpen ? "open" : "close");
			return;
		}
		if(messageChannelOpen && currentMessageChannelName != null) {
			logger.error("WebView tried to open channel, but a channel is already open!");
			sendMessageEnToServer(false, currentMessageChannelName);
		}
		if(!messageChannelOpen && currentMessageChannelName != null && !currentMessageChannelName.equals(channelName)) {
			logger.error("WebView tried to close the wrong channel!");
		}
		if(!messageChannelOpen && currentMessageChannelName == null) {
			logger.error("WebView tried to close channel, but the channel is not open!");
			return;
		}
		if(packetSendCallback != null) {
			if(!packetSendCallback.sendPacket(new CPacketWebViewMessageEnV4EAG(messageChannelOpen, messageChannelOpen ? channelName : null))) {
				logger.error("WebView tried to send a packet to the server, but the server does not support this protocol!");
				return;
			}
			if(messageChannelOpen) {
				logger.info("WebView opened message channel to server: \"{}\"", channelName);
				currentMessageChannelName = channelName;
			}else {
				logger.info("WebView closed message channel to server: \"{}\"", currentMessageChannelName);
				currentMessageChannelName = null;
			}
		}else {
			logger.error("WebView tried to send a message, but no callback for sending packets is set!");
		}
	}

	private interface JSWebViewOptions extends JSObject {
		
		@JSProperty("uri")
		void setURI(JSString title);
		
		@JSProperty
		void setBlob(Uint8Array data);
		
	}

	@JSBody(params = { "a", "b", "c", "d", "e" }, script = "return { contentMode: a, fallbackTitle: b, "
			+ "scriptEnabled: c, strictCSPEnable: d, serverMessageAPIEnabled: e};")
	private static native JSWebViewOptions makeOptions(int contentMode, JSString fallbackTitle, boolean scriptEnabled,
			boolean strictCSPEnable, boolean serverMessageAPIEnabled);

	private static int hashPermissionFlags(WebViewOptions opts) {
		int i = (opts.scriptEnabled ? 1 : 0);
		i |= ((enableCSP && cspSupport && opts.strictCSPEnable) ? 0 : 2);
		i |= (opts.serverMessageAPIEnabled ? 4 : 0);
		return i;
	}

	public static void beginShowing(final WebViewOptions options, int x, int y, int w, int h) {
		if(!supported()) {
			return;
		}
		if(showingState) {
			endShowing();
		}
		showingState = true;
		++webviewResetSerial;
		
		messageQueue.clear();
		
		int state = BEGIN_SHOWING_DIRECT;
		if(options.scriptEnabled) {
			PermissionsCache.Permission perm = PermissionsCache.getJavaScriptAllowed(options.permissionsOriginUUID, hashPermissionFlags(options));
			if(perm == null) {
				state = BEGIN_SHOWING_ENABLE_JAVASCRIPT;
			}else if(!perm.choice) {
				state = BEGIN_SHOWING_CONTENT_BLOCKED;
			}
		}
		
		boolean isBlob = options.contentMode == EnumWebViewContentMode.BLOB_BASED;
		JSWebViewOptions opts = makeOptions(isBlob ? 1 : 0, BetterJSStringConverter.stringToJS(options.fallbackTitle),
				options.scriptEnabled, options.strictCSPEnable, options.serverMessageAPIEnabled);
		if(isBlob) {
			ByteBuffer buf = WASMGCDirectArrayConverter.byteArrayToBuffer(options.blob);
			try {
				opts.setBlob(WASMGCBufferAllocator.getUnsignedByteBufferView(buf));
				beginShowing0(state, opts, x, y, w, h);
			}finally {
				PlatformRuntime.freeByteBuffer(buf);
			}
		}else {
			opts.setURI(BetterJSStringConverter.stringToJS(options.url.toString()));
			beginShowing0(state, opts, x, y, w, h);
		}
		
		final int serial = webviewResetSerial;
		setJavaScriptAllowedCurrent = () -> {
			if(serial == webviewResetSerial) {
				PermissionsCache.setJavaScriptAllowed(options.permissionsOriginUUID, hashPermissionFlags(options), true);
			}
		};
		setJavaScriptBlockedCurrent = () -> {
			if(serial == webviewResetSerial) {
				PermissionsCache.setJavaScriptAllowed(options.permissionsOriginUUID, hashPermissionFlags(options), false);
			}
		};
		setJavaScriptClearedCurrent = () -> {
			if(serial == webviewResetSerial) {
				PermissionsCache.clearJavaScriptAllowed(options.permissionsOriginUUID);
			}
		};
	}

	private static final int BEGIN_SHOWING_DIRECT = 0;
	private static final int BEGIN_SHOWING_ENABLE_JAVASCRIPT = 1;
	private static final int BEGIN_SHOWING_CONTENT_BLOCKED = 2;

	@Import(module = "platformWebView", name = "beginShowing")
	private static native void beginShowing0(int state, JSWebViewOptions options, int x, int y, int w, int h);

	@Import(module = "platformWebView", name = "resize")
	public static native void resize(int x, int y, int w, int h);

	public static void endShowing() {
		if(!supported()) {
			return;
		}
		++webviewResetSerial;
		if(showingState) {
			showingState = false;
			endShowing0();
		}
		if(currentMessageChannelName != null) {
			sendMessageEnToServer(false, currentMessageChannelName);
		}
		messageQueue.clear();
		setJavaScriptAllowedCurrent = null;
		setJavaScriptBlockedCurrent = null;
		setJavaScriptClearedCurrent = null;
	}

	@Import(module = "platformWebView", name = "endShowing")
	private static native void endShowing0();

	public static boolean fallbackSupported() {
		return false;
	}

	public static void launchFallback(WebViewOptions options) {
		
	}

	public static boolean fallbackRunning() {
		return false;
	}

	public static String getFallbackURL() {
		return null;
	}

	public static void endFallbackServer() {
		
	}

}
