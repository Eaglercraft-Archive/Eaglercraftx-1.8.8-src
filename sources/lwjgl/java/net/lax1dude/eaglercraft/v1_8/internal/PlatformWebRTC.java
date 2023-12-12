package net.lax1dude.eaglercraft.v1_8.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.sp.lan.LANPeerEvent;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayQuery;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayServerSocket;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayWorldsQuery;

/**
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class PlatformWebRTC {

	public static boolean supported() {
		return false;
	}

	public static RelayServerSocket openRelayConnection(String addr, int timeout) {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static RelayQuery openRelayQuery(String addr) {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static RelayWorldsQuery openRelayWorldsQuery(String addr) {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static void startRTCLANClient() {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static int clientLANReadyState() {
		return 0;
	}

	public static void clientLANCloseConnection() {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static void clientLANSendPacket(byte[] pkt) {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static byte[] clientLANReadPacket() {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static List<byte[]> clientLANReadAllPacket() {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static void clientLANSetICEServersAndConnect(String[] servers) {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static void clearLANClientState() {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static String clientLANAwaitICECandidate() {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static String clientLANAwaitDescription() {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static boolean clientLANAwaitChannel() {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static boolean clientLANClosed() {
		return true;
	}

	public static void clientLANSetICECandidate(String candidate) {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static void clientLANSetDescription(String description) {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static void startRTCLANServer() {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static void serverLANInitializeServer(String[] servers) {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static void serverLANCloseServer() {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static LANPeerEvent serverLANGetEvent(String clientId) {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static List<LANPeerEvent> serverLANGetAllEvent(String clientId) {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static void serverLANWritePacket(String peer, byte[] data) {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static void serverLANCreatePeer(String peer) {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static void serverLANPeerICECandidates(String peer, String iceCandidates) {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static void serverLANPeerDescription(String peer, String description) {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static void serverLANDisconnectPeer(String peer) {
		throw new UnsupportedOperationException("LAN not supported on desktop runtime!");
	}

	public static int countPeers() {
		return 0;
	}
}
