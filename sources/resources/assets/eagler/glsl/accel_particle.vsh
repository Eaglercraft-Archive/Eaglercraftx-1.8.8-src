#line 2

/*
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
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

uniform mat4 u_matrixTransform;
uniform vec3 u_texCoordSize2f_particleSize1f;
uniform vec4 u_transformParam_1_2_3_4_f;
uniform float u_transformParam_5_f;
uniform vec4 u_color4f;

uniform sampler2D u_lightmapTexture;

void main() {
	v_color4f = u_color4f * p_color4f.bgra * texture(u_lightmapTexture, p_lightMap2f);

	vec2 tex2f = a_position2f * 0.5 + 0.5;
	tex2f.y = 1.0 - tex2f.y;
	tex2f = p_texCoords2i + tex2f * p_particleSize_texCoordsSize_2i.y;
	v_texCoord2f = tex2f * u_texCoordSize2f_particleSize1f.xy;

	float particleSize = u_texCoordSize2f_particleSize1f.z * p_particleSize_texCoordsSize_2i.x;

	vec3 pos3f = p_position3f;
	vec2 spos2f = a_position2f * particleSize;
	pos3f.x += u_transformParam_1_2_3_4_f.x * spos2f.x;
	pos3f.x += u_transformParam_1_2_3_4_f.w * spos2f.y;
	pos3f.y += u_transformParam_1_2_3_4_f.y * spos2f.y;
	pos3f.z += u_transformParam_1_2_3_4_f.z * spos2f.x;
	pos3f.z += u_transformParam_5_f * spos2f.y;

	gl_Position = u_matrixTransform * vec4(pos3f, 1.0);
}
