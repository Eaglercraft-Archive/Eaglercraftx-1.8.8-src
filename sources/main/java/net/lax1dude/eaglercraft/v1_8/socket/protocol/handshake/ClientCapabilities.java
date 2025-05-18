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

import java.util.ArrayList;
import java.util.List;

import com.carrotsearch.hppc.IntArrayList;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.update.UpdateService;
import net.lax1dude.eaglercraft.v1_8.voice.VoiceClientController;
import net.lax1dude.eaglercraft.v1_8.webview.WebViewOverlayController;

public class ClientCapabilities {

	static ClientCapabilities createCapabilities(boolean cookie) {
		ClientCapabilities caps = new ClientCapabilities();

		caps.addStandard(StandardCaps.REDIRECT, 0);
		caps.addStandard(StandardCaps.NOTIFICATION, 0);
		caps.addStandard(StandardCaps.PAUSE_MENU, 0);

		if (VoiceClientController.isClientSupported()) {
			caps.addStandard(StandardCaps.VOICE, 0);
		}

		if (UpdateService.supported()) {
			caps.addStandard(StandardCaps.UPDATE, 0);
		}

		if (WebViewOverlayController.supported() || WebViewOverlayController.fallbackSupported()) {
			caps.addStandard(StandardCaps.WEBVIEW, 0);
		}

		if (cookie) {
			caps.addStandard(StandardCaps.COOKIE, 0);
		}

		return caps;
	}

	static class ExtCapability {
		final EaglercraftUUID uuid;
		final int vers;
		ExtCapability(EaglercraftUUID uuid, int vers) {
			this.uuid = uuid;
			this.vers = vers;
		}
	}

	private int standardCaps = 0;
	private IntArrayList standardCapsVers = new IntArrayList();
	private List<ExtCapability> extendedCaps = new ArrayList<>();

	int getStandardCaps() {
		return standardCaps;
	}

	int[] getStandardCapsVers() {
		return standardCapsVers.toArray();
	}

	ExtCapability[] getExtendedCaps() {
		return extendedCaps.toArray(new ExtCapability[extendedCaps.size()]);
	}

	private void addStandard(int type, int... versions) {
		int bit = (1 << type);
		standardCapsVers.insert(Integer.bitCount(standardCaps & (bit - 1)), bits(versions));
		standardCaps |= bit;
	}

	private void addExtended(EaglercraftUUID type, int... versions) {
		extendedCaps.add(new ExtCapability(type, bits(versions)));
	}

	private int bits(int... versions) {
		int bits = 0;
		for(int i = 0; i < versions.length; ++i) {
			bits |= (1 << versions[i]);
		}
		return bits;
	}

}
