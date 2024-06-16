package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.ajax.ProgressEvent;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.typedarrays.ArrayBuffer;

import com.google.common.collect.ListMultimap;

import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformApplication;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformAssets;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformUpdateSvc;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.update.UpdateCertificate;
import net.lax1dude.eaglercraft.v1_8.update.UpdateProgressStruct;
import net.lax1dude.eaglercraft.v1_8.update.UpdateService;

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
public class TeaVMUpdateThread implements Runnable {

	private static final Logger logger = LogManager.getLogger("TeaVMUpdateThread");

	public final UpdateCertificate updateCert;
	public final UpdateProgressStruct updateProg;

	public TeaVMUpdateThread(UpdateCertificate updateCert, UpdateProgressStruct updateProg) {
		this.updateCert = updateCert;
		this.updateProg = updateProg;
	}

	@Override
	public void run() {
		boolean success = false;
		try {
			logger.info("Starting update thread...");
			updateProg.clear();
			updateProg.isBusy = true;
			updateProg.statusString1 = updateCert.bundleDisplayName + " - " + updateCert.bundleDisplayVersion;
			updateProg.statusString2 = "Please Wait";

			List<String> urlListA = new ArrayList();
			ListMultimap<String,String> downloadSources = updateCert.getSourceMultimap();

			List<String> ls = downloadSources.get("list");
			for(int k = 0, l = ls.size(); k < l; ++k) {
				String str1 = ls.get(k);
				updateProg.statusString2 = "Fetch List (" + (k + 1) + "/" + l + ")";
				byte[] b = downloadWithProgress(str1);
				if(b == null) {
					logger.error("Failed to load additional url list: {}", str1);
					continue;
				}
				try {
					String[] str2 = EagUtils.linesArray(new String(b, StandardCharsets.UTF_8));
					for(int i = 0; i < str2.length; ++i) {
						if(!StringUtils.isAllBlank(str2[i]) && (str2[i] = str2[i].trim()).charAt(0) != '#') {
							String[] strrr = str2[i].split(":", 2);
							downloadSources.put(strrr[0].trim(), strrr[1].trim());
						}
					}
				}catch(Throwable t) {
					logger.error("Failed to load/parse url list: {}", str1);
					logger.error(t);
				}
			}
			
			updateProg.statusString2 = "Please Wait";
			
			urlListA.addAll(downloadSources.get("url"));
			
			List<String> ls2 = downloadSources.get("use-gateway");
			ls = downloadSources.get("ipfs");
			for(int k = 0, l = ls.size(); k < l; ++k) {
				String str1 = ls.get(k);
				String cid = str1;
				String path = "";
				int pathSep = str1.indexOf('/');
				if(pathSep != -1) {
					path = cid.substring(pathSep + 1);
					cid = cid.substring(0, pathSep);
				}
				for(int p = 0, q = ls2.size(); p < q; ++p) {
					String str2 = ls2.get(p);
					urlListA.add(formatIPFSURL(cid, path, str2));
				}
			}
			
			List<String> urlListB = new ArrayList();
			ls = downloadSources.get("use-proxy");
			for(int k = 0, l = ls.size(); k < l; ++k) {
				String str1 = ls.get(k);
				for(int p = 0, q = urlListA.size(); p < q; ++p) {
					String str2 = urlListA.get(p);
					urlListB.add(formatProxyURL(str2, str1));
				}
			}
			
			Collections.shuffle(urlListA);
			Collections.shuffle(urlListB);
			
			urlListA.addAll(urlListB);
			
			for(int i = 0, l = urlListA.size(); i < l; ++i) {
				String url = urlListA.get(i);
				updateProg.statusString2 = "Attempt (" + (i + 1) + "/" + l + ")";
				byte[] b = downloadWithProgress(url);
				if(b == null) {
					updateProg.progressBar = 1.0f;
					updateProg.statusString3 = "FAILED!";
					EagUtils.sleep(300l);
					updateProg.progressBar = -1.0f;
					updateProg.statusString3 = null;
					continue;
				}
				updateProg.progressBar = 1.0f;
				updateProg.statusString2 = "Verifying";
				logger.info("Verifying downloaded file...");
				if(updateCert.isBundleDataValid(b)) {
					logger.info("Success! Signature is valid!");
					downloadSignedOffline(updateCert, b);
					success = true;
					return;
				}
				updateProg.statusString2 = "Signature Invalid!";
				logger.error("File signature is invalid: {}", url);
				EagUtils.sleep(1000l);
			}
			
			updateProg.progressBar = -1.0f;
			updateProg.statusString3 = null;
			
		}catch(Throwable t) {
			logger.error("Uncaught exception downloading updates!");
			logger.error(t);
		}finally {
			PlatformUpdateSvc.updateThread = null;
			updateProg.isBusy = false;
			if(!success) {
				logger.error("Failed to download updates! No valid URL was found for {}", updateCert.bundleDisplayVersion);
				Window.alert("ERROR: Failed to download updates!\n\nIf you are on a device with restricted internet access, try a different device or connect to a different WiFi network\n\nCheck the debug console for more info");
			}else {
				UpdateService.dismiss(updateCert);
			}
		}
	}

