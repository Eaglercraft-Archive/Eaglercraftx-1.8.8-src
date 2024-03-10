package net.lax1dude.eaglercraft.v1_8.update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformApplication;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformUpdateSvc;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public class UpdateService {

	private static final Logger logger = LogManager.getLogger("UpdateService");

	private static UpdateCertificate myUpdateCert = null;
	private static boolean isBundleDataValid = false;

	private static UpdateCertificate latestUpdateFound = null;
	private static final Set<UpdateCertificate> availableUpdates = new HashSet();
	private static final Set<RawKnownCertHolder> fastUpdateKnownCheckSet = new HashSet();
	private static final Set<UpdateCertificate> dismissedUpdates = new HashSet();

	private static class RawKnownCertHolder {

		private final byte[] data;
		private final int hashcode;
		private final long age;

		public RawKnownCertHolder(byte[] data) {
			this.data = data;
			this.hashcode = Arrays.hashCode(data);
			this.age = System.currentTimeMillis();
		}

		public int hashCode() {
			return hashcode;
		}

		public boolean equals(Object o) {
			return o != null && (o == this || ((o instanceof RawKnownCertHolder) && Arrays.equals(((RawKnownCertHolder)o).data, data)));
		}
	}

	public static boolean supported() {
		return EaglercraftVersion.enableUpdateService && EagRuntime.getConfiguration().allowUpdateSvc() && PlatformUpdateSvc.supported();
	}

	public static void initialize() {
		if(!supported()) {
			logger.info("Update service is not supported on this client");
			return;
		}
		PlatformUpdateSvc.initialize();
		if(PlatformUpdateSvc.getClientSignatureData() != null) {
			logger.info("Testing client update certificate...");
			try {
				myUpdateCert = UpdateCertificate.parseAndVerifyCertificate(PlatformUpdateSvc.getClientSignatureData());
				if(!EaglercraftVersion.updateBundlePackageName.equalsIgnoreCase(myUpdateCert.bundlePackageName)) {
					throw new CertificateInvalidException("Certificate package name does not match current client package name!");
				}
				if(EaglercraftVersion.updateBundlePackageVersionInt != myUpdateCert.bundleVersionInteger) {
					throw new CertificateInvalidException("Certificate client version does not match current client version!");
				}
			}catch(Throwable t) {
				myUpdateCert = null;
				logger.error("Client update certificate is invalid!");
				logger.error(t);
			}
			if(myUpdateCert != null && PlatformUpdateSvc.getClientBundleData() != null) {
				isBundleDataValid = myUpdateCert.isBundleDataValid(PlatformUpdateSvc.getClientBundleData());
				if(!isBundleDataValid) {
					logger.error("Client checksum does not match certificate! \"Download Offline\" button will download a fresh client");
				}
			}
		}
		byte[] latestUpdate = PlatformApplication.getLocalStorage(EaglercraftVersion.updateLatestLocalStorageKey);
		if(latestUpdate != null) {
			addCertificateToSet(latestUpdate, false);
		}
	}

	public static byte[] getClientSignatureData() {
		if(myUpdateCert != null) {
			return PlatformUpdateSvc.getClientSignatureData();
		}
		return null;
	}

	public static byte[] getClientBundleData() {
		if(isBundleDataValid) {
			return PlatformUpdateSvc.getClientBundleData();
		}
		return null;
	}

	public static UpdateCertificate getClientCertificate() {
		return myUpdateCert;
	}

	public static void addCertificateToSet(byte[] certificateData) {
		addCertificateToSet(certificateData, true);
	}

	private static void addCertificateToSet(byte[] certificateData, boolean saveLatest) {
		if (EagRuntime.getConfiguration().allowUpdateDL()) {
			synchronized(availableUpdates) {
				try {
					if(certificateData.length > 32767) {
						throw new CertificateInvalidException("Certificate is too large! (" + certificateData.length + " bytes)");
					}
					if(!fastUpdateKnownCheckSet.add(new RawKnownCertHolder(certificateData))) {
						if (EagRuntime.getConfiguration().isLogInvalidCerts()) {
							logger.info("Ignoring {} byte certificate that has already been processed", certificateData.length);
						}
						freeMemory();
						return;
					}
					UpdateCertificate cert = UpdateCertificate.parseAndVerifyCertificate(certificateData);
					if (EaglercraftVersion.updateBundlePackageName.equalsIgnoreCase(cert.bundlePackageName)) {
						if (myUpdateCert == null || !Arrays.equals(cert.bundleDataHash, myUpdateCert.bundleDataHash)) {
							if(availableUpdates.add(cert)) {
								logger.info("Found new update: {} - {}", cert.bundleDisplayName, cert.bundleDisplayVersion);
								if (cert.bundleVersionInteger > EaglercraftVersion.updateBundlePackageVersionInt
										&& (latestUpdateFound == null
												|| cert.bundleVersionInteger > latestUpdateFound.bundleVersionInteger
												|| (cert.bundleVersionInteger == latestUpdateFound.bundleVersionInteger
														&& cert.sigTimestamp > latestUpdateFound.sigTimestamp))
										&& !dismissedUpdates.contains(cert)) {
									latestUpdateFound = cert;
									if (saveLatest) {
										PlatformApplication.setLocalStorage(EaglercraftVersion.updateLatestLocalStorageKey,
												certificateData);
									}
								}
							}else if(EagRuntime.getConfiguration().isLogInvalidCerts()) {
								logger.info("Ignoring already indexed update: {} - {}", cert.bundleDisplayName, cert.bundleDisplayVersion);
							}
						}
					} else {
						if (EagRuntime.getConfiguration().isLogInvalidCerts()) {
							logger.warn("Ignoring 3rd party update certificate: {} - {} ({})", cert.bundleDisplayName,
									cert.bundleDisplayVersion, cert.bundlePackageName);
							logger.warn("Note: the certificate still had a valid signature (leaked private key?!)");
						}
					}
				} catch (Throwable t) {
					if (EagRuntime.getConfiguration().isLogInvalidCerts()) {
						logger.error("Invalid update certificate recieved! The certificate may be from a different client");
						logger.error(t);
					}
				}
			}
		}
	}

	private static void freeMemory() {
		if(fastUpdateKnownCheckSet.size() > 127) {
			List<RawKnownCertHolder> lst = new ArrayList(fastUpdateKnownCheckSet);
			fastUpdateKnownCheckSet.clear();
			lst.sort((c1, c2) -> { return (int)(c2.age - c1.age); });
			for(int i = 0; i < 64; ++i) {
				fastUpdateKnownCheckSet.add(lst.get(i));
			}
		}
	}

	public static void startClientUpdateFrom(UpdateCertificate clientUpdate) {
		PlatformUpdateSvc.startClientUpdateFrom(clientUpdate);
	}

	public static UpdateProgressStruct getUpdatingStatus() {
		return PlatformUpdateSvc.getUpdatingStatus();
	}

	public static UpdateCertificate getLatestUpdateFound() {
		return latestUpdateFound;
	}

	public static Collection<UpdateCertificate> getAvailableUpdates() {
		return availableUpdates;
	}

	public static void dismiss(UpdateCertificate cert) {
		if(latestUpdateFound == cert) {
			latestUpdateFound = null;
		}
		dismissedUpdates.add(cert);
	}

	public static void quine() {
		if(myUpdateCert != null) {
			byte[] data = getClientBundleData();
			if(data != null) {
				logger.info("Generating signed offline download...");
				PlatformUpdateSvc.quine(myUpdateCert, data);
			}else {
				logger.error("Client checksum does not match certificate! Downloading a fresh client...");
				PlatformUpdateSvc.startClientUpdateFrom(myUpdateCert);
			}
		}
	}

	public static boolean shouldDisableDownloadButton() {
		return EagRuntime.getConfiguration().getDownloadOfflineButtonLink() == null && (myUpdateCert == null
				|| (getClientBundleData() == null && PlatformUpdateSvc.getUpdatingStatus().isBusy));
	}
}
