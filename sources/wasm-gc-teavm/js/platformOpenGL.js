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

const VAO_IMPL_NONE = -1;
const VAO_IMPL_CORE = 0;
const VAO_IMPL_OES = 1;

const INSTANCE_IMPL_NONE = -1;
const INSTANCE_IMPL_CORE = 0;
const INSTANCE_IMPL_ANGLE = 1;

const CAP_A_BIT_EXT_GPU_SHADER5 = 1;
const CAP_A_BIT_OES_GPU_SHADER5 = 2;
const CAP_A_BIT_FBO_RENDER_MIPMAP = 4;
const CAP_A_BIT_TEXTURE_LOD_CAPABLE = 8;
const CAP_A_BIT_NPOT_CAPABLE = 16;
const CAP_A_BIT_HDR_FBO16F = 32;
const CAP_A_BIT_HDR_FBO32F = 64;
const CAP_A_BIT_ANISOTROPIC = 128;

const CAP_B_BIT_HDR_LINEAR16F = 1;
const CAP_B_BIT_HDR_LINEAR32F = 2;

const platfOpenGLName = "platformOpenGL";

/**
 * @param {WebGL2RenderingContext} ctx
 * @param {number} glesVersIn
 * @param {boolean} allowExts
 * @param {Object} glImports
 */
