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
class FixedFunctionShader {

	class FixedFunctionConstants {

		static final String VERSION = "#version 300 es";
		static final String FILENAME_VSH = "/assets/eagler/glsl/core.vsh";
		static final String FILENAME_FSH = "/assets/eagler/glsl/core.fsh";

		static final String PRECISION_INT = "lowp";
		static final String PRECISION_FLOAT = "mediump";
		static final String PRECISION_SAMPLER = "lowp";
		
		static final String MACRO_ATTRIB_TEXTURE = "COMPILE_TEXTURE_ATTRIB";
		static final String MACRO_ATTRIB_COLOR = "COMPILE_COLOR_ATTRIB";
		static final String MACRO_ATTRIB_NORMAL = "COMPILE_NORMAL_ATTRIB";
		static final String MACRO_ATTRIB_LIGHTMAP = "COMPILE_LIGHTMAP_ATTRIB";
		
		static final String MACRO_ENABLE_TEXTURE2D = "COMPILE_ENABLE_TEXTURE2D";
		static final String MACRO_ENABLE_LIGHTMAP = "COMPILE_ENABLE_LIGHTMAP";
		static final String MACRO_ENABLE_ALPHA_TEST = "COMPILE_ENABLE_ALPHA_TEST";
		static final String MACRO_ENABLE_MC_LIGHTING = "COMPILE_ENABLE_MC_LIGHTING";
		static final String MACRO_ENABLE_END_PORTAL = "COMPILE_ENABLE_TEX_GEN";
		static final String MACRO_ENABLE_ANISOTROPIC_FIX = "COMPILE_ENABLE_ANISOTROPIC_FIX";
		static final String MACRO_ENABLE_FOG = "COMPILE_ENABLE_FOG";
		static final String MACRO_ENABLE_BLEND_ADD = "COMPILE_BLEND_ADD";

		static final String ATTRIB_POSITION = "a_position3f";
		static final String ATTRIB_TEXTURE = "a_texture2f";
		static final String ATTRIB_COLOR = "a_color4f";
		static final String ATTRIB_NORMAL = "a_normal4f";
		static final String ATTRIB_LIGHTMAP = "a_lightmap2f";

		static final String UNIFORM_COLOR_NAME = "u_color4f";
		static final String UNIFORM_BLEND_SRC_COLOR_NAME = "u_colorBlendSrc4f";
		static final String UNIFORM_BLEND_ADD_COLOR_NAME = "u_colorBlendAdd4f";
		static final String UNIFORM_ALPHA_TEST_NAME = "u_alphaTestRef1f";
		static final String UNIFORM_LIGHTS_ENABLED_NAME = "u_lightsEnabled1i";
		static final String UNIFORM_LIGHTS_VECTORS_NAME = "u_lightsDirections4fv";
		static final String UNIFORM_LIGHTS_AMBIENT_NAME = "u_lightsAmbient3f";
		static final String UNIFORM_CONSTANT_NORMAL_NAME = "u_uniformNormal3f";
		static final String UNIFORM_FOG_PARAM_NAME = "u_fogParameters4f";
		static final String UNIFORM_FOG_COLOR_NAME = "u_fogColor4f";
		static final String UNIFORM_TEX_GEN_S_NAME = "u_texGenS4f";
		static final String UNIFORM_TEX_GEN_T_NAME = "u_texGenT4f";
		static final String UNIFORM_TEX_GEN_R_NAME = "u_texGenR4f";
		static final String UNIFORM_TEX_GEN_Q_NAME = "u_texGenQ4f";
		static final String UNIFORM_MODEL_MATRIX_NAME = "u_modelviewMat4f";
		static final String UNIFORM_TEX_GEN_PLANE_NAME = "u_texGenPlane4i";
		static final String UNIFORM_PROJECTION_MATRIX_NAME = "u_projectionMat4f";
		static final String UNIFORM_TEXTURE_COORDS_01_NAME = "u_textureCoords01";
		static final String UNIFORM_TEXTURE_MATRIX_01_NAME = "u_textureMat4f01";
		static final String UNIFORM_TEXTURE_COORDS_02_NAME = "u_textureCoords02";
		static final String UNIFORM_TEXTURE_MATRIX_02_NAME = "u_textureMat4f02";
		static final String UNIFORM_TEXTURE_ANISOTROPIC_FIX = "u_textureAnisotropicFix";

		static final String UNIFORM_TEXTURE_UNIT_01_NAME = "u_samplerTexture";
		static final String UNIFORM_TEXTURE_UNIT_02_NAME = "u_samplerLightmap";
		
		static final String OUTPUT_COLOR = "output4f";
		
	}
	
}
