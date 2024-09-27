package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import org.teavm.jso.browser.Window;
import org.teavm.jso.typedarrays.ArrayBuffer;

import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformApplication;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.RuntimeInitializationFailureException;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.ClientMain.EPKFileEntry;
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
public class EPKDownloadHelper {

	private static final Logger logger = LogManager.getLogger("BrowserRuntime");

	public static void downloadEPKFilesOfVersion(EPKFileEntry[] epkFiles, String expectedVersionIdentifier,
			Map<String, byte[]> loadedFiles) {
		byte[] bTrue = Base64.decodeBase64("true");
		boolean oldEPKInvalidFlag = Arrays.equals(bTrue, PlatformApplication.getLocalStorage("epkInvalidFlag", false));
		boolean epkInvalidFlag = oldEPKInvalidFlag;
		attempt_loop: for(int a = 0; a < 3; ++a) {
			if(a == 1 && !PlatformRuntime.hasFetchSupportTeaVM()) continue;
			loadedFiles.clear();
			boolean canBeInvalid = expectedVersionIdentifier != null;
			for(int i = 0; i < epkFiles.length; ++i) {
				boolean noCache = false;
				String url = null;
				switch(a) {
				case 0:
					url = epkFiles[i].url;
					noCache = false;
					break;
				case 1:
					logger.warn("Failed to download one or more correct/valid files, attempting to bypass the browser's cache...");
					url = epkFiles[i].url;
					noCache = true;
					break;
				case 2:
					logger.warn("Failed to download one or more correct/valid files, attempting to bypass the server's cache...");
					url = injectCacheInvalidationHack(epkFiles[i].url, expectedVersionIdentifier);
					noCache = true;
					break;
				}
				boolean b = url.startsWith("data:");
				boolean c = !b && !url.startsWith("blob:");
				String toCheck = url.indexOf("://") != -1 ? url : PlatformRuntime.win.getLocation().getFullURL();
				boolean canBeCorrupt = c && (a < 1 || toCheck.startsWith("http:") || toCheck.startsWith("https:"));
				canBeInvalid &= c;
				String logURL = b ? "<data: " + url.length() + " chars>" : url;
				
				logger.info("Downloading: {}", logURL);
				
				ArrayBuffer epkFileData = PlatformRuntime.downloadRemoteURI(url, !noCache);
				
				if(epkFileData == null) {
					if(a < 2 && canBeCorrupt) {
						logger.error("Could not download EPK file \"{}\"", logURL);
						continue attempt_loop;
					}else {
						throw new RuntimeInitializationFailureException("Could not download EPK file \"" + logURL + "\"");
					}
				}
				
				logger.info("Decompressing: {}", logURL);
				
				try {
					EPKLoader.loadEPK(epkFileData, epkFiles[i].path, loadedFiles);
				}catch(Throwable t) {
					if(a < 2 && canBeCorrupt) {
						logger.error("Could not extract EPK file \"{}\"", logURL);
						continue attempt_loop;
					}else {
						throw new RuntimeInitializationFailureException("Could not extract EPK file \"" + logURL + "\"", t);
					}
				}
			}
			if(canBeInvalid) {
				byte[] dat = loadedFiles.get("EPKVersionIdentifier.txt");
				if(dat != null) {
					String epkStr = (new String(dat, StandardCharsets.UTF_8)).trim();
					if(expectedVersionIdentifier.equals(epkStr)) {
						epkInvalidFlag = false;
						break;
					}
					logger.error("EPK version identifier \"{}\" does not match the expected identifier \"{}\"", epkStr, expectedVersionIdentifier);
				}else {
					logger.error("Version identifier file is missing from the EPK, expecting \"{}\"", expectedVersionIdentifier);
				}
				if(epkInvalidFlag) {
					break;
				}else {
					if(a < 2) {
						continue;
					}else {
						logger.error("Nothing we can do about this, ignoring the invalid EPK version and setting invalid flag to true");
						epkInvalidFlag = true;
					}
				}
			}else {
				epkInvalidFlag = false;
				break;
			}
		}
		if(epkInvalidFlag != oldEPKInvalidFlag) {
			PlatformApplication.setLocalStorage("epkInvalidFlag", epkInvalidFlag ? bTrue : null, false);
		}
	}

	private static String injectCacheInvalidationHack(String url, String cacheFixStr) {
		if(cacheFixStr != null) {
			cacheFixStr = Window.encodeURIComponent(cacheFixStr);
		}else {
			cacheFixStr = "t" + System.currentTimeMillis();
		}
		String toCheck = url.indexOf("://") != -1 ? url : PlatformRuntime.win.getLocation().getFullURL();
		if(toCheck.startsWith("http:") || toCheck.startsWith("https:")) {
			int i = url.indexOf('?');
			if(i == url.length() - 1) {
				return url + "eaglerCacheFix=" + cacheFixStr;
			}else if(i != -1) {
				String s = url.substring(i + 1);
				if(!s.startsWith("&") && !s.startsWith("#")) {
					s = "&" + s;
				}
				return url.substring(0, i + 1) + "eaglerCacheFix=" + cacheFixStr + s;
			}else {
				i = url.indexOf('#');
				if(i != -1) {
					return url.substring(0, i) + "?eaglerCacheFix=" + cacheFixStr + url.substring(i);
				}else {
					return url + "?eaglerCacheFix=" + cacheFixStr;
				}
			}
		}else {
			return url;
		}
	}

}
