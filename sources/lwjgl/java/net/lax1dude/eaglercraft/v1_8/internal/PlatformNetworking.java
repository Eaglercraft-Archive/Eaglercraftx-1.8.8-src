package net.lax1dude.eaglercraft.v1_8.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.java_websocket.enums.ReadyState;

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
public class PlatformNetworking {
	
	static final Logger networkLogger = LogManager.getLogger("PlatformNetworking");
	
	private static WebSocketPlayClient wsPlayClient = null;
	static EnumEaglerConnectionState playConnectState = EnumEaglerConnectionState.CLOSED;
	static EnumServerRateLimit serverRateLimit = null;
	
	static String currentURI = null;
	
	public static EnumEaglerConnectionState playConnectionState() {
		return ((wsPlayClient == null || wsPlayClient.isClosed()) && playConnectState == EnumEaglerConnectionState.CONNECTING) ? EnumEaglerConnectionState.FAILED :
			((wsPlayClient != null && wsPlayClient.getReadyState() == ReadyState.NOT_YET_CONNECTED) ? EnumEaglerConnectionState.CONNECTING : 
				(((wsPlayClient == null || wsPlayClient.isClosed()) && playConnectState != EnumEaglerConnectionState.FAILED) ? EnumEaglerConnectionState.CLOSED : playConnectState));
	}
	
	public static void startPlayConnection(String destination) {
		if(!playConnectionState().isClosed()) {
			networkLogger.warn("Tried connecting to a server while already connected to a different server!");
			playDisconnect();
		}
		
		currentURI = destination;
		
		synchronized(playPackets) {
			playPackets.clear();
		}
		
		playConnectState = EnumEaglerConnectionState.CONNECTING;
		networkLogger.info("Connecting to server: {}", destination);
		
		URI u;
		
		try {
			u = new URI(destination);
		}catch(URISyntaxException ex) {
			networkLogger.error("Invalid server URI: {}", destination);
			playConnectState = EnumEaglerConnectionState.FAILED;
			return;
		}
		
		wsPlayClient = new WebSocketPlayClient(u);
		wsPlayClient.connect();
	}
	
	public static void playDisconnect() {
		if(!playConnectionState().isClosed() && wsPlayClient != null) {
			try {
				wsPlayClient.closeBlocking();
			} catch (InterruptedException e) {
				// :(
			}
			playConnectState = EnumEaglerConnectionState.CLOSED;
		}
	}
	
	private static final List<byte[]> playPackets = new LinkedList();
	
	public static byte[] readPlayPacket() {
		synchronized(playPackets) {
			return playPackets.size() > 0 ? playPackets.remove(0) : null;
		}
	}

	public static int countAvailableReadData() {
		int total = 0;
		synchronized(playPackets) {
			for(int i = 0, l = playPackets.size(); i < l; ++i) {
				total += playPackets.get(i).length;
			}
		}
		return total;
	}
	
	static void recievedPlayPacket(byte[] arg0) {
		synchronized(playPackets) {
			playPackets.add(arg0);
		}
	}
	
	public static void writePlayPacket(byte[] pkt) {
		if(wsPlayClient == null || wsPlayClient.isClosed()) {
			networkLogger.error("Tried to send {} byte play packet while the socket was closed!", pkt.length);
		}else {
			wsPlayClient.send(pkt);
		}
	}
	
	public static IServerQuery sendServerQuery(String uri, String accept) {
		URI u;
		
		try {
			u = new URI(uri);
		}catch(URISyntaxException ex) {
			networkLogger.error("Invalid server URI: {}", uri);
			playConnectState = EnumEaglerConnectionState.FAILED;
			return null;
		}
		
		return new WebSocketServerQuery(accept, u);
	}
	
	public static EnumServerRateLimit getRateLimit() {
		return serverRateLimit == null ? EnumServerRateLimit.OK : serverRateLimit;
	}
	
	public static String getCurrentURI() {
		return currentURI;
	}
	
}
