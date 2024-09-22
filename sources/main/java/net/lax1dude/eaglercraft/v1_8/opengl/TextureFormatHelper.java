package net.lax1dude.eaglercraft.v1_8.opengl;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ExtGLEnums.*;

/**
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
public class TextureFormatHelper {

	public static int getFormatFromInternal(int internalFormat) {
		switch(internalFormat) {
		case _GL_R8:
		case 0x822D: // GL_R16F
		case 0x822E: // GL_R32F
			return GL_RED;
		case _GL_RG8:
		case 0x822F: // GL_RG16F
		case 0x8230: // GL_RG32F
			return _GL_RG;
		case GL_RGB8:
		case _GL_RGB16F:
		case 0x8815: // GL_RGB32F
			return GL_RGB;
		case GL_RGBA8:
		case 0x881A: // GL_RGBA16F
		case 0x8814: // GL_RGBA32F
			return GL_RGBA;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public static int getTypeFromInternal(int internalFormat) {
		switch(internalFormat) {
		case _GL_R8:
		case _GL_RG8:
		case GL_RGB8:
		case GL_RGBA8:
			return GL_UNSIGNED_BYTE;
		case 0x822D: // GL_R16F
		case 0x822F: // GL_RG16F
		case _GL_RGB16F:
		case 0x881A: // GL_RGBA16F
			return _GL_HALF_FLOAT;
		case 0x822E: // GL_R32F
		case 0x8230: // GL_RG32F
		case 0x8815: // GL_RGB32F
		case 0x8814: // GL_RGBA32F
			return GL_FLOAT;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public static int trivializeInternalFormatToGLES20(int internalFormat) {
		switch(internalFormat) {
		case _GL_R8:
			return GL_LUMINANCE;
		case GL_RGB8:
			return GL_RGB;
		case GL_RGBA8:
			return GL_RGBA;
		default:
			throw new UnsupportedOperationException();
		}
	}

}
