/*
 * Copyright (c) 2023-2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.opengl;

import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

/**
 * This streaming implementation was designed by reverse engineering the OpenGL
 * driver that powers most Intel-based Chromebooks, performance may vary on
 * other platforms
 */
public class StreamBuffer {

	protected static IBufferGL buffer = null;

	protected static int currentOffset = 0;
	protected static int currentSize = 0;

	public static IBufferGL getBuffer() {
		if (buffer == null) {
			return buffer = _wglGenBuffers();
		}
		return buffer;
	}

	public static int uploadData(int elSize, int elCount, boolean quads) {
		EaglercraftGPU.bindGLArrayBuffer(getBuffer());
		int off = (currentOffset + elSize - 1) / elSize;
		if (quads) {
			off = (off + 3) & -4;
		}
		int offBytes = off * elSize;
		int reqBytes = elCount * elSize;
		if (currentSize - offBytes >= reqBytes) {
			currentOffset = offBytes + reqBytes;
			return off;
		} else {
			currentOffset = 0;
			currentSize = (reqBytes + 0xFFFF) & 0xFFFFF000;
			_wglBufferData(GL_ARRAY_BUFFER, currentSize, GL_STREAM_DRAW);
			return 0;
		}
	}

	public static void destroyPool() {
		if (buffer != null) {
			_wglDeleteBuffers(buffer);
			buffer = null;
		}
	}

}