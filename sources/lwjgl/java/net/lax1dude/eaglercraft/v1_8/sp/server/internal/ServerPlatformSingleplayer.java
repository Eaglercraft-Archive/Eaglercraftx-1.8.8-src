package net.lax1dude.eaglercraft.v1_8.sp.server.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.v1_8.Filesystem;
import net.lax1dude.eaglercraft.v1_8.internal.IClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.IEaglerFilesystem;
import net.lax1dude.eaglercraft.v1_8.internal.IPCPacketData;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformWebRTC;
import net.lax1dude.eaglercraft.v1_8.internal.lwjgl.DesktopClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.sp.server.IWASMCrashCallback;
import net.lax1dude.eaglercraft.v1_8.sp.server.internal.lwjgl.MemoryConnection;

/**
 * Copyright (c) 2023-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class ServerPlatformSingleplayer {

	private static IEaglerFilesystem filesystem = null;

	public static void initializeContext() {
		if(filesystem == null) {
			filesystem = Filesystem.getHandleFor(getClientConfigAdapter().getWorldsDB());
		}
	}

	public static void initializeContextSingleThread(Consumer<IPCPacketData> packetSendCallback) {
		throw new UnsupportedOperationException();
	}

	public static IEaglerFilesystem getWorldsDatabase() {
		return filesystem;
	}

	public static void sendPacket(IPCPacketData packet) {
		if(PlatformWebRTC.serverLANPeerPassIPC(packet)) {
			return;
		}
		synchronized(MemoryConnection.serverToClientQueue) {
			MemoryConnection.serverToClientQueue.add(packet);
		}
	}

	public static IPCPacketData recievePacket() {
		synchronized(MemoryConnection.clientToServerQueue) {
			if(MemoryConnection.clientToServerQueue.size() > 0) {
				return MemoryConnection.clientToServerQueue.remove(0);
			}
		}
		return null;
	}

	public static List<IPCPacketData> recieveAllPacket() {
		synchronized(MemoryConnection.clientToServerQueue) {
			if(MemoryConnection.clientToServerQueue.size() == 0) {
				return null;
			}else {
				List<IPCPacketData> ret = new ArrayList<>(MemoryConnection.clientToServerQueue);
				MemoryConnection.clientToServerQueue.clear();
				return ret;
			}
		}
	}

	public static IClientConfigAdapter getClientConfigAdapter() {
		return DesktopClientConfigAdapter.instance;
	}

	public static void immediateContinue() {
		
	}

	public static void platformShutdown() {
		filesystem = null;
	}

	public static boolean isSingleThreadMode() {
		return false;
	}

	public static void setCrashCallbackWASM(IWASMCrashCallback callback) {
		
	}

}
