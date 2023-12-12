package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import org.teavm.jso.typedarrays.ArrayBufferView;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.webgl.WebGLBuffer;
import org.teavm.jso.webgl.WebGLProgram;
import org.teavm.jso.webgl.WebGLRenderingContext;
import org.teavm.jso.webgl.WebGLTexture;
import org.teavm.jso.webgl.WebGLUniformLocation;

/**
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
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
public interface WebGL2RenderingContext extends WebGLRenderingContext {

	int TEXTURE_MAX_LEVEL              = 0x0000813D;
	int TEXTURE_MAX_ANISOTROPY_EXT     = 0x000084FE;
	int UNSIGNED_INT_24_8              = 0x000084FA;
	int ANY_SAMPLES_PASSED             = 0x00008D6A; 
	int QUERY_RESULT                   = 0x00008866;
	int QUERY_RESULT_AVAILABLE         = 0x00008867;
	int DEPTH24_STENCIL8               = 0x000088F0;
	int DEPTH_COMPONENT24              = 0x000081A6;
	int DEPTH_COMPONENT32F             = 0x00008CAC;
	int READ_FRAMEBUFFER               = 0x00008CA8;
	int DRAW_FRAMEBUFFER               = 0x00008CA9;
	int RGB8                           = 0x00008051;
	int RGBA8                          = 0x00008058;
	int R8                             = 0x00008229;
	int RED                            = 0x00001903;
	
	WebGLQuery createQuery();

	void beginQuery(int p1, WebGLQuery obj);

	void endQuery(int p1);

	void deleteQuery(WebGLQuery obj);

	int getQueryParameter(WebGLQuery obj, int p2);

	WebGLVertexArray createVertexArray();

	void deleteVertexArray(WebGLVertexArray obj);  

	void bindVertexArray(WebGLVertexArray obj); 
	
	void renderbufferStorageMultisample(int p1, int p2, int p3, int p4, int p5);
	
	void blitFramebuffer(int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, int p9, int p10);

	void drawBuffers(int[] p1);
	
	void readBuffer(int p1);

	void vertexAttribDivisor(int p1, int p2);

	void drawArraysInstanced(int p1, int p2, int p3, int p4);

	void drawElementsInstanced(int p1, int p2, int p3, int p4, int p5);

	int getUniformBlockIndex(WebGLProgram p1, String p2);

	void bindBufferRange(int p1, int p2, WebGLBuffer p3, int p4, int p5);

	void uniformBlockBinding(WebGLProgram p1, int p2, int p3);

	void uniformMatrix3x2fv(WebGLUniformLocation location, boolean transpose, Float32Array value);

	void uniformMatrix4x2fv(WebGLUniformLocation location, boolean transpose, Float32Array value);

	void uniformMatrix4x3fv(WebGLUniformLocation location, boolean transpose, Float32Array value);

	void texStorage2D(int target, int levels, int internalFormat, int width, int height);

	void texImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int format,
			int type, ArrayBufferView pixels);

	void framebufferTextureLayer(int target, int attachment, WebGLTexture texture, int level, int layer);

}
