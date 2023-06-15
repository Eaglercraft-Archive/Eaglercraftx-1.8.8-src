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

layout(location = 0) out vec4 realisticWaterReflectionOutput4f;
layout(location = 1) out vec4 realisticWaterHitVectorOutput4f;
layout(location = 2) out vec4 realisticWaterRefraction4f;

uniform sampler2D u_gbufferColorTexture4f;
uniform sampler2D u_gbufferDepthTexture;
uniform sampler2D u_realisticWaterMaskNormal;
uniform sampler2D u_realisticWaterDepthTexture;
uniform sampler2D u_lastFrameReflectionInput4f;
uniform sampler2D u_lastFrameHitVectorInput4f;
uniform sampler2D u_lastFrameColorTexture;
uniform sampler2D u_lastFrameDepthTexture;

uniform mat4 u_inverseProjectionMatrix4f;
uniform mat4 u_inverseViewProjMatrix4f;
uniform mat4 u_reprojectionMatrix4f;
uniform mat4 u_lastInverseProjMatrix4f;
uniform mat4 u_reprojectionInverseViewMatrix4f;
uniform mat4 u_projectionMatrix4f;
uniform mat4 u_viewToPreviousProjMatrix4f;

// x = near plane
// y = far plane
// z = near plane * 2
// w = far plane - near plane
uniform vec4 u_nearFarPlane4f;

uniform vec4 u_pixelAlignment4f;

uniform vec4 u_refractFogColor4f;

#define reprojDepthLimit 0.25

#define GET_LINEAR_DEPTH_FROM_VALUE(depthSample) (u_nearFarPlane4f.z / (u_nearFarPlane4f.y + u_nearFarPlane4f.x + (depthSample * 2.0 - 1.0) * u_nearFarPlane4f.w))

#define CREATE_DEPTH_MATRIX(matrix4fInput) mat4x2(matrix4fInput[0].zw,matrix4fInput[1].zw,matrix4fInput[2].zw,matrix4fInput[3].zw)

