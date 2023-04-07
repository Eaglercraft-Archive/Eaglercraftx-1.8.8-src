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
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info)
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
