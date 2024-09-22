package net.lax1dude.eaglercraft.v1_8.cookie;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerOutputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerZLIB;
import net.lax1dude.eaglercraft.v1_8.crypto.AESLightEngine;
import net.lax1dude.eaglercraft.v1_8.crypto.SHA1Digest;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformApplication;
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
public class ServerCookieDataStore {

	private static final Logger logger = LogManager.getLogger("ServerCookieDataStore");

	private static final Map<String,ServerCookie> dataStore = new HashMap<>();

	public static final String localStorageKey = "c";

	public static class ServerCookie {

		public final String server;
		public final byte[] cookie;
		public final long expires;
		public final boolean revokeQuerySupported;
		public final boolean saveCookieToDisk;

		public ServerCookie(String server, byte[] cookie, long expires, boolean revokeQuerySupported, boolean saveCookieToDisk) {
			this.server = server;
			this.cookie = cookie;
			this.expires = expires;
			this.revokeQuerySupported = revokeQuerySupported;
			this.saveCookieToDisk = saveCookieToDisk;
		}

	}

	public static void load() {
		if(EagRuntime.getConfiguration().isEnableServerCookies()) {
			loadData(HardwareFingerprint.getFingerprint());
		}
	}

	public static ServerCookie loadCookie(String server) {
		if(!EagRuntime.getConfiguration().isEnableServerCookies()) {
			return null;
		}
		server = normalize(server);
		ServerCookie cookie = dataStore.get(server);
		if(cookie == null) {
			return null;
		}
		long timestamp = System.currentTimeMillis();
		if(timestamp > cookie.expires) {
			dataStore.remove(server);
			saveData(HardwareFingerprint.getFingerprint());
			return null;
		}
		return cookie;
	}

	public static void saveCookie(String server, long expires, byte[] data, boolean revokeQuerySupported, boolean saveCookieToDisk) {
		if(!EagRuntime.getConfiguration().isEnableServerCookies()) {
			return;
		}
		server = normalize(server);
		if(expires > 604800l) {
			clearCookie(server);
			logger.error("Server \"{}\" tried to set a cookie for {} days! (The max is 7 days)", server, (expires / 604800l));
			return;
		}
		if(data.length > 255) {
			clearCookie(server);
			logger.error("Server \"{}\" tried to set a {} byte cookie! (The max length is 255 bytes)", server, data.length);
			return;
		}
		if(expires < 0l || data.length == 0) {
			clearCookie(server);
			return;
		}
		long expiresRelative = System.currentTimeMillis() + expires * 1000l;
		dataStore.put(server, new ServerCookie(server, data, expiresRelative, revokeQuerySupported, saveCookieToDisk));
		saveData(HardwareFingerprint.getFingerprint());
	}

	public static void clearCookie(String server) {
		if(!EagRuntime.getConfiguration().isEnableServerCookies()) {
			return;
		}
		if(dataStore.remove(normalize(server)) != null) {
			saveData(HardwareFingerprint.getFingerprint());
		}
	}

	public static void clearCookiesLow() {
		dataStore.clear();
	}

	private static String normalize(String server) {
		int j = server.indexOf('/');
		if(j != -1) {
			int i = server.indexOf("://");
			if(i != -1) {
				j = server.indexOf('/', i + 3);
				if(j == -1) {
					return server.toLowerCase();
				}else {
					return server.substring(0, j).toLowerCase() + server.substring(j);
				}
			}else {
				return server.substring(0, j).toLowerCase() + server.substring(j);
			}
		}else {
			return server.toLowerCase();
		}
	}

	public static Set<String> getAllServers() {
		return dataStore.keySet();
	}

	public static List<String> getRevokableServers() {
		List<String> ret = new ArrayList<>(dataStore.size());
		for(ServerCookie c : dataStore.values()) {
			if(c.revokeQuerySupported) {
				ret.add(c.server);
			}
		}
		return ret;
	}

	public static int size() {
		return dataStore.size();
	}

	public static int numRevokable() {
		int i = 0;
		for(ServerCookie c : dataStore.values()) {
			if(c.revokeQuerySupported) {
				++i;
			}
		}
		return i;
	}

	public static void flush() {
		Iterator<ServerCookie> itr = dataStore.values().iterator();
		boolean changed = false;
		while(itr.hasNext()) {
			long timestamp = System.currentTimeMillis();
			ServerCookie cookie = itr.next();
			if(timestamp > cookie.expires) {
				itr.remove();
				changed = true;
			}
		}
		if(changed) {
			saveData(HardwareFingerprint.getFingerprint());
		}
	}

