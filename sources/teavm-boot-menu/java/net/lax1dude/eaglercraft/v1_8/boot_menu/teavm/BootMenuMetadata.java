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

package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.cache.EaglerLoadingCache;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

public class BootMenuMetadata {

	protected static final Logger logger = LogManager.getLogger("BootMenuMetadata");

	public static class LaunchTemplate {

		public final EnumClientLaunchType type;
		public final String joinServer;
		public final String launchOptsVar;
		public final String launchOptsAssetsURIVar;
		public final String launchOptsContainerVar;
		public final String mainFunction;
		public final String launchOpts;
		public final boolean clearCookiedBeforeLaunch;

		protected LaunchTemplate(EnumClientLaunchType type, String joinServer, String launchOptsVar,
				String launchOptsAssetsURIVar, String launchOptsContainerVar, String mainFunction, String launchOpts,
				boolean clearCookiedBeforeLaunch) {
			this.type = type;
			this.joinServer = joinServer;
			this.launchOptsVar = launchOptsVar;
			this.launchOptsAssetsURIVar = launchOptsAssetsURIVar;
			this.launchOptsContainerVar = launchOptsContainerVar;
			this.mainFunction = mainFunction;
			this.launchOpts = launchOpts;
			this.clearCookiedBeforeLaunch = clearCookiedBeforeLaunch;
		}

		protected LaunchTemplate(JSONObject jsonObject) {
			type = EnumClientLaunchType.valueOf(jsonObject.getString("client_launch_type"));
			joinServer = jsonObject.optString("join_server");
			launchOptsVar = jsonObject.optString("client_launch_opts_var");
			launchOptsAssetsURIVar = jsonObject.optString("client_launch_opts_assetsURI_var");
			launchOptsContainerVar = jsonObject.optString("client_launch_opts_container_var");
			mainFunction = jsonObject.optString("client_launch_main_func");
			clearCookiedBeforeLaunch = jsonObject.optBoolean("clear_cookies_before_launch");
			launchOpts = null;
		}

		protected LaunchTemplate mutateOpts(String newOpts) {
			if(newOpts == launchOpts) {
				return this;
			}
			return new LaunchTemplate(type, joinServer, launchOptsVar, launchOptsAssetsURIVar, launchOptsContainerVar,
					mainFunction, newOpts, clearCookiedBeforeLaunch);
		}

		public LaunchConfigEntry createLaunchConfig(EaglercraftUUID uuid, EaglercraftUUID clientDataUUID, String displayName) {
			return new LaunchConfigEntry(uuid, clientDataUUID, displayName, type, joinServer, launchOptsVar,
					launchOptsAssetsURIVar, launchOptsContainerVar, mainFunction, launchOpts, clearCookiedBeforeLaunch);
		}

		public void configureLaunchConfig(LaunchConfigEntry etr) {
			switch(type) {
			case STANDARD_OFFLINE_V1:
				etr.launchOpts = launchOpts;
				etr.launchOptsVar = launchOptsVar;
				etr.launchOptsAssetsURIVar = launchOptsAssetsURIVar;
				etr.launchOptsContainerVar = launchOptsContainerVar;
				etr.mainFunction = mainFunction;
				break;
			case EAGLERX_V1:
			case EAGLERX_SIGNED_V1:
			case EAGLER_1_5_V2:
			case PEYTON_V2:
				etr.launchOpts = launchOpts;
				break;
			case EAGLER_1_5_V1:
				etr.launchOpts = launchOpts;
				etr.joinServer = joinServer;
				break;
			case EAGLER_BETA_V1:
				etr.joinServer = joinServer;
				break;
			case PEYTON_V1:
				break;
			default: //?
				break;
			}
		}

	}

	public static class DefaultLaunchTemplate {

		public final String templateName;
		public final Set<EnumClientFormatType> supportedFormats;
		public final Set<EnumOfflineParseType> parseTypes;
		public final LaunchTemplate templateState;

		protected DefaultLaunchTemplate(String templateName, Set<EnumClientFormatType> supportedFormats,
				Set<EnumOfflineParseType> parseTypes, LaunchTemplate templateState) {
			this.templateName = templateName;
			this.supportedFormats = supportedFormats;
			this.parseTypes = parseTypes;
			this.templateState = templateState;
		}

		public LaunchConfigEntry createLaunchConfig(EaglercraftUUID uuid, EaglercraftUUID clientDataUUID) {
			return templateState.createLaunchConfig(uuid, clientDataUUID, templateName);
		}

