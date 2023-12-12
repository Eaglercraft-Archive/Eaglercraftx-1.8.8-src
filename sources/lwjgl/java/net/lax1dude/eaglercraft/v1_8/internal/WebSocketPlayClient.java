package net.lax1dude.eaglercraft.v1_8.internal;

import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.extensions.permessage_deflate.PerMessageDeflateExtension;
import org.java_websocket.handshake.ServerHandshake;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

/**
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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
class WebSocketPlayClient extends WebSocketClient {

	private static final Draft perMessageDeflateDraft = new Draft_6455(new PerMessageDeflateExtension());
	
	public static final Logger logger = LogManager.getLogger("WebSocket");

	WebSocketPlayClient(URI serverUri) {
		super(serverUri, perMessageDeflateDraft);
		this.setConnectionLostTimeout(15);
		this.setTcpNoDelay(true);
	}

	@Override
	public void onOpen(ServerHandshake arg0) {
		PlatformNetworking.playConnectState = EnumEaglerConnectionState.CONNECTED;
		PlatformNetworking.serverRateLimit = EnumServerRateLimit.OK;
		logger.info("Connection opened: {}", this.uri.toString());
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
		logger.info("Connection closed: {}", this.uri.toString());
	}

	@Override
	public void onError(Exception arg0) {
		logger.error("Exception thrown by websocket \"" + this.getURI().toString() + "\"!");
		logger.error(arg0);
		PlatformNetworking.playConnectState = EnumEaglerConnectionState.FAILED;
	}

	@Override
	public void onMessage(String arg0) {
		if(arg0.equalsIgnoreCase("BLOCKED")) {
			logger.error("Reached full IP ratelimit!");
			PlatformNetworking.serverRateLimit = EnumServerRateLimit.BLOCKED;
		}else if(arg0.equalsIgnoreCase("LOCKED")) {
			logger.error("Reached full IP ratelimit lockout!");
			PlatformNetworking.serverRateLimit = EnumServerRateLimit.LOCKED_OUT;
		}
	}

	@Override
	public void onMessage(ByteBuffer arg0) {
		PlatformNetworking.recievedPlayPacket(arg0.array());
	}
	
}
