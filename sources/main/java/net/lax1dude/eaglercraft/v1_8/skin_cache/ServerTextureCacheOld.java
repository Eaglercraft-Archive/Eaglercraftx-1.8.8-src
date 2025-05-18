/*
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.skin_cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.*;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.*;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.texture.TextureManager;

public class ServerTextureCacheOld extends ServerTextureCache {

	private static abstract class PendingLookup {

		protected final long expiresAt = EagRuntime.steadyTimeMillis() + (30l * 1000l);

		protected abstract void handleResult(GameMessagePacket packet);

		protected abstract void handleTimeout();

	}

	private final Map<EaglercraftUUID, PendingLookup> pendingSkinLookup = new HashMap<>(64);
	private final Map<EaglercraftUUID, PendingLookup> pendingCapeLookup = new HashMap<>(64);

	public ServerTextureCacheOld(NetHandlerPlayClient netHandler, TextureManager textureManager) {
		super(netHandler, textureManager);
		if(protocolVers > 4) {
			throw new IllegalStateException();
		}
	}

	@Override
	protected void _dropPlayer(EaglercraftUUID playerUUID, boolean skin, boolean cape) {
		if(skin) {
			pendingSkinLookup.remove(playerUUID);
		}
		if(cape) {
			pendingCapeLookup.remove(playerUUID);
		}
	}

	@Override
	protected void sendSkinRequest(PlayerTextureEntry etr) {
		pendingSkinLookup.put(etr.uuid, new PendingLookup() {
			@Override
			protected void handleResult(GameMessagePacket packet) {
				etr.state &= ~STATE_S_PENDING;
				if(packet instanceof SPacketOtherSkinPresetEAG) {
					SPacketOtherSkinPresetEAG pkt = (SPacketOtherSkinPresetEAG) packet;
					etr.handleSkinResultPreset(pkt.presetSkin);
				}else if(packet instanceof SPacketOtherSkinCustomV4EAG) {
					SPacketOtherSkinCustomV4EAG pkt = (SPacketOtherSkinCustomV4EAG) packet;
					etr.handleSkinResultCustomV4(pkt.customSkin, pkt.modelID);
				}else if(packet instanceof SPacketOtherSkinCustomV3EAG) {
					SPacketOtherSkinCustomV3EAG pkt = (SPacketOtherSkinCustomV3EAG) packet;
					etr.handleSkinResultCustomV3(pkt.customSkin, pkt.modelID);
				}else {
					throw new IllegalStateException();
				}
			}
			@Override
			protected void handleTimeout() {
				etr.state &= ~STATE_S_PENDING;
			}
		});
		netHandler.sendEaglerMessage(new CPacketGetOtherSkinEAG(etr.uuid.getMostSignificantBits(),
				etr.uuid.getLeastSignificantBits()));
	}

	@Override
	protected void sendCapeRequest(PlayerTextureEntry etr) {
		pendingCapeLookup.put(etr.uuid, new PendingLookup() {
			@Override
			protected void handleResult(GameMessagePacket packet) {
				etr.state &= ~STATE_C_PENDING;
				if(packet instanceof SPacketOtherCapePresetEAG) {
					SPacketOtherCapePresetEAG pkt = (SPacketOtherCapePresetEAG) packet;
					etr.handleCapeResultPreset(pkt.presetCape);
				}else if(packet instanceof SPacketOtherCapeCustomEAG) {
					SPacketOtherCapeCustomEAG pkt = (SPacketOtherCapeCustomEAG) packet;
					etr.handleCapeResultCustom(pkt.customCape);
				}else {
					throw new IllegalStateException();
				}
			}
			@Override
			protected void handleTimeout() {
				etr.state &= ~STATE_C_PENDING;
			}
		});
		netHandler.sendEaglerMessage(new CPacketGetOtherCapeEAG(etr.uuid.getMostSignificantBits(),
				etr.uuid.getLeastSignificantBits()));
	}

	@Override
	protected void sendSkinRequest(ForeignTextureEntry etr) {
		pendingSkinLookup.put(etr.uuid, new PendingLookup() {
			@Override
			protected void handleResult(GameMessagePacket packet) {
				etr.state &= ~STATE_S_PENDING;
				if(packet instanceof SPacketOtherSkinPresetEAG) {
					SPacketOtherSkinPresetEAG pkt = (SPacketOtherSkinPresetEAG) packet;
					etr.handleSkinResultPreset(pkt.presetSkin);
				}else if(packet instanceof SPacketOtherSkinCustomV4EAG) {
					SPacketOtherSkinCustomV4EAG pkt = (SPacketOtherSkinCustomV4EAG) packet;
					etr.handleSkinResultCustomV4(pkt.customSkin, pkt.modelID);
				}else if(packet instanceof SPacketOtherSkinCustomV3EAG) {
					SPacketOtherSkinCustomV3EAG pkt = (SPacketOtherSkinCustomV3EAG) packet;
					etr.handleSkinResultCustomV3(pkt.customSkin, pkt.modelID);
				}else {
					throw new IllegalStateException();
				}
			}
			@Override
			protected void handleTimeout() {
				etr.state &= ~STATE_S_PENDING;
			}
		});
		netHandler.sendEaglerMessage(new CPacketGetSkinByURLEAG(etr.uuid.getMostSignificantBits(),
				etr.uuid.getLeastSignificantBits(), etr.url));
	}

	@Override
	protected void sendTexturesRequest(PlayerTextureEntry etr) {
		throw new IllegalStateException();
	}

	@Override
	protected void lookupFlush(long now) {
		lookupFlush(pendingSkinLookup, now);
		lookupFlush(pendingCapeLookup, now);
	}

	private void lookupFlush(Map<?, PendingLookup> pending, long now) {
		if(!pending.isEmpty()) {
			Iterator<PendingLookup> itr1 = pending.values().iterator();
			while(itr1.hasNext()) {
				PendingLookup etr = itr1.next();
				if(now > etr.expiresAt) {
					itr1.remove();
					etr.handleTimeout();
				}
			}
		}
	}

	@Override
	public void handlePacket(SPacketOtherSkinPresetEAG packet) {
		PendingLookup lookup = pendingSkinLookup.remove(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast));
		if(lookup != null) {
			lookup.handleResult(packet);
		}
	}

	@Override
	public void handlePacket(SPacketOtherSkinPresetV5EAG packet) {
		throw new IllegalStateException();
	}

	@Override
	public void handlePacket(SPacketOtherSkinCustomV3EAG packet) {
		PendingLookup lookup = pendingSkinLookup.remove(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast));
		if(lookup != null) {
			lookup.handleResult(packet);
		}
	}

	@Override
	public void handlePacket(SPacketOtherSkinCustomV4EAG packet) {
		PendingLookup lookup = pendingSkinLookup.remove(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast));
		if(lookup != null) {
			lookup.handleResult(packet);
		}
	}

	@Override
	public void handlePacket(SPacketOtherSkinCustomV5EAG packet) {
		throw new IllegalStateException();
	}

	@Override
	public void handlePacket(SPacketOtherCapePresetEAG packet) {
		PendingLookup lookup = pendingCapeLookup.remove(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast));
		if(lookup != null) {
			lookup.handleResult(packet);
		}
	}

	@Override
	public void handlePacket(SPacketOtherCapePresetV5EAG packet) {
		throw new IllegalStateException();
	}

	@Override
	public void handlePacket(SPacketOtherCapeCustomEAG packet) {
		PendingLookup lookup = pendingCapeLookup.remove(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast));
		if(lookup != null) {
			lookup.handleResult(packet);
		}
	}

	@Override
	public void handlePacket(SPacketOtherCapeCustomV5EAG packet) {
		throw new IllegalStateException();
	}

	@Override
	public void handlePacket(SPacketOtherTexturesV5EAG packet) {
		throw new IllegalStateException();
	}

}
