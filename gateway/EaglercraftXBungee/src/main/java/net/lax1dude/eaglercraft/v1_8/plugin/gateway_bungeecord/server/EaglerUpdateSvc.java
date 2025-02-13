/*
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.EaglerXBungeeAPIHelper;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.auth.SHA1Digest;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerUpdateConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler.ClientCertificateHolder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class EaglerUpdateSvc {

	private static final List<ClientCertificateHolder> certs = new ArrayList<>();
	private static final Map<String,CachedClientCertificate> certsCache = new HashMap<>();
	private static final Set<String> deadURLS = new HashSet<>();

	private static class CachedClientCertificate {
		private final ClientCertificateHolder cert;
		private final long lastModified;
		public CachedClientCertificate(ClientCertificateHolder cert, long lastModified) {
			this.cert = cert;
			this.lastModified = lastModified;
		}
	}

	private static long lastDownload = 0l;
	private static long lastEnumerate = 0l;

	public static void updateTick() {
		Logger log = EaglerXBungee.logger();
		long millis = EaglerXBungeeAPIHelper.steadyTimeMillis();
		EaglerUpdateConfig conf = EaglerXBungee.getEagler().getConfig().getUpdateConfig();
		if(conf.isDownloadLatestCerts() && millis - lastDownload > (long)conf.getCheckForUpdatesEvery() * 1000l) {
			lastDownload = millis;
			lastEnumerate = 0l;
			try {
				downloadUpdates();
			}catch(Throwable t) {
				log.severe("Uncaught exception downloading certificates!");
				t.printStackTrace();
			}
			millis = EaglerXBungeeAPIHelper.steadyTimeMillis();
		}
		if(conf.isEnableEagcertFolder() && millis - lastEnumerate > 5000l) {
			lastEnumerate = millis;
			try {
				enumerateEagcertDirectory();
			}catch(Throwable t) {
				log.severe("Uncaught exception reading eagcert directory!");
				t.printStackTrace();
			}
		}
	}

	private static void downloadUpdates() throws Throwable {
		Logger log = EaglerXBungee.logger();
		EaglerUpdateConfig conf = EaglerXBungee.getEagler().getConfig().getUpdateConfig();
		File eagcert = new File(EaglerXBungee.getEagler().getDataFolder(), "eagcert");
		if(!eagcert.isDirectory()) {
			if(!eagcert.mkdirs()) {
				log.severe("Could not create directory: " + eagcert.getAbsolutePath());
				return;
			}
		}
		Set<String> filenames = new HashSet<>();
		for(String str : conf.getDownloadCertURLs()) {
			try {
				URL url = new URL(str);
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				con.setDoInput(true);
				con.setDoOutput(false);
				con.setRequestMethod("GET");
				con.setConnectTimeout(30000);
				con.setReadTimeout(30000);
				con.setRequestProperty("User-Agent", "Mozilla/5.0 EaglerXBungee/" + EaglerXBungee.getEagler().getDescription().getVersion());
				con.connect();
				int code = con.getResponseCode();
				if(code / 100 != 2) {
					con.disconnect();
					throw new IOException("Response code was " + code);
				}else {
					deadURLS.remove(str);
				}
				ByteArrayOutputStream bao = new ByteArrayOutputStream(32767);
				try(InputStream is = con.getInputStream()) {
					byte[] readBuffer = new byte[1024];
					int c;
					while((c = is.read(readBuffer, 0, 1024)) != -1) {
						bao.write(readBuffer, 0, c);
					}
				}
				byte[] done = bao.toByteArray();
				SHA1Digest digest = new SHA1Digest();
				digest.update(done, 0, done.length);
				byte[] hash = new byte[20];
				digest.doFinal(hash, 0);
				char[] hexchars = new char[40];
				for(int i = 0; i < 20; ++i) {
					hexchars[i << 1] = hex[(hash[i] >> 4) & 15];
					hexchars[(i << 1) + 1] = hex[hash[i] & 15];
				}
				String strr = "$dl." + new String(hexchars) + ".cert";
				filenames.add(strr);
				File cacheFile = new File(eagcert, strr);
				if(cacheFile.exists()) {
					continue; // no change
				}
				try(OutputStream os = new FileOutputStream(cacheFile)) {
					os.write(done);
				}
				log.info("Downloading new certificate: " + str);
			}catch(Throwable t) {
				if(deadURLS.add(str)) {
					log.severe("Failed to download certificate: " + str);
					log.severe("Reason: " + t.toString());
				}
			}
		}
		long millis = System.currentTimeMillis();
		File[] dirList = eagcert.listFiles();
		for(int i = 0; i < dirList.length; ++i) {
			File f = dirList[i];
			String n = f.getName();
			if(!n.startsWith("$dl.")) {
				continue;
			}
			if(millis - f.lastModified() > 86400000l && !filenames.contains(n)) {
				log.warning("Deleting stale certificate: " + n);
				if(!f.delete()) {
					log.severe("Failed to delete: " + n);
				}
			}
		}
	}

	private static final char[] hex = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static void enumerateEagcertDirectory() throws Throwable {
		Logger log = EaglerXBungee.logger();
		File eagcert = new File(EaglerXBungee.getEagler().getDataFolder(), "eagcert");
		if(!eagcert.isDirectory()) {
			if(!eagcert.mkdirs()) {
				log.severe("Could not create directory: " + eagcert.getAbsolutePath());
				return;
			}
		}
		boolean dirty = false;
		File[] dirList = eagcert.listFiles();
		Set<String> existingFiles = new HashSet<>();
		for(int i = 0; i < dirList.length; ++i) {
			File f = dirList[i];
			String n = f.getName();
			long lastModified = f.lastModified();
			existingFiles.add(n);
			CachedClientCertificate cc = certsCache.get(n);
			if(cc != null) {
				if(cc.lastModified != lastModified) {
					try {
						byte[] fileData = new byte[(int)f.length()];
						if(fileData.length > 0xFFFF) {
							throw new IOException("File is too long! Max: 65535 bytes");
						}
						try(FileInputStream fis = new FileInputStream(f)) {
							for(int j = 0, k; (k = fis.read(fileData, j, fileData.length - j)) != -1 && j < fileData.length; j += k);
						}
						certsCache.remove(n);
						ClientCertificateHolder ch = tryMakeHolder(fileData);
						certsCache.put(n, new CachedClientCertificate(ch, lastModified));
						dirty = true;
						sendCertificateToPlayers(ch);
						log.info("Reloaded certificate: " + f.getAbsolutePath());
					}catch(IOException ex) {
						log.severe("Failed to read: " + f.getAbsolutePath());
						log.severe("Reason: " + ex.toString());
					}
				}
				continue;
			}
			try {
				byte[] fileData = new byte[(int)f.length()];
				if(fileData.length > 0xFFFF) {
					throw new IOException("File is too long! Max: 65535 bytes");
				}
				try(FileInputStream fis = new FileInputStream(f)) {
					for(int j = 0, k; j < fileData.length && (k = fis.read(fileData, j, fileData.length - j)) != -1; j += k);
				}
				ClientCertificateHolder ch = tryMakeHolder(fileData);
				certsCache.put(n, new CachedClientCertificate(ch, lastModified));
				dirty = true;
				sendCertificateToPlayers(ch);
				log.info("Loaded certificate: " + f.getAbsolutePath());
			}catch(IOException ex) {
				log.severe("Failed to read: " + f.getAbsolutePath());
				log.severe("Reason: " + ex.toString());
			}
		}
		Iterator<String> itr = certsCache.keySet().iterator();
		while(itr.hasNext()) {
			String etr = itr.next();
			if(!existingFiles.contains(etr)) {
				itr.remove();
				dirty = true;
				log.warning("Certificate was deleted: " + etr);
			}
		}
		if(dirty) {
			remakeCertsList();
		}
	}

	private static void remakeCertsList() {
		synchronized(certs) {
			certs.clear();
			for(CachedClientCertificate cc : certsCache.values()) {
				certs.add(cc.cert);
			}
		}
	}

	public static List<ClientCertificateHolder> getCertList() {
		return certs;
	}

	public static ClientCertificateHolder tryMakeHolder(byte[] data) {
		int hash = Arrays.hashCode(data);
		ClientCertificateHolder ret = tryGetHolder(data, hash);
		if(ret == null) {
			ret = new ClientCertificateHolder(data, hash);
		}
		return ret;
	}

	public static ClientCertificateHolder tryGetHolder(byte[] data, int hash) {
		synchronized(certs) {
			for(ClientCertificateHolder cc : certs) {
				if(cc.hash == hash && Arrays.equals(cc.data, data)) {
					return cc;
				}
			}
		}
		for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
			if(p.getPendingConnection() instanceof EaglerInitialHandler) {
				EaglerInitialHandler pp = (EaglerInitialHandler)p.getPendingConnection();
				if(pp.clientCertificate != null && pp.clientCertificate.hash == hash && Arrays.equals(pp.clientCertificate.data, data)) {
					return pp.clientCertificate;
				}
			}
		}
		return null;
	}

	public static void sendCertificateToPlayers(ClientCertificateHolder holder) {
		for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
			if(p.getPendingConnection() instanceof EaglerInitialHandler) {
				EaglerInitialHandler pp = (EaglerInitialHandler)p.getPendingConnection();
				boolean bb;
				synchronized(pp.certificatesSent) {
					bb = pp.certificatesSent.contains(holder.hashCode());
				}
				if(!bb) {
					Set<ClientCertificateHolder> set = pp.certificatesToSend;
					synchronized(set) {
						set.add(holder);
					}
				}
			}
		}
	}
}