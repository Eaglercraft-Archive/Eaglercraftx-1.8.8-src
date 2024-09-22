#line 2

/*
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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

EAGLER_VSH_LAYOUT_BEGIN()
EAGLER_IN(0, vec3, a_position3f)
EAGLER_IN(1, vec2, c_position2i)
EAGLER_IN(2, vec2, c_coords2i)
EAGLER_IN(3, vec4, c_color4f)
EAGLER_VSH_LAYOUT_END()

EAGLER_OUT(vec2, v_texCoord2f)
EAGLER_OUT(vec4, v_color4f)

uniform mat4 u_matrixTransform;
uniform vec2 u_charSize2f;
uniform vec2 u_charCoordSize2f;
uniform vec4 u_color4f;

void main() {
	v_color4f = c_color4f.bgra;
	float shadowBit = a_position3f.z;
	float boldBit = shadowBit >= 0.5 ? 1.0 : 0.0;
	shadowBit -= boldBit * 0.5;
	v_color4f.rgb *= (1.0 - shadowBit * 3.0);
	v_texCoord2f = (c_coords2i + a_position3f.xy) * u_charCoordSize2f;
	vec2 pos2d = c_position2i + vec2(shadowBit * 4.0);
	pos2d += a_position3f.xy * u_charSize2f;
	pos2d.x += boldBit;
	float italicBit = v_color4f.a >= 0.5 ? 2.0 : 0.0;
	v_color4f.a -= italicBit * 0.25;
	pos2d.x -= (a_position3f.y - 0.5) * italicBit;
	v_color4f.a *= 2.0;
	v_color4f *= u_color4f;
	EAGLER_VERT_POSITION = u_matrixTransform * vec4(pos2d, 0.0, 1.0);
}
