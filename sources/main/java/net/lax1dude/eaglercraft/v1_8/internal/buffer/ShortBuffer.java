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
public interface ShortBuffer extends Buffer {

	ShortBuffer duplicate();

	short get();

	ShortBuffer put(short b);

	short get(int index);

	ShortBuffer put(int index, short b);

	short getElement(int index);

	void putElement(int index, short value);

	ShortBuffer get(short[] dst, int offset, int length);

	ShortBuffer get(short[] dst);

	ShortBuffer put(ShortBuffer src);

	ShortBuffer put(short[] src, int offset, int length);

	ShortBuffer put(short[] src);

	boolean isDirect();

	ShortBuffer mark();

	ShortBuffer reset();

	ShortBuffer clear();

	ShortBuffer flip();

	ShortBuffer rewind();

	ShortBuffer limit(int newLimit);

	ShortBuffer position(int newPosition);

	short[] array();

}
