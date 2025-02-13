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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.html.HtmlEscapers;

import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.cache.EaglerLoadingCache;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.update.CertificateInvalidException;
import net.lax1dude.eaglercraft.v1_8.update.UpdateCertificate;

public class OfflineDownloadFactory {

	private static final Logger logger = LogManager.getLogger("OfflineDownloadFactory");

	public static void downloadOffline(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			Map<EaglercraftUUID, Supplier<byte[]>> loaders, IProgressMsgCallback cb) {
		EaglerLoadingCache<EaglercraftUUID, byte[]> loadingCache = new EaglerLoadingCache<EaglercraftUUID, byte[]>((uuid) -> {
			Supplier<byte[]> sup = loaders.get(uuid);
			return sup != null ? sup.get() : null;
		});
		switch(launchConf.type) {
		case STANDARD_OFFLINE_V1:
			if(clientData.type == EnumClientFormatType.EAGLER_STANDARD_OFFLINE) {
				downloadClientStandardOffline(launchConf, clientData, loadingCache, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for STANDARD_OFFLINE_V1!");
			}
			break;
		case EAGLERX_V1:
			if(clientData.type == EnumClientFormatType.EAGLER_STANDARD_OFFLINE) {
				downloadClientEaglerX18(launchConf, clientData, loadingCache, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for EAGLERX_V1!");
			}
			break;
		case EAGLERX_SIGNED_V1:
			if(clientData.type == EnumClientFormatType.EAGLER_SIGNED_OFFLINE) {
				downloadClientEaglerX18Signed(launchConf, clientData, loadingCache, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for EAGLERX_SIGNED_V1!");
			}
			break;
		case EAGLER_1_5_V1:
			if(clientData.type == EnumClientFormatType.EAGLER_STANDARD_OFFLINE) {
				downloadClientEagler15Old(launchConf, clientData, loadingCache, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for EAGLER_1_5_V1!");
			}
			break;
		case EAGLER_1_5_V2:
			if(clientData.type == EnumClientFormatType.EAGLER_STANDARD_1_5_OFFLINE) {
				downloadClientEagler15New(launchConf, clientData, loadingCache, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for EAGLER_1_5_V2!");
			}
			break;
		case EAGLER_BETA_V1:
			if(clientData.type == EnumClientFormatType.EAGLER_STANDARD_OFFLINE) {
				downloadClientEaglerB13(launchConf, clientData, loadingCache, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for EAGLER_1_5_V2!");
			}
			break;
		case PEYTON_V1:
			if(clientData.type == EnumClientFormatType.EAGLER_STANDARD_OFFLINE) {
				downloadClientPeytonIndev(launchConf, clientData, loadingCache, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for PEYTON_V1!");
			}
			break;
		case PEYTON_V2:
			if(clientData.type == EnumClientFormatType.EAGLER_STANDARD_OFFLINE) {
				downloadClientPeytonAlphaBeta(launchConf, clientData, loadingCache, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for PEYTON_V2!");
			}
			break;
		}
	}

	static String loadTemplate(String name) {
		name = "/assets/eagler/boot_menu/" + name;
		String template = BootMenuAssets.loadResourceString(name);
		if(template == null) {
			throw new NullPointerException("Could not locate offline download template: " + name);
		}
		return template;
	}

	private static void doUpdateMessage(IProgressMsgCallback cb, String str) {
		logger.info(str);
		cb.updateMessage(str);
	}

	private static void downloadClientEaglerX18(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<EaglercraftUUID, byte[]> loadingCache, IProgressMsgCallback cb) {
		doUpdateMessage(cb, "Resolving classes.js (" + clientData.mainPayload + ")");
		byte[] classesJSBytes = loadingCache.get(clientData.mainPayload);
		if(classesJSBytes == null) {
			String msg = "Could not resolve classes.js! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		String assetsEPKVal;
		int epkNum = clientData.epkFiles.size();
		if(epkNum > 1 || !StringUtils.isEmpty(clientData.epkFiles.get(0).extractTo)) {
			StringBuilder assetsEPKBuilder = new StringBuilder("[ ");
			for(int i = 0; i < epkNum; ++i) {
				EPKDataEntry epk = clientData.epkFiles.get(i);
				doUpdateMessage(cb, "Resolving assets.epk (" + epk.dataUUID + ", path: /" + epk.extractTo + ")");
				byte[] epkData = loadingCache.get(epk.dataUUID);
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
				assetsEPKBuilder.append(stupidJSONEscape(epk.extractTo));
				assetsEPKBuilder.append("\" }");
			}
			assetsEPKBuilder.append(" ]");
			assetsEPKVal = assetsEPKBuilder.toString();
		}else {
			EPKDataEntry epk = clientData.epkFiles.get(0);
			doUpdateMessage(cb, "Resolving assets.epk (" + epk.dataUUID + ", path: /)");
			byte[] epkData = loadingCache.get(epk.dataUUID);
			if(epkData == null) {
				String msg = "Could not resolve assets.epk! (" + epk.dataUUID + ", path: /)";
				logger.error(msg);
				throw new NullPointerException(msg);
			}
			assetsEPKVal = "\"data:application/octet-stream;base64," + Base64.encodeBase64String(epkData) + "\"";
		}
		doUpdateMessage(cb, "Loading offline_template_eaglercraftX_1_8.html");
		String template = loadTemplate("offline_template_eaglercraftX_1_8.html");
		template = template.replace("${client_name}", HtmlEscapers.htmlEscaper().escape(launchConf.displayName));
		template = template.replace("${date}", (new SimpleDateFormat("MM/dd/yyyy")).format(new Date()));
		int relayIdCount = RelayRandomizeHelper.countRelayMacro(launchConf.launchOpts);
		template = template.replace("${relayId_max}", Integer.toString(relayIdCount));
		String launchOptsFormatted;
		try {
			launchOptsFormatted = (new JSONObject(launchConf.launchOpts)).toString(4);
		}catch(JSONException ex) {
			throw new IllegalArgumentException("Launch options JSON is invalid! " + ex.toString());
		}
		if(relayIdCount > 0) {
			launchOptsFormatted = RelayRandomizeHelper.replaceRelayMacroWithEqRelayId(launchOptsFormatted);
		}
		template = template.replace("${launch_opts}", launchOptsFormatted);
		JSONObject launchConfJSON = new JSONObject();
		launchConf.writeJSON(launchConfJSON);
		template = template.replace("${launch_conf_json}", launchConfJSON.toString());
		template = template.replace(StringUtils.reverse("}kpe_stessa{$"), assetsEPKVal);
		template = template.replace(StringUtils.reverse("}sj_sessalc{$"), new String(removeUseStrict(classesJSBytes), StandardCharsets.UTF_8));
		doUpdateMessage(cb, "Downloading file...");
		EagRuntime.downloadFileWithName(launchConf.displayName.replaceAll("[^a-zA-Z0-9\\-_\\.]", "_") + ".html", template.getBytes(StandardCharsets.UTF_8));
	}

	private static void downloadClientEaglerX18Signed(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<EaglercraftUUID, byte[]> loadingCache, IProgressMsgCallback cb) {
		doUpdateMessage(cb, "Resolving client signature (" + clientData.clientSignature + ")");
		byte[] clientSignature = loadingCache.get(clientData.clientSignature);
		if(clientSignature == null) {
			String msg = "Could not resolve client signature! (" + clientData.clientSignature + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		doUpdateMessage(cb, "Resolving client payload (" + clientData.mainPayload + ")");
		byte[] clientPayload = loadingCache.get(clientData.mainPayload);
		if(clientPayload == null) {
			String msg = "Could not resolve client payload! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		doUpdateMessage(cb, "Checking signature validity");
		UpdateCertificate cert = null;
		try {
			cert = UpdateCertificate.parseAndVerifyCertificate(clientSignature);
		}catch(CertificateInvalidException | IOException e) {
			logger.error("Signature invalid or not recognized!");
			logger.error(e);
			logger.info("The client will be exported anyway. RIP");
		}
		if(cert != null) {
			if(!cert.isBundleDataValid(clientPayload)) {
				logger.error("Client payload checksum does not match the certificate!");
				cert = null;
			}
		}else {
			logger.info("Signature is valid: {} - {}", cert.bundleDisplayName, cert.bundleDisplayVersion);
		}
		doUpdateMessage(cb, "Loading offline_template_eaglercraftX_1_8_signed.html");
		String template = loadTemplate("offline_template_eaglercraftX_1_8_signed.html");
		template = template.replace("${client_name}", HtmlEscapers.htmlEscaper().escape(cert != null ? (cert.bundleDisplayName + " " + cert.bundleDisplayVersion) : launchConf.displayName));
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		template = template.replace("${date}", df.format(new Date()));
		int relayIdCount = RelayRandomizeHelper.countRelayMacro(launchConf.launchOpts);
		template = template.replace("${relayId_max}", Integer.toString(relayIdCount));
		String launchOptsFormatted;
		try {
			launchOptsFormatted = (new JSONObject(launchConf.launchOpts)).toString(4);
		}catch(JSONException ex) {
			throw new IllegalArgumentException("Launch options JSON is invalid! " + ex.toString());
		}
		if(relayIdCount > 0) {
			launchOptsFormatted = RelayRandomizeHelper.replaceRelayMacroWithEqRelayId(launchOptsFormatted);
		}
		template = template.replace("${launch_opts}", launchOptsFormatted);
		JSONObject launchConfJSON = new JSONObject();
		launchConf.writeJSON(launchConfJSON);
		template = template.replace("${launch_conf_json}", launchConfJSON.toString());
		String fileName;
		if(cert != null) {
			String d8 = df.format(new Date(cert.sigTimestamp));
			template = template.replace("${signature_details}", cert.bundleDisplayName + " " + cert.bundleDisplayVersion
					+ " (" + cert.bundleVersionInteger + ") " + d8 + ", Author: " + cert.bundleAuthorName);
			template = template.replace("${client_name_or_origin_date}", "This file is from <span style=\"color:#AA0000;\">" + d8 + "</span>");
			fileName = cert.bundleDisplayName + "_" + cert.bundleDisplayVersion + "_Offline_Signed";
		}else {
			template = template.replace("${signature_details}", "INVALID! (Or just from a 3rd party client)");
			template = template.replace("${client_name_or_origin_date}", HtmlEscapers.htmlEscaper().escape(launchConf.displayName));
			fileName = launchConf.displayName;
		}
		template = template.replace("${client_signature}", Base64.encodeBase64String(clientSignature));
		template = template.replace("${client_bundle}", Base64.encodeBase64String(clientPayload));
		doUpdateMessage(cb, "Downloading file...");
		EagRuntime.downloadFileWithName(fileName.replaceAll("[^a-zA-Z0-9\\-_\\.]", "_") + ".html", template.getBytes(StandardCharsets.UTF_8));
	}

	private static void downloadClientEagler15New(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<EaglercraftUUID, byte[]> loadingCache, IProgressMsgCallback cb) {
		if(clientData.epkFiles.size() != 1) {
			throw new UnsupportedOperationException("Wrong number of EPK files: " + clientData.epkFiles.size());
		}
		doUpdateMessage(cb, "Resolving classes.js (" + clientData.mainPayload + ")");
		byte[] classesJSBytes = loadingCache.get(clientData.mainPayload);
		if(classesJSBytes == null) {
			String msg = "Could not resolve classes.js! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		doUpdateMessage(cb, "Resolving classes_server.js (" + clientData.integratedServer + ")");
		byte[] classesServerJSBytes = loadingCache.get(clientData.integratedServer);
		if(classesServerJSBytes == null) {
			String msg = "Could not resolve classes_server.js! (" + clientData.integratedServer + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		doUpdateMessage(cb, "Resolving assets.epk (" + clientData.epkFiles.get(0).dataUUID + ")");
		byte[] assetsEPKBytes = loadingCache.get(clientData.epkFiles.get(0).dataUUID);
		if(assetsEPKBytes == null) {
			String msg = "Could not resolve assets.epk! (" + clientData.epkFiles.get(0).dataUUID + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		doUpdateMessage(cb, "Loading offline_template_eaglercraft_1_5.html");
		String template = loadTemplate("offline_template_eaglercraft_1_5.html");
		template = template.replace("${client_name}", HtmlEscapers.htmlEscaper().escape(launchConf.displayName));
		template = template.replace("${date}", (new SimpleDateFormat("MM/dd/yyyy")).format(new Date()));
		int relayIdCount = RelayRandomizeHelper.countRelayMacro(launchConf.launchOpts);
		template = template.replace("${relayId_max}", Integer.toString(relayIdCount));
		String launchOptsFormatted;
		try {
			launchOptsFormatted = (new JSONObject(launchConf.launchOpts)).toString(4);
		}catch(JSONException ex) {
			throw new IllegalArgumentException("Launch options JSON is invalid! " + ex.toString());
		}
		if(relayIdCount > 0) {
			launchOptsFormatted = RelayRandomizeHelper.replaceRelayMacroWithEqRelayId(launchOptsFormatted);
		}
		template = template.replace("${launch_opts}", launchOptsFormatted);
		JSONObject launchConfJSON = new JSONObject();
		launchConf.writeJSON(launchConfJSON);
		template = template.replace("${launch_conf_json}", launchConfJSON.toString());
		template = template.replace(StringUtils.reverse("}kpe_stessa{$"), Base64.encodeBase64String(assetsEPKBytes));
		template = template.replace(StringUtils.reverse("}sj_sessalc{$"), new String(removeUseStrict(classesJSBytes), StandardCharsets.UTF_8));
		template = template.replace("${classes_server_js}", new String(removeUseStrict(classesServerJSBytes), StandardCharsets.UTF_8));
		doUpdateMessage(cb, "Downloading file...");
		EagRuntime.downloadFileWithName(launchConf.displayName.replaceAll("[^a-zA-Z0-9\\-_\\.]", "_") + ".html", template.getBytes(StandardCharsets.UTF_8));
	}

	private static void downloadClientEagler15Old(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<EaglercraftUUID, byte[]> loadingCache, IProgressMsgCallback cb) {
		if(clientData.epkFiles.size() != 1) {
			throw new UnsupportedOperationException("Wrong number of EPK files: " + clientData.epkFiles.size());
		}
		doUpdateMessage(cb, "Resolving classes.js (" + clientData.mainPayload + ")");
		byte[] classesJSBytes = loadingCache.get(clientData.mainPayload);
		if(classesJSBytes == null) {
			String msg = "Could not resolve classes.js! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		doUpdateMessage(cb, "Resolving assets.epk (" + clientData.epkFiles.get(0).dataUUID + ")");
		byte[] assetsEPKBytes = loadingCache.get(clientData.epkFiles.get(0).dataUUID);
		if(assetsEPKBytes == null) {
			String msg = "Could not resolve assets.epk! (" + clientData.epkFiles.get(0).dataUUID + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		doUpdateMessage(cb, "Loading offline_template_eaglercraft_1_5_legacy.html");
		String template = loadTemplate("offline_template_eaglercraft_1_5_legacy.html");
		template = template.replace("${client_name}", HtmlEscapers.htmlEscaper().escape(launchConf.displayName));
		template = template.replace("${date}", (new SimpleDateFormat("MM/dd/yyyy")).format(new Date()));
		JSONObject launchConfJSON = new JSONObject();
		launchConf.writeJSON(launchConfJSON);
		template = template.replace("${launch_conf_json}", launchConfJSON.toString());
		template = template.replace("${launch_opts}", ClientBootFactory.translateNBTOpts(launchConf.launchOpts));
		template = template.replace(StringUtils.reverse("}kpe_stessa{$"), Base64.encodeBase64String(assetsEPKBytes));
		template = template.replace(StringUtils.reverse("}sj_sessalc{$"), new String(removeUseStrict(classesJSBytes), StandardCharsets.UTF_8));
		doUpdateMessage(cb, "Downloading file...");
		EagRuntime.downloadFileWithName(launchConf.displayName.replaceAll("[^a-zA-Z0-9\\-_\\.]", "_") + ".html", template.getBytes(StandardCharsets.UTF_8));
	}

	private static void downloadClientEaglerB13(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<EaglercraftUUID, byte[]> loadingCache, IProgressMsgCallback cb) {
		if(clientData.epkFiles.size() != 1) {
			throw new UnsupportedOperationException("Wrong number of EPK files: " + clientData.epkFiles.size());
		}
		doUpdateMessage(cb, "Resolving classes.js (" + clientData.mainPayload + ")");
		byte[] classesJSBytes = loadingCache.get(clientData.mainPayload);
		if(classesJSBytes == null) {
			String msg = "Could not resolve classes.js! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		doUpdateMessage(cb, "Resolving assets.epk (" + clientData.epkFiles.get(0).dataUUID + ")");
		byte[] assetsEPKBytes = loadingCache.get(clientData.epkFiles.get(0).dataUUID);
		if(assetsEPKBytes == null) {
			String msg = "Could not resolve assets.epk! (" + clientData.epkFiles.get(0).dataUUID + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		doUpdateMessage(cb, "Loading offline_template_eaglercraft_b1_3.html");
		String template = loadTemplate("offline_template_eaglercraft_b1_3.html");
		template = template.replace("${client_name}", HtmlEscapers.htmlEscaper().escape(launchConf.displayName));
		template = template.replace("${date}", (new SimpleDateFormat("MM/dd/yyyy")).format(new Date()));
		JSONObject launchConfJSON = new JSONObject();
		launchConf.writeJSON(launchConfJSON);
		template = template.replace("${launch_conf_json}", launchConfJSON.toString());
		template = template.replace(StringUtils.reverse("}kpe_stessa{$"), Base64.encodeBase64String(assetsEPKBytes));
		template = template.replace(StringUtils.reverse("}sj_sessalc{$"), new String(removeUseStrict(classesJSBytes), StandardCharsets.UTF_8));
		doUpdateMessage(cb, "Downloading file...");
		EagRuntime.downloadFileWithName(launchConf.displayName.replaceAll("[^a-zA-Z0-9\\-_\\.]", "_") + ".html", template.getBytes(StandardCharsets.UTF_8));
	}

	private static void downloadClientPeytonAlphaBeta(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<EaglercraftUUID, byte[]> loadingCache, IProgressMsgCallback cb) {
		if(clientData.epkFiles.size() != 1) {
			throw new UnsupportedOperationException("Wrong number of EPK files: " + clientData.epkFiles.size());
		}
		doUpdateMessage(cb, "Resolving classes.js (" + clientData.mainPayload + ")");
		byte[] classesJSBytes = loadingCache.get(clientData.mainPayload);
		if(classesJSBytes == null) {
			String msg = "Could not resolve classes.js! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		doUpdateMessage(cb, "Resolving assets.epk (" + clientData.epkFiles.get(0).dataUUID + ")");
		byte[] assetsEPKBytes = loadingCache.get(clientData.epkFiles.get(0).dataUUID);
		if(assetsEPKBytes == null) {
			String msg = "Could not resolve assets.epk! (" + clientData.epkFiles.get(0).dataUUID + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		doUpdateMessage(cb, "Loading offline_template_peytonplayz585_a_b.html");
		String template = loadTemplate("offline_template_peytonplayz585_a_b.html");
		template = template.replace("${client_name}", HtmlEscapers.htmlEscaper().escape(launchConf.displayName));
		template = template.replace("${date}", (new SimpleDateFormat("MM/dd/yyyy")).format(new Date()));
		String launchOptsFormatted;
		try {
			launchOptsFormatted = (new JSONObject(launchConf.launchOpts)).toString(4);
		}catch(JSONException ex) {
			throw new IllegalArgumentException("Launch options JSON is invalid! " + ex.toString());
		}
		template = template.replace("${launch_opts}", launchOptsFormatted);
		JSONObject launchConfJSON = new JSONObject();
		launchConf.writeJSON(launchConfJSON);
		template = template.replace("${launch_conf_json}", launchConfJSON.toString());
		template = template.replace(StringUtils.reverse("}kpe_stessa{$"), Base64.encodeBase64String(assetsEPKBytes));
		template = template.replace(StringUtils.reverse("}sj_sessalc{$"), new String(removeUseStrict(classesJSBytes), StandardCharsets.UTF_8));
		doUpdateMessage(cb, "Downloading file...");
		EagRuntime.downloadFileWithName(launchConf.displayName.replaceAll("[^a-zA-Z0-9\\-_\\.]", "_") + ".html", template.getBytes(StandardCharsets.UTF_8));
	}

	private static void downloadClientPeytonIndev(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<EaglercraftUUID, byte[]> loadingCache, IProgressMsgCallback cb) {
		if(clientData.epkFiles.size() != 1) {
			throw new UnsupportedOperationException("Wrong number of EPK files: " + clientData.epkFiles.size());
		}
		doUpdateMessage(cb, "Resolving classes.js (" + clientData.mainPayload + ")");
		byte[] classesJSBytes = loadingCache.get(clientData.mainPayload);
		if(classesJSBytes == null) {
			String msg = "Could not resolve classes.js! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		doUpdateMessage(cb, "Resolving assets.epk (" + clientData.epkFiles.get(0).dataUUID + ")");
		byte[] assetsEPKBytes = loadingCache.get(clientData.epkFiles.get(0).dataUUID);
		if(assetsEPKBytes == null) {
			String msg = "Could not resolve assets.epk! (" + clientData.epkFiles.get(0).dataUUID + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		doUpdateMessage(cb, "Loading offline_template_peytonplayz585_indev.html");
		String template = loadTemplate("offline_template_peytonplayz585_indev.html");
		template = template.replace("${client_name}", HtmlEscapers.htmlEscaper().escape(launchConf.displayName));
		template = template.replace("${date}", (new SimpleDateFormat("MM/dd/yyyy")).format(new Date()));
		JSONObject launchConfJSON = new JSONObject();
		launchConf.writeJSON(launchConfJSON);
		template = template.replace("${launch_conf_json}", launchConfJSON.toString());
		template = template.replace(StringUtils.reverse("}kpe_stessa{$"), Base64.encodeBase64String(assetsEPKBytes));
		template = template.replace(StringUtils.reverse("}sj_sessalc{$"), new String(removeUseStrict(classesJSBytes), StandardCharsets.UTF_8));
		doUpdateMessage(cb, "Downloading file...");
		EagRuntime.downloadFileWithName(launchConf.displayName.replaceAll("[^a-zA-Z0-9\\-_\\.]", "_") + ".html", template.getBytes(StandardCharsets.UTF_8));
	}

	static String stupidJSONEscape(String str) {
		str = (new JSONArray().put(str)).toString();
		return str.substring(2, str.length() - 2);
	}

	private static void downloadClientStandardOffline(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<EaglercraftUUID, byte[]> loadingCache, IProgressMsgCallback cb) {
		doUpdateMessage(cb, "Resolving classes.js (" + clientData.mainPayload + ")");
		byte[] classesJSBytes = loadingCache.get(clientData.mainPayload);
		if(classesJSBytes == null) {
			String msg = "Could not resolve classes.js! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		JSONArray assetsEPKs = new JSONArray();
		for(EPKDataEntry epk : clientData.epkFiles) {
			doUpdateMessage(cb, "Resolving assets.epk (" + epk.dataUUID + ", path: /" + epk.extractTo + ")");
			byte[] epkData = loadingCache.get(epk.dataUUID);
			if(epkData == null) {
				String msg = "Could not resolve assets.epk! (" + epk.dataUUID + ", path: /" + epk.extractTo + ")";
				logger.error(msg);
				throw new NullPointerException(msg);
			}
			JSONObject obj = new JSONObject();
			obj.put("url", "data:application/octet-stream;base64," + Base64.encodeBase64String(epkData));
			obj.put("path", epk.extractTo);
			assetsEPKs.put(obj);
		}
		doUpdateMessage(cb, "Loading offline_template_eaglercraftX_1_8.html");
		String template = loadTemplate("offline_template_eaglercraftX_1_8.html");
		template = template.replace("${client_name}", HtmlEscapers.htmlEscaper().escape(launchConf.displayName));
		template = template.replace("${date}", (new SimpleDateFormat("MM/dd/yyyy")).format(new Date()));
		template = template.replace("${launch_opts_var_name}", stupidJSONEscape(launchConf.launchOptsVar));
		template = template.replace("${launch_opts_var_container_name}", stupidJSONEscape(launchConf.launchOptsContainerVar));
		template = template.replace("${launch_opts_var_assetsURI_name}", stupidJSONEscape(launchConf.launchOptsAssetsURIVar));
		template = template.replace("${main_function_name}", stupidJSONEscape(launchConf.mainFunction));
		int relayIdCount = RelayRandomizeHelper.countRelayMacro(launchConf.launchOpts);
		template = template.replace("${relayId_max}", Integer.toString(relayIdCount));
		String launchOptsFormatted;
		try {
			launchOptsFormatted = (new JSONObject(launchConf.launchOpts)).toString(4);
		}catch(JSONException ex) {
			throw new IllegalArgumentException("Launch options JSON is invalid! " + ex.toString());
		}
		if(relayIdCount > 0) {
			launchOptsFormatted = RelayRandomizeHelper.replaceRelayMacroWithEqRelayId(launchOptsFormatted);
		}
		template = template.replace("${launch_opts}", launchOptsFormatted);
		JSONObject launchConfJSON = new JSONObject();
		launchConf.writeJSON(launchConfJSON);
		template = template.replace("${launch_conf_json}", launchConfJSON.toString());
		template = template.replace(StringUtils.reverse("}kpe_stessa{$"), assetsEPKs.toString());
		template = template.replace(StringUtils.reverse("}sj_sessalc{$"), new String(removeUseStrict(classesJSBytes), StandardCharsets.UTF_8));
		doUpdateMessage(cb, "Downloading file...");
		EagRuntime.downloadFileWithName(launchConf.displayName.replaceAll("[^a-zA-Z0-9\\-_\\.]", "_") + ".html", template.getBytes(StandardCharsets.UTF_8));
	}

	public static byte[] removeClientScriptElement(byte[] input, boolean addUseStrict) {
		byte[] str = "\"use strict\";\r\nif(typeof window !== \"undefined\") window.eaglercraftXClientScriptElement = document.currentScript;".getBytes(StandardCharsets.UTF_8);
		if(input.length < str.length + 2) {
			return input;
		}
		if(Arrays.equals(str, 0, str.length, input, 0, str.length)) {
			return doUseStrict(input, str.length, addUseStrict);
		}
		if(Arrays.equals(str, 0, str.length, input, 1, str.length + 1)) {
			return doUseStrict(input, str.length + 1, addUseStrict);
		}
		if(Arrays.equals(str, 0, str.length, input, 2, str.length + 2)) {
			return doUseStrict(input, str.length + 2, addUseStrict);
		}
		str = "\"use strict\";\nif(typeof window !== \"undefined\") window.eaglercraftXClientScriptElement = document.currentScript;".getBytes(StandardCharsets.UTF_8);
		if(input.length < str.length) {
			return input;
		}
		if(Arrays.equals(str, 0, str.length, input, 0, str.length)) {
			return doUseStrict(input, str.length, addUseStrict);
		}
		if(Arrays.equals(str, 0, str.length, input, 1, str.length + 1)) {
			return doUseStrict(input, str.length + 1, addUseStrict);
		}
		if(Arrays.equals(str, 0, str.length, input, 2, str.length + 2)) {
			return doUseStrict(input, str.length + 2, addUseStrict);
		}
		if(addUseStrict) {
			str = new byte[] {(byte)34, (byte)117, (byte)115, (byte)101, (byte)32, (byte)115, (byte)116, (byte)114, (byte)105, (byte)99, (byte)116, (byte)34, (byte)59};
			if(Arrays.equals(str, 0, str.length, input, 0, str.length)) {
				return input;
			}
			if(Arrays.equals(str, 0, str.length, input, 1, str.length + 1)) {
				return input;
			}
			if(Arrays.equals(str, 0, str.length, input, 2, str.length + 2)) {
				return input;
			}
			return doUseStrict(input, 0, addUseStrict);
		}else {
			return input;
		}
	}

	private static byte[] doUseStrict(byte[] input, int removeLength, boolean addUseStrict) {
		if(addUseStrict) {
			byte[] useStrict = new byte[] {(byte)34, (byte)117, (byte)115, (byte)101, (byte)32, (byte)115, (byte)116, (byte)114, (byte)105, (byte)99, (byte)116, (byte)34, (byte)59, (byte)10};
			while(removeLength < input.length && (input[removeLength] == '\n' || input[removeLength] == '\r')) {
				++removeLength;
			}
			int endRemoveLength = input.length;
			while(endRemoveLength > removeLength + 1 && (input[endRemoveLength - 1] == '\n' || input[endRemoveLength - 1] == '\r')) {
				--endRemoveLength;
			}
			byte[] ret = new byte[endRemoveLength - removeLength + useStrict.length];
			System.arraycopy(useStrict, 0, ret, 0, useStrict.length);
			System.arraycopy(input, removeLength, ret, useStrict.length, endRemoveLength - removeLength);
			return ret;
		}else {
			int endRemoveLength = input.length;
			while(endRemoveLength > removeLength + 1 && (input[endRemoveLength - 1] == '\n' || input[endRemoveLength - 1] == '\r')) {
				--endRemoveLength;
			}
			if(removeLength > 0 || endRemoveLength != input.length) {
				return Arrays.copyOfRange(input, removeLength, endRemoveLength);
			}else {
				return input;
			}
		}
	}

	public static byte[] removeUseStrict(byte[] input) {
		byte[] str = new byte[] {(byte)34, (byte)117, (byte)115, (byte)101, (byte)32, (byte)115, (byte)116, (byte)114, (byte)105, (byte)99, (byte)116, (byte)34, (byte)59};
		if(input.length < str.length + 2) {
			return input;
		}
		int i = 0;
		if (Arrays.equals(str, 0, str.length, input, 0, str.length)
				|| Arrays.equals(str, 0, str.length, input, ++i, str.length + i)
				|| Arrays.equals(str, 0, str.length, input, ++i, str.length + i)) {
			int removeLength = str.length + i;
			while(removeLength < input.length && (input[removeLength] == '\n' || input[removeLength] == '\r')) {
				++removeLength;
			}
			int endRemoveLength = input.length;
			while(endRemoveLength > removeLength + 1 && (input[endRemoveLength - 1] == '\n' || input[endRemoveLength - 1] == '\r')) {
				--endRemoveLength;
			}
			return Arrays.copyOfRange(input, removeLength, endRemoveLength);
		}else {
			int endRemoveLength = input.length;
			while(endRemoveLength > 1 && (input[endRemoveLength - 1] == '\n' || input[endRemoveLength - 1] == '\r')) {
				--endRemoveLength;
			}
			if(endRemoveLength != input.length) {
				return Arrays.copyOfRange(input, 0, endRemoveLength);
			}else {
				return input;
			}
		}
	}

}