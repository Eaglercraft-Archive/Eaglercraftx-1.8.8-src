#line 2

/*
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

in vec3 a_position3f;

#ifdef COMPILE_TEXTURE_ATTRIB
in vec2 a_texture2f;
out vec2 v_texture2f;
uniform mat4 u_textureMat4f01;
#endif

#ifdef COMPILE_COLOR_ATTRIB
in vec4 a_color4f;
out vec4 v_color4f;
#endif

#ifdef COMPILE_NORMAL_ATTRIB
in vec4 a_normal4f;
out vec3 v_normal3f;
out float v_block1f;
#endif

#ifdef COMPILE_STATE_WAVING_BLOCKS
uniform mat4 u_modelMatrix4f;
uniform mat4 u_viewMatrix4f;
uniform vec3 u_wavingBlockOffset3f;
uniform vec4 u_wavingBlockParam4f;
#ifndef COMPILE_NORMAL_ATTRIB
uniform float u_blockConstant1f;
#endif
#define DO_COMPILE_STATE_WAVING_BLOCKS
#define FAKE_SIN(valueIn, valueOut)\
	valueOut = abs(1.0 - fract(valueIn * 0.159155) * 2.0);\
	valueOut = valueOut * valueOut * (3.0 - 2.0 * valueOut) * 2.0 - 1.0;
#define LIB_INCLUDE_WAVING_BLOCKS_FUNCTION
#endif

#EAGLER INCLUDE (2) "eagler:glsl/deferred/lib/waving_blocks.glsl"

#ifdef COMPILE_NORMAL_MATERIAL_TEXTURE
out vec3 v_viewdir3f;
#endif

#ifdef COMPILE_LIGHTMAP_ATTRIB
in vec2 a_lightmap2f;
out vec2 v_lightmap2f;
uniform mat4 u_textureMat4f02;
#endif

uniform mat4 u_modelviewMat4f;
uniform mat4 u_projectionMat4f;

#define TEX_MAT3(mat4In) mat3(mat4In[0].xyw,mat4In[1].xyw,mat4In[3].xyw)

void main() {

#ifdef COMPILE_TEXTURE_ATTRIB
	vec3 v_textureTmp3f = TEX_MAT3(u_textureMat4f01) * vec3(a_texture2f, 1.0);
	v_texture2f = v_textureTmp3f.xy / v_textureTmp3f.z;
#endif

#ifdef COMPILE_COLOR_ATTRIB
	v_color4f = a_color4f;
#endif

#ifdef COMPILE_NORMAL_ATTRIB
	v_normal3f = normalize(mat3(u_modelviewMat4f) * a_normal4f.xyz);
	float blockId = v_block1f = floor((a_normal4f.w + 1.0) * 127.0 + 0.5);
#endif

#ifdef COMPILE_LIGHTMAP_ATTRIB
	vec3 v_lightmapTmp3f = TEX_MAT3(u_textureMat4f02) * vec3(a_lightmap2f, 1.0);
	v_lightmap2f = v_lightmapTmp3f.xy / v_lightmapTmp3f.z;
#endif

	vec4 pos = vec4(a_position3f, 1.0);

#ifdef DO_COMPILE_STATE_WAVING_BLOCKS
#ifndef COMPILE_NORMAL_ATTRIB
	float blockId = u_blockConstant1f;
#endif
#ifdef COMPILE_LIGHTMAP_ATTRIB
	if(v_lightmap2f.y > 0.33) {
		COMPUTE_WAVING_BLOCKS(pos, min(v_lightmap2f.y * 3.0 - 1.0, 1.0), 24.0, blockId, u_modelMatrix4f, u_viewMatrix4f, u_modelviewMat4f, u_wavingBlockOffset3f, u_wavingBlockParam4f)
	}else {
		pos = u_modelviewMat4f * pos;
	}
#else
	COMPUTE_WAVING_BLOCKS(pos, 1.0, 32.0, blockId, u_modelMatrix4f, u_viewMatrix4f, u_modelviewMat4f, u_wavingBlockOffset3f, u_wavingBlockParam4f)
#endif
#else
	pos = u_modelviewMat4f * pos;
#endif

#ifdef COMPILE_NORMAL_MATERIAL_TEXTURE
	v_viewdir3f = pos.xyz / pos.w;
#endif

	gl_Position = u_projectionMat4f * pos;

}
