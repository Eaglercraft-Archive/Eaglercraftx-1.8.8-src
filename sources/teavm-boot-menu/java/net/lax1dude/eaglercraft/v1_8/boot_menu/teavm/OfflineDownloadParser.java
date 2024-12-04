package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
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
public class OfflineDownloadParser {

	private static final Logger logger = LogManager.getLogger("OfflineDownloadParser");

	public static EnumOfflineParseType detectOfflineType(String offlineDownloadData) {
		int hintIndex = offlineDownloadData.indexOf(StringUtils.reverse(">\"tniHesraPenilffOtfarcrelgae\"=epyt elyts<"));
		if(hintIndex != -1) {
			hintIndex += 42;
			int closeTagIndex = offlineDownloadData.indexOf(StringUtils.reverse(">elyts/<"), hintIndex);
			if(closeTagIndex != -1) {
				try {
					JSONObject parseHint = new JSONObject(offlineDownloadData.substring(hintIndex, closeTagIndex));
					return EnumOfflineParseType.valueOf(parseHint.getString("type"));
				}catch(JSONException | IllegalArgumentException ex) {
					logger.error("This offline download has a parse hint section, but the JSON is corrupt!");
					logger.error(ex);
				}
			}
		}
		if(offlineDownloadData.startsWith("EAGPKG$$")) {
			logger.info("Detected format: EAGLERCRAFT_EPK_FILE");
			return EnumOfflineParseType.EAGLERCRAFT_EPK_FILE;
		}
		if(foundWithin(offlineDownloadData.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad>\"erutangiStneilCXtfarcrelgae\"=di \"tfarcrelgae\"=epyt elyts<")), 32, 2048)) {
			logger.info("Detected format: EAGLERCRAFTX_1_8_SIGNED (post u24)");
			return EnumOfflineParseType.EAGLERCRAFTX_1_8_SIGNED;
		}
		if(foundWithin(offlineDownloadData.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad\" = erutangiStneilCXtfarcrelgae.wodniw")), 32, 2048)) {
			logger.info("Detected format: EAGLERCRAFTX_1_8_SIGNED (pre u24)");
			return EnumOfflineParseType.EAGLERCRAFTX_1_8_SIGNED;
		}
		if(foundWithin(offlineDownloadData.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad\" = IRUstessa.stpOXtfarcrelgae.wodniw")), 8388608, offlineDownloadData.length() - 1048576)) {
			logger.info("Detected format: EAGLERCRAFTX_1_8_OFFLINE (en_US)");
			return EnumOfflineParseType.EAGLERCRAFTX_1_8_OFFLINE;
		}
		if(foundWithin(offlineDownloadData.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad\" :lru { [ = IRUstessa.stpOXtfarcrelgae.wodniw")), 8388608, offlineDownloadData.length() - 1048576)) {
			logger.info("Detected format: EAGLERCRAFTX_1_8_OFFLINE (International)");
			return EnumOfflineParseType.EAGLERCRAFTX_1_8_OFFLINE;
		}
		if(foundWithin(offlineDownloadData.indexOf(StringUtils.reverse("]OFNI[ :partstooBredaoL")), 0, 16384)) {
			if(offlineDownloadData.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad\" = IRUstessa.stpOXtfarcrelgae.wodniw")) != -1) {
				logger.info("Detected format: EAGLERCRAFTX_1_8_OFFLINE (WASM-GC)");
				return EnumOfflineParseType.EAGLERCRAFTX_1_8_OFFLINE;
			}
		}
		if(foundWithin(offlineDownloadData.indexOf(StringUtils.reverse("{ = stpOtfarcrelgae.wodniw")), 32, 2048) && foundWithin(offlineDownloadData.indexOf(StringUtils.reverse(">\"rekrow_ps\"=di \"rekrowrelgae/txet\"=epyt tpircs<")), 4194304, offlineDownloadData.length() - 1048576)) {
			logger.info("Detected format: EAGLERCRAFTX_1_5_NEW_OFFLINE");
			return EnumOfflineParseType.EAGLERCRAFT_1_5_NEW_OFFLINE;
		}
		if(foundWithin(offlineDownloadData.indexOf(StringUtils.reverse(",\"emarf_emag\"\t\t\n[ = stpOtfarcenim.wodniw")), 32, 2048) || foundWithin(offlineDownloadData.indexOf(StringUtils.reverse(",\"emarf_emag\"        \n[ = stpOtfarcenim.wodniw")), 32, 2048)) {
			if(foundWithin(offlineDownloadData.indexOf("\"eaglercraft.minecraft = \\\"b1.3\\\"\\n\""), 524288, offlineDownloadData.length() - 1048576)) {
				logger.info("Detected format: EAGLERCRAFT_BETA_B1_3_OFFLINE");
				return EnumOfflineParseType.EAGLERCRAFT_BETA_B1_3_OFFLINE;
			}else if(foundWithin(offlineDownloadData.indexOf("\"eaglercraft.minecraft = \\\"1.5.2\\\"\\n\""), 2097152, offlineDownloadData.length() - 2097152)) {
				logger.info("Detected format: EAGLERCRAFTX_1_5_OLD_OFFLINE");
				return EnumOfflineParseType.EAGLERCRAFT_1_5_OLD_OFFLINE;
			}
		}
		if(foundWithin(offlineDownloadData.indexOf(StringUtils.reverse("{ = gifnoc.wodniw")), 32, 512)) {
			logger.info("Detected format: PEYTONPLAYZ585_ALPHA_BETA");
			return EnumOfflineParseType.PEYTONPLAYZ585_ALPHA_BETA;
		}
		if(foundWithin(offlineDownloadData.indexOf(StringUtils.reverse(",\"emarf_emag\"\t\t\n[ = gifnoCcissalc.wodniw")), 32, 512) || foundWithin(offlineDownloadData.indexOf(StringUtils.reverse(",\"emarf_emag\"    \n[ = gifnoCcissalc.wodniw")), 32, 512)) {
			logger.info("Detected format: PEYTONPLAYZ585_INDEV");
			return EnumOfflineParseType.PEYTONPLAYZ585_INDEV;
		}
		if(foundWithin(offlineDownloadData.indexOf(StringUtils.reverse(">eltit/<evirD elgooG - evirD yM>eltit<\n>/ \"8-FTU\"=tesrahc atem<\n>daeh<")), 32, 2048)) {
			logger.info("Detected format: EAGLERCRAFTX_1_5_NEW_OFFLINE (maybe)");
			return EnumOfflineParseType.EAGLERCRAFT_1_5_NEW_OFFLINE;
		}
		if(foundWithin(offlineDownloadData.indexOf(StringUtils.reverse(">eltit/<lacol tfarcrelgae>eltit<\n>daeh<")), 32, 2048)) {
			if(foundWithin(offlineDownloadData.indexOf("\"eaglercraft.minecraft = \\\"b1.3\\\"\\n\""), 524288, offlineDownloadData.length() - 1048576)) {
				logger.info("Detected format: EAGLERCRAFT_BETA_B1_3_OFFLINE");
				return EnumOfflineParseType.EAGLERCRAFT_BETA_B1_3_OFFLINE;
			}else if(foundWithin(offlineDownloadData.indexOf("\"eaglercraft.minecraft = \\\"1.5.2\\\"\\n\""), 2097152, offlineDownloadData.length() - 2097152)) {
				logger.info("Detected format: EAGLERCRAFTX_1_5_OLD_OFFLINE");
				return EnumOfflineParseType.EAGLERCRAFT_1_5_OLD_OFFLINE;
			}
		}
		if(foundWithin(offlineDownloadData.indexOf(StringUtils.reverse(">eltit/<8.1 XtfarcrelgaE>eltit<\n>/ \"8.8.1 ,8.1 ,tfarcenim ,xtfarcrelgae ,tfarcrelgae\"=tnetnoc \"sdrowyek\"=eman atem<\n>/ \"enilffO 8.1 XtfarcrelgaE\"=tnetnoc \"noitpircsed\"=eman atem<")), 32, 2048)) {
			logger.info("Detected format: EAGLERCRAFTX_1_8_OFFLINE (maybe)");
			return EnumOfflineParseType.EAGLERCRAFTX_1_8_OFFLINE;
		}
		if(foundWithin(offlineDownloadData.indexOf(StringUtils.reverse(">eltit/<3.7.1 ateB>eltit<")), 8, 512)) {
			logger.info("Detected format: PEYTONPLAYZ585_ALPHA_BETA (maybe)");
			return EnumOfflineParseType.PEYTONPLAYZ585_ALPHA_BETA;
		}
		if(foundWithin(offlineDownloadData.indexOf(StringUtils.reverse(">eltit/<6.2.1v ahplA>eltit<")), 8, 512)) {
			logger.info("Detected format: PEYTONPLAYZ585_ALPHA_BETA (maybe)");
			return EnumOfflineParseType.PEYTONPLAYZ585_ALPHA_BETA;
		}
		if(foundWithin(offlineDownloadData.indexOf(StringUtils.reverse(">eltit/<vednI tfarceniM>eltit<")), 8, 512)) {
			logger.info("Detected format: PEYTONPLAYZ585_INDEV (maybe)");
			return EnumOfflineParseType.PEYTONPLAYZ585_INDEV;
		}
		return null;
	}

	private static boolean foundWithin(int idx, int min, int max) {
		return idx >= min && idx < max;
	}

	public static class ParsedOfflineAdapter {

		public final EnumOfflineParseType parseType;
		public final LaunchConfigEntry launchData;
		public final ClientDataEntry clientData;
		public final Map<EaglercraftUUID, byte[]> blobs;

		public ParsedOfflineAdapter(EnumOfflineParseType parseType, ClientDataEntry clientData,
				Map<EaglercraftUUID, byte[]> blobs) {
			this.parseType = parseType;
			this.launchData = null;
			this.clientData = clientData;
			this.blobs = blobs;
		}

		public ParsedOfflineAdapter(LaunchConfigEntry launchData, ClientDataEntry clientData,
				Map<EaglercraftUUID, byte[]> blobs) {
			this.parseType = EnumOfflineParseType.inferFromClientFormat(launchData.type);
			this.launchData = launchData;
			this.clientData = clientData;
			this.blobs = blobs;
		}

	}

	public static List<ParsedOfflineAdapter> parseOffline(EnumOfflineParseType parseType, String offlineDownloadData) {
		if(parseType == null) {
			parseType = detectOfflineType(offlineDownloadData);
			if(parseType == null) {
				throw new IllegalArgumentException("Could not automatically detect offline download type!");
			}
		}
		switch(parseType) {
		case EAGLERCRAFTX_1_8_OFFLINE:
			return parseOfflineEaglerX18(offlineDownloadData);
		case EAGLERCRAFTX_1_8_SIGNED:
			return parseOfflineEaglerX18Signed(offlineDownloadData);
		case EAGLERCRAFTX_1_8_FAT_OFFLINE:
			return parseOfflineEaglerX18Fat(offlineDownloadData);
		case EAGLERCRAFTX_1_8_FAT_SIGNED:
			return parseOfflineEaglerX18FatSigned(offlineDownloadData);
		case EAGLERCRAFT_1_5_NEW_OFFLINE:
			return parseOfflineEagler15New(offlineDownloadData);
		case EAGLERCRAFT_1_5_OLD_OFFLINE:
			return parseOfflineEagler15Old(offlineDownloadData);
		case EAGLERCRAFT_BETA_B1_3_OFFLINE:
			return parseOfflineEaglerB13(offlineDownloadData);
		case PEYTONPLAYZ585_ALPHA_BETA:
			return parseOfflinePeytonAlphaBeta(offlineDownloadData);
		case PEYTONPLAYZ585_INDEV:
			return parseOfflinePeytonIndev(offlineDownloadData);
		case EXPORTED_STANDARD_OFFLINE:
			return parseStandardOffline(offlineDownloadData);
		default:
			throw new UnsupportedOperationException();
		}
	}

	public static class TagIsolator {

		protected final String openTag;
		protected final String closeTag;
		protected final String str;
		protected int currentIndex = 0;
		
		public TagIsolator(String openTag, String closeTag, String str) {
			this.openTag = openTag;
			this.closeTag = closeTag;
			this.str = str;
		}
		
		public String nextScript() {
			if(currentIndex != -1) {
				currentIndex = str.indexOf(openTag, currentIndex);
				if(currentIndex == -1) {
					return null;
				}
				currentIndex += openTag.length();
				int i2 = str.indexOf(closeTag, currentIndex);
				if(i2 == -1) {
					currentIndex = -1;
					return null;
				}
				String ret = str.substring(currentIndex, i2);
				currentIndex = i2 + closeTag.length();
				return ret;
			}else {
				return null;
			}
		}
		
		public List<String> getAllTags() {
			List<String> ret = new ArrayList<>();
			String str;
			while((str = nextScript()) != null) {
				ret.add(str);
			}
			return ret;
		}
		
	}

	private static LaunchConfigEntry tryReadParseHint(EnumOfflineParseType expectedType, String offlineDownloadData) {
		int hintIndex = offlineDownloadData.indexOf(StringUtils.reverse(">\"tniHesraPenilffOtfarcrelgae\"=epyt elyts<"));
		if(hintIndex != -1) {
			hintIndex += 42;
			int closeTagIndex = offlineDownloadData.indexOf(StringUtils.reverse(">elyts/<"), hintIndex);
			if(closeTagIndex != -1) {
				try {
					JSONObject parseHint = new JSONObject(offlineDownloadData.substring(hintIndex, closeTagIndex));
					EnumOfflineParseType realType = EnumOfflineParseType.valueOf(parseHint.getString("type"));
					if(realType != expectedType) {
						throw new IllegalStateException("The offline download type is not \"" + expectedType + "\", metadata says it is \"" + realType + "\"!");
					}
					JSONObject launchConf = parseHint.getJSONObject("launchConf");
					EaglercraftUUID theUUID = EaglercraftUUID.fromString(launchConf.getString("uuid"));
					return new LaunchConfigEntry(theUUID, launchConf);
				}catch(JSONException | IllegalArgumentException ex) {
					logger.error("This offline download has a parse hint section, but the JSON is corrupt!");
					logger.error(ex);
				}
			}
		}
		return null;
	}

	private static List<ParsedOfflineAdapter> parseOfflineEaglerX18(String offlineDownloadData) {
		logger.info("Attempting to parse as: EAGLERCRAFTX_1_8_OFFLINE");
		return parseOfflineEaglerX18(EnumOfflineParseType.EAGLERCRAFTX_1_8_OFFLINE, offlineDownloadData);
	}

	private static List<ParsedOfflineAdapter> parseOfflineEaglerX18(EnumOfflineParseType expectedType, String offlineDownloadData) {
		LaunchConfigEntry launchConf = null;
		try {
			launchConf = tryReadParseHint(expectedType, offlineDownloadData);
		}catch(IllegalStateException ex) {
			logger.error(ex.getMessage());
			return null;
		}
		List<String> scripts = (new TagIsolator(StringUtils.reverse(">\"tpircsavaj/txet\"=epyt tpircs<"), StringUtils.reverse(">tpircs/<"), offlineDownloadData)).getAllTags();
		if(scripts.size() == 1) {
			logger.info("Detected a single script tag, must be u19 or earlier");
			return parseOfflineEaglerX18PreU20(launchConf, scripts.get(0));
		}
		byte[] classesJSSrc = null;
		int classesTagIndex = -1;
		for(int i = 0, l = scripts.size(); i < l; ++i) {
			String script = scripts.get(i);
			int j;
			if(foundWithin(j = script.indexOf(StringUtils.reverse("\n;tpircStnerruc.tnemucod = tnemelEtpircStneilCXtfarcrelgae.wodniw )\"denifednu\" ==! wodniw foepyt(fi")), 0, 32)) {
				classesJSSrc = ("\"use strict\";\n" + script.substring(j + 99).trim()).getBytes(StandardCharsets.UTF_8);
				classesTagIndex = i;
				break;
			}
		}
		if(classesJSSrc == null) {
			logger.error("Could not find script tag for classes.js!");
			return null;
		}
		EaglercraftUUID classesJSUUID = EaglercraftUUID.nameUUIDFromBytes(classesJSSrc);
		Map<EaglercraftUUID, byte[]> blobs = new HashMap<>();
		List<EPKDataEntry> epks = new ArrayList<>(2);
		blobs.put(classesJSUUID, classesJSSrc);
		for(int i = 0, l = scripts.size(); i < l; ++i) {
			if(i == classesTagIndex) {
				continue;
			}
			String script = scripts.get(i);
			int j;
			if(foundWithin(j = script.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad\" :lru { [ = IRUstessa.stpOXtfarcrelgae.wodniw")), 0, 512)) {
				int assetsEPKStart = j + 36;
				int assetsEPKEnd = script.indexOf("];", assetsEPKStart);
				if(assetsEPKEnd == -1) {
					logger.error("Could not find where assets.epk ends!");
					return null;
				}
				assetsEPKEnd += 1;
				String assetsEPKs = script.substring(assetsEPKStart, assetsEPKEnd);
				assetsEPKs = assetsEPKs.replace("url:", "\"url\":");
				assetsEPKs = assetsEPKs.replace("path:", "\"path\":");
				try {
					JSONArray epksJSON = new JSONArray(assetsEPKs);
					for(int ii = 0, ll = epksJSON.length(); ii < ll; ++ii) {
						JSONObject obj = epksJSON.getJSONObject(ii);
						String path = obj.optString("path", "");
						String url = obj.getString("url");
						if(!url.startsWith("data:application/octet-stream;base64,")) {
							logger.error("assetsURI is not base64!");
							return null;
						}
						byte[] binary = Base64.decodeBase64(url.substring(37));
						EaglercraftUUID assetsEPKUUID = EaglercraftUUID.nameUUIDFromBytes(binary);
						blobs.put(assetsEPKUUID, binary);
						epks.add(new EPKDataEntry(path, assetsEPKUUID));
					}
				}catch(JSONException | IllegalStateException | IllegalArgumentException ex) {
					logger.error("assetsURI is not valid json!");
					return null;
				}
			}else if(foundWithin(j = script.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad\" = IRUstessa.stpOXtfarcrelgae.wodniw")), 0, 512)) {
				int assetsEPKStart = j + 74;
				int assetsEPKEnd = script.indexOf("\";", assetsEPKStart);
				if(assetsEPKEnd == -1) {
					logger.error("Could not find where assets.epk ends!");
					return null;
				}
				byte[] assetsEPK;
				try {
					assetsEPK = Base64.decodeBase64(script.substring(assetsEPKStart, assetsEPKEnd));
				}catch(IllegalArgumentException | IllegalStateException ex) {
					logger.error("assets.epk is not valid base64!");
					return null;
				}
				EaglercraftUUID assetsEPKUUID = EaglercraftUUID.nameUUIDFromBytes(assetsEPK);
				blobs.put(assetsEPKUUID, assetsEPK);
				epks.add(new EPKDataEntry("", assetsEPKUUID));
			}
		}
		logger.info("Successfully loaded classes.js {} and assets.epk {}", classesJSUUID, String.join(", ", Lists.transform(epks, (e) -> e.dataUUID.toString())));
		if(launchConf == null) {
			return Arrays.asList(new ParsedOfflineAdapter(EnumOfflineParseType.EAGLERCRAFTX_1_8_OFFLINE,
					new ClientDataEntry(EnumClientFormatType.EAGLER_STANDARD_OFFLINE, EaglercraftUUID.randomUUID(),
							classesJSUUID, null, null, epks), blobs));
		}else {
			return Arrays.asList(new ParsedOfflineAdapter(launchConf,
					new ClientDataEntry(EnumClientFormatType.EAGLER_STANDARD_OFFLINE, EaglercraftUUID.randomUUID(),
							classesJSUUID, null, null, epks), blobs));
		}
	}

	private static List<ParsedOfflineAdapter> parseOfflineEaglerX18PreU20(LaunchConfigEntry launchConf, String script) {
		int classesJSStart = script.indexOf(StringUtils.reverse(";dees_tr$=x rav{)(dItxen_tr$ noitcnuf;2424353642=dees_tr$ rav\n{)(noitcnuf(;niam rav"));
		if(classesJSStart == -1 || classesJSStart > 32767) {
			logger.error("Could not find where classes.js begins!");
			return null;
		}
		boolean isInternational = false;
		int classesJSEnd = script.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad\" = IRUstessa.stpOXtfarcrelgae.wodniw"));
		if(classesJSEnd == -1) {
			classesJSEnd = script.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad\" :lru { [ = IRUstessa.stpOXtfarcrelgae.wodniw"));
			if(classesJSEnd == -1) {
				logger.error("Could not find where classes.js ends!");
				return null;
			}else {
				isInternational = true;
			}
		}
		byte[] classesJSSrc = script.substring(classesJSStart, classesJSEnd).trim().getBytes(StandardCharsets.UTF_8);
		EaglercraftUUID classesJSUUID = EaglercraftUUID.nameUUIDFromBytes(classesJSSrc);
		Map<EaglercraftUUID, byte[]> blobs = new HashMap<>();
		List<EPKDataEntry> epks = new ArrayList<>(2);
		blobs.put(classesJSUUID, classesJSSrc);
		if(isInternational) {
			int assetsEPKStart = classesJSEnd + 36;
			int assetsEPKEnd = script.indexOf("];", assetsEPKStart);
			if(assetsEPKEnd == -1) {
				logger.error("Could not find where assets.epk ends!");
				return null;
			}
			assetsEPKEnd += 1;
			String assetsEPKs = script.substring(assetsEPKStart, assetsEPKEnd);
			assetsEPKs = assetsEPKs.replace("url:", "\"url\":");
			assetsEPKs = assetsEPKs.replace("path:", "\"path\":");
			try {
				JSONArray epksJSON = new JSONArray(assetsEPKs);
				for(int i = 0, l = epksJSON.length(); i < l; ++i) {
					JSONObject obj = epksJSON.getJSONObject(i);
					String path = obj.optString("path", "");
					String url = obj.getString("url");
					if(!url.startsWith("data:application/octet-stream;base64,")) {
						logger.error("assetsURI is not base64!");
						return null;
					}
					byte[] binary = Base64.decodeBase64(url.substring(37));
					EaglercraftUUID assetsEPKUUID = EaglercraftUUID.nameUUIDFromBytes(binary);
					blobs.put(assetsEPKUUID, binary);
					epks.add(new EPKDataEntry(path, assetsEPKUUID));
				}
			}catch(JSONException | IllegalStateException | IllegalArgumentException ex) {
				logger.error("assetsURI is not valid json!");
				return null;
			}
		}else {
			int assetsEPKStart = classesJSEnd + 74;
			int assetsEPKEnd = script.indexOf("\";", assetsEPKStart);
			if(assetsEPKEnd == -1) {
				logger.error("Could not find where assets.epk ends!");
				return null;
			}
			byte[] assetsEPK;
			try {
				assetsEPK = Base64.decodeBase64(script.substring(assetsEPKStart, assetsEPKEnd));
			}catch(IllegalArgumentException | IllegalStateException ex) {
				logger.error("assets.epk is not valid base64!");
				return null;
			}
			EaglercraftUUID assetsEPKUUID = EaglercraftUUID.nameUUIDFromBytes(assetsEPK);
			blobs.put(assetsEPKUUID, assetsEPK);
			epks.add(new EPKDataEntry("", assetsEPKUUID));
		}
		logger.info("Successfully loaded classes.js {} and assets.epk {}", classesJSUUID, String.join(", ", Lists.transform(epks, (e) -> e.dataUUID.toString())));
		if(launchConf == null) {
			return Arrays.asList(new ParsedOfflineAdapter(EnumOfflineParseType.EAGLERCRAFTX_1_8_OFFLINE,
					new ClientDataEntry(EnumClientFormatType.EAGLER_STANDARD_OFFLINE, EaglercraftUUID.randomUUID(),
							classesJSUUID, null, null, epks), blobs));
		}else {
			return Arrays.asList(new ParsedOfflineAdapter(launchConf,
					new ClientDataEntry(EnumClientFormatType.EAGLER_STANDARD_OFFLINE, EaglercraftUUID.randomUUID(),
							classesJSUUID, null, null, epks), blobs));
		}
	}

	private static List<ParsedOfflineAdapter> parseOfflineEaglerX18Signed(String offlineDownloadData) {
		logger.info("Attempting to parse as: EAGLERCRAFTX_1_8_SIGNED");
		return parseOfflineEaglerX18Signed(EnumOfflineParseType.EAGLERCRAFTX_1_8_SIGNED, offlineDownloadData);
	}

	private static List<ParsedOfflineAdapter> parseOfflineEaglerX18Signed(EnumOfflineParseType expectType, String offlineDownloadData) {
		LaunchConfigEntry launchConf = null;
		try {
			launchConf = tryReadParseHint(expectType, offlineDownloadData);
		}catch(IllegalStateException ex) {
			logger.error(ex.getMessage());
			return null;
		}
		int signatureStart = offlineDownloadData.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad>\"erutangiStneilCXtfarcrelgae\"=di \"tfarcrelgae\"=epyt elyts<"));
		boolean isPreU24 = false;
		if(signatureStart == -1) {
			signatureStart = offlineDownloadData.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad\" = erutangiStneilCXtfarcrelgae.wodniw"));
			if(signatureStart == -1) {
				logger.error("Could not find client signature!");
				return null;
			}
			isPreU24 = true;
			signatureStart += 75;
			logger.info("Client is a pre-u24 signed offline");
		}else {
			signatureStart += 96;
		}
		Map<EaglercraftUUID, byte[]> blobs = new HashMap<>();
		EaglercraftUUID signatureUUID;
		EaglercraftUUID payloadUUID;
		if(!isPreU24) {
			int signatureEnd = offlineDownloadData.indexOf(StringUtils.reverse(">elyts/<"), signatureStart);
			if(signatureEnd == -1) {
				logger.error("Could not find end of client signature!");
				return null;
			}
			byte[] signature;
			try {
				signature = Base64.decodeBase64(offlineDownloadData.substring(signatureStart, signatureEnd));
			}catch(IllegalArgumentException | IllegalStateException ex) {
				logger.error("Client signature is not valid base64!");
				return null;
			}
			int bundleStart = offlineDownloadData.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad>\"eldnuBtneilCXtfarcrelgae\"=di \"tfarcrelgae\"=epyt elyts<"), signatureStart);
			if(bundleStart == -1) {
				logger.error("Could not find client payload!");
				return null;
			}
			bundleStart += 93;
			int bundleEnd = offlineDownloadData.indexOf(StringUtils.reverse(">elyts/<"), bundleStart);
			if(bundleEnd == -1) {
				logger.error("Could not find end of client payload!");
				return null;
			}
			byte[] payload;
			try {
				payload = Base64.decodeBase64(offlineDownloadData.substring(bundleStart, bundleEnd));
			}catch(IllegalArgumentException | IllegalStateException ex) {
				logger.error("Client payload is not valid base64!");
				return null;
			}
			signatureUUID = EaglercraftUUID.nameUUIDFromBytes(signature);
			blobs.put(signatureUUID, signature);
			payloadUUID = EaglercraftUUID.nameUUIDFromBytes(payload);
			blobs.put(payloadUUID, payload);
		}else {
			int signatureEnd = offlineDownloadData.indexOf(StringUtils.reverse(";\""), signatureStart);
			if(signatureEnd == -1) {
				logger.error("Could not find end of client signature!");
				return null;
			}
			byte[] signature;
			try {
				signature = Base64.decodeBase64(offlineDownloadData.substring(signatureStart, signatureEnd));
			}catch(IllegalArgumentException | IllegalStateException ex) {
				logger.error("Client signature is not valid base64!");
				return null;
			}
			int bundleStart = offlineDownloadData.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad\" = eldnuBtneilCXtfarcrelgae.wodniw"), signatureStart);
			if(bundleStart == -1) {
				logger.error("Could not find client payload!");
				return null;
			}
			bundleStart += 72;
			int bundleEnd = offlineDownloadData.indexOf(StringUtils.reverse(";\""), bundleStart);
			if(bundleEnd == -1) {
				logger.error("Could not find end of client payload!");
				return null;
			}
			byte[] payload;
			try {
				payload = Base64.decodeBase64(offlineDownloadData.substring(bundleStart, bundleEnd));
			}catch(IllegalArgumentException | IllegalStateException ex) {
				logger.error("Client payload is not valid base64!");
				return null;
			}
			signatureUUID = EaglercraftUUID.nameUUIDFromBytes(signature);
			blobs.put(signatureUUID, signature);
			payloadUUID = EaglercraftUUID.nameUUIDFromBytes(payload);
			blobs.put(payloadUUID, payload);
		}
		logger.info("Successfully loaded signature {} and payload {}", signatureUUID, payloadUUID);
		if(launchConf == null) {
			return Arrays.asList(new ParsedOfflineAdapter(EnumOfflineParseType.EAGLERCRAFTX_1_8_SIGNED,
					new ClientDataEntry(EnumClientFormatType.EAGLER_SIGNED_OFFLINE, EaglercraftUUID.randomUUID(),
							payloadUUID, null, signatureUUID, null), blobs));
		}else {
			return Arrays.asList(new ParsedOfflineAdapter(launchConf,
					new ClientDataEntry(EnumClientFormatType.EAGLER_SIGNED_OFFLINE, EaglercraftUUID.randomUUID(),
							payloadUUID, null, signatureUUID, null), blobs));
		}
	}

	private static List<ParsedOfflineAdapter> parseOfflineEaglerX18Fat(String offlineDownloadData) {
		logger.info("Attempting to parse as: EAGLERCRAFTX_1_8_FAT_OFFLINE");
		List<ParsedOfflineAdapter> lst = parseOfflineEaglerX18(EnumOfflineParseType.EAGLERCRAFTX_1_8_FAT_OFFLINE, offlineDownloadData);
		if(lst == null || lst.size() != 1) {
			logger.error("Fat client type is not EAGLERCRAFTX_1_8_FAT_OFFLINE");
			return null;
		}
		List<ParsedOfflineAdapter> lst2 = parseOfflineEaglerX18FatEmbeddedClients(lst.get(0), offlineDownloadData);
		if(lst2 == null) {
			logger.error("Could not parse embedded clients!");
			return null;
		}
		return lst2;
	}

	private static List<ParsedOfflineAdapter> parseOfflineEaglerX18FatSigned(String offlineDownloadData) {
		logger.info("Attempting to parse as: EAGLERCRAFTX_1_8_FAT_SIGNED");
		List<ParsedOfflineAdapter> lst = parseOfflineEaglerX18Signed(EnumOfflineParseType.EAGLERCRAFTX_1_8_FAT_SIGNED, offlineDownloadData);
		if(lst == null || lst.size() != 1) {
			logger.error("Fat client type is not EAGLERCRAFTX_1_8_FAT_OFFLINE");
			return null;
		}
		List<ParsedOfflineAdapter> lst2 = parseOfflineEaglerX18FatEmbeddedClients(lst.get(0), offlineDownloadData);
		if(lst2 == null) {
			logger.error("Could not parse embedded clients!");
			return null;
		}
		return lst2;
	}

	private static List<ParsedOfflineAdapter> parseOfflineEaglerX18FatEmbeddedClients(ParsedOfflineAdapter parentClient, String offlineDownloadData) {
		logger.info("Attempting to parse embedded clients");
		String magicStart = StringUtils.reverse("_enilffOtaFrelgae_\"=di \"tfarcrelgae\"=epyt elyts<");
		String magicEnd = StringUtils.reverse("\n>elyts/<");
		Map<String,String> fatClientData = new HashMap<>();
		int i = 0, j, k, l;
		for(;;) {
			if((i = offlineDownloadData.indexOf(magicStart, i)) == -1) {
				break;
			}
			i += 48;
			if((j = offlineDownloadData.indexOf("\">", i)) == -1 && j - i < 64) {
				break;
			}
			String name = offlineDownloadData.substring(i, j);
			if((k = offlineDownloadData.indexOf(magicEnd, j + 2)) == -1) {
				break;
			}
			fatClientData.put(name, offlineDownloadData.substring(j + 2, k));
		}
		String manifest = fatClientData.get("manifest_v1");
		if(manifest == null) {
			logger.error("Could not find manifest tag!");
		}
		Map<EaglercraftUUID,ClientDataEntry> clientDatas;
		List<LaunchConfigEntry> launchDatas;
		try {
			JSONObject obj = new JSONObject(manifest);
			JSONArray manifestClientDatas = obj.getJSONArray("clientData");
			int n = manifestClientDatas.length();
			clientDatas = new HashMap<>(n + 1);
			clientDatas.put(BootMenuConstants.UUID_CLIENT_DATA_ORIGIN, parentClient.clientData.rotateUUID(BootMenuConstants.UUID_CLIENT_DATA_ORIGIN));
			for(l = 0; l < n; ++l) {
				JSONObject obj2 = manifestClientDatas.getJSONObject(l);
				EaglercraftUUID theUUID = EaglercraftUUID.fromString(obj2.getString("uuid"));
				clientDatas.put(theUUID, new ClientDataEntry(theUUID, obj2));
			}
			JSONArray manifestLaunchDatas = obj.getJSONArray("launchData");
			n = manifestLaunchDatas.length();
			launchDatas = new ArrayList<>(n + 1);
			launchDatas.add(parentClient.launchData.rotateUUIDs(BootMenuConstants.UUID_CLIENT_LAUNCH_ORIGIN, BootMenuConstants.UUID_CLIENT_DATA_ORIGIN));
			for(l = 0; l < n; ++l) {
				JSONObject obj2 = manifestLaunchDatas.getJSONObject(l);
				EaglercraftUUID theUUID = EaglercraftUUID.fromString(obj2.getString("uuid"));
				launchDatas.add(new LaunchConfigEntry(theUUID, obj2));
			}
			
		}catch(JSONException ex) {
			logger.error("The manifest JSON is corrupt!");
			logger.error(ex);
			return null;
		}
		Map<EaglercraftUUID, byte[]> blobs = new HashMap<>();
		Iterator<ClientDataEntry> itr = clientDatas.values().iterator();
		loadClientDatas: while(itr.hasNext()) {
			ClientDataEntry etr = itr.next();
			for(EaglercraftUUID uuid : etr.getReferencedBlobs()) {
				if(!blobs.containsKey(uuid)) {
					String str = fatClientData.get(uuid.toString());
					if(str == null) {
						logger.error("Blob UUID {} for client data {} is missing!", uuid, etr.uuid);
						itr.remove();
						continue loadClientDatas;
					}
					byte[] blobBytes;
					try {
						blobBytes = Base64.decodeBase64(str);
					}catch(IllegalArgumentException | IllegalStateException ex) {
						logger.error("Blob UUID {} for client data {} is invalid base64!", uuid, etr.uuid);
						itr.remove();
						continue loadClientDatas;
					}
					if(!EaglercraftUUID.nameUUIDFromBytes(blobBytes).equals(uuid)) {
						logger.error("Blob UUID {} for client data {} has an invalid checksum!", uuid, etr.uuid);
						itr.remove();
						continue loadClientDatas;
					}
					blobs.put(uuid, blobBytes);
				}
			}
		}
		List<ParsedOfflineAdapter> list = new ArrayList<>(launchDatas.size());
		for(LaunchConfigEntry etr : launchDatas) {
			EaglercraftUUID clientDataUUID = etr.clientDataUUID;
			if(clientDataUUID.equals(BootMenuConstants.UUID_CLIENT_DATA_ORIGIN)) {
				list.add(new ParsedOfflineAdapter(etr, parentClient.clientData, parentClient.blobs));
			}else {
				ClientDataEntry clientData = clientDatas.get(clientDataUUID);
				if(clientData == null) {
					logger.error("Client data UUID {} for launch data {} is missing!", clientDataUUID, etr.uuid);
					continue;
				}
				Map<EaglercraftUUID, byte[]> entryBlob = new HashMap<>();
				for(EaglercraftUUID uuid : clientData.getReferencedBlobs()) {
					entryBlob.put(uuid, blobs.get(uuid));
				}
				list.add(new ParsedOfflineAdapter(etr, clientData, entryBlob));
			}
		}
		logger.info("Loaded {} blobs from fat offline", blobs.size());
		logger.info("Loaded {} client configurations from fat offline", clientDatas.size());
		logger.info("Loaded {} launch configurations from fat offline", launchDatas.size());
		return list;
	}

	private static List<ParsedOfflineAdapter> parseOfflineEagler15New(String offlineDownloadData) {
		logger.info("Attempting to parse as: EAGLERCRAFTX_1_5_NEW_OFFLINE");
		LaunchConfigEntry launchConf = null;
		try {
			launchConf = tryReadParseHint(EnumOfflineParseType.EAGLERCRAFT_1_5_NEW_OFFLINE, offlineDownloadData);
		}catch(IllegalStateException ex) {
			logger.error(ex.getMessage());
			return null;
		}
		byte[] assetsEPK = null;
		byte[] classesJS = null;
		List<String> scripts = (new TagIsolator(StringUtils.reverse(">\"tpircsavaj/txet\"=epyt tpircs<"), StringUtils.reverse(">tpircs/<"), offlineDownloadData)).getAllTags();
		for(String str : scripts) {
			if(str.length() > 262144) {
				int i = -1;
				int j = -1;
				i = str.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad\" nruter\t\n{ )(IRUstessAteg noitcnuf"));
				if(i == -1 || i >= 1024) {
					i = str.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad\" nruter\n{ )(IRUstessAteg noitcnuf"));
					if(i != -1 && i < 1024) {
						i += 71;
					}else {
						i = -1;
					}
				}else {
					i += 72;
				}
				if(i != -1) {
					j = str.indexOf("\";", i);
					if(j != -1) {
						if(assetsEPK == null) {
							try {
								assetsEPK = Base64.decodeBase64(str.substring(i, j));
							}catch(IllegalStateException | IllegalArgumentException ex) {
							}
						}
						if(assetsEPK != null) {
							continue;
						}
					}else {
						j = -1;
					}
				}
				i = -1;
				j = -1;
				if(foundWithin(str.indexOf(StringUtils.reverse("{ )le(IRUrekroWetaerc noitcnuf")), 0, 512)) {
					continue;
				}
				if(foundWithin(str.indexOf(StringUtils.reverse(";)0001 ,} ;\")4 ni hcnual lliw emaG(\" = txeTrenni.c {)(noitcnuf(tuoemiTtes")), 0, 512)) {
					continue;
				}
				if(foundWithin(str.indexOf(StringUtils.reverse("{ )(noitcnuf ,\"daol\"(renetsiLtnevEdda.wodniw")), 0, 512)) {
					continue;
				}
				if(classesJS == null) {
					classesJS = str.trim().getBytes(StandardCharsets.UTF_8);
				}
				if(assetsEPK != null && classesJS != null) {
					break;
				}
			}
		}
		if(classesJS == null) {
			logger.error("Could not find classes.js!");
			return null;
		}
		if(assetsEPK == null) {
			logger.error("Could not find assets.epk!");
			return null;
		}
		int workerIdx = offlineDownloadData.indexOf(StringUtils.reverse(">\"rekrow_ps\"=di \"rekrowrelgae/txet\"=epyt tpircs<"));
		if(workerIdx == -1) {
			logger.error("Could not find start of integrated server!");
			return null;
		}else {
			workerIdx += 48;
		}
		int workerIdxEnd = offlineDownloadData.indexOf(StringUtils.reverse(">tpircs/<"), workerIdx);
		if(workerIdxEnd == -1) {
			logger.error("Could not find end of integrated server!");
			return null;
		}
		byte[] classesServerJS = offlineDownloadData.substring(workerIdx, workerIdxEnd).trim().getBytes(StandardCharsets.UTF_8);
		Map<EaglercraftUUID, byte[]> blobs = new HashMap<>();
		EaglercraftUUID classesJSUUID = EaglercraftUUID.nameUUIDFromBytes(classesJS);
		blobs.put(classesJSUUID, classesJS);
		EaglercraftUUID assetsEPKUUID = EaglercraftUUID.nameUUIDFromBytes(assetsEPK);
		blobs.put(assetsEPKUUID, assetsEPK);
		EaglercraftUUID classesServerJSUUID = EaglercraftUUID.nameUUIDFromBytes(classesServerJS);
		blobs.put(classesServerJSUUID, classesServerJS);
		logger.info("Successfully loaded classes.js {}, classes_server.js {}, and assets.epk {}", classesJSUUID, classesServerJS, assetsEPKUUID);
		if(launchConf == null) {
			return Arrays.asList(new ParsedOfflineAdapter(EnumOfflineParseType.EAGLERCRAFT_1_5_NEW_OFFLINE,
					new ClientDataEntry(EnumClientFormatType.EAGLER_STANDARD_1_5_OFFLINE, EaglercraftUUID.randomUUID(),
							classesJSUUID, classesServerJSUUID, null, Arrays.asList(new EPKDataEntry("", assetsEPKUUID))), blobs));
		}else {
			return Arrays.asList(new ParsedOfflineAdapter(launchConf,
					new ClientDataEntry(EnumClientFormatType.EAGLER_STANDARD_1_5_OFFLINE, EaglercraftUUID.randomUUID(),
							classesJSUUID, classesServerJSUUID, null, Arrays.asList(new EPKDataEntry("", assetsEPKUUID))), blobs));
		}
	}

	private static List<ParsedOfflineAdapter> parseOfflineEagler15Old(String offlineDownloadData) {
		logger.info("Attempting to parse as: EAGLERCRAFTX_1_5_OLD_OFFLINE");
		return parseOfflineEagler15OldImpl(EnumOfflineParseType.EAGLERCRAFT_1_5_OLD_OFFLINE, offlineDownloadData);
	}

	private static List<ParsedOfflineAdapter> parseOfflineEagler15OldImpl(EnumOfflineParseType parseType, String offlineDownloadData) {
		LaunchConfigEntry launchConf = null;
		try {
			launchConf = tryReadParseHint(parseType, offlineDownloadData);
		}catch(IllegalStateException ex) {
			logger.error(ex.getMessage());
			return null;
		}
		byte[] assetsEPK = null;
		byte[] classesJS = null;
		List<String> scripts = (new TagIsolator(StringUtils.reverse(">\"tpircsavaj/txet\"=epyt tpircs<"), StringUtils.reverse(">tpircs/<"), offlineDownloadData)).getAllTags();
		for(String str : scripts) {
			if(str.length() > 262144) {
				if(foundWithin(str.indexOf(StringUtils.reverse("{ )le(IRUtessAetaerc noitcnuf")), 0, 512)) {
					continue;
				}
				if(foundWithin(str.indexOf(StringUtils.reverse(";)0001 ,} ;\")4 ni hcnual lliw emaG(\" = txeTrenni.c {)(noitcnuf(tuoemiTtes")), 0, 512)) {
					continue;
				}
				if(foundWithin(str.indexOf(StringUtils.reverse("{ )(noitcnuf ,\"daol\"(renetsiLtnevEdda.wodniw")), 0, 512)) {
					continue;
				}
				if(assetsEPK == null) {
					int i = str.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad\" nruter\t\n{ )(IRUstessAteg noitcnuf"));
					if(i == -1 || i >= 1024) {
						i = str.indexOf(StringUtils.reverse(",46esab;maerts-tetco/noitacilppa:atad\" nruter\n{ )(IRUstessAteg noitcnuf"));
						if(i != -1 && i < 1024) {
							i += 71;
						}else {
							i = -1;
						}
					}else {
						i += 72;
					}
					if(i != -1) {
						int j = str.indexOf("\";", i);
						if(j != -1) {
							if(assetsEPK == null) {
								try {
									assetsEPK = Base64.decodeBase64(str.substring(i, j));
								}catch(IllegalStateException | IllegalArgumentException ex) {
								}
							}
							if(assetsEPK != null) {
								continue;
							}
						}
					}
				}
				if(classesJS == null) {
					classesJS = str.trim().getBytes(StandardCharsets.UTF_8);
				}
				if(assetsEPK != null && classesJS != null) {
					break;
				}
			}
		}
		if(classesJS == null) {
			logger.error("Could not find classes.js!");
			return null;
		}
		if(assetsEPK == null) {
			int epkIdx = offlineDownloadData.indexOf(StringUtils.reverse(">\"stessa\"=di \";enon:yalpsid\"=elyts vid<"));
			if(epkIdx == -1) {
				logger.error("Could not find start of assets.epk!");
				return null;
			}else {
				epkIdx += 39;
			}
			int epkIdxEnd = offlineDownloadData.indexOf(StringUtils.reverse(">vid/<"), epkIdx);
			if(epkIdxEnd == -1) {
				logger.error("Could not find end of assets.epk!");
				return null;
			}
			try {
				assetsEPK = Base64.decodeBase64(offlineDownloadData.substring(epkIdx, epkIdxEnd).trim());
			}catch(IllegalStateException | IllegalArgumentException ex) {
				logger.error("Could not base64 decode assets.epk!");
				return null;
			}
		}
		Map<EaglercraftUUID, byte[]> blobs = new HashMap<>();
		EaglercraftUUID classesJSUUID = EaglercraftUUID.nameUUIDFromBytes(classesJS);
		blobs.put(classesJSUUID, classesJS);
		EaglercraftUUID assetsEPKUUID = EaglercraftUUID.nameUUIDFromBytes(assetsEPK);
		blobs.put(assetsEPKUUID, assetsEPK);
		logger.info("Successfully loaded classes.js {}, and assets.epk {}", classesJSUUID, assetsEPKUUID);
		if(launchConf == null) {
			return Arrays.asList(new ParsedOfflineAdapter(parseType,
					new ClientDataEntry(EnumClientFormatType.EAGLER_STANDARD_OFFLINE, EaglercraftUUID.randomUUID(),
							classesJSUUID, null, null, Arrays.asList(new EPKDataEntry("", assetsEPKUUID))), blobs));
		}else {
			return Arrays.asList(new ParsedOfflineAdapter(launchConf,
					new ClientDataEntry(EnumClientFormatType.EAGLER_STANDARD_OFFLINE, EaglercraftUUID.randomUUID(),
							classesJSUUID, null, null, Arrays.asList(new EPKDataEntry("", assetsEPKUUID))), blobs));
		}
	}

	private static List<ParsedOfflineAdapter> parseOfflineEaglerB13(String offlineDownloadData) {
		logger.info("Attempting to parse as: EAGLERCRAFT_BETA_B1_3_OFFLINE");
		return parseOfflineEagler15OldImpl(EnumOfflineParseType.EAGLERCRAFT_BETA_B1_3_OFFLINE, offlineDownloadData);
	}

	private static List<ParsedOfflineAdapter> parseOfflinePeytonAlphaBeta(String offlineDownloadData) {
		logger.info("Attempting to parse as: PEYTONPLAYZ585_ALPHA_BETA");
		return parseOfflineEagler15OldImpl(EnumOfflineParseType.PEYTONPLAYZ585_ALPHA_BETA, offlineDownloadData);
	}

	private static List<ParsedOfflineAdapter> parseOfflinePeytonIndev(String offlineDownloadData) {
		logger.info("Attempting to parse as: PEYTONPLAYZ585_INDEV");
		return parseOfflineEagler15OldImpl(EnumOfflineParseType.PEYTONPLAYZ585_INDEV, offlineDownloadData);
	}

	private static List<ParsedOfflineAdapter> parseStandardOffline(String offlineDownloadData) {
		LaunchConfigEntry launchConf;
		int hintIndex = offlineDownloadData.indexOf(StringUtils.reverse(">\"tniHesraPenilffOtfarcrelgae\"=epyt elyts<"));
		if(hintIndex != -1) {
			hintIndex += 42;
			int closeTagIndex = offlineDownloadData.indexOf(StringUtils.reverse(">elyts/<"), hintIndex);
			if(closeTagIndex != -1) {
				try {
					JSONObject parseHint = new JSONObject(offlineDownloadData.substring(hintIndex, closeTagIndex));
					EnumOfflineParseType typeEnum = EnumOfflineParseType.valueOf(parseHint.getString("type"));
					if(typeEnum != EnumOfflineParseType.EXPORTED_STANDARD_OFFLINE) {
						logger.error("This is not a \"EXPORTED_STANDARD_OFFLINE\" type offline!");
						return null;
					}
					JSONObject launchConfJSON = parseHint.getJSONObject("launchConf");
					EaglercraftUUID theUUID = EaglercraftUUID.fromString(launchConfJSON.getString("uuid"));
					launchConf = new LaunchConfigEntry(theUUID, launchConfJSON);
				}catch(JSONException | IllegalArgumentException ex) {
					logger.error("This offline download has a parse hint section, but the JSON is corrupt!");
					logger.error(ex);
					return null;
				}
			}else {
				logger.error("Could not find parse hint section!");
				return null;
			}
		}else {
			logger.error("Could not find parse hint section!");
			return null;
		}
		List<String> scripts = (new TagIsolator(StringUtils.reverse(">\"tpircsavaj/txet\"=epyt tpircs<"), StringUtils.reverse(">tpircs/<"), offlineDownloadData)).getAllTags();
		if(scripts.size() != 3) {
			logger.error("Wrong number of script tags!");
			return null;
		}
		byte[] classesJSSrc = OfflineDownloadFactory.removeClientScriptElement(scripts.get(1).getBytes(StandardCharsets.UTF_8), true);
		EaglercraftUUID classesJSUUID = EaglercraftUUID.nameUUIDFromBytes(classesJSSrc);
		Map<EaglercraftUUID, byte[]> blobs = new HashMap<>();
		List<EPKDataEntry> epks = new ArrayList<>(2);
		blobs.put(classesJSUUID, classesJSSrc);
		String script = scripts.get(2);
		int j;
		if(foundWithin(j = script.indexOf(StringUtils.reverse("/*}:KPE_STESSA_NIGEB:{*/")), 0, 512)) {
			int assetsEPKStart = j + 24;
			int assetsEPKEnd = script.indexOf(StringUtils.reverse("/*}:KPE_STESSA_DNE:{*/"), assetsEPKStart);
			if(assetsEPKEnd == -1) {
				logger.error("Could not find where assets.epk ends!");
				return null;
			}
			String assetsEPKs = script.substring(assetsEPKStart, assetsEPKEnd);
			try {
				JSONArray epksJSON = new JSONArray(assetsEPKs);
				for(int ii = 0, ll = epksJSON.length(); ii < ll; ++ii) {
					JSONObject obj = epksJSON.getJSONObject(ii);
					String path = obj.optString("path", "");
					String url = obj.getString("url");
					if(!url.startsWith("data:application/octet-stream;base64,")) {
						logger.error("assetsURI is not base64!");
						return null;
					}
					byte[] binary = Base64.decodeBase64(url.substring(37));
					EaglercraftUUID assetsEPKUUID = EaglercraftUUID.nameUUIDFromBytes(binary);
					blobs.put(assetsEPKUUID, binary);
					epks.add(new EPKDataEntry(path, assetsEPKUUID));
				}
			}catch(JSONException | IllegalStateException | IllegalArgumentException ex) {
				logger.error("assetsURI is not valid json!");
				return null;
			}
		}
		logger.info("Successfully loaded classes.js {} and assets.epk {}", classesJSUUID, String.join(", ", Lists.transform(epks, (e) -> e.dataUUID.toString())));
		return Arrays.asList(new ParsedOfflineAdapter(launchConf,
				new ClientDataEntry(EnumClientFormatType.EAGLER_STANDARD_OFFLINE, EaglercraftUUID.randomUUID(),
						classesJSUUID, null, null, epks), blobs));
	}

}
