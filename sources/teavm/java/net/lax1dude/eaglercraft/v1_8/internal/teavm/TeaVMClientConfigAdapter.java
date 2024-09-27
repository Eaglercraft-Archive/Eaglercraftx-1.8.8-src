package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
import net.lax1dude.eaglercraft.v1_8.ThreadLocalRandom;
import net.lax1dude.eaglercraft.v1_8.boot_menu.teavm.IBootMenuConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArrayReader;

import net.lax1dude.eaglercraft.v1_8.internal.IClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.IClientConfigAdapterHooks;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.opts.JSEaglercraftXOptsHooks;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.opts.JSEaglercraftXOptsRelay;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.opts.JSEaglercraftXOptsRoot;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.opts.JSEaglercraftXOptsServer;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayEntry;

/**
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
public class TeaVMClientConfigAdapter implements IClientConfigAdapter, IBootMenuConfigAdapter {

	public static final IClientConfigAdapter instance = new TeaVMClientConfigAdapter();

	private String defaultLocale = "en_US";
	private List<DefaultServer> defaultServers = new ArrayList<>();
	private List<RelayEntry> relays = new ArrayList<>();
	private String serverToJoin = null;   
	private String worldsDB = "worlds";
	private String resourcePacksDB = "resourcePacks";
	private JSONObject integratedServerOpts;
	private boolean checkShaderGLErrors = false;
	private boolean demoMode = EaglercraftVersion.forceDemoMode;
	private boolean isAllowUpdateSvc = EaglercraftVersion.enableUpdateService;
	private boolean isAllowUpdateDL = EaglercraftVersion.enableUpdateService;
	private boolean isEnableDownloadOfflineButton = true;
	private String downloadOfflineButtonLink = null;
	private boolean useSpecialCursors = false;
	private boolean logInvalidCerts = false;
	private boolean checkRelaysForUpdates = false;
	private boolean enableSignatureBadge = false;
	private boolean allowVoiceClient = true;
	private boolean allowFNAWSkins = true;
	private String localStorageNamespace = "_eaglercraftX";
	private final TeaVMClientConfigAdapterHooks hooks = new TeaVMClientConfigAdapterHooks();
	private boolean enableMinceraft = true;
	private boolean enableServerCookies = true;
	private boolean allowServerRedirects = true;
	private boolean crashOnUncaughtExceptions = false;
	private boolean openDebugConsoleOnLaunch = false;
	private boolean fixDebugConsoleUnloadListener = false;
	private boolean forceWebViewSupport = false;
	private boolean enableWebViewCSP = false;
	private boolean autoFixLegacyStyleAttr = false;
	private boolean showBootMenuOnLaunch = false;
	private boolean bootMenuBlocksUnsignedClients = false;
	private boolean allowBootMenu = true;
	private boolean forceProfanityFilter = false;
	private boolean forceWebGL1 = false;
	private boolean forceWebGL2 = false;
	private boolean allowExperimentalWebGL1 = false;
	private boolean useWebGLExt = true;
	private boolean useDelayOnSwap = false;
	private boolean useJOrbisAudioDecoder = false;
	private boolean useXHRFetch = false;
	private boolean useVisualViewport = true;
	private boolean deobfStackTraces = true;
	private boolean disableBlobURLs = false;
	private boolean eaglerNoDelay = false;
	private boolean ramdiskMode = false;
	private boolean singleThreadMode = false;
	private boolean enableEPKVersionCheck = true;

	public void loadNative(JSObject jsObject) {
		integratedServerOpts = new JSONObject();
		JSEaglercraftXOptsRoot eaglercraftXOpts = (JSEaglercraftXOptsRoot)jsObject;
		
		defaultLocale = eaglercraftXOpts.getLang("en_US");
		serverToJoin = eaglercraftXOpts.getJoinServer(null);
		worldsDB = eaglercraftXOpts.getWorldsDB("worlds");
		resourcePacksDB = eaglercraftXOpts.getResourcePacksDB("resourcePacks");
		checkShaderGLErrors = eaglercraftXOpts.getCheckShaderGLErrors(false);
		demoMode = EaglercraftVersion.forceDemoMode || eaglercraftXOpts.getDemoMode(false);
		isAllowUpdateSvc = EaglercraftVersion.enableUpdateService && !demoMode && eaglercraftXOpts.getAllowUpdateSvc(true);
		isAllowUpdateDL = EaglercraftVersion.enableUpdateService && !demoMode && eaglercraftXOpts.getAllowUpdateDL(true);
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
		showBootMenuOnLaunch = eaglercraftXOpts.getShowBootMenuOnLaunch(false);
		bootMenuBlocksUnsignedClients = eaglercraftXOpts.getBootMenuBlocksUnsignedClients(false);
		allowBootMenu = eaglercraftXOpts.getAllowBootMenu(!demoMode);
		forceProfanityFilter = eaglercraftXOpts.getForceProfanityFilter(false);
		forceWebGL1 = eaglercraftXOpts.getForceWebGL1(false);
		forceWebGL2 = eaglercraftXOpts.getForceWebGL2(false);
		allowExperimentalWebGL1 = eaglercraftXOpts.getAllowExperimentalWebGL1(true);
		useWebGLExt = eaglercraftXOpts.getUseWebGLExt(true);
		useDelayOnSwap = eaglercraftXOpts.getUseDelayOnSwap(false);
		useJOrbisAudioDecoder = eaglercraftXOpts.getUseJOrbisAudioDecoder(false);
		useXHRFetch = eaglercraftXOpts.getUseXHRFetch(false);
		useVisualViewport = eaglercraftXOpts.getUseVisualViewport(true);
		deobfStackTraces = eaglercraftXOpts.getDeobfStackTraces(true);
		disableBlobURLs = eaglercraftXOpts.getDisableBlobURLs(false);
		eaglerNoDelay = eaglercraftXOpts.getEaglerNoDelay(false);
		ramdiskMode = eaglercraftXOpts.getRamdiskMode(false);
		singleThreadMode = eaglercraftXOpts.getSingleThreadMode(false);
		enableEPKVersionCheck = eaglercraftXOpts.getEnableEPKVersionCheck(true);
		JSEaglercraftXOptsHooks hooksObj = eaglercraftXOpts.getHooks();
		if(hooksObj != null) {
			hooks.loadHooks(hooksObj);
		}
		
		integratedServerOpts.put("worldsDB", worldsDB);
		integratedServerOpts.put("demoMode", demoMode);
		integratedServerOpts.put("lang", defaultLocale);
		integratedServerOpts.put("allowUpdateSvc", isAllowUpdateSvc);
		integratedServerOpts.put("allowUpdateDL", isAllowUpdateDL);
		integratedServerOpts.put("allowVoiceClient", allowVoiceClient);
		integratedServerOpts.put("allowFNAWSkins", allowFNAWSkins);
		integratedServerOpts.put("crashOnUncaughtExceptions", crashOnUncaughtExceptions);
		integratedServerOpts.put("deobfStackTraces", deobfStackTraces);
		integratedServerOpts.put("disableBlobURLs", disableBlobURLs);
		integratedServerOpts.put("eaglerNoDelay", eaglerNoDelay);
		integratedServerOpts.put("ramdiskMode", ramdiskMode);
		integratedServerOpts.put("singleThreadMode", singleThreadMode);
		
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

	public void loadJSON(JSONObject eaglercraftOpts) {
		integratedServerOpts = eaglercraftOpts;
		defaultLocale = eaglercraftOpts.optString("lang", "en_US");
		serverToJoin = eaglercraftOpts.optString("joinServer", null);
		worldsDB = eaglercraftOpts.optString("worldsDB", "worlds");
		resourcePacksDB = eaglercraftOpts.optString("resourcePacksDB", "resourcePacks");
		checkShaderGLErrors = eaglercraftOpts.optBoolean("checkShaderGLErrors", false);
		if(EaglercraftVersion.forceDemoMode) {
			eaglercraftOpts.put("demoMode", true);
		}
		demoMode = EaglercraftVersion.forceDemoMode || eaglercraftOpts.optBoolean("demoMode", false);
		isAllowUpdateSvc = EaglercraftVersion.enableUpdateService && !demoMode && eaglercraftOpts.optBoolean("allowUpdateSvc", true);
		isAllowUpdateDL = EaglercraftVersion.enableUpdateService && !demoMode && eaglercraftOpts.optBoolean("allowUpdateDL", true);
		isEnableDownloadOfflineButton = eaglercraftOpts.optBoolean("enableDownloadOfflineButton", true);
		downloadOfflineButtonLink = eaglercraftOpts.optString("downloadOfflineButtonLink", null);
		useSpecialCursors = eaglercraftOpts.optBoolean("html5CursorSupport", false);
		logInvalidCerts = EaglercraftVersion.enableUpdateService && !demoMode && eaglercraftOpts.optBoolean("logInvalidCerts", false);
		enableSignatureBadge = eaglercraftOpts.optBoolean("enableSignatureBadge", false);
		allowVoiceClient = eaglercraftOpts.optBoolean("allowVoiceClient", true);
		allowFNAWSkins = eaglercraftOpts.optBoolean("allowFNAWSkins", true);
		localStorageNamespace = eaglercraftOpts.optString("localStorageNamespace", EaglercraftVersion.localStorageNamespace);
		enableMinceraft = eaglercraftOpts.optBoolean("enableMinceraft", true);
		enableServerCookies = !demoMode && eaglercraftOpts.optBoolean("enableServerCookies", true);
		allowServerRedirects = eaglercraftOpts.optBoolean("allowServerRedirects", true);
		crashOnUncaughtExceptions = eaglercraftOpts.optBoolean("crashOnUncaughtExceptions", false);
		openDebugConsoleOnLaunch = eaglercraftOpts.optBoolean("openDebugConsoleOnLaunch", false);
		fixDebugConsoleUnloadListener = eaglercraftOpts.optBoolean("fixDebugConsoleUnloadListener", false);
		forceWebViewSupport = eaglercraftOpts.optBoolean("forceWebViewSupport", false);
		enableWebViewCSP = eaglercraftOpts.optBoolean("enableWebViewCSP", true);
		autoFixLegacyStyleAttr = eaglercraftOpts.optBoolean("autoFixLegacyStyleAttr", true);
		showBootMenuOnLaunch = eaglercraftOpts.optBoolean("showBootMenuOnLaunch", false);
		bootMenuBlocksUnsignedClients = eaglercraftOpts.optBoolean("bootMenuBlocksUnsignedClients", false);
		allowBootMenu = eaglercraftOpts.optBoolean("allowBootMenu", !demoMode);
		forceProfanityFilter = eaglercraftOpts.optBoolean("forceProfanityFilter", false);
		forceWebGL1 = eaglercraftOpts.optBoolean("forceWebGL1", false);
		forceWebGL2 = eaglercraftOpts.optBoolean("forceWebGL2", false);
		allowExperimentalWebGL1 = eaglercraftOpts.optBoolean("allowExperimentalWebGL1", true);
		useWebGLExt = eaglercraftOpts.optBoolean("useWebGLExt", true);
		useDelayOnSwap = eaglercraftOpts.optBoolean("useDelayOnSwap", false);
		useJOrbisAudioDecoder = eaglercraftOpts.optBoolean("useJOrbisAudioDecoder", false);
		useXHRFetch = eaglercraftOpts.optBoolean("useXHRFetch", false);
		useVisualViewport = eaglercraftOpts.optBoolean("useVisualViewport", true);
		deobfStackTraces = eaglercraftOpts.optBoolean("deobfStackTraces", true);
		disableBlobURLs = eaglercraftOpts.optBoolean("disableBlobURLs", false);
		eaglerNoDelay = eaglercraftOpts.optBoolean("eaglerNoDelay", false);
		ramdiskMode = eaglercraftOpts.optBoolean("ramdiskMode", false);
		singleThreadMode = eaglercraftOpts.optBoolean("singleThreadMode", false);
		enableEPKVersionCheck = eaglercraftOpts.optBoolean("enableEPKVersionCheck", true);
		defaultServers.clear();
		JSONArray serversArray = eaglercraftOpts.optJSONArray("servers");
		if(serversArray != null) {
			for(int i = 0, l = serversArray.length(); i < l; ++i) {
				JSONObject serverEntry = serversArray.getJSONObject(i);
				boolean hideAddr = serverEntry.optBoolean("hideAddr", false);
				String serverAddr = serverEntry.optString("addr", null);
				if(serverAddr != null) {
					String serverName = serverEntry.optString("name", "Default Server #" + i);
					defaultServers.add(new DefaultServer(serverName, serverAddr, hideAddr));
				}
			}
		}

		relays.clear();
		JSONArray relaysArray = eaglercraftOpts.optJSONArray("relays");
		if(relaysArray != null) {
			boolean gotAPrimary = false;
			for (int i = 0, l = relaysArray.length(); i < l; ++i) {
				JSONObject relay = relaysArray.getJSONObject(i);
				boolean p = relay.optBoolean("primary");
				if(p) {
					if(gotAPrimary) {
						p = false;
					}else {
						gotAPrimary = true;
					}
				}
				relays.add(new RelayEntry(relay.getString("addr"), relay.getString("comment"), p));
			}
		}

		boolean officialUpdates = !demoMode && EaglercraftVersion.updateBundlePackageName.equals("net.lax1dude.eaglercraft.v1_8.client");
		if (relays.size() <= 0) {
			int choice = ThreadLocalRandom.current().nextInt(3);
			relays.add(new RelayEntry("wss://relay.deev.is/", "lax1dude relay #1", choice == 0));
			relays.add(new RelayEntry("wss://relay.lax1dude.net/", "lax1dude relay #2", choice == 1));
			relays.add(new RelayEntry("wss://relay.shhnowisnottheti.me/", "ayunami relay #1", choice == 2));
			checkRelaysForUpdates = !demoMode && eaglercraftOpts.optBoolean("checkRelaysForUpdates", officialUpdates);
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
			checkRelaysForUpdates = !demoMode && eaglercraftOpts.optBoolean("checkRelaysForUpdates", officialUpdates);
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
		return integratedServerOpts;
	}

	@Override
	public List<RelayEntry> getRelays() {
		return relays;
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
		return isAllowUpdateSvc;
	}

	@Override
	public boolean allowUpdateDL() {
		return isAllowUpdateDL;
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
		return checkRelaysForUpdates;
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

	public boolean isUseDelayOnSwapTeaVM() {
		return useDelayOnSwap;
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

	public boolean isEnableEPKVersionCheckTeaVM() {
		return enableEPKVersionCheck;
	}

	@Override
	public boolean isShowBootMenuOnLaunch() {
		return showBootMenuOnLaunch;
	}

	@Override
	public boolean isBootMenuBlocksUnsignedClients() {
		return bootMenuBlocksUnsignedClients;
	}

	@Override
	public boolean isAllowBootMenu() {
		return allowBootMenu;
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
	public IClientConfigAdapterHooks getHooks() {
		return hooks;
	}

	public JSONObject toJSONObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("lang", defaultLocale);
		jsonObject.put("joinServer", serverToJoin);
		jsonObject.put("worldsDB", worldsDB);
		jsonObject.put("resourcePacksDB", resourcePacksDB);
		jsonObject.put("checkShaderGLErrors", checkShaderGLErrors);
		jsonObject.put("demoMode", demoMode);
		jsonObject.put("allowUpdateSvc", isAllowUpdateSvc);
		jsonObject.put("allowUpdateDL", isAllowUpdateDL);
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
		jsonObject.put("showBootMenuOnLaunch", showBootMenuOnLaunch);
		jsonObject.put("bootMenuBlocksUnsignedClients", bootMenuBlocksUnsignedClients);
		jsonObject.put("allowBootMenu", allowBootMenu);
		jsonObject.put("forceProfanityFilter", forceProfanityFilter);
		jsonObject.put("forceWebGL1", forceWebGL1);
		jsonObject.put("forceWebGL2", forceWebGL2);
		jsonObject.put("allowExperimentalWebGL1", allowExperimentalWebGL1);
		jsonObject.put("useWebGLExt", useWebGLExt);
		jsonObject.put("useDelayOnSwap", useDelayOnSwap);
		jsonObject.put("useJOrbisAudioDecoder", useJOrbisAudioDecoder);
		jsonObject.put("useXHRFetch", useXHRFetch);
		jsonObject.put("useVisualViewport", useVisualViewport);
		jsonObject.put("deobfStackTraces", deobfStackTraces);
		jsonObject.put("disableBlobURLs", disableBlobURLs);
		jsonObject.put("eaglerNoDelay", eaglerNoDelay);
		jsonObject.put("ramdiskMode", ramdiskMode);
		jsonObject.put("singleThreadMode", singleThreadMode);
		jsonObject.put("enableEPKVersionCheck", enableEPKVersionCheck);
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
