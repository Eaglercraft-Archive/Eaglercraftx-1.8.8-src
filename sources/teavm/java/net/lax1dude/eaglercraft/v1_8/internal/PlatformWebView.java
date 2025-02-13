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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.css.CSSStyleDeclaration;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.MessageEvent;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.typedarrays.ArrayBuffer;

import net.lax1dude.eaglercraft.v1_8.internal.teavm.AdvancedHTMLIFrameElement;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.IFrameSafetyException;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketWebViewMessageEnV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketWebViewMessageV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketWebViewMessageV4EAG;
import net.lax1dude.eaglercraft.v1_8.webview.PermissionsCache;
import net.lax1dude.eaglercraft.v1_8.webview.WebViewOverlayController.IPacketSendCallback;

public class PlatformWebView {

	private static final Logger logger = LogManager.getLogger("PlatformWebView");

	private static boolean supportKnown = false;
	private static boolean supportForce = false;
	private static boolean enableCSP = true;
	private static boolean supported = false;
	private static boolean cspSupport = false;

	private static HTMLElement currentIFrameContainer = null;
	private static HTMLElement currentAllowJavaScript = null;

	private static AdvancedHTMLIFrameElement currentIFrame = null;
	private static WebViewOptions currentOptions = null;

	private static int webviewResetSerial = 0;

	private static String currentMessageChannelName = null;

	private static Window win;
	private static HTMLElement rootElement;

	private static Consumer<MessageEvent> currentMessageHandler = null;

	private static final List<Runnable> messageQueue = new LinkedList<>();

	private static IPacketSendCallback packetSendCallback = null;

	static void initRoot(Window win, HTMLElement rootElement) {
		PlatformWebView.win = win;
		PlatformWebView.rootElement = rootElement;
	}

	public static boolean supported() {
		if(!supportKnown) {
			IClientConfigAdapter cfg = PlatformRuntime.getClientConfigAdapter();
			supportForce = cfg.isForceWebViewSupport();
			enableCSP = cfg.isEnableWebViewCSP();
			if(supportForce) {
				supported = true;
				cspSupport = true;
			}else {
				supported = false;
				cspSupport = false;
				try {
					AdvancedHTMLIFrameElement tmp = (AdvancedHTMLIFrameElement)win.getDocument().createElement("iframe");
					supported = tmp != null && tmp.checkSafetyFeaturesSupported();
					cspSupport = enableCSP && supported && tmp.checkCSPSupported();
				}catch(Throwable ex) {
					logger.error("Error checking iframe support");
					logger.error(ex);
				}
			}
			if(!supported) {
				logger.error("This browser does not meet the safety requirements for webview support, this feature will be disabled");
			}else if(!cspSupport && enableCSP) {
				logger.warn("This browser does not support CSP attribute on iframes! (try Chrome)");
			}
			supportKnown = true;
		}
		return supported;
	}

	public static boolean isShowing() {
		return currentIFrameContainer != null;
	}

	private static int hashPermissionFlags(WebViewOptions opts) {
		int i = (opts.scriptEnabled ? 1 : 0);
		i |= ((enableCSP && cspSupport && opts.strictCSPEnable) ? 0 : 2);
		i |= (opts.serverMessageAPIEnabled ? 4 : 0);
		return i;
	}

	public static void beginShowing(WebViewOptions options, int x, int y, int w, int h) {
		if(!supported()) {
			return;
		}
		setupShowing(x, y, w, h);
		if(options.scriptEnabled) {
			PermissionsCache.Permission perm = PermissionsCache.getJavaScriptAllowed(options.permissionsOriginUUID, hashPermissionFlags(options));
			if(perm == null) {
				beginShowingEnableJavaScript(options);
			}else if(perm.choice) {
				beginShowingDirect(options);
			}else {
				beginShowingContentBlocked(options);
			}
		}else {
			beginShowingDirect(options);
		}
	}

	private static void setupShowing(int x, int y, int w, int h) {
		if(currentIFrameContainer != null) {
			endShowing();
		}
		currentIFrameContainer = win.getDocument().createElement("div");
		currentIFrameContainer.getClassList().add("_eaglercraftX_webview_container_element");
		CSSStyleDeclaration decl = currentIFrameContainer.getStyle();
		decl.setProperty("border", "5px solid #333333");
		decl.setProperty("z-index", "11");
		decl.setProperty("position", "absolute");
		decl.setProperty("background-color", "#DDDDDD");
		decl.setProperty("font-family", "sans-serif");
		resize(x, y, w, h);
		rootElement.appendChild(currentIFrameContainer);
	}

