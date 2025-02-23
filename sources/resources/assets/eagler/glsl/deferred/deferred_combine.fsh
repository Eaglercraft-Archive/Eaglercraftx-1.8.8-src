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

precision lowp int;
precision highp float;
precision highp sampler2D;

in vec2 v_position2f;

layout(location = 0) out vec4 output4f;

uniform sampler2D u_gbufferColorTexture;
uniform sampler2D u_gbufferNormalTexture;
uniform sampler2D u_gbufferMaterialTexture;

uniform sampler2D u_gbufferDepthTexture;

uniform vec2 u_halfResolutionPixelAlignment2f;

#ifdef COMPILE_GLOBAL_AMBIENT_OCCLUSION
uniform sampler2D u_ssaoTexture;
#endif

#ifdef COMPILE_SCREEN_SPACE_REFLECTIONS
uniform sampler2D u_ssrReflectionTexture;
#endif

#ifdef COMPILE_ENV_MAP_REFLECTIONS
uniform sampler2D u_environmentMap;
#endif

uniform sampler2D u_irradianceMap;

uniform sampler2D u_brdfLUT;
uniform sampler2D u_metalsLUT;

uniform mat4 u_inverseProjMatrix4f;
uniform mat4 u_inverseViewMatrix4f;

uniform vec3 u_sunDirection3f;
uniform float u_skyLightFactor1f;

#if defined(COMPILE_SCREEN_SPACE_REFLECTIONS) || defined(COMPILE_ENV_MAP_REFLECTIONS)
#define LIB_INCLUDE_PBR_IMAGE_BASED_LIGHTING_SPECULAR
#endif

#EAGLER INCLUDE (3) "eagler:glsl/deferred/lib/pbr_env_map.glsl"
#EAGLER INCLUDE (4) "eagler:glsl/deferred/lib/branchless_comparison.glsl"

