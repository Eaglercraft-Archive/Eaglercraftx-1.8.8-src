package net.optifine.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.optifine.BetterGrass;
import net.optifine.BetterSnow;
import net.optifine.Config;
import net.optifine.CustomItems;
import net.optifine.CustomSky;
import net.optifine.SmartLeaves;

public class TextureUtils {

	public static String fixResourcePath(String path, String basePath) {
		String s = "assets/minecraft/";

		if (path.startsWith(s)) {
			path = path.substring(s.length());
			return path;
		} else if (path.startsWith("./")) {
			path = path.substring(2);

			if (!basePath.endsWith("/")) {
				basePath = basePath + "/";
			}

			path = basePath + path;
			return path;
		} else {
			if (path.startsWith("/~")) {
				path = path.substring(1);
			}

			String s1 = "mcpatcher/";

			if (path.startsWith("~/")) {
				path = path.substring(2);
				path = s1 + path;
				return path;
			} else if (path.startsWith("/")) {
				path = s1 + path.substring(1);
				return path;
			} else {
				return path;
			}
		}
	}

	public static String getBasePath(String path) {
		int i = path.lastIndexOf(47);
		return i < 0 ? "" : path.substring(0, i);
	}

	public static void registerResourceListener() {
		IResourceManager iresourcemanager = Minecraft.getMinecraft().getResourceManager();
		if (iresourcemanager instanceof IReloadableResourceManager) {
			IReloadableResourceManager ireloadableresourcemanager = (IReloadableResourceManager) iresourcemanager;
			IResourceManagerReloadListener iresourcemanagerreloadlistener = new IResourceManagerReloadListener() {
				public void onResourceManagerReload(IResourceManager var1) {
					TextureUtils.resourcesReloaded(var1);
				}
			};
			ireloadableresourcemanager.registerReloadListener(iresourcemanagerreloadlistener);
		}
	}

	public static void resourcesReloaded(IResourceManager rm) {
		if (Minecraft.getMinecraft().getTextureMapBlocks() != null) {
			Config.dbg("*** Reloading custom textures ***");
			CustomSky.reset();
			// TextureAnimations.reset();
			// update();
			// NaturalTextures.update();
			BetterGrass.update();
			BetterSnow.update();
			// TextureAnimations.update();
			// CustomColors.update();
			CustomSky.update();
			// RandomEntities.update();
			CustomItems.updateModels();
			// CustomEntityModels.update();
			// Shaders.resourcesReloaded();
			// Lang.resourcesReloaded();
			// Config.updateTexturePackClouds();
			SmartLeaves.updateLeavesModels();
			// CustomPanorama.update();
			// CustomGuis.update();
			// LayerMooshroomMushroom.update();
			// CustomLoadingScreens.update();
			// CustomBlockLayers.update();
			Minecraft.getMinecraft().getTextureManager().tick();
		}
	}
}
