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
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

public class ClientDataEntry {

	public final EnumClientFormatType type;
	public final EaglercraftUUID uuid;
	public final EaglercraftUUID mainPayload;
	public final EaglercraftUUID integratedServer;
	public final EaglercraftUUID clientSignature;
	public final List<EPKDataEntry> epkFiles;

	public ClientDataEntry(EnumClientFormatType type, EaglercraftUUID uuid, EaglercraftUUID mainPayload,
			EaglercraftUUID integratedServer, EaglercraftUUID clientSignature, List<EPKDataEntry> epkFiles) {
		this.type = type;
		this.uuid = uuid;
		this.mainPayload = mainPayload;
		this.integratedServer = integratedServer;
		this.clientSignature = clientSignature;
		this.epkFiles = epkFiles;
	}

	public ClientDataEntry(EaglercraftUUID uuid, JSONObject jsonObject) {
		this.uuid = uuid;
		EaglercraftUUID sanityUUID = EaglercraftUUID.fromString(jsonObject.getString("uuid"));
		if(!sanityUUID.equals(uuid)) {
			throw new IllegalArgumentException("The file's name UUID does not equal the UUID string found in the file!");
		}
		int typeId = jsonObject.getInt("type");
		type = EnumClientFormatType.getById(typeId);
		if(type == null) {
			throw new IllegalArgumentException("Unknown client data type " + typeId + "!");
		}
		switch(type) {
		default:
		case EAGLER_STANDARD_OFFLINE:
			mainPayload = EaglercraftUUID.fromString(jsonObject.getString("mainPayload"));
			integratedServer = null;
			clientSignature = null;
			epkFiles = loadEPKFiles(jsonObject.getJSONArray("epkFiles"));
			break;
		case EAGLER_STANDARD_1_5_OFFLINE:
			mainPayload = EaglercraftUUID.fromString(jsonObject.getString("mainPayload"));
			integratedServer = EaglercraftUUID.fromString(jsonObject.getString("integratedServer"));
			clientSignature = null;
			epkFiles = loadEPKFiles(jsonObject.getJSONArray("epkFiles"));
			break;
		case EAGLER_SIGNED_OFFLINE:
			mainPayload = EaglercraftUUID.fromString(jsonObject.getString("mainPayload"));
			integratedServer = null;
			clientSignature = EaglercraftUUID.fromString(jsonObject.getString("clientSignature"));
			epkFiles = null;
			break;
		}
	}

	public void writeJSON(JSONObject jsonObject) {
		jsonObject.put("uuid", uuid.toString());
		jsonObject.put("type", type.id);
		switch(type) {
		case EAGLER_STANDARD_OFFLINE:
		default:
			jsonObject.put("mainPayload", mainPayload.toString());
			jsonObject.put("epkFiles", storeEPKFiles(epkFiles));
			break;
		case EAGLER_STANDARD_1_5_OFFLINE:
			jsonObject.put("mainPayload", mainPayload.toString());
			jsonObject.put("integratedServer", integratedServer.toString());
			jsonObject.put("epkFiles", storeEPKFiles(epkFiles));
			break;
		case EAGLER_SIGNED_OFFLINE:
			jsonObject.put("mainPayload", mainPayload.toString());
			jsonObject.put("clientSignature", clientSignature.toString());
			break;
		}
	}

	protected static List<EPKDataEntry> loadEPKFiles(JSONArray arr) {
		int cnt = arr.length();
		List<EPKDataEntry> ret = new ArrayList<>(cnt);
		for(int i = 0; i < cnt; ++i) {
			JSONObject obj = arr.getJSONObject(i);
			ret.add(new EPKDataEntry(obj.optString("path", ""), EaglercraftUUID.fromString(obj.getString("uuid"))));
		}
		return ret;
	}

	protected static JSONArray storeEPKFiles(List<EPKDataEntry> arr) {
		int cnt = arr.size();
		JSONArray ret = new JSONArray(cnt);
		for(int i = 0; i < cnt; ++i) {
			EPKDataEntry etr = arr.get(i);
			JSONObject obj = (new JSONObject()).put("uuid", etr.dataUUID.toString());
			if(etr.extractTo.length() > 0) {
				obj.put("path", etr.extractTo);
			}
			ret.put(obj);
		}
		return ret;
	}

	public Collection<EaglercraftUUID> getReferencedBlobs() {
		List<EaglercraftUUID> toRet = new ArrayList<>(4);
		switch(type) {
		case EAGLER_STANDARD_OFFLINE:
		default:
			toRet.add(mainPayload);
			epkFiles.stream().map((e) -> e.dataUUID).forEach(toRet::add);
			break;
		case EAGLER_STANDARD_1_5_OFFLINE:
			toRet.add(mainPayload);
			toRet.add(integratedServer);
			epkFiles.stream().map((e) -> e.dataUUID).forEach(toRet::add);
			break;
		case EAGLER_SIGNED_OFFLINE:
			toRet.add(mainPayload);
			toRet.add(clientSignature);
			break;
		}
		return toRet;
	}

	public ClientDataEntry rotateUUID(EaglercraftUUID rotatedClientUUID) {
		return new ClientDataEntry(type, rotatedClientUUID, mainPayload, integratedServer, clientSignature, epkFiles);
	}

}