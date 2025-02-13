/*
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

package net.lax1dude.eaglercraft.v1_8.sp.server.skins;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinCustomV3EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SkinPacketVersionCache;

public class CustomSkullData {

	public String skinURL;
	public long lastHit;
	public SkinPacketVersionCache skinData;

	public CustomSkullData(String skinURL, byte[] skinData) {
		this.skinURL = skinURL;
		this.lastHit = EagRuntime.steadyTimeMillis();
		if(skinData.length != 16384) {
			byte[] fixed = new byte[16384];
			System.arraycopy(skinData, 0, fixed, 0, skinData.length > fixed.length ? fixed.length : skinData.length);
			skinData = fixed;
		}
		this.skinData = SkinPacketVersionCache.createCustomV3(0l, 0l, 0, skinData);
	}

	public byte[] getFullSkin() {
		return ((SPacketOtherSkinCustomV3EAG)skinData.getV3()).customSkin;
	}

	public GameMessagePacket getSkinPacket(EaglercraftUUID uuid, GamePluginMessageProtocol protocol) {
		return SkinPacketVersionCache.rewriteUUID(skinData.get(protocol), uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
	}

}