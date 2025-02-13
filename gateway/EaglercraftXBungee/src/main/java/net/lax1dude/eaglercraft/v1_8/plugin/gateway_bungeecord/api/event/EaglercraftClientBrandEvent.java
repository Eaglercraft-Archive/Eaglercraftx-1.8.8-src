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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event;

import java.net.InetAddress;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class EaglercraftClientBrandEvent extends Event implements Cancellable {

	private final String clientBrand;
	private final String clientVersion;
	private final String origin;
	private final int protocolVersion;
	private final InetAddress remoteAddress;
	private boolean cancelled;
	private BaseComponent message;

	public EaglercraftClientBrandEvent(String clientBrand, String clientVersion, String origin, int protocolVersion,
			InetAddress remoteAddress) {
		this.clientBrand = clientBrand;
		this.clientVersion = clientVersion;
		this.origin = origin;
		this.protocolVersion = protocolVersion;
		this.remoteAddress = remoteAddress;
	}

	public BaseComponent getMessage() {
		return message;
	}

	public void setMessage(BaseComponent message) {
		this.message = message;
	}

	public String getClientBrand() {
		return clientBrand;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public String getOrigin() {
		return origin;
	}

	public int getProtocolVersion() {
		return protocolVersion;
	}

	public InetAddress getRemoteAddress() {
		return remoteAddress;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public void setKickMessage(String message) {
		this.cancelled = true;
		this.message = new TextComponent(message);
	}

	public void setKickMessage(BaseComponent message) {
		this.cancelled = true;
		this.message = message;
	}

}