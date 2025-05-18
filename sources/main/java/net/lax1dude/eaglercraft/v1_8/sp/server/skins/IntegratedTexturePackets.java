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

public class IntegratedTexturePackets {

	public static PlayerTextureData handleTextureData(byte[] skinV1, byte[] capeV1) {
		int skinId;
		byte[] skinTextureDataV3;
		eagler: {
			if (skinV1 != null && skinV1.length > 0) {
				int packetType = (int)skinV1[0] & 0xFF;
				switch (packetType) {
				case 0x01:
					if (skinV1.length == 5) {
						skinId = ((skinV1[1] & 0x7F) << 24) | ((skinV1[2] & 0xFF) << 16)
								| ((skinV1[3] & 0xFF) << 8) | (skinV1[4] & 0xFF);
						skinTextureDataV3 = null;
						break eagler;
					}
					break;
				case 0x02:
					if (skinV1.length == 16386) {
						skinId = -(Math.min((int) skinV1[1] & 0x7F, 0x7E) | 0x80) - 1;
						skinTextureDataV3 = new byte[16384];
						System.arraycopy(skinV1, 2, skinTextureDataV3, 0, 16384);
						break eagler;
					}
					break;
				default:
					break;
				}
			}
			skinId = 0;
			skinTextureDataV3 = null;
		}
		int capeId;
		byte[] capeTextureData;
		eagler: {
			if (capeV1 != null && capeV1.length > 0) {
				int packetType = (int)capeV1[0] & 0xFF;
				switch (packetType) {
				case 0x01:
					if(capeV1.length == 5) {
						capeId = ((capeV1[1] & 0x7F) << 24) | ((capeV1[2] & 0xFF) << 16)
								| ((capeV1[3] & 0xFF) << 8) | (capeV1[4] & 0xFF);
						capeTextureData = null;
						break eagler;
					}
					break;
				case 0x02:
					if (capeV1.length == 1174) {
						capeId = -1;
						capeTextureData = new byte[1173];
						System.arraycopy(capeV1, 1, capeTextureData, 0, 1173);
						break eagler;
					}
					break;
				default:
					break;
				}
			}
			capeId = 0;
			capeTextureData = null;
		}
		return new PlayerTextureData(skinId, skinTextureDataV3, null, capeId, capeTextureData);
	}

}
