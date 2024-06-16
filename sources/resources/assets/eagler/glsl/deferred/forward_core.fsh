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

#ifdef COMPILE_FOG_LIGHT_SHAFTS
in vec2 v_positionClip2f;
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

#ifdef COMPILE_ENABLE_TEXTURE2D
uniform sampler2D u_samplerTexture;
#ifndef COMPILE_TEXTURE_ATTRIB
uniform vec2 u_textureCoords01;
#endif
#else
#undef COMPILE_NORMAL_MATERIAL_TEXTURE
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

#ifdef COMPILE_ENABLE_ALPHA_TEST
uniform float u_alphaTestRef1f;
#endif

#ifdef COMPILE_ENABLE_ANISOTROPIC_FIX
uniform vec2 u_textureAnisotropicFix;
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

#ifdef COMPILE_NORMAL_MATERIAL_TEXTURE
uniform sampler2D u_samplerNormalMaterial;
#else
uniform vec3 u_materialConstants3f;
#endif

uniform sampler2D u_metalsLUT;

#define LIB_INCLUDE_PBR_LIGHTING_FUNCTION
#define LIB_INCLUDE_PBR_LIGHTING_PREFETCH
#EAGLER INCLUDE (3) "eagler:glsl/deferred/lib/pbr_lighting.glsl"

#ifdef COMPILE_PARABOLOID
#undef COMPILE_SUN_SHADOW_SMOOTH
#undef COMPILE_SUN_SHADOW_LOD1
#undef COMPILE_SUN_SHADOW_LOD2
#endif

#ifdef COMPILE_PARABOLOID_ENV_MAP
uniform sampler2D u_environmentMap;
uniform sampler2D u_brdfLUT;
#define LIB_INCLUDE_PBR_IMAGE_BASED_LIGHTING_SPECULAR
#define LIB_INCLUDE_PBR_IMAGE_BASED_LIGHTING_PREFETCH
#endif

uniform sampler2D u_irradianceMap;

#ifdef COMPILE_FOG_LIGHT_SHAFTS
uniform sampler2D u_lightShaftsTexture;
#endif

#EAGLER INCLUDE (4) "eagler:glsl/deferred/lib/pbr_env_map.glsl"

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
	vec4 diffuseColor4f;
	vec3 normalVector3f;
	vec2 lightmapCoords2f;
	vec3 materialData3f;
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

	// ========= CALCULATE DIFFUSE COLOR ========== //

#ifdef COMPILE_COLOR_ATTRIB
	diffuseColor4f = v_color4f * u_color4f;
#else
	diffuseColor4f = u_color4f;
#endif

#ifdef COMPILE_ENABLE_TEXTURE2D
	vec2 texCoords2f;
#ifdef COMPILE_ENABLE_TEX_GEN
	vec4 texGenVector;
	vec4 texGenPosSrc[2];
	texGenPosSrc[0] = vec4(v_objectPosition3f, 1.0);
	texGenPosSrc[1] = v_position4f;
	texGenVector.x = dot(texGenPosSrc[u_texGenPlane4i.x], u_texGenS4f);
	texGenVector.y = dot(texGenPosSrc[u_texGenPlane4i.y], u_texGenT4f);
	texGenVector.z = dot(texGenPosSrc[u_texGenPlane4i.z], u_texGenR4f);
	texGenVector.w = dot(texGenPosSrc[u_texGenPlane4i.w], u_texGenQ4f);
	texGenVector = vec4(mat4x3(
		u_textureMat4f01[0].xyw,
		u_textureMat4f01[1].xyw,
		u_textureMat4f01[2].xyw,
		u_textureMat4f01[3].xyw
	) * texGenVector, 0.0);
	texCoords2f = texGenVector.xy / texGenVector.z;
#else
	
#ifdef COMPILE_TEXTURE_ATTRIB
#ifdef COMPILE_ENABLE_ANISOTROPIC_FIX
	texCoords2f = floor(v_texture2f * u_textureAnisotropicFix) + 0.5;
	texCoords2f /= u_textureAnisotropicFix;
