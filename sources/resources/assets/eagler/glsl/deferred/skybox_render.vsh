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

layout(location = 0) in vec3 a_position3f;
layout(location = 1) in vec2 a_colorIndex2f;

out vec3 v_position3f;
out vec3 v_color3f;

uniform mat4 u_viewMatrix4f;
#ifdef COMPILE_PARABOLOID_SKY
uniform float u_farPlane1f;
#else
uniform mat4 u_projMatrix4f;
#endif
uniform sampler2D u_renderedAtmosphere;

void main() {
	v_position3f = a_position3f;
	v_color3f = textureLod(u_renderedAtmosphere, a_colorIndex2f, 0.0).rgb;
	vec4 pos = u_viewMatrix4f * vec4(a_position3f, 0.0);

#ifdef COMPILE_PARABOLOID_SKY
	float dist = pos.z;
	pos.xyz = normalize(pos.xyz);
	pos.xy /= 1.0 - pos.z;
	pos.z = dist / u_farPlane1f;
	gl_Position = vec4(pos.xyz, 1.0);
#else
	gl_Position = u_projMatrix4f * vec4(pos.xyz, 1.0);
#endif
}
