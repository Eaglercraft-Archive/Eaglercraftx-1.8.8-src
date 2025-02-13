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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.boot_menu.teavm.BootMenuEntryPoint.UnsignedClientEPKLoader;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

public class FatOfflineDownloadFactory {

	private static final Logger logger = LogManager.getLogger("FatOfflineDownloadFactory");

	public static void downloadOffline(List<BootableClientEntry> lst, IProgressMsgCallback cb) {
		Map<EaglercraftUUID, byte[]> loadedBlobs = new HashMap<>();
		JSONArray manifestClientDatas = new JSONArray();
		Set<EaglercraftUUID> manifestClientDatasSet = new HashSet<>();
		JSONArray manifestLaunchDatas = new JSONArray();
		Set<EaglercraftUUID> manifestLaunchDatasSet = new HashSet<>();
		for(BootableClientEntry etr : lst) {
			ClientDataEntry clientData = etr.bootAdapter.getClientDataEntry();
			LaunchConfigEntry launchConf = etr.bootAdapter.getLaunchConfigEntry();
			if(launchConf.uuid.equals(BootMenuConstants.UUID_CLIENT_LAUNCH_ORIGIN)) {
				logger.warn("Cannot export origin launch configuration to fat offline!");
				continue;
			}
			if(manifestLaunchDatasSet.add(launchConf.uuid)) {
				JSONObject obj = new JSONObject();
				launchConf.writeJSON(obj);
				manifestLaunchDatas.put(obj);
				if(!clientData.uuid.equals(BootMenuConstants.UUID_CLIENT_DATA_ORIGIN) && manifestClientDatasSet.add(clientData.uuid)) {
					obj = new JSONObject();
					clientData.writeJSON(obj);
					manifestClientDatas.put(obj);
					Map<EaglercraftUUID, Supplier<byte[]>> loaders = etr.bootAdapter.getBlobLoaders();
					for(EaglercraftUUID uuid : clientData.getReferencedBlobs()) {
						doUpdateMessage(cb, "Resolving data for \"" + launchConf.displayName + "\" (" + uuid + ")");
						if(!cacheLoad(loadedBlobs, loaders, uuid)) {
							throw new NullPointerException("Blob for configuration \"" + launchConf.displayName + "\" is missing: " + uuid);
						}
					}
				}
			}else {
				logger.warn("Skipping duplicate launch config uuid: {}", launchConf.uuid);
			}
		}
		JSONObject manifest = new JSONObject();
		manifest.put("clientData", manifestClientDatas);
		manifest.put("launchData", manifestLaunchDatas);
		String manifestStr = manifest.toString().replace(StringUtils.reverse(">elyts/<"), "<_style>");
		boolean isSigned = BootMenuEntryPoint.isSignedClient();
		String template;
		if(isSigned) {
			doUpdateMessage(cb, "Loading offline_template_eaglercraftX_1_8_fat_signed.html");
			template = OfflineDownloadFactory.loadTemplate("offline_template_eaglercraftX_1_8_fat_signed.html");
		}else {
			doUpdateMessage(cb, "Loading offline_template_eaglercraftX_1_8_fat_offline.html");
			template = OfflineDownloadFactory.loadTemplate("offline_template_eaglercraftX_1_8_fat_offline.html");
		}
		template = template.replace("${date}", (new SimpleDateFormat("MM/dd/yyyy")).format(new Date()));
		template = template.replace("${num_clients}", Integer.toString(manifestLaunchDatas.length() + 1));
		JSONObject optsDump = BootMenuEntryPoint.getOriginLaunchOptsJSON();
		optsDump.put("bootMenuBlocksUnsignedClients", false);
		RelayRandomizeHelper.makeOptsJSONHaveMacroHack(optsDump);
		String optsStr = optsDump.toString();
		JSONObject launchConfJSON = new JSONObject();
		(new LaunchConfigEntry(BootMenuConstants.UUID_CLIENT_LAUNCH_ORIGIN, BootMenuConstants.UUID_CLIENT_DATA_ORIGIN,
				BootMenuConstants.client_projectForkName + " " + BootMenuConstants.client_projectOriginRevision + " "
						+ BootMenuConstants.client_projectOriginVersion,
				isSigned ? EnumClientLaunchType.EAGLERX_SIGNED_V1 : EnumClientLaunchType.EAGLERX_V1, null, null, null,
				null, null, optsStr, false)).writeJSON(launchConfJSON);
		template = template.replace("${launch_conf_json}", launchConfJSON.toString());
		int relayIdCount = RelayRandomizeHelper.countRelayMacro(optsStr);
		if(relayIdCount > 0) {
			optsStr = RelayRandomizeHelper.replaceRelayMacroWithEqRelayId(optsStr);
		}
		template = template.replace("${relayId_max}", Integer.toString(relayIdCount));
		template = template.replace("${launch_opts}", optsStr);
		if(isSigned) {
			doUpdateMessage(cb, "Retrieving origin client signature and payload");
			template = template.replace("${client_signature}", Base64.encodeBase64String(BootMenuEntryPoint.getSignedClientSignature()));
			template = template.replace("${client_bundle}", Base64.encodeBase64String(BootMenuEntryPoint.getSignedClientBundle()));
		}else {
			doUpdateMessage(cb, "Retrieving origin client classes.js");
			byte[] classesJS = BootMenuEntryPoint.getUnsignedClientClassesJS();
			if(classesJS == null) {
				throw new NullPointerException("Could not load classes.js!");
			}
			template = template.replace(StringUtils.reverse("}sj_sessalc{$"), new String(OfflineDownloadFactory.removeUseStrict(classesJS), StandardCharsets.UTF_8));
			UnsignedClientEPKLoader epkLoader = BootMenuEntryPoint.getUnsignedClientAssetsEPK();
			String assetsEPKVal;
			int epkNum = epkLoader.list.size();
			if(epkNum > 1 || !StringUtils.isEmpty(epkLoader.list.get(0).extractTo)) {
				StringBuilder assetsEPKBuilder = new StringBuilder("[ ");
				for(int i = 0; i < epkNum; ++i) {
					EPKDataEntry epk = epkLoader.list.get(i);
					doUpdateMessage(cb, "Resolving assets.epk (" + epk.dataUUID + ", path: /" + epk.extractTo + ")");
					Supplier<byte[]> epkDataGetter = epkLoader.loaders.get(epk.dataUUID);
					byte[] epkData = null;
					if(epkDataGetter != null) {
						epkData = epkDataGetter.get();
					}
					if(epkData == null) {
						String msg = "Could not resolve assets.epk! (" + epk.dataUUID + ", path: /" + epk.extractTo + ")";
						logger.error(msg);
						throw new NullPointerException(msg);
					}
					if(i > 0) {
						assetsEPKBuilder.append(", ");
					}
					assetsEPKBuilder.append("{ url: \"data:application/octet-stream;base64,");
					assetsEPKBuilder.append(Base64.encodeBase64String(epkData));
					assetsEPKBuilder.append("\", path: \"");
					assetsEPKBuilder.append(OfflineDownloadFactory.stupidJSONEscape(epk.extractTo));
					assetsEPKBuilder.append("\" }");
				}
				assetsEPKBuilder.append(" ]");
				assetsEPKVal = assetsEPKBuilder.toString();
			}else {
				EPKDataEntry epk = epkLoader.list.get(0);
				doUpdateMessage(cb, "Resolving assets.epk (" + epk.dataUUID + ", path: /)");
				Supplier<byte[]> epkDataGetter = epkLoader.loaders.get(epk.dataUUID);
				byte[] epkData = null;
				if(epkDataGetter != null) {
					epkData = epkDataGetter.get();
				}
				if(epkData == null) {
					String msg = "Could not resolve assets.epk! (" + epk.dataUUID + ", path: /)";
					logger.error(msg);
					throw new NullPointerException(msg);
				}
				assetsEPKVal = "\"data:application/octet-stream;base64," + Base64.encodeBase64String(epkData) + "\"";
			}
			
			template = template.replace(StringUtils.reverse("}kpe_stessa{$"), assetsEPKVal);
		}
		doUpdateMessage(cb, "Embedding additional clients as base64");
		StringBuilder fatOfflineDataBuilder = new StringBuilder();
		fatOfflineDataBuilder.append(StringUtils.reverse(">\"1v_tsefinam_enilffOtaFrelgae_\"=di \"tfarcrelgae\"=epyt elyts<"));
		fatOfflineDataBuilder.append(manifestStr);
		fatOfflineDataBuilder.append(StringUtils.reverse("\n>elyts/<"));
		for(Entry<EaglercraftUUID, byte[]> etr : loadedBlobs.entrySet()) {
			fatOfflineDataBuilder.append(StringUtils.reverse("_enilffOtaFrelgae_\"=di \"tfarcrelgae\"=epyt elyts<") + etr.getKey().toString() + "\">");
			fatOfflineDataBuilder.append(Base64.encodeBase64String(etr.getValue()));
			fatOfflineDataBuilder.append(StringUtils.reverse("\n>elyts/<"));
		}
		template = template.replace(StringUtils.reverse("}atad_enilffo_taf{$"), fatOfflineDataBuilder.toString());
		doUpdateMessage(cb, "Downloading file...");
		EagRuntime.downloadFileWithName("EaglercraftX_1.8_Fat_Offline_Download.html", template.getBytes(StandardCharsets.UTF_8));
	}

	private static void doUpdateMessage(IProgressMsgCallback cb, String str) {
		logger.info(str);
		cb.updateMessage(str);
	}

	private static boolean cacheLoad(Map<EaglercraftUUID, byte[]> loadedBlobs,
			Map<EaglercraftUUID, Supplier<byte[]>> loaders, EaglercraftUUID uuid) {
		if(!loadedBlobs.containsKey(uuid)) {
			Supplier<byte[]> getter = loaders.get(uuid);
			if(getter != null) {
				byte[] dat = getter.get();
				if(dat != null) {
					loadedBlobs.put(uuid, dat);
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}else {
			return true;
		}
	}

}