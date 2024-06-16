#line 2

/*
 * Copyright (c) 2023 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */

precision highp sampler2DShadow;

in vec4 v_position4f;

uniform vec4 u_color4f;

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

uniform sampler2D u_environmentMap;
uniform sampler2D u_brdfLUT;

#define GLASS_ROUGHNESS 0.15
#define GLASS_F0 0.4

vec3 eaglercraftLighting_Glass(in vec3 radiance, in vec3 viewDir, in vec3 lightDir, in vec3 normalVec) {
	float roughness = 1.0 - GLASS_ROUGHNESS * 0.85;
	vec3 H = normalize(viewDir + lightDir);
	vec3 NdotHVL = max(normalVec * mat3(H, viewDir, lightDir), vec3(0.0));
	float NDF = (GLASS_ROUGHNESS * GLASS_ROUGHNESS * GLASS_ROUGHNESS * GLASS_ROUGHNESS);
	float denom = NdotHVL.x * NdotHVL.x * (NDF - 1.0) + 1.0;
	NDF /= denom * denom * 3.141592;
	float gs = GLASS_ROUGHNESS + 1.0;
	gs *= gs * 0.125;
	vec2 Ndot = NdotHVL.yz;
	Ndot /= Ndot * (1.0 - gs) + gs;
	NDF *= Ndot.x * Ndot.y;
	float fresnel = pow(max(1.0 - NdotHVL.x, 0.0), 5.0);
	vec3 F = vec3(GLASS_F0 + (1.0 - GLASS_F0) * fresnel);
	denom = 4.0 * NdotHVL.y * NdotHVL.z + 0.0001;
	return (NDF * F / denom) * radiance * NdotHVL.z;
}

vec3 eaglercraftIBL_Specular_Glass(in vec3 envMapSample, in vec3 viewDir, in vec3 normalVec) {
	float NdotV = dot(normalVec, -viewDir);
	float fresnel = pow(max(1.0 - NdotV, 0.0), 5.0);
	vec3 F = vec3(GLASS_F0 + (max(1.0 - GLASS_ROUGHNESS, GLASS_F0) - GLASS_F0) * fresnel);
	vec2 brdf2f = vec2(max(NdotV, 0.0), GLASS_ROUGHNESS);
	brdf2f = 1.0 - brdf2f;
	brdf2f *= brdf2f;
	brdf2f = 1.0 - brdf2f;
	brdf2f = textureLod(u_brdfLUT, brdf2f, 0.0).rg;
	return envMapSample * (F * brdf2f.r + brdf2f.g);
}

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

void main() {
	vec4 worldPosition4f;
	vec4 worldDirection4f;
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
#else
	lightmapCoords2f = vec2(0.0, 1.0);
#endif

#ifdef COMPILE_NORMAL_ATTRIB
	normalVector3f = normalize(v_normal3f);
	block1f = v_block1f;
#else
	normalVector3f = u_uniformNormal3f;
	block1f = u_blockConstant1f;
#endif

	normalVector3f = normalize(mat3(u_inverseViewMatrix4f) * normalVector3f);

	// ============ SUN LIGHTING ============== //

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
		lightColor3f = eaglercraftLighting_Glass(lightColor3f, -worldDirection4f.xyz, u_sunDirection4f.xyz, normalVector3f) * u_blockSkySunDynamicLightFac4f.z;
	}

	// =========== ENVIRONMENT MAP =========== //

	for(;;) {
		float dst2 = dot(worldPosition4f.xyz, worldPosition4f.xyz);
		if(dst2 > 16.0) {
			break;
		}
		vec3 reflectDir = reflect(worldDirection4f.xyz, normalVector3f);
		reflectDir.xz /= abs(reflectDir.y) + 1.0;
		float dst = 1.0 - dot(reflectDir.xz, reflectDir.xz);
		dst *= dst;
		reflectDir.xz = reflectDir.xz * 0.975;
		vec4 envMapSample4f;
		if(dst < 0.005) {
			vec4 sample1 = textureLod(u_environmentMap, reflectDir.xz * vec2(0.5, 0.25) + vec2(0.5, 0.25), 0.0);
			vec4 sample2 = textureLod(u_environmentMap, reflectDir.xz * vec2(0.5, -0.25) + vec2(0.5, 0.75), 0.0);
			envMapSample4f = vec4(mix(sample1.rgb, sample2.rgb, smoothstep(0.0, 1.0, reflectDir.y * -12.5 + 0.5)).rgb, min(sample1.a, sample2.a));
		}else {
			reflectDir.xz = reflectDir.xz * vec2(0.5, reflectDir.y > 0.0 ? 0.25 : -0.25);
			reflectDir.xz += vec2(0.5, reflectDir.y > 0.0 ? 0.25 : 0.75);
			envMapSample4f = textureLod(u_environmentMap, reflectDir.xz, 0.0);
		}
		if(envMapSample4f.a > 0.0) {
			lightColor3f += eaglercraftIBL_Specular_Glass(envMapSample4f.rgb, worldDirection4f.xyz, normalVector3f) * (1.0 - sqrt(dst2) * 0.25);
		}
		break;
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
		lightColor3f += eaglercraftLighting_Glass(dlightColor3f, -worldDirection4f.xyz, dlightDir3f, normalVector3f) * u_blockSkySunDynamicLightFac4f.w;
	}

#endif

	// ============ CACLULATE FOG ============= //

	float fogFade = 0.0;
	if(u_fogParameters4f.x > 0.0) {
		float atmos = u_fogParameters4f.x >= 4.0 ? 4.0 : 0.0;
		float type = u_fogParameters4f.x - atmos;
		fogFade = mix(u_fogColorDark4f.a, u_fogColorLight4f.a, lightmapCoords2f.g);

		float f;
		float l = length(v_position4f.xyz);
		if(type == 1.0) {
			f = (l - u_fogParameters4f.z) / (u_fogParameters4f.w - u_fogParameters4f.z);
		}else {
			f = 1.0 - exp(-u_fogParameters4f.y * l);
		}

		fogFade *= clamp(f, 0.0, 1.0);
	}

	// ============ OUTPUT COLOR ============== //

	output4f = vec4(lightColor3f * (1.0 - fogFade), 0.1);
}
