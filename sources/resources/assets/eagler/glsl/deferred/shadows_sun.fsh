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

precision lowp int;
precision highp float;
precision highp sampler2D;
precision highp sampler2DShadow;

in vec2 v_position2f;

#ifdef COMPILE_COLORED_SHADOW
layout(location = 0) out vec4 output4f;
#else
layout(location = 0) out float output1f;
#endif

uniform sampler2D u_gbufferNormalTexture;
uniform sampler2D u_gbufferDepthTexture;
uniform sampler2DShadow u_sunShadowDepthTexture;
#ifdef COMPILE_COLORED_SHADOW
uniform sampler2D u_sunShadowColorTexture;
#endif

uniform mat4 u_inverseViewMatrix4f;
uniform mat4 u_inverseViewProjMatrix4f;

#ifdef COMPILE_SUN_SHADOW_LOD0
uniform mat4 u_sunShadowMatrixLOD04f;
#define SUN_SHADOW_MAP_FRAC 1.0
#endif
#ifdef COMPILE_SUN_SHADOW_LOD1
uniform mat4 u_sunShadowMatrixLOD04f;
uniform mat4 u_sunShadowMatrixLOD14f;
#define SUN_SHADOW_MAP_FRAC 0.5
#endif
#ifdef COMPILE_SUN_SHADOW_LOD2
uniform mat4 u_sunShadowMatrixLOD04f;
uniform mat4 u_sunShadowMatrixLOD14f;
uniform mat4 u_sunShadowMatrixLOD24f;
#define SUN_SHADOW_MAP_FRAC 0.3333333
#endif
#ifdef COMPILE_SUN_SHADOW_SMOOTH
const vec2 POISSON_DISK[7] = vec2[](
vec2(-0.077, 0.995), vec2(0.998, 0.015),
vec2(-0.116, -0.987), vec2(-0.916, 0.359),
vec2(-0.697, -0.511), vec2(0.740, -0.612),
vec2(0.675, 0.682));
#define SMOOTH_SHADOW_SAMPLES 1.0 / 8.0
#define SMOOTH_SHADOW_RADIUS 0.00075
#define SMOOTH_SHADOW_POISSON_SAMPLE(idx, tex, lod, vec3Pos, accum, tmpVec2)\
	tmpVec2 = vec3Pos.xy + POISSON_DISK[idx] * SMOOTH_SHADOW_RADIUS;\
	tmpVec2 = clamp(tmpVec2, vec2(0.001), vec2(0.999));\
	tmpVec2.y += lod;\
	tmpVec2.y *= SUN_SHADOW_MAP_FRAC;\
	accum += textureLod(tex, vec3(tmpVec2, vec3Pos.z), 0.0) * SMOOTH_SHADOW_SAMPLES;
#endif

uniform vec3 u_sunDirection3f;

