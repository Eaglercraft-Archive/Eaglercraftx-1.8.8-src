package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import org.lwjgl.system.jemalloc.JEmalloc;

/**
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class EaglerLWJGLAllocator {
	
	public static class WrongBufferClassType extends RuntimeException {
		public WrongBufferClassType(String msg) {
			super(msg);
		}
	}

	public static ByteBuffer allocByteBuffer(int len) {
		return new EaglerLWJGLByteBuffer(JEmalloc.nje_malloc(len), len, true);
	}

	public static ShortBuffer allocShortBuffer(int len) {
		return new EaglerLWJGLShortBuffer(JEmalloc.nje_malloc(len << 1), len, true);
	}

	public static IntBuffer allocIntBuffer(int len) {
		return new EaglerLWJGLIntBuffer(JEmalloc.nje_malloc(len << 2), len, true);
	}

	public static FloatBuffer allocFloatBuffer(int len) {
		return new EaglerLWJGLFloatBuffer(JEmalloc.nje_malloc(len << 2), len, true);
	}

	public static void freeByteBuffer(ByteBuffer buffer) {
		if(buffer instanceof EaglerLWJGLByteBuffer) {
			EaglerLWJGLByteBuffer buf = (EaglerLWJGLByteBuffer)buffer;
			if(buf.original) {
				JEmalloc.nje_free(buf.address);
			}else {
				throwNotOriginal(buffer);
			}
		}else {
			throwNotEagler(buffer);
		}
	}

	public static long getAddress(ByteBuffer buffer) {
		if(buffer instanceof EaglerLWJGLByteBuffer) {
			EaglerLWJGLByteBuffer b = (EaglerLWJGLByteBuffer)buffer;
			return b.address + b.position();
		}else {
			throw notEagler(buffer);
		}
	}

	public static void freeShortBuffer(ShortBuffer buffer) {
		if(buffer instanceof EaglerLWJGLShortBuffer) {
			EaglerLWJGLShortBuffer buf = (EaglerLWJGLShortBuffer)buffer;
			if(buf.original) {
				JEmalloc.nje_free(buf.address);
			}else {
				throwNotOriginal(buffer);
			}
		}else {
			throwNotEagler(buffer);
		}
	}

	public static long getAddress(ShortBuffer buffer) {
		if(buffer instanceof EaglerLWJGLShortBuffer) {
			EaglerLWJGLShortBuffer b = (EaglerLWJGLShortBuffer)buffer;
			return b.address + (b.position() << 1);
		}else {
			throw notEagler(buffer);
		}
	}

	public static void freeIntBuffer(IntBuffer buffer) {
		if(buffer instanceof EaglerLWJGLIntBuffer) {
			EaglerLWJGLIntBuffer buf = (EaglerLWJGLIntBuffer)buffer;
			if(buf.original) {
				JEmalloc.nje_free(buf.address);
			}else {
				throwNotOriginal(buffer);
			}
		}else {
			throwNotEagler(buffer);
		}
	}

	public static long getAddress(IntBuffer buffer) {
		if(buffer instanceof EaglerLWJGLIntBuffer) {
			EaglerLWJGLIntBuffer b = (EaglerLWJGLIntBuffer)buffer;
			return b.address + (b.position() << 2);
		}else {
			throw notEagler(buffer);
		}
	}

	public static void freeFloatBuffer(FloatBuffer buffer) {
		if(buffer instanceof EaglerLWJGLFloatBuffer) {
			EaglerLWJGLFloatBuffer buf = (EaglerLWJGLFloatBuffer)buffer;
			if(buf.original) {
				JEmalloc.nje_free(buf.address);
			}else {
				throwNotOriginal(buffer);
			}
		}else {
			throwNotEagler(buffer);
		}
	}

	public static long getAddress(FloatBuffer buffer) {
		if(buffer instanceof EaglerLWJGLFloatBuffer) {
			EaglerLWJGLFloatBuffer b = (EaglerLWJGLFloatBuffer)buffer;
			return b.address + (b.position() << 2);
		}else {
			throw notEagler(buffer);
		}
	}
	
	private static void throwNotOriginal(Object clazz) {
		throw notOriginal(clazz);
	}
	
	private static WrongBufferClassType notOriginal(Object clazz) {
		return new WrongBufferClassType("Tried to pass a " + clazz.getClass().getSimpleName() + " which was not the original buffer");
	}
	
	private static void throwNotEagler(Object clazz) {
		throw notEagler(clazz);
	}
	
	private static WrongBufferClassType notEagler(Object clazz) {
		return new WrongBufferClassType("Tried to pass a " + clazz.getClass().getSimpleName() + " which is not a native eagler buffer");
	}
	
}
