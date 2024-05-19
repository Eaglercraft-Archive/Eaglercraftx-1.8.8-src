package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event;

import java.util.UUID;

import net.md_5.bungee.api.plugin.Event;

/**
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
public class EaglercraftRegisterCapeEvent extends Event {

	private final String username;
	private final UUID uuid;
	private byte[] customTex = null;

	public EaglercraftRegisterCapeEvent(String username, UUID uuid) {
		this.username = username;
		this.uuid = uuid;
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
		customTex[1] = (byte)(p >> 24);
		customTex[2] = (byte)(p >> 16);
		customTex[3] = (byte)(p >> 8);
		customTex[4] = (byte)(p & 0xFF);
	}

	public void setForceUseCustom(byte[] tex) {
		customTex = new byte[1 + tex.length];
		customTex[0] = (byte)2;
		System.arraycopy(tex, 0, customTex, 1, tex.length);
	}

	public void setForceUseCustomByPacket(byte[] packet) {
		customTex = packet;
	}

	public byte[] getForceSetUseCustomPacket() {
		return customTex;
	}
}