void main() {
	vec2 v_position2f2 = (floor(v_position2f * u_pixelAlignment4f.xy) + 0.25) * (2.0 / u_pixelAlignment4f.zw);
	realisticWaterReflectionOutput4f = vec4(0.0, 0.0, 0.0, 0.0);
	realisticWaterHitVectorOutput4f = vec4(0.0, 0.0, 0.0, 0.0);
	realisticWaterRefraction4f = vec4(0.0, 0.0, 0.0, 0.0);
	vec4 waterSurfaceNormal4f = textureLod(u_realisticWaterMaskNormal, v_position2f2, 0.0);

	if(waterSurfaceNormal4f.a <= 0.0) {
		return;
	}

	float gbufferDepth = textureLod(u_gbufferDepthTexture, v_position2f2, 0.0).r;

	if(gbufferDepth < 0.000001) {
		return;
	}

	vec4 gbufferDepthClipSpace4f = vec4(v_position2f2, gbufferDepth, 1.0);
	gbufferDepthClipSpace4f.xyz *= 2.0;
	gbufferDepthClipSpace4f.xyz -= 1.0;
	vec2 gbufferDepthView = CREATE_DEPTH_MATRIX(u_inverseProjectionMatrix4f) * gbufferDepthClipSpace4f;
	gbufferDepthView.x /= gbufferDepthView.y;

	float waterSurfaceDepth = textureLod(u_realisticWaterDepthTexture, v_position2f2, 0.0).r;
	vec4 waterSurfaceDepthClipSpace4f = vec4(gbufferDepthClipSpace4f.xy, waterSurfaceDepth * 2.0 - 1.0, 1.0);
	vec2 waterDepthView = CREATE_DEPTH_MATRIX(u_inverseProjectionMatrix4f) * waterSurfaceDepthClipSpace4f;
	waterDepthView.x /= waterDepthView.y;

	float fog = clamp(1.25 - 1.0 / exp((waterDepthView.x - gbufferDepthView.x) * 0.05), 0.0, 1.0);

	vec3 refractColor3f = textureLod(u_gbufferColorTexture4f, v_position2f2, 0.0).rgb;
	refractColor3f *= mix(vec3(1.0), vec3(0.02, 0.025, 0.12), min(fog + 0.1, 1.0));
	vec3 fogColor3f = u_refractFogColor4f.rgb * (waterSurfaceNormal4f.a * u_refractFogColor4f.a * 0.95 + 0.05);
	realisticWaterRefraction4f = vec4(mix(refractColor3f, fogColor3f, fog), 1.0);

	vec4 fragPos4f = u_inverseViewProjMatrix4f * waterSurfaceDepthClipSpace4f;
	fragPos4f.xyz /= fragPos4f.w;
	fragPos4f.w = 1.0;
	vec4 reprojPos4f = u_reprojectionMatrix4f * fragPos4f;
	vec4 reprojClipPos4f = vec4(reprojPos4f.xyz / reprojPos4f.w, 1.0);
	reprojPos4f = reprojClipPos4f;
	reprojPos4f.xyz *= 0.5;
	reprojPos4f.xyz += 0.5;
	reprojPos4f.xy = (floor(reprojPos4f.xy * u_pixelAlignment4f.zw) + 0.5) * (0.5 / u_pixelAlignment4f.xy);
	if(reprojPos4f.xy != clamp(reprojPos4f.xy, vec2(0.001), vec2(0.999)) || abs(GET_LINEAR_DEPTH_FROM_VALUE(textureLod(u_lastFrameDepthTexture, reprojPos4f.xy, 0.0).r) - GET_LINEAR_DEPTH_FROM_VALUE(reprojPos4f.z)) > reprojDepthLimit) {
		realisticWaterHitVectorOutput4f = vec4(0.0, 0.0, 0.0, 50.0);
		return;
	}

	vec4 lastFrameHitVector4f = textureLod(u_lastFrameHitVectorInput4f, reprojPos4f.xy, 0.0);
	if(lastFrameHitVector4f.g <= 0.0) {
		realisticWaterReflectionOutput4f = textureLod(u_lastFrameReflectionInput4f, reprojPos4f.xy, 0.0);
		realisticWaterHitVectorOutput4f = vec4(0.0, 0.0, 0.0, lastFrameHitVector4f.a);
		return;
	}

	realisticWaterReflectionOutput4f = vec4(0.0, 0.0, 0.0, 1.0);

	lastFrameHitVector4f.g -= 0.004;

	vec4 lastFrameFragPosView4f = u_lastInverseProjMatrix4f * vec4(reprojClipPos4f.xyz, 1.0);
	lastFrameFragPosView4f.xyz /= lastFrameFragPosView4f.w;
	lastFrameFragPosView4f.w = 1.0;
	vec4 lastFrameHitPos4f = vec4(lastFrameFragPosView4f.xyz + lastFrameHitVector4f.xyz, 1.0);

	vec4 thisFrameHitPos4f = u_reprojectionInverseViewMatrix4f * lastFrameHitPos4f;
	thisFrameHitPos4f.xyz /= thisFrameHitPos4f.w;
	thisFrameHitPos4f.w = 1.0;

	vec4 thisFrameHitPosProj4f = u_projectionMatrix4f * thisFrameHitPos4f;
	thisFrameHitPosProj4f.xyz /= thisFrameHitPosProj4f.w;
	thisFrameHitPosProj4f.w = 1.0;
	vec3 thisFrameHitPosProjTex3f = thisFrameHitPosProj4f.xyz * 0.5 + 0.5;

	if(thisFrameHitPosProjTex3f.xy != clamp(thisFrameHitPosProjTex3f.xy, vec2(0.001), vec2(0.999))) {
		return;
	}

	float fragDepthSample = textureLod(u_gbufferDepthTexture, thisFrameHitPosProjTex3f.xy, 0.0).r * 2.0 - 1.0;
	vec2 thisFrameHitPosProjDepthPos = CREATE_DEPTH_MATRIX(u_inverseProjectionMatrix4f) * vec4(thisFrameHitPosProj4f.xy, fragDepthSample, 1.0);
	thisFrameHitPosProjDepthPos.x /= thisFrameHitPosProjDepthPos.y;

	if(thisFrameHitPosProjDepthPos.x - thisFrameHitPos4f.z - 0.125 < 0.0) {
		return;
	}

	waterSurfaceNormal4f.xyz *= 2.0;
	waterSurfaceNormal4f.xyz -= 1.0;

	vec3 lastFrameHitPosNormal3f = textureLod(u_realisticWaterMaskNormal, thisFrameHitPosProjTex3f.xy, 0.0).rgb;
	lastFrameHitPosNormal3f *= 2.0;
	lastFrameHitPosNormal3f -= 1.0;

	vec4 fragPosView4f = u_inverseProjectionMatrix4f * waterSurfaceDepthClipSpace4f;
	fragPosView4f.xyz /= fragPosView4f.w;
	fragPosView4f.w = 1.0;

	vec3 rayOrigin = fragPosView4f.xyz;
	vec3 planePos = thisFrameHitPos4f.xyz;
	vec3 planeNormal = lastFrameHitPosNormal3f;

	vec3 newRayDirection = reflect(normalize(rayOrigin), waterSurfaceNormal4f.xyz);

	float dist = dot(planeNormal, newRayDirection);
	if(dist > 0.9) {
		return;
	}

	dist = dot(planeNormal, planePos - rayOrigin) / dist;
	if(dist < 0.0) {
		return;
	}

	realisticWaterHitVectorOutput4f = vec4(newRayDirection * dist, 1.0);
	realisticWaterHitVectorOutput4f.y += 0.004;

	thisFrameHitPosProj4f = u_viewToPreviousProjMatrix4f * vec4(rayOrigin + newRayDirection * dist, 1.0);
	thisFrameHitPosProj4f.xyz /= thisFrameHitPosProj4f.w;
	thisFrameHitPosProj4f.w = 1.0;
	thisFrameHitPosProjTex3f = thisFrameHitPosProj4f.xyz * 0.5 + 0.5;

	if(thisFrameHitPosProjTex3f.xy != clamp(thisFrameHitPosProjTex3f.xy, vec2(0.001), vec2(0.999))) {
		return;
	}

	fragDepthSample = textureLod(u_lastFrameDepthTexture, thisFrameHitPosProjTex3f.xy, 0.0).r * 2.0 - 1.0;

	vec2 thisFrameHitPosProjPos = CREATE_DEPTH_MATRIX(u_lastInverseProjMatrix4f) * thisFrameHitPosProj4f;
	thisFrameHitPosProjPos.x /= thisFrameHitPosProjPos.y;

	thisFrameHitPosProjDepthPos = CREATE_DEPTH_MATRIX(u_lastInverseProjMatrix4f) * vec4(thisFrameHitPosProj4f.xy, fragDepthSample, 1.0);
	thisFrameHitPosProjDepthPos.x /= thisFrameHitPosProjDepthPos.y;

	if(thisFrameHitPosProjDepthPos.x - thisFrameHitPosProjPos.x - 0.125 < 0.0) {
		realisticWaterHitVectorOutput4f = vec4(0.0, 0.0, 0.0, 0.0);
		return;
	}

	realisticWaterReflectionOutput4f = vec4(textureLod(u_lastFrameColorTexture, thisFrameHitPosProjTex3f.xy, 0.0).rgb, 0.0);
}
