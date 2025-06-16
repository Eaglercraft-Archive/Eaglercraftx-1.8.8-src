/*
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
import net.lax1dude.eaglercraft.v1_8.ThreadLocalRandom;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArrayReader;

import net.lax1dude.eaglercraft.v1_8.internal.IClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.IClientConfigAdapterHooks;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.opts.JSEaglercraftXOptsHooks;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.opts.JSEaglercraftXOptsRelay;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.opts.JSEaglercraftXOptsRoot;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.opts.JSEaglercraftXOptsServer;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayEntry;

public class WASMGCClientConfigAdapter implements IClientConfigAdapter {

	public static final IClientConfigAdapter instance = new WASMGCClientConfigAdapter();

	private String defaultLocale = "en_US";
	private List<DefaultServer> defaultServers = new ArrayList<>();
	private List<RelayEntry> relays = new ArrayList<>();
	private String serverToJoin = null;   
	private String worldsDB = "worlds";
	private String resourcePacksDB = "resourcePacks";
	private boolean checkGLErrors = false;
	private boolean checkShaderGLErrors = false;
	private boolean demoMode = EaglercraftVersion.forceDemoMode;
	private boolean isEnableDownloadOfflineButton = true;
	private String downloadOfflineButtonLink = null;
	private boolean useSpecialCursors = false;
	private boolean logInvalidCerts = false;
	private boolean checkRelaysForUpdates = false;
	private boolean enableSignatureBadge = false;
	private boolean allowVoiceClient = true;
	private boolean allowFNAWSkins = true;
	private String localStorageNamespace = "_eaglercraftX";
	private final WASMGCClientConfigAdapterHooks hooks = new WASMGCClientConfigAdapterHooks();
	private boolean enableMinceraft = true;
	private boolean enableServerCookies = true;
	private boolean allowServerRedirects = true;
	private boolean crashOnUncaughtExceptions = false;
	private boolean openDebugConsoleOnLaunch = false;
	private boolean fixDebugConsoleUnloadListener = false;
	private boolean forceWebViewSupport = false;
	private boolean enableWebViewCSP = false;
	private boolean autoFixLegacyStyleAttr = false;
	private boolean forceProfanityFilter = false;
	private boolean forceWebGL1 = false;
	private boolean forceWebGL2 = false;
	private boolean allowExperimentalWebGL1 = false;
	private boolean useWebGLExt = true;
	private boolean useJOrbisAudioDecoder = false;
	private boolean useXHRFetch = false;
	private boolean useVisualViewport = true;
	private boolean deobfStackTraces = true;
	private boolean disableBlobURLs = false;
	private boolean eaglerNoDelay = false;
	private boolean ramdiskMode = false;
	private boolean singleThreadMode = false;
	private boolean enforceVSync = true;
	private boolean keepAliveHack = true;
	private boolean finishOnSwap = true;

	public void loadNative(JSObject jsObject) {
		JSEaglercraftXOptsRoot eaglercraftXOpts = (JSEaglercraftXOptsRoot)jsObject;
		
		defaultLocale = eaglercraftXOpts.getLang("en_US");
		serverToJoin = eaglercraftXOpts.getJoinServer(null);
		worldsDB = eaglercraftXOpts.getWorldsDB("worlds");
		resourcePacksDB = eaglercraftXOpts.getResourcePacksDB("resourcePacks");
		checkGLErrors = eaglercraftXOpts.getCheckGLErrors(false);
		checkShaderGLErrors = eaglercraftXOpts.getCheckShaderGLErrors(false);
		demoMode = EaglercraftVersion.forceDemoMode || eaglercraftXOpts.getDemoMode(false);
		isEnableDownloadOfflineButton = eaglercraftXOpts.getEnableDownloadOfflineButton(true);
		downloadOfflineButtonLink = eaglercraftXOpts.getDownloadOfflineButtonLink(null);
		useSpecialCursors = eaglercraftXOpts.getHtml5CursorSupport(false);
		logInvalidCerts = EaglercraftVersion.enableUpdateService && !demoMode && eaglercraftXOpts.getLogInvalidCerts(false);
		enableSignatureBadge = eaglercraftXOpts.getEnableSignatureBadge(false);
		allowVoiceClient = eaglercraftXOpts.getAllowVoiceClient(true);
		allowFNAWSkins = !demoMode && eaglercraftXOpts.getAllowFNAWSkins(true);
		localStorageNamespace = eaglercraftXOpts.getLocalStorageNamespace(EaglercraftVersion.localStorageNamespace);
		enableMinceraft = eaglercraftXOpts.getEnableMinceraft(true);
		enableServerCookies = !demoMode && eaglercraftXOpts.getEnableServerCookies(true);
		allowServerRedirects = eaglercraftXOpts.getAllowServerRedirects(true);
		crashOnUncaughtExceptions = eaglercraftXOpts.getCrashOnUncaughtExceptions(false);
		openDebugConsoleOnLaunch = eaglercraftXOpts.getOpenDebugConsoleOnLaunch(false);
		fixDebugConsoleUnloadListener = eaglercraftXOpts.getFixDebugConsoleUnloadListener(false);
		forceWebViewSupport = eaglercraftXOpts.getForceWebViewSupport(false);
		enableWebViewCSP = eaglercraftXOpts.getEnableWebViewCSP(true);
		autoFixLegacyStyleAttr = eaglercraftXOpts.getAutoFixLegacyStyleAttr(true);
		forceProfanityFilter = eaglercraftXOpts.getForceProfanityFilter(false);
		forceWebGL1 = eaglercraftXOpts.getForceWebGL1(false);
		forceWebGL2 = eaglercraftXOpts.getForceWebGL2(false);
		allowExperimentalWebGL1 = eaglercraftXOpts.getAllowExperimentalWebGL1(true);
		useWebGLExt = eaglercraftXOpts.getUseWebGLExt(true);
		useJOrbisAudioDecoder = eaglercraftXOpts.getUseJOrbisAudioDecoder(false);
		useXHRFetch = eaglercraftXOpts.getUseXHRFetch(false);
		useVisualViewport = eaglercraftXOpts.getUseVisualViewport(true);
		deobfStackTraces = eaglercraftXOpts.getDeobfStackTraces(true);
		disableBlobURLs = eaglercraftXOpts.getDisableBlobURLs(false);
		eaglerNoDelay = eaglercraftXOpts.getEaglerNoDelay(false);
		ramdiskMode = eaglercraftXOpts.getRamdiskMode(false);
		singleThreadMode = eaglercraftXOpts.getSingleThreadMode(false);
		enforceVSync = eaglercraftXOpts.getEnforceVSync(true);
		keepAliveHack = eaglercraftXOpts.getKeepAliveHack(true);
		finishOnSwap = eaglercraftXOpts.getFinishOnSwap(true);
		JSEaglercraftXOptsHooks hooksObj = eaglercraftXOpts.getHooks();
		if(hooksObj != null) {
			hooks.loadHooks(hooksObj);
		}
		
		defaultServers.clear();
		JSArrayReader<JSEaglercraftXOptsServer> serversArray = eaglercraftXOpts.getServers();
		if(serversArray != null) {
			for(int i = 0, l = serversArray.getLength(); i < l; ++i) {
				JSEaglercraftXOptsServer serverEntry = serversArray.get(i);
				boolean hideAddr = serverEntry.getHideAddr(false);
				String serverAddr = serverEntry.getAddr();
				if(serverAddr != null) {
					String serverName = serverEntry.getName("Default Server #" + i);
					defaultServers.add(new DefaultServer(serverName, serverAddr, hideAddr));
				}
			}
		}
		
		relays.clear();
		JSArrayReader<JSEaglercraftXOptsRelay> relaysArray = eaglercraftXOpts.getRelays();
		if(relaysArray != null) {
			boolean gotAPrimary = false;
			for(int i = 0, l = relaysArray.getLength(); i < l; ++i) {
				JSEaglercraftXOptsRelay relay = relaysArray.get(i);
				String addr = relay.getAddr();
				if(addr != null) {
					boolean p = relay.getPrimary();
					if(p) {
						if(gotAPrimary) {
							p = false;
						}else {
							gotAPrimary = true;
						}
					}
					relays.add(new RelayEntry(relay.getAddr(), relay.getComment("Default Relay #" + i), p));
				}
			}
		}
		
		boolean officialUpdates = !demoMode && EaglercraftVersion.updateBundlePackageName.equals("net.lax1dude.eaglercraft.v1_8.client");
		if (relays.size() <= 0) {
			int choice = ThreadLocalRandom.current().nextInt(3);
			relays.add(new RelayEntry("wss://relay.deev.is/", "lax1dude relay #1", choice == 0));
			relays.add(new RelayEntry("wss://relay.lax1dude.net/", "lax1dude relay #2", choice == 1));
			relays.add(new RelayEntry("wss://relay.shhnowisnottheti.me/", "ayunami relay #1", choice == 2));
			checkRelaysForUpdates = !demoMode && eaglercraftXOpts.getCheckRelaysForUpdates(officialUpdates);
		}else {
			if(officialUpdates) {
				for(int i = 0, l = relays.size(); i < l; ++i) {
					String addr = relays.get(i).address;
					if(!addr.contains("deev.is") && !addr.contains("lax1dude.net") && !addr.contains("shhnowisnottheti.me")) {
						officialUpdates = false;
						break;
					}
				}
			}
			checkRelaysForUpdates = !demoMode && eaglercraftXOpts.getCheckRelaysForUpdates(officialUpdates);
		}
		
		RelayManager.relayManager.load(EagRuntime.getStorage("r"));
		
		if (RelayManager.relayManager.count() <= 0) {
			RelayManager.relayManager.loadDefaults();
			RelayManager.relayManager.save();
		}
	}

	@Override
	public String getDefaultLocale() {
		return defaultLocale;
	}

	@Override
	public List<DefaultServer> getDefaultServerList() {
		return defaultServers;
	}

	@Override
	public String getServerToJoin() {
		return serverToJoin;
	}

	@Override
	public String getWorldsDB() {
		return worldsDB;
	}

	@Override
	public String getResourcePacksDB() {
		return resourcePacksDB;
	}

	@Override
	public JSONObject getIntegratedServerOpts() {
		return null;
	}

	@Override
	public List<RelayEntry> getRelays() {
		return relays;
	}

	@Override
	public boolean isCheckGLErrors() {
		return checkGLErrors;
	}

	@Override
	public boolean isCheckShaderGLErrors() {
		return checkShaderGLErrors;
	}

	@Override
	public boolean isDemo() {
		return demoMode;
	}

	@Override
	public boolean allowUpdateSvc() {
		return false;
	}

	@Override
	public boolean allowUpdateDL() {
		return false;
	}

	@Override
	public boolean isEnableDownloadOfflineButton() {
		return isEnableDownloadOfflineButton;
	}

	@Override
	public String getDownloadOfflineButtonLink() {
		return downloadOfflineButtonLink;
	}

	@Override
	public boolean useSpecialCursors() {
		return useSpecialCursors;
	}

	@Override
	public boolean isLogInvalidCerts() {
		return logInvalidCerts;
	}

	@Override
	public boolean isCheckRelaysForUpdates() {
		return false;
	}

	@Override
	public boolean isEnableSignatureBadge() {
		return enableSignatureBadge;
	}

	@Override
	public boolean isAllowVoiceClient() {
		return allowVoiceClient;
	}

	@Override
	public boolean isAllowFNAWSkins() {
		return allowFNAWSkins;
	}

	@Override
	public String getLocalStorageNamespace() {
		return localStorageNamespace;
	}

	@Override
	public boolean isEnableMinceraft() {
		return enableMinceraft;
	}

	@Override
	public boolean isEnableServerCookies() {
		return enableServerCookies;
	}

	@Override
	public boolean isAllowServerRedirects() {
		return allowServerRedirects;
	}

	@Override
	public boolean isOpenDebugConsoleOnLaunch() {
		return openDebugConsoleOnLaunch;
	}

	public boolean isFixDebugConsoleUnloadListenerTeaVM() {
		return fixDebugConsoleUnloadListener;
	}

	@Override
	public boolean isForceWebViewSupport() {
		return forceWebViewSupport;
	}

	@Override
	public boolean isEnableWebViewCSP() {
		return enableWebViewCSP;
	}

	public boolean isAutoFixLegacyStyleAttrTeaVM() {
		return autoFixLegacyStyleAttr;
	}

	public boolean isForceWebGL1TeaVM() {
		return forceWebGL1;
	}

	public boolean isForceWebGL2TeaVM() {
		return forceWebGL2;
	}

	public boolean isAllowExperimentalWebGL1TeaVM() {
		return allowExperimentalWebGL1;
	}

	public boolean isUseWebGLExtTeaVM() {
		return useWebGLExt;
	}

	public boolean isUseJOrbisAudioDecoderTeaVM() {
		return useJOrbisAudioDecoder;
	}

	public boolean isUseXHRFetchTeaVM() {
		return useXHRFetch;
	}

	public boolean isDeobfStackTracesTeaVM() {
		return deobfStackTraces;
	}

	public boolean isUseVisualViewportTeaVM() {
		return useVisualViewport;
	}

	public boolean isDisableBlobURLsTeaVM() {
		return disableBlobURLs;
	}

	public boolean isSingleThreadModeTeaVM() {
		return singleThreadMode;
	}

	@Override
	public boolean isAllowBootMenu() {
		return false;
	}

	@Override
	public boolean isForceProfanityFilter() {
		return forceProfanityFilter;
	}

	@Override
	public boolean isEaglerNoDelay() {
		return eaglerNoDelay;
	}

	@Override
	public boolean isRamdiskMode() {
		return ramdiskMode;
	}

	@Override
	public boolean isEnforceVSync() {
		return enforceVSync;
	}

	public boolean isKeepAliveHackTeaVM() {
		return keepAliveHack;
	}

	public boolean isFinishOnSwapTeaVM() {
		return finishOnSwap;
	}

	@Override
	public IClientConfigAdapterHooks getHooks() {
		return hooks;
	}

	public JSONObject toJSONObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("lang", defaultLocale);
		jsonObject.put("joinServer", serverToJoin);
		jsonObject.put("worldsDB", worldsDB);
		jsonObject.put("resourcePacksDB", resourcePacksDB);
		jsonObject.put("checkGLErrors", checkGLErrors);
		jsonObject.put("checkShaderGLErrors", checkShaderGLErrors);
		jsonObject.put("demoMode", demoMode);
		jsonObject.put("enableDownloadOfflineButton", isEnableDownloadOfflineButton);
		jsonObject.put("downloadOfflineButtonLink", downloadOfflineButtonLink);
		jsonObject.put("html5CursorSupport", useSpecialCursors);
		jsonObject.put("logInvalidCerts", logInvalidCerts);
		jsonObject.put("checkRelaysForUpdates", checkRelaysForUpdates);
		jsonObject.put("enableSignatureBadge", enableSignatureBadge);
		jsonObject.put("allowVoiceClient", allowVoiceClient);
		jsonObject.put("allowFNAWSkins", allowFNAWSkins);
		jsonObject.put("localStorageNamespace", localStorageNamespace);
		jsonObject.put("enableMinceraft", enableMinceraft);
		jsonObject.put("enableServerCookies", enableServerCookies);
		jsonObject.put("allowServerRedirects", allowServerRedirects);
		jsonObject.put("crashOnUncaughtExceptions", crashOnUncaughtExceptions);
		jsonObject.put("openDebugConsoleOnLaunch", openDebugConsoleOnLaunch);
		jsonObject.put("fixDebugConsoleUnloadListener", fixDebugConsoleUnloadListener);
		jsonObject.put("forceWebViewSupport", forceWebViewSupport);
		jsonObject.put("enableWebViewCSP", enableWebViewCSP);
		jsonObject.put("autoFixLegacyStyleAttr", autoFixLegacyStyleAttr);
		jsonObject.put("forceProfanityFilter", forceProfanityFilter);
		jsonObject.put("forceWebGL1", forceWebGL1);
		jsonObject.put("forceWebGL2", forceWebGL2);
		jsonObject.put("allowExperimentalWebGL1", allowExperimentalWebGL1);
		jsonObject.put("useWebGLExt", useWebGLExt);
		jsonObject.put("useJOrbisAudioDecoder", useJOrbisAudioDecoder);
		jsonObject.put("useXHRFetch", useXHRFetch);
		jsonObject.put("useVisualViewport", useVisualViewport);
		jsonObject.put("deobfStackTraces", deobfStackTraces);
		jsonObject.put("disableBlobURLs", disableBlobURLs);
		jsonObject.put("eaglerNoDelay", eaglerNoDelay);
		jsonObject.put("ramdiskMode", ramdiskMode);
		jsonObject.put("singleThreadMode", singleThreadMode);
		jsonObject.put("enforceVSync", enforceVSync);
		jsonObject.put("keepAliveHack", keepAliveHack);
		jsonObject.put("finishOnSwap", finishOnSwap);
		JSONArray serversArr = new JSONArray();
		for(int i = 0, l = defaultServers.size(); i < l; ++i) {
			DefaultServer srv = defaultServers.get(i);
			JSONObject obj = new JSONObject();
			obj.put("addr", srv.addr);
			obj.put("hideAddr", srv.hideAddress);
			obj.put("name", srv.name);
			serversArr.put(obj);
		}
		jsonObject.put("servers", serversArr);
		JSONArray relaysArr = new JSONArray();
		for(int i = 0, l = relays.size(); i < l; ++i) {
			RelayEntry rl = relays.get(i);
			JSONObject obj = new JSONObject();
			obj.put("addr", rl.address);
			obj.put("comment", rl.comment);
			obj.put("primary", rl.primary);
			relaysArr.put(obj);
		}
		jsonObject.put("relays", relaysArr);
		return jsonObject;
	}

	@Override
	public String toString() {
		return toJSONObject().toString();
	}

	public String toStringFormatted() {
		return toJSONObject().toString(4);
	}

}