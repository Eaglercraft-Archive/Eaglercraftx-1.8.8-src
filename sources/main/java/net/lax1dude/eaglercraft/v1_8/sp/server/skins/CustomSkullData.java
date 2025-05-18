/*
 * Copyright (c) 2023-2025 lax1dude. All Rights Reserved.
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

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinCustomV3EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinCustomV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinCustomV5EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SkinPacketVersionCache;

public class CustomSkullData {

	protected long lastHit;
	protected byte[] textureDataV3;
	protected byte[] textureDataV4;

	public CustomSkullData(byte[] skinData) {
		if(skinData.length != 16384) {
			byte[] fixed = new byte[16384];
			System.arraycopy(skinData, 0, fixed, 0, skinData.length > fixed.length ? fixed.length : skinData.length);
			skinData = fixed;
		}
		textureDataV3 = skinData;
		lastHit = EagRuntime.steadyTimeMillis();
	}

	public byte[] getFullSkin() {
		return textureDataV3;
	}

	public GameMessagePacket getSkin(long uuidMost, long uuidLeast, GamePluginMessageProtocol protocol) {
		switch(protocol) {
		case V3:
			return new SPacketOtherSkinCustomV3EAG(uuidMost, uuidLeast, 0xFF, textureDataV3);
		case V4:
			if(textureDataV4 == null) {
				textureDataV4 = SkinPacketVersionCache.convertToV4Raw(textureDataV3);
			}
			return new SPacketOtherSkinCustomV4EAG(uuidMost, uuidLeast, 0xFF, textureDataV4);
		default:
			throw new IllegalStateException();
		}
	}

	public GameMessagePacket getSkinV5(int requestId, GamePluginMessageProtocol protocol) {
		if(protocol.ver >= 5) {
			if(textureDataV4 == null) {
				textureDataV4 = SkinPacketVersionCache.convertToV4Raw(textureDataV3);
			}
			return new SPacketOtherSkinCustomV5EAG(requestId, 0xFF, textureDataV4);
		}else {
			throw new IllegalStateException();
		}
	}

}