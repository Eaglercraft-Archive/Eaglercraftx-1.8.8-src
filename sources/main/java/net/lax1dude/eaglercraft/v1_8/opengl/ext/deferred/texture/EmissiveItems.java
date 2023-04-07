package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Copyright (c) 2023 LAX1DUDE. All Rights Reserved.
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
public class EmissiveItems implements IResourceManagerReloadListener {

	private static final Logger logger = LogManager.getLogger("EmissiveItemsCSV");

	private static final Map<String,float[]> entries = new HashMap();

	public static float[] getItemEmission(ItemStack itemStack) {
		return getItemEmission(itemStack.getItem(), itemStack.getItemDamage());
	}

	public static float[] getItemEmission(Item item, int damage) {
		return entries.get(Item.itemRegistry.getNameForObject(item).toString() + "#" + damage);
	}

	@Override
	public void onResourceManagerReload(IResourceManager var1) {
		try {
			IResource itemsCsv = var1.getResource(new ResourceLocation("eagler:glsl/deferred/emissive_items.csv"));
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(itemsCsv.getInputStream(), StandardCharsets.UTF_8))) {
				entries.clear();
				String line;
				boolean firstLine = true;
				while((line = reader.readLine()) != null) {
					if((line = line.trim()).length() > 0) {
						if(firstLine) {
							firstLine = false;
							continue;
						}
						String[] split = line.split(",");
						if(split.length == 6) {
							try {
								int dmg = Integer.parseInt(split[1]);
								float r = Float.parseFloat(split[2]);
								float g = Float.parseFloat(split[3]);
								float b = Float.parseFloat(split[4]);
								float i = Float.parseFloat(split[5]);
								r *= i;
								g *= i;
								b *= i;
								entries.put(split[0] + "#" + dmg, new float[] { r, g, b });
								continue;
							}catch(NumberFormatException ex) {
							}
						}
						logger.error("Skipping bad emissive item entry: {}", line);
					}
				}
			}
		}catch(Throwable t) {
			logger.error("Could not load list of emissive items!");
			logger.error(t);
		}
	}

}
