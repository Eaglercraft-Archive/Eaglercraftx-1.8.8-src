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

layout(location = 0) out float output1f;

uniform sampler2D u_gbufferDepthTexture;
uniform sampler2D u_gbufferNormalTexture;
uniform sampler2D u_noiseConstantTexture;

uniform mat4 u_projectionMatrix4f;
uniform mat4 u_inverseProjectionMatrix4f;

uniform mat2 u_randomizerDataMatrix2f;

#EAGLER INCLUDE (4) "eagler:glsl/deferred/lib/branchless_comparison.glsl"

const vec3 ssaoKernel[8] = vec3[](
vec3(0.599,0.721,0.350),vec3(0.114,0.791,0.601),
vec3(0.067,0.995,0.069),vec3(0.511,-0.510,0.692),
vec3(0.626,-0.667,0.404),vec3(0.896,-0.169,0.411),
vec3(0.716,-0.439,0.543),vec3(-0.400,0.733,0.550));
#define radius 1.5
#define SAMPLE_SSAO(idx, pos, matTBN, matProjInv2f, divisor, occlusion, tmpVec4_1, tmpVec4_2)\
	tmpVec4_1.xyz = pos + (matTBN * ssaoKernel[idx]) * radius;\
	tmpVec4_1.w = 1.0;\
	tmpVec4_2 = u_projectionMatrix4f * tmpVec4_1;\
	tmpVec4_2.xyz /= tmpVec4_2.w;\
	tmpVec4_2.xyz = clamp(tmpVec4_2.xyz, -0.99, 0.99);\
	tmpVec4_2.zw = matProjInv2f * vec4(tmpVec4_2.xy, textureLod(u_gbufferDepthTexture, tmpVec4_2.xy * 0.5 + 0.5, 0.0).r * 2.0 - 1.0, 1.0);\
	tmpVec4_2.z /= tmpVec4_2.w;\
	tmpVec4_2.x = smoothstep(0.0, 1.0, radius * 0.5 / abs(pos.z - tmpVec4_2.z));\
	divisor += COMPARE_GT_0_1(tmpVec4_2.x, 0.0);\
	occlusion += COMPARE_GT_0_1(tmpVec4_2.z, tmpVec4_1.z) * tmpVec4_2.x;

void main() {
	vec3 originalClipSpacePos = vec3(v_position2f, textureLod(u_gbufferDepthTexture, v_position2f, 0.0).r);
	
	if(originalClipSpacePos.z == 0.0) {
		output1f = 1.0;
		return;
	}
	
	originalClipSpacePos *= 2.0;
	originalClipSpacePos -= 1.0;
	
	vec3 normal3f = textureLod(u_gbufferNormalTexture, v_position2f, 0.0).rgb;
	normal3f *= 2.0;
	normal3f -= 1.0;
	
	vec4 originalViewSpacePos = u_inverseProjectionMatrix4f * vec4(originalClipSpacePos, 1.0);
	originalViewSpacePos.xyz /= originalViewSpacePos.w;
	originalViewSpacePos.w = 1.0;
	
	vec4 noiseVec = textureLod(u_noiseConstantTexture, 13.3725 / fract((u_randomizerDataMatrix2f * (v_position2f * 0.42695346 + originalViewSpacePos.xy * 1.373769945645 + normal3f.xz * 42.69456453)) * 1.123234234), 0.0);
	noiseVec.xyz *= 2.0;
	noiseVec.xyz -= 1.0;
	
	vec3 tangent = normalize(noiseVec.xyz - normal3f * dot(noiseVec.xyz, normal3f));
	vec3 bitangent = cross(normal3f, tangent);
	mat3 TBN = mat3(tangent, bitangent, normal3f) * noiseVec.w;
	
	float divisor = 0.0;
	float occlusion = 0.0;
	vec4 tmpVec4_1;
	vec4 tmpVec4_2;
	
	mat4x2 matProjInv2f = mat4x2(u_inverseProjectionMatrix4f[0].zw, u_inverseProjectionMatrix4f[1].zw, u_inverseProjectionMatrix4f[2].zw, u_inverseProjectionMatrix4f[3].zw);
	
	SAMPLE_SSAO(0, originalViewSpacePos.xyz, TBN, matProjInv2f, divisor, occlusion, tmpVec4_1, tmpVec4_2)
	SAMPLE_SSAO(1, originalViewSpacePos.xyz, TBN, matProjInv2f, divisor, occlusion, tmpVec4_1, tmpVec4_2)
	SAMPLE_SSAO(2, originalViewSpacePos.xyz, TBN, matProjInv2f, divisor, occlusion, tmpVec4_1, tmpVec4_2)
	SAMPLE_SSAO(3, originalViewSpacePos.xyz, TBN, matProjInv2f, divisor, occlusion, tmpVec4_1, tmpVec4_2)
	SAMPLE_SSAO(4, originalViewSpacePos.xyz, TBN, matProjInv2f, divisor, occlusion, tmpVec4_1, tmpVec4_2)
	SAMPLE_SSAO(5, originalViewSpacePos.xyz, TBN, matProjInv2f, divisor, occlusion, tmpVec4_1, tmpVec4_2)
	SAMPLE_SSAO(6, originalViewSpacePos.xyz, TBN, matProjInv2f, divisor, occlusion, tmpVec4_1, tmpVec4_2)
	SAMPLE_SSAO(7, originalViewSpacePos.xyz, TBN, matProjInv2f, divisor, occlusion, tmpVec4_1, tmpVec4_2)
	
	output1f = max(1.0 - (occlusion / max(divisor, 0.001)), 0.0);
}
