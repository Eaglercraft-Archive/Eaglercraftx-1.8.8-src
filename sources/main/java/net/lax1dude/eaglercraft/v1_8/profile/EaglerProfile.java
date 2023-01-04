package net.lax1dude.eaglercraft.v1_8.profile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info) 
 * 
 */
public class EaglerProfile {

	private static String username;
	
	public static int presetSkinId;
	public static int customSkinId;
	
	public static final List<CustomSkin> customSkins = new ArrayList();
	
	public static final EaglercraftRandom rand;
	
	public static ResourceLocation getActiveSkinResourceLocation() {
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

	public static byte[] getSkinPacket() throws IOException {
		if(presetSkinId == -1) {
			if(customSkinId >= 0 && customSkinId < customSkins.size()) {
				return SkinPackets.writeMySkinCustom(customSkins.get(customSkinId));
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

	private static boolean doesSkinExist(String name) {
		for(int i = 0, l = customSkins.size(); i < l; ++i) {
			if(customSkins.get(i).name.equalsIgnoreCase(name)) {
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

	public static void clearCustomSkins() {
		for(int i = 0, l = customSkins.size(); i < l; ++i) {
			customSkins.get(i).delete();
		}
		customSkins.clear();
	}

	public static void read() {
		byte[] profileStorage = EagRuntime.getStorage("p");

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

	}

	public static void write() {
		NBTTagCompound profile = new NBTTagCompound();
		profile.setInteger("presetSkin", presetSkinId);
		profile.setInteger("customSkin", customSkinId);
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
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			CompressedStreamTools.writeCompressed(profile, bao);
		} catch (IOException e) {
			return;
		}
		EagRuntime.setStorage("p", bao.toByteArray());
	}

	static {
		String[] defaultNames = new String[] {
				"Yeeish", "Yeeish", "Yee", "Yee", "Yeer", "Yeeler", "Eagler", "Eagl",
				"Darver", "Darvler", "Vool", "Vigg", "Vigg", "Deev", "Yigg", "Yeeg"
		};
		
		rand = new EaglercraftRandom();
		
		do {
			username = defaultNames[rand.nextInt(defaultNames.length)] + defaultNames[rand.nextInt(defaultNames.length)] + (100 + rand.nextInt(900));
		}while(username.length() > 16);
		
		setName(username);
		
		presetSkinId = rand.nextInt(DefaultSkins.defaultSkinsMap.length);
		customSkinId = -1;
		
	}

}
