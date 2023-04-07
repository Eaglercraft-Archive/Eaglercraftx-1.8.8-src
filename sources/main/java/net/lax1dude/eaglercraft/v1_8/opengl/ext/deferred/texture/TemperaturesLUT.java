package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture;

import java.io.IOException;
import java.io.InputStream;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
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
public class TemperaturesLUT implements IResourceManagerReloadListener {

	private static final Logger logger = LogManager.getLogger("TemperaturesLUT");

	public static final float[][] colorTemperatureLUT = new float[390][3];

	@Override
	public void onResourceManagerReload(IResourceManager var1) {
		try {
			IResource res = var1.getResource(new ResourceLocation("eagler:glsl/deferred/temperatures.lut"));
			try(InputStream is = res.getInputStream()) {
				for(int i = 0; i < 390; ++i) {
					colorTemperatureLUT[i][0] = ((int)is.read() & 0xFF) * 0.0039216f;
					colorTemperatureLUT[i][0] *= colorTemperatureLUT[i][0];
					colorTemperatureLUT[i][1] = ((int)is.read() & 0xFF) * 0.0039216f;
					colorTemperatureLUT[i][1] *= colorTemperatureLUT[i][1];
					colorTemperatureLUT[i][2] = ((int)is.read() & 0xFF) * 0.0039216f;
					colorTemperatureLUT[i][2] *= colorTemperatureLUT[i][2];
				}
			}
		} catch (IOException e) {
			logger.error("Failed to load color temperature lookup table!");
			logger.error(e);
		}
	}

	public static float[] getColorTemperature(int kelvin) {
		if (kelvin < 1000) kelvin = 1000;
		if (kelvin > 39000) kelvin = 39000;
		int k = ((kelvin - 100) / 100);
		return colorTemperatureLUT[k];
	}

	public static void getColorTemperature(int kelvin, float[] ret) {
		if (kelvin < 1000) kelvin = 1000;
		if (kelvin > 39000) kelvin = 39000;
		int k = ((kelvin - 100) / 100);
		ret[0] = colorTemperatureLUT[k][0];
		ret[1] = colorTemperatureLUT[k][1];
		ret[2] = colorTemperatureLUT[k][2];
	}

}
