package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
public class DynamicLightManager {

	static final Map<String, DynamicLightInstance> lightRenderers = new HashMap();
	static final List<DynamicLightInstance> lightRenderList = new LinkedList();
	static long renderTimeout = 5000l;
	static boolean isRenderLightsPass = false;

	private static long lastTick = 0l;

	public static void renderDynamicLight(String lightName, double posX, double posY, double posZ, float red,
			float green, float blue, boolean shadows) {
		if(isRenderLightsPass) {
			DynamicLightInstance dl = lightRenderers.get(lightName);
			if(dl == null) {
				lightRenderers.put(lightName, dl = new DynamicLightInstance(lightName, shadows));
			}
			dl.updateLight(posX, posY, posZ, red, green, blue);
			lightRenderList.add(dl);
		}
	}

	public static boolean isRenderingLights() {
		return isRenderLightsPass;
	}

	public static void setIsRenderingLights(boolean b) {
		isRenderLightsPass = b;
	}

	static void updateTimers() {
		long millis = System.currentTimeMillis();
		if(millis - lastTick > 1000l) {
			lastTick = millis;
			Iterator<DynamicLightInstance> itr = lightRenderers.values().iterator();
			while(itr.hasNext()) {
				DynamicLightInstance dl = itr.next();
				if(millis - dl.lastCacheHit > renderTimeout) {
					dl.destroy();
					itr.remove();
				}
			}
		}
	}

	static void destroyAll() {
		Iterator<DynamicLightInstance> itr = lightRenderers.values().iterator();
		while(itr.hasNext()) {
			itr.next().destroy();
		}
		lightRenderers.clear();
	}

}
