package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.json.JSONArray;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.server.export.EPKCompiler;

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
public class EPKClientFactory {

	private static final Logger logger = LogManager.getLogger("EPKClientFactory");

	private static void doUpdateMessage(IProgressMsgCallback cb, String str) {
		logger.info(str);
		cb.updateMessage(str);
	}

	public static void downloadEPKClient(LaunchConfigEntry launchConf, ClientDataEntry clientData,
			Map<EaglercraftUUID, Supplier<byte[]>> loaders, IProgressMsgCallback cb) {
		doUpdateMessage(cb, "Generating manifest...");
		EaglercraftUUID randomLaunchUUID = EaglercraftUUID.randomUUID();
		EaglercraftUUID randomClientUUID = EaglercraftUUID.randomUUID();
		launchConf = launchConf.rotateUUIDs(randomLaunchUUID, randomClientUUID);
		clientData = clientData.rotateUUID(randomClientUUID);
		JSONArray launchDatas = new JSONArray();
		JSONObject launchObj = new JSONObject();
		launchConf.writeJSON(launchObj);
		launchDatas.put(launchObj);
		JSONArray clientDatas = new JSONArray();
		JSONObject clientObj = new JSONObject();
		clientData.writeJSON(clientObj);
		clientDatas.put(clientObj);
		JSONObject manifestObj = new JSONObject();
		manifestObj.put("launchData", launchDatas);
		manifestObj.put("clientData", clientDatas);
		byte[] manifestBytes = manifestObj.toString().getBytes(StandardCharsets.UTF_8);
		Map<String,byte[]> blobs = new HashMap<>();
		for(EaglercraftUUID uuid : clientData.getReferencedBlobs()) {
			String name = uuid.toString();
			doUpdateMessage(cb, "Resolving blobs (" + name + ")");
			if(!blobs.containsKey(name)) {
				Supplier<byte[]> loader = loaders.get(uuid);
				byte[] dat = null;
				if(loader != null) {
					dat = loader.get();
				}
				if(dat == null) {
					String msg = "Could not resolve blob! (" + name + ")";
					logger.error(msg);
					throw new NullPointerException(msg);
				}
				blobs.put(name, dat);
			}
		}
		doUpdateMessage(cb, "Compressing EPK file...");
		String fileName = launchConf.displayName.replaceAll("[^a-zA-Z0-9\\-_\\.]", "_");
		if(fileName.length() > 251) {
			fileName = fileName.substring(0, 251);
		}
		fileName = fileName + ".epk";
		EPKCompiler epkComp = new EPKCompiler(fileName, "unknown", "epk/client-archive-v1", true, false,
				"\n\n #  Eagler EPK v2.0 Client Archive v1\n #  Clients: \"" + launchConf.displayName + "\"\n\n");
		byte[] epkData;
		try {
			epkComp.append("manifest.json", manifestBytes);
			for(Entry<String,byte[]> blob : blobs.entrySet()) {
				epkComp.append(blob.getKey(), blob.getValue());
			}
		}finally {
			epkData = epkComp.complete();
		}
		doUpdateMessage(cb, "Downloading file...");
		EagRuntime.downloadFileWithName(fileName, epkData);
	}

}
