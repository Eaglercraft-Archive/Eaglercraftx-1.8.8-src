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

package net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.opts;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArrayReader;

public interface JSEaglercraftXOptsRoot extends JSObject {

	@JSBody(params = { "def" }, script = "return (typeof this.lang === \"string\") ? this.lang : def;")
	String getLang(String defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.joinServer === \"string\") ? this.joinServer : def;")
	String getJoinServer(String defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.localesURI === \"string\") ? this.localesURI : def;")
	String getLocalesURI(String defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.worldsDB === \"string\") ? this.worldsDB : def;")
	String getWorldsDB(String defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.resourcePacksDB === \"string\") ? this.resourcePacksDB : def;")
	String getResourcePacksDB(String defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.demoMode === \"boolean\") ? this.demoMode : def;")
	boolean getDemoMode(boolean defaultValue);

	@JSBody(script = "return (typeof this.servers === \"object\") ? this.servers : null;")
	JSArrayReader<JSEaglercraftXOptsServer> getServers();

	@JSBody(script = "return (typeof this.relays === \"object\") ? this.relays : null;")
	JSArrayReader<JSEaglercraftXOptsRelay> getRelays();

	@JSBody(params = { "def" }, script = "return (typeof this.checkGLErrors === \"boolean\") ? this.checkGLErrors : def;")
	boolean getCheckGLErrors(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.checkShaderGLErrors === \"boolean\") ? this.checkShaderGLErrors : def;")
	boolean getCheckShaderGLErrors(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.enableDownloadOfflineButton === \"boolean\") ? this.enableDownloadOfflineButton : def;")
	boolean getEnableDownloadOfflineButton(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.downloadOfflineButtonLink === \"string\") ? this.downloadOfflineButtonLink : def;")
	String getDownloadOfflineButtonLink(String defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.html5CursorSupport === \"boolean\") ? this.html5CursorSupport : def;")
	boolean getHtml5CursorSupport(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.allowUpdateSvc === \"boolean\") ? this.allowUpdateSvc : def;")
	boolean getAllowUpdateSvc(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.allowUpdateDL === \"boolean\") ? this.allowUpdateDL : def;")
	boolean getAllowUpdateDL(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.logInvalidCerts === \"boolean\") ? this.logInvalidCerts : def;")
	boolean getLogInvalidCerts(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.enableSignatureBadge === \"boolean\") ? this.enableSignatureBadge : def;")
	boolean getEnableSignatureBadge(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.checkRelaysForUpdates === \"boolean\") ? this.checkRelaysForUpdates : def;")
	boolean getCheckRelaysForUpdates(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.allowVoiceClient === \"boolean\") ? this.allowVoiceClient : def;")
	boolean getAllowVoiceClient(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.allowFNAWSkins === \"boolean\") ? this.allowFNAWSkins : def;")
	boolean getAllowFNAWSkins(boolean defaultValue);

	@JSBody(script = "return (typeof this.hooks === \"object\") ? this.hooks : null;")
	JSEaglercraftXOptsHooks getHooks();

	@JSBody(params = { "def" }, script = "return (typeof this.localStorageNamespace === \"string\") ? this.localStorageNamespace : def;")
	String getLocalStorageNamespace(String defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.enableMinceraft === \"boolean\") ? this.enableMinceraft : def;")
	boolean getEnableMinceraft(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.enableServerCookies === \"boolean\") ? this.enableServerCookies : def;")
	boolean getEnableServerCookies(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.allowServerRedirects === \"boolean\") ? this.allowServerRedirects : def;")
	boolean getAllowServerRedirects(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.crashOnUncaughtExceptions === \"boolean\") ? this.crashOnUncaughtExceptions : def;")
	boolean getCrashOnUncaughtExceptions(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.openDebugConsoleOnLaunch === \"boolean\") ? this.openDebugConsoleOnLaunch : def;")
	boolean getOpenDebugConsoleOnLaunch(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.fixDebugConsoleUnloadListener === \"boolean\") ? this.fixDebugConsoleUnloadListener : def;")
	boolean getFixDebugConsoleUnloadListener(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.forceWebViewSupport === \"boolean\") ? this.forceWebViewSupport : def;")
	boolean getForceWebViewSupport(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.enableWebViewCSP === \"boolean\") ? this.enableWebViewCSP : def;")
	boolean getEnableWebViewCSP(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.autoFixLegacyStyleAttr === \"boolean\") ? this.autoFixLegacyStyleAttr : def;")
	boolean getAutoFixLegacyStyleAttr(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.showBootMenuOnLaunch === \"boolean\") ? this.showBootMenuOnLaunch : def;")
	boolean getShowBootMenuOnLaunch(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.bootMenuBlocksUnsignedClients === \"boolean\") ? this.bootMenuBlocksUnsignedClients : def;")
	boolean getBootMenuBlocksUnsignedClients(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.allowBootMenu === \"boolean\") ? this.allowBootMenu : def;")
	boolean getAllowBootMenu(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.forceProfanityFilter === \"boolean\") ? this.forceProfanityFilter : def;")
	boolean getForceProfanityFilter(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.forceWebGL1 === \"boolean\") ? this.forceWebGL1 : def;")
	boolean getForceWebGL1(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.forceWebGL2 === \"boolean\") ? this.forceWebGL2 : def;")
	boolean getForceWebGL2(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.allowExperimentalWebGL1 === \"boolean\") ? this.allowExperimentalWebGL1 : def;")
	boolean getAllowExperimentalWebGL1(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.useWebGLExt === \"boolean\") ? this.useWebGLExt : def;")
	boolean getUseWebGLExt(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.useDelayOnSwap === \"boolean\") ? this.useDelayOnSwap : def;")
	boolean getUseDelayOnSwap(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.useJOrbisAudioDecoder === \"boolean\") ? this.useJOrbisAudioDecoder : def;")
	boolean getUseJOrbisAudioDecoder(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.useXHRFetch === \"boolean\") ? this.useXHRFetch : def;")
	boolean getUseXHRFetch(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.useVisualViewport === \"boolean\") ? this.useVisualViewport : def;")
	boolean getUseVisualViewport(boolean defaultValue);

	@JSBody(params = { "def" }, script = "return (typeof this.deobfStackTraces === \"boolean\") ? this.deobfStackTraces : def;")
	boolean getDeobfStackTraces(boolean deobfStackTraces);

	@JSBody(params = { "def" }, script = "return (typeof this.disableBlobURLs === \"boolean\") ? this.disableBlobURLs : def;")
	boolean getDisableBlobURLs(boolean deobfStackTraces);

	@JSBody(params = { "def" }, script = "return (typeof this.eaglerNoDelay === \"boolean\") ? this.eaglerNoDelay : def;")
	boolean getEaglerNoDelay(boolean deobfStackTraces);

	@JSBody(params = { "def" }, script = "return (typeof this.ramdiskMode === \"boolean\") ? this.ramdiskMode : def;")
	boolean getRamdiskMode(boolean deobfStackTraces);

	@JSBody(params = { "def" }, script = "return (typeof this.singleThreadMode === \"boolean\") ? this.singleThreadMode : def;")
	boolean getSingleThreadMode(boolean deobfStackTraces);

	@JSBody(params = { "def" }, script = "return (typeof this.enforceVSync === \"boolean\") ? this.enforceVSync : def;")
	boolean getEnforceVSync(boolean enforceVSync);

	@JSBody(params = { "def" }, script = "return (typeof this.enableEPKVersionCheck === \"boolean\") ? this.enableEPKVersionCheck : def;")
	boolean getEnableEPKVersionCheck(boolean deobfStackTraces);

}