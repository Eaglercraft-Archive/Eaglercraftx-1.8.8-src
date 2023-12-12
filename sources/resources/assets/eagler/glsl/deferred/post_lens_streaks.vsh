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

layout(location = 0) in vec2 a_position2f;
layout(location = 1) in vec2 a_texcoord2f;

out vec2 v_texcoord2f;
out float v_occlusion1f;

uniform sampler2D u_sunOcclusionValue;

uniform mat3 u_sunFlareMatrix3f;

void main() {
	v_occlusion1f = max(textureLod(u_sunOcclusionValue, vec2(0.5, 0.5), 0.0).r * 1.5 - 0.5, 0.0);
	if(v_occlusion1f == 0.0) {
		gl_Position = vec4(-10.0, -10.0, -10.0, 1.0);
		return;
	}
	v_texcoord2f = a_texcoord2f;
	vec3 pos3f = u_sunFlareMatrix3f * vec3(a_position2f, 1.0);
	gl_Position = vec4(pos3f.x, pos3f.y, 0.0, pos3f.z);
}