void main() {
#ifdef COMPILE_COLORED_SHADOW
	output4f = vec4(0.0);
#else
	output1f = 0.0;
#endif
	float depth = textureLod(u_gbufferDepthTexture, v_position2f, 0.0).r;
	if(depth < 0.00001) {
		return;
	}
	vec4 normalVector4f = textureLod(u_gbufferNormalTexture, v_position2f, 0.0);
	if(normalVector4f.a < 0.5) {
		return;
	}
	normalVector4f.xyz *= 2.0;
	normalVector4f.xyz -= 1.0;
	vec3 worldSpaceNormal = normalize(mat3(u_inverseViewMatrix4f) * normalVector4f.xyz);
	if(dot(u_sunDirection3f, worldSpaceNormal) < 0.0) {
		return;
	}
	vec4 worldSpacePosition = vec4(v_position2f, depth, 1.0);
	worldSpacePosition.xyz *= 2.0;
	worldSpacePosition.xyz -= 1.0;
	worldSpacePosition = u_inverseViewProjMatrix4f * worldSpacePosition;
	worldSpacePosition.xyz /= worldSpacePosition.w;
	worldSpacePosition.xyz += worldSpaceNormal * 0.05;
	worldSpacePosition.w = 1.0;
	float skyLight = max(normalVector4f.a * 2.0 - 1.0, 0.0);
	float shadowSample;
	vec2 tmpVec2;
	vec4 shadowSpacePosition;
	for(;;) {
		shadowSpacePosition = u_sunShadowMatrixLOD04f * worldSpacePosition;
		if(shadowSpacePosition.xyz == clamp(shadowSpacePosition.xyz, vec3(0.005), vec3(0.995))) {
			shadowSample = textureLod(u_sunShadowDepthTexture, vec3(shadowSpacePosition.xy * vec2(1.0, SUN_SHADOW_MAP_FRAC), shadowSpacePosition.z), 0.0);
#ifdef COMPILE_SUN_SHADOW_SMOOTH
			shadowSample *= SMOOTH_SHADOW_SAMPLES;
			SMOOTH_SHADOW_POISSON_SAMPLE(0, u_sunShadowDepthTexture, 0.0, shadowSpacePosition.xyz, shadowSample, tmpVec2)
			SMOOTH_SHADOW_POISSON_SAMPLE(1, u_sunShadowDepthTexture, 0.0, shadowSpacePosition.xyz, shadowSample, tmpVec2)
			SMOOTH_SHADOW_POISSON_SAMPLE(2, u_sunShadowDepthTexture, 0.0, shadowSpacePosition.xyz, shadowSample, tmpVec2)
			SMOOTH_SHADOW_POISSON_SAMPLE(3, u_sunShadowDepthTexture, 0.0, shadowSpacePosition.xyz, shadowSample, tmpVec2)
			SMOOTH_SHADOW_POISSON_SAMPLE(4, u_sunShadowDepthTexture, 0.0, shadowSpacePosition.xyz, shadowSample, tmpVec2)
			SMOOTH_SHADOW_POISSON_SAMPLE(5, u_sunShadowDepthTexture, 0.0, shadowSpacePosition.xyz, shadowSample, tmpVec2)
			SMOOTH_SHADOW_POISSON_SAMPLE(6, u_sunShadowDepthTexture, 0.0, shadowSpacePosition.xyz, shadowSample, tmpVec2)
			shadowSample = max(shadowSample * 2.0 - 1.0, 0.0);
#endif
#ifdef COMPILE_COLORED_SHADOW
			shadowSpacePosition.y *= SUN_SHADOW_MAP_FRAC;
#endif
			break;
		}

#if defined(COMPILE_SUN_SHADOW_LOD1) || defined(COMPILE_SUN_SHADOW_LOD2)
		shadowSpacePosition = u_sunShadowMatrixLOD14f * worldSpacePosition;
		if(shadowSpacePosition.xyz == clamp(shadowSpacePosition.xyz, vec3(0.005), vec3(0.995))) {
			shadowSpacePosition.y += 1.0;
			shadowSpacePosition.y *= SUN_SHADOW_MAP_FRAC;
			shadowSample = textureLod(u_sunShadowDepthTexture, vec3(shadowSpacePosition.xy, shadowSpacePosition.z + 0.00015), 0.0);
			break;
		}
#endif

#ifdef COMPILE_SUN_SHADOW_LOD2
		shadowSpacePosition = u_sunShadowMatrixLOD24f * worldSpacePosition;
		if(shadowSpacePosition.xyz == clamp(shadowSpacePosition.xyz, vec3(0.005), vec3(0.995))) {
			shadowSpacePosition.y += 2.0;
			shadowSpacePosition.y *= SUN_SHADOW_MAP_FRAC;
			shadowSample = textureLod(u_sunShadowDepthTexture, vec3(shadowSpacePosition.xy, shadowSpacePosition.z + 0.00015), 0.0);
			break;
		}
#endif

#ifdef COMPILE_COLORED_SHADOW
		output4f = vec4(normalVector4f.a);
#else
		output1f = normalVector4f.a;
#endif
		return;
	}

	shadowSample *= skyLight;

#ifdef COMPILE_COLORED_SHADOW
	// reuse normalVector4f:
	normalVector4f = shadowSample > 0.05 ? textureLod(u_sunShadowColorTexture, shadowSpacePosition.xy, 0.0) : vec4(0.0);

	// saturate the colors, looks nice
	float luma = dot(normalVector4f.rgb, vec3(0.299, 0.587, 0.114));
	normalVector4f.rgb = clamp((normalVector4f.rgb - luma) * 1.5 + luma, vec3(0.0), vec3(1.0));

	output4f = vec4(mix(normalVector4f.rgb, vec3(1.0), normalVector4f.a) * shadowSample, shadowSample);
#else
	output1f = shadowSample;
#endif

}
