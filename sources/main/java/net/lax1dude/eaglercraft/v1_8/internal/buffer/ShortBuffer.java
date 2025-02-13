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

public abstract class ShortBuffer implements Buffer {

	public abstract ShortBuffer duplicate();

	public abstract short get();

	public abstract ShortBuffer put(short b);

	public abstract short get(int index);

	public abstract ShortBuffer put(int index, short b);

	public abstract short getElement(int index);

	public abstract void putElement(int index, short value);

	public abstract ShortBuffer get(short[] dst, int offset, int length);

	public abstract ShortBuffer get(short[] dst);

	public abstract ShortBuffer put(ShortBuffer src);

	public abstract ShortBuffer put(short[] src, int offset, int length);

	public abstract ShortBuffer put(short[] src);

	public abstract boolean isDirect();

	public abstract ShortBuffer mark();

	public abstract ShortBuffer reset();

	public abstract ShortBuffer clear();

	public abstract ShortBuffer flip();

	public abstract ShortBuffer rewind();

	public abstract ShortBuffer limit(int newLimit);

	public abstract int limit();

	public abstract ShortBuffer position(int newPosition);

	public abstract int position();

	public abstract int remaining();

	public abstract boolean hasRemaining();

	public abstract int capacity();

	public abstract boolean hasArray();

	public abstract short[] array();

}