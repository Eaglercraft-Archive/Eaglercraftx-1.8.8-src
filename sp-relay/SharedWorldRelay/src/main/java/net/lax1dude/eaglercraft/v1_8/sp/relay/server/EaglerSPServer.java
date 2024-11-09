package net.lax1dude.eaglercraft.v1_8.sp.relay.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;

import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.*;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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
public class EaglerSPServer {
	
	public final WebSocket socket;
	public final String code;
	public final Map<String,EaglerSPClient> clients;
	public final String serverName;
	public final String serverAddress;
	public final boolean serverHidden;
	
	EaglerSPServer(WebSocket sock, String code, String serverName, String serverAddress) {
		this.socket = sock;
		this.code = code;
		this.clients = new HashMap();
		
		if(serverName.endsWith(";1")) {
			this.serverHidden = true;
			serverName = serverName.substring(0, serverName.length() - 2);
		}else if(serverName.endsWith(";0")) {
			this.serverHidden = false;
			serverName = serverName.substring(0, serverName.length() - 2);
		}else {
			this.serverHidden = false;
		}
		
		this.serverName = serverName;
		this.serverAddress = serverAddress;
	}
	
	public void send(RelayPacket packet) {
		if(socket.isOpen()) {
			try {
				socket.send(RelayPacket.writePacket(packet, EaglerSPRelay.logger));
			}catch(IOException ex) {
				EaglerSPRelay.logger.debug("Error sending data to {}", serverAddress);
				EaglerSPRelay.logger.debug(ex);
				try {
					socket.send(RelayPacket.writePacket(new RelayPacketFFErrorCode(RelayPacketFFErrorCode.TYPE_INTERNAL_ERROR,
							"Internal Server Error"), EaglerSPRelay.logger));
				}catch(IOException ex2) {
				}
				socket.close();
			}
		}else {
			EaglerSPRelay.logger.debug("WARNING: Tried to send data to {} after the connection closed.", serverAddress);
		}
	}
	
	public boolean handle(RelayPacket _packet) throws IOException {
		if(_packet instanceof RelayPacket03ICECandidate) {
			RelayPacket03ICECandidate packet = (RelayPacket03ICECandidate)_packet;
			EaglerSPClient cl = clients.get(packet.peerId);
			if(cl != null) {
				if(LoginState.assertEquals(cl, LoginState.SENT_ICE_CANDIDATE)) {
					cl.state = LoginState.RECIEVED_ICE_CANIDATE;
					cl.handleServerICECandidate(packet);
					EaglerSPRelay.logger.debug("[{}][Server -> Relay -> Client] PKT 0x03: ICECandidate", cl.socket.getAttachment());
				}
			}else {
				socket.send(RelayPacket.writePacket(new RelayPacketFFErrorCode(RelayPacketFFErrorCode.TYPE_UNKNOWN_CLIENT,
						"Unknown Client ID: " + packet.peerId), EaglerSPRelay.logger));
			}
			return true;
		}else if(_packet instanceof RelayPacket04Description) {
			RelayPacket04Description packet = (RelayPacket04Description)_packet;
			EaglerSPClient cl = clients.get(packet.peerId);
			if(cl != null) {
				if(LoginState.assertEquals(cl, LoginState.SENT_DESCRIPTION)) {
					cl.state = LoginState.RECIEVED_DESCRIPTION;
					cl.handleServerDescription(packet);
					EaglerSPRelay.logger.debug("[{}][Server -> Relay -> Client] PKT 0x04: Description", cl.socket.getAttachment());
				}
			}else {
				socket.send(RelayPacket.writePacket(new RelayPacketFFErrorCode(RelayPacketFFErrorCode.TYPE_UNKNOWN_CLIENT,
						"Unknown Client ID: " + packet.peerId), EaglerSPRelay.logger));
			}
			return true;
		}else if(_packet instanceof RelayPacketFEDisconnectClient) {
			RelayPacketFEDisconnectClient packet = (RelayPacketFEDisconnectClient)_packet;
			EaglerSPClient cl = clients.get(packet.clientId);
			if(cl != null) {
				cl.handleServerDisconnectClient(packet);
				EaglerSPRelay.logger.debug("[{}][Server -> Relay -> Client] PKT 0xFE: Disconnect: {}: {}", cl.socket.getAttachment(),
						packet.code, packet.reason);
			}else {
				socket.send(RelayPacket.writePacket(new RelayPacketFFErrorCode(RelayPacketFFErrorCode.TYPE_UNKNOWN_CLIENT,
						"Unknown Client ID: " + packet.clientId), EaglerSPRelay.logger));
			}
			return true;
		}else {
			return false;
		}
	}
	
	public void handleNewClient(EaglerSPClient client) {
		synchronized(clients) {
			clients.put(client.id, client);
			send(new RelayPacket02NewClient(client.id));
			EaglerSPRelay.logger.debug("[{}][Relay -> Server] PKT 0x02: Notify server of the client, id: {}", serverAddress, client.id);
		}
	}
	
	public void handleClientDisconnect(EaglerSPClient client) {
		synchronized(clients) {
			clients.remove(client.id);
		}
		if(!client.serverNotifiedOfClose) {
			send(new RelayPacketFEDisconnectClient(client.id, RelayPacketFEDisconnectClient.TYPE_UNKNOWN, "End of stream"));
			client.serverNotifiedOfClose = true;
		}
	}

	public void handleClientICECandidate(EaglerSPClient client, RelayPacket03ICECandidate packet) {
		send(new RelayPacket03ICECandidate(client.id, packet.candidate));
	}

	public void handleClientDescription(EaglerSPClient client, RelayPacket04Description packet) {
		send(new RelayPacket04Description(client.id, packet.description));
	}

	public void handleClientSuccess(EaglerSPClient client, RelayPacket05ClientSuccess packet) {
		send(new RelayPacket05ClientSuccess(client.id));
	}

	public void handleClientFailure(EaglerSPClient client, RelayPacket06ClientFailure packet) {
		send(new RelayPacket06ClientFailure(client.id));
	}

}
