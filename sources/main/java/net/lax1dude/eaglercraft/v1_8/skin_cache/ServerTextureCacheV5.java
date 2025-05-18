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

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.predicates.IntObjectPredicate;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.*;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.*;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.texture.TextureManager;

public class ServerTextureCacheV5 extends ServerTextureCache {

	private static abstract class PendingLookup {

		protected final EaglercraftUUID uuid;
		protected final long expiresAt = EagRuntime.steadyTimeMillis() + (30l * 1000l);

		protected PendingLookup(EaglercraftUUID uuid) {
			this.uuid = uuid;
		}

		protected abstract void handleResult(GameMessagePacket packet);

		protected abstract void handleTimeout();

	}

	private final IntObjectMap<PendingLookup> pendingSkinLookup = new IntObjectHashMap<>(64);
	private final IntObjectMap<PendingLookup> pendingCapeLookup = new IntObjectHashMap<>(64);
	private final IntObjectMap<PendingLookup> pendingTextureLookup = new IntObjectHashMap<>(64);

	private int lookupIdA = 0;
	private int lookupIdB = 0;
	private int lookupIdC = 0;

	public ServerTextureCacheV5(NetHandlerPlayClient netHandler, TextureManager textureManager) {
		super(netHandler, textureManager);
	}

	private int nextLookupIdA() {
		return lookupIdA = (lookupIdA + 1) & 0x3FFF;
	}

	private int nextLookupIdB() {
		return lookupIdB = (lookupIdB + 1) & 0x3FFF;
	}

	private int nextLookupIdC() {
		return lookupIdC = (lookupIdC + 1) & 0x3FFF;
	}

	@Override
	protected void sendSkinRequest(PlayerTextureEntry etr) {
		int lookupId = nextLookupIdA();
		pendingSkinLookup.put(lookupId, new PendingLookup(etr.uuid) {
			@Override
			protected void handleResult(GameMessagePacket packet) {
				etr.state &= ~STATE_S_PENDING;
				if(packet instanceof SPacketOtherSkinPresetV5EAG) {
					SPacketOtherSkinPresetV5EAG pkt = (SPacketOtherSkinPresetV5EAG) packet;
					etr.handleSkinResultPreset(pkt.presetSkin);
				}else if(packet instanceof SPacketOtherSkinCustomV5EAG) {
					SPacketOtherSkinCustomV5EAG pkt = (SPacketOtherSkinCustomV5EAG) packet;
					etr.handleSkinResultCustomV4(pkt.customSkin, pkt.modelID);
				}else {
					throw new IllegalStateException();
				}
			}
			@Override
			protected void handleTimeout() {
				etr.state &= ~STATE_S_PENDING;
			}
		});
		netHandler.sendEaglerMessage(new CPacketGetOtherSkinV5EAG(lookupId, etr.uuid.getMostSignificantBits(),
				etr.uuid.getLeastSignificantBits()));
	}

