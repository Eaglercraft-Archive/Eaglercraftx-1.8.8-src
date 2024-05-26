package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server;

import com.velocitypowered.proxy.connection.client.ConnectedPlayer;

import io.netty.channel.Channel;

/**
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
public class EaglerConnectionInstance {

	public final Channel channel;

	public final long creationTime;
	public boolean hasBeenForwarded = false;
	
	public long lastServerPingPacket;
	public long lastClientPingPacket;
	public long lastClientPongPacket;

	public boolean isWebSocket = false;
	public boolean isRegularHttp = false;
	
	public ConnectedPlayer userConnection = null;
	public EaglerPlayerData eaglerData = null;
	
	public EaglerConnectionInstance(Channel channel) {
		this.channel = channel;
		this.creationTime = this.lastServerPingPacket = this.lastClientPingPacket =
				this.lastClientPongPacket = System.currentTimeMillis();
	}

}
