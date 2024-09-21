package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
import net.md_5.bungee.api.plugin.Event;

/**
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
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
public class EaglercraftHandleAuthCookieEvent extends Event {

	public static enum AuthResponse {
		ALLOW, DENY, REQUIRE_AUTH
	}

	private final EaglerListenerConfig listener;
	private final InetAddress authRemoteAddress;
	private final String authOrigin;
	private final boolean enableCookies;
	private final byte[] cookieData;
	private final EaglercraftIsAuthRequiredEvent.AuthMethod eventAuthMethod;
	private final String eventAuthMessage;
	private final Object authAttachment;

	private AuthResponse eventResponse;
	private byte[] authUsername;
	private String authProfileUsername;
	private UUID authProfileUUID;
	private String authRequestedServerRespose;
	private String authDeniedMessage = "Bad Cookie!";
	private String applyTexturesPropValue;
	private String applyTexturesPropSignature;
	private boolean overrideEaglerToVanillaSkins;
	private Consumer<EaglercraftHandleAuthCookieEvent> continueThread;
	private Runnable continueRunnable;
	private volatile boolean hasContinue = false;

	public EaglercraftHandleAuthCookieEvent(EaglerListenerConfig listener, InetAddress authRemoteAddress,
			String authOrigin, byte[] authUsername, String authProfileUsername, UUID authProfileUUID,
			boolean enableCookies, byte[] cookieData, EaglercraftIsAuthRequiredEvent.AuthMethod eventAuthMethod,
			String eventAuthMessage, Object authAttachment, String authRequestedServerRespose,
			Consumer<EaglercraftHandleAuthCookieEvent> continueThread) {
		this.listener = listener;
		this.authRemoteAddress = authRemoteAddress;
		this.authOrigin = authOrigin;
		this.authUsername = authUsername;
		this.authProfileUsername = authProfileUsername;
		this.authProfileUUID = authProfileUUID;
		this.enableCookies = enableCookies;
		this.cookieData = cookieData;
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

	public boolean getCookiesEnabled() {
		return enableCookies;
	}

	public byte[] getCookieData() {
		return cookieData;
	}

	public String getCookieDataString() {
		return cookieData != null ? new String(cookieData, StandardCharsets.UTF_8) : null;
	}

	public byte[] getAuthUsername() {
		return authUsername;
	}

	public String getProfileUsername() {
		return authProfileUsername;
	}

	public void setProfileUsername(String username) {
		this.authProfileUsername = username;
	}

	public UUID getProfileUUID() {
		return authProfileUUID;
	}

	public void setProfileUUID(UUID uuid) {
		this.authProfileUUID = uuid;
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

	public void setLoginPasswordRequired() {
		this.eventResponse = AuthResponse.REQUIRE_AUTH;
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
						continueThread.accept(EaglercraftHandleAuthCookieEvent.this);
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