		@Override
		public String toString() {
			return templateName;
		}
	}

	public final String basePath;

	public final Map<EnumClientLaunchType,LaunchTemplate> formatDefaultOptsMap = new HashMap<>();
	public final List<DefaultLaunchTemplate> defaultLaunchTemplates = new ArrayList<>();

	public BootMenuMetadata(String basePath) {
		this.basePath = basePath;
		this.loadAllData();
	}

	protected void loadAllData() {
		logger.info("Loading client templates and default settings...");
		formatDefaultOptsMap.clear();
		defaultLaunchTemplates.clear();
		EaglerLoadingCache<String, String> optsFileLoader = new EaglerLoadingCache<>(this::loadDataFileString);
		EaglerLoadingCache<String, LaunchTemplate> templateFileLoader = new EaglerLoadingCache<>(this::loadDataFileLaunchTemplate);
		byte[] data = BootMenuAssets.loadResourceBytes(basePath + "meta_opts_templates.json");
		if(data == null) {
			throw new RuntimeException("Missing metadata file: meta_opts_templates.json");
		}
		JSONObject jsonObject = new JSONObject(new String(data, StandardCharsets.UTF_8));
		JSONObject defaults = jsonObject.getJSONObject("defaults");
		for(String str : defaults.keySet()) {
			EnumClientLaunchType fmt = EnumClientLaunchType.valueOf(str);
			JSONObject etr = defaults.getJSONObject(str);
			LaunchTemplate launchTemplateBase = templateFileLoader.get(etr.getString("conf"));
			String optsFileName = etr.optString("opts", null);
			String eagOpts = optsFileName != null ? optsFileLoader.get(optsFileName) : null;
			formatDefaultOptsMap.put(fmt, launchTemplateBase.mutateOpts(eagOpts));
		}
		JSONArray templates = jsonObject.getJSONArray("templates");
		for(int i = 0, l = templates.length(); i < l; ++i) {
			JSONObject obj = templates.getJSONObject(i);
			LaunchTemplate launchTemplateBase = templateFileLoader.get(obj.getString("conf"));
			String optsFileName = obj.optString("opts", null);
			String eagOpts = optsFileName != null ? optsFileLoader.get(optsFileName) : null;
			JSONArray allowList = obj.getJSONArray("allow");
			Set<EnumClientFormatType> toAllow = new HashSet<>(allowList.length());
			for(int j = 0, m = allowList.length(); j < m; ++j) {
				toAllow.add(EnumClientFormatType.valueOf(allowList.getString(j)));
			}
			JSONArray parseTypesList = obj.getJSONArray("parseTypes");
			Set<EnumOfflineParseType> toParseTypes = new HashSet<>(parseTypesList.length());
			for(int j = 0, m = parseTypesList.length(); j < m; ++j) {
				toParseTypes.add(EnumOfflineParseType.valueOf(parseTypesList.getString(j)));
			}
			defaultLaunchTemplates.add(new DefaultLaunchTemplate(obj.getString("name"), toAllow, toParseTypes, launchTemplateBase.mutateOpts(eagOpts)));
		}
	}

	public List<DefaultLaunchTemplate> getTemplatesForClientData(EnumClientFormatType formatType) {
		List<DefaultLaunchTemplate> ret = new ArrayList<>();
		for(DefaultLaunchTemplate template : defaultLaunchTemplates) {
			if(template.supportedFormats.contains(formatType)) {
				ret.add(template);
			}
		}
		return ret;
	}

	public List<DefaultLaunchTemplate> getTemplatesForParseType(EnumOfflineParseType parseType) {
		List<DefaultLaunchTemplate> ret = new ArrayList<>();
		for(DefaultLaunchTemplate template : defaultLaunchTemplates) {
			if(template.parseTypes.contains(parseType)) {
				ret.add(template);
			}
		}
		return ret;
	}

	protected String loadDataFileString(String name) {
		byte[] data = BootMenuAssets.loadResourceBytes(basePath + name);
		if(data == null) {
			throw new RuntimeException("Missing metadata file: " + name);
		}
		return new String(data, StandardCharsets.UTF_8);
	}

	protected LaunchTemplate loadDataFileLaunchTemplate(String name) {
		return new LaunchTemplate(new JSONObject(loadDataFileString(name)));
	}

}