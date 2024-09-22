package net.lax1dude.eaglercraft.v1_8.profile;

import net.lax1dude.eaglercraft.v1_8.ArrayUtils;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.crypto.MD5Digest;

/**
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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
public class SkinPackets {

	public static final int PACKET_MY_SKIN_PRESET = 0x01;
	public static final int PACKET_MY_SKIN_CUSTOM = 0x02;

	public static byte[] writeMySkinPreset(int skinId) {
		return new byte[] { (byte) PACKET_MY_SKIN_PRESET, (byte) (skinId >>> 24), (byte) (skinId >>> 16),
				(byte) (skinId >>> 8), (byte) (skinId & 0xFF) };
	}

	public static byte[] writeMySkinCustomV3(CustomSkin customSkin) {
		byte[] packet = new byte[2 + 16384];
		packet[0] = (byte) PACKET_MY_SKIN_CUSTOM;
		packet[1] = (byte) customSkin.model.id;
		System.arraycopy(customSkin.texture, 0, packet, 2, 16384);
		return packet;
	}

	public static byte[] writeMySkinCustomV4(CustomSkin customSkin) {
		byte[] packet = new byte[2 + 12288];
		packet[0] = (byte) PACKET_MY_SKIN_CUSTOM;
		packet[1] = (byte) customSkin.model.id;
		byte[] v3data = customSkin.texture;
		for(int i = 0, j, k; i < 4096; ++i) {
			j = i << 2;
			k = i * 3 + 2;
			packet[k] = v3data[j + 1];
			packet[k + 1] = v3data[j + 2];
			packet[k + 2] = (byte)((v3data[j + 3] >>> 1) | (v3data[j] & 0x80));
		}
		return packet;
	}

	public static EaglercraftUUID createEaglerURLSkinUUID(String skinUrl){
		MD5Digest dg = new MD5Digest();
		byte[] bytes = ArrayUtils.asciiString("EaglercraftSkinURL:" + skinUrl);
		dg.update(bytes, 0, bytes.length);
		byte[] md5Bytes = new byte[16];
		dg.doFinal(md5Bytes, 0);
		md5Bytes[6] &= 0x0f;
		md5Bytes[6] |= 0x30;
		md5Bytes[8] &= 0x3f;
		md5Bytes[8] |= 0x80;
		return new EaglercraftUUID(md5Bytes);
	}

}
