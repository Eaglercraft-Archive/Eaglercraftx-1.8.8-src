package net.lax1dude.eaglercraft.v1_8.internal;

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
public enum EnumPlatformANGLE {

	DEFAULT(225281 /* GLFW_ANGLE_PLATFORM_TYPE_NONE */, "default", "Default"),
	D3D11(225285 /* GLFW_ANGLE_PLATFORM_TYPE_D3D11 */, "d3d11", "Direct3D11"),
	OPENGL(225282 /* GLFW_ANGLE_PLATFORM_TYPE_OPENGL */, "opengl", "OpenGL"),
	OPENGLES(225283 /* GLFW_ANGLE_PLATFORM_TYPE_OPENGLES */, "opengles", "OpenGL ES"),
	METAL(225288 /* GLFW_ANGLE_PLATFORM_TYPE_METAL */, "metal", "Metal"),
	VULKAN(225287 /* GLFW_ANGLE_PLATFORM_TYPE_VULKAN */, "vulkan", "Vulkan");
	
	public final int eglEnum;
	public final String id;
	public final String name;

	private EnumPlatformANGLE(int eglEnum, String id, String name) {
		this.eglEnum = eglEnum;
		this.id = id;
		this.name = name;
	}
	
	public String toString() {
		return id;
	}
	
	public static EnumPlatformANGLE fromId(String id) {
		if(id.equals("d3d11") || id.equals("d3d") || id.equals("dx11")) {
			return D3D11;
		}else if(id.equals("opengl")) {
			return OPENGL;
		}else if(id.equals("opengles")) {
			return OPENGLES;
		}else if(id.equals("metal")) {
			return METAL;
		}else if(id.equals("vulkan")) {
			return VULKAN;
		}else {
			return DEFAULT;
		}
	}
	
	public static EnumPlatformANGLE fromGLRendererString(String str) {
		str = str.toLowerCase();
		if(str.contains("direct3d11") || str.contains("d3d11")) {
			return D3D11;
		}else if(str.contains("opengl es")) {
			return OPENGLES;
		}else if(str.contains("opengl")) {
			return OPENGL;
		}else if(str.contains("metal")) {
			return METAL;
		}else if(str.contains("vulkan")) {
			return VULKAN;
		}else {
			return DEFAULT;
		}
	}
	
}