function setCurrentGLContext(ctx, glesVersIn, allowExts, glImports) {
	const wglExtVAO = (allowExts && glesVersIn === 200) ? ctx.getExtension("OES_vertex_array_object") : null;
	const wglExtInstancing = (allowExts && glesVersIn === 200) ? ctx.getExtension("ANGLE_instanced_arrays") : null;
	const hasANGLEInstancedArrays = allowExts && glesVersIn === 200 && wglExtInstancing !== null;
	const hasEXTColorBufferFloat = allowExts && (glesVersIn === 310 || glesVersIn === 300) && ctx.getExtension("EXT_color_buffer_float") !== null;
	const hasEXTColorBufferHalfFloat = allowExts && !hasEXTColorBufferFloat && (glesVersIn === 310 || glesVersIn === 300 || glesVersIn === 200)
			&& ctx.getExtension("EXT_color_buffer_half_float") !== null;
	const hasEXTShaderTextureLOD = allowExts && glesVersIn === 200 && ctx.getExtension("EXT_shader_texture_lod") !== null;
	const hasOESFBORenderMipmap = allowExts && glesVersIn === 200 && ctx.getExtension("OES_fbo_render_mipmap") !== null;
	const hasOESVertexArrayObject = allowExts && glesVersIn === 200 && wglExtVAO !== null;
	const hasOESTextureFloat = allowExts && glesVersIn === 200 && ctx.getExtension("OES_texture_float") !== null;
	const hasOESTextureFloatLinear = allowExts && glesVersIn >= 300 && ctx.getExtension("OES_texture_float_linear") !== null;
	const hasOESTextureHalfFloat = allowExts && glesVersIn === 200 && ctx.getExtension("OES_texture_half_float") !== null;
	const hasOESTextureHalfFloatLinear = allowExts && glesVersIn === 200 && ctx.getExtension("OES_texture_half_float_linear") !== null;
	const hasEXTTextureFilterAnisotropic = allowExts && ctx.getExtension("EXT_texture_filter_anisotropic") !== null;
	const hasWEBGLDebugRendererInfo = ctx.getExtension("WEBGL_debug_renderer_info") !== null;
	const hasFBO16FSupport = glesVersIn >= 320 || ((glesVersIn >= 300 || hasOESTextureFloat) && (hasEXTColorBufferFloat || hasEXTColorBufferHalfFloat));
	const hasFBO32FSupport = glesVersIn >= 320 || ((glesVersIn >= 300 || hasOESTextureHalfFloat) && hasEXTColorBufferFloat);
	const hasLinearHDR16FSupport = glesVersIn >= 300 || hasOESTextureHalfFloatLinear;
	const hasLinearHDR32FSupport = glesVersIn >= 300 && hasOESTextureFloatLinear;
	const vertexArrayImpl = glesVersIn >= 300 ? VAO_IMPL_CORE : ((glesVersIn === 200 && hasOESVertexArrayObject) ? VAO_IMPL_OES : VAO_IMPL_NONE);
	const instancingImpl = glesVersIn >= 300 ? INSTANCE_IMPL_CORE : ((glesVersIn === 200 && hasANGLEInstancedArrays) ? INSTANCE_IMPL_ANGLE : INSTANCE_IMPL_NONE);
	
	const capBits = [ glesVersIn, vertexArrayImpl, instancingImpl, 0, 0 ];
	if(glesVersIn >= 300 || hasOESFBORenderMipmap) capBits[3] |= CAP_A_BIT_FBO_RENDER_MIPMAP;
	if(glesVersIn >= 300 || hasEXTShaderTextureLOD) capBits[3] |= CAP_A_BIT_TEXTURE_LOD_CAPABLE;
	if(glesVersIn >= 300) capBits[3] |= CAP_A_BIT_NPOT_CAPABLE;
	if(hasFBO16FSupport) capBits[3] |= CAP_A_BIT_HDR_FBO16F;
	if(hasFBO32FSupport) capBits[3] |= CAP_A_BIT_HDR_FBO32F;
	if(hasEXTTextureFilterAnisotropic) capBits[3] |= CAP_A_BIT_ANISOTROPIC;
	if(hasLinearHDR16FSupport) capBits[4] |= CAP_B_BIT_HDR_LINEAR16F;
	if(hasLinearHDR32FSupport) capBits[4] |= CAP_B_BIT_HDR_LINEAR32F;
	
	/**
	 * @param {number} idx
	 * @return {number}
	 */
	glImports["getCapBits"] = function(idx) {
		return capBits[idx];
	};
	
	glImports["glEnable"] = ctx.enable.bind(ctx);
	glImports["glDisable"] = ctx.disable.bind(ctx);
	glImports["glClearColor"] = ctx.clearColor.bind(ctx);
	glImports["glClearDepth"] = ctx.clearDepth.bind(ctx);
	glImports["glClear"] = ctx.clear.bind(ctx);
	glImports["glDepthFunc"] = ctx.depthFunc.bind(ctx);
	glImports["glDepthMask"] = ctx.depthMask.bind(ctx);
	glImports["glCullFace"] = ctx.cullFace.bind(ctx);
	glImports["glViewport"] = ctx.viewport.bind(ctx);
	glImports["glBlendFunc"] = ctx.blendFunc.bind(ctx);
	glImports["glBlendFuncSeparate"] = ctx.blendFuncSeparate.bind(ctx);
	glImports["glBlendEquation"] = ctx.blendEquation.bind(ctx);
	glImports["glBlendColor"] = ctx.blendColor.bind(ctx);
	glImports["glColorMask"] = ctx.colorMask.bind(ctx);
	glImports["glDrawBuffers"] = glesVersIn >= 300 ? ctx.drawBuffers.bind(ctx) : unsupportedFunc(platfOpenGLName, "glDrawBuffers");
	glImports["glReadBuffer"] = glesVersIn >= 300 ? ctx.readBuffer.bind(ctx) : unsupportedFunc(platfOpenGLName, "glReadBuffer");
	glImports["glReadPixels"] = ctx.readPixels.bind(ctx);
	glImports["glPolygonOffset"] = ctx.polygonOffset.bind(ctx);
	glImports["glLineWidth"] = ctx.lineWidth.bind(ctx);
	glImports["glGenBuffers"] = ctx.createBuffer.bind(ctx);
	glImports["glGenTextures"] = ctx.createTexture.bind(ctx);
	glImports["glCreateProgram"] = ctx.createProgram.bind(ctx);
	glImports["glCreateShader"] = ctx.createShader.bind(ctx);
	glImports["glCreateFramebuffer"] = ctx.createFramebuffer.bind(ctx);
	glImports["glCreateRenderbuffer"] = ctx.createRenderbuffer.bind(ctx);
	glImports["glGenQueries"] = glesVersIn >= 300 ? ctx.createQuery.bind(ctx) : unsupportedFunc(platfOpenGLName, "glGenQueries");
	glImports["glDeleteBuffers"] = ctx.deleteBuffer.bind(ctx);
	glImports["glDeleteTextures"] = ctx.deleteTexture.bind(ctx);
	glImports["glDeleteProgram"] = ctx.deleteProgram.bind(ctx);
	glImports["glDeleteShader"] = ctx.deleteShader.bind(ctx);
	glImports["glDeleteFramebuffer"] = ctx.deleteFramebuffer.bind(ctx);
	glImports["glDeleteRenderbuffer"] = ctx.deleteRenderbuffer.bind(ctx);
	glImports["glDeleteQueries"] = glesVersIn >= 300 ? ctx.deleteQuery.bind(ctx) : unsupportedFunc(platfOpenGLName, "glDeleteQueries");
	glImports["glBindBuffer"] = ctx.bindBuffer.bind(ctx);
	glImports["glBufferData"] = ctx.bufferData.bind(ctx);
	glImports["glBufferSubData"] = ctx.bufferSubData.bind(ctx);
	glImports["glEnableVertexAttribArray"] = ctx.enableVertexAttribArray.bind(ctx);
	glImports["glDisableVertexAttribArray"] = ctx.disableVertexAttribArray.bind(ctx);
	glImports["glVertexAttribPointer"] = ctx.vertexAttribPointer.bind(ctx);
	glImports["glActiveTexture"] = ctx.activeTexture.bind(ctx);
	glImports["glBindTexture"] = ctx.bindTexture.bind(ctx);
	glImports["glTexParameterf"] = ctx.texParameterf.bind(ctx);
	glImports["glTexParameteri"] = ctx.texParameteri.bind(ctx);
	glImports["glTexImage3D"] = glesVersIn >= 300 ? ctx.texImage3D.bind(ctx) : unsupportedFunc(platfOpenGLName, "glTexImage3D");
	glImports["glTexImage2D"] = ctx.texImage2D.bind(ctx);
	glImports["glTexSubImage2D"] = ctx.texSubImage2D.bind(ctx);
	glImports["glCopyTexSubImage2D"] = ctx.copyTexSubImage2D.bind(ctx);
	glImports["glTexStorage2D"] = glesVersIn >= 300 ? ctx.texStorage2D.bind(ctx) : unsupportedFunc(platfOpenGLName, "glTexStorage2D");
	glImports["glPixelStorei"] = ctx.pixelStorei.bind(ctx);
	glImports["glGenerateMipmap"] = ctx.generateMipmap.bind(ctx);
	glImports["glShaderSource"] = ctx.shaderSource.bind(ctx);
	glImports["glCompileShader"] = ctx.compileShader.bind(ctx);
	glImports["glGetShaderi"] = ctx.getShaderParameter.bind(ctx);
	glImports["glGetShaderInfoLog"] = ctx.getShaderInfoLog.bind(ctx);
	glImports["glUseProgram"] = ctx.useProgram.bind(ctx);
	glImports["glAttachShader"] = ctx.attachShader.bind(ctx);
	glImports["glDetachShader"] = ctx.detachShader.bind(ctx);
	glImports["glLinkProgram"] = ctx.linkProgram.bind(ctx);
	glImports["glGetProgrami"] = ctx.getProgramParameter.bind(ctx);
	glImports["glGetProgramInfoLog"] = ctx.getProgramInfoLog.bind(ctx);
	glImports["glDrawArrays"] = ctx.drawArrays.bind(ctx);
	glImports["glDrawElements"] = ctx.drawElements.bind(ctx);
	glImports["glBindAttribLocation"] = ctx.bindAttribLocation.bind(ctx);
	glImports["glGetAttribLocation"] = ctx.getAttribLocation.bind(ctx);
	glImports["glGetUniformLocation"] = ctx.getUniformLocation.bind(ctx);
	glImports["glGetUniformBlockIndex"] = glesVersIn >= 300 ? ctx.getUniformBlockIndex.bind(ctx) : unsupportedFunc(platfOpenGLName, "glGetUniformBlockIndex");
	glImports["glBindBufferRange"] = glesVersIn >= 300 ? ctx.bindBufferRange.bind(ctx) : unsupportedFunc(platfOpenGLName, "glBindBufferRange");
	glImports["glUniformBlockBinding"] = glesVersIn >= 300 ? ctx.uniformBlockBinding.bind(ctx) : unsupportedFunc(platfOpenGLName, "glUniformBlockBinding");
	glImports["glUniform1f"] = ctx.uniform1f.bind(ctx);
	glImports["glUniform2f"] = ctx.uniform2f.bind(ctx);
	glImports["glUniform3f"] = ctx.uniform3f.bind(ctx);
	glImports["glUniform4f"] = ctx.uniform4f.bind(ctx);
	glImports["glUniform1i"] = ctx.uniform1i.bind(ctx);
	glImports["glUniform2i"] = ctx.uniform2i.bind(ctx);
	glImports["glUniform3i"] = ctx.uniform3i.bind(ctx);
	glImports["glUniform4i"] = ctx.uniform4i.bind(ctx);
	glImports["glUniformMatrix2fv"] = ctx.uniformMatrix2fv.bind(ctx);
	glImports["glUniformMatrix3fv"] = ctx.uniformMatrix3fv.bind(ctx);
	glImports["glUniformMatrix4fv"] = ctx.uniformMatrix4fv.bind(ctx);
	glImports["glUniformMatrix3x2fv"] = glesVersIn >= 300 ? ctx.uniformMatrix3x2fv.bind(ctx) : unsupportedFunc(platfOpenGLName, "glUniformMatrix3x2fv");
	glImports["glUniformMatrix4x2fv"] = glesVersIn >= 300 ? ctx.uniformMatrix4x2fv.bind(ctx) : unsupportedFunc(platfOpenGLName, "glUniformMatrix4x2fv");
	glImports["glUniformMatrix4x3fv"] = glesVersIn >= 300 ? ctx.uniformMatrix4x3fv.bind(ctx) : unsupportedFunc(platfOpenGLName, "glUniformMatrix4x3fv");
	glImports["glBindFramebuffer"] = ctx.bindFramebuffer.bind(ctx);
	glImports["glCheckFramebufferStatus"] = ctx.checkFramebufferStatus.bind(ctx);
	glImports["glBlitFramebuffer"] = glesVersIn >= 300 ? ctx.blitFramebuffer.bind(ctx) : unsupportedFunc(platfOpenGLName, "glBlitFramebuffer");
	glImports["glRenderbufferStorage"] = ctx.renderbufferStorage.bind(ctx);
	glImports["glFramebufferTexture2D"] = ctx.framebufferTexture2D.bind(ctx);
	glImports["glFramebufferTextureLayer"] = glesVersIn >= 300 ? ctx.framebufferTextureLayer.bind(ctx) : unsupportedFunc(platfOpenGLName, "glFramebufferTextureLayer");
	glImports["glBindRenderbuffer"] = ctx.bindRenderbuffer.bind(ctx);
	glImports["glFramebufferRenderbuffer"] = ctx.framebufferRenderbuffer.bind(ctx);
	glImports["glGetError"] = ctx.getError.bind(ctx);
	glImports["getAllExtensions"] = ctx.getSupportedExtensions.bind(ctx);
	glImports["isContextLost"] = ctx.isContextLost.bind(ctx);
	
	const exts = [];
	if(hasANGLEInstancedArrays) exts.push("ANGLE_instanced_arrays");
	if(hasEXTColorBufferFloat) exts.push("EXT_color_buffer_float");
	if(hasEXTColorBufferHalfFloat) exts.push("EXT_color_buffer_half_float");
	if(hasEXTShaderTextureLOD) exts.push("EXT_shader_texture_lod");
	if(hasOESFBORenderMipmap) exts.push("OES_fbo_render_mipmap");
	if(hasOESVertexArrayObject) exts.push("OES_vertex_array_object");
	if(hasOESTextureFloat) exts.push("OES_texture_float");
	if(hasOESTextureFloatLinear) exts.push("OES_texture_float_linear");
	if(hasOESTextureHalfFloat) exts.push("OES_texture_half_float");
	if(hasOESTextureHalfFloatLinear) exts.push("OES_texture_half_float_linear");
	if(hasEXTTextureFilterAnisotropic) exts.push("EXT_texture_filter_anisotropic");
	if(hasWEBGLDebugRendererInfo) exts.push("WEBGL_debug_renderer_info");
	
	/**
	 * @return {Array}
	 */
	glImports["dumpActiveExtensions"] = function() {
		return exts;
	};
	
	/**
	 * @param {number} p
	 * @return {number}
	 */
	glImports["glGetInteger"] = function(p) {
		const ret = /** @type {*} */ (ctx.getParameter(p));
		return (typeof ret === "number") ? (/** @type {number} */ (ret)) : 0;
	};
	
	/**
	 * @param {number} p
	 * @return {string|null}
	 */
	glImports["glGetString"] = function(p) {
		var s;
		if(hasWEBGLDebugRendererInfo) {
			switch(p) {
			case 0x1f00: // VENDOR
				s = ctx.getParameter(0x9245); // UNMASKED_VENDOR_WEBGL
				if(s == null) {
					s = ctx.getParameter(0x1f00); // VENDOR
				}
				break;
			case 0x1f01: // RENDERER
				s = ctx.getParameter(0x9246); // UNMASKED_RENDERER_WEBGL
				if(s == null) {
					s = ctx.getParameter(0x1f01); // RENDERER
				}
				break;
			default:
				s = ctx.getParameter(p);
				break;
			}
		}else {
			s = ctx.getParameter(p);
		}
		if(typeof s === "string") {
			return s;
		}else {
			return null;
		}
	};
	
	switch(vertexArrayImpl) {
	case VAO_IMPL_CORE:
		glImports["glGenVertexArrays"] = ctx.createVertexArray.bind(ctx);
		glImports["glDeleteVertexArrays"] = ctx.deleteVertexArray.bind(ctx);
		glImports["glBindVertexArray"] = ctx.bindVertexArray.bind(ctx);
		break;
	case VAO_IMPL_OES:
		glImports["glGenVertexArrays"] = wglExtVAO.createVertexArrayOES.bind(wglExtVAO);
		glImports["glDeleteVertexArrays"] = wglExtVAO.deleteVertexArrayOES.bind(wglExtVAO);
		glImports["glBindVertexArray"] = wglExtVAO.bindVertexArrayOES.bind(wglExtVAO);
		break;
	case VAO_IMPL_NONE:
	default:
		setUnsupportedFunc(glImports, platfOpenGLName, "glGenVertexArrays");
		setUnsupportedFunc(glImports, platfOpenGLName, "glDeleteVertexArrays");
		setUnsupportedFunc(glImports, platfOpenGLName, "glBindVertexArray");
		break;
	}
	
	switch(instancingImpl) {
	case INSTANCE_IMPL_CORE:
		glImports["glVertexAttribDivisor"] = ctx.vertexAttribDivisor.bind(ctx);
		glImports["glDrawArraysInstanced"] = ctx.drawArraysInstanced.bind(ctx);
		glImports["glDrawElementsInstanced"] = ctx.drawElementsInstanced.bind(ctx);
		break;
	case INSTANCE_IMPL_ANGLE:
		glImports["glVertexAttribDivisor"] = wglExtInstancing.vertexAttribDivisorANGLE.bind(wglExtInstancing);
		glImports["glDrawArraysInstanced"] = wglExtInstancing.drawArraysInstancedANGLE.bind(wglExtInstancing);
		glImports["glDrawElementsInstanced"] = wglExtInstancing.drawElementsInstancedANGLE.bind(wglExtInstancing);
		break;
	case INSTANCE_IMPL_NONE:
	default:
		setUnsupportedFunc(glImports, platfOpenGLName, "glVertexAttribDivisor");
		setUnsupportedFunc(glImports, platfOpenGLName, "glDrawArraysInstanced");
		setUnsupportedFunc(glImports, platfOpenGLName, "glDrawElementsInstanced");
		break;
	}
}

