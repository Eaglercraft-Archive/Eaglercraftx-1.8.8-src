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

layout(location = 0) out vec4 output4f;

#define SKY_BRIGHTNESS 0.015

uniform sampler2D u_skyTexture;

uniform vec2 u_skyTextureScale2f;

void main() {
	gl_FragDepth = 0.0;
	vec3 viewDir = normalize(v_position3f);

	vec3 blending = abs(viewDir * viewDir * viewDir);
	blending = normalize(max(blending, 0.00001));
	float b = (blending.x + blending.y + blending.z);
	blending /= b;

	vec3 blendedSkyColor = texture(u_skyTexture, v_position3f.zy * u_skyTextureScale2f).rgb * blending.x;
	blendedSkyColor += texture(u_skyTexture, v_position3f.xz * u_skyTextureScale2f).rgb * blending.y;
	blendedSkyColor += texture(u_skyTexture, v_position3f.xy * u_skyTextureScale2f).rgb * blending.z;

	output4f = vec4(blendedSkyColor * blendedSkyColor * blendedSkyColor * SKY_BRIGHTNESS, 0.0);
}
