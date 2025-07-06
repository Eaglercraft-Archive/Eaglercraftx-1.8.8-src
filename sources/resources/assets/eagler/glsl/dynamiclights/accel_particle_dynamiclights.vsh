#line 2

/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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
precision mediump sampler2D;

layout(location = 0) in vec2 a_position2f;

layout(location = 1) in vec3 p_position3f;
layout(location = 2) in vec2 p_texCoords2i;
layout(location = 3) in vec2 p_lightMap2f;
layout(location = 4) in vec2 p_particleSize_texCoordsSize_2i;
layout(location = 5) in vec4 p_color4f;

out vec2 v_texCoord2f;
out vec4 v_color4f;
out vec2 v_lightmap2f;

uniform mat4 u_modelViewMatrix4f;
uniform mat4 u_projectionMatrix4f;
uniform mat4 u_inverseViewMatrix4f;
uniform vec3 u_texCoordSize2f_particleSize1f;
uniform vec3 u_transformParam_1_2_5_f;
uniform vec2 u_transformParam_3_4_f;
uniform vec4 u_color4f;

layout(std140) uniform u_chunkLightingData {
	mediump uvec2 u_dynamicLightOffsetCount2i;
	mediump int _paddingA_;
	mediump int _paddingB_;
	mediump uvec4 u_dynamicLightArray[4];
};

void main() {
	v_color4f = u_color4f * p_color4f.bgra;

	vec2 tex2f = a_position2f * 0.5 + 0.5;
	tex2f.y = 1.0 - tex2f.y;
	tex2f = p_texCoords2i + tex2f * p_particleSize_texCoordsSize_2i.y;
	v_texCoord2f = tex2f * u_texCoordSize2f_particleSize1f.xy;

	float particleSize = u_texCoordSize2f_particleSize1f.z * p_particleSize_texCoordsSize_2i.x;

	vec3 pos3f = p_position3f;
	vec2 spos2f = a_position2f * particleSize;
	pos3f += u_transformParam_1_2_5_f * spos2f.xyy;
	pos3f.zx += u_transformParam_3_4_f * spos2f;

	vec4 pos4f = u_modelViewMatrix4f * vec4(pos3f, 1.0);
	gl_Position = u_projectionMatrix4f * pos4f;

	float blockLight = 0.0;

	vec4 dlight;
	uvec4 dlighti1, dlighti2;
	if(u_dynamicLightOffsetCount2i.y > 0u) {
		vec3 dlightOffset = vec3(ivec3(
			int(u_dynamicLightOffsetCount2i.x << 16),
			int(u_dynamicLightOffsetCount2i.x << 8),
			int(u_dynamicLightOffsetCount2i.x)
		) >> 24);
		vec4 worldPosition4f = u_inverseViewMatrix4f * pos4f;
		worldPosition4f.xyz = worldPosition4f.xyz / worldPosition4f.w + dlightOffset;
		for(uint i = 0u; i < 4u; ++i) {
			dlighti1 = u_dynamicLightArray[i];
			dlighti2 = dlighti1 << 16;

			dlight = vec4(ivec4(ivec2(dlighti2.xy), ivec2(dlighti1.xy)) >> 16) * 0.0009765923;
			dlight.xyz = dlight.xyz - worldPosition4f.xyz;
			blockLight = max((dlight.w - length(dlight.xyz)) * 0.066667, blockLight);
			if(i * 2u + 1u >= u_dynamicLightOffsetCount2i.y) {
				break;
			}

			dlight = vec4(ivec4(ivec2(dlighti2.zw), ivec2(dlighti1.zw)) >> 16) * 0.0009765923;
			dlight.xyz = dlight.xyz - worldPosition4f.xyz;
			blockLight = max((dlight.w - length(dlight.xyz)) * 0.066667, blockLight);
			if(i * 2u + 2u >= u_dynamicLightOffsetCount2i.y) {
				break;
			}
		}
	}

	v_lightmap2f = vec2(max(p_lightMap2f.x, blockLight), p_lightMap2f.y);
}
