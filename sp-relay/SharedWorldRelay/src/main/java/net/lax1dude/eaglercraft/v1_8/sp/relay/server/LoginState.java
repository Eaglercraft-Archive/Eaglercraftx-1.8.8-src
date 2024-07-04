package net.lax1dude.eaglercraft.v1_8.sp.relay.server;

import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacketFEDisconnectClient;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
 * <br><br>
 * SENT = Client has sent something to the server<br>
 * RECIEVED = Server has sent something to the client
 */
public enum LoginState {
	
	INIT, SENT_ICE_CANDIDATE, RECIEVED_ICE_CANIDATE, SENT_DESCRIPTION, RECIEVED_DESCRIPTION, FINISHED;
	
	public static boolean assertEquals(EaglerSPClient client, LoginState state) {
		if(client.state != state) {
			String msg = "client is in state " + client.state.name() + " when it was supposed to be " + state.name();
			client.disconnect(RelayPacketFEDisconnectClient.TYPE_INVALID_OPERATION, msg);
			EaglerSPRelay.logger.debug("[{}][Relay -> Client] PKT 0xFE: TYPE_INVALID_OPERATION: {}", (String) client.socket.getAttachment(), msg);
			return false;
		}else {
			return true;
		}
	}
	
}
