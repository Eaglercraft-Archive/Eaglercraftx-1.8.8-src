package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config;

import java.util.Collection;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.bungee.Configuration;

/**
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
public class EaglerUpdateConfig {

	static EaglerUpdateConfig loadConfig(Configuration config) {
		boolean blockAllClientUpdates = config.getBoolean("block_all_client_updates", false);
		boolean discardLoginPacketCerts = config.getBoolean("discard_login_packet_certs", false);
		int certPacketDataRateLimit = config.getInt("cert_packet_data_rate_limit", 524288);
		boolean enableEagcertFolder = config.getBoolean("enable_eagcert_folder", true);
		boolean downloadLatestCerts = config.getBoolean("download_latest_certs", true);
		int checkForUpdatesEvery = config.getInt("check_for_update_every", 28800);
		Collection<String> downloadCertURLs = (Collection<String>)config.getList("download_certs_from");
		return new EaglerUpdateConfig(blockAllClientUpdates, discardLoginPacketCerts, certPacketDataRateLimit,
				enableEagcertFolder, downloadLatestCerts, checkForUpdatesEvery, downloadCertURLs);
	}

	private final boolean blockAllClientUpdates;
	private final boolean discardLoginPacketCerts;
	private final int certPacketDataRateLimit;
	private final boolean enableEagcertFolder;
	private final boolean downloadLatestCerts;
	private final int checkForUpdatesEvery;
	private final Collection<String> downloadCertURLs;

	public EaglerUpdateConfig(boolean blockAllClientUpdates, boolean discardLoginPacketCerts,
			int certPacketDataRateLimit, boolean enableEagcertFolder, boolean downloadLatestCerts,
			int checkForUpdatesEvery, Collection<String> downloadCertURLs) {
		this.blockAllClientUpdates = blockAllClientUpdates;
		this.discardLoginPacketCerts = discardLoginPacketCerts;
		this.certPacketDataRateLimit = certPacketDataRateLimit;
		this.enableEagcertFolder = enableEagcertFolder;
		this.downloadLatestCerts = downloadLatestCerts;
		this.checkForUpdatesEvery = checkForUpdatesEvery;
		this.downloadCertURLs = downloadCertURLs;
	}

	public boolean isBlockAllClientUpdates() {
		return blockAllClientUpdates;
	}

	public boolean isDiscardLoginPacketCerts() {
		return discardLoginPacketCerts;
	}

	public int getCertPacketDataRateLimit() {
		return certPacketDataRateLimit;
	}

	public boolean isEnableEagcertFolder() {
		return enableEagcertFolder;
	}

	public boolean isDownloadLatestCerts() {
		return downloadLatestCerts && enableEagcertFolder;
	}

	public int getCheckForUpdatesEvery() {
		return checkForUpdatesEvery;
	}

	public Collection<String> getDownloadCertURLs() {
		return downloadCertURLs;
	}

}
