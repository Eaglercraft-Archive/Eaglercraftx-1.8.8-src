#line 2

/*
 * Copyright (c) 2023-2024 lax1dude. All Rights Reserved.
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
EAGLER_IN(0, vec2, a_position2f)
EAGLER_VSH_LAYOUT_END()

EAGLER_OUT(vec2, v_texCoords2f)

uniform vec4 u_srcCoords4f;
uniform vec4 u_dstCoords4f;

void main() {
	vec2 uv = a_position2f * 0.5 + 0.5;
	v_texCoords2f = u_srcCoords4f.xy + u_srcCoords4f.zw * uv;
	EAGLER_VERT_POSITION = vec4(u_dstCoords4f.xy + u_dstCoords4f.zw * uv, 0.0, 1.0);
}
