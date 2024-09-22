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
public interface IntBuffer extends Buffer {

	IntBuffer duplicate();

	int get();

	IntBuffer put(int b);

	int get(int index);

	IntBuffer put(int index, int b);

	int getElement(int index);

	void putElement(int index, int value);

	IntBuffer get(int[] dst, int offset, int length);

	IntBuffer get(int[] dst);

	IntBuffer put(IntBuffer src);

	IntBuffer put(int[] src, int offset, int length);

	IntBuffer put(int[] src);

	boolean isDirect();

	IntBuffer mark();

	IntBuffer reset();

	IntBuffer clear();

	IntBuffer flip();

	IntBuffer rewind();

	IntBuffer limit(int newLimit);

	IntBuffer position(int newPosition);

	int[] array();

}

