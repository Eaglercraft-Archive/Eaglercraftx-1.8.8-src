package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

/**
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
public class EaglercraftWebViewChannelEvent extends Event {

	public static enum EventType {
		CHANNEL_OPEN, CHANNEL_CLOSE;
	}

	private final ProxiedPlayer player;
	private final EaglerListenerConfig listener;
	private final String channel;
	private final EventType type;

	public EaglercraftWebViewChannelEvent(ProxiedPlayer player, EaglerListenerConfig listener, String channel, EventType type) {
		this.player = player;
		this.listener = listener;
		this.channel = channel;
		this.type = type;
	}

	public ProxiedPlayer getPlayer() {
		return player;
	}

	public EaglerListenerConfig getListener() {
		return listener;
	}

	public String getChannel() {
		return channel;
	}

	public EventType getType() {
		return type;
	}

}
