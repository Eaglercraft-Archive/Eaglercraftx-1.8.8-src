package net.lax1dude.eaglercraft.v1_8.internal.buffer;


/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
public interface ByteBuffer extends Buffer {

	ByteBuffer duplicate();

	byte get();

	ByteBuffer put(byte b);

	byte get(int index);

	ByteBuffer put(int index, byte b);

	ByteBuffer get(byte[] dst, int offset, int length);

	ByteBuffer get(byte[] dst);

	ByteBuffer put(ByteBuffer src);

	ByteBuffer put(byte[] src, int offset, int length);

	ByteBuffer put(byte[] src);

	char getChar();

	ByteBuffer putChar(char value);

	char getChar(int index);

	ByteBuffer putChar(int index, char value);

	short getShort();

	ByteBuffer putShort(short value);

	short getShort(int index);

	ByteBuffer putShort(int index, short value);

	ShortBuffer asShortBuffer();

	int getInt();

	ByteBuffer putInt(int value);

	int getInt(int index);

	ByteBuffer putInt(int index, int value);

	IntBuffer asIntBuffer();

	long getLong();

	ByteBuffer putLong(long value);

	long getLong(int index);

	ByteBuffer putLong(int index, long value);

	float getFloat();

	ByteBuffer putFloat(float value);

	float getFloat(int index);

	ByteBuffer putFloat(int index, float value);

	FloatBuffer asFloatBuffer();

	ByteBuffer mark();

	ByteBuffer reset();

	ByteBuffer clear();

	ByteBuffer flip();

	ByteBuffer rewind();

	ByteBuffer limit(int newLimit);

	ByteBuffer position(int newPosition);

	byte[] array();

}
