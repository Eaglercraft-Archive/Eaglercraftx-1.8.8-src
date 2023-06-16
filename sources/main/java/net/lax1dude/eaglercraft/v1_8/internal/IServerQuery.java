package net.lax1dude.eaglercraft.v1_8.internal;

import org.json.JSONObject;

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
public interface IServerQuery {

	public static final long defaultTimeout = 10000l;

	public static enum QueryReadyState {
		CONNECTING(true, false), OPEN(true, false), CLOSED(false, true), FAILED(false, true);
		
		private final boolean open;
		private final boolean closed;
		
		private QueryReadyState(boolean open, boolean closed) {
			this.open = open;
			this.closed = closed;
		}
		
		public boolean isOpen() {
			return open;
		}
		
		public boolean isClosed() {
			return closed;
		}

	}

	void send(String str);

	default void send(JSONObject json) {
		send(json.toString());
	}

	void send(byte[] bytes);

	int responsesAvailable();

	QueryResponse getResponse();

	int binaryResponsesAvailable();

	byte[] getBinaryResponse();

	QueryReadyState readyState();

	default boolean isOpen() {
		return readyState().isOpen();
	}

	default boolean isClosed() {
		return readyState().isClosed();
	}

	void close();

	EnumServerRateLimit getRateLimit();

	default boolean awaitResponseAvailable(long timeout) {
		long start = System.currentTimeMillis();
		while(isOpen() && responsesAvailable() <= 0 && (timeout <= 0l || System.currentTimeMillis() - start < timeout)) {
			try {
				Thread.sleep(0l, 250000);
			} catch (InterruptedException e) {
			}
		}
		return responsesAvailable() > 0;
	}
	
	default boolean awaitResponseAvailable() {
		return awaitResponseAvailable(defaultTimeout);
	}
	
	default boolean awaitResponseBinaryAvailable(long timeout) {
		long start = System.currentTimeMillis();
		while(isOpen() && binaryResponsesAvailable() <= 0 && (timeout <= 0l || System.currentTimeMillis() - start < timeout)) {
			try {
				Thread.sleep(0l, 250000);
			} catch (InterruptedException e) {
			}
		}
		return binaryResponsesAvailable() > 0;
	}

	default boolean awaitResponseBinaryAvailable() {
		return awaitResponseBinaryAvailable(defaultTimeout);
	}

	default QueryResponse awaitResponse(long timeout) {
		return awaitResponseAvailable(timeout) ? getResponse() : null;
	}
	
	default QueryResponse awaitResponse() {
		return awaitResponseAvailable() ? getResponse() : null;
	}
	
	default byte[] awaitResponseBinary(long timeout) {
		return awaitResponseBinaryAvailable(timeout) ? getBinaryResponse() : null;
	}

	default byte[] awaitResponseBinary() {
		return awaitResponseBinaryAvailable() ? getBinaryResponse() : null;
	}

}
