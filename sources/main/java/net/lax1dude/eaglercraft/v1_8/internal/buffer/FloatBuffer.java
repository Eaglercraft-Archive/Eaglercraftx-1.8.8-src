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
public interface FloatBuffer extends Buffer {

	FloatBuffer slice();

	FloatBuffer duplicate();

	FloatBuffer asReadOnlyBuffer();

	float get();

	FloatBuffer put(float b);

	float get(int index);

	FloatBuffer put(int index, float b);

	float getElement(int index);

	void putElement(int index, float value);

	FloatBuffer get(float[] dst, int offset, int length);

	FloatBuffer get(float[] dst);

	FloatBuffer put(FloatBuffer src);

	FloatBuffer put(float[] src, int offset, int length);

	FloatBuffer put(float[] src);

	int getArrayOffset();

	FloatBuffer compact();

	boolean isDirect();

	FloatBuffer mark();

	FloatBuffer reset();

	FloatBuffer clear();

	FloatBuffer flip();

	FloatBuffer rewind();

	FloatBuffer limit(int newLimit);

	FloatBuffer position(int newPosition);
	
}

