package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.json.JSONArray;
import org.json.JSONObject;
import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLScriptElement;

import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerOutputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerZLIB;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.cache.EaglerLoadingCache;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMBlobURLHandle;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMBlobURLManager;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

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
public class ClientBootFactory {

	private static final Logger logger = LogManager.getLogger("ClientBootFactory");

	private static class UUIDStringPair {

		private final EaglercraftUUID uuid;
		private final String str;

		private UUIDStringPair(EaglercraftUUID uuid, String str) {
			this.uuid = uuid;
			this.str = str;
		}

		@Override
		public int hashCode() {
			return (31 + uuid.hashCode()) * 31 + str.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if(this == obj)
				return true;
			if(obj == null || !(obj instanceof UUIDStringPair))
				return false;
			UUIDStringPair other = (UUIDStringPair) obj;
			return Objects.equals(str, other.str) && Objects.equals(uuid, other.uuid);
		}

	}

	public static void bootClient(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			Map<EaglercraftUUID, Supplier<byte[]>> loaders, IProgressMsgCallback cb) {
		if(launchConf.clearCookiesBeforeLaunch) {
			BootMenuEntryPoint.clearCookies();
		}
		if(BootMenuConstants.UUID_CLIENT_DATA_ORIGIN.equals(clientData.uuid)) {
			bootOriginClient(launchConf, clientData, cb);
			return;
		}
		EaglerLoadingCache<EaglercraftUUID, byte[]> loadingCacheA = new EaglerLoadingCache<EaglercraftUUID, byte[]>((uuid) -> {
			Supplier<byte[]> sup = loaders.get(uuid);
			return sup != null ? sup.get() : null;
		});
		EaglerLoadingCache<UUIDStringPair, TeaVMBlobURLHandle> loadingCacheB = new EaglerLoadingCache<UUIDStringPair, TeaVMBlobURLHandle>((uuidMime) -> {
			byte[] b = loadingCacheA.get(uuidMime.uuid);
			return b != null ? TeaVMBlobURLManager.registerNewURLByte(b, uuidMime.str) : null;
		});
		switch(launchConf.type) {
		case STANDARD_OFFLINE_V1:
			if(clientData.type == EnumClientFormatType.EAGLER_STANDARD_OFFLINE) {
				bootClientStandardOffline(launchConf, clientData, loadingCacheB, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for STANDARD_OFFLINE_V1!");
			}
			break;
		case EAGLERX_V1:
			if(clientData.type == EnumClientFormatType.EAGLER_STANDARD_OFFLINE) {
				bootClientEaglerX18(launchConf, clientData, loadingCacheB, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for EAGLERX_V1!");
			}
			break;
		case EAGLERX_SIGNED_V1:
			if(clientData.type == EnumClientFormatType.EAGLER_SIGNED_OFFLINE) {
				bootClientEaglerX18Signed(launchConf, clientData, loadingCacheA, loadingCacheB, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for EAGLERX_SIGNED_V1!");
			}
			break;
		case EAGLER_1_5_V1:
			if(clientData.type == EnumClientFormatType.EAGLER_STANDARD_OFFLINE) {
				bootClientEagler15Old(launchConf, clientData, loadingCacheB, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for EAGLER_1_5_V1!");
			}
			break;
		case EAGLER_1_5_V2:
			if(clientData.type == EnumClientFormatType.EAGLER_STANDARD_1_5_OFFLINE) {
				bootClientEagler15New(launchConf, clientData, loadingCacheA, loadingCacheB, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for EAGLER_1_5_V2!");
			}
			break;
		case EAGLER_BETA_V1:
			if(clientData.type == EnumClientFormatType.EAGLER_STANDARD_OFFLINE) {
				bootClientEaglerB13(launchConf, clientData, loadingCacheB, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for EAGLER_1_5_V2!");
			}
			break;
		case PEYTON_V1:
			if(clientData.type == EnumClientFormatType.EAGLER_STANDARD_OFFLINE) {
				bootClientPeytonIndev(launchConf, clientData, loadingCacheB, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for PEYTON_V1!");
			}
			break;
		case PEYTON_V2:
			if(clientData.type == EnumClientFormatType.EAGLER_STANDARD_OFFLINE) {
				bootClientPeytonAlphaBeta(launchConf, clientData, loadingCacheB, cb);
			}else {
				throw new UnsupportedOperationException("Wrong client data type " + clientData.type + " for PEYTON_V2!");
			}
			break;
		}
	}

	private static void doUpdateMessage(IProgressMsgCallback cb, String str) {
		logger.info(str);
		cb.updateMessage(str);
	}

	@JSBody(params = { "mainName" }, script = "window[mainName] = null;")
	private static native boolean clearMain(String mainName);

	@JSBody(params = { "mainName" }, script = "return (typeof window[mainName] === \"function\");")
	private static native boolean isMainReady(String mainName);

	@JSBody(params = { "optsVarName", "optsVarJSON", "mainName", "blockUnsigned" }, script = "setTimeout(function() { window[optsVarName] = JSON.parse(optsVarJSON); if(blockUnsigned) window[optsVarName].bootMenuBlocksUnsignedClients = true; window[mainName](); }, 1);")
	private static native void callMain(String optsVarName, String optsVarJSON, String mainName, boolean blockUnsigned);

	@JSBody(params = { "clientSigURL", "clientPayloadURL", "launchOptsJSON", "blockUnsigned" }, script = "window.eaglercraftXOptsHints = JSON.parse(launchOptsJSON); if(blockUnsigned) window.eaglercraftXOptsHints.bootMenuBlocksUnsignedClients = true; window.eaglercraftXClientSignature = clientSigURL; window.eaglercraftXClientBundle = clientPayloadURL;")
	private static native void setupSignedClientOpts(String clientSigURL, String clientPayloadURL, String launchOptsJSON, boolean blockUnsigned);

	@JSBody(params = { "optsVarName", "containerId", "assetsEPKURL", "b64Opts", "joinServer", "mainName", "blockUnsigned" }, script = "setTimeout(function() { window[optsVarName] = [ containerId, assetsEPKURL, b64Opts ]; if(blockUnsigned && (typeof window.eaglercraftXOpts === \"object\")) window.eaglercraftXOpts.bootMenuBlocksUnsignedClients = true; if(joinServer.length > 0) window[optsVarName].push(joinServer); window[mainName](); }, 1);")
	private static native void callMainOld15(String optsVarName, String containerId, String assetsEPKURL, String b64Opts, String joinServer, String mainName, boolean blockUnsigned);

	@JSBody(params = { "optsVarName", "containerId", "assetsEPKURL", "joinServer", "mainName", "blockUnsigned" }, script = "setTimeout(function() { window[optsVarName] = [ containerId, assetsEPKURL ]; if(blockUnsigned && (typeof window.eaglercraftXOpts === \"object\")) window.eaglercraftXOpts.bootMenuBlocksUnsignedClients = true; if(joinServer.length > 0) window[optsVarName].push(joinServer); window[mainName](); }, 1);")
	private static native void callMainOldB13(String optsVarName, String containerId, String assetsEPKURL, String joinServer, String mainName, boolean blockUnsigned);

	@JSBody(params = { "optsVarName", "optsVarJSON", "blockUnsigned" }, script = "window[optsVarName] = JSON.parse(optsVarJSON); if(blockUnsigned) window[optsVarName].bootMenuBlocksUnsignedClients = true;")
	private static native void setJSONOpts(String optsVarName, String optsVarJSON, boolean blockUnsigned);

	private static void bootOriginClient(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			IProgressMsgCallback cb) {
		doUpdateMessage(cb, "Preparing launch opts...");
		JSONObject launchOpts = new JSONObject(launchConf.launchOpts);
		launchOpts.put("container", BootMenuEntryPoint.getOriginContainer());
		launchOpts.put("assetsURI", BootMenuEntryPoint.getUnsignedClientAssetsEPKRaw());
		final String launchOptsStr = RelayRandomizeHelper.replaceRelayMacroWithConstant(launchOpts.toString());
		BootMenuMain.continueBootToOriginClient(() -> {
			setJSONOpts("eaglercraftXOpts", launchOptsStr, IBootMenuConfigAdapter.instance.isBootMenuBlocksUnsignedClients());
		});
	}

	private static void bootClientEaglerX18(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<UUIDStringPair, TeaVMBlobURLHandle> loadingCache, IProgressMsgCallback cb) {
		boolean blockUnsigned = IBootMenuConfigAdapter.instance.isBootMenuBlocksUnsignedClients();
		if(blockUnsigned) {
			throw new UnsignedBootException();
		}
		JSONObject launchOpts = new JSONObject(launchConf.launchOpts);
		doUpdateMessage(cb, "Resolving classes.js (" + clientData.mainPayload + ")");
		TeaVMBlobURLHandle classesJSURL = loadingCache.get(new UUIDStringPair(clientData.mainPayload, "text/javascript"));
		List<TeaVMBlobURLHandle> toCleanOnException = new ArrayList<>();
		if(classesJSURL == null) {
			String msg = "Could not resolve classes.js! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		toCleanOnException.add(classesJSURL);
		JSONArray epkFiles = new JSONArray();
		for(EPKDataEntry etr : clientData.epkFiles) {
			doUpdateMessage(cb, "Resolving assets.epk (" + etr.dataUUID + ", path: /" + etr.extractTo + ")");
			TeaVMBlobURLHandle epkURL = loadingCache.get(new UUIDStringPair(etr.dataUUID, "application/octet-stream"));
			if(epkURL == null) {
				for(TeaVMBlobURLHandle url : toCleanOnException) {
					try {
						TeaVMBlobURLManager.releaseURL(url);
					}catch(Throwable t) {
					}
				}
				String msg = "Could not resolve assets.epk! (" + etr.dataUUID + ", path: /" + etr.extractTo + ")";
				logger.error(msg);
				throw new NullPointerException(msg);
			}
			toCleanOnException.add(epkURL);
			JSONObject epkEntry = new JSONObject();
			epkEntry.put("url", epkURL.toExternalForm());
			epkEntry.put("path", etr.extractTo);
			epkFiles.put(epkEntry);
		}
		launchOpts.put("assetsURI", epkFiles);
		doUpdateMessage(cb, "Launching client...");
		BootMenuMain.runLaterMS(() -> {
			clearMain("main");
			HTMLDocument docTemp = BootMenuMain.doc;
			launchOpts.put("container", BootMenuMain.createRootElementForClient());
			final String launchOptsStr = RelayRandomizeHelper.replaceRelayMacroWithConstant(launchOpts.toString());
			HTMLScriptElement scriptElement = (HTMLScriptElement)docTemp.createElement("script");
			scriptElement.addEventListener("load", (evt) -> {
				BootMenuMain.runLater(() -> {
					while(!isMainReady("main")) {
						logger.error("main function is not available yet! waiting 250ms...");
						EagUtils.sleep(250l);
					}
					BootMenuMain.stopEventLoop();
					callMain("eaglercraftXOpts", launchOptsStr, "main", blockUnsigned);
				});
			});
			scriptElement.setType("text/javascript");
			scriptElement.setSrc(classesJSURL.toExternalForm());
			docTemp.getHead().appendChild(scriptElement);
		}, 250);
	}

	private static void bootClientEaglerX18Signed(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<EaglercraftUUID, byte[]> loadingCacheBytes,
			EaglerLoadingCache<UUIDStringPair, TeaVMBlobURLHandle> loadingCache, IProgressMsgCallback cb) {
		JSONObject launchOpts = new JSONObject(launchConf.launchOpts);
		if(!launchOpts.has("hintsVersion")) {
			launchOpts.put("hintsVersion", 1);
		}
		doUpdateMessage(cb, "Resolving client signature (" + clientData.clientSignature + ")");
		byte[] clientSigBytes = loadingCacheBytes.get(clientData.clientSignature);
		final TeaVMBlobURLHandle clientSigURL = loadingCache.get(new UUIDStringPair(clientData.clientSignature, "application/octet-stream"));
		List<TeaVMBlobURLHandle> toCleanOnException = new ArrayList<>();
		if(clientSigURL == null) {
			clientSigBytes = null;
			String msg = "Could not resolve client signature! (" + clientData.clientSignature + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		toCleanOnException.add(clientSigURL);
		doUpdateMessage(cb, "Resolving client payload (" + clientData.mainPayload + ")");
		byte[] clientPayloadBytes = loadingCacheBytes.get(clientData.mainPayload);
		final TeaVMBlobURLHandle clientPayloadURL = loadingCache.get(new UUIDStringPair(clientData.mainPayload, "application/octet-stream"));
		if(clientPayloadURL == null) {
			clientSigBytes = null;
			clientPayloadBytes = null;
			for(TeaVMBlobURLHandle url : toCleanOnException) {
				try {
					TeaVMBlobURLManager.releaseURL(url);
				}catch(Throwable t) {
				}
			}
			String msg = "Could not resolve client payload! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		toCleanOnException.add(clientPayloadURL);
		boolean blockUnsigned = IBootMenuConfigAdapter.instance.isBootMenuBlocksUnsignedClients();
		if(blockUnsigned) {
			doUpdateMessage(cb, "Verifying signature...");
			boolean valid = SignatureCheckHelper.checkSignatureValid(clientSigBytes, clientPayloadBytes);
			if(!valid) {
				for(TeaVMBlobURLHandle url : toCleanOnException) {
					try {
						TeaVMBlobURLManager.releaseURL(url);
					}catch(Throwable t) {
					}
				}
				throw new UnsignedBootException();
			}
		}
		doUpdateMessage(cb, "Decompressing payload...");
		EaglerOutputStream bao = new EaglerOutputStream(0x1000000);
		try(InputStream is = EaglerZLIB.newGZIPInputStream(new EaglerInputStream(clientPayloadBytes))) {
			byte[] copybuffer = new byte[16384];
			int i;
			while((i = is.read(copybuffer)) != -1) {
				bao.write(copybuffer, 0, i);
			}
		}catch(IOException ex) {
			clientSigBytes = null;
			clientPayloadBytes = null;
			for(TeaVMBlobURLHandle url : toCleanOnException) {
				try {
					TeaVMBlobURLManager.releaseURL(url);
				}catch(Throwable t) {
				}
			}
			throw new IllegalArgumentException("Could not decompress signed client payload!");
		}
		byte[] decompressed = bao.toByteArray();
		bao = null;
		clientSigBytes = null;
		clientPayloadBytes = null;
		final TeaVMBlobURLHandle clientPayloadURLDecompress = TeaVMBlobURLManager.registerNewURLByte(decompressed, "text/javascript");
		toCleanOnException.add(clientPayloadURLDecompress);
		doUpdateMessage(cb, "Launching client...");
		BootMenuMain.runLaterMS(() -> {
			HTMLDocument docTemp = BootMenuMain.doc;
			launchOpts.put("container", BootMenuMain.createRootElementForClient());
			final String launchOptsStr = RelayRandomizeHelper.replaceRelayMacroWithConstant(launchOpts.toString());
			setupSignedClientOpts(clientSigURL.toExternalForm(), clientPayloadURL.toExternalForm(), launchOptsStr, blockUnsigned);
			HTMLScriptElement scriptElement = (HTMLScriptElement)docTemp.createElement("script");
			scriptElement.setType("text/javascript");
			scriptElement.setSrc(clientPayloadURLDecompress.toExternalForm());
			docTemp.getHead().appendChild(scriptElement);
			BootMenuMain.stopEventLoop();
		}, 250);
	}

	private static void bootClientEagler15New(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<EaglercraftUUID, byte[]> loadingCacheBytes,
			EaglerLoadingCache<UUIDStringPair, TeaVMBlobURLHandle> loadingCache, IProgressMsgCallback cb) {
		boolean blockUnsigned = IBootMenuConfigAdapter.instance.isBootMenuBlocksUnsignedClients();
		if(blockUnsigned) {
			throw new UnsignedBootException();
		}
		if(clientData.epkFiles.size() != 1) {
			throw new UnsupportedOperationException("Wrong number of EPK files: " + clientData.epkFiles.size());
		}
		JSONObject launchOpts = new JSONObject(launchConf.launchOpts);
		doUpdateMessage(cb, "Resolving classes.js (" + clientData.mainPayload + ")");
		TeaVMBlobURLHandle classesJSURL = loadingCache.get(new UUIDStringPair(clientData.mainPayload, "text/javascript"));
		List<TeaVMBlobURLHandle> toCleanOnException = new ArrayList<>();
		if(classesJSURL == null) {
			String msg = "Could not resolve classes.js! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		toCleanOnException.add(classesJSURL);
		doUpdateMessage(cb, "Resolving classes_server.js (" + clientData.integratedServer + ")");
		byte[] classesServerJS = loadingCacheBytes.get(clientData.integratedServer);
		if(classesServerJS == null) {
			for(TeaVMBlobURLHandle url : toCleanOnException) {
				try {
					TeaVMBlobURLManager.releaseURL(url);
				}catch(Throwable t) {
				}
			}
			String msg = "Could not resolve classes_server.js! (" + clientData.integratedServer + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		classesServerJS = OfflineDownloadFactory.removeUseStrict(classesServerJS);
		byte[] appendToClassesServerJS = "\"use strict\";var eaglercraftServerOpts;onmessage = function(o) { eaglercraftServerOpts = o.data; main(); };".getBytes(StandardCharsets.UTF_8);
		byte[] concatClassesServerJS = new byte[classesServerJS.length + appendToClassesServerJS.length];
		System.arraycopy(appendToClassesServerJS, 0, concatClassesServerJS, 0, appendToClassesServerJS.length);
		System.arraycopy(classesServerJS, 0, concatClassesServerJS, appendToClassesServerJS.length, classesServerJS.length);
		TeaVMBlobURLHandle classesServerJSURL = TeaVMBlobURLManager.registerNewURLByte(concatClassesServerJS, "text/javascript");
		toCleanOnException.add(classesServerJSURL);
		launchOpts.put("serverWorkerURI", classesServerJSURL);
		doUpdateMessage(cb, "Resolving assets.epk (" + clientData.epkFiles.get(0).dataUUID + ")");
		TeaVMBlobURLHandle assetsEPKURL = loadingCache.get(new UUIDStringPair(clientData.epkFiles.get(0).dataUUID, "application/octet-stream"));
		if(assetsEPKURL == null) {
			for(TeaVMBlobURLHandle url : toCleanOnException) {
				try {
					TeaVMBlobURLManager.releaseURL(url);
				}catch(Throwable t) {
				}
			}
			String msg = "Could not resolve assets.epk! (" + clientData.epkFiles.get(0).dataUUID + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		toCleanOnException.add(assetsEPKURL);
		launchOpts.put("assetsURI", assetsEPKURL.toExternalForm());
		doUpdateMessage(cb, "Launching client...");
		BootMenuMain.runLaterMS(() -> {
			clearMain("main");
			HTMLDocument docTemp = BootMenuMain.doc;
			launchOpts.put("container", BootMenuMain.createRootElementForClient());
			final String launchOptsStr = RelayRandomizeHelper.replaceRelayMacroWithConstant(launchOpts.toString());
			HTMLScriptElement scriptElement = (HTMLScriptElement)docTemp.createElement("script");
			scriptElement.addEventListener("load", (evt) -> {
				BootMenuMain.runLater(() -> {
					while(!isMainReady("main")) {
						logger.error("main function is not available yet! waiting 250ms...");
						EagUtils.sleep(250l);
					}
					BootMenuMain.stopEventLoop();
					callMain("eaglercraftOpts", launchOptsStr, "main", blockUnsigned);
				});
			});
			scriptElement.setType("text/javascript");
			scriptElement.setSrc(classesJSURL.toExternalForm());
			docTemp.getHead().appendChild(scriptElement);
		}, 250);
	}

	private static void bootClientEagler15Old(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<UUIDStringPair, TeaVMBlobURLHandle> loadingCache, IProgressMsgCallback cb) {
		boolean blockUnsigned = IBootMenuConfigAdapter.instance.isBootMenuBlocksUnsignedClients();
		if(blockUnsigned) {
			throw new UnsignedBootException();
		}
		final String b64Opts = translateNBTOpts(launchConf.launchOpts);
		if(clientData.epkFiles.size() != 1) {
			throw new UnsupportedOperationException("Wrong number of EPK files: " + clientData.epkFiles.size());
		}
		doUpdateMessage(cb, "Resolving classes.js (" + clientData.mainPayload + ")");
		final TeaVMBlobURLHandle classesJSURL = loadingCache.get(new UUIDStringPair(clientData.mainPayload, "text/javascript"));
		List<TeaVMBlobURLHandle> toCleanOnException = new ArrayList<>();
		if(classesJSURL == null) {
			String msg = "Could not resolve classes.js! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		toCleanOnException.add(classesJSURL);
		doUpdateMessage(cb, "Resolving assets.epk (" + clientData.epkFiles.get(0) + ")");
		final TeaVMBlobURLHandle assetsEPKURL = loadingCache.get(new UUIDStringPair(clientData.epkFiles.get(0).dataUUID, "application/octet-stream"));
		if(assetsEPKURL == null) {
			for(TeaVMBlobURLHandle url : toCleanOnException) {
				try {
					TeaVMBlobURLManager.releaseURL(url);
				}catch(Throwable t) {
				}
			}
			String msg = "Could not resolve assets.epk! (" + clientData.epkFiles.get(0).dataUUID + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		toCleanOnException.add(assetsEPKURL);
		doUpdateMessage(cb, "Launching client...");
		BootMenuMain.runLaterMS(() -> {
			clearMain("main");
			HTMLDocument docTemp = BootMenuMain.doc;
			final String container = BootMenuMain.createRootElementForClient();
			HTMLScriptElement scriptElement = (HTMLScriptElement)docTemp.createElement("script");
			scriptElement.addEventListener("load", (evt) -> {
				BootMenuMain.runLater(() -> {
					while(!isMainReady("main")) {
						logger.error("main function is not available yet! waiting 250ms...");
						EagUtils.sleep(250l);
					}
					BootMenuMain.stopEventLoop();
					callMainOld15("minecraftOpts", container, assetsEPKURL.toExternalForm(), b64Opts, launchConf.joinServer, "main", blockUnsigned);
				});
			});
			scriptElement.setType("text/javascript");
			scriptElement.setSrc(classesJSURL.toExternalForm());
			docTemp.getHead().appendChild(scriptElement);
		}, 250);
	}

	static String translateNBTOpts(String opts) {
		opts = opts.trim();
		if(!opts.startsWith("[NBT]") || !opts.endsWith("[/NBT]")) {
			throw new IllegalArgumentException("Eaglercraft opts are not in NBT format!");
		}
		NBTTagCompound readTag;
		try {
			readTag = JsonToNBT.getTagFromJson(opts.substring(5, opts.length() - 6).trim());
		} catch (NBTException e) {
			throw new IllegalArgumentException("Eaglercraft opts are invalid: " + e.getMessage());
		}
		EaglerOutputStream bao = new EaglerOutputStream(256);
		try {
			CompressedStreamTools.write(readTag, new DataOutputStream(bao));
		} catch (IOException e) {
			throw new RuntimeException("Could not write NBT tag compound!");
		}
		return Base64.encodeBase64String(bao.toByteArray());
	}

	private static void bootClientEaglerB13(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<UUIDStringPair, TeaVMBlobURLHandle> loadingCache, IProgressMsgCallback cb) {
		boolean blockUnsigned = IBootMenuConfigAdapter.instance.isBootMenuBlocksUnsignedClients();
		if(blockUnsigned) {
			throw new UnsignedBootException();
		}
		if(clientData.epkFiles.size() != 1) {
			throw new UnsupportedOperationException("Wrong number of EPK files: " + clientData.epkFiles.size());
		}
		doUpdateMessage(cb, "Resolving classes.js (" + clientData.mainPayload + ")");
		final TeaVMBlobURLHandle classesJSURL = loadingCache.get(new UUIDStringPair(clientData.mainPayload, "text/javascript"));
		List<TeaVMBlobURLHandle> toCleanOnException = new ArrayList<>();
		if(classesJSURL == null) {
			String msg = "Could not resolve classes.js! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		toCleanOnException.add(classesJSURL);
		doUpdateMessage(cb, "Resolving assets.epk (" + clientData.epkFiles.get(0) + ")");
		final TeaVMBlobURLHandle assetsEPKURL = loadingCache.get(new UUIDStringPair(clientData.epkFiles.get(0).dataUUID, "application/octet-stream"));
		if(assetsEPKURL == null) {
			for(TeaVMBlobURLHandle url : toCleanOnException) {
				try {
					TeaVMBlobURLManager.releaseURL(url);
				}catch(Throwable t) {
				}
			}
			String msg = "Could not resolve assets.epk! (" + clientData.epkFiles.get(0).dataUUID + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		toCleanOnException.add(assetsEPKURL);
		doUpdateMessage(cb, "Launching client...");
		BootMenuMain.runLaterMS(() -> {
			clearMain("main");
			HTMLDocument docTemp = BootMenuMain.doc;
			final String container = BootMenuMain.createRootElementForClient();
			HTMLScriptElement scriptElement = (HTMLScriptElement)docTemp.createElement("script");
			scriptElement.addEventListener("load", (evt) -> {
				BootMenuMain.runLater(() -> {
					while(!isMainReady("main")) {
						logger.error("main function is not available yet! waiting 250ms...");
						EagUtils.sleep(250l);
					}
					BootMenuMain.stopEventLoop();
					callMainOldB13("minecraftOpts", container, assetsEPKURL.toExternalForm(), launchConf.joinServer, "main", blockUnsigned);
				});
			});
			scriptElement.setType("text/javascript");
			scriptElement.setSrc(classesJSURL.toExternalForm());
			docTemp.getHead().appendChild(scriptElement);
		}, 250);
	}

	private static void bootClientPeytonAlphaBeta(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<UUIDStringPair, TeaVMBlobURLHandle> loadingCache, IProgressMsgCallback cb) {
		boolean blockUnsigned = IBootMenuConfigAdapter.instance.isBootMenuBlocksUnsignedClients();
		if(blockUnsigned) {
			throw new UnsignedBootException();
		}
		if(clientData.epkFiles.size() != 1) {
			throw new UnsupportedOperationException("Wrong number of EPK files: " + clientData.epkFiles.size());
		}
		JSONObject launchOpts = new JSONObject(launchConf.launchOpts);
		doUpdateMessage(cb, "Resolving classes.js (" + clientData.mainPayload + ")");
		final TeaVMBlobURLHandle classesJSURL = loadingCache.get(new UUIDStringPair(clientData.mainPayload, "text/javascript"));
		List<TeaVMBlobURLHandle> toCleanOnException = new ArrayList<>();
		if(classesJSURL == null) {
			String msg = "Could not resolve classes.js! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		toCleanOnException.add(classesJSURL);
		doUpdateMessage(cb, "Resolving assets.epk (" + clientData.epkFiles.get(0) + ")");
		final TeaVMBlobURLHandle assetsEPKURL = loadingCache.get(new UUIDStringPair(clientData.epkFiles.get(0).dataUUID, "application/octet-stream"));
		if(assetsEPKURL == null) {
			for(TeaVMBlobURLHandle url : toCleanOnException) {
				try {
					TeaVMBlobURLManager.releaseURL(url);
				}catch(Throwable t) {
				}
			}
			String msg = "Could not resolve assets.epk! (" + clientData.epkFiles.get(0).dataUUID + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		toCleanOnException.add(assetsEPKURL);
		launchOpts.put("assetsLocation", assetsEPKURL.toExternalForm());
		doUpdateMessage(cb, "Launching client...");
		BootMenuMain.runLaterMS(() -> {
			clearMain("main");
			HTMLDocument docTemp = BootMenuMain.doc;
			launchOpts.put("gameContainer", BootMenuMain.createRootElementForClient());
			final String launchOptsStr = launchOpts.toString();
			HTMLScriptElement scriptElement = (HTMLScriptElement)docTemp.createElement("script");
			scriptElement.addEventListener("load", (evt) -> {
				BootMenuMain.runLater(() -> {
					while(!isMainReady("main")) {
						logger.error("main function is not available yet! waiting 250ms...");
						EagUtils.sleep(250l);
					}
					BootMenuMain.stopEventLoop();
					callMain("config", launchOptsStr, "main", blockUnsigned);
				});
			});
			scriptElement.setType("text/javascript");
			scriptElement.setSrc(classesJSURL.toExternalForm());
			docTemp.getHead().appendChild(scriptElement);
		}, 250);
	}

	private static void bootClientPeytonIndev(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<UUIDStringPair, TeaVMBlobURLHandle> loadingCache, IProgressMsgCallback cb) {
		boolean blockUnsigned = IBootMenuConfigAdapter.instance.isBootMenuBlocksUnsignedClients();
		if(blockUnsigned) {
			throw new UnsignedBootException();
		}
		if(clientData.epkFiles.size() != 1) {
			throw new UnsupportedOperationException("Wrong number of EPK files: " + clientData.epkFiles.size());
		}
		doUpdateMessage(cb, "Resolving classes.js (" + clientData.mainPayload + ")");
		final TeaVMBlobURLHandle classesJSURL = loadingCache.get(new UUIDStringPair(clientData.mainPayload, "text/javascript"));
		List<TeaVMBlobURLHandle> toCleanOnException = new ArrayList<>();
		if(classesJSURL == null) {
			String msg = "Could not resolve classes.js! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		toCleanOnException.add(classesJSURL);
		doUpdateMessage(cb, "Resolving assets.epk (" + clientData.epkFiles.get(0) + ")");
		final TeaVMBlobURLHandle assetsEPKURL = loadingCache.get(new UUIDStringPair(clientData.epkFiles.get(0).dataUUID, "application/octet-stream"));
		if(assetsEPKURL == null) {
			for(TeaVMBlobURLHandle url : toCleanOnException) {
				try {
					TeaVMBlobURLManager.releaseURL(url);
				}catch(Throwable t) {
				}
			}
			String msg = "Could not resolve assets.epk! (" + clientData.epkFiles.get(0).dataUUID + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		toCleanOnException.add(assetsEPKURL);
		doUpdateMessage(cb, "Launching client...");
		BootMenuMain.runLaterMS(() -> {
			clearMain("main");
			HTMLDocument docTemp = BootMenuMain.doc;
			final String container = BootMenuMain.createRootElementForClient();
			HTMLScriptElement scriptElement = (HTMLScriptElement)docTemp.createElement("script");
			scriptElement.addEventListener("load", (evt) -> {
				BootMenuMain.runLater(() -> {
					while(!isMainReady("main")) {
						logger.error("main function is not available yet! waiting 250ms...");
						EagUtils.sleep(250l);
					}
					BootMenuMain.stopEventLoop();
					callMainOldB13("classicConfig", container, assetsEPKURL.toExternalForm(), "", "main", blockUnsigned);
				});
			});
			scriptElement.setType("text/javascript");
			scriptElement.setSrc(classesJSURL.toExternalForm());
			docTemp.getHead().appendChild(scriptElement);
		}, 250);
	}

	private static void bootClientStandardOffline(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			EaglerLoadingCache<UUIDStringPair, TeaVMBlobURLHandle> loadingCache, IProgressMsgCallback cb) {
		boolean blockUnsigned = IBootMenuConfigAdapter.instance.isBootMenuBlocksUnsignedClients();
		if(blockUnsigned) {
			throw new UnsignedBootException();
		}
		JSONObject launchOpts = new JSONObject(launchConf.launchOpts);
		doUpdateMessage(cb, "Resolving classes.js (" + clientData.mainPayload + ")");
		TeaVMBlobURLHandle classesJSURL = loadingCache.get(new UUIDStringPair(clientData.mainPayload, "text/javascript"));
		List<TeaVMBlobURLHandle> toCleanOnException = new ArrayList<>();
		if(classesJSURL == null) {
			String msg = "Could not resolve classes.js! (" + clientData.mainPayload + ")";
			logger.error(msg);
			throw new NullPointerException(msg);
		}
		toCleanOnException.add(classesJSURL);
		JSONArray epkFiles = new JSONArray();
		for(EPKDataEntry etr : clientData.epkFiles) {
			doUpdateMessage(cb, "Resolving assets.epk (" + etr.dataUUID + ", path: /" + etr.extractTo + ")");
			TeaVMBlobURLHandle epkURL = loadingCache.get(new UUIDStringPair(etr.dataUUID, "application/octet-stream"));
			if(epkURL == null) {
				for(TeaVMBlobURLHandle url : toCleanOnException) {
					try {
						TeaVMBlobURLManager.releaseURL(url);
					}catch(Throwable t) {
					}
				}
				String msg = "Could not resolve assets.epk! (" + etr.dataUUID + ", path: /" + etr.extractTo + ")";
				logger.error(msg);
				throw new NullPointerException(msg);
			}
			toCleanOnException.add(epkURL);
			JSONObject epkEntry = new JSONObject();
			epkEntry.put("url", epkURL.toExternalForm());
			epkEntry.put("path", etr.extractTo);
			epkFiles.put(epkEntry);
		}
		launchOpts.put(launchConf.launchOptsAssetsURIVar, epkFiles);
		doUpdateMessage(cb, "Launching client...");
		BootMenuMain.runLaterMS(() -> {
			clearMain("main");
			HTMLDocument docTemp = BootMenuMain.doc;
			launchOpts.put(launchConf.launchOptsContainerVar, BootMenuMain.createRootElementForClient());
			final String launchOptsStr = RelayRandomizeHelper.replaceRelayMacroWithConstant(launchOpts.toString());
			HTMLScriptElement scriptElement = (HTMLScriptElement)docTemp.createElement("script");
			scriptElement.addEventListener("load", (evt) -> {
				BootMenuMain.runLater(() -> {
					while(!isMainReady("main")) {
						logger.error("main function is not available yet! waiting 250ms...");
						EagUtils.sleep(250l);
					}
					BootMenuMain.stopEventLoop();
					callMain(launchConf.launchOptsVar, launchOptsStr, launchConf.mainFunction, blockUnsigned);
				});
			});
			scriptElement.setType("text/javascript");
			scriptElement.setSrc(classesJSURL.toExternalForm());
			docTemp.getHead().appendChild(scriptElement);
		}, 250);
	}

}
