package net.lax1dude.eaglercraft.v1_8.opengl;

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
public class FixedFunctionShader {

	public static final int initialSize = 0x8000;
	public static final int initialCount = 3;
	public static final int maxCount = 8;

	public class FixedFunctionState {

		public static final int fixedFunctionStatesCount = 12;
		public static final int fixedFunctionStatesBits = (1 << 12) - 1;
		public static final int extentionStateBits = fixedFunctionStatesBits ^ 0xFFFFFFFF;

		public static final int STATE_HAS_ATTRIB_TEXTURE = 1;
		public static final int STATE_HAS_ATTRIB_COLOR = 2;
		public static final int STATE_HAS_ATTRIB_NORMAL = 4;
		public static final int STATE_HAS_ATTRIB_LIGHTMAP = 8;
		public static final int STATE_ENABLE_TEXTURE2D = 16;
		public static final int STATE_ENABLE_LIGHTMAP = 32;
		public static final int STATE_ENABLE_ALPHA_TEST = 64;
		public static final int STATE_ENABLE_MC_LIGHTING = 128;
		public static final int STATE_ENABLE_END_PORTAL = 256;
		public static final int STATE_ENABLE_ANISOTROPIC_FIX = 512;
		public static final int STATE_ENABLE_FOG = 1024;
		public static final int STATE_ENABLE_BLEND_ADD = 2048;

	}

	public class FixedFunctionConstants {

		public static final String VERSION = "#version 300 es";
		public static final String FILENAME_VSH = "/assets/eagler/glsl/core.vsh";
		public static final String FILENAME_FSH = "/assets/eagler/glsl/core.fsh";

		public static final String PRECISION_INT = "lowp";
		public static final String PRECISION_FLOAT = "highp";
		public static final String PRECISION_SAMPLER = "mediump";
		
		public static final String MACRO_ATTRIB_TEXTURE = "COMPILE_TEXTURE_ATTRIB";
		public static final String MACRO_ATTRIB_COLOR = "COMPILE_COLOR_ATTRIB";
		public static final String MACRO_ATTRIB_NORMAL = "COMPILE_NORMAL_ATTRIB";
		public static final String MACRO_ATTRIB_LIGHTMAP = "COMPILE_LIGHTMAP_ATTRIB";
		
		public static final String MACRO_ENABLE_TEXTURE2D = "COMPILE_ENABLE_TEXTURE2D";
		public static final String MACRO_ENABLE_LIGHTMAP = "COMPILE_ENABLE_LIGHTMAP";
		public static final String MACRO_ENABLE_ALPHA_TEST = "COMPILE_ENABLE_ALPHA_TEST";
		public static final String MACRO_ENABLE_MC_LIGHTING = "COMPILE_ENABLE_MC_LIGHTING";
		public static final String MACRO_ENABLE_END_PORTAL = "COMPILE_ENABLE_TEX_GEN";
		public static final String MACRO_ENABLE_ANISOTROPIC_FIX = "COMPILE_ENABLE_ANISOTROPIC_FIX";
		public static final String MACRO_ENABLE_FOG = "COMPILE_ENABLE_FOG";
		public static final String MACRO_ENABLE_BLEND_ADD = "COMPILE_BLEND_ADD";

		public static final String ATTRIB_POSITION = "a_position3f";
		public static final String ATTRIB_TEXTURE = "a_texture2f";
		public static final String ATTRIB_COLOR = "a_color4f";
		public static final String ATTRIB_NORMAL = "a_normal4f";
		public static final String ATTRIB_LIGHTMAP = "a_lightmap2f";

		public static final String UNIFORM_COLOR_NAME = "u_color4f";
		public static final String UNIFORM_BLEND_SRC_COLOR_NAME = "u_colorBlendSrc4f";
		public static final String UNIFORM_BLEND_ADD_COLOR_NAME = "u_colorBlendAdd4f";
		public static final String UNIFORM_ALPHA_TEST_NAME = "u_alphaTestRef1f";
		public static final String UNIFORM_LIGHTS_ENABLED_NAME = "u_lightsEnabled1i";
		public static final String UNIFORM_LIGHTS_VECTORS_NAME = "u_lightsDirections4fv";
		public static final String UNIFORM_LIGHTS_AMBIENT_NAME = "u_lightsAmbient3f";
		public static final String UNIFORM_CONSTANT_NORMAL_NAME = "u_uniformNormal3f";
		public static final String UNIFORM_FOG_PARAM_NAME = "u_fogParameters4f";
		public static final String UNIFORM_FOG_COLOR_NAME = "u_fogColor4f";
		public static final String UNIFORM_TEX_GEN_S_NAME = "u_texGenS4f";
		public static final String UNIFORM_TEX_GEN_T_NAME = "u_texGenT4f";
		public static final String UNIFORM_TEX_GEN_R_NAME = "u_texGenR4f";
		public static final String UNIFORM_TEX_GEN_Q_NAME = "u_texGenQ4f";
		public static final String UNIFORM_MODEL_MATRIX_NAME = "u_modelviewMat4f";
		public static final String UNIFORM_TEX_GEN_PLANE_NAME = "u_texGenPlane4i";
		public static final String UNIFORM_PROJECTION_MATRIX_NAME = "u_projectionMat4f";
		public static final String UNIFORM_MODEL_PROJECTION_MATRIX_NAME = "u_modelviewProjMat4f";
		public static final String UNIFORM_TEXTURE_COORDS_01_NAME = "u_textureCoords01";
		public static final String UNIFORM_TEXTURE_MATRIX_01_NAME = "u_textureMat4f01";
		public static final String UNIFORM_TEXTURE_COORDS_02_NAME = "u_textureCoords02";
		public static final String UNIFORM_TEXTURE_MATRIX_02_NAME = "u_textureMat4f02";
		public static final String UNIFORM_TEXTURE_ANISOTROPIC_FIX = "u_textureAnisotropicFix";

		public static final String UNIFORM_TEXTURE_UNIT_01_NAME = "u_samplerTexture";
		public static final String UNIFORM_TEXTURE_UNIT_02_NAME = "u_samplerLightmap";
		
		public static final String OUTPUT_COLOR = "output4f";

	}

}
