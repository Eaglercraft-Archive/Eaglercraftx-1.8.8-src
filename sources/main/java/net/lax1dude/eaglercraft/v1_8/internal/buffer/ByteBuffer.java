/*
 * Copyright (c) 2022-2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.internal.buffer;


public abstract class ByteBuffer implements Buffer {

	public abstract ByteBuffer duplicate();

	public abstract byte get();

	public abstract ByteBuffer put(byte b);

	public abstract byte get(int index);

	public abstract ByteBuffer put(int index, byte b);

	public abstract ByteBuffer get(byte[] dst, int offset, int length);

	public abstract ByteBuffer get(byte[] dst);

	public abstract ByteBuffer put(ByteBuffer src);

	public abstract ByteBuffer put(byte[] src, int offset, int length);

	public abstract ByteBuffer put(byte[] src);

	public abstract char getChar();

	public abstract ByteBuffer putChar(char value);

	public abstract char getChar(int index);

	public abstract ByteBuffer putChar(int index, char value);

	public abstract short getShort();

	public abstract ByteBuffer putShort(short value);

	public abstract short getShort(int index);

	public abstract ByteBuffer putShort(int index, short value);

	public abstract ShortBuffer asShortBuffer();

	public abstract int getInt();

	public abstract ByteBuffer putInt(int value);

	public abstract int getInt(int index);

	public abstract ByteBuffer putInt(int index, int value);

	public abstract IntBuffer asIntBuffer();

	public abstract long getLong();

	public abstract ByteBuffer putLong(long value);

	public abstract long getLong(int index);

	public abstract ByteBuffer putLong(int index, long value);

	public abstract float getFloat();

	public abstract ByteBuffer putFloat(float value);

	public abstract float getFloat(int index);

	public abstract ByteBuffer putFloat(int index, float value);

	public abstract FloatBuffer asFloatBuffer();

	public abstract ByteBuffer mark();

	public abstract ByteBuffer reset();

	public abstract ByteBuffer clear();

	public abstract ByteBuffer flip();

	public abstract ByteBuffer rewind();

	public abstract ByteBuffer limit(int newLimit);

	public abstract int limit();

	public abstract ByteBuffer position(int newPosition);

	public abstract int position();

	public abstract int remaining();

	public abstract boolean hasRemaining();

	public abstract int capacity();

	public abstract boolean hasArray();

	public abstract byte[] array();

}