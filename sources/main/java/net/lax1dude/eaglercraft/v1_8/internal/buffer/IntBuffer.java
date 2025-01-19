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
public abstract class IntBuffer implements Buffer {

	public abstract IntBuffer duplicate();

	public abstract int get();

	public abstract IntBuffer put(int b);

	public abstract int get(int index);

	public abstract IntBuffer put(int index, int b);

	public abstract int getElement(int index);

	public abstract void putElement(int index, int value);

	public abstract IntBuffer get(int[] dst, int offset, int length);

	public abstract IntBuffer get(int[] dst);

	public abstract IntBuffer put(IntBuffer src);

	public abstract IntBuffer put(int[] src, int offset, int length);

	public abstract IntBuffer put(int[] src);

	public abstract boolean isDirect();

	public abstract IntBuffer mark();

	public abstract IntBuffer reset();

	public abstract IntBuffer clear();

	public abstract IntBuffer flip();

	public abstract IntBuffer rewind();

	public abstract IntBuffer limit(int newLimit);

	public abstract int limit();

	public abstract IntBuffer position(int newPosition);

	public abstract int position();

	public abstract int remaining();

	public abstract boolean hasRemaining();

	public abstract int capacity();

	public abstract boolean hasArray();

	public abstract int[] array();

}

