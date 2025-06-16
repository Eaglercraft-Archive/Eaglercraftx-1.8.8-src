#line 2

/*
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
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

layout(location = 0) out float output1f;

uniform mat4 u_inverseViewMatrix4f;
uniform mat4 u_inverseViewProjMatrix4f;

uniform sampler2D u_gbufferNormalTexture;
uniform sampler2D u_gbufferDepthTexture;
uniform sampler2D u_gbufferMaterialTexture;
uniform sampler2D u_sunShadowDepthTexture;

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

const vec2 POISSON_DISK[4] = vec2[](
vec2(0.998, -0.0438),
vec2(-0.345, -0.933),
vec2(-0.996, 0.046),
vec2(0.230, 0.960));
#define SMOOTH_SHADOW_SAMPLES (1.0 / 5.0)
#define SMOOTH_SHADOW_RADIUS 0.000488
#define SCATTER_POISSON_SAMPLE(idx, tex, lod, vec3Pos, accum, tmpVec2)\
	tmpVec2 = vec3Pos.xy + POISSON_DISK[idx] * SMOOTH_SHADOW_RADIUS;\
	tmpVec2 = clamp(tmpVec2, vec2(0.001), vec2(0.999));\
	tmpVec2.y += lod;\
	tmpVec2.y *= SUN_SHADOW_MAP_FRAC;\
	accum += scatterSampleInterpolated(tex, vec3(tmpVec2, vec3Pos.z));

#define SUN_SHADOW_DEPTH_SIZE_2F vec2(SUN_SHADOW_DEPTH_SIZE_2F_X, SUN_SHADOW_DEPTH_SIZE_2F_Y)
#define SUN_SHADOW_DEPTH_SIZE_2F_INV vec2((1.0 / SUN_SHADOW_DEPTH_SIZE_2F_X), (1.0 / SUN_SHADOW_DEPTH_SIZE_2F_Y))

uniform vec3 u_sunDirection3f;

#define SCATTER_SAMPLE(tex, vec2Pos, comp) max(textureLod(tex, (vec2Pos), 0.0).r - comp, 0.0)

float scatterSampleInterpolated(in sampler2D texIn, in vec3 vec3Pos) {
	vec2 icoord2f = vec3Pos.xy * SUN_SHADOW_DEPTH_SIZE_2F;
	vec2 floor2fTmp = floor(icoord2f);
	vec2 ceil2fTmp = ceil(icoord2f);
	vec2 ret1 = vec2( // top two samples
		SCATTER_SAMPLE(texIn, (floor2fTmp + 0.5) * SUN_SHADOW_DEPTH_SIZE_2F_INV, vec3Pos.z),
		SCATTER_SAMPLE(texIn, (vec2(ceil2fTmp.x, floor2fTmp.y) + 0.5) * SUN_SHADOW_DEPTH_SIZE_2F_INV, vec3Pos.z)
	);
	vec2 ret2 = vec2( // bottom two samples
		SCATTER_SAMPLE(texIn, (vec2(floor2fTmp.x, ceil2fTmp.y) + 0.5) * SUN_SHADOW_DEPTH_SIZE_2F_INV, vec3Pos.z),
		SCATTER_SAMPLE(texIn, (ceil2fTmp + 0.5) * SUN_SHADOW_DEPTH_SIZE_2F_INV, vec3Pos.z)
	);
	vec2 factors = icoord2f - floor2fTmp;
	vec2 cunt = vec2(1.0 - factors.x, factors.x);
	ret1 = ret1 * cunt * (1.0 - factors.y) + ret2 * cunt * factors.y;
	return (ret1.x + ret1.y) * 0.25;
}

void main() {
	output1f = 0.0;
	float depth = textureLod(u_gbufferDepthTexture, v_position2f, 0.0).r;
	if(depth == 0.0) {
		return;
	}
	float material1f = textureLod(u_gbufferMaterialTexture, v_position2f, 0.0).a;
	material1f = 2.0 * material1f - step(0.5, material1f);
	if(material1f < 0.05) {
		return;
	}
	vec4 normalVector4f = textureLod(u_gbufferNormalTexture, v_position2f, 0.0);
	if(normalVector4f.a < 0.5) {
		return;
	}
	normalVector4f.xyz *= 2.0;
	normalVector4f.xyz -= 1.0;
	vec3 worldSpaceNormal = normalize(mat3(u_inverseViewMatrix4f) * normalVector4f.xyz);
	vec4 worldSpacePosition = vec4(v_position2f, depth, 1.0);
	worldSpacePosition.xyz *= 2.0;
	worldSpacePosition.xyz -= 1.0;
	worldSpacePosition = u_inverseViewProjMatrix4f * worldSpacePosition;
	worldSpacePosition.xyz /= worldSpacePosition.w;
	worldSpacePosition.xyz += worldSpaceNormal * 0.05;
	worldSpacePosition.w = 1.0;
	float shadowSample;
	vec2 tmpVec2;
	vec4 shadowSpacePosition;
	for(;;) {
		shadowSpacePosition = u_sunShadowMatrixLOD04f * worldSpacePosition;
		if(shadowSpacePosition.xyz == clamp(shadowSpacePosition.xyz, vec3(0.005), vec3(0.995))) {
			shadowSample = scatterSampleInterpolated(u_sunShadowDepthTexture, vec3(shadowSpacePosition.xy * vec2(1.0, SUN_SHADOW_MAP_FRAC), shadowSpacePosition.z));
			SCATTER_POISSON_SAMPLE(0, u_sunShadowDepthTexture, 0.0, shadowSpacePosition.xyz, shadowSample, tmpVec2)
			SCATTER_POISSON_SAMPLE(1, u_sunShadowDepthTexture, 0.0, shadowSpacePosition.xyz, shadowSample, tmpVec2)
			SCATTER_POISSON_SAMPLE(2, u_sunShadowDepthTexture, 0.0, shadowSpacePosition.xyz, shadowSample, tmpVec2)
			SCATTER_POISSON_SAMPLE(3, u_sunShadowDepthTexture, 0.0, shadowSpacePosition.xyz, shadowSample, tmpVec2)
			shadowSample *= SMOOTH_SHADOW_SAMPLES;
			break;
		}

#if defined(COMPILE_SUN_SHADOW_LOD1) || defined(COMPILE_SUN_SHADOW_LOD2)
		shadowSpacePosition = u_sunShadowMatrixLOD14f * worldSpacePosition;
		if(shadowSpacePosition.xyz == clamp(shadowSpacePosition.xyz, vec3(0.005), vec3(0.995))) {
			shadowSpacePosition.y += 1.0;
			shadowSpacePosition.y *= SUN_SHADOW_MAP_FRAC;
			shadowSample = scatterSampleInterpolated(u_sunShadowDepthTexture, vec3(shadowSpacePosition.xy, shadowSpacePosition.z + 0.00015));
			break;
		}
#endif

#ifdef COMPILE_SUN_SHADOW_LOD2
		shadowSpacePosition = u_sunShadowMatrixLOD24f * worldSpacePosition;
		if(shadowSpacePosition.xyz == clamp(shadowSpacePosition.xyz, vec3(0.005), vec3(0.995))) {
			shadowSpacePosition.y += 2.0;
			shadowSpacePosition.y *= SUN_SHADOW_MAP_FRAC;
			shadowSample = scatterSampleInterpolated(u_sunShadowDepthTexture, vec3(shadowSpacePosition.xy, shadowSpacePosition.z + 0.00015));
			break;
		}
#endif

		output1f = normalVector4f.a * min(material1f, 0.5);
		return;
	}

	material1f = (1.0 - material1f) * 512.0;
	output1f = max(1.0 - shadowSample * material1f, 0.0);
}