	private static void loadData(byte[] key) {
		dataStore.clear();
		byte[] cookiesTag = PlatformApplication.getLocalStorage(localStorageKey, false);
		if(cookiesTag == null) {
			return;
		}
		if(cookiesTag.length <= 25) {
			PlatformApplication.setLocalStorage(localStorageKey, null, false);
			return;
		}
		try {
			byte[] decrypted;
			int decryptedLen;
			switch(cookiesTag[0]) {
			case 2:
				if(key == null || key.length == 0) {
					throw new IOException("Data is encrypted!");
				}
				decrypted = new byte[cookiesTag.length - 5];
				decryptedLen = (cookiesTag[1] << 24) | (cookiesTag[2] << 16) | (cookiesTag[3] << 8) | (cookiesTag[4] & 0xFF);
				if(decryptedLen < 25) {
					throw new IOException("too short!");
				}
				AESLightEngine aes = new AESLightEngine();
				aes.init(false, key);
				int bs = aes.getBlockSize();
				if(decrypted.length % bs != 0) {
					throw new IOException("length not aligned to block size!");
				}
				byte[] cbcHelper = new byte[] { (byte) 29, (byte) 163, (byte) 4, (byte) 20, (byte) 207, (byte) 26,
						(byte) 140, (byte) 55, (byte) 246, (byte) 250, (byte) 141, (byte) 183, (byte) 153, (byte) 154,
						(byte) 59, (byte) 4 };
				for(int i = 0; i < decryptedLen; i += bs) {
					processBlockDecryptHelper(aes, cookiesTag, 5 + i, decrypted, i, bs, Math.min(decryptedLen - i, bs), cbcHelper);
				}
				if(decrypted[0] != (byte)0x69) {
					throw new IOException("Data is corrupt!");
				}
				break;
			case 1:
				if(key != null && key.length > 0) {
					throw new IOException("Data isn't encrypted!");
				}
				decrypted = cookiesTag;
				decryptedLen = cookiesTag.length;
				break;
			default:
				throw new IOException("Unknown type!");
			}
			SHA1Digest digest = new SHA1Digest();
			digest.update(decrypted, 25, decryptedLen - 25);
			byte[] digestOut = new byte[20];
			digest.doFinal(digestOut, 0);
			for(int i = 0; i < 20; ++i) {
				if(digestOut[i] != decrypted[5 + i]) {
					throw new IOException("Invalid checksum!");
				}
			}
			int decompressedLen = (decrypted[1] << 24) | (decrypted[2] << 16) | (decrypted[3] << 8) | (decrypted[4] & 0xFF);
			byte[] decompressed = new byte[decompressedLen];
			try (InputStream zstream = EaglerZLIB.newInflaterInputStream(new EaglerInputStream(decrypted, 25, decryptedLen - 25))) {
				int i = 0, j;
				while(i < decompressedLen && (j = zstream.read(decompressed, i, decompressedLen - i)) != -1) {
					i += j;
				}
				if(i != decompressedLen) {
					throw new IOException("Length does not match!");
				}
			}
			DataInputStream dis = new DataInputStream(new EaglerInputStream(decompressed));
			int readCount = dis.readInt();
			long time = System.currentTimeMillis();
			for(int i = 0; i < readCount; ++i) {
				byte flags = dis.readByte();
				long expires = dis.readLong();
				int len = dis.readUnsignedShort();
				String server = dis.readUTF();
				if(len == 0) {
					continue;
				}
				if(expires < time) {
					dis.skipBytes(len);
					continue;
				}
				byte[] cookieData = new byte[len];
				dis.readFully(cookieData);
				server = normalize(server);
				dataStore.put(server, new ServerCookie(server, cookieData, expires, (flags & 1) != 0, (flags & 2) != 0));
			}
			if(dis.available() > 0) {
				throw new IOException("Extra bytes remaining!");
			}
		}catch (IOException e) {
			dataStore.clear();
			PlatformApplication.setLocalStorage(localStorageKey, null, false);
			return;
		}
	}

