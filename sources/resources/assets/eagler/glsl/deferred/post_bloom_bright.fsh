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

layout(location = 0) out vec4 output4f;

uniform sampler2D u_lightingHDRFramebufferTexture;
uniform sampler2D u_framebufferLumaAvgInput;
uniform sampler2D u_gbufferMaterialTexture;
uniform sampler2D u_gbufferDepthTexture;

uniform vec4 u_outputSize4f;

void main() {
	float exposure = textureLod(u_framebufferLumaAvgInput, vec2(0.5), 0.0).r;
	float emission = 0.0;
	vec2 alignedUV = (floor(v_position2f * u_outputSize4f.xy) + 0.5) * u_outputSize4f.zw;
	vec4 inputColor = textureLod(u_lightingHDRFramebufferTexture, alignedUV, 0.0);
	if(inputColor.a > 0.0) {
		emission = textureLod(u_gbufferMaterialTexture, alignedUV, 0.0).b;
	}else {
		emission = textureLod(u_gbufferDepthTexture, alignedUV, 0.0).r == 0.0 ? 10.0 : 0.0;
	}
	float f = dot(inputColor.rgb, vec3(0.2126, 0.7152, 0.0722)) * (5.0 + emission * 15.0);
	if(f > 2.0 + exposure) {
		output4f = vec4(min(inputColor.rgb, vec3(5.0)) * (0.75 + exposure * 1.5) * min(f - 2.0 - exposure, 1.0), 1.0);
	}else {
		output4f = vec4(0.0);
	}
}
