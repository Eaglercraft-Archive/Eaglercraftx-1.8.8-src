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

layout(location = 0) out float exposureOut1f;

uniform sampler2D u_inputTexture;
uniform vec4 u_sampleOffset4f;

#ifdef CALCULATE_LUMINANCE
#define TAKE_SAMPLE(samplerIn, posIn) dot(textureLod(samplerIn, posIn, 0.0).rgb, vec3(0.299, 0.587, 0.114))
#else
#define TAKE_SAMPLE(samplerIn, posIn) textureLod(samplerIn, posIn, 0.0).r
#endif

void main() {

	vec2 pixelPos = floor(v_position2f / u_sampleOffset4f.xy);

	float a = min(TAKE_SAMPLE(u_inputTexture, (pixelPos + vec2(0.25, 0.25)) * u_sampleOffset4f.zw), 250.0);
	a += min(TAKE_SAMPLE(u_inputTexture, (pixelPos + vec2(0.75, 0.25)) * u_sampleOffset4f.zw), 250.0);
	a += min(TAKE_SAMPLE(u_inputTexture, (pixelPos + vec2(0.75, 0.75)) * u_sampleOffset4f.zw), 250.0);
	a += min(TAKE_SAMPLE(u_inputTexture, (pixelPos + vec2(0.25, 0.75)) * u_sampleOffset4f.zw), 250.0);

	exposureOut1f = a * 0.25;
}
