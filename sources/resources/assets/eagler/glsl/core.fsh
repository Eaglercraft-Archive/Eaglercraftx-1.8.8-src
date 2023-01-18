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

#if defined(COMPILE_ENABLE_TEX_GEN) || defined(COMPILE_ENABLE_FOG)
in vec4 v_position4f;
#endif

#ifdef COMPILE_TEXTURE_ATTRIB
in vec2 v_texture2f;
#endif

uniform vec4 u_color4f;

#ifdef COMPILE_BLEND_ADD
uniform vec4 u_colorBlendSrc4f;
uniform vec4 u_colorBlendAdd4f;
#endif

#ifdef COMPILE_COLOR_ATTRIB
in vec4 v_color4f;
#endif

#ifdef COMPILE_NORMAL_ATTRIB
in vec3 v_normal3f;
#endif

#ifdef COMPILE_LIGHTMAP_ATTRIB
in vec2 v_lightmap2f;
#endif

#ifdef COMPILE_ENABLE_TEXTURE2D
uniform sampler2D u_samplerTexture;
#if !defined(COMPILE_TEXTURE_ATTRIB) && !defined(COMPILE_ENABLE_TEX_GEN)
uniform vec2 u_textureCoords01;
#endif
#endif

#ifdef COMPILE_ENABLE_LIGHTMAP
uniform sampler2D u_samplerLightmap;
#ifndef COMPILE_LIGHTMAP_ATTRIB
uniform vec2 u_textureCoords02;
#endif
#endif

#ifdef COMPILE_ENABLE_ALPHA_TEST
uniform float u_alphaTestRef1f;
#endif

#ifdef COMPILE_ENABLE_MC_LIGHTING
uniform int u_lightsEnabled1i;
uniform vec4 u_lightsDirections4fv[4];
uniform vec3 u_lightsAmbient3f;
#ifndef COMPILE_NORMAL_ATTRIB
uniform vec3 u_uniformNormal3f;
#endif
#endif

#ifdef COMPILE_ENABLE_FOG
uniform vec4 u_fogParameters4f;
uniform vec4 u_fogColor4f;
#endif

#ifdef COMPILE_ENABLE_TEX_GEN
in vec3 v_objectPosition3f;
uniform ivec4 u_texGenPlane4i;
uniform vec4 u_texGenS4f;
uniform vec4 u_texGenT4f;
uniform vec4 u_texGenR4f;
uniform vec4 u_texGenQ4f;
uniform mat4 u_textureMat4f01;
#endif

#ifdef COMPILE_ENABLE_ANISOTROPIC_FIX
uniform vec2 u_textureAnisotropicFix;
#endif

layout(location = 0) out vec4 output4f;

void main() {

#ifdef COMPILE_COLOR_ATTRIB
	vec4 color = v_color4f * u_color4f;
#else
	vec4 color = u_color4f;
#endif
	
#ifdef COMPILE_ENABLE_TEX_GEN
	vec4 texGenVector;
	
	vec4 texGenPosSrc[2];
	texGenPosSrc[0] = vec4(v_objectPosition3f, 1.0);
	texGenPosSrc[1] = v_position4f;
	
	texGenVector.x = dot(texGenPosSrc[u_texGenPlane4i.x], u_texGenS4f);
	texGenVector.y = dot(texGenPosSrc[u_texGenPlane4i.y], u_texGenT4f);
	texGenVector.z = dot(texGenPosSrc[u_texGenPlane4i.z], u_texGenR4f);
	texGenVector.w = dot(texGenPosSrc[u_texGenPlane4i.w], u_texGenQ4f);
	
	texGenVector = u_textureMat4f01 * texGenVector;
	color *= texture(u_samplerTexture, texGenVector.xy / texGenVector.w);
	
#ifdef COMPILE_ENABLE_ALPHA_TEST
	if(color.a < u_alphaTestRef1f) discard;
#endif

#else

#ifdef COMPILE_ENABLE_TEXTURE2D
#ifdef COMPILE_TEXTURE_ATTRIB
#ifdef COMPILE_ENABLE_ANISOTROPIC_FIX
	// d3d11 doesn't support GL_NEAREST upscaling with anisotropic
	// filtering enabled, so it needs this stupid fix to 'work'
	vec2 uv = floor(v_texture2f * u_textureAnisotropicFix) + 0.5;
	color *= texture(u_samplerTexture, uv / u_textureAnisotropicFix);
#else
	color *= texture(u_samplerTexture, v_texture2f);
#endif
#else
	color *= texture(u_samplerTexture, u_textureCoords01);
#endif
#endif

#ifdef COMPILE_ENABLE_LIGHTMAP
#ifdef COMPILE_LIGHTMAP_ATTRIB
	color *= texture(u_samplerLightmap, v_lightmap2f);
#else
	color *= texture(u_samplerLightmap, u_textureCoords02);
#endif
#endif

#ifdef COMPILE_ENABLE_ALPHA_TEST
	if(color.a < u_alphaTestRef1f) discard;
#endif

#endif

#ifdef COMPILE_ENABLE_MC_LIGHTING
#ifdef COMPILE_NORMAL_ATTRIB
	vec3 normal = normalize(v_normal3f);
#else
	vec3 normal = u_uniformNormal3f;
#endif
	float diffuse = 0.0;
	for(int i = 0; i < u_lightsEnabled1i; ++i) {
		vec4 light = u_lightsDirections4fv[i];
		diffuse += max(dot(light.xyz, normal), 0.0) * light.w;
	}
	color.rgb *= min(u_lightsAmbient3f + vec3(diffuse), 1.0);
#endif

#ifdef COMPILE_BLEND_ADD
	color.rgba = color.rgba * u_colorBlendSrc4f + u_colorBlendAdd4f;
#endif

#ifdef COMPILE_ENABLE_FOG
	vec3 fogPos = v_position4f.xyz / v_position4f.w;
	float dist = sqrt(dot(fogPos, fogPos));
	float fogDensity = u_fogParameters4f.y;
	float fogStart = u_fogParameters4f.z;
	float fogEnd = u_fogParameters4f.w;
	float f = u_fogParameters4f.x > 0.0 ? 1.0 - pow(2.718, -(fogDensity * dist)) :
		(dist - fogStart) / (fogEnd - fogStart);
	color.rgb = mix(color.rgb, u_fogColor4f.rgb, clamp(f, 0.0, 1.0) * u_fogColor4f.a);
#endif

	output4f = color;
}
