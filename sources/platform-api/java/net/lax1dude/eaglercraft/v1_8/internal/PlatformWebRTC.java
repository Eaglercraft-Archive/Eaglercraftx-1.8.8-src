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

package net.lax1dude.eaglercraft.v1_8.internal;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.sp.lan.LANPeerEvent;

public class PlatformWebRTC {

	public static native void initialize();

	public static native boolean supported();

	public static native void runScheduledTasks();

	public static native void startRTCLANClient();

	public static native int clientLANReadyState();

	public static native void clientLANCloseConnection();

	public static native void clientLANSendPacket(byte[] pkt);

	public static native byte[] clientLANReadPacket();

	public static native List<byte[]> clientLANReadAllPacket();

	public static native void clientLANSetICEServersAndConnect(String[] servers);

	public static native void clearLANClientState();

	public static native String clientLANAwaitICECandidate();

	public static native String clientLANAwaitDescription();

	public static native boolean clientLANAwaitChannel();

	public static native boolean clientLANClosed();

	public static native void clientLANSetICECandidate(String candidate);

	public static native void clientLANSetDescription(String description);

	public static native void startRTCLANServer();

	public static native void serverLANInitializeServer(String[] servers);

	public static native void serverLANCloseServer();

	public static native void serverLANCreatePeer(String peer);

	public static native LANPeerEvent serverLANGetEvent(String peer);

	public static native List<LANPeerEvent> serverLANGetAllEvent(String peer);

	public static native void serverLANWritePacket(String peer, byte[] data);

	public static native void serverLANPeerICECandidates(String peer, String iceCandidates);

	public static native void serverLANPeerDescription(String peer, String description);

	public static native void serverLANPeerMapIPC(String peer, String ipcChannel);

	public static native void serverLANDisconnectPeer(String peer);

	public static native int countPeers();

}