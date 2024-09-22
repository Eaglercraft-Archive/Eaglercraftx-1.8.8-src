package net.lax1dude.eaglercraft.v1_8.internal.lwjgl;

import java.net.URI;

import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
import net.lax1dude.eaglercraft.v1_8.internal.AbstractWebSocketClient;
import net.lax1dude.eaglercraft.v1_8.internal.EnumEaglerConnectionState;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public class DesktopWebSocketClient extends AbstractWebSocketClient {

	static final Logger logger = LogManager.getLogger("DesktopWebSocketClient");
	
	volatile EnumEaglerConnectionState playConnectState = EnumEaglerConnectionState.CONNECTING;
	final Object connectOpenMutex = new Object();
	final WebSocketClientImpl clientImpl;
	final URI currentURI;
	final String currentURIStr;

	public DesktopWebSocketClient(URI currentURI) {
		super(currentURI.toString());
		this.currentURI = currentURI;
		currentURIStr = currentURI.toString();
		clientImpl = new WebSocketClientImpl(this, currentURI);
		clientImpl.addHeader("Origin", "EAG_LWJGL_" + (EaglercraftVersion.projectForkName + "_"
				+ EaglercraftVersion.projectOriginVersion).replaceAll("[^a-zA-Z0-9\\-_\\.]", "_"));
	}

	@Override
	public EnumEaglerConnectionState getState() {
		return playConnectState;
	}

	@Override
	public boolean connectBlocking(int timeoutMS) {
		synchronized(connectOpenMutex) {
			try {
				connectOpenMutex.wait(timeoutMS);
			} catch (InterruptedException e) {
				return false;
			}
		}
		return playConnectState.isOpen();
	}

	@Override
	public boolean isOpen() {
		return playConnectState.isOpen();
	}

	@Override
	public boolean isClosed() {
		return playConnectState.isClosed();
	}

	@Override
	public void close() {
		if(!playConnectState.isClosed()) {
			try {
				clientImpl.closeBlocking();
			} catch (InterruptedException e) {
			}
			playConnectState = EnumEaglerConnectionState.CLOSED;
		}
	}

	@Override
	public void send(String str) {
		if(clientImpl.isClosed()) {
			logger.error("[{}]: Client tried to send {} char packet while the socket was closed!", currentURIStr, str.length());
		}else {
			clientImpl.send(str);
		}
	}

	@Override
	public void send(byte[] bytes) {
		if(clientImpl.isClosed()) {
			logger.error("[{}]: Client tried to send {} byte packet while the socket was closed!", currentURIStr, bytes.length);
		}else {
			clientImpl.send(bytes);
		}
	}

	public void handleString(String str) {
		addRecievedFrame(new DesktopWebSocketFrameString(str));
	}

	public void handleBytes(byte[] array) {
		addRecievedFrame(new DesktopWebSocketFrameBinary(array));
	}

}
