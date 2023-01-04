package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event;

import java.net.InetAddress;
import java.util.UUID;
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
public class EaglercraftHandleAuthPasswordEvent extends Event {

	public static enum AuthResponse {
		ALLOW, DENY
	}

	private final EaglerListenerConfig listener;
	private final InetAddress authRemoteAddress; 
	private final String authOrigin; 
	private final byte[] authUsername;
	private final byte[] authSaltingData;
	private final byte[] authPasswordData;
	private final EaglercraftIsAuthRequiredEvent.AuthMethod eventAuthMethod;
	private final String eventAuthMessage;
	private final Object authAttachment;

	private AuthResponse eventResponse;
	private CharSequence authProfileUsername;
	private UUID authProfileUUID;
	private String authRequestedServerRespose;
	private String authDeniedMessage = "Password Incorrect!";
	private String applyTexturesPropValue;
	private String applyTexturesPropSignature;
	private boolean overrideEaglerToVanillaSkins;
	private Consumer<EaglercraftHandleAuthPasswordEvent> continueThread;
	private Runnable continueRunnable;
	private volatile boolean hasContinue = false;

	public EaglercraftHandleAuthPasswordEvent(EaglerListenerConfig listener, InetAddress authRemoteAddress,
			String authOrigin, byte[] authUsername, byte[] authSaltingData, CharSequence authProfileUsername,
			UUID authProfileUUID, byte[] authPasswordData, EaglercraftIsAuthRequiredEvent.AuthMethod eventAuthMethod,
			String eventAuthMessage, Object authAttachment, String authRequestedServerRespose,
			Consumer<EaglercraftHandleAuthPasswordEvent> continueThread) {
		this.listener = listener;
		this.authRemoteAddress = authRemoteAddress;
		this.authOrigin = authOrigin;
		this.authUsername = authUsername;
		this.authSaltingData = authSaltingData;
		this.authProfileUsername = authProfileUsername;
		this.authProfileUUID = authProfileUUID;
		this.authPasswordData = authPasswordData;
		this.eventAuthMethod = eventAuthMethod;
		this.eventAuthMessage = eventAuthMessage;
		this.authAttachment = authAttachment;
		this.authRequestedServerRespose = authRequestedServerRespose;
		this.continueThread = continueThread;
	}

	public EaglerListenerConfig getListener() {
		return listener;
	}

	public InetAddress getRemoteAddress() {
		return authRemoteAddress;
	}

	public String getOriginHeader() {
		return authOrigin;
	}

	public byte[] getAuthUsername() {
		return authUsername;
	}

	public byte[] getAuthSaltingData() {
		return authSaltingData;
	}

	public CharSequence getProfileUsername() {
		return authProfileUsername;
	}

	public void setProfileUsername(CharSequence username) {
		this.authProfileUsername = username;
	}

	public UUID getProfileUUID() {
		return authProfileUUID;
	}

	public void setProfileUUID(UUID uuid) {
		this.authProfileUUID = uuid;
	}

	public byte[] getAuthPasswordDataResponse() {
		return authPasswordData;
	}

	public EaglercraftIsAuthRequiredEvent.AuthMethod getAuthType() {
		return eventAuthMethod;
	}

	public String getAuthMessage() {
		return eventAuthMessage;
	}

	public <T> T getAuthAttachment() {
		return (T)authAttachment;
	}

	public String getAuthRequestedServer() {
		return authRequestedServerRespose;
	}

	public void setAuthRequestedServer(String server) {
		this.authRequestedServerRespose = server;
	}

	public void setLoginAllowed() {
		this.eventResponse = AuthResponse.ALLOW;
		this.authDeniedMessage = null;
	}

	public void setLoginDenied(String message) {
		this.eventResponse = AuthResponse.DENY;
		this.authDeniedMessage = message;
	}

	public AuthResponse getLoginAllowed() {
		return eventResponse;
	}

	public String getLoginDeniedMessage() {
		return authDeniedMessage;
	}

	public Runnable makeAsyncContinue() {
		if(continueRunnable == null) {
			continueRunnable = new Runnable() {
				@Override
				public void run() {
					if(!hasContinue) {
						hasContinue = true;
						continueThread.accept(EaglercraftHandleAuthPasswordEvent.this);
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

	public void applyTexturesProperty(String value, String signature) {
		applyTexturesPropValue = value;
		applyTexturesPropSignature = signature;
	}

	public String getApplyTexturesPropertyValue() {
		return applyTexturesPropValue;
	}

	public String getApplyTexturesPropertySignature() {
		return applyTexturesPropSignature;
	}

	public void setOverrideEaglerToVanillaSkins(boolean overrideEaglerToVanillaSkins) {
		this.overrideEaglerToVanillaSkins = overrideEaglerToVanillaSkins;
	}

	public boolean isOverrideEaglerToVanillaSkins() {
		return overrideEaglerToVanillaSkins;
	}
}
