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

package net.lax1dude.eaglercraft.v1_8.update;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerOutputStream;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformApplication;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayManager;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayQueryDispatch;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayServerSocket;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket00Handshake;
import net.minecraft.client.Minecraft;

public class RelayUpdateChecker {

	private static class RelayEntry {
		
		private final String uri;
		private boolean queued;
		private boolean handshake;
		private RelayServerSocket currentSocket;
		
		private RelayEntry(String uri) {
			this.uri = uri;
		}
		
	}

	private static final List<RelayEntry> relaysList = new ArrayList<>();

	private static long lastUpdateCheck = -1l;
	private static boolean hasInit = false;

	private static final long updateCheckRate = 3l * 60l * 60l * 1000l;

	private static final String magic = "~!REQUEST_UPDATE_CERT";

	public static void runTick() {
		if(!EagRuntime.getConfiguration().isCheckRelaysForUpdates()) {
			return;
		}
		if(!hasInit) {
			hasInit = true;
			for(net.lax1dude.eaglercraft.v1_8.sp.relay.RelayEntry etr : EagRuntime.getConfiguration().getRelays()) {
				relaysList.add(new RelayEntry(etr.address));
			}
			byte[] b = PlatformApplication.getLocalStorage("lastRelayUpdate", false);
			if(b != null) {
				try {
					lastUpdateCheck = (new DataInputStream(new EaglerInputStream(b))).readLong();
				} catch (IOException e) {
				}
			}
		}
		long millis = System.currentTimeMillis();
		Minecraft mc = Minecraft.getMinecraft();
		if ((mc.theWorld == null || mc.isSingleplayer())
				&& (millis - lastUpdateCheck > updateCheckRate || millis + 60000l < lastUpdateCheck)) {
			lastUpdateCheck = millis;
			try {
				EaglerOutputStream bao = new EaglerOutputStream(8);
				(new DataOutputStream(bao)).writeLong(lastUpdateCheck);
				PlatformApplication.setLocalStorage("lastRelayUpdate", bao.toByteArray(), false);
			} catch (IOException e) {
			}
			for (int i = 0, l = relaysList.size(); i < l; ++i) {
				relaysList.get(i).queued = true;
			}
		}
		for(int i = 0, l = relaysList.size(); i < l; ++i) {
			RelayEntry etr = relaysList.get(i);
			if(etr.currentSocket != null) {
				updateRelay(etr);
				if(etr.currentSocket != null) {
					return;
				}
			}
		}
		for(int i = 0, l = relaysList.size(); i < l; ++i) {
			RelayEntry etr = relaysList.get(i);
			if(etr.queued) {
				etr.queued = false;
				connect(etr);
				if(etr.currentSocket != null) {
					return;
				}
			}
		}
	}

	private static void connect(RelayEntry socket) {
		try {
			socket.handshake = false;
			socket.currentSocket = RelayQueryDispatch.openRelayConnection(socket.uri, 10000);
			if(socket.currentSocket.isClosed()) {
				socket.currentSocket = null;
			}
		}catch(Throwable t) {
		}
	}

	private static void updateRelay(RelayEntry socket) {
		try {
			socket.currentSocket.update();
			if(socket.currentSocket.isClosed()) {
				socket.currentSocket = null;
			}else if(socket.currentSocket.isOpen()) {
				if(!socket.handshake) {
					socket.handshake = true;
					socket.currentSocket.writePacket(new RelayPacket00Handshake(0x02, RelayManager.preferredRelayVersion, magic));
				}else {
					// close immediately
					if(socket.currentSocket.nextPacket() != null) {
						socket.currentSocket.close();
						socket.currentSocket = null;
					}
				}
			}
		}catch(Throwable t) {
		}
	}
}