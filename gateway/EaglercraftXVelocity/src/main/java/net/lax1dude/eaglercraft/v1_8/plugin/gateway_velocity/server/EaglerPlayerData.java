package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server;

import java.util.*;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.SimpleRateLimiter;

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
public class EaglerPlayerData {

	public static class ClientCertificateHolder {
		public final byte[] data;
		public final int hash;
		public ClientCertificateHolder(byte[] data, int hash) {
			this.data = data;
			this.hash = hash;
		}
	}

	public final SimpleRateLimiter skinLookupRateLimiter;
	public final SimpleRateLimiter skinUUIDLookupRateLimiter;
	public final SimpleRateLimiter skinTextureDownloadRateLimiter;
	public final SimpleRateLimiter capeLookupRateLimiter;
	public final SimpleRateLimiter voiceConnectRateLimiter;
	public final EaglerListenerConfig listener;
	public final String origin;
	public final ClientCertificateHolder clientCertificate;
	public final Set<ClientCertificateHolder> certificatesToSend;
	public final Set<Integer> certificatesSent;
	public boolean currentFNAWSkinEnableStatus = true;

	public EaglerPlayerData(EaglerListenerConfig listener, String origin, ClientCertificateHolder clientCertificate) {
		this.listener = listener;
		this.origin = origin;
		this.skinLookupRateLimiter = new SimpleRateLimiter();
		this.skinUUIDLookupRateLimiter = new SimpleRateLimiter();
		this.skinTextureDownloadRateLimiter = new SimpleRateLimiter();
		this.capeLookupRateLimiter = new SimpleRateLimiter();
		this.voiceConnectRateLimiter = new SimpleRateLimiter();
		this.clientCertificate = clientCertificate;
		this.certificatesToSend = new HashSet();
		this.certificatesSent = new HashSet();
		if(clientCertificate != null) {
			this.certificatesSent.add(clientCertificate.hashCode());
		}
	}

	public String getOrigin() {
		return origin;
	}

	public EaglerListenerConfig getEaglerListenerConfig() {
		return listener;
	}
}
