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

uniform mat4x3 u_sampleMatrix4x3f;
uniform sampler2D u_cloudsTexture;

#define SAMPLE_DENSITY(v, a_, f)\
	f = u_sampleMatrix4x3f * v;\
	f.xy = (f.xz / (f.y + 1.0)) * 0.975 * 0.5 + 0.5;\
	if(f.xy == clamp(f.xy, vec2(0.001), vec2(0.999)))\
		a_ += textureLod(u_cloudsTexture, f.xy, 0.0).a * 0.125;\
	else\
		a_ += 0.125;

void main() {
	vec3 f;
	float accum = 0.0;

	SAMPLE_DENSITY(vec4(0.000, 0.000, 1.000, 1.0), accum, f)
	SAMPLE_DENSITY(vec4(0.844, 0.521, 0.126, 1.0), accum, f)
	SAMPLE_DENSITY(vec4(-0.187, 0.979, 0.087, 1.0), accum, f)
	SAMPLE_DENSITY(vec4(0.402, -0.904, 0.145, 1.0), accum, f)
	SAMPLE_DENSITY(vec4(-0.944, -0.316, 0.098, 1.0), accum, f)
	SAMPLE_DENSITY(vec4(-0.759, 0.427, 0.491, 1.0), accum, f)
	SAMPLE_DENSITY(vec4(0.955, -0.285, 0.076, 1.0), accum, f)
	SAMPLE_DENSITY(vec4(-0.322, -0.664, 0.675, 1.0), accum, f)

	occlusionOut1f = clamp(sqrt(accum) * 3.0 - 1.0, 0.0, 1.0);
}
