/*
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

package net.lax1dude.eaglercraft.v1_8.sp.lan;

public interface LANPeerEvent {

	String getPeerId();
	
	public static class LANPeerICECandidateEvent implements LANPeerEvent {
		
		public final String clientId;
		public final String candidates;
		
		public LANPeerICECandidateEvent(String clientId, String candidates) {
			this.clientId = clientId;
			this.candidates = candidates;
		}

		@Override
		public String getPeerId() {
			return clientId;
		}
		
	}
	
	public static class LANPeerDescriptionEvent implements LANPeerEvent {
		
		public final String clientId;
		public final String description;
		
		public LANPeerDescriptionEvent(String clientId, String description) {
			this.clientId = clientId;
			this.description = description;
		}

		@Override
		public String getPeerId() {
			return clientId;
		}
		
	}
	
	public static class LANPeerDataChannelEvent implements LANPeerEvent {
		
		public final String clientId;
		
		public LANPeerDataChannelEvent(String clientId) {
			this.clientId = clientId;
		}

		@Override
		public String getPeerId() {
			return clientId;
		}
		
	}
	
	public static class LANPeerPacketEvent implements LANPeerEvent {
		
		public final String clientId;
		public final byte[] payload;
		
		public LANPeerPacketEvent(String clientId, byte[] payload) {
			this.clientId = clientId;
			this.payload = payload;
		}

		@Override
		public String getPeerId() {
			return clientId;
		}
		
	}
	
	public static class LANPeerDisconnectEvent implements LANPeerEvent {
		
		public final String clientId;
		
		public LANPeerDisconnectEvent(String clientId) {
			this.clientId = clientId;
		}

		@Override
		public String getPeerId() {
			return clientId;
		}
		
	}
	
}