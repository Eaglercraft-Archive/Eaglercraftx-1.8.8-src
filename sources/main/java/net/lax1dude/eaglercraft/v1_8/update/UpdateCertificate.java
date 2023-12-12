package net.lax1dude.eaglercraft.v1_8.update;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder.ListMultimapBuilder;

import net.lax1dude.eaglercraft.v1_8.EaglerZLIB;
import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
import net.lax1dude.eaglercraft.v1_8.crypto.SHA256Digest;

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
public class UpdateCertificate {

	public static class DLSource {

		public final String type;
		public final String addr;
		private DLSource(String type, String addr) {
			this.type = type;
			this.addr = addr;
		}

		@Override
		public int hashCode() {
			return Objects.hash(addr, type);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DLSource other = (DLSource) obj;
			return Objects.equals(addr, other.addr) && Objects.equals(type, other.type);
		}

	}

	public final byte[] rawCertData;

	public final int sigVersion;
	public final long sigTimestamp;

	public final int bundleDataLength;
	public final byte[] bundleDataHash;

	public final String bundlePackageName;
	public final String bundleDisplayName;
	public final String bundleAuthorName;
	public final int bundleVersionInteger;
	public final String bundleDisplayVersion;
	public final String bundleVersionComment;

	public final DLSource[] bundleDataSources;

	public static UpdateCertificate parseAndVerifyCertificate(byte[] certData) throws IOException, CertificateInvalidException {
		InputStream is = new ByteArrayInputStream(certData);
		if(is.read() != 'E' || is.read() != 'A' || is.read() != 'G' || is.read() != 'S' || is.read() != 'I' || is.read() != 'G') {
			throw new IOException("Data is not a certificate!");
		}
		
		int vers = is.read() << 8;
		vers |= is.read();
		if(vers != 1) {
			throw new IOException("Invalid certificate version: " + vers);
		}
		
		byte[] rsa2048sum = new byte[256];
		is.read(rsa2048sum);
		
		byte[] rsa2048sumDec = (new BigInteger(rsa2048sum)).modPow(new BigInteger("65537"), EaglercraftVersion.updateSignatureModulus).toByteArray();
		
		if(rsa2048sumDec.length > 256) {
			throw new IOException("Invalid decrypted hash length: " + rsa2048sum.length);
		}
		
		if(rsa2048sumDec.length < 256) {
			byte[] tmp = rsa2048sumDec;
			rsa2048sumDec = new byte[256];
			System.arraycopy(tmp, 0, rsa2048sumDec, 256 - tmp.length, tmp.length);
		}
		
		int payloadLen = is.read() << 8;
		payloadLen |= is.read();
		
		byte[] signaturePayload = new byte[payloadLen];
		is.read(signaturePayload);
		
		SHA256Digest sha256 = new SHA256Digest();
		sha256.update(new byte[] { (byte) 170, (byte) 191, (byte) 203, (byte) 188, (byte) 47, (byte) 37, (byte) 17,
				(byte) 187, (byte) 169, (byte) 225, (byte) 247, (byte) 193, (byte) 100, (byte) 101, (byte) 233,
				(byte) 106, (byte) 80, (byte) 204, (byte) 192, (byte) 140, (byte) 19, (byte) 18, (byte) 165, (byte) 252,
				(byte) 138, (byte) 187, (byte) 229, (byte) 148, (byte) 118, (byte) 208, (byte) 179, (byte) 233 }, 0, 32);
		sha256.update(signaturePayload, 0, signaturePayload.length);
		byte[] hash2048 = new byte[256];
		sha256.doFinal(hash2048, 0);
		sha256.reset();
		sha256.update(new byte[] { (byte) 95, (byte) 222, (byte) 208, (byte) 153, (byte) 171, (byte) 133, (byte) 7,
				(byte) 88, (byte) 111, (byte) 87, (byte) 37, (byte) 104, (byte) 98, (byte) 115, (byte) 185, (byte) 153,
				(byte) 206, (byte) 188, (byte) 143, (byte) 18, (byte) 247, (byte) 28, (byte) 130, (byte) 87, (byte) 56,
				(byte) 223, (byte) 45, (byte) 192, (byte) 108, (byte) 166, (byte) 254, (byte) 19 }, 0, 32);
		sha256.update(signaturePayload, 0, signaturePayload.length);
		sha256.doFinal(hash2048, 32);
		sha256.reset();
		sha256.update(new byte[] { (byte) 101, (byte) 245, (byte) 91, (byte) 125, (byte) 50, (byte) 79, (byte) 71,
				(byte) 52, (byte) 244, (byte) 249, (byte) 84, (byte) 5, (byte) 139, (byte) 21, (byte) 13, (byte) 200,
				(byte) 75, (byte) 0, (byte) 103, (byte) 1, (byte) 14, (byte) 159, (byte) 199, (byte) 194, (byte) 56,
				(byte) 161, (byte) 63, (byte) 248, (byte) 90, (byte) 134, (byte) 96, (byte) 160 }, 0, 32);
		sha256.update(signaturePayload, 0, signaturePayload.length);
		sha256.doFinal(hash2048, 64);
		sha256.reset();
		sha256.update(new byte[] { (byte) 84, (byte) 208, (byte) 74, (byte) 114, (byte) 251, (byte) 86, (byte) 195,
				(byte) 222, (byte) 90, (byte) 18, (byte) 194, (byte) 226, (byte) 20, (byte) 56, (byte) 191, (byte) 235,
				(byte) 187, (byte) 93, (byte) 18, (byte) 122, (byte) 161, (byte) 40, (byte) 160, (byte) 88, (byte) 151,
				(byte) 88, (byte) 215, (byte) 216, (byte) 253, (byte) 235, (byte) 7, (byte) 60 }, 0, 32);
		sha256.update(signaturePayload, 0, signaturePayload.length);
		sha256.doFinal(hash2048, 96);
		
		hash2048[0] = (byte)((signaturePayload.length >> 8) & 0xFF);
		hash2048[1] = (byte)(signaturePayload.length & 0xFF);
		
		if(!Arrays.equals(hash2048, rsa2048sumDec)) {
			throw new CertificateInvalidException("SHA256 checksum of signature payload is invalid!");
		}
		
		return new UpdateCertificate(certData, EaglerZLIB.newGZIPInputStream(new ByteArrayInputStream(signaturePayload)), vers);
	}

