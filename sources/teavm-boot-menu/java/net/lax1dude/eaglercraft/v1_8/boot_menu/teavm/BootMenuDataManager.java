package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.teavm.jso.browser.Storage;
import org.teavm.jso.browser.Window;

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
public class BootMenuDataManager {

	protected static final Logger logger = LogManager.getLogger("BootMenuDataManager");

	protected BootMenuDatastore datastore;

	public final Set<EaglercraftUUID> existingBlobs = new HashSet<>();
	public final Map<EaglercraftUUID,ClientDataEntry> clientDatas = new HashMap<>();
	public final Map<EaglercraftUUID,LaunchConfigEntry> launchDatas = new HashMap<>();
	public final List<EaglercraftUUID> launchDatasList = new ArrayList<>();
	public final List<EaglercraftUUID> launchOrderList = new ArrayList<>();

	public int confBootTimeout = 0;
	public String confMenuTitle = "EaglercraftX 1.8 Boot Manager";

	public BootMenuDataManager(BootMenuDatastore datastore) {
		this.datastore = datastore;
		this.loadAllData();
		this.loadAdditionalConf();
	}

	public void installNewClientData(LaunchConfigEntry launchConfig, ClientDataEntry clientData, Map<EaglercraftUUID,byte[]> clientBlobs, boolean rotateUUIDs) {
		if(rotateUUIDs) {
			EaglercraftUUID rotatedLaunchUUID = EaglercraftUUID.randomUUID();
			EaglercraftUUID rotatedClientUUID = EaglercraftUUID.randomUUID();
			launchConfig = launchConfig.rotateUUIDs(rotatedLaunchUUID, rotatedClientUUID);
			clientData = clientData.rotateUUID(rotatedClientUUID);
		}
		if(launchDatas.containsKey(launchConfig.uuid) || BootMenuConstants.UUID_CLIENT_LAUNCH_ORIGIN.equals(launchConfig.uuid)) {
			throw new IllegalArgumentException("Launch data UUID \"" + launchConfig.uuid + "\" already exists!");
		}
		if(clientDatas.containsKey(clientData.uuid) || BootMenuConstants.UUID_CLIENT_DATA_ORIGIN.equals(launchConfig.uuid)) {
			throw new IllegalArgumentException("Client data UUID \"" + clientData.uuid + "\" already exists!");
		}
		if(!launchConfig.clientDataUUID.equals(clientData.uuid)) {
			throw new IllegalArgumentException("Mismatched client data UUID and launch configuration!");
		}
		logger.info("Installing new client data \"{}\"...", clientData.uuid);
		if(clientBlobs != null && !clientBlobs.isEmpty()) {
			for(Entry<EaglercraftUUID,byte[]> etr : clientBlobs.entrySet()) {
				EaglercraftUUID k = etr.getKey();
				byte[] v = etr.getValue();
				String name = "blobs/" + k;
				if(!datastore.containsKey(name)) {
					logger.info(" - Adding blob to datastore \"{}\" ({} bytes long)", name, v.length);
					datastore.setItem(name, v);
					existingBlobs.add(k);
				}else {
					logger.info(" - Skipping blob \"{}\" because it already exists", name);
				}
			}
		}
		String name = "clientDatas/" + clientData.uuid;
		logger.info(" - Writing client data: \"{}\"", name);
		JSONObject obj = new JSONObject();
		clientData.writeJSON(obj);
		datastore.setItem(name, obj.toString().getBytes(StandardCharsets.UTF_8));
		clientDatas.put(clientData.uuid, clientData);
		installNewLaunchConfig(launchConfig, false);
	}

