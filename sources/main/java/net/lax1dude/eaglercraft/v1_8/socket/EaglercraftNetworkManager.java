package net.lax1dude.eaglercraft.v1_8.socket;

import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.internal.EnumEaglerConnectionState;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IChatComponent;

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
public abstract class EaglercraftNetworkManager {
	
	protected final String address;
	protected INetHandler nethandler = null;
	protected EnumConnectionState packetState = EnumConnectionState.HANDSHAKING;
	protected final PacketBuffer temporaryBuffer;
	protected int debugPacketCounter = 0;
	
	protected String pluginBrand = null;
	protected String pluginVersion = null;
	
	public static final Logger logger = LogManager.getLogger("NetworkManager");

	public EaglercraftNetworkManager(String address) {
		this.address = address;
		this.temporaryBuffer = new PacketBuffer(Unpooled.buffer(0x1FFFF));
	}
	
	public void setPluginInfo(String pluginBrand, String pluginVersion) {
		this.pluginBrand = pluginBrand;
		this.pluginVersion = pluginVersion;
	}
	
	public String getPluginBrand() {
		return pluginBrand;
	}
	
	public String getPluginVersion() {
		return pluginVersion;
	}
	
	public abstract void connect();
	
	public abstract EnumEaglerConnectionState getConnectStatus();
	
	public String getAddress() {
		return address;
	}
	
	public abstract void closeChannel(IChatComponent reason);
	
	public void setConnectionState(EnumConnectionState state) {
		packetState = state;
	}
	
	public abstract void processReceivedPackets() throws IOException;

	public abstract void sendPacket(Packet pkt);
	
	public void setNetHandler(INetHandler nethandler) {
		this.nethandler = nethandler;
	}
	
	public boolean isLocalChannel() {
		return false;
	}
	
	public boolean isChannelOpen() {
		return getConnectStatus() == EnumEaglerConnectionState.CONNECTED;
	}

	public boolean getIsencrypted() {
		return false;
	}

	public void setCompressionTreshold(int compressionTreshold) {
		throw new CompressionNotSupportedException();
	}

	public abstract boolean checkDisconnected();
	
	protected boolean clientDisconnected = false;
	
	protected void doClientDisconnect(IChatComponent msg) {
		if(!clientDisconnected) {
			clientDisconnected = true;
			if(nethandler != null) {
				this.nethandler.onDisconnect(msg);
			}
		}
	}
	
}