	private UpdateCertificate(byte[] certData, InputStream is, int sigVers) throws IOException {
		this.rawCertData = certData;
		this.sigVersion = sigVers;
		DataInputStream dis = new DataInputStream(is);
		this.sigTimestamp = dis.readLong();
		this.bundleDataLength = dis.readInt();
		this.bundleDataHash = new byte[32];
		dis.read(bundleDataHash);
		this.bundlePackageName = dis.readUTF();
		this.bundleDisplayName = dis.readUTF();
		this.bundleAuthorName = dis.readUTF();
		this.bundleVersionInteger = dis.readInt();
		this.bundleDisplayVersion = dis.readUTF();
		this.bundleVersionComment = dis.readUTF();
		dis.skip(dis.read());
		int sourceCount = dis.readInt();
		this.bundleDataSources = new DLSource[sourceCount];
		for(int i = 0; i < sourceCount; ++i) {
			dis.skip(4);
			bundleDataSources[i] = new DLSource(dis.readUTF(), dis.readUTF());
		}
	}

	public boolean isBundleDataValid(byte[] bundleData) {
		if(bundleData.length != bundleDataLength) {
			return false;
		}
		SHA256Digest sha256 = new SHA256Digest();
		sha256.update(bundleData, 0, bundleData.length);
		byte[] out = new byte[32];
		sha256.doFinal(out, 0);
		return Arrays.equals(out, bundleDataHash);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bundleDataHash);
		result = prime * result + Arrays.hashCode(bundleDataSources);
		result = prime * result
				+ Objects.hash(bundleAuthorName, bundleDataLength, bundleDisplayName, bundleDisplayVersion,
						bundlePackageName, bundleVersionComment, bundleVersionInteger, sigTimestamp, sigVersion);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UpdateCertificate))
			return false;
		UpdateCertificate other = (UpdateCertificate) obj;
		return Objects.equals(bundleAuthorName, other.bundleAuthorName)
				&& Arrays.equals(bundleDataHash, other.bundleDataHash) && bundleDataLength == other.bundleDataLength
				&& Arrays.equals(bundleDataSources, other.bundleDataSources)
				&& Objects.equals(bundleDisplayName, other.bundleDisplayName)
				&& Objects.equals(bundleDisplayVersion, other.bundleDisplayVersion)
				&& Objects.equals(bundlePackageName, other.bundlePackageName)
				&& Objects.equals(bundleVersionComment, other.bundleVersionComment)
				&& bundleVersionInteger == other.bundleVersionInteger && sigTimestamp == other.sigTimestamp
				&& sigVersion == other.sigVersion;
	}

	public ListMultimap<String,String> getSourceMultimap() {
		ListMultimap<String,String> ret = ListMultimapBuilder.hashKeys().arrayListValues().build();
		for(int i = 0; i < bundleDataSources.length; ++i) {
			ret.put(bundleDataSources[i].type, bundleDataSources[i].addr);
		}
		return ret;
	}
}
