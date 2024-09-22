package net.lax1dude.eaglercraft.v1_8.internal.lwjgl;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.WebViewOptions;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketWebViewMessageEnV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketWebViewMessageV4EAG;
import net.lax1dude.eaglercraft.v1_8.webview.PermissionsCache;
import net.lax1dude.eaglercraft.v1_8.webview.PermissionsCache.Permission;
import net.lax1dude.eaglercraft.v1_8.webview.WebViewOverlayController.IPacketSendCallback;

import static net.lax1dude.eaglercraft.v1_8.internal.lwjgl.FallbackWebViewProtocol.*;

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
class FallbackWebViewWSD extends WebSocketServer {

	static final Logger logger = FallbackWebViewServer.logger;

	private Object onStartNotify;

	volatile boolean hasStarted = false;

	private final Object webSockMutex = new Object();
	volatile WebSocket webSocket = null;

	volatile boolean hasHandshake = false;
	volatile String currentChannelName = null;

	private IPacketSendCallback callback = null;
	WebViewOptions options;

	private boolean enableCSP;
	private boolean cspSupport;

	FallbackWebViewWSD(String address, int port, WebViewOptions options) {
		super(new InetSocketAddress(address, port), 1);
		this.setTcpNoDelay(true);
		this.setReuseAddr(true);
		this.setConnectionLostTimeout(30);
		this.options = options;
		this.enableCSP = PlatformRuntime.getClientConfigAdapter().isEnableWebViewCSP();
		this.cspSupport = true;
	}

	public void doStartup(Object onStartNotify) {
		this.onStartNotify = onStartNotify;
		this.start();
	}

	private void handleOpen() {
		hasHandshake = false;
		currentChannelName = null;
	}

	private void handleClose() {
		if(currentChannelName != null && callback != null) {
			callback.sendPacket(new CPacketWebViewMessageEnV4EAG(false, null));
		}
		currentChannelName = null;
	}

	private int hashPermissionFlags() {
		int i = (options.scriptEnabled ? 1 : 0);
		i |= ((enableCSP && cspSupport && options.strictCSPEnable) ? 0 : 2);
		i |= (options.serverMessageAPIEnabled ? 4 : 0);
		return i;
	}

	private void handleMessage(String str) {
		WebSocket ws = webSocket;
		FallbackWebViewPacket _packet = readPacket(str);
		if(_packet != null) {
			if(!hasHandshake) {
				if(_packet instanceof CPacketClientHandshake) {
					hasHandshake = true;
					Permission perm = PermissionsCache.getJavaScriptAllowed(options.permissionsOriginUUID, hashPermissionFlags());
					ws.send(writePacket(new SPacketServerHandshake(options, EnumWebViewJSPermission.fromPermission(perm))));
				}else {
					terminate("Unknown or unexpected packet: " + _packet.getClass().getSimpleName());
				}
			}else {
				if(_packet instanceof CPacketWebViewChannelOpen) {
					CPacketWebViewChannelOpen packet = (CPacketWebViewChannelOpen)_packet;
					if(currentChannelName == null) {
						currentChannelName = packet.messageChannel;
						logger.info("[{}]: opened WebView channel \"{}\"", ws.getRemoteSocketAddress(), packet.messageChannel);
						safeCallbackSend(new CPacketWebViewMessageEnV4EAG(true, packet.messageChannel));
					}else {
						terminate("Tried to open multiple channels");
					}
				}else if(_packet instanceof CPacketWebViewMessage) {
					CPacketWebViewMessage packet = (CPacketWebViewMessage)_packet;
					if(currentChannelName != null) {
						safeCallbackSend(new CPacketWebViewMessageV4EAG(packet.messageContent));
					}else {
						terminate("Tried to send message without opening channel");
					}
				}else if(_packet instanceof CPacketWebViewChannelClose) {
					if(currentChannelName != null) {
						currentChannelName = null;
						safeCallbackSend(new CPacketWebViewMessageEnV4EAG(false, null));
					}else {
						terminate("Tried to close missing channel");
					}
				}else if(_packet instanceof CPacketWebViewJSPermission) {
					CPacketWebViewJSPermission packet = (CPacketWebViewJSPermission)_packet;
					switch(packet.permission) {
					case NOT_SET:
						PermissionsCache.clearJavaScriptAllowed(options.permissionsOriginUUID);
						break;
					case ALLOW:
						PermissionsCache.setJavaScriptAllowed(options.permissionsOriginUUID, hashPermissionFlags(), true);
						break;
					case BLOCK:
						PermissionsCache.setJavaScriptAllowed(options.permissionsOriginUUID, hashPermissionFlags(), false);
						break;
					default:
						terminate("Unknown permission state selected!");
						break;
					}
					
				}else {
					terminate("Unknown or unexpected packet: " + _packet.getClass().getSimpleName());
				}
			}
		}else {
			terminate("Invalid packet recieved");
		}
	}

