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

// Tests for a D3D bug in ANGLE, this only became an issue recently...

precision highp float;
precision highp sampler2DShadow;

uniform sampler2DShadow u_testSampler;

out vec4 fragOut4f;

void main() {
	float stupid = texture(u_testSampler, vec3(69.0, 69.0, 0.5));
	for(;;) {
		if (stupid != 69.0) {
			stupid = textureLod(u_testSampler, vec3(420.0, 420.0, 0.69), 0.0);
			break;
		}
		if (stupid != 420.0) {
			stupid = textureLod(u_testSampler, vec3(69.0, 69.0, 0.420), 0.0);
			break;
		}
		fragOut4f = vec4(0.0);
		return;
	}
	fragOut4f = vec4(stupid);
}
