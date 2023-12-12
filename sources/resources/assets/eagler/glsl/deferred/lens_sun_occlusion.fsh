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

layout(location = 0) out float occlusionOut1f;

uniform mat3 u_sampleMatrix3f;
uniform sampler2D u_depthBufferTexture;
uniform sampler2D u_cloudsSunOcclusion;

#define SAMPLE_DEPTH(v, a, f)\
	f = u_sampleMatrix3f * v;\
	f.xy /= f.z;\
	if(f.xy == clamp(f.xy, vec2(0.001), vec2(0.999)))\
		a += textureLod(u_depthBufferTexture, f.xy, 0.0).r > 0.000001 ? 0.0 : 0.0417;

void main() {
	vec3 f;
	float accum = 0.0;
	float cloud = textureLod(u_cloudsSunOcclusion, vec2(0.5, 0.5), 0.0).r;
	if(cloud < 0.01) {
		occlusionOut1f = 0.0;
		return;
	}

	SAMPLE_DEPTH(vec3(0.0, 0.0, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(-0.235, -0.962, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(0.029, 0.996, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(0.834, -0.509, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(-0.981, -0.086, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(0.821, 0.478, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(-0.614, 0.563, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(0.251, -0.578, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(-0.571, -0.491, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(0.142, 0.494, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(0.533, -0.036, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(0.970, -0.035, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(-0.388, 0.918, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(-0.521, 0.067, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(-0.140, -0.471, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(0.487, 0.692, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(-0.157, 0.331, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(0.559, -0.760, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(0.156, -0.956, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(0.181, -0.267, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(-0.773, 0.272, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(0.329, 0.228, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(-0.341, -0.187, 1.0), accum, f)
	SAMPLE_DEPTH(vec3(-0.121, 0.689, 1.0), accum, f)

	occlusionOut1f = min(accum * cloud, 1.0);
}