	private void handleMessage(ByteBuffer buffer) {
		if(currentChannelName != null) {
			safeCallbackSend(new CPacketWebViewMessageV4EAG(buffer.array()));
		}else {
			terminate("Sent binary webview message while channel was closed");
		}
	}

	private void terminate(String msg) {
		if(webSocket != null) {
			logger.error("[{}]: Terminating connection, reason: \"{}\"", webSocket.getRemoteSocketAddress(), msg);
			webSocket.send(writePacket(new SPacketServerError(msg)));
			webSocket.close();
		}
	}

	private void safeCallbackSend(GameMessagePacket packet) {
		if(callback != null) {
			callback.sendPacket(packet);
		}else {
			logger.error("webview sent packet to server, but there's no callback registered to send packets!");
		}
	}

	void handleServerMessageStr(String msg) {
		if(webSocket != null) {
			if(currentChannelName != null) {
				webSocket.send(writePacket(new SPacketWebViewMessage(msg)));
			}else {
				logger.error("Recieved string message from server, but the channel is not open!");
			}
		}else {
			logger.error("Recieved string message from server, but there is no active websocket!");
		}
	}

	void handleServerMessageBytes(byte[] msg) {
		if(webSocket != null) {
			if(currentChannelName != null) {
				webSocket.send(msg);
			}else {
				logger.error("Recieved binary message from server, but the channel is not open!");
			}
		}else {
			logger.error("Recieved binary message from server, but there is no active websocket!");
		}
	}

	@Override
	public void onStart() {
		hasStarted = true;
		if(onStartNotify != null) {
			synchronized(onStartNotify) {
				onStartNotify.notifyAll();
			}
			onStartNotify = null;
		}else {
			logger.warn("No mutex to notify!");
		}
	}

	@Override
	public void onOpen(WebSocket arg0, ClientHandshake arg1) {
		boolean result;
		synchronized(webSockMutex) {
			if(webSocket == null) {
				webSocket = arg0;
				result = true;
			}else {
				result = false;
			}
		}
		if(result) {
			logger.info("[{}]: WebSocket connection opened", arg0.getRemoteSocketAddress());
			handleOpen();
		}else {
			logger.error("[{}]: Rejecting duplicate connection", arg0.getRemoteSocketAddress());
			arg0.send(writePacket(new SPacketServerError("You already have a tab open!")));
			arg0.close();
		}
	}

	@Override
	public void onMessage(WebSocket arg0, String arg1) {
		if(arg0 == webSocket) {
			handleMessage(arg1);
		}
	}

	@Override
	public void onMessage(WebSocket arg0, ByteBuffer arg1) {
		if(arg0 == webSocket) {
			handleMessage(arg1);
		}
	}

	@Override
	public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
		synchronized(webSockMutex) {
			if(arg0 == webSocket) {
				logger.info("[{}]: WebSocket connection closed", arg0.getRemoteSocketAddress());
				try {
					handleClose();
				}finally {
					webSocket = null;
				}
			}
		}
	}

	@Override
	public void onError(WebSocket arg0, Exception arg1) {
		logger.error("[{}]: WebSocket caught exception", arg0 != null ? arg0.getRemoteSocketAddress() : "null");
		logger.error(arg1);
	}

	public void setEaglerPacketSendCallback(IPacketSendCallback callback) {
		this.callback = callback;
	}

}
