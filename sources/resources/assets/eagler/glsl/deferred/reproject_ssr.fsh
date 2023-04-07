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

in vec2 v_position2f;

layout(location = 0) out vec4 reflectionOutput4f;
layout(location = 1) out vec4 hitVectorOutput4f;

uniform sampler2D u_gbufferDepthTexture;
uniform sampler2D u_gbufferNormalTexture;
uniform sampler2D u_reprojectionReflectionInput4f;
uniform sampler2D u_reprojectionHitVectorInput4f;
uniform sampler2D u_lastFrameColorInput4f;
uniform sampler2D u_lastFrameDepthInput;

uniform mat4 u_lastProjectionMatrix4f;
uniform mat4x2 u_lastInverseProjMatrix4x2f;

uniform mat4 u_inverseProjectionMatrix4f;

uniform float u_sampleStep1f;

uniform vec4 u_pixelAlignment4f;

#define maxAge 55.0
#define maxSamples 50.0

void main() {
	vec2 v_position2f2 = (floor(v_position2f * u_pixelAlignment4f.xy) + 0.25) * (2.0 / u_pixelAlignment4f.zw);
	reflectionOutput4f = vec4(0.0, 0.0, 0.0, 0.0);
	hitVectorOutput4f = vec4(0.0, 0.0, 0.0, 0.0);
	float fragDepth = textureLod(u_gbufferDepthTexture, v_position2f2, 0.0).r;

	if(fragDepth < 0.000001) {
		return;
	}

	vec4 reflectionInput4f = textureLod(u_reprojectionReflectionInput4f, v_position2f, 0.0);
	vec4 hitVectorInput4f = textureLod(u_reprojectionHitVectorInput4f, v_position2f, 0.0);
	hitVectorInput4f.a += 1.0;
	float f = reflectionInput4f.a < 1.0 ? 1.0 : reflectionInput4f.a;
	reflectionInput4f.a = hitVectorInput4f.a > maxAge ? f : reflectionInput4f.a;
	if(reflectionInput4f.a < 1.0) {
		reflectionOutput4f = reflectionInput4f;
		hitVectorOutput4f = hitVectorInput4f;
		return;
	}

	vec4 fragPos4f = u_inverseProjectionMatrix4f * (vec4(v_position2f2, fragDepth, 1.0) * 2.0 - 1.0);
	fragPos4f.xyz /= fragPos4f.w;
	fragPos4f.w = 1.0;
	vec4 reflectionNormal4f = textureLod(u_gbufferNormalTexture, v_position2f2, 0.0);
	reflectionNormal4f.xyz *= 2.0;
	reflectionNormal4f.xyz -= 1.0;
	reflectionNormal4f.xyz = reflect(normalize(fragPos4f.xyz), reflectionNormal4f.xyz);
	reflectionNormal4f.w = 1.0;
	float sampleStepMod = (reflectionInput4f.a * 0.03 + 0.15 + length(fragPos4f.xyz) * 0.03) * u_sampleStep1f;
	vec3 sampleOffset3f = reflectionNormal4f.xyz * reflectionInput4f.a * sampleStepMod;
	fragPos4f.xyz += sampleOffset3f;
	reflectionNormal4f = u_lastProjectionMatrix4f * fragPos4f;
	reflectionNormal4f.xyz /= reflectionNormal4f.w;
	reflectionNormal4f.w = 1.0;
	vec3 reflectionSamplePos3f = reflectionNormal4f.xyz;
	reflectionSamplePos3f *= 0.5;
	reflectionSamplePos3f += 0.5;
	reflectionSamplePos3f.xy = (floor(reflectionSamplePos3f.xy * u_pixelAlignment4f.zw) + 0.5) * (0.5 / u_pixelAlignment4f.xy);

	if(clamp(reflectionSamplePos3f.xy, vec2(0.001), vec2(0.999)) != reflectionSamplePos3f.xy) {
		return;
	}

	float reflectDepthSample = textureLod(u_lastFrameDepthInput, reflectionSamplePos3f.xy, 0.0).r;
	vec2 sampleFragDepth = u_lastInverseProjMatrix4x2f * vec4(reflectionNormal4f.xy, reflectDepthSample * 2.0 - 1.0, 1.0);
	sampleFragDepth.x /= sampleFragDepth.y;

	reflectDepthSample = sampleFragDepth.x - fragPos4f.z;
	if(reflectDepthSample < sampleStepMod * 3.0) {
		reflectionInput4f.a += 1.0;
		reflectionOutput4f = reflectionInput4f.a >= maxSamples ? vec4(0.0, 0.0, 0.0, 0.0) : reflectionInput4f;
		hitVectorOutput4f = vec4(0.0, 0.0, 0.0, hitVectorInput4f.a);
		return;
	}

	if(abs(reflectDepthSample) > sampleStepMod * 6.0) {
		return;
	}

	vec4 colorSample = textureLod(u_lastFrameColorInput4f, reflectionSamplePos3f.xy, 0.0);
	reflectionOutput4f = vec4(colorSample.rgb, 0.0);
	reflectionOutput4f.g += 0.0005;
	hitVectorOutput4f = vec4(colorSample.a > 0.0 ? sampleOffset3f : vec3(0.0), 0.0);
	hitVectorOutput4f.g += colorSample.a > 0.0 ? 0.0004 : 0.0;
}
