package net.lax1dude.eaglercraft.v1_8.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.java_websocket.enums.ReadyState;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

/**
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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
public class PlatformNetworking {
	
	static final Logger networkLogger = LogManager.getLogger("PlatformNetworking");
	
	private static WebSocketPlayClient wsPlayClient = null;
	static EnumEaglerConnectionState playConnectState = EnumEaglerConnectionState.CLOSED;
	static EnumServerRateLimit serverRateLimit = null;
	
	static String currentURI = null;
	
	public static EnumEaglerConnectionState playConnectionState() {
		return ((wsPlayClient == null || wsPlayClient.isClosed()) && playConnectState == EnumEaglerConnectionState.CONNECTING) ? EnumEaglerConnectionState.FAILED :
			((wsPlayClient != null && wsPlayClient.getReadyState() == ReadyState.NOT_YET_CONNECTED) ? EnumEaglerConnectionState.CONNECTING : 
				(((wsPlayClient == null || wsPlayClient.isClosed()) && playConnectState != EnumEaglerConnectionState.FAILED) ? EnumEaglerConnectionState.CLOSED : playConnectState));
	}
	
	public static void startPlayConnection(String destination) {
		if(!playConnectionState().isClosed()) {
			networkLogger.warn("Tried connecting to a server while already connected to a different server!");
			playDisconnect();
		}
		
		currentURI = destination;
		
		synchronized(playPackets) {
			playPackets.clear();
		}
		
		playConnectState = EnumEaglerConnectionState.CONNECTING;
		networkLogger.info("Connecting to server: {}", destination);
		
		URI u;
		
		try {
			u = new URI(destination);
		}catch(URISyntaxException ex) {
			networkLogger.error("Invalid server URI: {}", destination);
			playConnectState = EnumEaglerConnectionState.FAILED;
			return;
		}
		
		wsPlayClient = new WebSocketPlayClient(u);
		wsPlayClient.connect();
	}
	
	public static void playDisconnect() {
		if(!playConnectionState().isClosed() && wsPlayClient != null) {
			try {
				wsPlayClient.closeBlocking();
			} catch (InterruptedException e) {
				// :(
			}
			playConnectState = EnumEaglerConnectionState.CLOSED;
		}
	}
	
	private static final List<byte[]> playPackets = new LinkedList();
	
	public static byte[] readPlayPacket() {
		synchronized(playPackets) {
			return playPackets.size() > 0 ? playPackets.remove(0) : null;
		}
	}
	
	public static List<byte[]> readAllPacket() {
		synchronized(playPackets) {
			if(!playPackets.isEmpty()) {
				List<byte[]> ret = new ArrayList<>(playPackets);
				playPackets.clear();
				return ret;
			}else {
				return null;
			}
		}
	}
	
	public static int countAvailableReadData() {
		int total = 0;
		synchronized(playPackets) {
			for(int i = 0, l = playPackets.size(); i < l; ++i) {
				total += playPackets.get(i).length;
			}
		}
		return total;
	}
	
	static void recievedPlayPacket(byte[] arg0) {
		synchronized(playPackets) {
			playPackets.add(arg0);
		}
	}
	
	public static void writePlayPacket(byte[] pkt) {
		if(wsPlayClient == null || wsPlayClient.isClosed()) {
			networkLogger.error("Tried to send {} byte play packet while the socket was closed!", pkt.length);
		}else {
			wsPlayClient.send(pkt);
		}
	}
	
	public static IServerQuery sendServerQuery(String uri, String accept) {
		URI u;
		
		try {
			u = new URI(uri);
		}catch(URISyntaxException ex) {
			networkLogger.error("Invalid server URI: {}", uri);
			playConnectState = EnumEaglerConnectionState.FAILED;
			return null;
		}
		
		return new WebSocketServerQuery(accept, u);
	}
	
	public static EnumServerRateLimit getRateLimit() {
		return serverRateLimit == null ? EnumServerRateLimit.OK : serverRateLimit;
	}
	
	public static String getCurrentURI() {
		return currentURI;
	}
	
}
