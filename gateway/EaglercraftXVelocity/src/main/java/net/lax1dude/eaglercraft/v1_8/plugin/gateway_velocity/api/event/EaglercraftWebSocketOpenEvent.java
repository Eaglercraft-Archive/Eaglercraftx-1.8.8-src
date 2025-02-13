/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event;

import io.netty.channel.Channel;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerListenerConfig;

public class EaglercraftWebSocketOpenEvent {

	private final Channel channel;
	private final EaglerListenerConfig listener;
	private final String realIP;
	private final String origin;
	private final String userAgent;
	private boolean cancelled = false;

	public EaglercraftWebSocketOpenEvent(Channel channel, EaglerListenerConfig listener, String realIP, String origin, String userAgent) {
		this.channel = channel;
		this.listener = listener;
		this.realIP = realIP;
		this.origin = origin;
		this.userAgent = userAgent;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean var1) {
		cancelled = var1;
	}

	public Channel getChannel() {
		return channel;
	}

	public EaglerListenerConfig getListener() {
		return listener;
	}

	public String getRealIP() {
		return realIP;
	}

	public String getOrigin() {
		return origin;
	}

	public String getUserAgent() {
		return userAgent;
	}

}