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

package net.lax1dude.eaglercraft.v1_8.sp.server.skins;

import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherCapeCustomEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherCapeCustomV5EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherCapePresetEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherCapePresetV5EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinCustomV3EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinCustomV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinCustomV5EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinPresetEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinPresetV5EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherTexturesV5EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SkinPacketVersionCache;

public class PlayerTextureData {

	private int skinId;
	private byte[] skinTextureDataV3;
	private byte[] skinTextureDataV4;

	private int capeId;
	private byte[] capeTextureData;

	public PlayerTextureData(int skinId, byte[] skinTextureDataV3, byte[] skinTextureDataV4, int capeId,
			byte[] capeTextureData) {
		this.skinId = skinId;
		this.skinTextureDataV3 = skinTextureDataV3;
		this.skinTextureDataV4 = skinTextureDataV4;
		this.capeId = capeId;
		this.capeTextureData = capeTextureData;
	}

	private byte[] getTextureV3() {
		if (skinTextureDataV3 != null) {
			return skinTextureDataV3;
		} else {
			return skinTextureDataV3 = SkinPacketVersionCache.convertToV3Raw(skinTextureDataV4);
		}
	}

	private byte[] getTextureV4() {
		if (skinTextureDataV4 != null) {
			return skinTextureDataV4;
		} else {
			return skinTextureDataV4 = SkinPacketVersionCache.convertToV4Raw(skinTextureDataV3);
		}
	}

	public GameMessagePacket getSkin(long uuidMost, long uuidLeast, GamePluginMessageProtocol protocol) {
		switch(protocol) {
		case V3:
			if (skinId < 0) {
				return new SPacketOtherSkinCustomV3EAG(uuidMost, uuidLeast, -skinId + 1, getTextureV3());
			} else {
				return new SPacketOtherSkinPresetEAG(uuidMost, uuidLeast, skinId);
			}
		case V4:
			if (skinId < 0) {
				return new SPacketOtherSkinCustomV4EAG(uuidMost, uuidLeast, -skinId + 1, getTextureV4());
			} else {
				return new SPacketOtherSkinPresetEAG(uuidMost, uuidLeast, skinId);
			}
		default:
			throw new IllegalStateException();
		}
	}

	public GameMessagePacket getSkinV5(int requestId, GamePluginMessageProtocol protocol) {
		if(protocol.ver >= 5) {
			if (skinId < 0) {
				return new SPacketOtherSkinCustomV5EAG(requestId, -skinId + 1, getTextureV4());
			} else {
				return new SPacketOtherSkinPresetV5EAG(requestId, skinId);
			}
		}else {
			throw new IllegalStateException();
		}
	}

	public GameMessagePacket getCape(long uuidMost, long uuidLeast, GamePluginMessageProtocol protocol) {
		if(protocol.ver <= 4) {
			if (capeId < 0) {
				return new SPacketOtherCapeCustomEAG(uuidMost, uuidLeast, capeTextureData);
			} else {
				return new SPacketOtherCapePresetEAG(uuidMost, uuidLeast, capeId);
			}
		}else {
			throw new IllegalStateException();
		}
	}

	public GameMessagePacket getCapeV5(int requestId, GamePluginMessageProtocol protocol) {
		if(protocol.ver >= 5) {
			if (capeId < 0) {
				return new SPacketOtherCapeCustomV5EAG(requestId, capeTextureData);
			} else {
				return new SPacketOtherCapePresetV5EAG(requestId, capeId);
			}
		}else {
			throw new IllegalStateException();
		}
	}

	public GameMessagePacket getTexturesV5(int requestId, GamePluginMessageProtocol protocol) {
		if(protocol.ver >= 5) {
			return new SPacketOtherTexturesV5EAG(requestId, skinId, skinId < 0 ? getTextureV4() : null, capeId,
					capeId < 0 ? capeTextureData : null);
		}else {
			throw new IllegalStateException();
		}
	}

}
