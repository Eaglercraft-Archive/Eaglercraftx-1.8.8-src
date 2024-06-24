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

in vec3 a_position3f;

#if defined(COMPILE_ENABLE_ALPHA_TEST) && defined(COMPILE_ENABLE_TEXTURE2D)
#define DO_SHADOW_ALPHA_TEST
#endif

#ifdef DO_SHADOW_ALPHA_TEST
#ifdef COMPILE_TEXTURE_ATTRIB
in vec2 a_texture2f;
out vec2 v_texture2f;
uniform mat4 u_textureMat4f01;
#endif
#endif

uniform mat4 u_modelviewProjMat4f;

#ifdef COMPILE_STATE_WAVING_BLOCKS
#ifdef COMPILE_NORMAL_ATTRIB
in vec4 a_normal4f;
#else
uniform float u_blockConstant1f;
#endif
#ifdef COMPILE_ENABLE_LIGHTMAP
#ifdef COMPILE_LIGHTMAP_ATTRIB
in vec2 a_lightmap2f;
uniform mat4 u_textureMat4f02;
#else
uniform vec2 u_textureCoords02;
#endif
#endif
uniform mat4 u_modelMatrix4f;
uniform mat4 u_viewMatrix4f;
uniform vec3 u_wavingBlockOffset3f;
uniform vec4 u_wavingBlockParam4f;
#define DO_COMPILE_STATE_WAVING_BLOCKS
#define FAKE_SIN(valueIn, valueOut)\
	valueOut = abs(1.0 - fract(valueIn * 0.159155) * 2.0);\
	valueOut = valueOut * valueOut * (6.0 - 4.0 * valueOut) - 1.0;
#define LIB_INCLUDE_WAVING_BLOCKS_FUNCTION
#endif

#EAGLER INCLUDE (2) "eagler:glsl/deferred/lib/waving_blocks.glsl"

#define TEX_MAT3(mat4In) mat3(mat4In[0].xyw,mat4In[1].xyw,mat4In[3].xyw)

void main() {
#ifdef DO_SHADOW_ALPHA_TEST
#ifdef COMPILE_TEXTURE_ATTRIB
	vec3 v_textureTmp3f = TEX_MAT3(u_textureMat4f01) * vec3(a_texture2f, 1.0);
	v_texture2f = v_textureTmp3f.xy / v_textureTmp3f.z;
#endif
#endif
#ifdef DO_COMPILE_STATE_WAVING_BLOCKS
	vec4 pos = vec4(a_position3f, 1.0);
#ifdef COMPILE_NORMAL_ATTRIB
	float blockId = floor((a_normal4f.w + 1.0) * 127.0 + 0.5);
#else
	float blockId = u_blockConstant1f;
#endif
#ifdef COMPILE_ENABLE_LIGHTMAP
#ifdef COMPILE_LIGHTMAP_ATTRIB
	mat4x2 texMat4x2 = mat4x2(
		u_textureMat4f02[0].yw,
		u_textureMat4f02[1].yw,
		u_textureMat4f02[2].yw,
		u_textureMat4f02[3].yw
	);
	vec2 v_lightmapTmp2f = texMat4x2 * vec4(a_lightmap2f, 0.0, 1.0);
	v_lightmapTmp2f.x = v_lightmapTmp2f.x / v_lightmapTmp2f.y;
#else
	vec2 v_lightmapTmp2f = vec2(u_textureCoords02.y, 0.0);
#endif
	if(v_lightmapTmp2f.x > 0.33) {
		COMPUTE_WAVING_BLOCKS(pos, min(v_lightmapTmp2f.x * 3.0 - 1.0, 1.0), 24.0, blockId, u_modelMatrix4f, u_viewMatrix4f, u_modelviewProjMat4f, u_wavingBlockOffset3f, u_wavingBlockParam4f)
	}else {
		pos = u_modelviewProjMat4f * pos;
	}
#else
	COMPUTE_WAVING_BLOCKS(pos, 1.0, 32.0, blockId, u_modelMatrix4f, u_viewMatrix4f, u_modelviewProjMat4f, u_wavingBlockOffset3f, u_wavingBlockParam4f)
#endif
	gl_Position = pos;
#else
	gl_Position = u_modelviewProjMat4f * vec4(a_position3f, 1.0);
#endif
}