	public void installNewLaunchConfig(LaunchConfigEntry launchConfig, ClientDataEntry clientData, Map<EaglercraftUUID,byte[]> clientBlobs, boolean rotateUUIDs) {
		if(rotateUUIDs) {
			EaglercraftUUID rotatedLaunchUUID = EaglercraftUUID.randomUUID();
			launchConfig = launchConfig.rotateUUIDs(rotatedLaunchUUID, launchConfig.clientDataUUID);
		}
		if(BootMenuConstants.UUID_CLIENT_LAUNCH_ORIGIN.equals(launchConfig.uuid)) {
			throw new IllegalArgumentException("The origin launch configuration cannot be overwritten!");
		}
		if(!clientDatas.containsKey(launchConfig.clientDataUUID) && !BootMenuConstants.UUID_CLIENT_DATA_ORIGIN.equals(launchConfig.clientDataUUID)) {
			logger.info("Installing new client data \"{}\"...", clientData.uuid);
			if(clientBlobs != null && !clientBlobs.isEmpty()) {
				for(Entry<EaglercraftUUID,byte[]> etr : clientBlobs.entrySet()) {
					EaglercraftUUID k = etr.getKey();
					byte[] v = etr.getValue();
					String name = "blobs/" + k;
					if(!datastore.containsKey(name)) {
						logger.info(" - Adding blob to datastore \"{}\" ({} bytes long)", name, v.length);
						datastore.setItem(name, v);
						existingBlobs.add(k);
					}else {
						logger.info(" - Skipping blob \"{}\" because it already exists", name);
					}
				}
			}
			String name = "clientDatas/" + clientData.uuid;
			logger.info(" - Writing client data: \"{}\"", name);
			JSONObject obj = new JSONObject();
			clientData.writeJSON(obj);
			datastore.setItem(name, obj.toString().getBytes(StandardCharsets.UTF_8));
			clientDatas.put(clientData.uuid, clientData);
		}
		logger.info("Installing new launch config \"{}\"...", launchConfig.uuid);
		String name = "launchDatas/" + launchConfig.uuid;
		JSONObject obj = new JSONObject();
		launchConfig.writeJSON(obj);
		datastore.setItem(name, obj.toString().getBytes(StandardCharsets.UTF_8));
		boolean writeManifest = false;
		if(launchDatas.put(launchConfig.uuid, launchConfig) == null) {
			launchDatasList.add(launchConfig.uuid);
			writeManifest = true;
		}
		if(!launchOrderList.contains(launchConfig.uuid)) {
			launchOrderList.add(launchConfig.uuid);
			writeManifest = true;
		}
		if(writeManifest) {
			writeManifest();
		}
	}

	public void installNewLaunchConfig(LaunchConfigEntry launchConfig, boolean rotateUUIDs) {
		if(rotateUUIDs) {
			EaglercraftUUID rotatedLaunchUUID = EaglercraftUUID.randomUUID();
			launchConfig = launchConfig.rotateUUIDs(rotatedLaunchUUID, launchConfig.clientDataUUID);
		}
		if(BootMenuConstants.UUID_CLIENT_LAUNCH_ORIGIN.equals(launchConfig.uuid)) {
			throw new IllegalArgumentException("The origin launch configuration cannot be overwritten!");
		}
		if(!clientDatas.containsKey(launchConfig.clientDataUUID) && !BootMenuConstants.UUID_CLIENT_DATA_ORIGIN.equals(launchConfig.uuid)) {
			throw new IllegalArgumentException("Client data UUID \"" + launchConfig.clientDataUUID + "\" does not exist!");
		}
		logger.info("Installing new launch config \"{}\"...", launchConfig.uuid);
		String name = "launchDatas/" + launchConfig.uuid;
		JSONObject obj = new JSONObject();
		launchConfig.writeJSON(obj);
		datastore.setItem(name, obj.toString().getBytes(StandardCharsets.UTF_8));
		boolean writeManifest = false;
		if(launchDatas.put(launchConfig.uuid, launchConfig) == null) {
			launchDatasList.add(launchConfig.uuid);
			writeManifest = true;
		}
		if(!launchOrderList.contains(launchConfig.uuid)) {
			launchOrderList.add(launchConfig.uuid);
			writeManifest = true;
		}
		if(writeManifest) {
			writeManifest();
		}
	}

	public void deleteLaunchConfig(EaglercraftUUID launchConfig) {
		if(launchDatas.remove(launchConfig) != null) {
			boolean removed = false;
			while(launchDatasList.remove(launchConfig)) {
				removed = true;
			}
			while(launchOrderList.remove(launchConfig)) {
				removed = true;
			}
			String name = "launchDatas/" + launchConfig;
			logger.info("Deleting launch config \"{}\" from datastore", name);
			if(!datastore.deleteItem(name)) {
				logger.warn("Failed to delete file! Removing it from the list anyway...");
			}
			if(removed) {
				writeManifest();
			}
			garbageCollectClientDatas();
		}
	}

	protected void loadAllData() {
		logger.info("Loading custom boot configurations from datastore...");
		existingBlobs.clear();
		clientDatas.clear();
		launchDatas.clear();
		launchDatasList.clear();
		launchOrderList.clear();
		byte[] manifestBytes = datastore.getItem("manifest.json");
		if(manifestBytes == null) {
			return;
		}
		List<EaglercraftUUID> profilesToLoad;
		try {
			JSONArray arr = (new JSONObject(new String(manifestBytes, StandardCharsets.UTF_8))).getJSONArray("launchProfiles");
			profilesToLoad = new ArrayList<>(arr.length());
			for(int i = 0, l = arr.length(); i < l; ++i) {
				profilesToLoad.add(EaglercraftUUID.fromString(arr.getString(i)));
			}
			arr = (new JSONObject(new String(manifestBytes, StandardCharsets.UTF_8))).getJSONArray("launchOrderList");
			for(int i = 0, l = arr.length(); i < l; ++i) {
				launchOrderList.add(EaglercraftUUID.fromString(arr.getString(i)));
			}
		}catch(JSONException | IllegalArgumentException exp) {
			logger.error("Manifest is corrupt!");
			logger.error(exp);
			return;
		}
		for(EaglercraftUUID uuid : profilesToLoad) {
			if(loadLaunchDataFromStore(uuid) != null) {
				launchDatasList.add(uuid);
			}
		}
		logger.info("Loading {} client(s) successfully", clientDatas.size());
		logger.info("Loading {} profile(s) successfully", launchDatas.size());
	}