void main() {
	vec3 diffuseColor3f;
	vec3 normalVector3f;
	vec2 lightmapCoords2f;
	vec4 materialData4f;

	float depth = textureLod(u_gbufferDepthTexture, v_position2f, 0.0).r;
	if(depth == 0.0) {
		discard;
	}

	vec4 sampleVar4f = textureLod(u_gbufferColorTexture, v_position2f, 0.0);
	diffuseColor3f.rgb = sampleVar4f.rgb * sampleVar4f.rgb;
	lightmapCoords2f.x = sampleVar4f.a;
	sampleVar4f = textureLod(u_gbufferNormalTexture, v_position2f, 0.0);
	normalVector3f.xyz = sampleVar4f.rgb * 2.0 - 1.0;
	normalVector3f.xyz = mat3(u_inverseViewMatrix4f) * normalVector3f.xyz;
	normalVector3f.xyz = normalize(normalVector3f.xyz);
	lightmapCoords2f.y = sampleVar4f.a;
	materialData4f = textureLod(u_gbufferMaterialTexture, v_position2f, 0.0);

	float shadow = 0.075;

#ifdef COMPILE_GLOBAL_AMBIENT_OCCLUSION
	vec4 ao = textureLod(u_ssaoTexture, min(v_position2f * u_halfResolutionPixelAlignment2f, 1.0), 0.0);
	ao.g = mix(COMPARE_GT_0_1(0.0, ao.b), 1.0, ao.g);
	shadow = mix(shadow, shadow * ao.g, 0.9);
#endif

	lightmapCoords2f *= lightmapCoords2f;
	vec3 irradianceMapSamplePos2f = normalVector3f;
	irradianceMapSamplePos2f.xz /= abs(irradianceMapSamplePos2f.y) + 1.0;
	float dst = 1.0 - dot(irradianceMapSamplePos2f.xz, irradianceMapSamplePos2f.xz);
	dst *= dst;
	irradianceMapSamplePos2f.xz *= 0.975;
	vec3 skyLight = vec3(sqrt(0.01 + max(-u_sunDirection3f.y, 0.0)));
	if(dst < 0.005) {
		vec4 sample1 = textureLod(u_irradianceMap, irradianceMapSamplePos2f.xz * vec2(0.5, 0.25) + vec2(0.5, 0.25), 0.0);
		vec4 sample2 = textureLod(u_irradianceMap, irradianceMapSamplePos2f.xz * vec2(0.5, -0.25) + vec2(0.5, 0.75), 0.0);
		skyLight += mix(sample1.rgb, sample2.rgb, smoothstep(0.0, 1.0, irradianceMapSamplePos2f.y * -12.5 + 0.5)).rgb;
	}else {
		irradianceMapSamplePos2f.xz *= vec2(0.5, COMPARE_GT_C_C(irradianceMapSamplePos2f.y, 0.0, 0.25, -0.25));
		irradianceMapSamplePos2f.xz += vec2(0.5, COMPARE_GT_C_C(irradianceMapSamplePos2f.y, 0.0, 0.25, 0.75));
		skyLight += textureLod(u_irradianceMap, irradianceMapSamplePos2f.xz, 0.0).rgb;
	}

	skyLight *= lightmapCoords2f.g * u_skyLightFactor1f;

	vec3 blockLight = lightmapCoords2f.r * vec3(1.0, 0.5809, 0.2433) * 2.0;
	float emissive = materialData4f.b == 1.0 ? 0.0 : materialData4f.b;
	vec3 specular = vec3(0.0);

#ifdef COMPILE_ENV_MAP_REFLECTIONS
	float f = COMPARE_LT_0_ANY(materialData4f.g, 0.06);
	f += COMPARE_LT_0_ANY(materialData4f.r, 0.5);
	while((materialData4f.a >= 0.5 ? f : -1.0) == 0.0) {
		vec4 worldPosition4f = vec4(v_position2f, depth, 1.0) * 2.0 - 1.0;
		worldPosition4f = u_inverseProjMatrix4f * worldPosition4f;
		worldPosition4f.xyz /= worldPosition4f.w;
		float posDst = dot(worldPosition4f.xyz, worldPosition4f.xyz);
		if(posDst > 25.0) {
			break;
		}
		worldPosition4f = u_inverseViewMatrix4f * vec4(worldPosition4f.xyz, 0.0);
		vec3 viewDir3f = normalize(worldPosition4f.xyz); // need confirmation this should be negative
		vec3 reflectDir = reflect(viewDir3f, normalVector3f);
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
			reflectDir.xz = reflectDir.xz * vec2(0.5, COMPARE_GT_C_C(reflectDir.y, 0.0, 0.25, -0.25));
			reflectDir.xz += vec2(0.5, COMPARE_GT_C_C(reflectDir.y, 0.0, 0.25, 0.75));
			envMapSample4f = textureLod(u_environmentMap, reflectDir.xz, 0.0);
		}
		envMapSample4f.a += min(lightmapCoords2f.g * 2.0, 1.0) * (1.0 - envMapSample4f.a);
		if(envMapSample4f.a == 1.0) {
			specular = eaglercraftIBL_Specular(diffuseColor3f.rgb, envMapSample4f.rgb * envMapSample4f.a, viewDir3f, normalVector3f, materialData4f.rgb);
			specular *= 1.0 - sqrt(posDst) * 0.2;
		}
		break;
	}
#endif

#ifdef COMPILE_SCREEN_SPACE_REFLECTIONS
#ifndef COMPILE_ENV_MAP_REFLECTIONS
	float f = COMPARE_LT_0_ANY(materialData4f.g, 0.06);
	f += COMPARE_LT_0_ANY(materialData4f.r, 0.5);
	if(f == 0.0) {
#else
	if((materialData4f.a < 0.5 ? f : -1.0) == 0.0) {
#endif
		vec4 ssrSample = textureLod(u_ssrReflectionTexture, min(v_position2f * u_halfResolutionPixelAlignment2f, 1.0), 0.0);
		if(ssrSample.g > 0.0) {
			ssrSample.g -= 0.005;
			vec4 worldPosition4f = vec4(v_position2f, depth, 1.0) * 2.0 - 1.0;
			worldPosition4f = u_inverseProjMatrix4f * worldPosition4f;
			worldPosition4f = u_inverseViewMatrix4f * vec4(worldPosition4f.xyz / worldPosition4f.w, 0.0);
			vec3 viewDir3f = normalize(worldPosition4f.xyz); // need confirmation this should be negative
			specular = eaglercraftIBL_Specular(diffuseColor3f.rgb, ssrSample.rgb, viewDir3f, normalVector3f.xyz, materialData4f.rgb);
		}
	}
#endif

	output4f = vec4((diffuseColor3f.rgb * max(skyLight + blockLight, vec3(emissive * emissive * 20.0 + 0.075)) + specular * 8.0) * shadow, 1.0);

}
