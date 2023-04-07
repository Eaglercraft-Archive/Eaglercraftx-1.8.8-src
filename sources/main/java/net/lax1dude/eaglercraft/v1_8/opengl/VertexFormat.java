package net.lax1dude.eaglercraft.v1_8.opengl;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

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
public enum VertexFormat {

	BLOCK(true, true, false, true),
	BLOCK_SHADERS(true, true, true, true),
	ITEM(true, true, true, false),
	OLDMODEL_POSITION_TEX_NORMAL(true, false, true, false),
	PARTICLE_POSITION_TEX_COLOR_LMAP(true, true, true, true),
	POSITION(false, false, false, false),
	POSITION_COLOR(false, true, false, false),
	POSITION_TEX(true, false, false, false),
	POSITION_NORMAL(false, false, true, false),
	POSITION_TEX_COLOR(true, true, false, false),
	POSITION_TEX_NORMAL(true, false, true, false),
	POSITION_TEX_LMAP_COLOR(true, true, false, true),
	POSITION_TEX_COLOR_NORMAL(true, true, true, false);

	public static final int COMPONENT_POSITION_SIZE = 3;
	public static final int COMPONENT_POSITION_FORMAT = GL_FLOAT;
	public static final int COMPONENT_POSITION_STRIDE = 12;

	public static final int COMPONENT_TEX_SIZE = 2;
	public static final int COMPONENT_TEX_FORMAT = GL_FLOAT;
	public static final int COMPONENT_TEX_STRIDE = 8;

	public static final int COMPONENT_COLOR_SIZE = 4;
	public static final int COMPONENT_COLOR_FORMAT = GL_UNSIGNED_BYTE;
	public static final int COMPONENT_COLOR_STRIDE = 4;

	public static final int COMPONENT_NORMAL_SIZE = 4;
	public static final int COMPONENT_NORMAL_FORMAT = GL_BYTE;
	public static final int COMPONENT_NORMAL_STRIDE = 4;

	public static final int COMPONENT_LIGHTMAP_SIZE = 2;
	public static final int COMPONENT_LIGHTMAP_FORMAT = GL_UNSIGNED_SHORT;
	public static final int COMPONENT_LIGHTMAP_STRIDE = 4;

	public final boolean attribPositionEnabled;
	public final int attribPositionIndex;
	public final int attribPositionOffset;
	public final int attribPositionFormat;
	public final boolean attribPositionNormalized;
	public final int attribPositionSize;
	public final int attribPositionStride;

	public final boolean attribTextureEnabled;
	public final int attribTextureIndex;
	public final int attribTextureOffset;
	public final int attribTextureFormat;
	public final boolean attribTextureNormalized;
	public final int attribTextureSize;
	public final int attribTextureStride;

	public final boolean attribColorEnabled;
	public final int attribColorIndex;
	public final int attribColorOffset;
	public final int attribColorFormat;
	public final boolean attribColorNormalized;
	public final int attribColorSize;
	public final int attribColorStride;

	public final boolean attribNormalEnabled;
	public final int attribNormalIndex;
	public final int attribNormalOffset;
	public final int attribNormalFormat;
	public final boolean attribNormalNormalized;
	public final int attribNormalSize;
	public final int attribNormalStride;

	public final boolean attribLightmapEnabled;
	public final int attribLightmapIndex;
	public final int attribLightmapOffset;
	public final int attribLightmapFormat;
	public final boolean attribLightmapNormalized;
	public final int attribLightmapSize;
	public final int attribLightmapStride;

	public final int attribCount;
	public final int attribStride;
	
	public final int eaglercraftAttribBits;

	private VertexFormat(boolean texture, boolean color, boolean normal, boolean lightmap) {
		
		int index = 0;
		int bytes = 0;
		int bitfield = 0;
		
		attribPositionEnabled = true;
		attribPositionIndex = index++;
		attribPositionOffset = bytes;
		attribPositionFormat = COMPONENT_POSITION_FORMAT;
		attribPositionNormalized = false;
		attribPositionSize = COMPONENT_POSITION_SIZE;
		bytes += COMPONENT_POSITION_STRIDE;
		
		if(color) {
			attribColorEnabled = true;
			attribColorIndex = index++;
			attribColorOffset = bytes;
			attribColorFormat = COMPONENT_COLOR_FORMAT;
			attribColorNormalized = true;
			attribColorSize = COMPONENT_COLOR_SIZE;
			bytes += COMPONENT_COLOR_STRIDE;
			bitfield |= EaglercraftGPU.ATTRIB_COLOR;
		}else {
			attribColorEnabled = false;
			attribColorIndex = -1;
			attribColorOffset = -1;
			attribColorFormat = -1;
			attribColorNormalized = false;
			attribColorSize = -1;
		}

		if(texture) {
			attribTextureEnabled = true;
			attribTextureIndex = index++;
			attribTextureOffset = bytes;
			attribTextureFormat = COMPONENT_TEX_FORMAT;
			attribTextureNormalized = false;
			attribTextureSize = COMPONENT_TEX_SIZE;
			bytes += COMPONENT_TEX_STRIDE;
			bitfield |= EaglercraftGPU.ATTRIB_TEXTURE;
		}else {
			attribTextureEnabled = false;
			attribTextureIndex = -1;
			attribTextureOffset = -1;
			attribTextureFormat = -1;
			attribTextureNormalized = false;
			attribTextureSize = -1;
		}
		
		if(normal) {
			attribNormalEnabled = true;
			attribNormalIndex = index++;
			attribNormalOffset = bytes;
			attribNormalFormat = COMPONENT_NORMAL_FORMAT;
			attribNormalNormalized = true;
			attribNormalSize = COMPONENT_NORMAL_SIZE;
			bytes += COMPONENT_NORMAL_STRIDE;
			bitfield |= EaglercraftGPU.ATTRIB_NORMAL;
		}else {
			attribNormalEnabled = false;
			attribNormalIndex = -1;
			attribNormalOffset = -1;
			attribNormalFormat = -1;
			attribNormalNormalized = false;
			attribNormalSize = -1;
		}
		
		if(lightmap) {
			attribLightmapEnabled = true;
			attribLightmapIndex = index++;
			attribLightmapOffset = bytes;
			attribLightmapFormat = COMPONENT_LIGHTMAP_FORMAT;
			attribLightmapNormalized = false;
			attribLightmapSize = COMPONENT_LIGHTMAP_SIZE;
			bytes += COMPONENT_LIGHTMAP_STRIDE;
			bitfield |= EaglercraftGPU.ATTRIB_LIGHTMAP;
		}else {
			attribLightmapEnabled = false;
			attribLightmapIndex = -1;
			attribLightmapOffset = -1;
			attribLightmapFormat = -1;
			attribLightmapNormalized = false;
			attribLightmapSize = -1;
		}
		
		attribCount = index;
		attribStride = attribPositionStride =  bytes;
		attribColorStride = color ? bytes : -1;
		attribTextureStride = texture ? bytes : -1;
		attribNormalStride = normal ? bytes : -1;
		attribLightmapStride = lightmap ? bytes : -1;
		eaglercraftAttribBits = bitfield;
		
	}
	
}