	@Override
	protected void sendCapeRequest(PlayerTextureEntry etr) {
		int lookupId = nextLookupIdB();
		pendingCapeLookup.put(lookupId, new PendingLookup(etr.uuid) {
			@Override
			protected void handleResult(GameMessagePacket packet) {
				etr.state &= ~STATE_C_PENDING;
				if(packet instanceof SPacketOtherCapePresetV5EAG) {
					SPacketOtherCapePresetV5EAG pkt = (SPacketOtherCapePresetV5EAG) packet;
					etr.handleCapeResultPreset(pkt.presetCape);
				}else if(packet instanceof SPacketOtherCapeCustomV5EAG) {
					SPacketOtherCapeCustomV5EAG pkt = (SPacketOtherCapeCustomV5EAG) packet;
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
		netHandler.sendEaglerMessage(new CPacketGetOtherCapeV5EAG(lookupId, etr.uuid.getMostSignificantBits(),
				etr.uuid.getLeastSignificantBits()));
	}

	@Override
	protected void sendSkinRequest(ForeignTextureEntry etr) {
		int lookupId = nextLookupIdA();
		pendingSkinLookup.put(lookupId, new PendingLookup(null) {
			@Override
			protected void handleResult(GameMessagePacket packet) {
				etr.state &= ~STATE_S_PENDING;
				if(packet instanceof SPacketOtherSkinPresetV5EAG) {
					SPacketOtherSkinPresetV5EAG pkt = (SPacketOtherSkinPresetV5EAG) packet;
					etr.handleSkinResultPreset(pkt.presetSkin);
				}else if(packet instanceof SPacketOtherSkinCustomV5EAG) {
					SPacketOtherSkinCustomV5EAG pkt = (SPacketOtherSkinCustomV5EAG) packet;
					etr.handleSkinResultCustomV4(pkt.customSkin, pkt.modelID);
				}else {
					throw new IllegalStateException();
				}
			}
			@Override
			protected void handleTimeout() {
				etr.state &= ~STATE_S_PENDING;
			}
		});
		netHandler.sendEaglerMessage(new CPacketGetSkinByURLV5EAG(lookupId, etr.url));
	}

	@Override
	protected void sendTexturesRequest(PlayerTextureEntry etr) {
		int lookupId = nextLookupIdC();
		pendingTextureLookup.put(lookupId, new PendingLookup(etr.uuid) {
			@Override
			protected void handleResult(GameMessagePacket packet) {
				etr.state &= ~STATE_SC_PENDING;
				if(packet instanceof SPacketOtherTexturesV5EAG) {
					SPacketOtherTexturesV5EAG pkt = (SPacketOtherTexturesV5EAG) packet;
					if(pkt.skinID >= 0) {
						etr.handleSkinResultPreset(pkt.skinID);
					}else {
						etr.handleSkinResultCustomV4(pkt.customSkin, -pkt.skinID - 1);
					}
					if(pkt.capeID >= 0) {
						etr.handleCapeResultPreset(pkt.capeID);
					}else {
						etr.handleCapeResultCustom(pkt.customCape);
					}
				}else {
					throw new IllegalStateException();
				}
			}
			@Override
			protected void handleTimeout() {
				etr.state &= ~STATE_SC_PENDING;
			}
		});
		netHandler.sendEaglerMessage(new CPacketGetOtherTexturesV5EAG(lookupId, etr.uuid.getMostSignificantBits(),
				etr.uuid.getLeastSignificantBits()));
	}

	@Override
	protected void _dropPlayer(EaglercraftUUID playerUUID, boolean skin, boolean cape) {
		IntObjectPredicate<PendingLookup> pred = (i, o) -> {
			return o.uuid != null && playerUUID.equals(o.uuid);
		};
		if(skin) {
			pendingSkinLookup.removeAll(pred);
		}
		if(cape) {
			pendingCapeLookup.removeAll(pred);
		}
		pendingTextureLookup.removeAll(pred);
	}

	@Override
	protected void lookupFlush(long now) {
		IntObjectPredicate<PendingLookup> pred = (i, o) -> {
			return now > o.expiresAt;
		};
		pendingSkinLookup.removeAll(pred);
		pendingCapeLookup.removeAll(pred);
		pendingTextureLookup.removeAll(pred);
	}

	@Override
	public void handlePacket(SPacketOtherSkinPresetEAG packet) {
		throw new IllegalStateException();
	}

	@Override
	public void handlePacket(SPacketOtherSkinPresetV5EAG packet) {
		PendingLookup lookup = pendingSkinLookup.remove(packet.requestId);
		if(lookup != null) {
			lookup.handleResult(packet);
		}
	}

	@Override
	public void handlePacket(SPacketOtherSkinCustomV3EAG packet) {
		throw new IllegalStateException();
	}

	@Override
	public void handlePacket(SPacketOtherSkinCustomV4EAG packet) {
		throw new IllegalStateException();
	}

	@Override
	public void handlePacket(SPacketOtherSkinCustomV5EAG packet) {
		PendingLookup lookup = pendingSkinLookup.remove(packet.requestId);
		if(lookup != null) {
			lookup.handleResult(packet);
		}
	}

	@Override
	public void handlePacket(SPacketOtherCapePresetEAG packet) {
		throw new IllegalStateException();
	}

	@Override
	public void handlePacket(SPacketOtherCapePresetV5EAG packet) {
		PendingLookup lookup = pendingCapeLookup.remove(packet.requestId);
		if(lookup != null) {
			lookup.handleResult(packet);
		}
	}

	@Override
	public void handlePacket(SPacketOtherCapeCustomEAG packet) {
		throw new IllegalStateException();
	}

	@Override
	public void handlePacket(SPacketOtherCapeCustomV5EAG packet) {
		PendingLookup lookup = pendingCapeLookup.remove(packet.requestId);
		if(lookup != null) {
			lookup.handleResult(packet);
		}
	}

	@Override
	public void handlePacket(SPacketOtherTexturesV5EAG packet) {
		PendingLookup lookup = pendingTextureLookup.remove(packet.requestId);
		if(lookup != null) {
			lookup.handleResult(packet);
		}
	}

}
