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

#ifdef COMPILE_REPROJECT_SSAO
layout(location = 0) out vec4 reprojectionSSAOOutput4f;
#ifdef COMPILE_REPROJECT_SSR
layout(location = 1) out vec4 reprojectionReflectionOutput4f;
layout(location = 2) out vec4 reprojectionHitVectorOutput4f;
#endif
#else
layout(location = 0) out vec4 reprojectionReflectionOutput4f;
layout(location = 1) out vec4 reprojectionHitVectorOutput4f;
#endif

uniform sampler2D u_gbufferDepthTexture;
#ifdef COMPILE_REPROJECT_SSAO
uniform sampler2D u_ssaoSampleTexture;
// R: ssao history length
// G: ssao value
// B: pixel opacity
// A: padding
uniform sampler2D u_reprojectionSSAOInput4f;
#endif

#ifdef COMPILE_REPROJECT_SSR
uniform sampler2D u_gbufferNormalTexture;
uniform sampler2D u_gbufferMaterialTexture;

// R: reflection R
// G: reflection G
// B: reflection B
// A: current step
uniform sampler2D u_reprojectionReflectionInput4f;

// R: relative X of reflected pixel from origin
// G: relative Y of reflected pixel from origin
// B: relative Z of reflected pixel from origin
// A: is reflected flag
uniform sampler2D u_reprojectionHitVectorInput4f;

uniform sampler2D u_lastFrameColorInput4f;

uniform mat4 u_projectionMatrix4f;
uniform mat4 u_inverseProjectionMatrix4f;
uniform mat4 u_lastInverseProjMatrix4f;

// matrix to transform view pos in old frame to view pos in current frame
uniform mat4 u_reprojectionInverseViewMatrix4f;
uniform mat4 u_viewToPreviousProjMatrix4f;

#endif

uniform sampler2D u_reprojectionDepthTexture;

uniform mat4 u_inverseViewProjMatrix4f;
uniform mat4 u_reprojectionMatrix4f;

// x = near plane
// y = far plane
// z = near plane * 2
// w = far plane - near plane
uniform vec4 u_nearFarPlane4f;

uniform vec4 u_pixelAlignment4f;

#define reprojDepthLimit 0.25

#define GET_LINEAR_DEPTH_FROM_VALUE(depthSample) (u_nearFarPlane4f.z / (u_nearFarPlane4f.y + u_nearFarPlane4f.x + (depthSample * 2.0 - 1.0) * u_nearFarPlane4f.w))

#define CREATE_DEPTH_MATRIX(matrix4fInput) mat4x2(matrix4fInput[0].zw,matrix4fInput[1].zw,matrix4fInput[2].zw,matrix4fInput[3].zw)