	protected LaunchConfigEntry loadLaunchDataFromStore(EaglercraftUUID uuid) {
		LaunchConfigEntry ret = launchDatas.get(uuid);
		if(ret != null) {
			return ret;
		}
		String name = "launchDatas/" + uuid;
		byte[] fileData = datastore.getItem(name);
		if(fileData == null) {
			logger.error("Could not locate launch data \"{}\"!", name);
			return null;
		}
		try {
			ret = new LaunchConfigEntry(uuid, new JSONObject(new String(fileData, StandardCharsets.UTF_8)));
		}catch(JSONException | IllegalArgumentException exp) {
			logger.error("Launch data \"{}\" is corrupt!", name);
			logger.error(exp);
			return null;
		}
		if(!BootMenuConstants.UUID_CLIENT_DATA_ORIGIN.equals(ret.clientDataUUID)) {
			if(loadClientDataFromStore(ret.clientDataUUID) == null) {
				logger.error("Client data \"{}\" for launch data \"{}\" is missing/corrupt!", ret.clientDataUUID, name);
				return null;
			}
		}
		launchDatas.put(uuid, ret);
		return ret;
	}

	protected ClientDataEntry loadClientDataFromStore(EaglercraftUUID uuid) {
		ClientDataEntry ret = clientDatas.get(uuid);
		if(ret != null) {
			return ret;
		}
		String name = "clientDatas/" + uuid;
		byte[] fileData = datastore.getItem(name);
		if(fileData == null) {
			logger.error("Could not locate client data \"{}\"!", name);
			return null;
		}
		try {
			ret = new ClientDataEntry(uuid, new JSONObject(new String(fileData, StandardCharsets.UTF_8)));
		}catch(JSONException | IllegalArgumentException exp) {
			logger.error("Client data \"{}\" is corrupt!", name);
			logger.error(exp);
			return null;
		}
		existingBlobs.addAll(ret.getReferencedBlobs());
		clientDatas.put(uuid, ret);
		return ret;
	}

	public void writeManifest() {
		JSONObject manifest = new JSONObject();
		JSONArray launchProfileArray = new JSONArray();
		for(EaglercraftUUID uuid : launchDatasList) {
			launchProfileArray.put(uuid.toString());
		}
		manifest.put("launchProfiles", launchProfileArray);
		JSONArray launchOrderListArray = new JSONArray();
		for(EaglercraftUUID uuid : launchOrderList) {
			launchOrderListArray.put(uuid.toString());
		}
		manifest.put("launchOrderList", launchOrderListArray);
		datastore.setItem("manifest.json", manifest.toString().getBytes(StandardCharsets.UTF_8));
	}

	protected void garbageCollectClientDatas() {
		Set<EaglercraftUUID> referencedClientData = new HashSet<>();
		for(LaunchConfigEntry etr : launchDatas.values()) {
			referencedClientData.add(etr.clientDataUUID);
		}
		Set<EaglercraftUUID> toDelete = new HashSet<>(clientDatas.keySet());
		toDelete.removeAll(referencedClientData);
		boolean garbageCollectBlobs = !toDelete.isEmpty();
		if(garbageCollectBlobs) {
			for(EaglercraftUUID del : toDelete) {
				clientDatas.remove(del);
				String name = "clientDatas/" + del;
				logger.info("Deleting orphaned client \"{}\" from datastore", name);
				datastore.deleteItem(name);
			}
			garbageCollectClientBlobs();
		}
	}

	protected void garbageCollectClientBlobs() {
		Set<EaglercraftUUID> referencedClientBlob = new HashSet<>();
		for(ClientDataEntry etr : clientDatas.values()) {
			referencedClientBlob.addAll(etr.getReferencedBlobs());
		}
		Set<EaglercraftUUID> toDelete = new HashSet<>(existingBlobs);
		toDelete.removeAll(referencedClientBlob);
		if(!toDelete.isEmpty()) {
			for(EaglercraftUUID del : toDelete) {
				existingBlobs.remove(del);
				String name = "blobs/" + del;
				logger.info("Deleting orphaned blob \"{}\" from datastore", name);
				if(!datastore.deleteItem(name)) {
					logger.warn("Failed to delete file! Skipping...");
				}
			}
		}
	}

