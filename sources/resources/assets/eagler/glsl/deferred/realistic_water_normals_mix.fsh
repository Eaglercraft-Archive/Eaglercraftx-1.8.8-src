#line 2

/*
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
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

in vec2 v_position2f;

layout(location = 0) out vec4 combinedNormalsOutput4f;

uniform sampler2D u_gbufferNormalsTexture;
uniform sampler2D u_surfaceNormalsTexture;

void main() {
	combinedNormalsOutput4f = textureLod(u_surfaceNormalsTexture, v_position2f, 0.0);
	if(combinedNormalsOutput4f.a > 0.0) return;
	combinedNormalsOutput4f.rgb = textureLod(u_gbufferNormalsTexture, v_position2f, 0.0).rgb;
	combinedNormalsOutput4f.a = 0.0;
}
