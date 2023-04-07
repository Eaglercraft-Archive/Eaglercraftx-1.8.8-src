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
precision mediump sampler2D;
precision highp sampler2DShadow;

in vec4 v_position4f;
in vec2 v_texCoord2f;
in vec4 v_color4f;
in vec2 v_lightmap2f;

layout(location = 0) out vec4 output4f;

uniform sampler2D u_diffuseTexture;

uniform vec2 u_textureYScale2f;

uniform mat4 u_inverseViewMatrix4f;

#ifdef COMPILE_DYNAMIC_LIGHTS
struct DynamicLight {
	mediump vec4 u_lightPosition4f;
	mediump vec4 u_lightColor4f;
};
layout(std140) uniform u_chunkLightingData {
	mediump int u_dynamicLightCount1i;
	mediump int _paddingA_;
	mediump int _paddingB_;
	mediump int _paddingC_;
	DynamicLight u_dynamicLightArray[12];
};
#endif

layout(std140) uniform u_worldLightingData {
	mediump vec4 u_sunDirection4f;
	mediump vec4 u_sunColor3f_sky1f;
	mediump vec4 u_fogParameters4f;
	mediump vec4 u_fogColorLight4f;
	mediump vec4 u_fogColorDark4f;
	mediump vec4 u_fogColorAddSun4f;
	mediump vec4 u_blockSkySunDynamicLightFac4f;
#ifdef COMPILE_SUN_SHADOW_LOD0
	mediump mat4 u_sunShadowMatrixLOD04f;
#define DO_COMPILE_SUN_SHADOWS
#define SUN_SHADOW_MAP_FRAC 1.0
#endif
#ifdef COMPILE_SUN_SHADOW_LOD1
	mediump mat4 u_sunShadowMatrixLOD04f;
	mediump mat4 u_sunShadowMatrixLOD14f;
#define DO_COMPILE_SUN_SHADOWS
#define SUN_SHADOW_MAP_FRAC 0.5
#endif
#ifdef COMPILE_SUN_SHADOW_LOD2
	mediump mat4 u_sunShadowMatrixLOD04f;
	mediump mat4 u_sunShadowMatrixLOD14f;
	mediump mat4 u_sunShadowMatrixLOD24f;
#define DO_COMPILE_SUN_SHADOWS
#define SUN_SHADOW_MAP_FRAC 0.3333333
#endif
};

uniform sampler2D u_samplerNormalMaterial;

uniform sampler2D u_metalsLUT;

#define LIB_INCLUDE_PBR_LIGHTING_FUNCTION
#define LIB_INCLUDE_PBR_LIGHTING_PREFETCH
#EAGLER INCLUDE (3) "eagler:glsl/deferred/lib/pbr_lighting.glsl"

uniform sampler2D u_irradianceMap;

#ifdef DO_COMPILE_SUN_SHADOWS
uniform sampler2DShadow u_sunShadowDepthTexture;
#endif

