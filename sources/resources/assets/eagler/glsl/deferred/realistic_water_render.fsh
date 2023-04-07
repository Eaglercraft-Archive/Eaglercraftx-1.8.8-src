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

precision highp sampler2DShadow;

in vec4 v_position4f;
in vec2 v_positionClip2f;

#ifdef COMPILE_TEXTURE_ATTRIB
in vec2 v_texture2f;
#else
uniform vec2 u_textureCoords01;
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

#ifdef COMPILE_LIGHTMAP_ATTRIB
in vec2 v_lightmap2f;
#else
uniform vec2 u_textureCoords02;
#endif

uniform mat4 u_inverseViewMatrix4f;
uniform mat4 u_modelViewProjMat4f_;

layout(location = 0) out vec4 output4f;

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

#ifdef DO_COMPILE_SUN_SHADOWS
uniform sampler2DShadow u_sunShadowDepthTexture;
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
#endif

uniform sampler2D u_environmentMap;
uniform sampler2D u_irradianceMap;
uniform sampler2D u_reflectionMap;
uniform sampler2D u_refractionMap;
uniform sampler2D u_brdfLUT;
uniform sampler2D u_normalMap;

#ifdef COMPILE_FOG_LIGHT_SHAFTS
uniform sampler2D u_lightShaftsTexture;
#endif

uniform vec4 u_waterWindOffset4f;
uniform vec3 u_wavingBlockOffset3f;

#define WATER_ROUGHNESS 0.03
#define WATER_F0 0.6

vec3 eaglercraftLighting_Water(in vec3 albedo, in vec3 radiance, in vec3 viewDir, in vec3 lightDir, in vec3 normalVec) {
	float roughness = 1.0 - WATER_ROUGHNESS * 0.85;
	vec3 H = normalize(viewDir + lightDir);
	vec3 NdotHVL = max(normalVec * mat3(H, viewDir, lightDir), vec3(0.0));
	float NDF = (WATER_ROUGHNESS * WATER_ROUGHNESS * WATER_ROUGHNESS * WATER_ROUGHNESS);
	float denom = NdotHVL.x * NdotHVL.x * (NDF - 1.0) + 1.0;
	NDF /= denom * denom * 3.141592;
	float gs = WATER_ROUGHNESS + 1.0;
	gs *= gs * 0.125;
	vec2 Ndot = NdotHVL.yz;
	Ndot /= Ndot * (1.0 - gs) + gs;
	NDF *= Ndot.x * Ndot.y;
	float fresnel = pow(max(1.0 - NdotHVL.x, 0.0), 5.0);
	vec3 F = vec3(WATER_F0 + (1.0 - WATER_F0) * fresnel);
	vec3 kD = (1.0 - F) * albedo / 3.141592;
	denom = 4.0 * NdotHVL.y * NdotHVL.z + 0.0001;
	return (kD + (NDF * F / denom)) * radiance * NdotHVL.z;
}

vec3 eaglercraftIBL_Specular_Water(in vec3 envMapSample, in vec3 viewDir, in vec3 normalVec) {
	float NdotV = dot(normalVec, -viewDir);
	float fresnel = pow(max(1.0 - NdotV, 0.0), 5.0);
	vec3 F = vec3(WATER_F0 + (max(1.0 - WATER_ROUGHNESS, WATER_F0) - WATER_F0) * fresnel);
	vec2 brdf2f = vec2(max(NdotV, 0.0), WATER_ROUGHNESS);
	brdf2f = 1.0 - brdf2f;
	brdf2f *= brdf2f;
	brdf2f = 1.0 - brdf2f;
	brdf2f = textureLod(u_brdfLUT, brdf2f, 0.0).rg;
	return envMapSample * (F * brdf2f.r + brdf2f.g);
}

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

