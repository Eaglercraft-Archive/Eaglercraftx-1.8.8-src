#line 2

/*
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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

in vec3 a_position3f;

#if defined(COMPILE_ENABLE_TEX_GEN) || defined(COMPILE_ENABLE_FOG)
#define _COMPILE_VARYING_POSITION
#endif

#ifdef _COMPILE_VARYING_POSITION
out vec4 v_position4f;
#endif

#ifdef COMPILE_ENABLE_TEX_GEN
out vec3 v_objectPosition3f;
#endif

#ifdef COMPILE_TEXTURE_ATTRIB
in vec2 a_texture2f;
out vec2 v_texture2f;
uniform mat4 u_textureMat4f01;
#endif

#ifdef COMPILE_COLOR_ATTRIB
in vec4 a_color4f;
out vec4 v_color4f;
#endif

#ifdef COMPILE_NORMAL_ATTRIB
in vec4 a_normal4f;
out vec3 v_normal3f;
#endif

#ifdef COMPILE_LIGHTMAP_ATTRIB
in vec2 a_lightmap2f;
out vec2 v_lightmap2f;
uniform mat4 u_textureMat4f02;
#endif

#ifdef COMPILE_ENABLE_LIGHTMAP
out float v_dynamicLight1f;
#endif

uniform mat4 u_modelviewMat4f;
uniform mat4 u_projectionMat4f;
uniform mat4 u_inverseViewMatrix4f;

#define TEX_MAT3(mat4In) mat3(mat4In[0].xyw,mat4In[1].xyw,mat4In[3].xyw)

layout(std140) uniform u_chunkLightingData {
	mediump uvec2 u_dynamicLightOffsetCount2i;
	mediump int _paddingA_;
	mediump int _paddingB_;
	mediump uvec4 u_dynamicLightArray[4];
};

void main() {
#ifdef COMPILE_ENABLE_TEX_GEN
	v_objectPosition3f = a_position3f;
#endif

#ifndef _COMPILE_VARYING_POSITION
	vec4 v_position4f;
#endif

	v_position4f = u_modelviewMat4f * vec4(a_position3f, 1.0);

#ifdef COMPILE_TEXTURE_ATTRIB
	vec3 v_textureTmp3f = TEX_MAT3(u_textureMat4f01) * vec3(a_texture2f, 1.0);
	v_texture2f = v_textureTmp3f.xy / v_textureTmp3f.z;
#endif
	
#ifdef COMPILE_COLOR_ATTRIB
	v_color4f = a_color4f;
#endif
	
#ifdef COMPILE_NORMAL_ATTRIB
	v_normal3f = normalize(mat3(u_modelviewMat4f) * a_normal4f.xyz);
#endif
	
#ifdef COMPILE_LIGHTMAP_ATTRIB
	vec3 v_lightmapTmp3f = TEX_MAT3(u_textureMat4f02) * vec3(a_lightmap2f, 1.0);
	v_lightmap2f = v_lightmapTmp3f.xy / v_lightmapTmp3f.z;
#endif
	
	gl_Position = u_projectionMat4f * v_position4f;
	
#ifdef COMPILE_ENABLE_LIGHTMAP
	float blockLight = 0.0;
	
	vec4 dlight;
	uvec4 dlighti1, dlighti2;
	if(u_dynamicLightOffsetCount2i.y > 0u) {
		vec3 dlightOffset = vec3(ivec3(
			int(u_dynamicLightOffsetCount2i.x << 16),
			int(u_dynamicLightOffsetCount2i.x << 8),
			int(u_dynamicLightOffsetCount2i.x)
		) >> 24);
		vec4 worldPosition4f = u_inverseViewMatrix4f * v_position4f;
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
	
	v_dynamicLight1f = blockLight;
#endif
}