void main() {
	vec4 worldPosition4f;
	vec4 worldDirection4f;
	vec4 diffuseColor4f;
	vec3 normalVector3f;
	vec2 lightmapCoords2f;
	vec3 materialData3f;

	// =========== RESOLVE CONSTANTS ============ //

	worldPosition4f = u_inverseViewMatrix4f * v_position4f;
	worldPosition4f.xyz /= worldPosition4f.w;
	worldPosition4f.w = 1.0;
	worldDirection4f = u_inverseViewMatrix4f * vec4(v_position4f.xyz / v_position4f.w, 0.0);
	worldDirection4f.xyz = normalize(worldDirection4f.xyz);

	lightmapCoords2f = v_lightmap2f;

	normalVector3f = normalize(u_inverseViewMatrix4f[2].xyz);

	// ========= CALCULATE DIFFUSE COLOR ========== //

	diffuseColor4f = texture(u_diffuseTexture, v_texCoord2f) * v_color4f;

	// ============= ALPHA TEST ============== //

	if(diffuseColor4f.a < 0.004) discard;

	// ========== RESOLVE MATERIALS =========== //

	materialData3f = texture(u_samplerNormalMaterial, vec2(v_texCoord2f.x, v_texCoord2f.y * u_textureYScale2f.x + u_textureYScale2f.y)).rgb;

	vec3 metalN, metalK;
	PREFETCH_METALS(diffuseColor4f.rgb, materialData3f.g, metalN, metalK)

	// ============ SUN LIGHTING ============== //

	diffuseColor4f.rgb *= diffuseColor4f.rgb;

	vec3 lightColor3f = vec3(0.0);
	if(dot(u_sunDirection4f.xyz, normalVector3f) > 0.0 && lightmapCoords2f.g > 0.5 &&
			(u_sunColor3f_sky1f.r + u_sunColor3f_sky1f.g + u_sunColor3f_sky1f.b) > 0.001) {
#ifdef DO_COMPILE_SUN_SHADOWS

		// ========== SUN SHADOW: LOD0 ============ //

		float skyLight = max(lightmapCoords2f.g * 2.0 - 1.0, 0.0);
		float shadowSample = 1.0;
		vec4 shadowWorldPos4f = worldPosition4f;
		shadowWorldPos4f.xyz += normalVector3f * 0.05;

		vec4 shadowTexPos4f;
		vec2 tmpVec2;
		for(;;) {
			shadowTexPos4f = u_sunShadowMatrixLOD04f * shadowWorldPos4f;
			if(shadowTexPos4f.xyz == clamp(shadowTexPos4f.xyz, vec3(0.005), vec3(0.995))) {
				shadowSample = textureLod(u_sunShadowDepthTexture, vec3(shadowTexPos4f.xy * vec2(1.0, SUN_SHADOW_MAP_FRAC), shadowTexPos4f.z), 0.0);
				break;
			}

#if defined(COMPILE_SUN_SHADOW_LOD1) || defined(COMPILE_SUN_SHADOW_LOD2)
			shadowTexPos4f = u_sunShadowMatrixLOD14f * shadowWorldPos4f;
			if(shadowTexPos4f.xyz == clamp(shadowTexPos4f.xyz, vec3(0.005), vec3(0.995))) {
				shadowTexPos4f.y += 1.0;
				shadowTexPos4f.y *= SUN_SHADOW_MAP_FRAC;
				shadowSample = textureLod(u_sunShadowDepthTexture, vec3(shadowTexPos4f.xy, shadowTexPos4f.z + 0.00015), 0.0);
				break;
			}
#endif

#ifdef COMPILE_SUN_SHADOW_LOD2
			shadowTexPos4f = u_sunShadowMatrixLOD24f * shadowWorldPos4f;
			if(shadowTexPos4f.xyz == clamp(shadowTexPos4f.xyz, vec3(0.005), vec3(0.995))) {
				shadowTexPos4f.y += 2.0;
				shadowTexPos4f.y *= SUN_SHADOW_MAP_FRAC;
				shadowSample = textureLod(u_sunShadowDepthTexture, vec3(shadowTexPos4f.xy, shadowTexPos4f.z + 0.00015), 0.0);
			}
#endif
			break;
		}
#endif
		lightColor3f = u_sunColor3f_sky1f.rgb * max(lightmapCoords2f.g * 2.0 - 1.0, 0.0);
#ifdef DO_COMPILE_SUN_SHADOWS
		lightColor3f *= shadowSample * skyLight;
#endif
		vec3 normalWrap3f = normalVector3f * (dot(-worldDirection4f.xyz, normalVector3f) < 0.0 ? -1.0 : 1.0);
		lightColor3f = eaglercraftLighting(diffuseColor4f.rgb, lightColor3f, -worldDirection4f.xyz, u_sunDirection4f.xyz, normalWrap3f, materialData3f, metalN, metalK) * u_blockSkySunDynamicLightFac4f.z;
	}

	// =========== IRRADIANCE MAP =========== //

	lightmapCoords2f *= lightmapCoords2f;

	vec3 irradianceMapSamplePos2f = normalVector3f;
	irradianceMapSamplePos2f.xz /= abs(irradianceMapSamplePos2f.y) + 1.0;
	float dst = 1.0 - dot(irradianceMapSamplePos2f.xz, irradianceMapSamplePos2f.xz);
	dst *= dst;
	irradianceMapSamplePos2f.xz *= 0.975;
	vec3 skyLight = vec3(sqrt(0.01 + max(u_sunDirection4f.w, 0.0)));
	if(dst < 0.005) {
		vec4 sample1 = textureLod(u_irradianceMap, irradianceMapSamplePos2f.xz * vec2(0.5, 0.25) + vec2(0.5, 0.25), 0.0);
		vec4 sample2 = textureLod(u_irradianceMap, irradianceMapSamplePos2f.xz * vec2(0.5, -0.25) + vec2(0.5, 0.75), 0.0);
		skyLight += mix(sample1.rgb, sample2.rgb, smoothstep(0.0, 1.0, irradianceMapSamplePos2f.y * -12.5 + 0.5)).rgb;
	}else {
		irradianceMapSamplePos2f.xz *= vec2(0.5, irradianceMapSamplePos2f.y > 0.0 ? 0.25 : -0.25);
		irradianceMapSamplePos2f.xz += vec2(0.5, irradianceMapSamplePos2f.y > 0.0 ? 0.25 : 0.75);
		skyLight += textureLod(u_irradianceMap, irradianceMapSamplePos2f.xz, 0.0).rgb;
	}
	skyLight *= lightmapCoords2f.g * u_sunColor3f_sky1f.w;

#ifdef COMPILE_DYNAMIC_LIGHTS

	// =========== DYNAMIC LIGHTING =========== //

	vec3 dlightDist3f, dlightDir3f, dlightColor3f;
	int safeLightCount = u_dynamicLightCount1i > 12 ? 0 : u_dynamicLightCount1i; // hate this
	for(int i = 0; i < safeLightCount; ++i) {
		dlightDist3f = worldPosition4f.xyz - u_dynamicLightArray[i].u_lightPosition4f.xyz;
		dlightDir3f = normalize(dlightDist3f);
		dlightDir3f = dlightDir3f * (dot(dlightDir3f, normalVector3f) < 0.0 ? 1.0 : -1.0);
		dlightDir3f = materialData3f.b == 1.0 ? normalVector3f : -dlightDir3f;
		if(dot(dlightDir3f, normalVector3f) <= 0.0) {
			continue;
		}
		dlightColor3f = u_dynamicLightArray[i].u_lightColor4f.rgb / dot(dlightDist3f, dlightDist3f);
		if(dlightColor3f.r + dlightColor3f.g + dlightColor3f.b < 0.025) {
			continue;
		}
		lightColor3f += eaglercraftLighting(diffuseColor4f.rgb, dlightColor3f, -worldDirection4f.xyz, dlightDir3f, normalVector3f, materialData3f, metalN, metalK) * u_blockSkySunDynamicLightFac4f.w;
	}

#endif

	// ============ OUTPUT COLOR ============== //

	vec3 blockLight = lightmapCoords2f.r * vec3(1.0, 0.5809, 0.2433) * 2.0 * u_blockSkySunDynamicLightFac4f.x;
	skyLight *= u_blockSkySunDynamicLightFac4f.y;
	float emissive = materialData3f.b == 1.0 ? 0.0 : materialData3f.b;
	diffuseColor4f.rgb *= max(skyLight + blockLight, vec3(emissive * emissive * 20.0 + 0.075)) * 0.075;
	diffuseColor4f.rgb += lightColor3f;

	output4f = vec4(diffuseColor4f.rgb * diffuseColor4f.a, diffuseColor4f.a);
}
