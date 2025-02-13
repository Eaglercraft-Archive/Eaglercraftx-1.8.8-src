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

package net.lax1dude.eaglercraft.v1_8.internal.lwjgl;

import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.extensions.permessage_deflate.PerMessageDeflateExtension;
import org.java_websocket.handshake.ServerHandshake;

import net.lax1dude.eaglercraft.v1_8.internal.EnumEaglerConnectionState;

class WebSocketClientImpl extends WebSocketClient {

	private static final Draft perMessageDeflateDraft = new Draft_6455(new PerMessageDeflateExtension());
	
	protected final DesktopWebSocketClient clientObj;

	WebSocketClientImpl(DesktopWebSocketClient clientObj, URI serverUri) {
		super(serverUri, perMessageDeflateDraft);
		this.clientObj = clientObj;
		this.setConnectionLostTimeout(15);
		this.setTcpNoDelay(true);
		this.connect();
	}

	@Override
	public void onOpen(ServerHandshake arg0) {
		clientObj.playConnectState = EnumEaglerConnectionState.CONNECTED;
		DesktopWebSocketClient.logger.info("Connection opened: {}", this.uri.toString());
		synchronized(clientObj.connectOpenMutex) {
			clientObj.connectOpenMutex.notifyAll();
		}
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
		DesktopWebSocketClient.logger.info("Connection closed: {}", this.uri.toString());
		if(clientObj.playConnectState != EnumEaglerConnectionState.FAILED) {
			clientObj.playConnectState = EnumEaglerConnectionState.CLOSED;
		}
	}

	@Override
	public void onError(Exception arg0) {
		DesktopWebSocketClient.logger.error("Exception thrown by websocket \"" + this.getURI().toString() + "\"!");
		DesktopWebSocketClient.logger.error(arg0);
		if(clientObj.playConnectState == EnumEaglerConnectionState.CONNECTING) {
			clientObj.playConnectState = EnumEaglerConnectionState.FAILED;
		}
	}

	@Override
	public void onMessage(String arg0) {
		clientObj.handleString(arg0);
	}

	@Override
	public void onMessage(ByteBuffer arg0) {
		clientObj.handleBytes(arg0.array());
	}

}