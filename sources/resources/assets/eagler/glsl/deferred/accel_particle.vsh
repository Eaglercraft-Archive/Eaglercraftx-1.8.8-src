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

#ifdef COMPILE_FORWARD_VSH
out vec4 v_position4f;
uniform mat4 u_modelViewMatrix4f;
uniform mat4 u_projectionMatrix4f;
#endif

#ifdef COMPILE_GBUFFER_VSH
uniform mat4 u_matrixTransform;
#endif

uniform vec3 u_texCoordSize2f_particleSize1f;
uniform vec3 u_transformParam_1_2_5_f;
uniform vec2 u_transformParam_3_4_f;

void main() {
	v_color4f = p_color4f.bgra;
	v_lightmap2f = p_lightMap2f;

	vec2 tex2f = a_position2f * 0.5 + 0.5;
	tex2f.y = 1.0 - tex2f.y;
	tex2f = p_texCoords2i + tex2f * p_particleSize_texCoordsSize_2i.y;
	v_texCoord2f = tex2f * u_texCoordSize2f_particleSize1f.xy;

	float particleSize = u_texCoordSize2f_particleSize1f.z * p_particleSize_texCoordsSize_2i.x;

	vec3 pos3f = p_position3f;
	vec2 spos2f = a_position2f * particleSize;
	pos3f += u_transformParam_1_2_5_f * spos2f.xyy;
	pos3f.zx += u_transformParam_3_4_f * spos2f;

#ifdef COMPILE_GBUFFER_VSH
	gl_Position = u_matrixTransform * vec4(pos3f, 1.0);
#endif
#ifdef COMPILE_FORWARD_VSH
	v_position4f = u_modelViewMatrix4f * vec4(pos3f, 1.0);
	gl_Position = u_projectionMatrix4f * v_position4f;
#endif
}
