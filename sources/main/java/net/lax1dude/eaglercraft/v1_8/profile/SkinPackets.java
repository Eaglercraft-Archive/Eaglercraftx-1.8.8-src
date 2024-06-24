package net.lax1dude.eaglercraft.v1_8.profile;

import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.ArrayUtils;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.crypto.MD5Digest;
import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.minecraft.network.PacketBuffer;

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
	public static final int PACKET_GET_OTHER_SKIN = 0x03;
	public static final int PACKET_OTHER_SKIN_PRESET = 0x04;
	public static final int PACKET_OTHER_SKIN_CUSTOM = 0x05;
	public static final int PACKET_GET_SKIN_BY_URL = 0x06;
	public static final int PACKET_INSTALL_NEW_SKIN = 0x07;

	public static void readPluginMessage(PacketBuffer buffer, ServerSkinCache skinCache) throws IOException {
		try {
			int type = (int)buffer.readByte() & 0xFF;
			switch(type) {
			case PACKET_OTHER_SKIN_PRESET: {
				EaglercraftUUID responseUUID = buffer.readUuid();
				int responsePreset = buffer.readInt();
				if(buffer.isReadable()) {
					throw new IOException("PACKET_OTHER_SKIN_PRESET had " + buffer.readableBytes() + " remaining bytes!");
				}
				skinCache.cacheSkinPreset(responseUUID, responsePreset);
				break;
			}
			case PACKET_OTHER_SKIN_CUSTOM: {
				EaglercraftUUID responseUUID = buffer.readUuid();
				int model = (int)buffer.readByte() & 0xFF;
				SkinModel modelId;
				if(model == (byte)0xFF) {
					modelId = skinCache.getRequestedSkinType(responseUUID);
				}else {
					modelId = SkinModel.getModelFromId(model & 0x7F);
					if((model & 0x80) != 0 && modelId.sanitize) {
						modelId = SkinModel.STEVE;
					}
				}
				if(modelId.highPoly != null) {
					modelId = SkinModel.STEVE;
				}
				int bytesToRead = modelId.width * modelId.height * 4;
				byte[] readSkin = new byte[bytesToRead];
				buffer.readBytes(readSkin);
				if(buffer.isReadable()) {
					throw new IOException("PACKET_MY_SKIN_CUSTOM had " + buffer.readableBytes() + " remaining bytes!");
				}
				skinCache.cacheSkinCustom(responseUUID, readSkin, modelId);
				break;
			}
			default:
				throw new IOException("Unknown skin packet type: " + type);
			}
		}catch(IOException ex) {
			throw ex;
		}catch(Throwable t) {
			throw new IOException("Failed to parse skin packet!", t);
		}
	}

	public static byte[] writeMySkinPreset(int skinId) {
		return new byte[] { (byte) PACKET_MY_SKIN_PRESET, (byte) (skinId >>> 24), (byte) (skinId >>> 16),
				(byte) (skinId >>> 8), (byte) (skinId & 0xFF) };
	}

	public static byte[] writeMySkinCustom(CustomSkin customSkin) {
		byte[] packet = new byte[2 + customSkin.texture.length];
		packet[0] = (byte) PACKET_MY_SKIN_CUSTOM;
		packet[1] = (byte) customSkin.model.id;
		System.arraycopy(customSkin.texture, 0, packet, 2, customSkin.texture.length);
		return packet;
	}

	public static PacketBuffer writeCreateCustomSkull(byte[] customSkin) {
		int len = 3 + customSkin.length;
		PacketBuffer ret = new PacketBuffer(Unpooled.buffer(len, len));
		ret.writeByte(PACKET_INSTALL_NEW_SKIN);
		ret.writeShort(customSkin.length);
		ret.writeBytes(customSkin);
		return ret;
	}

	public static PacketBuffer writeGetOtherSkin(EaglercraftUUID skinId) throws IOException {
		PacketBuffer ret = new PacketBuffer(Unpooled.buffer(17, 17));
		ret.writeByte(PACKET_GET_OTHER_SKIN);
		ret.writeUuid(skinId);
		return ret;
	}

	public static PacketBuffer writeGetSkinByURL(EaglercraftUUID skinId, String skinUrl) throws IOException {
		int len = 19 + skinUrl.length();
		PacketBuffer ret = new PacketBuffer(Unpooled.buffer(len, len));
		ret.writeByte(PACKET_GET_SKIN_BY_URL);
		ret.writeUuid(skinId);
		byte[] url = ArrayUtils.asciiString(skinUrl);
		ret.writeShort((int)url.length);
		ret.writeBytes(url);
		return ret;
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
