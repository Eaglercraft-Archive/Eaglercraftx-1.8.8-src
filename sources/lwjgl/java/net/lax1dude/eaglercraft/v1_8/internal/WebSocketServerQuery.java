package net.lax1dude.eaglercraft.v1_8.internal;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.extensions.permessage_deflate.PerMessageDeflateExtension;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

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
class WebSocketServerQuery extends WebSocketClient implements IServerQuery {

	private static final Draft perMessageDeflateDraft = new Draft_6455(new PerMessageDeflateExtension());

	public static final Logger logger = LogManager.getLogger("WebSocketQuery");

	private final List<QueryResponse> queryResponses = new LinkedList();
	private final List<byte[]> queryResponsesBytes = new LinkedList();
	private final String type;
	private boolean open = true;
	private boolean alive = false;
	private long pingStart = -1l;
	private long pingTimer = -1l;
	private EnumServerRateLimit rateLimit = EnumServerRateLimit.OK;

	WebSocketServerQuery(String type, URI serverUri) {
		super(serverUri, perMessageDeflateDraft);
		this.type = type;
		this.setConnectionLostTimeout(5);
		this.setTcpNoDelay(true);
		this.connect();
	}

	@Override
	public int responsesAvailable() {
		synchronized(queryResponses) {
			return queryResponses.size();
		}
	}

	@Override
	public QueryResponse getResponse() {
		synchronized(queryResponses) {
			if(queryResponses.size() > 0) {
				return queryResponses.remove(0);
			}else {
				return null;
			}
		}
	}

	@Override
	public int binaryResponsesAvailable() {
		synchronized(queryResponsesBytes) {
			return queryResponsesBytes.size();
		}
	}

	@Override
	public byte[] getBinaryResponse() {
		synchronized(queryResponsesBytes) {
			if(queryResponsesBytes.size() > 0) {
				return queryResponsesBytes.remove(0);
			}else {
				return null;
			}
		}
	}

	@Override
	public QueryReadyState readyState() {
		return open ? (alive ? QueryReadyState.OPEN : QueryReadyState.CONNECTING)
				: (alive ? QueryReadyState.CLOSED : QueryReadyState.FAILED);
	}

	@Override
	public EnumServerRateLimit getRateLimit() {
		return rateLimit;
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
		open = false;
	}

	@Override
	public void onError(Exception arg0) {
		logger.error("Exception thrown by websocket query \"" + this.getURI().toString() + "\"!");
		logger.error(arg0);
	}

	@Override
	public void onMessage(String arg0) {
		alive = true;
		if(pingTimer == -1) {
			pingTimer = System.currentTimeMillis() - pingStart;
			if(pingTimer < 1) {
				pingTimer = 1;
			}
		}
		if(arg0.equalsIgnoreCase("BLOCKED")) {
			logger.error("Reached full IP ratelimit for {}!", this.uri.toString());
			rateLimit = EnumServerRateLimit.BLOCKED;
			return;
		}
		if(arg0.equalsIgnoreCase("LOCKED")) {
			logger.error("Reached full IP ratelimit lockout for {}!", this.uri.toString());
			rateLimit = EnumServerRateLimit.LOCKED_OUT;
			return;
		}
		try {
			JSONObject obj = new JSONObject(arg0);
			if("blocked".equalsIgnoreCase(obj.optString("type", null))) {
				logger.error("Reached query ratelimit for {}!", this.uri.toString());
				rateLimit = EnumServerRateLimit.BLOCKED;
			}else if("locked".equalsIgnoreCase(obj.optString("type", null))) {
				logger.error("Reached query ratelimit lockout for {}!", this.uri.toString());
				rateLimit = EnumServerRateLimit.LOCKED_OUT;
			}else {
				QueryResponse response = new QueryResponse(obj, pingTimer);
				synchronized(queryResponses) {
					queryResponses.add(response);
				}
			}
		}catch(Throwable t) {
			logger.error("Exception thrown parsing websocket query response from \"" + this.getURI().toString() + "\"!");
			logger.error(t);
		}
	}

	@Override
	public void onMessage(ByteBuffer arg0) {
		alive = true;
		if(pingTimer == -1) {
			pingTimer = System.currentTimeMillis() - pingStart;
			if(pingTimer < 1) {
				pingTimer = 1;
			}
		}
		synchronized(queryResponsesBytes) {
			queryResponsesBytes.add(arg0.array());
		}
	}

	@Override
	public void onOpen(ServerHandshake arg0) {
		pingStart = System.currentTimeMillis();
		send("Accept: " + type);
	}

}