	private byte[] downloadWithProgress(String url) {
		updateProg.progressBar = 0.0f;
		try {
			updateProg.statusString3 = url;
			logger.info("Trying to download: {}", url);
			byte[] b = downloadWithProgress0(this, url);
			if(b == null) {
				logger.error("Failed to download: {}", url);
			}
			return b;
		}finally {
			updateProg.statusString3 = null;
		}
	}

	@Async
	private static native byte[] downloadWithProgress0(TeaVMUpdateThread self, String url);

	private static void downloadWithProgress0(TeaVMUpdateThread self, String url, AsyncCallback<byte[]> cb) {
		try {
			self.downloadWithProgressImpl(url, cb);
		}catch(Throwable t) {
			logger.error("Exception caught downloading file: {}", url);
			logger.error(t);
			cb.complete(null);
		}
	}

	private void downloadWithProgressImpl(String url, final AsyncCallback<byte[]> cb) {
		final XMLHttpRequest xhr = XMLHttpRequest.create();
		xhr.open("GET", url);
		xhr.setResponseType("arraybuffer");
		TeaVMUtils.addEventListener(xhr, "progress", new EventListener<ProgressEvent>() {
			@Override
			public void handleEvent(ProgressEvent evt) {
				updateProg.progressBar = Math.min((float)evt.getLoaded() / (float)updateCert.bundleDataLength, 1.0f);
			}
		});
		TeaVMUtils.addEventListener(xhr, "readystatechange", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				if(xhr.getReadyState() == 4) {
					if(xhr.getStatus() == 200) {
						ArrayBuffer data = (ArrayBuffer)xhr.getResponse();
						if(data.getByteLength() == updateCert.bundleDataLength) {
							cb.complete(TeaVMUtils.wrapByteArrayBuffer(data));
						}else {
							logger.error("Unexpected response length {} (expect: {}) from URL: {}", xhr.getStatus(), xhr.getStatusText(), url);
							cb.complete(null);
						}
					}else {
						logger.error("Got response code {} \"{}\" for url: {}", xhr.getStatus(), xhr.getStatusText(), url);
						cb.complete(null);
					}
				}
			}
		});
		TeaVMUtils.addEventListener(xhr, "error", new EventListener<ProgressEvent>() {
			@Override
			public void handleEvent(ProgressEvent evt) {
				logger.error("Exception caught downloading file: {}", url);
				
			}
		});
		xhr.send();
	}

	private static String formatIPFSURL(String cid, String path, String pattern) {
		return pattern.replace("$cid$", cid).replace("$path$", path);
	}

	private static String formatProxyURL(String path, String pattern) {
		return pattern.replace("$url$", Window.encodeURIComponent(path));
	}

	public static void downloadSignedOffline(UpdateCertificate cert, byte[] data) {
		PlatformApplication.downloadFileWithName(cert.bundleDisplayName.replaceAll("[^a-zA-Z0-9\\-_\\.]", "_") + "_" + cert.bundleDisplayVersion.replaceAll("[^a-zA-Z0-9\\-_]", "_") + "_Offline_Signed.html", generateSignedOffline(cert, data));
	}

	public static byte[] generateSignedOffline(UpdateCertificate cert, byte[] data) {
		return generateSignedOffline(cert.rawCertData, data, EagRuntime.fixDateFormat(new SimpleDateFormat("MM/dd/yyyy")).format(new Date(cert.sigTimestamp)));
	}

	public static byte[] generateSignedOffline(byte[] cert, byte[] data, String date) {
		byte[] b = PlatformAssets.getResourceBytes("SignedClientTemplate.txt");
		if(b == null) {
			throw new RuntimeException("Could not load SignedClientTemplate.txt from assets.epk!");
		}
		String templateHtml = new String(b, StandardCharsets.UTF_8);
		templateHtml = templateHtml.replace("${client_signature}", Base64.encodeBase64String(cert));
		templateHtml = templateHtml.replace("${client_bundle}", Base64.encodeBase64String(data));
		templateHtml = templateHtml.replace("${date}", date);
		return templateHtml.getBytes(StandardCharsets.UTF_8);
	}
}
