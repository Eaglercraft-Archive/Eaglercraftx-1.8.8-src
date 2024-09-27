package net.lax1dude.eaglercraft.v1_8.profile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerOutputStream;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.HString;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

/**
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class EaglerProfile {

	private static String username;
	
	public static int presetSkinId;
	public static int customSkinId;
	
	public static int presetCapeId;
	public static int customCapeId;

	public static boolean isServerSkinOverride = false;
	public static int overridePresetSkinId = -1;
	public static final ResourceLocation overrideCustomSkinTexture = new ResourceLocation("eagler:skins/custom/tex_server_override");
	public static EaglerSkinTexture overrideCustomSkin = null;
	public static SkinModel overrideCustomSkinModel = SkinModel.STEVE;

	public static boolean isServerCapeOverride = false;
	public static int overridePresetCapeId = -1;
	public static final ResourceLocation overrideCustomCapeTexture = new ResourceLocation("eagler:capes/custom/tex_server_override");
	public static EaglerSkinTexture overrideCustomCape = null;

	public static final List<CustomSkin> customSkins = new ArrayList<>();
	public static final List<CustomCape> customCapes = new ArrayList<>();
	
	public static final EaglercraftRandom rand;
	
	public static ResourceLocation getActiveSkinResourceLocation() {
		if(isServerSkinOverride) {
			if(overridePresetSkinId == -1) {
				return overrideCustomSkinTexture;
			}else {
				if(overridePresetSkinId >= 0 && overridePresetSkinId < DefaultSkins.defaultSkinsMap.length) {
					return DefaultSkins.defaultSkinsMap[overridePresetSkinId].location;
				}else {
					return DefaultSkins.defaultSkinsMap[0].location;
				}
			}
		}
		if(presetSkinId == -1) {
			if(customSkinId >= 0 && customSkinId < customSkins.size()) {
				return customSkins.get(customSkinId).getResource();
			}else {
				customSkinId = -1;
				presetSkinId = 0;
				return DefaultSkins.defaultSkinsMap[0].location;
			}
		}else {
			if(presetSkinId >= 0 && presetSkinId < DefaultSkins.defaultSkinsMap.length) {
				return DefaultSkins.defaultSkinsMap[presetSkinId].location;
			}else {
				presetSkinId = 0;
				return DefaultSkins.defaultSkinsMap[0].location;
			}
		}
	}
	
	public static SkinModel getActiveSkinModel() {
		if(isServerSkinOverride) {
			if(overridePresetSkinId == -1) {
				return overrideCustomSkinModel;
			}else {
				if(overridePresetSkinId >= 0 && overridePresetSkinId < DefaultSkins.defaultSkinsMap.length) {
					return DefaultSkins.defaultSkinsMap[overridePresetSkinId].model;
				}else {
					return DefaultSkins.defaultSkinsMap[0].model;
				}
			}
		}
		if(presetSkinId == -1) {
			if(customSkinId >= 0 && customSkinId < customSkins.size()) {
				return customSkins.get(customSkinId).model;
			}else {
				customSkinId = -1;
				presetSkinId = 0;
				return DefaultSkins.defaultSkinsMap[0].model;
			}
		}else {
			if(presetSkinId >= 0 && presetSkinId < DefaultSkins.defaultSkinsMap.length) {
				return DefaultSkins.defaultSkinsMap[presetSkinId].model;
			}else {
				presetSkinId = 0;
				return DefaultSkins.defaultSkinsMap[0].model;
			}
		}
	}
	
	public static ResourceLocation getActiveCapeResourceLocation() {
		if(isServerCapeOverride) {
			if(overridePresetCapeId == -1) {
				return overrideCustomCapeTexture;
			}else {
				if(overridePresetCapeId >= 0 && overridePresetCapeId < DefaultCapes.defaultCapesMap.length) {
					return DefaultCapes.defaultCapesMap[overridePresetCapeId].location;
				}else {
					return DefaultCapes.defaultCapesMap[0].location;
				}
			}
		}
		if(presetCapeId == -1) {
			if(customCapeId >= 0 && customCapeId < customCapes.size()) {
				return customCapes.get(customCapeId).getResource();
			}else {
				customCapeId = -1;
				presetCapeId = 0;
				return DefaultCapes.defaultCapesMap[0].location;
			}
		}else {
			if(presetCapeId >= 0 && presetCapeId < DefaultCapes.defaultCapesMap.length) {
				return DefaultCapes.defaultCapesMap[presetCapeId].location;
			}else {
				presetCapeId = 0;
				return DefaultCapes.defaultCapesMap[0].location;
			}
		}
	}

	public static EaglercraftUUID getPlayerUUID() {
		return Minecraft.getMinecraft().getSession().getProfile().getId();
	}

	public static String getName() {
		return username;
	}

	public static void setName(String str) {
		username = str;
		Minecraft mc = Minecraft.getMinecraft();
		if(mc != null) {
			mc.getSession().reset();
		}
	}

	public static byte[] getSkinPacket(int vers) {
		if(presetSkinId == -1) {
			if(customSkinId >= 0 && customSkinId < customSkins.size()) {
				CustomSkin toSend = customSkins.get(customSkinId);
				if(vers <= 3) {
					return SkinPackets.writeMySkinCustomV3(toSend);
				}else {
					return SkinPackets.writeMySkinCustomV4(toSend);
				}
			}else {
				customSkinId = -1;
				presetSkinId = 0;
				return SkinPackets.writeMySkinPreset(0);
			}
		}else {
			if(presetSkinId >= 0 && presetSkinId < DefaultSkins.defaultSkinsMap.length) {
				return SkinPackets.writeMySkinPreset(presetSkinId);
			}else {
				presetSkinId = 0;
				return SkinPackets.writeMySkinPreset(0);
			}
		}
	}

	public static byte[] getCapePacket() {
		if(presetCapeId == -1) {
			if(customCapeId >= 0 && customCapeId < customCapes.size()) {
				return CapePackets.writeMyCapeCustom(customCapes.get(customCapeId));
			}else {
				customCapeId = -1;
				presetCapeId = 0;
				return CapePackets.writeMyCapePreset(0);
			}
		}else {
			if(presetCapeId >= 0 && presetCapeId < DefaultCapes.defaultCapesMap.length) {
				return CapePackets.writeMyCapePreset(presetCapeId);
			}else {
				presetCapeId = 0;
				return CapePackets.writeMyCapePreset(0);
			}
		}
	}

	public static void handleForceSkinPreset(int preset) {
		isServerSkinOverride = true;
		overridePresetSkinId = preset;
		ServerSkinCache.needReloadClientSkin = true;
	}

	public static void handleForceSkinCustom(int modelID, byte[] datav3) {
		if(datav3.length != 16384) {
			return;
		}
		isServerSkinOverride = true;
		overridePresetSkinId = -1;
		overrideCustomSkinModel = SkinModel.getModelFromId(modelID);
		if(overrideCustomSkinModel.highPoly != null) {
			overrideCustomSkinModel = SkinModel.STEVE;
		}
		if(overrideCustomSkin == null) {
			overrideCustomSkin = new EaglerSkinTexture(datav3, 64, 64);
			Minecraft.getMinecraft().getTextureManager().loadTexture(overrideCustomSkinTexture, overrideCustomSkin);
		}else {
			overrideCustomSkin.copyPixelsIn(datav3);
		}
		ServerSkinCache.needReloadClientSkin = true;
	}

	public static void handleForceCapePreset(int preset) {
		isServerCapeOverride = true;
		overridePresetCapeId = preset;
		ServerCapeCache.needReloadClientCape = true;
	}

	public static void handleForceCapeCustom(byte[] custom) {
		if(custom.length != 1173) {
			return;
		}
		byte[] pixels32x32 = new byte[4096];
		SkinConverter.convertCape23x17RGBto32x32RGBA(custom, pixels32x32);
		isServerCapeOverride = true;
		overridePresetCapeId = -1;
		if(overrideCustomCape == null) {
			overrideCustomCape = new EaglerSkinTexture(pixels32x32, 32, 32);
			Minecraft.getMinecraft().getTextureManager().loadTexture(overrideCustomCapeTexture, overrideCustomCape);
		}else {
			overrideCustomCape.copyPixelsIn(pixels32x32);
		}
		ServerCapeCache.needReloadClientCape = true;
	}

	public static void clearServerSkinOverride() {
		isServerSkinOverride = false;
		isServerCapeOverride = false;
	}

	private static boolean doesSkinExist(String name) {
		for(int i = 0, l = customSkins.size(); i < l; ++i) {
			if(customSkins.get(i).name.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	private static boolean doesCapeExist(String name) {
		for(int i = 0, l = customCapes.size(); i < l; ++i) {
			if(customCapes.get(i).name.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public static int addCustomSkin(String fileName, byte[] rawSkin) {
		if(doesSkinExist(fileName)) {
			String newName;
			int i = 2;
			while(doesSkinExist(newName = fileName + " (" + i + ")")) {
				++i;
			}
			fileName = newName;
		}
		CustomSkin newSkin = new CustomSkin(fileName, rawSkin, SkinModel.STEVE);
		newSkin.load();
		int r = customSkins.size();
		customSkins.add(newSkin);
		return r;
	}

	public static int addCustomCape(String fileName, byte[] rawCape23x17RGB) {
		if(doesCapeExist(fileName)) {
			String newName;
			int i = 2;
			while(doesCapeExist(newName = fileName + " (" + i + ")")) {
				++i;
			}
			fileName = newName;
		}
		CustomCape newCape = new CustomCape(fileName, rawCape23x17RGB);
		newCape.load();
		int r = customCapes.size();
		customCapes.add(newCape);
		return r;
	}

	public static void clearCustomSkins() {
		for(int i = 0, l = customSkins.size(); i < l; ++i) {
			customSkins.get(i).delete();
		}
		customSkins.clear();
	}

	public static void clearCustomCapes() {
		for(int i = 0, l = customCapes.size(); i < l; ++i) {
			customCapes.get(i).delete();
		}
		customCapes.clear();
	}

	public static void read() {
		read(EagRuntime.getStorage("p"));
	}

	public static void read(byte[] profileStorage) {
		if (profileStorage == null) {
			return;
		}

		NBTTagCompound profile;
		try {
			profile = CompressedStreamTools.readCompressed(new EaglerInputStream(profileStorage));
		}catch(IOException ex) {
			return;
		}

		if (profile == null || profile.hasNoTags()) {
			return;
		}

		presetSkinId = profile.getInteger("presetSkin");
		customSkinId = profile.getInteger("customSkin");

		if(profile.hasKey("presetCape", 99)) presetCapeId = profile.getInteger("presetCape");
		if(profile.hasKey("customCape", 99)) customCapeId = profile.getInteger("customCape");

		String loadUsername = profile.getString("username").trim();

		if(!loadUsername.isEmpty()) {
			username = loadUsername.replaceAll("[^A-Za-z0-9]", "_");
		}

		clearCustomSkins();

		NBTTagList skinsList = profile.getTagList("skins", 10);
		for(int i = 0, l = skinsList.tagCount(); i < l; ++i) {
			NBTTagCompound skin = skinsList.getCompoundTagAt(i);
			String skinName = skin.getString("name");
			byte[] skinData = skin.getByteArray("data");
			if(skinData.length != 16384) continue;
			for(int y = 20; y < 32; ++y) {
				for(int x = 16; x < 40; ++x) {
					skinData[(y << 8) | (x << 2)] = (byte)0xff;
				}
			}
			int skinModel = skin.getByte("model");
			CustomSkin newSkin = new CustomSkin(skinName, skinData, SkinModel.getModelFromId(skinModel));
			newSkin.load();
			customSkins.add(newSkin);
		}

		if(profile.hasKey("capes", 9)) {
			clearCustomCapes();
			NBTTagList capesList = profile.getTagList("capes", 10);
			for(int i = 0, l = capesList.tagCount(); i < l; ++i) {
				NBTTagCompound cape = capesList.getCompoundTagAt(i);
				String capeName = cape.getString("name");
				byte[] capeData = cape.getByteArray("data");
				if(capeData.length != 1173) continue;
				CustomCape newCape = new CustomCape(capeName, capeData);
				newCape.load();
				customCapes.add(newCape);
			}
		}

		if(presetSkinId == -1) {
			if(customSkinId < 0 || customSkinId >= customSkins.size()) {
				presetSkinId = 0;
				customSkinId = -1;
			}
		}else {
			customSkinId = -1;
			if(presetSkinId < 0 || presetSkinId >= DefaultSkins.defaultSkinsMap.length) {
				presetSkinId = 0;
			}
		}

		if(presetCapeId == -1) {
			if(customCapeId < 0 || customCapeId >= customCapes.size()) {
				presetCapeId = 0;
				customCapeId = -1;
			}
		}else {
			customCapeId = -1;
			if(presetCapeId < 0 || presetCapeId >= DefaultCapes.defaultCapesMap.length) {
				presetCapeId = 0;
			}
		}

	}

	public static byte[] write() {
		NBTTagCompound profile = new NBTTagCompound();
		profile.setInteger("presetSkin", presetSkinId);
		profile.setInteger("customSkin", customSkinId);
		profile.setInteger("presetCape", presetCapeId);
		profile.setInteger("customCape", customCapeId);
		profile.setString("username", username);
		NBTTagList skinsList = new NBTTagList();
		for(int i = 0, l = customSkins.size(); i < l; ++i) {
			CustomSkin sk = customSkins.get(i);
			NBTTagCompound skin = new NBTTagCompound();
			skin.setString("name", sk.name);
			skin.setByteArray("data", sk.texture);
			skin.setByte("model", (byte)sk.model.id);
			skinsList.appendTag(skin);
		}
		profile.setTag("skins", skinsList);
		NBTTagList capesList = new NBTTagList();
		for(int i = 0, l = customCapes.size(); i < l; ++i) {
			CustomCape cp = customCapes.get(i);
			NBTTagCompound cape = new NBTTagCompound();
			cape.setString("name", cp.name);
			cape.setByteArray("data", cp.texture);
			capesList.appendTag(cape);
		}
		profile.setTag("capes", capesList);
		EaglerOutputStream bao = new EaglerOutputStream();
		try {
			CompressedStreamTools.writeCompressed(profile, bao);
		} catch (IOException e) {
			return null;
		}
		return bao.toByteArray();
	}

	public static void save() {
		byte[] b = write();
		if(b != null) {
			EagRuntime.setStorage("p", b);
		}
	}

	static {
		String[] defaultNames = new String[] {
				"Yeeish", "Yeeish", "Yee", "Yee", "Yeer", "Yeeler", "Eagler", "Eagl",
				"Darver", "Darvler", "Vool", "Vigg", "Vigg", "Deev", "Yigg", "Yeeg"
		};
		
		rand = new EaglercraftRandom();
		
		do {
			username = HString.format("%s%s%04d", defaultNames[rand.nextInt(defaultNames.length)], defaultNames[rand.nextInt(defaultNames.length)], rand.nextInt(10000));
		}while(username.length() > 16);
		
		setName(username);
		
		do {
			presetSkinId = rand.nextInt(DefaultSkins.defaultSkinsMap.length);
		}while(DefaultSkins.defaultSkinsMap[presetSkinId].model.highPoly != null);
		customSkinId = -1;
		
		presetCapeId = 0;
		customCapeId = -1;
		
	}

	public static boolean isDefaultUsername(String str) {
		return str.toLowerCase().matches("^(yeeish|yee|yeer|yeeler|eagler|eagl|darver|darvler|vool|vigg|deev|yigg|yeeg){2}\\d{2,4}$");
	}

}
