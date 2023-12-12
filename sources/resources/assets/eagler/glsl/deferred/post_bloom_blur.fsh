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

uniform sampler2D u_inputTexture;

uniform vec2 u_sampleOffset2f;
uniform vec4 u_outputSize4f;

void main() {
	vec2 alignedUV = (floor(v_position2f * u_outputSize4f.xy) + 0.5) * u_outputSize4f.zw;
	vec4 accum = textureLod(u_inputTexture, alignedUV - u_sampleOffset2f * 7.0, 0.0) * 0.0005;
	accum += textureLod(u_inputTexture, alignedUV - u_sampleOffset2f * 6.0, 0.0) * 0.0024;
	accum += textureLod(u_inputTexture, alignedUV - u_sampleOffset2f * 5.0, 0.0) * 0.0092;
	accum += textureLod(u_inputTexture, alignedUV - u_sampleOffset2f * 4.0, 0.0) * 0.0278;
	accum += textureLod(u_inputTexture, alignedUV - u_sampleOffset2f * 3.0, 0.0) * 0.0656;
	accum += textureLod(u_inputTexture, alignedUV - u_sampleOffset2f * 2.0, 0.0) * 0.1210;
	accum += textureLod(u_inputTexture, alignedUV - u_sampleOffset2f, 0.0) * 0.1747;
	accum += textureLod(u_inputTexture, alignedUV, 0.0) * 0.1974;
	accum += textureLod(u_inputTexture, alignedUV + u_sampleOffset2f, 0.0) * 0.1747;
	accum += textureLod(u_inputTexture, alignedUV + u_sampleOffset2f * 2.0, 0.0) * 0.1210;
	accum += textureLod(u_inputTexture, alignedUV + u_sampleOffset2f * 3.0, 0.0) * 0.0656;
	accum += textureLod(u_inputTexture, alignedUV + u_sampleOffset2f * 4.0, 0.0) * 0.0278;
	accum += textureLod(u_inputTexture, alignedUV + u_sampleOffset2f * 5.0, 0.0) * 0.0092;
	accum += textureLod(u_inputTexture, alignedUV + u_sampleOffset2f * 6.0, 0.0) * 0.0024;
	accum += textureLod(u_inputTexture, alignedUV + u_sampleOffset2f * 7.0, 0.0) * 0.0005;
	output4f = accum;
}
