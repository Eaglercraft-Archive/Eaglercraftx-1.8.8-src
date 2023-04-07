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

#ifdef COMPILE_TEXTURE_ATTRIB
in vec2 v_texture2f;
#endif

uniform vec4 u_color4f;

#ifdef COMPILE_COLOR_ATTRIB
in vec4 v_color4f;
#endif

#ifdef COMPILE_NORMAL_ATTRIB
in vec3 v_normal3f;
in float v_block1f;
#else
uniform vec3 u_uniformNormal3f;
uniform float u_blockConstant1f;
#endif

uniform float u_useEnvMap1f;

#ifdef COMPILE_LIGHTMAP_ATTRIB
in vec2 v_lightmap2f;
#endif

#ifdef COMPILE_ENABLE_TEXTURE2D
uniform sampler2D u_samplerTexture;
#ifndef COMPILE_TEXTURE_ATTRIB
uniform vec2 u_textureCoords01;
#endif
#endif

#ifdef COMPILE_ENABLE_LIGHTMAP
#ifndef COMPILE_LIGHTMAP_ATTRIB
uniform vec2 u_textureCoords02;
#endif
#endif

#ifdef COMPILE_ENABLE_ALPHA_TEST
uniform float u_alphaTestRef1f;
#endif

#ifdef COMPILE_ENABLE_ANISOTROPIC_FIX
uniform vec2 u_textureAnisotropicFix;
#endif

#ifdef COMPILE_BLEND_ADD
uniform vec4 u_colorBlendSrc4f;
uniform vec4 u_colorBlendAdd4f;
#endif

#ifdef COMPILE_NORMAL_MATERIAL_TEXTURE
uniform sampler2D u_samplerNormalMaterial;
in vec3 v_viewdir3f;
#ifndef COMPILE_TEXTURE_ATTRIB
uniform vec2 u_textureCoords01;
#endif
#else
uniform vec3 u_materialConstants3f;
#endif

#ifdef COMPILE_NORMAL_MATERIAL_TEXTURE
mat3 cotangent_frame(in vec3 N, in vec3 p, in vec2 uv) {
	vec3 dp1 = dFdx(p);
	vec3 dp2 = dFdy(p);
	vec2 duv1 = dFdx(uv);
	vec2 duv2 = dFdy(uv);
	vec3 dp2perp = cross(dp2, N);
	vec3 dp1perp = cross(N, dp1);
	vec3 T = dp2perp * duv1.x + dp1perp * duv2.x;
	vec3 B = dp2perp * duv1.y + dp1perp * duv2.y;
	float invmax = inversesqrt(max(dot(T,T), dot(B,B)));
	return mat3(T * invmax, B * invmax, N);
}
#endif

layout(location = 0) out vec4 gbufferColor4f;
layout(location = 1) out vec4 gbufferNormal4f;
layout(location = 2) out vec4 gbufferMaterial4f;

void main() {
#ifdef COMPILE_COLOR_ATTRIB
	vec4 color = v_color4f * u_color4f;
#else
	vec4 color = u_color4f;
#endif

	vec3 normal;
#ifdef COMPILE_NORMAL_ATTRIB
	normal = normalize(v_normal3f);
#else
	normal = u_uniformNormal3f;
#endif

#if defined(COMPILE_ENABLE_TEXTURE2D) || defined(COMPILE_NORMAL_MATERIAL_TEXTURE)
	vec2 uv;
#ifdef COMPILE_TEXTURE_ATTRIB
#ifdef COMPILE_ENABLE_ANISOTROPIC_FIX
	uv = floor(uv * u_textureAnisotropicFix) + 0.5;
	uv /= u_textureAnisotropicFix;
#else
	uv = v_texture2f;
#endif
#else
	uv = u_textureCoords01;
#endif
#ifdef COMPILE_ENABLE_TEXTURE2D
	color *= texture(u_samplerTexture, uv);
#endif
#endif

	vec2 lightmap = vec2(0.0, 1.0);

#ifdef COMPILE_ENABLE_LIGHTMAP
#ifdef COMPILE_LIGHTMAP_ATTRIB
	lightmap = v_lightmap2f;
#else
	lightmap = u_textureCoords02;
#endif
#endif

#ifdef COMPILE_BLEND_ADD
	color = color * u_colorBlendSrc4f + u_colorBlendAdd4f;
#endif

#ifdef COMPILE_ENABLE_ALPHA_TEST
	if(color.a < u_alphaTestRef1f) discard;
#endif

#ifdef COMPILE_NORMAL_MATERIAL_TEXTURE
	vec2 uv2 = vec2(1.0, 0.5) * uv;
	vec2 normal2 = texture(u_samplerNormalMaterial, uv2).xy;
	mat3 cf;
	if(normal2.x + normal2.y > 0.0) {
		normal2 *= 2.0;
		normal2 -= 1.0;
		cf = cotangent_frame(normal, normalize(v_viewdir3f), uv);
		normal = cf * vec3(normal2, sqrt(1.0 - dot(normal2, normal2)));
	}
	uv2.y += 0.5;
	vec3 material = texture(u_samplerNormalMaterial, uv2).rgb;
#else
	vec3 material = u_materialConstants3f;
#endif

	gbufferColor4f.rgb = color.rgb;
	gbufferColor4f.a = lightmap.r;
	gbufferNormal4f.rgb = normal * 0.5 + 0.5;
	gbufferNormal4f.a = lightmap.g;
	gbufferMaterial4f = vec4(material.rgb, u_useEnvMap1f);
}
