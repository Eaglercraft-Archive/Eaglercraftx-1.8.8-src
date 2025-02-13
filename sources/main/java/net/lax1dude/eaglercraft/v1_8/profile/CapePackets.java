/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.profile;

public class CapePackets {

	public static final int PACKET_MY_CAPE_PRESET = 0x01;
	public static final int PACKET_MY_CAPE_CUSTOM = 0x02;

	public static byte[] writeMyCapePreset(int capeId) {
		return new byte[] { (byte) PACKET_MY_CAPE_PRESET, (byte) (capeId >>> 24), (byte) (capeId >>> 16),
				(byte) (capeId >>> 8), (byte) (capeId & 0xFF) };
	}

	public static byte[] writeMyCapeCustom(CustomCape customCape) {
		byte[] packet = new byte[1 + customCape.texture.length];
		packet[0] = (byte) PACKET_MY_CAPE_CUSTOM;
		System.arraycopy(customCape.texture, 0, packet, 1, customCape.texture.length);
		return packet;
	}

}