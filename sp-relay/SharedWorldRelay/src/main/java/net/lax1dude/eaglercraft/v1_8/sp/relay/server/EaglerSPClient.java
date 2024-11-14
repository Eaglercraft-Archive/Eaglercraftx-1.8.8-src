package net.lax1dude.eaglercraft.v1_8.sp.relay.server;

import java.io.IOException;
import java.util.Random;

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
public class EaglerSPClient {
	
	public final WebSocket socket;
	public final EaglerSPServer server;
	public final String id;
	public final long createdOn;
	public boolean serverNotifiedOfClose = false;
	public LoginState state = LoginState.INIT;
	public final String address;
	
	EaglerSPClient(WebSocket sock, EaglerSPServer srv, String id, String addr) {
		this.socket = sock;
		this.server = srv;
		this.id = id;
		this.createdOn = Util.millis();
		this.address = addr;
	}
	
	public void send(RelayPacket packet) {
		if(socket.isOpen()) {
			try {
				socket.send(RelayPacket.writePacket(packet, EaglerSPRelay.logger));
			}catch(IOException ex) {
				EaglerSPRelay.logger.debug("Error sending data to {}", (Object) socket.getAttachment());
				EaglerSPRelay.logger.debug(ex);
				disconnect(RelayPacketFEDisconnectClient.TYPE_INTERNAL_ERROR, "Internal Server Error");
				socket.close();
			}
		}else {
			EaglerSPRelay.logger.debug("WARNING: Tried to send data to {} after the connection closed.", (Object) socket.getAttachment());
		}
	}
	
	public boolean handle(RelayPacket packet) throws IOException {
		if(packet instanceof RelayPacket03ICECandidate) {
			if(LoginState.assertEquals(this, LoginState.RECIEVED_DESCRIPTION)) {
				state = LoginState.SENT_ICE_CANDIDATE;
				server.handleClientICECandidate(this, (RelayPacket03ICECandidate)packet);
				EaglerSPRelay.logger.debug("[{}][Client -> Relay -> Server] PKT 0x03: ICECandidate", (Object) socket.getAttachment());
			}
			return true;
		}else if(packet instanceof RelayPacket04Description) {
			if(LoginState.assertEquals(this, LoginState.INIT)) {
				state = LoginState.SENT_DESCRIPTION;
				server.handleClientDescription(this, (RelayPacket04Description)packet);
				EaglerSPRelay.logger.debug("[{}][Client -> Relay -> Server] PKT 0x04: Description", (Object) socket.getAttachment());
			}
			return true;
		}else if(packet instanceof RelayPacket05ClientSuccess) {
			if(LoginState.assertEquals(this, LoginState.RECIEVED_ICE_CANIDATE)) {
				state = LoginState.FINISHED;
				server.handleClientSuccess(this, (RelayPacket05ClientSuccess)packet);
				EaglerSPRelay.logger.debug("[{}][Client -> Relay -> Server] PKT 0x05: ClientSuccess", (Object) socket.getAttachment());
				disconnect(RelayPacketFEDisconnectClient.TYPE_FINISHED_SUCCESS, "Successful connection");
			}
			return true;
		}else if(packet instanceof RelayPacket06ClientFailure) {
			if(LoginState.assertEquals(this, LoginState.RECIEVED_ICE_CANIDATE)) {
				state = LoginState.FINISHED;
				server.handleClientFailure(this, (RelayPacket06ClientFailure)packet);
				EaglerSPRelay.logger.debug("[{}][Client -> Relay -> Server] PKT 0x05: ClientFailure", (Object) socket.getAttachment());
				disconnect(RelayPacketFEDisconnectClient.TYPE_FINISHED_FAILED, "Failed connection");
			}
			return true;
		}else {
			return false;
		}
	}
	
	public void handleServerICECandidate(RelayPacket03ICECandidate desc) {
		send(new RelayPacket03ICECandidate("", desc.candidate));
	}
	
	public void handleServerDescription(RelayPacket04Description desc) {
		send(new RelayPacket04Description("", desc.description));
	}

	public void handleServerDisconnectClient(RelayPacketFEDisconnectClient packet) {
		disconnect(packet.code, packet.reason);
	}
	
	public void disconnect(int code, String reason) {
		RelayPacket pkt = new RelayPacketFEDisconnectClient(id, code, reason);
		if(!serverNotifiedOfClose) {
			if (code != RelayPacketFEDisconnectClient.TYPE_FINISHED_SUCCESS) server.send(pkt);
			serverNotifiedOfClose = true;
		}
		if(socket.isOpen()) {
			send(pkt);
			socket.close();
		}
		EaglerSPRelay.logger.debug("[{}][Relay -> Client] PKT 0xFE: #{} {}", (Object) socket.getAttachment(), code, reason);
	}
	
	public static final int clientCodeLength = 16;
	private static final String clientCodeChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static String generateClientId() {
		Random r = new Random();
		char[] ret = new char[clientCodeLength];
		for(int i = 0; i < ret.length; ++i) {
			ret[i] = clientCodeChars.charAt(r.nextInt(clientCodeChars.length()));
		}
		return new String(ret);
	}

}
