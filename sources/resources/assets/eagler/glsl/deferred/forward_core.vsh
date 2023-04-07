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

out vec4 v_position4f;

#ifdef COMPILE_FOG_LIGHT_SHAFTS
out vec2 v_positionClip2f;
#endif

#ifdef COMPILE_TEXTURE_ATTRIB
in vec2 a_texture2f;
out vec2 v_texture2f;
uniform mat4 u_textureMat4f01;
#endif

#ifdef COMPILE_ENABLE_TEX_GEN
out vec3 v_objectPosition3f;
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

#ifdef COMPILE_LIGHTMAP_ATTRIB
in vec2 a_lightmap2f;
out vec2 v_lightmap2f;
uniform mat4 u_textureMat4f02;
#endif

uniform mat4 u_modelviewMat4f;

#ifdef COMPILE_PARABOLOID
uniform float u_farPlane1f;
#else
uniform mat4 u_projectionMat4f;
#endif

#define TEX_MAT3(mat4In) mat3(mat4In[0].xyw,mat4In[1].xyw,mat4In[3].xyw)

void main() {
#ifdef COMPILE_ENABLE_TEX_GEN
	v_objectPosition3f = a_position3f;
#endif
	v_position4f = u_modelviewMat4f * vec4(a_position3f, 1.0);

#ifdef COMPILE_TEXTURE_ATTRIB
	vec3 v_textureTmp3f = TEX_MAT3(u_textureMat4f01) * vec3(a_texture2f, 1.0);
	v_texture2f = v_textureTmp3f.xy / v_textureTmp3f.z;
#endif
	
#ifdef COMPILE_COLOR_ATTRIB
	v_color4f = a_color4f;
#endif
	
#ifdef COMPILE_NORMAL_ATTRIB
	v_normal3f = normalize(mat3(u_modelviewMat4f) * a_normal4f.xyz);
	v_block1f = floor((a_normal4f.w + 1.0) * 127.0 + 0.5);
#endif
	
#ifdef COMPILE_LIGHTMAP_ATTRIB
	vec3 v_lightmapTmp3f = TEX_MAT3(u_textureMat4f02) * vec3(a_lightmap2f, 1.0);
	v_lightmap2f = v_lightmapTmp3f.xy / v_lightmapTmp3f.z;
#endif

#ifdef COMPILE_PARABOLOID
	vec3 pos = v_position4f.xyz / v_position4f.w;
	float dist = pos.z;
	pos.xyz = normalize(pos.xyz);
	pos.xy /= 1.0 - pos.z;
	pos.z = dist / u_farPlane1f + 1.00005;
	gl_Position = vec4(pos.xyz, 1.0);
#else
	gl_Position = u_projectionMat4f * v_position4f;
#endif

#ifdef COMPILE_FOG_LIGHT_SHAFTS
	v_positionClip2f = gl_Position.xy / gl_Position.w;
#endif
}
