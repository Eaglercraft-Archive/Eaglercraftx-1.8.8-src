package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.update.CertificateInvalidException;
import net.lax1dude.eaglercraft.v1_8.update.UpdateCertificate;

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
public class SignatureCheckHelper {

	private static final Logger logger = LogManager.getLogger("SignatureCheckHelper");

	public static boolean checkSignatureValid(byte[] signatureBytes, byte[] payloadBytes) {
		UpdateCertificate cert;
		try {
			cert = UpdateCertificate.parseAndVerifyCertificate(signatureBytes);
		} catch (CertificateInvalidException | IOException e) {
			logger.error("The client's signature is invalid because the update certificate is bad");
			logger.error(e);
			return false;
		}
		if(!cert.isBundleDataValid(payloadBytes)) {
			logger.error("The client's signature is invalid because the payload checksum does not match the expected checksum in the update certificate");
			logger.error("(Update certificate client name and version: {} - {})", cert.bundleDisplayName, cert.bundleDisplayVersion);
			return false;
		}else {
			logger.info("Signature is valid: {} - {}", cert.bundleDisplayName, cert.bundleDisplayVersion);
			return true;
		}
	}

}
