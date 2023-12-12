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

uniform sampler2D u_noiseTexture;

uniform float u_textureSlice1f;
uniform vec2 u_textureSize2f;
uniform mat4x3 u_sampleOffsetMatrix4f;
uniform vec3 u_cloudMovement3f;

#define GET_CLOUDS(pos3f, accum, factor, tmp3f)\
	tmp3f.z = floor(pos3f.z);\
	tmp3f.xy = pos3f.xy * 0.015625 + (tmp3f.z * 0.265625);\
	pos3f.x = textureLod(u_noiseTexture, tmp3f.xy, 0.0).x;\
	pos3f.y = textureLod(u_noiseTexture, tmp3f.xy + 0.265625, 0.0).x;\
	accum += mix(pos3f.x, pos3f.y, pos3f.z - tmp3f.z) * factor;

void main() {
	vec3 p = vec3(v_position2f.x, u_textureSlice1f, v_position2f.y) * vec3(u_textureSize2f.x, 1.0, u_textureSize2f.y);
	p = u_sampleOffsetMatrix4f * vec4(p, 1.0);
	vec3 sampleCoord3f = p + u_cloudMovement3f;
	float noise = 0.0;

	vec3 in3f = sampleCoord3f;
	GET_CLOUDS(in3f, noise, 0.5, p)
	in3f = sampleCoord3f * 2.0 + u_cloudMovement3f;
	GET_CLOUDS(in3f, noise, 0.25, p)
	in3f = sampleCoord3f * 7.0 - u_cloudMovement3f;
	GET_CLOUDS(in3f, noise, 0.125, p)
	in3f = (sampleCoord3f + u_cloudMovement3f) * 16.0;
	GET_CLOUDS(in3f, noise, 0.0625, p)

	output1f = noise;
}
