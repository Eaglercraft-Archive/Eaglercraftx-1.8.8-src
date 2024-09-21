package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
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
public class EaglercraftVoiceStatusChangeEvent extends Event {

	public static enum EnumVoiceState {
		SERVER_DISABLE, DISABLED, ENABLED;
	}

	private final ProxiedPlayer playerObj;
	private final EaglerListenerConfig listener;
	private final EaglerInitialHandler eaglerHandler;
	private final EnumVoiceState voiceStateOld;
	private final EnumVoiceState voiceStateNew;

	public EaglercraftVoiceStatusChangeEvent(ProxiedPlayer playerObj, EaglerListenerConfig listener,
			EaglerInitialHandler eaglerHandler, EnumVoiceState voiceStateOld, EnumVoiceState voiceStateNew) {
		this.playerObj = playerObj;
		this.listener = listener;
		this.eaglerHandler = eaglerHandler;
		this.voiceStateOld = voiceStateOld;
		this.voiceStateNew = voiceStateNew;
	}

	public ProxiedPlayer getPlayerObj() {
		return playerObj;
	}

	public EaglerListenerConfig getListener() {
		return listener;
	}

	public EaglerInitialHandler getEaglerHandler() {
		return eaglerHandler;
	}

	public EnumVoiceState getVoiceStateOld() {
		return voiceStateOld;
	}

	public EnumVoiceState getVoiceStateNew() {
		return voiceStateNew;
	}

}
