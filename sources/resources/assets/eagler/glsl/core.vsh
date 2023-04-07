#line 2

/*
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

in vec3 a_position3f;

#if defined(COMPILE_ENABLE_TEX_GEN) || defined(COMPILE_ENABLE_FOG)
#define _COMPILE_VARYING_POSITION
#endif

#ifdef _COMPILE_VARYING_POSITION
out vec4 v_position4f;
#endif

#ifdef COMPILE_ENABLE_TEX_GEN
out vec3 v_objectPosition3f;
#endif

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
#endif

#ifdef COMPILE_LIGHTMAP_ATTRIB
in vec2 a_lightmap2f;
out vec2 v_lightmap2f;
uniform mat4 u_textureMat4f02;
#endif

#ifdef _COMPILE_VARYING_POSITION
uniform mat4 u_modelviewMat4f;
uniform mat4 u_projectionMat4f;
#else
uniform mat4 u_modelviewProjMat4f;
#ifdef COMPILE_NORMAL_ATTRIB
uniform mat4 u_modelviewMat4f;
#endif
#endif

#define TEX_MAT3(mat4In) mat3(mat4In[0].xyw,mat4In[1].xyw,mat4In[3].xyw)

void main() {
#ifdef COMPILE_ENABLE_TEX_GEN
	v_objectPosition3f = a_position3f;
#endif

#ifdef _COMPILE_VARYING_POSITION
	v_position4f = u_modelviewMat4f * vec4(a_position3f, 1.0);
#endif

#ifdef COMPILE_TEXTURE_ATTRIB
	vec3 v_textureTmp3f = TEX_MAT3(u_textureMat4f01) * vec3(a_texture2f, 1.0);
	v_texture2f = v_textureTmp3f.xy / v_textureTmp3f.z;
#endif
	
#ifdef COMPILE_COLOR_ATTRIB
	v_color4f = a_color4f;
#endif
	
#ifdef COMPILE_NORMAL_ATTRIB
	v_normal3f = normalize(mat3(u_modelviewMat4f) * a_normal4f.xyz);
#endif
	
#ifdef COMPILE_LIGHTMAP_ATTRIB
	vec3 v_lightmapTmp3f = TEX_MAT3(u_textureMat4f02) * vec3(a_lightmap2f, 1.0);
	v_lightmap2f = v_lightmapTmp3f.xy / v_lightmapTmp3f.z;
#endif

#ifdef _COMPILE_VARYING_POSITION
	gl_Position = u_projectionMat4f * v_position4f;
#else
	gl_Position = u_modelviewProjMat4f * vec4(a_position3f, 1.0);
#endif
}
