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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event;

import java.util.UUID;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.EaglerXVelocityAPIHelper;

public class EaglercraftRegisterCapeEvent {

	private final Object authAttachment;
	private final String username;
	private final UUID uuid;
	private byte[] customTex = null;

	public EaglercraftRegisterCapeEvent(String username, UUID uuid, Object authAttachment) {
		this.username = username;
		this.uuid = uuid;
		this.authAttachment = authAttachment;
	}

	public String getUsername() {
		return username;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setForceUsePreset(int p) {
		customTex = new byte[5];
		customTex[0] = (byte)1;
		customTex[1] = (byte)(p >>> 24);
		customTex[2] = (byte)(p >>> 16);
		customTex[3] = (byte)(p >>> 8);
		customTex[4] = (byte)(p & 0xFF);
	}

	/**
	 * @param tex raw 32x32 pixel RGBA texture (4096 bytes long), see capes in "sources/resources/assets/eagler/capes"
	 */
	public void setForceUseCustom(byte[] tex) {
		customTex = new byte[1174];
		customTex[0] = (byte)2;
		EaglerXVelocityAPIHelper.convertCape32x32RGBAto23x17RGB(tex, 0, customTex, 1);
	}

	public void setForceUseCustomByPacket(byte[] packet) {
		customTex = packet;
	}

	public byte[] getForceSetUseCustomPacket() {
		return customTex;
	}

	public <T> T getAuthAttachment() {
		return (T)authAttachment;
	}
}