package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

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
class DynamicLightInstance {

	public final String lightName;
	public final boolean shadow;
	long lastCacheHit = 0l;

	double posX;
	double posY;
	double posZ;
	float red;
	float green;
	float blue;
	float radius;

	public DynamicLightInstance(String lightName, boolean shadow) {
		this.lightName = lightName;
		this.shadow = shadow;
	}

	public void updateLight(double posX, double posY, double posZ, float red, float green, float blue) {
		this.lastCacheHit = System.currentTimeMillis();
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.radius = (float)(Math.sqrt(red + green + blue) * 3.0 + 0.5);
	}

	public void destroy() {
		
	}

	public float getRadiusInWorld() {
		return radius;
	}

}