function setNoGLContext(glImports) {
	setUnsupportedFunc(glImports, platfOpenGLName, "getCapBits");
	setUnsupportedFunc(glImports, platfOpenGLName, "glEnable");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDisable");
	setUnsupportedFunc(glImports, platfOpenGLName, "glClearColor");
	setUnsupportedFunc(glImports, platfOpenGLName, "glClearDepth");
	setUnsupportedFunc(glImports, platfOpenGLName, "glClear");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDepthFunc");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDepthMask");
	setUnsupportedFunc(glImports, platfOpenGLName, "glCullFace");
	setUnsupportedFunc(glImports, platfOpenGLName, "glViewport");
	setUnsupportedFunc(glImports, platfOpenGLName, "glBlendFunc");
	setUnsupportedFunc(glImports, platfOpenGLName, "glBlendFuncSeparate");
	setUnsupportedFunc(glImports, platfOpenGLName, "glBlendEquation");
	setUnsupportedFunc(glImports, platfOpenGLName, "glBlendColor");
	setUnsupportedFunc(glImports, platfOpenGLName, "glColorMask");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDrawBuffers");
	setUnsupportedFunc(glImports, platfOpenGLName, "glReadBuffer");
	setUnsupportedFunc(glImports, platfOpenGLName, "glReadPixels");
	setUnsupportedFunc(glImports, platfOpenGLName, "glPolygonOffset");
	setUnsupportedFunc(glImports, platfOpenGLName, "glLineWidth");
	setUnsupportedFunc(glImports, platfOpenGLName, "glGenBuffers");
	setUnsupportedFunc(glImports, platfOpenGLName, "glGenTextures");
	setUnsupportedFunc(glImports, platfOpenGLName, "glCreateProgram");
	setUnsupportedFunc(glImports, platfOpenGLName, "glCreateShader");
	setUnsupportedFunc(glImports, platfOpenGLName, "glCreateFramebuffer");
	setUnsupportedFunc(glImports, platfOpenGLName, "glCreateRenderbuffer");
	setUnsupportedFunc(glImports, platfOpenGLName, "glGenQueries");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDeleteBuffers");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDeleteTextures");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDeleteProgram");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDeleteShader");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDeleteFramebuffer");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDeleteRenderbuffer");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDeleteQueries");
	setUnsupportedFunc(glImports, platfOpenGLName, "glBindBuffer");
	setUnsupportedFunc(glImports, platfOpenGLName, "glBufferData");
	setUnsupportedFunc(glImports, platfOpenGLName, "glBufferSubData");
	setUnsupportedFunc(glImports, platfOpenGLName, "glEnableVertexAttribArray");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDisableVertexAttribArray");
	setUnsupportedFunc(glImports, platfOpenGLName, "glVertexAttribPointer");
	setUnsupportedFunc(glImports, platfOpenGLName, "glActiveTexture");
	setUnsupportedFunc(glImports, platfOpenGLName, "glBindTexture");
	setUnsupportedFunc(glImports, platfOpenGLName, "glTexParameterf");
	setUnsupportedFunc(glImports, platfOpenGLName, "glTexParameteri");
	setUnsupportedFunc(glImports, platfOpenGLName, "glTexImage3D");
	setUnsupportedFunc(glImports, platfOpenGLName, "glTexImage2D");
	setUnsupportedFunc(glImports, platfOpenGLName, "glTexSubImage2D");
	setUnsupportedFunc(glImports, platfOpenGLName, "glCopyTexSubImage2D");
	setUnsupportedFunc(glImports, platfOpenGLName, "glTexStorage2D");
	setUnsupportedFunc(glImports, platfOpenGLName, "glPixelStorei");
	setUnsupportedFunc(glImports, platfOpenGLName, "glGenerateMipmap");
	setUnsupportedFunc(glImports, platfOpenGLName, "glShaderSource");
	setUnsupportedFunc(glImports, platfOpenGLName, "glCompileShader");
	setUnsupportedFunc(glImports, platfOpenGLName, "glGetShaderi");
	setUnsupportedFunc(glImports, platfOpenGLName, "glGetShaderInfoLog");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUseProgram");
	setUnsupportedFunc(glImports, platfOpenGLName, "glAttachShader");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDetachShader");
	setUnsupportedFunc(glImports, platfOpenGLName, "glLinkProgram");
	setUnsupportedFunc(glImports, platfOpenGLName, "glGetProgrami");
	setUnsupportedFunc(glImports, platfOpenGLName, "glGetProgramInfoLog");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDrawArrays");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDrawElements");
	setUnsupportedFunc(glImports, platfOpenGLName, "glBindAttribLocation");
	setUnsupportedFunc(glImports, platfOpenGLName, "glGetAttribLocation");
	setUnsupportedFunc(glImports, platfOpenGLName, "glGetUniformLocation");
	setUnsupportedFunc(glImports, platfOpenGLName, "glGetUniformBlockIndex");
	setUnsupportedFunc(glImports, platfOpenGLName, "glBindBufferRange");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUniformBlockBinding");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUniform1f");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUniform2f");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUniform3f");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUniform4f");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUniform1i");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUniform2i");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUniform3i");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUniform4i");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUniformMatrix2fv");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUniformMatrix3fv");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUniformMatrix4fv");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUniformMatrix3x2fv");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUniformMatrix4x2fv");
	setUnsupportedFunc(glImports, platfOpenGLName, "glUniformMatrix4x3fv")
	setUnsupportedFunc(glImports, platfOpenGLName, "glBindFramebuffer");
	setUnsupportedFunc(glImports, platfOpenGLName, "glCheckFramebufferStatus");
	setUnsupportedFunc(glImports, platfOpenGLName, "glBlitFramebuffer");
	setUnsupportedFunc(glImports, platfOpenGLName, "glRenderbufferStorage");
	setUnsupportedFunc(glImports, platfOpenGLName, "glFramebufferTexture2D");
	setUnsupportedFunc(glImports, platfOpenGLName, "glFramebufferTextureLayer");
	setUnsupportedFunc(glImports, platfOpenGLName, "glBindRenderbuffer");
	setUnsupportedFunc(glImports, platfOpenGLName, "glFramebufferRenderbuffer");
	setUnsupportedFunc(glImports, platfOpenGLName, "glGetInteger");
	setUnsupportedFunc(glImports, platfOpenGLName, "glGetError");
	setUnsupportedFunc(glImports, platfOpenGLName, "getAllExtensions");
	setUnsupportedFunc(glImports, platfOpenGLName, "dumpActiveExtensions");
	setUnsupportedFunc(glImports, platfOpenGLName, "glGetString");
	setUnsupportedFunc(glImports, platfOpenGLName, "glGenVertexArrays");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDeleteVertexArrays");
	setUnsupportedFunc(glImports, platfOpenGLName, "glBindVertexArray");
	setUnsupportedFunc(glImports, platfOpenGLName, "glVertexAttribDivisor");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDrawArraysInstanced");
	setUnsupportedFunc(glImports, platfOpenGLName, "glDrawElementsInstanced");
	setUnsupportedFunc(glImports, platfOpenGLName, "isContextLost");
}
