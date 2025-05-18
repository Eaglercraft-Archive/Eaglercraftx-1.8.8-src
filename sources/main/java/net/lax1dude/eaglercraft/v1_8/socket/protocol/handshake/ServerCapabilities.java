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

package net.lax1dude.eaglercraft.v1_8.socket.protocol.handshake;

import com.carrotsearch.hppc.ObjectByteMap;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

public class ServerCapabilities {

	static final int VIRTUAL_V3_SERVER_CAPS = (1 << StandardCaps.UPDATE) | (1 << StandardCaps.VOICE);
	static final byte[] VIRTUAL_V3_SERVER_CAPS_VERS = new byte[] { 0, 0 };

	static final int VIRTUAL_V4_SERVER_CAPS = (1 << StandardCaps.UPDATE) | (1 << StandardCaps.VOICE)
			| (1 << StandardCaps.REDIRECT) | (1 << StandardCaps.NOTIFICATION) | (1 << StandardCaps.PAUSE_MENU)
			| (1 << StandardCaps.WEBVIEW) | (1 << StandardCaps.COOKIE);
	static final byte[] VIRTUAL_V4_SERVER_CAPS_VERS = new byte[] { 0, 0, 0, 0, 0, 0 };

	private final int standardCaps;
	private final byte[] standardCapVers;
	private final ObjectByteMap<EaglercraftUUID> extendedCaps;

	public ServerCapabilities(int standardCaps, byte[] standardCapVers, ObjectByteMap<EaglercraftUUID> extendedCaps) {
		this.standardCaps = standardCaps;
		this.standardCapVers = standardCapVers;
		this.extendedCaps = extendedCaps;
	}

	public boolean hasCapability(int cap, int ver) {
		int bit = 1 << cap;
		if((standardCaps & bit) != 0) {
			int versIndex = Integer.bitCount(standardCaps & (bit - 1));
			if(versIndex < standardCapVers.length) {
				return (standardCapVers[versIndex] & 0xFF) >= ver;
			}
		}
		return false;
	}

	public int getCapability(int cap) {
		int bit = 1 << cap;
		if((standardCaps & bit) != 0) {
			int versIndex = Integer.bitCount(standardCaps & (bit - 1));
			if(versIndex < standardCapVers.length) {
				return standardCapVers[versIndex] & 0xFF;
			}
		}
		return -1;
	}

	public int getExtCapability(EaglercraftUUID uuid) {
		if(extendedCaps != null) {
			int idx = extendedCaps.indexOf(uuid);
			if(idx >= 0) {
				return (int) extendedCaps.indexGet(idx) & 0xFF;
			}
		}
		return -1;
	}

	public static ServerCapabilities getLAN() {
		return new ServerCapabilities(VIRTUAL_V3_SERVER_CAPS, VIRTUAL_V3_SERVER_CAPS_VERS, null);
	}

}
