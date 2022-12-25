package net.lax1dude.eaglercraft.v1_8.placeholder_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 * Copyright (c) 2022 LAX1DUDE. All Rights Reserved.
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
public class PlaceholderServer extends WebSocketServer {

	public static PlaceholderServer websocketServer = null;
	private boolean isOpen = false;

	public static void main(String[] args) throws IOException {
		System.out.println();
		System.out.println("Copyright (c) 2022 lax1dude");
		System.out.println("All rights reserved.");
		System.out.println();
		System.out.println("Starting placeholder 1.8 server...");
		System.out.println();
		
		PlaceholderServerConfig.load();
		
		System.out.println("Starting WebSocket server...");
		System.out.println();
		websocketServer = new PlaceholderServer(new InetSocketAddress(PlaceholderServerConfig.host, PlaceholderServerConfig.port));
		websocketServer.start();
		
		long redirTimeout = (PlaceholderServerConfig.redirect != null && PlaceholderServerConfig.redirect.length() > 0) ? 500l : 100l;
		while(true) {
			try {
				Thread.sleep(200l);
				long millis = System.currentTimeMillis();
				for(WebSocket ws : websocketServer.getConnections()) {
					DummyConnection conn = ws.getAttachment();
					if (conn != null && ((conn.sendRedirectAt > 0l && millis - conn.sendRedirectAt > redirTimeout)
							|| millis - conn.age > (long) PlaceholderServerConfig.clientTimeout)) {
						ws.close();
					}
				}
			} catch (Throwable t) {
			}
		}
	}

	private PlaceholderServer(InetSocketAddress addr) {
		super(addr);
		setReuseAddr(true);
		setTcpNoDelay(true);
	}

	@Override
	public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
	}

	@Override
	public void onError(WebSocket arg0, Exception arg1) {
		System.err.println();
		if(arg0 != null) {
			System.err.println("Caught WebSocket exception on " + arg0.getRemoteSocketAddress() + "!");
			arg0.close();
		}else {
			System.err.println("Caught WebSocket exception!");
		}
		arg1.printStackTrace();
		if(!isOpen) {
			System.exit(-1);
		}
	}

	@Override
	public void onMessage(WebSocket arg0, String arg1) {
		((DummyConnection)arg0.getAttachment()).processString(arg1);
	}

	@Override
	public void onMessage(WebSocket arg0, ByteBuffer arg1) {
		((DummyConnection)arg0.getAttachment()).processBinary(arg1);
	}

	@Override
	public void onOpen(WebSocket arg0, ClientHandshake arg1) {
		arg0.setAttachment(new DummyConnection(arg0));
	}

	@Override
	public void onStart() {
		System.out.println();
		System.out.println("Listening on: " + getAddress());
		isOpen = true;
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					PlaceholderServer.this.stop();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}, "Shutdown Thread"));
		
		System.out.println("Use CTRL+C to exit");
	}

}
