#line 2

/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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
precision mediump float;
precision mediump sampler2D;

in vec4 v_position4f;
in vec2 v_texCoord2f;
in vec4 v_color4f;
in vec2 v_lightmap2f;

layout(location = 0) out vec4 output4f;

uniform sampler2D u_inputTexture;
uniform sampler2D u_lightmapTexture;

uniform mat4 u_inverseViewMatrix4f;

layout(std140) uniform u_chunkLightingData {
	mediump int u_dynamicLightCount1i;
	mediump int _paddingA_;
	mediump int _paddingB_;
	mediump int _paddingC_;
	mediump vec4 u_dynamicLightArray[12];
};

void main() {
	vec4 color = texture(u_inputTexture, v_texCoord2f) * v_color4f;

	if(color.a < 0.004) {
		discard;
	}

	vec4 light;
	float blockLight = v_lightmap2f.x;
	float diffuse = 0.0;
	float len;
	if(u_dynamicLightCount1i > 0) {
		vec4 worldPosition4f = u_inverseViewMatrix4f * v_position4f;
		worldPosition4f.xyz /= worldPosition4f.w;
		vec3 normalVector3f = normalize(u_inverseViewMatrix4f[2].xyz);
		int safeLightCount = u_dynamicLightCount1i > 12 ? 0 : u_dynamicLightCount1i;
		for(int i = 0; i < safeLightCount; ++i) {
			light = u_dynamicLightArray[i];
			light.xyz = light.xyz - worldPosition4f.xyz;
			len = length(light.xyz);
			diffuse += max(dot(light.xyz / len, normalVector3f) * 0.8 + 0.2, 0.0) * max(light.w - len, 0.0);
		}
		blockLight = min(blockLight + diffuse * 0.066667, 1.0);
	}

	color *= texture(u_lightmapTexture, vec2(blockLight, v_lightmap2f.y));

	output4f = color;
}
