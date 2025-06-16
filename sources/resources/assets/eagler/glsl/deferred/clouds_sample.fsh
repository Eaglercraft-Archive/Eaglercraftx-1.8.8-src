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
precision highp sampler3D;

in vec2 v_position2f;

layout(location = 0) out vec4 output4f;

uniform float u_rainStrength1f;
uniform vec4 u_densityModifier4f;
uniform float u_sampleStep1f;
uniform float u_cloudTimer1f;
uniform vec3 u_cloudOffset3f;
uniform vec3 u_sunDirection3f;
uniform vec3 u_sunColor3f;

uniform sampler3D u_noiseTexture3D;
uniform sampler2D u_skyIrradianceMap;
#define GET_CLOUDS(pos3f, out1f)\
	if(pos3f == clamp(pos3f, vec3(0.0), vec3(1.0))) {\
		out1f = length(pos3f - clamp(pos3f, vec3(0.05), vec3(0.95)));\
		out1f = smoothstep(0.0, 1.0, max(1.0 - out1f * 15.0, 0.0));\
		out1f *= textureLod(u_noiseTexture3D, pos3f.xzy, 0.0).r;\
		out1f += u_densityModifier4f.w;\
		out1f = max(out1f * out1f * u_densityModifier4f.x + out1f * u_densityModifier4f.y + u_densityModifier4f.z, 0.0);\
	}else out1f = 0.0;

void main() {
	vec2 latLong = v_position2f * 2.0 - 1.0;
	float latLongLen = dot(latLong, latLong);
	if(latLongLen > 1.025) {
		output4f = vec4(0.0);
		return;
	}

	float mag2 = 2.0 / (latLongLen + 1.0);
	vec3 dir;
	dir.y = mag2 - 1.0;
	dir.xz = latLong * mag2;

	vec3 samplePos = vec3(0.0, -4.5 + u_cloudOffset3f.y * 0.05, 0.0) + dir * u_sampleStep1f * 0.2;
	samplePos = samplePos * vec3(0.05, 0.1, 0.05) + vec3(0.5, 0.0, 0.5);

	float sample0, sample1;
	GET_CLOUDS(samplePos, sample0)

	if(sample0 < 0.002) {
		output4f = vec4(0.0, 0.0, 0.0, 1.0);
		return;
	}

	output4f.a = exp2(-sample0 * 5.0);

	vec3 sunDirection = u_sunDirection3f * vec3(1.0, 2.0, 1.0) * 0.025;
	float sunVisibility = sample0;

	vec3 pos = samplePos + sunDirection;
	GET_CLOUDS(pos, sample1)
	sunVisibility += sample1;
	pos = samplePos + sunDirection * 2.0;
	GET_CLOUDS(pos, sample1)
	sunVisibility += sample1;
	pos = samplePos + sunDirection * 3.0;
	GET_CLOUDS(pos, sample1)
	sunVisibility += sample1;
	pos = samplePos + sunDirection * 4.0;
	GET_CLOUDS(pos, sample1)
	sunVisibility += sample1;
	pos = samplePos + sunDirection * 5.0;
	GET_CLOUDS(pos, sample1)
	sunVisibility += sample1;

	sunVisibility = exp2(-sunVisibility * 50.0);
	sunVisibility *= 1.0 - exp2(-sample0 * 1.2);

	vec3 sky = textureLod(u_skyIrradianceMap, v_position2f * vec2(1.0, 0.5) + vec2(0.0, 0.5), 0.0).rgb * 0.05;
	float intergal = exp2(-7.33 * sample0) * -9.0 + 9.0;

	output4f.rgb = (u_sunColor3f * sunVisibility + sky) * intergal;
}
