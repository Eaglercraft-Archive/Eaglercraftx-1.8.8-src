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

public abstract class FloatBuffer implements Buffer {

	public abstract FloatBuffer duplicate();

	public abstract float get();

	public abstract FloatBuffer put(float b);

	public abstract float get(int index);

	public abstract FloatBuffer put(int index, float b);

	public abstract float getElement(int index);

	public abstract void putElement(int index, float value);

	public abstract FloatBuffer get(float[] dst, int offset, int length);

	public abstract FloatBuffer get(float[] dst);

	public abstract FloatBuffer put(FloatBuffer src);

	public abstract FloatBuffer put(float[] src, int offset, int length);

	public abstract FloatBuffer put(float[] src);

	public abstract boolean isDirect();

	public abstract FloatBuffer mark();

	public abstract FloatBuffer reset();

	public abstract FloatBuffer clear();

	public abstract FloatBuffer flip();

	public abstract FloatBuffer rewind();

	public abstract FloatBuffer limit(int newLimit);

	public abstract int limit();

	public abstract FloatBuffer position(int newPosition);

	public abstract int position();

	public abstract int remaining();

	public abstract boolean hasRemaining();

	public abstract int capacity();

	public abstract boolean hasArray();

	public abstract float[] array();

}