void main() {
	vec2 v_position2f2 = (floor(v_position2f * u_pixelAlignment4f.xy) + 0.25) * (2.0 / u_pixelAlignment4f.zw);
#ifdef COMPILE_REPROJECT_SSAO
	reprojectionSSAOOutput4f = vec4(0.0, 1.0, 0.0, 0.0);
#endif
#ifdef COMPILE_REPROJECT_SSR
	reprojectionReflectionOutput4f = vec4(0.0, 0.0, 0.0, 0.0);
	reprojectionHitVectorOutput4f = vec4(0.0, 0.0, 0.0, 0.0);
#endif
	float fragDepth = textureLod(u_gbufferDepthTexture, v_position2f, 0.0).r;

	if(fragDepth < 0.000001) {
		return;
	}

	vec4 fragClipSpacePos4f = vec4(v_position2f, fragDepth, 1.0) * 2.0 - 1.0;
	vec4 fragPos4f = u_inverseViewProjMatrix4f * fragClipSpacePos4f;
	fragPos4f.xyz /= fragPos4f.w;
	fragPos4f.w = 1.0;
	vec4 reprojPos4f = u_reprojectionMatrix4f * fragPos4f;
	vec4 reprojClipPos4f = vec4(reprojPos4f.xyz / reprojPos4f.w, 1.0);
	reprojPos4f = reprojClipPos4f;
	reprojPos4f.xyz *= 0.5;
	reprojPos4f.xyz += 0.5;
	reprojPos4f.xy = (floor(reprojPos4f.xy * u_pixelAlignment4f.zw) + 0.5) * (0.5 / u_pixelAlignment4f.xy);
	if(reprojPos4f.xy != clamp(reprojPos4f.xy, vec2(0.001), vec2(0.999)) || abs(GET_LINEAR_DEPTH_FROM_VALUE(textureLod(u_reprojectionDepthTexture, reprojPos4f.xy, 0.0).r) - GET_LINEAR_DEPTH_FROM_VALUE(reprojPos4f.z)) > reprojDepthLimit) {
#ifdef COMPILE_REPROJECT_SSAO
		reprojectionSSAOOutput4f = vec4(0.01, textureLod(u_ssaoSampleTexture, v_position2f, 0.0).r, 1.0, 0.0);
#endif
#ifdef COMPILE_REPROJECT_SSR
		reprojectionHitVectorOutput4f = vec4(0.0, 0.0, 0.0, 50.0);
#endif
		return;
	}

#ifdef COMPILE_REPROJECT_SSAO
	vec4 reprojectionSSAOInput4f = textureLod(u_reprojectionSSAOInput4f, reprojPos4f.xy, 0.0);
	reprojectionSSAOInput4f.g = mix(textureLod(u_ssaoSampleTexture, v_position2f, 0.0).r, reprojectionSSAOInput4f.g, min(reprojectionSSAOInput4f.r * 10.0, 0.85));
	reprojectionSSAOInput4f.r = min(reprojectionSSAOInput4f.r + 0.01, 1.0);
	reprojectionSSAOInput4f.b = 1.0;
	reprojectionSSAOOutput4f = reprojectionSSAOInput4f;
#endif

#ifdef COMPILE_REPROJECT_SSR
	vec4 materials = textureLod(u_gbufferMaterialTexture, v_position2f2, 0.0);
	float f = materials.g < 0.06 ? 1.0 : 0.0;
	f += materials.r < 0.5 ? 1.0 : 0.0;
	f += materials.a > 0.5 ? 1.0 : 0.0;
	if(f > 0.0) {
		return;
	}

	vec4 lastFrameHitVector4f = textureLod(u_reprojectionHitVectorInput4f, reprojPos4f.xy, 0.0);
	if(lastFrameHitVector4f.g <= 0.0) {
		reprojectionReflectionOutput4f = textureLod(u_reprojectionReflectionInput4f, reprojPos4f.xy, 0.0);
		reprojectionReflectionOutput4f.a = max(reprojectionReflectionOutput4f.a - 1.0, 1.0);
		reprojectionHitVectorOutput4f = vec4(0.0, 0.0, 0.0, lastFrameHitVector4f.a);
		return;
	}

	reprojectionReflectionOutput4f = vec4(0.0, 0.0, 0.0, 1.0);

	lastFrameHitVector4f.g -= 0.0004;

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

	vec3 lastFrameHitPosNormal3f = textureLod(u_gbufferNormalTexture, thisFrameHitPosProjTex3f.xy, 0.0).rgb;
	lastFrameHitPosNormal3f *= 2.0;
	lastFrameHitPosNormal3f -= 1.0;

	vec3 currentNormal3f = textureLod(u_gbufferNormalTexture, v_position2f2, 0.0).xyz;
	currentNormal3f *= 2.0;
	currentNormal3f -= 1.0;

	vec4 fragPosView4f = u_inverseProjectionMatrix4f * fragClipSpacePos4f;
	fragPosView4f.xyz /= fragPosView4f.w;
	fragPosView4f.w = 1.0;

	vec3 rayOrigin = fragPosView4f.xyz;
	vec3 planePos = thisFrameHitPos4f.xyz;
	vec3 planeNormal = lastFrameHitPosNormal3f;

	vec3 newRayDirection = reflect(normalize(rayOrigin), currentNormal3f.xyz);

	float dist = dot(planeNormal, newRayDirection);
	if(dist > 0.9) {
		return;
	}

	dist = dot(planeNormal, planePos - rayOrigin) / dist;
	if(dist < 0.0) {
		return;
	}

	reprojectionHitVectorOutput4f = vec4(newRayDirection * dist, 1.0);
	reprojectionHitVectorOutput4f.y += 0.0004;

	thisFrameHitPosProj4f = u_viewToPreviousProjMatrix4f * vec4(rayOrigin + newRayDirection * dist, 1.0);
	thisFrameHitPosProj4f.xyz /= thisFrameHitPosProj4f.w;
	thisFrameHitPosProj4f.w = 1.0;
	thisFrameHitPosProjTex3f = thisFrameHitPosProj4f.xyz * 0.5 + 0.5;

	if(thisFrameHitPosProjTex3f.xy != clamp(thisFrameHitPosProjTex3f.xy, vec2(0.001), vec2(0.999))) {
		return;
	}

	fragDepthSample = textureLod(u_reprojectionDepthTexture, thisFrameHitPosProjTex3f.xy, 0.0).r * 2.0 - 1.0;

	vec2 thisFrameHitPosProjPos = CREATE_DEPTH_MATRIX(u_lastInverseProjMatrix4f) * thisFrameHitPosProj4f;
	thisFrameHitPosProjPos.x /= thisFrameHitPosProjPos.y;

	thisFrameHitPosProjDepthPos = CREATE_DEPTH_MATRIX(u_lastInverseProjMatrix4f) * vec4(thisFrameHitPosProj4f.xy, fragDepthSample, 1.0);
	thisFrameHitPosProjDepthPos.x /= thisFrameHitPosProjDepthPos.y;

	if(thisFrameHitPosProjDepthPos.x - thisFrameHitPosProjPos.x - 0.125 < 0.0) {
		reprojectionHitVectorOutput4f = vec4(0.0, 0.0, 0.0, 0.0);
		return;
	}

	reprojectionReflectionOutput4f = vec4(textureLod(u_lastFrameColorInput4f, thisFrameHitPosProjTex3f.xy, 0.0).rgb, 0.0);
#endif
}
