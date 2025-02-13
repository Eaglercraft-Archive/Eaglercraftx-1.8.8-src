/*
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event;

import java.net.InetAddress;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
import net.md_5.bungee.api.plugin.Event;

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
	private boolean enableCookieAuth;
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

	public boolean getEnableCookieAuth() {
		return enableCookieAuth;
	}

	public void setEnableCookieAuth(boolean enable) {
		this.enableCookieAuth = enable;
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