#else
	texCoords2f = v_texture2f;
#endif
#else
	texCoords2f = u_textureCoords01;
#endif
#endif
	diffuseColor4f *= texture(u_samplerTexture, texCoords2f);
#endif

#ifdef COMPILE_BLEND_ADD
	diffuseColor4f = diffuseColor4f * u_colorBlendSrc4f + u_colorBlendAdd4f;
#endif

	// ============= ALPHA TEST ============== //

#ifdef COMPILE_ENABLE_ALPHA_TEST
	if(diffuseColor4f.a < u_alphaTestRef1f) discard;
#endif

	// ========== RESOLVE MATERIALS =========== //

#ifdef COMPILE_NORMAL_MATERIAL_TEXTURE
	vec2 uv2 = vec2(1.0, 0.5) * texCoords2f;
	uv2.y += 0.5;
	materialData3f = texture(u_samplerNormalMaterial, uv2).rgb;
#else
	materialData3f = u_materialConstants3f;
#endif

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
		lightColor3f = eaglercraftLighting(diffuseColor4f.rgb, lightColor3f, -worldDirection4f.xyz, u_sunDirection4f.xyz, normalVector3f, materialData3f, metalN, metalK) * u_blockSkySunDynamicLightFac4f.z;
	}

	float f;
#ifdef COMPILE_PARABOLOID_ENV_MAP

	// =========== ENVIRONMENT MAP =========== //

	f = materialData3f.g < 0.06 ? 1.0 : 0.0;
	f += materialData3f.r < 0.5 ? 1.0 : 0.0;
	while(f == 0.0) {
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
			lightColor3f += eaglercraftIBL_Specular(diffuseColor4f.rgb, envMapSample4f.rgb, worldDirection4f.xyz, normalVector3f, materialData3f, metalN, metalK) * (1.0 - sqrt(dst2) * 0.25);
		}
		break;
	}

#endif

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

	// ============ CACLULATE FOG ============= //

	vec4 fogBlend4f = vec4(0.0);
#ifndef COMPILE_ENABLE_TEX_GEN
	while(u_fogParameters4f.x > 0.0) {
		float atmos = u_fogParameters4f.x >= 4.0 ? 4.0 : 0.0;
		float type = u_fogParameters4f.x - atmos;
		fogBlend4f = mix(u_fogColorLight4f, u_fogColorDark4f, lightmapCoords2f.g);

		float l = length(v_position4f.xyz);
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

		fogBlend4f.rgb *= textureLod(u_irradianceMap, atmosSamplePos.xz, 0.0).rgb;

#ifdef COMPILE_FOG_LIGHT_SHAFTS
		fogBlend4f.rgb *= pow(textureLod(u_lightShaftsTexture, v_positionClip2f * 0.5 + 0.5, 0.0).r * 0.9 + 0.1, 2.25);
		fogBlend4f.a = fogBlend4f.a * 0.9 + 0.1;
#endif
		break;
	}
#endif

	// ============ OUTPUT COLOR ============== //

	vec3 blockLight = lightmapCoords2f.r * vec3(1.0, 0.5809, 0.2433) * 2.0 * u_blockSkySunDynamicLightFac4f.x;
	skyLight *= u_blockSkySunDynamicLightFac4f.y;
	float emissive = materialData3f.b == 1.0 ? 0.0 : materialData3f.b;
	diffuseColor4f.rgb *= max(skyLight + blockLight, vec3(emissive * emissive * 20.0 + 0.075)) * 0.075;
	diffuseColor4f.rgb += lightColor3f;

	diffuseColor4f.rgb = mix(diffuseColor4f.rgb, fogBlend4f.rgb, fogBlend4f.a);

	output4f = vec4(diffuseColor4f.rgb * diffuseColor4f.a, diffuseColor4f.a);
}
