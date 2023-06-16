package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event;

import java.net.InetAddress;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
import net.md_5.bungee.api.plugin.Event;

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
public class EaglercraftIsAuthRequiredEvent extends Event {

	public static enum AuthResponse {
		SKIP, REQUIRE, DENY
	}

	public static enum AuthMethod {
		PLAINTEXT, EAGLER_SHA256, AUTHME_SHA256
	}

	public EaglercraftIsAuthRequiredEvent(EaglerListenerConfig listener, InetAddress authRemoteAddress,
			String authOrigin, boolean wantsAuth, byte[] authUsername,
			Consumer<EaglercraftIsAuthRequiredEvent> continueThread) {
		this.listener = listener;
		this.authRemoteAddress = authRemoteAddress;
		this.authOrigin = authOrigin;
		this.wantsAuth = wantsAuth;
		this.authUsername = authUsername;
		this.continueThread = continueThread;
	}

	private final EaglerListenerConfig listener;
	private AuthResponse authResponse; 
	private final InetAddress authRemoteAddress;
	private final String authOrigin; 
	private final boolean wantsAuth;
	private final byte[] authUsername;
	private byte[] authSaltingData;
	private AuthMethod eventAuthMethod = null;
	private String eventAuthMessage = "enter the code:";
	private String kickUserMessage = "Login Denied";
	private Object authAttachment;
	private Consumer<EaglercraftIsAuthRequiredEvent> continueThread;
	private Runnable continueRunnable;
	private volatile boolean hasContinue = false;

	public EaglerListenerConfig getListener() {
		return listener;
	}

	public InetAddress getRemoteAddress() {
		return authRemoteAddress;
	}

	public String getOriginHeader() {
		return authOrigin;
	}

	public boolean isClientSolicitingPasscode() {
		return wantsAuth;
	}

	public byte[] getAuthUsername() {
		return authUsername;
	}

	public byte[] getSaltingData() {
		return authSaltingData;
	}

	public void setSaltingData(byte[] saltingData) {
		authSaltingData = saltingData;
	}

	public AuthMethod getUseAuthType() {
		return eventAuthMethod;
	}

	public void setUseAuthMethod(AuthMethod authMethod) {
		this.eventAuthMethod = authMethod;
	}

	public AuthResponse getAuthRequired() {
		return authResponse;
	}

	public void setAuthRequired(AuthResponse required) {
		this.authResponse = required;
	}

	public String getAuthMessage() {
		return eventAuthMessage;
	}

	public void setAuthMessage(String eventAuthMessage) {
		this.eventAuthMessage = eventAuthMessage;
	}

	public <T> T getAuthAttachment() {
		return (T)authAttachment;
	}

	public void setAuthAttachment(Object authAttachment) {
		this.authAttachment = authAttachment;
	}

	public boolean shouldKickUser() {
		return authResponse == null || authResponse == AuthResponse.DENY;
	}

	public String getKickMessage() {
		return kickUserMessage;
	}

	public void kickUser(String message) {
		authResponse = AuthResponse.DENY;
		kickUserMessage = message;
	}

	public Runnable makeAsyncContinue() {
		if(continueRunnable == null) {
			continueRunnable = new Runnable() {
				@Override
				public void run() {
					if(!hasContinue) {
						hasContinue = true;
						continueThread.accept(EaglercraftIsAuthRequiredEvent.this);
					}else {
						throw new IllegalStateException("Thread was already continued from a different function! Auth plugin conflict?");
					}
				}
			};
		}
		return continueRunnable;
	}

	public boolean isAsyncContinue() {
		return continueRunnable != null;
	}

	public void doDirectContinue() {
		continueThread.accept(this);
	}

}
