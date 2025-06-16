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

in vec3 v_position3f;
in vec3 v_color3f;

#ifndef COMPILE_PARABOLOID_SKY
in vec3 v_positionClip3f;
#endif

layout(location = 0) out vec4 output4f;

uniform vec3 u_sunDirection3f;
uniform vec3 u_sunColor3f;
uniform vec4 u_lightningColor4f;

#ifdef COMPILE_CLOUDS
uniform sampler2D u_cloudsTexture;
#endif

#ifndef COMPILE_PARABOLOID_SKY
uniform sampler2D u_sunOcclusion;
uniform sampler2D u_gbufferDepthTexture;
#endif

#define SKY_BRIGHTNESS 5.0

void main() {
	gl_FragDepth = 0.0;
	vec3 viewDir = normalize(v_position3f);
#ifdef COMPILE_PARABOLOID_SKY
	output4f = vec4(v_color3f * SKY_BRIGHTNESS, 0.0);
#else
	float f = max(dot(viewDir, u_sunDirection3f) - 0.995, 0.0) * 100.0;
	float intensity = min(f * 2.0, 1.0);
	intensity *= intensity * intensity * intensity * textureLod(u_sunOcclusion, vec2(0.5, 0.5), 0.0).r * 2.0;
	intensity *= step(textureLod(u_gbufferDepthTexture, (v_positionClip3f.xy / v_positionClip3f.z) * 0.5 + 0.5, 0.0).r, 0.0);
	output4f = vec4(v_color3f * SKY_BRIGHTNESS + intensity * u_sunColor3f, 0.0);
#endif
#ifdef COMPILE_CLOUDS
	if(viewDir.y < 0.01) {
		output4f.rgb = output4f.rgb * u_lightningColor4f.a + u_lightningColor4f.rgb;
		return;
	}
	vec2 cloudSampleCoord2f = (viewDir.xz / (viewDir.y + 1.0)) * 0.975 * 0.5 + 0.5;
	vec4 cloudSample = textureLod(u_cloudsTexture, cloudSampleCoord2f, 0.0);
	output4f.rgb = mix(output4f.rgb, output4f.rgb * cloudSample.a + cloudSample.rgb, smoothstep(0.0, 1.0, min(viewDir.y * 8.0, 1.0)));
#endif
	output4f.rgb = output4f.rgb * u_lightningColor4f.a + u_lightningColor4f.rgb;

}