void main() {
	vec4 worldPosition4f;
	vec4 worldDirection4f;
	vec4 diffuseColor4f;
	vec3 normalVector3f;
	vec2 lightmapCoords2f;
	float block1f;

	// =========== RESOLVE CONSTANTS ============ //

	worldPosition4f = u_inverseViewMatrix4f * v_position4f;
	worldPosition4f.xyz /= worldPosition4f.w;
	worldPosition4f.w = 1.0;
	worldDirection4f = u_inverseViewMatrix4f * vec4(v_position4f.xyz / v_position4f.w, 0.0);
	worldDirection4f.xyz = normalize(worldDirection4f.xyz);

#ifdef COMPILE_ENABLE_LIGHTMAP
#ifdef COMPILE_LIGHTMAP_ATTRIB
	lightmapCoords2f = v_lightmap2f;
#else
	lightmapCoords2f = u_textureCoords02;
#endif
#endif

#ifdef COMPILE_NORMAL_ATTRIB
	normalVector3f = normalize(v_normal3f);
	block1f = round(v_block1f);
#else
	normalVector3f = u_uniformNormal3f;
	block1f = u_blockConstant1f;
#endif

	normalVector3f = normalize(mat3(u_inverseViewMatrix4f) * normalVector3f);

	// ========= CALCULATE DIFFUSE COLOR ========== //

#ifdef COMPILE_COLOR_ATTRIB
	diffuseColor4f = v_color4f * u_color4f;
#else
	diffuseColor4f = u_color4f;
#endif

	diffuseColor4f *= vec4(0.0478, 0.0585, 0.1, 1.0);

	// ======== SAMPLE REFLECT/REFRACT ======== //

#ifdef COMPILE_TEXTURE_ATTRIB
	vec2 texCoords2f = v_texture2f;
#else
	vec2 texCoords2f = u_textureCoords01;
#endif

	vec4 worldPosition4fOff = worldPosition4f;
	worldPosition4fOff.xyz += u_wavingBlockOffset3f;

	vec2 rotatedUV2f = worldPosition4fOff.xz + (block1f == 10.0 ? u_waterWindOffset4f.z * texCoords2f : u_waterWindOffset4f.xy);
	rotatedUV2f *= (block1f == 10.0 ? 0.75 : 0.25);
	mat3 cf = cotangent_frame(normalVector3f, worldDirection4f.xyz, rotatedUV2f);
	vec3 surfaceNormalsMap3f = vec3(textureLod(u_normalMap, rotatedUV2f, 0.0).rg, 0.0);
	surfaceNormalsMap3f.xy *= 2.0;
	surfaceNormalsMap3f.xy -= 1.0;

	vec3 surfaceNormalsMapFlat3f = cf * surfaceNormalsMap3f;
	surfaceNormalsMapFlat3f *= 0.1;

	surfaceNormalsMap3f.z = 8.0;
	surfaceNormalsMap3f = normalize(surfaceNormalsMap3f);
	normalVector3f = surfaceNormalsMap3f = cf * surfaceNormalsMap3f;

	vec4 worldPosition4fOff2 = worldPosition4f;
	worldPosition4fOff2.xyz -= surfaceNormalsMapFlat3f * 4.0;
	vec3 reflectCoordsR = mat4x3(
		u_modelViewProjMat4f_[0].xyw,
		u_modelViewProjMat4f_[1].xyw,
		u_modelViewProjMat4f_[2].xyw,
		u_modelViewProjMat4f_[3].xyw) * worldPosition4fOff2;
	reflectCoordsR.xy /= reflectCoordsR.z;
	reflectCoordsR.xy *= 0.5;
	reflectCoordsR.xy += 0.5;

	worldPosition4fOff2 = worldPosition4f;
	worldPosition4fOff2.xyz += surfaceNormalsMapFlat3f;
	vec3 reflectCoordsL = mat4x3(
		u_modelViewProjMat4f_[0].xyw,
		u_modelViewProjMat4f_[1].xyw,
		u_modelViewProjMat4f_[2].xyw,
		u_modelViewProjMat4f_[3].xyw) * worldPosition4fOff2;
	reflectCoordsL.xy /= reflectCoordsL.z;
	reflectCoordsL.xy *= 0.5;
	reflectCoordsL.xy += 0.5;

	vec4 envMapSample4f = textureLod(u_reflectionMap, reflectCoordsR.xy, 0.0);
	vec4 refractionSample = textureLod(u_refractionMap, reflectCoordsL.xy, 0.0);

#ifdef COMPILE_COLOR_ATTRIB
	refractionSample *= v_color4f * v_color4f * u_color4f * u_color4f;
#else
	refractionSample *= u_color4f * u_color4f;
#endif

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
#ifdef COMPILE_SUN_SHADOW_SMOOTH
				shadowSample *= SMOOTH_SHADOW_SAMPLES;
				SMOOTH_SHADOW_POISSON_SAMPLE(0, u_sunShadowDepthTexture, 0.0, shadowTexPos4f.xyz, shadowSample, tmpVec2)
				SMOOTH_SHADOW_POISSON_SAMPLE(1, u_sunShadowDepthTexture, 0.0, shadowTexPos4f.xyz, shadowSample, tmpVec2)
				SMOOTH_SHADOW_POISSON_SAMPLE(2, u_sunShadowDepthTexture, 0.0, shadowTexPos4f.xyz, shadowSample, tmpVec2)
				SMOOTH_SHADOW_POISSON_SAMPLE(3, u_sunShadowDepthTexture, 0.0, shadowTexPos4f.xyz, shadowSample, tmpVec2)
				SMOOTH_SHADOW_POISSON_SAMPLE(4, u_sunShadowDepthTexture, 0.0, shadowTexPos4f.xyz, shadowSample, tmpVec2)
				SMOOTH_SHADOW_POISSON_SAMPLE(5, u_sunShadowDepthTexture, 0.0, shadowTexPos4f.xyz, shadowSample, tmpVec2)
				SMOOTH_SHADOW_POISSON_SAMPLE(6, u_sunShadowDepthTexture, 0.0, shadowTexPos4f.xyz, shadowSample, tmpVec2)
				shadowSample = max(shadowSample * 2.0 - 1.0, 0.0);
#endif
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
		lightColor3f = eaglercraftLighting_Water(diffuseColor4f.rgb, lightColor3f, -worldDirection4f.xyz, u_sunDirection4f.xyz, normalVector3f) * u_blockSkySunDynamicLightFac4f.z;
	}

	// =========== REFLECTION MAP =========== //

	lightmapCoords2f *= lightmapCoords2f;

	float e = 0.0;
	e += envMapSample4f.g <= 0.0 ? 0.0 : 1.0;
	e += lightmapCoords2f.y > 0.5 ? 0.0 : 1.0;
	//e += abs(normalVector3f.y) > 0.1 ? 0.0 : 1.0;
	if(e == 0.0) {
		vec3 reflectDir = reflect(worldDirection4f.xyz, normalVector3f);
		reflectDir.xz /= abs(reflectDir.y) + 1.0;
		float dst = 1.0 - dot(reflectDir.xz, reflectDir.xz);
		dst *= dst;
		reflectDir.xz *= 0.975;
		if(dst < 0.005) {
			vec4 sample1 = textureLod(u_environmentMap, reflectDir.xz * vec2(0.5, 0.25) + vec2(0.5, 0.25), 0.0);
			vec4 sample2 = textureLod(u_environmentMap, reflectDir.xz * vec2(0.5, -0.25) + vec2(0.5, 0.75), 0.0);
			envMapSample4f = vec4(mix(sample1.rgb, sample2.rgb, smoothstep(0.0, 1.0, reflectDir.y * -12.5 + 0.5)).rgb, min(sample1.a, sample2.a));
		}else {
			reflectDir.xz *= vec2(0.5, reflectDir.y > 0.0 ? 0.25 : -0.25);
			reflectDir.xz += vec2(0.5, reflectDir.y > 0.0 ? 0.25 : 0.75);
			envMapSample4f = textureLod(u_environmentMap, reflectDir.xz, 0.0);
		}
		envMapSample4f.rgb *= (lightmapCoords2f.y * 2.0 - 1.0);
	}

	if(envMapSample4f.g > 0.0) {
		lightColor3f += eaglercraftIBL_Specular_Water(envMapSample4f.rgb, worldDirection4f.xyz, normalVector3f) * 0.5;
	}

