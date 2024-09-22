package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

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
public class LaunchConfigEntry {

	public final EaglercraftUUID uuid;
	public final EaglercraftUUID clientDataUUID;
	public String displayName;
	public EnumClientLaunchType type;
	public String joinServer;
	public String launchOptsVar;
	public String launchOptsAssetsURIVar;
	public String launchOptsContainerVar;
	public String mainFunction;
	public String launchOpts;
	public boolean clearCookiesBeforeLaunch;

	public LaunchConfigEntry(EaglercraftUUID uuid, EaglercraftUUID clientDataUUID) {
		this.uuid = uuid;
		this.clientDataUUID = clientDataUUID;
	}

	public LaunchConfigEntry(EaglercraftUUID uuid, EaglercraftUUID clientDataUUID, String displayName,
			EnumClientLaunchType type, String joinServer, String launchOptsVar, String assetsURIVar,
			String containerVar, String mainFunction, String launchOpts, boolean clearCookiesBeforeLaunch) {
		this.uuid = uuid;
		this.clientDataUUID = clientDataUUID;
		this.displayName = displayName;
		this.type = type;
		this.joinServer = joinServer;
		this.launchOptsVar = launchOptsVar;
		this.launchOptsAssetsURIVar = assetsURIVar;
		this.launchOptsContainerVar = containerVar;
		this.mainFunction = mainFunction;
		this.launchOpts = launchOpts;
		this.clearCookiesBeforeLaunch = clearCookiesBeforeLaunch;
	}

	public LaunchConfigEntry(EaglercraftUUID uuid, JSONObject jsonObject) {
		this.uuid = uuid;
		EaglercraftUUID sanityUUID = EaglercraftUUID.fromString(jsonObject.getString("uuid"));
		if(!sanityUUID.equals(uuid)) {
			throw new IllegalArgumentException("The file's name UUID does not equal the UUID string found in the file!");
		}
		int typeId = jsonObject.getInt("type");
		type = EnumClientLaunchType.getById(typeId);
		if(type == null) {
			throw new IllegalArgumentException("Unknown launch configuration type " + typeId + "!");
		}
		clientDataUUID = EaglercraftUUID.fromString(jsonObject.getString("dataUUID"));
		displayName = jsonObject.getString("displayName");
		clearCookiesBeforeLaunch = jsonObject.getBoolean("clearCookies");
		switch(type) {
		case STANDARD_OFFLINE_V1:
			launchOpts = jsonObject.getString("launchOpts");
			launchOptsVar = jsonObject.getString("launchOptsVar");
			launchOptsAssetsURIVar = jsonObject.getString("assetsURIVar");
			launchOptsContainerVar = jsonObject.getString("containerVar");
			mainFunction = jsonObject.getString("entryPoint");
			break;
		case EAGLERX_V1:
		case EAGLERX_SIGNED_V1:
		case EAGLER_1_5_V2:
		case PEYTON_V2:
			launchOpts = jsonObject.getString("launchOpts");
			break;
		case EAGLER_1_5_V1:
			launchOpts = jsonObject.getString("launchOpts");
			joinServer = jsonObject.getString("joinServer");
			break;
		case EAGLER_BETA_V1:
			joinServer = jsonObject.getString("joinServer");
			break;
		case PEYTON_V1:
			break;
		default: //?
			break;
		}
	}

	public void writeJSON(JSONObject jsonObject) {
		jsonObject.put("uuid", uuid.toString());
		jsonObject.put("type", type.id);
		jsonObject.put("dataUUID", clientDataUUID.toString());
		jsonObject.put("displayName", displayName);
		jsonObject.put("clearCookies", clearCookiesBeforeLaunch);
		switch(type) {
		case STANDARD_OFFLINE_V1:
			jsonObject.put("launchOpts", launchOpts != null ? launchOpts : "{ }");
			jsonObject.put("launchOptsVar", launchOptsVar != null ? launchOptsVar : "eaglercraftXOpts");
			jsonObject.put("assetsURIVar", launchOptsAssetsURIVar != null ? launchOptsAssetsURIVar : "assetsURI");
			jsonObject.put("containerVar", launchOptsContainerVar != null ? launchOptsContainerVar : "container");
			jsonObject.put("entryPoint", mainFunction != null ? mainFunction : "main");
			break;
		case EAGLERX_V1:
		case EAGLERX_SIGNED_V1:
		case EAGLER_1_5_V2:
		case PEYTON_V2:
			jsonObject.put("launchOpts", launchOpts != null ? launchOpts : "{ }");
			break;
		case EAGLER_1_5_V1:
			jsonObject.put("launchOpts", launchOpts != null ? launchOpts : "[NBT]{ }[/NBT]");
			jsonObject.put("joinServer", joinServer != null ? joinServer : "");
			break;
		case EAGLER_BETA_V1:
			jsonObject.put("joinServer", joinServer != null ? joinServer : "");
			break;
		case PEYTON_V1:
			break;
		default: //?
			break;
		}
	}

	public LaunchConfigEntry rotateUUIDs(EaglercraftUUID rotatedLaunchUUID, EaglercraftUUID rotatedClientUUID) {
		return new LaunchConfigEntry(rotatedLaunchUUID, rotatedClientUUID, displayName, type, joinServer, launchOptsVar,
				launchOptsAssetsURIVar, launchOptsContainerVar, mainFunction, launchOpts, clearCookiesBeforeLaunch);
	}

	public LaunchConfigEntry fork() {
		return new LaunchConfigEntry(uuid, clientDataUUID, displayName, type, joinServer, launchOptsVar,
				launchOptsAssetsURIVar, launchOptsContainerVar, mainFunction, launchOpts, clearCookiesBeforeLaunch);
	}

}
