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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.teavm.jso.dom.html.HTMLElement;

import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

public class BootMenuFatOfflineLoader {

	protected static final Logger logger = LogManager.getLogger("BootMenuFatOfflineLoader");

	public final HTMLElement parentElement;

	public final Map<EaglercraftUUID,ClientDataEntry> clientDatas = new HashMap<>();
	public final List<LaunchConfigEntry> launchDatas = new ArrayList<>();

	public BootMenuFatOfflineLoader(HTMLElement parentElement) {
		this.parentElement = parentElement;
		this.loadAllData();
	}

	protected void loadAllData() {
		String manifest = loadDataString("manifest_v1");
		if(manifest != null) {
			JSONObject json = new JSONObject(manifest);
			JSONArray launches = json.getJSONArray("launchData");
			JSONArray clients = json.getJSONArray("clientData");
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
					if(clientDatas.containsKey(theEtr.clientDataUUID) || BootMenuConstants.UUID_CLIENT_DATA_ORIGIN.equals(theEtr.clientDataUUID)) {
						launchDatas.add(theEtr);
					}else {
						logger.warn("Skipping launch config {} because the client data {} is missing!", theUUID, theEtr.clientDataUUID);
					}
				}
			}
			logger.info("Loading {} client(s) successfully", clientDatas.size());
			logger.info("Loading {} profile(s) successfully", launchDatas.size());
		}
	}

	public String loadDataString(String key) {
		HTMLElement ret = parentElement.querySelector("#_eaglerFatOffline_" + key);
		return ret != null ? ret.getInnerText() : null;
	}

	public byte[] loadDataBinary(String key) {
		HTMLElement ret = parentElement.querySelector("#_eaglerFatOffline_" + key);
		if(ret == null) {
			return null;
		}
		try {
			return Base64.decodeBase64(ret.getInnerText());
		}catch(Throwable t) {
			return null;
		}
	}

}