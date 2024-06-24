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

layout(location = 1) in vec2 e_elementOffsetScale;
layout(location = 2) in vec4 e_elementTexture4f;
layout(location = 3) in vec3 e_elementColor3f;

out vec2 v_texcoord2f;
out vec3 v_color3f;
out float v_occlusion1f;

uniform sampler2D u_exposureValue;
uniform sampler2D u_sunOcclusionValue;

uniform vec2 u_sunPosition2f;
uniform float u_aspectRatio1f;
uniform float u_baseScale1f;

#define FAKE_SIN(valueIn, valueOut)\
	valueOut = abs(1.0 - fract(valueIn * 0.159155) * 2.0);\
	valueOut = valueOut * valueOut * (6.0 - 4.0 * valueOut) - 1.0;

void main() {
	v_occlusion1f = max(textureLod(u_sunOcclusionValue, vec2(0.5, 0.5), 0.0).r * 1.5 - 0.5, 0.0);
	if(v_occlusion1f == 0.0) {
		gl_Position = vec4(-10.0, -10.0, -10.0, 1.0);
		return;
	}

	v_texcoord2f = e_elementTexture4f.xy + (a_position2f * 0.5 + 0.5) * e_elementTexture4f.zw;

	float r = textureLod(u_exposureValue, vec2(0.5, 0.5), 0.0).r * 7.5;

	mat2 rotationMatrix;
	FAKE_SIN(vec2(r + 1.570795, r), rotationMatrix[0])
	rotationMatrix[1].x = -rotationMatrix[0].y;
	rotationMatrix[1].y = rotationMatrix[0].x;

	vec2 transformedVertex = rotationMatrix * (a_position2f * u_baseScale1f * e_elementOffsetScale.y / (4.0 + r * 0.75));
	transformedVertex.x *= u_aspectRatio1f;
	transformedVertex += u_sunPosition2f * (1.0 - e_elementOffsetScale.x);

	v_color3f = e_elementColor3f * (0.05 + dot(u_sunPosition2f, u_sunPosition2f));

	gl_Position = vec4(transformedVertex, 0.0, 1.0);
}
