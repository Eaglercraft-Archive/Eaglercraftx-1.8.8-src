package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.boot_menu.teavm.OfflineDownloadParser.ParsedOfflineAdapter;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.server.export.EPKDecompiler;

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
public class EPKClientParser {

	private static final Logger logger = LogManager.getLogger("EPKClientParser");

	public static List<ParsedOfflineAdapter> parseEPKClient(byte[] epkData) throws IOException {
		EPKDecompiler epkDecompiler = new EPKDecompiler(epkData);
		EPKDecompiler.FileEntry fetr = epkDecompiler.readFile();
		if(fetr == null || !fetr.type.equals("HEAD") || !fetr.name.equals("file-type")) {
			epkDecompiler.close();
			throw new IOException("File is incomplete!");
		}
		if(!Arrays.equals(fetr.data, "epk/client-archive-v1".getBytes(StandardCharsets.UTF_8))) {
			throw new IOException("File is not a client archive!");
		}
		Map<String,byte[]> files = new HashMap<>();
		while((fetr = epkDecompiler.readFile()) != null) {
			if(fetr.type.equals("FILE")) {
				files.put(fetr.name, fetr.data);
			}else {
				logger.error("Skipping non-FILE entry: {} {}", fetr.type, fetr.name);
			}
		}
		byte[] manifestData = files.get("manifest.json");
		if(manifestData == null) {
			throw new IOException("File is incomplete!");
		}
		List<EaglercraftUUID> lst = new ArrayList<>();
		Map<EaglercraftUUID,ClientDataEntry> clientDatas;
		List<LaunchConfigEntry> launchDatas;
		try {
			JSONObject mainfestJSON = new JSONObject(new String(manifestData, StandardCharsets.UTF_8));
			JSONArray launches = mainfestJSON.getJSONArray("launchData");
			JSONArray clients = mainfestJSON.getJSONArray("clientData");
			clientDatas = new HashMap<>(clients.length());
			launchDatas = new ArrayList<>(launches.length());
			for(int i = 0, l = clients.length(); i < l; ++i) {
				JSONObject obj = clients.getJSONObject(i);
				EaglercraftUUID theUUID = EaglercraftUUID.fromString(obj.getString("uuid"));
				if(!theUUID.equals(BootMenuConstants.UUID_CLIENT_DATA_ORIGIN)) {
					clientDatas.put(theUUID, new ClientDataEntry(theUUID, obj));
				}
			}
			for(int i = 0, l = launches.length(); i < l; ++i) {
				JSONObject obj = launches.getJSONObject(i);
				EaglercraftUUID theUUID = EaglercraftUUID.fromString(obj.getString("uuid"));
				if(!theUUID.equals(BootMenuConstants.UUID_CLIENT_LAUNCH_ORIGIN)) {
					LaunchConfigEntry theEtr = new LaunchConfigEntry(theUUID, obj);
					if(!BootMenuConstants.UUID_CLIENT_DATA_ORIGIN.equals(theEtr.clientDataUUID)) {
						if(clientDatas.containsKey(theEtr.clientDataUUID)) {
							launchDatas.add(theEtr);
						}else {
							logger.warn("Skipping launch config {} because the client data {} is missing!", theUUID, theEtr.clientDataUUID);
						}
					}
				}
			}
		}catch(JSONException ex) {
			throw new IOException("File manifest is corrupt!", ex);
		}
		Map<EaglercraftUUID, byte[]> blobs = new HashMap<>();
		Iterator<ClientDataEntry> itr = clientDatas.values().iterator();
		loadClientDatas: while(itr.hasNext()) {
			ClientDataEntry etr = itr.next();
			for(EaglercraftUUID uuid : etr.getReferencedBlobs()) {
				if(!blobs.containsKey(uuid)) {
					byte[] blobBytes = files.get(uuid.toString());
					if(blobBytes == null) {
						logger.error("Blob UUID {} for client data {} is missing!", uuid, etr.uuid);
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
			ClientDataEntry clientData = clientDatas.get(etr.clientDataUUID);
			if(clientData == null) {
				logger.error("Client data UUID {} for launch data {} is missing!", etr.clientDataUUID, etr.uuid);
				continue;
			}
			Map<EaglercraftUUID, byte[]> entryBlob = new HashMap<>();
			for(EaglercraftUUID uuid : clientData.getReferencedBlobs()) {
				entryBlob.put(uuid, blobs.get(uuid));
			}
			list.add(new ParsedOfflineAdapter(etr, clientData, entryBlob));
		}
		logger.info("Loaded {} blobs from fat offline", blobs.size());
		logger.info("Loaded {} client configurations from EPK file", clientDatas.size());
		logger.info("Loaded {} launch configurations from EPK file", launchDatas.size());
		return list;
	}

}
