#line 2

/*
 * Copyright (c) 2023-2025 lax1dude. All Rights Reserved.
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
in vec3 v_position3f;

layout(location = 0) out vec4 output4f;

uniform vec3 u_moonColor3f;
uniform vec3 u_lightDir3f;

uniform sampler2D u_moonTextures;
uniform sampler2D u_cloudsTexture;

#define MOON_SURFACE 0.9
#define MOON_MARGIN 0.0125

void main() {
	gl_FragDepth = 0.0;
	vec2 coord2f = v_position2f * 2.0 - 1.0;

	vec2 texUV = (coord2f * (1.0 / (MOON_SURFACE + MOON_MARGIN))) * 0.5 + 0.5;
	vec4 color4f = vec4(0.0);
	if(texUV == clamp(texUV, vec2(0.0), vec2(1.0))) {
		color4f = texture(u_moonTextures, texUV);
	}

	vec3 moonNormal3f;
	moonNormal3f.xy = color4f.rg * 2.0 - 1.0;
	moonNormal3f.z = sqrt(1.0 - dot(moonNormal3f.xy, moonNormal3f.xy));
	float NdotV = max(dot(moonNormal3f, u_lightDir3f), 0.0);
	vec3 viewDir = normalize(v_position3f);

	vec2 surfaceCoord2f = coord2f * (1.0 / MOON_SURFACE);
	vec3 moonAtmosNormalInner3f = vec3(surfaceCoord2f, sqrt(1.0 - dot(surfaceCoord2f, surfaceCoord2f)));
	vec3 moonAtmosNormalOuter3f = vec3(surfaceCoord2f, sqrt(-1.0 + dot(surfaceCoord2f, surfaceCoord2f)));
	float NdotVInner = max(dot(moonAtmosNormalInner3f, u_lightDir3f), 0.0);
	float NdotVOuter = max(dot(moonAtmosNormalOuter3f, u_lightDir3f) + 0.65, 0.0);

	float atmosInner = max((MOON_SURFACE * 0.2) / moonAtmosNormalInner3f.z - 0.2, 0.0);
	float atmosOuter = max((MOON_SURFACE * 0.2) / moonAtmosNormalOuter3f.z - 0.4, 0.0);

	output4f = vec4(u_moonColor3f * (color4f.b * color4f.b * NdotV + (NdotVInner * atmosInner + NdotVOuter * atmosOuter * vec3(0.8, 0.825, 0.9)) * (0.5 - max(u_lightDir3f.z, 0.0) * 0.25)), 0.0);

	if(viewDir.y < 0.01) {
		return;
	}

	vec2 cloudSampleCoord2f = (viewDir.xz / (viewDir.y + 1.0)) * 0.975 * 0.5 + 0.5;
	vec4 cloudSample = textureLod(u_cloudsTexture, cloudSampleCoord2f, 0.0);
	output4f.rgb = mix(output4f.rgb, output4f.rgb * max(cloudSample.a * 1.25 - 0.25, 0.0), smoothstep(0.0, 1.0, min(viewDir.y * 8.0, 1.0)));
}
