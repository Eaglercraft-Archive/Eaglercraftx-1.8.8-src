#line 2 6969

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

#ifdef EAGLER_HAS_GLES_300

// For GLES 3.00+ (WebGL 2.0)
#ifdef EAGLER_IS_VERTEX_SHADER

// Vertex Shaders:
#define EAGLER_VSH_LAYOUT_BEGIN()
#define EAGLER_VSH_LAYOUT_END()
#define EAGLER_IN(_loc, _type, _name) layout(location = _loc) in _type _name;
#define EAGLER_IN_AUTO(_type, _name) in _type _name;
#define EAGLER_OUT(_type, _name) out _type _name;
#define EAGLER_VERT_POSITION gl_Position

#else
#ifdef EAGLER_IS_FRAGMENT_SHADER

// Fragment Shaders:
#define EAGLER_IN(_type, _name) in _type _name;
#define EAGLER_FRAG_COLOR eagler_FragColor
#define EAGLER_FRAG_DEPTH gl_FragDepth

#define EAGLER_FRAG_OUT() layout(location = 0) out vec4 EAGLER_FRAG_COLOR;

#endif
#endif

// All Shaders:

#define EAGLER_TEXTURE_2D(tex, coord2f) texture(tex, coord2f)
#define EAGLER_TEXTURE_2D_LOD(_tex, _coord2f, _lod1f) textureLod(_tex, _coord2f, _lod1f)
#define EAGLER_HAS_TEXTURE_2D_LOD


#else
#ifdef EAGLER_HAS_GLES_200

// For GLES 2.00 (WebGL 1.0)
#ifdef EAGLER_IS_VERTEX_SHADER

// Vertex Shaders:
#define EAGLER_VSH_LAYOUT_BEGIN()
#define EAGLER_VSH_LAYOUT_END()
#define EAGLER_IN(_loc, _type, _name) attribute _type _name;
#define EAGLER_IN_AUTO(_type, _name) attribute _type _name;
#define EAGLER_OUT(_type, _name) varying _type _name;
#define EAGLER_VERT_POSITION gl_Position

#else
#ifdef EAGLER_IS_FRAGMENT_SHADER

// Fragment Shaders:
#define EAGLER_IN(_type, _name) varying _type _name;
#define EAGLER_FRAG_COLOR gl_FragColor
// TODO: Must require EXT_frag_depth to use this on GLES 2.0 (currently not needed)
#define EAGLER_FRAG_DEPTH gl_FragDepth

#define EAGLER_FRAG_OUT()

#endif
#endif

// All Shaders:

#define EAGLER_TEXTURE_2D(_tex, _coord2f) texture2D(_tex, _coord2f)

#ifdef EAGLER_HAS_GLES_200_SHADER_TEXTURE_LOD
#define EAGLER_TEXTURE_2D_LOD(_tex, _coord2f, _lod1f) texture2DLodEXT(_tex, _coord2f, _lod1f)
#define EAGLER_HAS_TEXTURE_2D_LOD
#else
// Beware!
#define EAGLER_TEXTURE_2D_LOD(_tex, _coord2f, _lod1f) texture2D(_tex, _coord2f)
#define EAGLER_HAS_TEXTURE_2D_LOD
#endif

#else
#error Unable to determine API version! (Missing directive EAGLER_HAS_GLES_200 or 300)
#endif
#endif

#line 1 0