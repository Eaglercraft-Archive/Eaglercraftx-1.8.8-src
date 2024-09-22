package net.lax1dude.eaglercraft.v1_8.internal.lwjgl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.internal.IClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.WebViewOptions;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketWebViewMessageV4EAG;
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
public class FallbackWebViewServer {

	static final Logger logger = LogManager.getLogger("FallbackWebViewServer");

	public static final String LISTEN_ADDR = "127.69.69.69";

	public static final File webViewClientHTML = new File("RTWebViewClient.html");

	public final WebViewOptions options;

	private FallbackWebViewWSD websocketServer;
	private FallbackWebViewHTTPD httpServer;

	private String currentURL;
	private volatile boolean dead;

	private IPacketSendCallback callback = null;

	public FallbackWebViewServer(WebViewOptions options) {
		this.options = options;
	}

	public void start() throws RuntimeException {
		dead = false;
		StringBuilder vigg = new StringBuilder();
		try(BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(webViewClientHTML), StandardCharsets.UTF_8))) {
			String line;
			while((line = reader.readLine()) != null) {
				vigg.append(line).append('\n');
			}
		}catch(IOException ex) {
			logger.error("Failed to read \"{}\"!");
		}
		String indexHTML = vigg.toString();
		
		Object mutex = new Object();
		websocketServer = new FallbackWebViewWSD(LISTEN_ADDR, randomPort(), options);
		websocketServer.setEaglerPacketSendCallback(callback);
		synchronized(mutex) {
			websocketServer.doStartup(mutex);
			try {
				mutex.wait(5000l);
			} catch (InterruptedException e) {
			}
		}
		if(!websocketServer.hasStarted) {
			logger.error("Failed to start WebSocket in time!");
			try {
				websocketServer.stop(5000);
			}catch(Throwable t) {
			}
			websocketServer = null;
			throw new RuntimeException("Failed to start WebSocket server!");
		}
		InetSocketAddress addr = websocketServer.getAddress();
		String wsAddr = "ws://" + addr.getHostString() + ":" + addr.getPort() + "/";
		logger.info("Listening for WebSocket on {}", wsAddr);
		indexHTML = indexHTML.replace("${client_websocket_uri}", wsAddr);
		
		JSONObject optsExport = new JSONObject();
		IClientConfigAdapter cfgAdapter = PlatformRuntime.getClientConfigAdapter();
		optsExport.put("forceWebViewSupport", cfgAdapter.isForceWebViewSupport());
		optsExport.put("enableWebViewCSP", cfgAdapter.isEnableWebViewCSP());
		indexHTML = indexHTML.replace("{eaglercraftXOpts}", optsExport.toString());
		
		httpServer = new FallbackWebViewHTTPD(LISTEN_ADDR, 0, indexHTML);
		try {
			httpServer.start(5000, true);
		} catch (IOException e) {
			logger.error("Failed to start NanoHTTPD!");
			try {
				websocketServer.stop(5000);
			}catch(Throwable t) {
			}
			websocketServer = null;
			httpServer = null;
			throw new RuntimeException("Failed to start NanoHTTPD!", e);
		}
		int httpPort = httpServer.getListeningPort();
		currentURL = "http://" + LISTEN_ADDR + ":" + httpPort + "/RTWebViewClient";
		logger.info("Listening for HTTP on {}", currentURL);
	}

	private int randomPort() {
		try(ServerSocket sockler = new ServerSocket(0)) {
			return sockler.getLocalPort();
		}catch(IOException ex) {
			throw new RuntimeException("Failed to find random port to bind to!", ex);
		}
	}

	public boolean isDead() {
		return dead;
	}

	public String getURL() {
		return !dead ? currentURL : null;
	}

	public void handleMessageFromServer(SPacketWebViewMessageV4EAG packet) {
		if(packet.type == SPacketWebViewMessageV4EAG.TYPE_STRING) {
			if(websocketServer != null) {
				websocketServer.handleServerMessageStr(new String(packet.data, StandardCharsets.UTF_8));
			}else {
				logger.error("Recieved string message, but the webview server is not running!");
			}
		}else if(packet.type == SPacketWebViewMessageV4EAG.TYPE_BINARY) {
			if(websocketServer != null) {
				websocketServer.handleServerMessageBytes(packet.data);
			}else {
				logger.error("Recieved string message, but the webview server is not running!");
			}
		}else {
			logger.error("Unknown server webview message type {}", packet.type);
		}
	}

	public void setPacketSendCallback(IPacketSendCallback callback) {
		this.callback = callback;
		if(websocketServer != null) {
			websocketServer.setEaglerPacketSendCallback(callback);
		}
	}

	public void runTick() {
		
	}

	public void killServer() {
		if(!dead) {
			dead = true;
			if(websocketServer != null) {
				try {
					websocketServer.stop(10000);
				} catch (Throwable th) {
					logger.error("Failed to stop WebSocket server, aborting");
					logger.error(th);
				}
				websocketServer = null;
			}
			if(httpServer != null) {
				try {
					httpServer.stop();
				} catch (Throwable th) {
					logger.error("Failed to stop HTTP server, aborting");
					logger.error(th);
				}
				httpServer = null;
			}
		}
	}

}