	public void garbageCollectAll() {
		logger.info("Garbage-collecting boot menu data store");
		Set<EaglercraftUUID> existingClients = new HashSet<>();
		Set<EaglercraftUUID> existingLaunches = new HashSet<>();
		Set<EaglercraftUUID> existingBlobs = new HashSet<>();
		Set<String> orphanedFiles = new HashSet<>();
		datastore.iterateAllKeys((str) -> {
			if(str.startsWith("clientDatas/")) {
				try {
					existingClients.add(EaglercraftUUID.fromString(str.substring(12)));
				}catch(IllegalArgumentException ex) {
					orphanedFiles.add(str);
				}
			}else if(str.startsWith("launchDatas/")) {
				try {
					existingLaunches.add(EaglercraftUUID.fromString(str.substring(12)));
				}catch(IllegalArgumentException ex) {
					orphanedFiles.add(str);
				}
			}else if(str.startsWith("blobs/")) {
				try {
					existingBlobs.add(EaglercraftUUID.fromString(str.substring(6)));
				}catch(IllegalArgumentException ex) {
					orphanedFiles.add(str);
				}
			}
		});
		Set<EaglercraftUUID> toDelete = new HashSet<>(existingLaunches);
		toDelete.removeAll(launchDatas.keySet());
		if(!toDelete.isEmpty()) {
			for(EaglercraftUUID del : toDelete) {
				String name = "launchDatas/" + del;
				logger.info("Deleting orphaned launch \"{}\" from datastore", name);
				if(!datastore.deleteItem(name)) {
					logger.warn("Failed to delete file! Skipping...");
				}
			}
		}
		Set<EaglercraftUUID> referencedData = new HashSet<>();
		for(LaunchConfigEntry etr : launchDatas.values()) {
			referencedData.add(etr.clientDataUUID);
		}
		toDelete = new HashSet<>(existingClients);
		toDelete.removeAll(referencedData);
		if(!toDelete.isEmpty()) {
			for(EaglercraftUUID del : toDelete) {
				clientDatas.remove(del);
				String name = "clientDatas/" + del;
				logger.info("Deleting orphaned client \"{}\" from datastore", name);
				if(!datastore.deleteItem(name)) {
					logger.warn("Failed to delete file! Skipping...");
				}
			}
		}
		referencedData.clear();
		for(ClientDataEntry etr : clientDatas.values()) {
			referencedData.addAll(etr.getReferencedBlobs());
		}
		toDelete.clear();
		toDelete.addAll(existingBlobs);
		toDelete.removeAll(referencedData);
		if(!toDelete.isEmpty()) {
			for(EaglercraftUUID del : toDelete) {
				existingBlobs.remove(del);
				String name = "blobs/" + del;
				logger.info("Deleting orphaned blob \"{}\" from datastore", name);
				if(!datastore.deleteItem(name)) {
					logger.warn("Failed to delete file! Skipping...");
				}
			}
		}
	}

	public void loadAdditionalConf() {
		logger.info("Loading config.json");
		byte[] dat = datastore.getItem("config.json");
		if(dat != null) {
			try {
				JSONObject obj = new JSONObject(new String(dat, StandardCharsets.UTF_8));
				confBootTimeout = obj.getInt("confBootTimeout");
				confMenuTitle = obj.getString("confMenuTitle");
			}catch(JSONException ex) {
				logger.error("Invalid config.json!");
				logger.error(ex);
			}
		}
	}

	public void saveAdditionalConf() {
		JSONObject confJSON = new JSONObject();
		confJSON.put("confBootTimeout", confBootTimeout);
		confJSON.put("confMenuTitle", confMenuTitle);
		byte[] confBytes = confJSON.toString().getBytes(StandardCharsets.UTF_8);
		datastore.setItem("config.json", confBytes);
	}

	public static int getBootMenuFlags(Window win) {
		try {
			Storage stor = win.getLocalStorage();
			if(stor != null) {
				String itm = stor.getItem(BootMenuConstants.getBootMenuFlagsKeyName());
				if(itm != null) {
					return Integer.parseInt(itm);
				}
			}
		}catch(Throwable t) {
		}
		return -1;
	}

	public static void setBootMenuFlags(Window win, int flags) {
		try {
			Storage stor = win.getLocalStorage();
			if(stor != null) {
				String key = BootMenuConstants.getBootMenuFlagsKeyName();
				if(flags != -1) {
					stor.setItem(key, Integer.toString(flags));
				}else {
					stor.removeItem(key);
				}
			}
		}catch(Throwable t) {
		}
	}

}