	private static void saveData(byte[] key) {
		Iterator<ServerCookie> itr = dataStore.values().iterator();
		List<ServerCookie> toSave = new ArrayList<>(dataStore.size());
		while(itr.hasNext()) {
			long timestamp = System.currentTimeMillis();
			ServerCookie cookie = itr.next();
			if(timestamp > cookie.expires || cookie.cookie.length > 255 || cookie.cookie.length == 0) {
				itr.remove();
			}else if(cookie.saveCookieToDisk) {
				toSave.add(cookie);
			}
		}
		if(toSave.size() == 0) {
			PlatformApplication.setLocalStorage(localStorageKey, null, false);
		}else {
			EaglerOutputStream bao = new EaglerOutputStream(1024);
			bao.skipBytes(25);
			int totalUncompressedLen;
			try(DataOutputStream zstream = new DataOutputStream(EaglerZLIB.newDeflaterOutputStream(bao))) {
				zstream.writeInt(dataStore.size());
				for(Entry<String,ServerCookie> etr : dataStore.entrySet()) {
					ServerCookie cookie = etr.getValue();
					zstream.writeByte((cookie.revokeQuerySupported ? 1 : 0) | (cookie.saveCookieToDisk ? 2 : 0));
					zstream.writeLong(cookie.expires);
					zstream.writeShort(cookie.cookie.length);
					zstream.writeUTF(etr.getKey());
					zstream.write(cookie.cookie);
				}
				totalUncompressedLen = zstream.size();
			} catch (IOException e) {
				logger.error("Failed to write cookies to local storage!");
				return;
			}
			byte[] toEncrypt = bao.toByteArray();
			SHA1Digest hash = new SHA1Digest();
			hash.update(toEncrypt, 25, toEncrypt.length - 25);
			hash.doFinal(toEncrypt, 5);
			toEncrypt[1] = (byte)(totalUncompressedLen >>> 24);
			toEncrypt[2] = (byte)(totalUncompressedLen >>> 16);
			toEncrypt[3] = (byte)(totalUncompressedLen >>> 8);
			toEncrypt[4] = (byte)(totalUncompressedLen & 0xFF);
			if(key != null && key.length > 0) {
				toEncrypt[0] = (byte)0x69;
				AESLightEngine aes = new AESLightEngine();
				aes.init(true, key);
				int bs = aes.getBlockSize();
				int blockCount = (toEncrypt.length % bs) != 0 ? (toEncrypt.length / bs + 1) : (toEncrypt.length / bs);
				byte[] encrypted = new byte[blockCount * bs + 5];
				encrypted[0] = (byte)2;
				encrypted[1] = (byte)(toEncrypt.length >>> 24);
				encrypted[2] = (byte)(toEncrypt.length >>> 16);
				encrypted[3] = (byte)(toEncrypt.length >>> 8);
				encrypted[4] = (byte)(toEncrypt.length & 0xFF);
				byte[] cbcHelper = new byte[] { (byte) 29, (byte) 163, (byte) 4, (byte) 20, (byte) 207, (byte) 26,
						(byte) 140, (byte) 55, (byte) 246, (byte) 250, (byte) 141, (byte) 183, (byte) 153, (byte) 154,
						(byte) 59, (byte) 4 };
				for(int i = 0; i < toEncrypt.length; i += bs) {
					processBlockEncryptHelper(aes, toEncrypt, i, encrypted, 5 + i, bs, cbcHelper);
				}
				PlatformApplication.setLocalStorage(localStorageKey, encrypted, false);
			}else {
				toEncrypt[0] = (byte)1;
				PlatformApplication.setLocalStorage(localStorageKey, toEncrypt, false);
			}
		}
	}

	private static void processBlockEncryptHelper(AESLightEngine aes, byte[] in, int inOff, byte[] out, int outOff,
			int len, byte[] cbcHelper) {
		int clampedBlockLength = Math.min(in.length - inOff, len);
		if(clampedBlockLength == len) {
			for(int i = 0; i < len; ++i) {
				in[i + inOff] ^= cbcHelper[i];
			}
			aes.processBlock(in, inOff, out, outOff);
			System.arraycopy(out, outOff, cbcHelper, 0, len);
		}else {
			byte[] paddedBlock = new byte[len];
			System.arraycopy(in, inOff, paddedBlock, 0, clampedBlockLength);
			byte padValue = (byte)(len - clampedBlockLength);
			for(byte i = 0; i < padValue; ++i) {
				paddedBlock[clampedBlockLength + i] = padValue;
			}
			for(int i = 0; i < len; ++i) {
				paddedBlock[i] ^= cbcHelper[i];
			}
			aes.processBlock(paddedBlock, 0, out, outOff);
		}
	}

	private static void processBlockDecryptHelper(AESLightEngine aes, byte[] in, int inOff, byte[] out, int outOff,
			int paddedLen, int unpaddedLen, byte[] cbcHelper) throws IOException {
		aes.processBlock(in, inOff, out, outOff);
		for(int i = 0; i < paddedLen; ++i) {
			out[i + outOff] ^= cbcHelper[i];
		}
		if(unpaddedLen == paddedLen) {
			System.arraycopy(in, inOff, cbcHelper, 0, paddedLen);
		}else {
			byte padValue = (byte)(paddedLen - unpaddedLen);
			for(byte i = 0; i < padValue; ++i) {
				if(out[outOff + unpaddedLen + i] != padValue) {
					throw new IOException("Invalid padding!");
				}
			}
		}
	}
}