#ifdef COMPILE_DYNAMIC_LIGHTS

	// =========== DYNAMIC LIGHTING =========== //

	vec3 dlightDist3f, dlightDir3f, dlightColor3f;
	int safeLightCount = u_dynamicLightCount1i > 12 ? 0 : u_dynamicLightCount1i; // hate this
	for(int i = 0; i < safeLightCount; ++i) {
		dlightDist3f = u_dynamicLightArray[i].u_lightPosition4f.xyz - worldPosition4f.xyz;
		dlightDir3f = normalize(dlightDist3f);
		if(dot(dlightDir3f, normalVector3f) <= 0.0) {
			continue;
		}
		dlightColor3f = u_dynamicLightArray[i].u_lightColor4f.rgb / dot(dlightDist3f, dlightDist3f);
		if(dlightColor3f.r + dlightColor3f.g + dlightColor3f.b < 0.025) {
			continue;
		}
		lightColor3f += eaglercraftLighting_Water(diffuseColor4f.rgb, dlightColor3f, -worldDirection4f.xyz, dlightDir3f, normalVector3f) * u_blockSkySunDynamicLightFac4f.w;
	}

#endif

	// ============ CACLULATE FOG ============= //

	vec4 fogBlend4f = vec4(0.0);
	while(u_fogParameters4f.x > 0.0) {
		float atmos = u_fogParameters4f.x >= 4.0 ? 4.0 : 0.0;
		float type = u_fogParameters4f.x - atmos;
		fogBlend4f = mix(u_fogColorLight4f, u_fogColorDark4f, lightmapCoords2f.g);

		float f, l = sqrt(dot(v_position4f.xyz, v_position4f.xyz));
		if(type == 1.0) {
			f = (l - u_fogParameters4f.z) / (u_fogParameters4f.w - u_fogParameters4f.z);
		}else {
			f = 1.0 - exp(-u_fogParameters4f.y * l);
		}

		fogBlend4f.a *= clamp(f, 0.0, 1.0);

		if(atmos == 0.0) {
			break;
		}

		vec3 atmosSamplePos = v_position4f.xyz / -l;
		atmosSamplePos.xz /= abs(atmosSamplePos.y) + 1.0;
		atmosSamplePos.xz *= vec2(-0.5, -0.25) * 0.75;
		atmosSamplePos.xz += vec2(0.5, 0.25);

		fogBlend4f.rgb *= textureLod(u_irradianceMap, atmosSamplePos.xz, 0.0).rgb + u_fogColorAddSun4f.rgb;

#ifdef COMPILE_FOG_LIGHT_SHAFTS
		fogBlend4f.rgb *= pow(textureLod(u_lightShaftsTexture, v_positionClip2f * 0.5 + 0.5, 0.0).r * 0.9 + 0.1, 2.25);
		fogBlend4f.a = fogBlend4f.a * 0.9 + 0.1;
#endif
		break;
	}

	// ============ OUTPUT COLOR ============== //

	vec3 blockLight = lightmapCoords2f.r * vec3(1.0, 0.5809, 0.2433) * 2.0 * u_blockSkySunDynamicLightFac4f.x;
	vec3 skyLight = (lightmapCoords2f.g + 0.05) * vec3(0.9102, 0.9, 1.0) * u_blockSkySunDynamicLightFac4f.y;
	diffuseColor4f.rgb *= (skyLight + blockLight) * 0.075;
	diffuseColor4f.rgb += lightColor3f;
	diffuseColor4f.rgb = mix(diffuseColor4f.rgb + refractionSample.rgb, fogBlend4f.rgb, fogBlend4f.a);
	output4f = vec4(diffuseColor4f.rgb, 1.0);
}
