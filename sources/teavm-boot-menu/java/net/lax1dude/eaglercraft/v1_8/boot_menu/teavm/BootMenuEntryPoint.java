package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.json.JSONArray;
import org.json.JSONObject;
import org.teavm.jso.JSBody;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.cookie.ServerCookieDataStore;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformApplication;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformUpdateSvc;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.ClientMain;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.ClientMain.EPKFileEntry;
import net.lax1dude.eaglercraft.v1_8.sp.internal.ClientPlatformSingleplayer;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;

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
public class BootMenuEntryPoint {

	@JSBody(params = {}, script = "if((typeof window.__isEaglerX188BootMenuAlreadyShow === \"string\") && window.__isEaglerX188BootMenuAlreadyShow === \"yes\") return true; window.__isEaglerX188BootMenuAlreadyShow = \"yes\"; return false;")
	private static native boolean getHasAlreadyBooted();

	@JSBody(params = {}, script = "window.__isEaglerX188BootMenuAlreadyShow = \"yes\";")
	private static native void setHasAlreadyBooted();

	public static boolean checkShouldLaunchFlag(Window win) {
		int flag = BootMenuDataManager.getBootMenuFlags(win);
		if(flag == -1) {
			return IBootMenuConfigAdapter.instance.isShowBootMenuOnLaunch() && !getHasAlreadyBooted();
		}
		if((flag & 2) != 0) {
			BootMenuDataManager.setBootMenuFlags(win, flag & ~2);
			setHasAlreadyBooted();
			return true;
		}
		return ((flag & 1) != 0 || IBootMenuConfigAdapter.instance.isShowBootMenuOnLaunch()) && !getHasAlreadyBooted();
	}

	private static boolean hasInit = false;
	private static byte[] signatureData = null;
	private static byte[] bundleData = null;

	public static void launchMenu(Window parentWindow, HTMLElement parentElement) {
		signatureData = PlatformUpdateSvc.getClientSignatureData();
		bundleData = PlatformUpdateSvc.getClientBundleData();
		hasInit = true;
		BootMenuMain.launchMenu(parentWindow, parentElement);
	}

	public static void bootOriginClient() {
		bootOriginClient(null);
	}

	public static void bootOriginClient(Runnable doBeforeBoot) {
		(new Thread(() -> {
			if(doBeforeBoot != null) {
				doBeforeBoot.run();
			}
			ClientMain._main();
		}, "main")).start();
	}

	public static boolean isSignedClient() {
		if(!hasInit) {
			signatureData = PlatformUpdateSvc.getClientSignatureData();
			bundleData = PlatformUpdateSvc.getClientBundleData();
			hasInit = true;
		}
		return signatureData != null && bundleData != null;
	}

	public static byte[] getSignedClientSignature() {
		return signatureData;
	}

	public static byte[] getSignedClientBundle() {
		return bundleData;
	}

	public static byte[] getUnsignedClientClassesJS() {
		return OfflineDownloadFactory.removeClientScriptElement(ClientPlatformSingleplayer.getIntegratedServerSourceTeaVM(), true);
	}

	public static class UnsignedClientEPKLoader {

		public final List<EPKDataEntry> list;
		public final Map<EaglercraftUUID,Supplier<byte[]>> loaders;
		
		public UnsignedClientEPKLoader(List<EPKDataEntry> list, Map<EaglercraftUUID,Supplier<byte[]>> loaders) {
			this.list = list;
			this.loaders = loaders;
		}
		
		public byte[] loadEntry(EaglercraftUUID epkUUID) {
			Supplier<byte[]> sup = loaders.get(epkUUID);
			return sup != null ? sup.get() : null;
		}
		
	}

	public static JSONArray getUnsignedClientAssetsEPKRaw() {
		JSONArray ret = new JSONArray(ClientMain.configEPKFiles.length);
		for (int i = 0; i < ClientMain.configEPKFiles.length; ++i) {
			EPKFileEntry etr = ClientMain.configEPKFiles[i];
			JSONObject obj = new JSONObject();
			obj.put("url", etr.url);
			obj.put("path", etr.path);
			ret.put(obj);
		}
		return ret;
	}

	public static UnsignedClientEPKLoader getUnsignedClientAssetsEPK() {
		List<EPKDataEntry> list = new ArrayList<>(2);
		Map<EaglercraftUUID, Supplier<byte[]>> loaders = new HashMap<>(2);
		for (int i = 0; i < ClientMain.configEPKFiles.length; ++i) {
			EPKFileEntry etr = ClientMain.configEPKFiles[i];
			EaglercraftUUID uuidGen = EaglercraftUUID
					.nameUUIDFromBytes(("EPKURL:" + etr.url).getBytes(StandardCharsets.UTF_8));
			list.add(new EPKDataEntry(etr.path, uuidGen));
			loaders.put(uuidGen,
					() -> TeaVMUtils.wrapByteArrayBuffer(PlatformRuntime.downloadRemoteURI(etr.url, true)));
		}
		return new UnsignedClientEPKLoader(list, loaders);
	}

	public static String getOriginLaunchOpts() {
		return ((TeaVMClientConfigAdapter)PlatformRuntime.getClientConfigAdapter()).toJSONObject().put("bootMenuBlocksUnsignedClients", false).toString(4);
	}

	public static JSONObject getOriginLaunchOptsJSON() {
		return ((TeaVMClientConfigAdapter)PlatformRuntime.getClientConfigAdapter()).toJSONObject();
	}

	public static String getOriginContainer() {
		return ClientMain.configRootElementId;
	}

	public static void installSignedClientAtRuntime(String displayName, Window win, byte[] clientCert,
			byte[] clientPayload, boolean setDefault, boolean setTimeout) {
		SignedClientInstaller.installSignedClientAtRuntime(displayName, win, clientCert, clientPayload, setDefault,
				setTimeout);
	}

	public static void setDisplayBootMenuNextRefresh(Window win, boolean en) {
		int i = BootMenuDataManager.getBootMenuFlags(win);
		if(i == -1) i = 0;
		if(en) {
			i |= 2;
		}else {
			i &= ~2;
		}
		BootMenuDataManager.setBootMenuFlags(win, i);
	}

	public static void clearCookies() {
		PlatformApplication.setLocalStorage(ServerCookieDataStore.localStorageKey, null, false);
		ServerCookieDataStore.clearCookiesLow();
	}

}
