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

layout(location = 0) out vec4 output4f;

uniform sampler2D u_gbufferColorTexture;
uniform sampler2D u_gbufferNormalTexture;
uniform sampler2D u_gbufferMaterialTexture;

uniform sampler2D u_gbufferDepthTexture;
uniform sampler2D u_metalsLUT;

uniform mat4 u_inverseViewMatrix4f;
uniform mat4 u_inverseProjectionMatrix4f;

#ifdef COMPILE_SUN_SHADOW
uniform sampler2D u_sunShadowTexture;
#endif

uniform vec3 u_sunDirection3f;
uniform vec3 u_sunColor3f;

#define LIB_INCLUDE_PBR_LIGHTING_FUNCTION
#EAGLER INCLUDE (3) "eagler:glsl/deferred/lib/pbr_lighting.glsl"

void main() {
	vec3 diffuseColor3f;
	vec3 normalVector3f;
	vec2 lightmapCoords2f;
	vec3 materialData3f;

#ifdef COMPILE_SUN_SHADOW
#ifdef COMPILE_COLORED_SHADOW
	vec4 shadow = textureLod(u_sunShadowTexture, v_position2f, 0.0);
	if(shadow.a < 0.05) {
		discard;
	}
#else
	vec3 shadow = vec3(textureLod(u_sunShadowTexture, v_position2f, 0.0).r);
	if(shadow.r < 0.05) {
		discard;
	}
#endif
#endif

	vec4 sampleVar4f = textureLod(u_gbufferNormalTexture, v_position2f, 0.0);

#ifndef COMPILE_SUN_SHADOW
	vec3 shadow = vec3(sampleVar4f.a, 0.0, 0.0);
	if(shadow.r < 0.5) {
		discard;
	}
	shadow = vec3(max(shadow.r * 2.0 - 1.0, 0.0));
#endif

	normalVector3f.xyz = sampleVar4f.rgb * 2.0 - 1.0;
	lightmapCoords2f.y = sampleVar4f.a;

	float depth = textureLod(u_gbufferDepthTexture, v_position2f, 0.0).r;

#ifndef COMPILE_SUN_SHADOW
	if(depth < 0.00001) {
		discard;
	}
#endif

	sampleVar4f = textureLod(u_gbufferColorTexture, v_position2f, 0.0);
	diffuseColor3f.rgb = sampleVar4f.rgb;
	lightmapCoords2f.x = sampleVar4f.a;
	materialData3f = textureLod(u_gbufferMaterialTexture, v_position2f, 0.0).rgb;

	vec3 worldSpaceNormal = normalize(mat3(u_inverseViewMatrix4f) * normalVector3f);
	vec4 worldSpacePosition = vec4(v_position2f, depth, 1.0);
	worldSpacePosition.xyz *= 2.0;
	worldSpacePosition.xyz -= 1.0;
	worldSpacePosition = u_inverseProjectionMatrix4f * worldSpacePosition;
	worldSpacePosition = u_inverseViewMatrix4f * vec4(worldSpacePosition.xyz / worldSpacePosition.w, 0.0);

	diffuseColor3f *= diffuseColor3f;
	output4f = vec4(eaglercraftLighting(diffuseColor3f, u_sunColor3f * shadow.rgb, normalize(-worldSpacePosition.xyz), u_sunDirection3f, worldSpaceNormal, materialData3f), 0.0);
}