	private static void beginShowingDirect(WebViewOptions options) {
		if(!supportForce) {
			try {
				currentOptions = options;
				currentIFrame = (AdvancedHTMLIFrameElement)win.getDocument().createElement("iframe");
				currentIFrame.setAllowSafe("");
				currentIFrame.setReferrerPolicy("strict-origin");
				Set<String> sandboxArgs = new HashSet<>();
				sandboxArgs.add("allow-downloads");
				if(options.scriptEnabled) {
					sandboxArgs.add("allow-scripts");
					sandboxArgs.add("allow-pointer-lock");
				}
				currentIFrame.setSandboxSafe(sandboxArgs);
			}catch(IFrameSafetyException ex) {
				logger.error("Caught safety exception while opening webview!");
				logger.error(ex);
				if(currentIFrame != null) {
					currentIFrame.delete();
					currentIFrame = null;
					currentOptions = null;
				}
				logger.error("Things you can try:");
				logger.error("1. Set window.eaglercraftXOpts.forceWebViewSupport to true");
				logger.error("2. Set window.eaglercraftXOpts.enableWebViewCSP to false");
				logger.error("(these settings may compromise security)");
				beginShowingSafetyError();
				return;
			}
		}else {
			currentOptions = options;
			currentIFrame = (AdvancedHTMLIFrameElement)win.getDocument().createElement("iframe");
			try {
				currentIFrame.setAllow("");
			}catch(Throwable t) {
			}
			try {
				currentIFrame.setReferrerPolicy("strict-origin");
			}catch(Throwable t) {
			}
			try {
				List<String> sandboxArgs = new ArrayList<>();
				sandboxArgs.add("allow-downloads");
				if(options.scriptEnabled) {
					sandboxArgs.add("allow-scripts");
					sandboxArgs.add("allow-pointer-lock");
				}
				currentIFrame.setSandbox(String.join(" ", sandboxArgs));
			}catch(Throwable t) {
			}
		}
		currentIFrame.setCredentialless(true);
		currentIFrame.setLoading("lazy");
		boolean cspWarn = false;
		if(options.contentMode == EnumWebViewContentMode.BLOB_BASED) {
			if(enableCSP && cspSupport) {
				if(currentIFrame.checkCSPSupported()) {
					StringBuilder csp = new StringBuilder();
					csp.append("default-src 'none';");
					String protos = options.strictCSPEnable ? "" : (PlatformRuntime.requireSSL() ? " https:" : " http: https:");
					if(options.scriptEnabled) {
						csp.append(" script-src 'unsafe-eval' 'unsafe-inline' data: blob:").append(protos).append(';');
						csp.append(" style-src 'unsafe-eval' 'unsafe-inline' data: blob:").append(protos).append(';');
						csp.append(" img-src data: blob:").append(protos).append(';');
						csp.append(" font-src data: blob:").append(protos).append(';');
						csp.append(" child-src data: blob:").append(protos).append(';');
						csp.append(" frame-src data: blob:;");
						csp.append(" media-src data: mediastream: blob:").append(protos).append(';');
						csp.append(" connect-src data: blob:").append(protos).append(';');
						csp.append(" worker-src data: blob:").append(protos).append(';');
					}else {
						csp.append(" style-src data: 'unsafe-inline'").append(protos).append(';');
						csp.append(" img-src data:").append(protos).append(';');
						csp.append(" font-src data:").append(protos).append(';');
						csp.append(" media-src data:").append(protos).append(';');
					}
					currentIFrame.setCSP(csp.toString());
				}else {
					logger.warn("This browser does not support CSP attribute on iframes! (try Chrome)");
					cspWarn = true;
				}
			}else {
				cspWarn = true;
			}
			if(cspWarn && options.strictCSPEnable) {
				logger.warn("Strict CSP was requested for this webview, but that feature is not available!");
			}
		}else {
			cspWarn = true;
		}
		CSSStyleDeclaration decl = currentIFrame.getStyle();
		decl.setProperty("border", "none");
		decl.setProperty("background-color", "white");
		decl.setProperty("width", "100%");
		decl.setProperty("height", "100%");
		currentIFrame.getClassList().add("_eaglercraftX_webview_iframe_element");
		currentIFrameContainer.appendChild(currentIFrame);
		if(options.contentMode == EnumWebViewContentMode.BLOB_BASED) {
			currentIFrame.setSourceDocument(new String(options.blob, StandardCharsets.UTF_8));
		}else {
			currentIFrame.setSourceAddress(options.url.toString());
		}
		final int resetSer = webviewResetSerial;
		final AdvancedHTMLIFrameElement curIFrame = currentIFrame;
		final boolean[] focusTracker = new boolean[1];
		currentIFrame.addEventListener("mouseover", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				if(resetSer == webviewResetSerial && curIFrame == currentIFrame) {
					if(!focusTracker[0]) {
						focusTracker[0] = true;
						currentIFrame.getContentWindow().focus();
					}
				}
			}
		});
		currentIFrame.addEventListener("mouseout", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				if(resetSer == webviewResetSerial && curIFrame == currentIFrame) {
					if(focusTracker[0]) {
						focusTracker[0] = false;
						win.focus();
					}
				}
			}
		});
		if(options.scriptEnabled && options.serverMessageAPIEnabled) {
			currentMessageHandler = new Consumer<MessageEvent>() {
				@Override
				public void accept(MessageEvent evt) {
					synchronized(messageQueue) {
						if(resetSer == webviewResetSerial && curIFrame == currentIFrame) {
							messageQueue.add(() -> {
								if(resetSer == webviewResetSerial && curIFrame == currentIFrame) {
									handleMessageRawFromFrame(evt.getData());
								}else {
									logger.warn("Recieved message from on dead IFrame handler: (#" + resetSer + ") " + curIFrame.getSourceAddress());
								}
							});
						}else {
							logger.warn("Recieved message from on dead IFrame handler: (#" + resetSer + ") " + curIFrame.getSourceAddress());
						}
					}
				}
			};
		}
		logger.info("WebView is loading: \"{}\"", options.contentMode == EnumWebViewContentMode.BLOB_BASED ? "about:srcdoc" : currentIFrame.getSourceAddress());
		logger.info("JavaScript: {}, Strict CSP: {}, Message API: {}", options.scriptEnabled,
				options.strictCSPEnable && !cspWarn, options.serverMessageAPIEnabled);
	}

	private static void beginShowingEnableJSSetup() {
		if(currentAllowJavaScript != null) {
			++webviewResetSerial;
			currentAllowJavaScript.delete();
			currentAllowJavaScript = null;
		}
		currentAllowJavaScript = win.getDocument().createElement("div");
		CSSStyleDeclaration decl = currentAllowJavaScript.getStyle();
		decl.setProperty("background-color", "white");
		decl.setProperty("width", "100%");
		decl.setProperty("height", "100%");
		currentAllowJavaScript.getClassList().add("_eaglercraftX_webview_permission_screen");
		currentIFrameContainer.appendChild(currentAllowJavaScript);
	}

	private static void beginShowingEnableJavaScript(final WebViewOptions options) {
		beginShowingEnableJSSetup();
		String strictCSPMarkup;
		if(options.contentMode != EnumWebViewContentMode.BLOB_BASED) {
			strictCSPMarkup = "<span style=\"color:red;\">Impossible</span>";
		}else if(!cspSupport || !enableCSP) {
			strictCSPMarkup = "<span style=\"color:red;\">Unsupported</span>";
		}else if(options.strictCSPEnable) {
			strictCSPMarkup = "<span style=\"color:green;\">Enabled</span>";
		}else {
			strictCSPMarkup = "<span style=\"color:red;\">Disabled</span>";
		}
		String messageAPIMarkup;
		if(options.serverMessageAPIEnabled) {
			messageAPIMarkup = "<span style=\"color:red;\">Enabled</span>";
		}else {
			messageAPIMarkup = "<span style=\"color:green;\">Disabled</span>";
		}
		currentAllowJavaScript.setInnerHTML(
				"<div style=\"padding-top:13vh;\"><div style=\"margin:auto;max-width:450px;border:6px double black;text-align:center;padding:20px;\">"
				+ "<h2><img width=\"32\" height=\"32\" style=\"vertical-align:middle;\" src=\"" + PlatformApplication.faviconURLTeaVM() + "\">&emsp;Allow JavaScript</h2>"
				+ "<p style=\"font-family:monospace;text-decoration:underline;word-wrap:break-word;\" class=\"_eaglercraftX_permission_target_url\"></p>"
				+ "<h4 style=\"line-height:1.4em;\">Strict CSP: " + strictCSPMarkup + "&ensp;|&ensp;"
				+ "Message API: " + messageAPIMarkup + "</h4>"
				+ "<p><input class=\"_eaglercraftX_remember_javascript\" type=\"checkbox\" checked> Remember my choice</p>"
				+ "<p><button style=\"font-size:1.5em;\" class=\"_eaglercraftX_allow_javascript\">Allow</button>&emsp;"
				+ "<button style=\"font-size:1.5em;\" class=\"_eaglercraftX_block_javascript\">Block</button></p></div></div>");
		final int serial = webviewResetSerial;
		if(options.contentMode != EnumWebViewContentMode.BLOB_BASED) {
			String urlStr = options.url.toString();
			currentAllowJavaScript.querySelector("._eaglercraftX_permission_target_url").setInnerText(urlStr.length() > 255 ? (urlStr.substring(0, 253) + "...") : urlStr);
		}
		currentAllowJavaScript.querySelector("._eaglercraftX_allow_javascript").addEventListener("click", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				if(webviewResetSerial == serial && currentAllowJavaScript != null) {
					HTMLInputElement chkbox = (HTMLInputElement)currentAllowJavaScript.querySelector("._eaglercraftX_remember_javascript");
					if(chkbox != null && chkbox.isChecked()) {
						PermissionsCache.setJavaScriptAllowed(options.permissionsOriginUUID, hashPermissionFlags(options), true);
					}
					currentAllowJavaScript.delete();
					currentAllowJavaScript = null;
					++webviewResetSerial;
					beginShowingDirect(options);
				}
			}
		});
		currentAllowJavaScript.querySelector("._eaglercraftX_block_javascript").addEventListener("click", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				if(webviewResetSerial == serial && currentAllowJavaScript != null) {
					HTMLInputElement chkbox = (HTMLInputElement)currentAllowJavaScript.querySelector("._eaglercraftX_remember_javascript");
					if(chkbox != null && chkbox.isChecked()) {
						PermissionsCache.setJavaScriptAllowed(options.permissionsOriginUUID, hashPermissionFlags(options), false);
					}
					beginShowingContentBlocked(options);
				}
			}
		});
	}

	private static void beginShowingContentBlocked(final WebViewOptions options) {
		beginShowingEnableJSSetup();
		currentAllowJavaScript.setInnerHTML(
				"<div style=\"padding-top:13vh;\"><h1 style=\"text-align:center;\">"
				+ "<img width=\"48\" height=\"48\" style=\"vertical-align:middle;\" src=\"" + PlatformApplication.faviconURLTeaVM() + "\">&emsp;Content Blocked</h1>"
				+ "<h4 style=\"text-align:center;\">You chose to block JavaScript execution for this embed</h4>"
				+ "<p style=\"text-align:center;\"><button style=\"font-size:1.0em;\" class=\"_eaglercraftX_re_evaluate_javascript\">Re-evaluate</button></p></div>");
		final int serial = webviewResetSerial;
		currentAllowJavaScript.querySelector("._eaglercraftX_re_evaluate_javascript").addEventListener("click", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				if(webviewResetSerial == serial && currentAllowJavaScript != null) {
					PermissionsCache.clearJavaScriptAllowed(options.permissionsOriginUUID);
					beginShowingEnableJavaScript(options);
				}
			}
		});
	}

	private static void beginShowingSafetyError() {
		beginShowingEnableJSSetup();
		currentAllowJavaScript.setInnerHTML(
				"<div style=\"padding-top:13vh;\"><h1 style=\"text-align:center;\">"
				+ "<img width=\"48\" height=\"48\" style=\"vertical-align:middle;\" src=\"" + PlatformApplication.faviconURLTeaVM() + "\">&emsp;IFrame Safety Error</h1>"
				+ "<h4 style=\"text-align:center;\">The content cannot be displayed safely!</h4>"
				+ "<h4 style=\"text-align:center;\">Check console for more details</h4></div>");
	}

	private static String getURLOrigin(URI urlObject) {
		String str = " " + urlObject.getScheme() + "://" + urlObject.getRawAuthority();
		if(str.startsWith(" http:")) {
			str = str + " https" + str.substring(5);
		}
		return str;
	}

	public static void resize(int x, int y, int w, int h) {
		if(currentIFrameContainer != null) {
			CSSStyleDeclaration decl = currentIFrameContainer.getStyle();
			float s = PlatformInput.getDPI();
			decl.setProperty("top", "" + (y / s) + "px");
			decl.setProperty("left", "" + (x / s) + "px");
			decl.setProperty("width", "" + ((w / s) - 10) + "px");
			decl.setProperty("height", "" + ((h / s) - 10) + "px");
		}
	}

	public static void endShowing() {
		++webviewResetSerial;
		if(currentIFrame != null) {
			currentIFrame.delete();
			currentIFrame = null;
		}
		synchronized(messageQueue) {
			messageQueue.clear();
		}
		currentMessageHandler = null;
		if(currentAllowJavaScript != null) {
			currentAllowJavaScript.delete();
			currentAllowJavaScript = null;
		}
		currentIFrameContainer.delete();
		currentIFrameContainer = null;
		if(currentMessageChannelName != null) {
			sendMessageEnToServer(false, currentMessageChannelName);
			currentMessageChannelName = null;
		}
		win.focus();
		currentOptions = null;
	}

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

	@JSBody(params = { "evt", "iframe" }, script = "return evt.source === iframe.contentWindow;")
	private static native boolean sourceEquals(MessageEvent evt, AdvancedHTMLIFrameElement iframe);

	static void onWindowMessageRecieved(MessageEvent evt) {
		if(currentIFrame != null && currentMessageHandler != null && sourceEquals(evt, currentIFrame)) {
			currentMessageHandler.accept(evt);
		}
	}

	public static void setPacketSendCallback(IPacketSendCallback callback) {
		packetSendCallback = callback;
	}

	public static void runTick() {
		if(currentIFrame == null) {
			return;
		}
		List<Runnable> lst = null;
		synchronized(messageQueue) {
			if(messageQueue.isEmpty()) {
				return;
			}
			lst = new ArrayList<>(messageQueue);
			messageQueue.clear();
		}
		for(int i = 0, l = lst.size(); i < l; ++i) {
			try {
				lst.get(i).run();
			}catch(Throwable t) {
				logger.error("Caught exception processing webview message!");
				logger.error(t);
			}
		}
	}

	@JSBody(params = { "channel", "contents" }, script = "return {ver:1,channel:channel,type:\"string\",data:contents};")
	private static native JSObject createStringMessage(String channel, String contents);

	@JSBody(params = { "channel", "contents" }, script = "return {ver:1,channel:channel,type:\"binary\",data:contents};")
	private static native JSObject createBinaryMessage(String channel, ArrayBuffer contents);

	public static void handleMessageFromServer(SPacketWebViewMessageV4EAG packet) {
		Window w;
		if(currentMessageChannelName != null && currentIFrame != null && (w = currentIFrame.getContentWindow()) != null) {
			JSObject obj = null;
			if(packet.type == SPacketWebViewMessageV4EAG.TYPE_STRING) {
				obj = createStringMessage(currentMessageChannelName, new String(packet.data, StandardCharsets.UTF_8));
			}else if(packet.type == SPacketWebViewMessageV4EAG.TYPE_BINARY) {
				obj = createBinaryMessage(currentMessageChannelName, TeaVMUtils.unwrapArrayBuffer(packet.data));
			}
			if(obj != null) {
				w.postMessage(obj, "*");
			}
		}else {
			logger.error("Server tried to send the WebView a message, but the message channel is not open!");
		}
	}

	@JSBody(params = { "obj" }, script = "return (typeof obj === \"object\") && (obj.ver === 1) && ((typeof obj.channel === \"string\") && obj.channel.length > 0);")
	private static native boolean checkRawMessageValid(JSObject obj);

	@JSBody(params = { "obj" }, script = "return (typeof obj.open === \"boolean\");")
	private static native boolean checkRawMessageValidEn(JSObject obj);

	@JSBody(params = { "obj" }, script = "return (typeof obj.data === \"string\");")
	private static native boolean checkRawMessageValidDataStr(JSObject obj);

	@JSBody(params = { "obj" }, script = "return (obj.data instanceof ArrayBuffer);")
	private static native boolean checkRawMessageValidDataBin(JSObject obj);

	private static interface WebViewMessage extends JSObject {
		
		@JSProperty
		String getType();
		
		@JSProperty
		String getChannel();
		
		@JSProperty("data")
		String getDataAsString();
		
		@JSProperty("data")
		ArrayBuffer getDataAsArrayBuffer();
		
		@JSProperty
		boolean getOpen();
		
	}

	private static void handleMessageRawFromFrame(JSObject obj) {
		if(checkRawMessageValid(obj)) {
			if(checkRawMessageValidEn(obj)) {
				WebViewMessage msg = (WebViewMessage)obj;
				sendMessageEnToServer(msg.getOpen(), msg.getChannel());
				return;
			}else if(checkRawMessageValidDataStr(obj)) {
				WebViewMessage msg = (WebViewMessage)obj;
				sendMessageToServer(msg.getChannel(), CPacketWebViewMessageV4EAG.TYPE_STRING, msg.getDataAsString().getBytes(StandardCharsets.UTF_8));
				return;
			}else if(checkRawMessageValidDataBin(obj)) {
				WebViewMessage msg = (WebViewMessage)obj;
				sendMessageToServer(msg.getChannel(), CPacketWebViewMessageV4EAG.TYPE_BINARY, TeaVMUtils.wrapByteArrayBuffer(msg.getDataAsArrayBuffer()));
				return;
			}
		}
		logger.warn("WebView sent an invalid message!");
	}

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

}