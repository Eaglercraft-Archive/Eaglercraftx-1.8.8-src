package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import org.teavm.interop.Address;
import org.teavm.interop.DirectMalloc;

/**
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
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
public class MemoryStack {

	public static final int STACK_SIZE = 2 * 1024 * 1024;
	public static final int MALLOC_THRESHOLD = 512 * 1024;
	public static final int RESERVE_SIZE = 64 * 1024;

	public static final Address stackBase;
	public static final Address stackMax;

	private static Address stackBottomPointer;
	private static Address stackTopPointer;

	static {
		stackBase = DirectMalloc.malloc(STACK_SIZE + 16);
		if(stackBase.toInt() == 0) {
			throw new IllegalStateException("Could not allocate MemoryStack of size " + STACK_SIZE);
		}
		stackMax = stackBase.add(STACK_SIZE);
		stackBottomPointer = stackBase;
		stackBottomPointer.putInt(0);
		stackBottomPointer.add(4).putInt(0);
		stackTopPointer = stackBottomPointer.add(8);
	}

	public static void push() {
		stackTopPointer.putAddress(stackBottomPointer);
		stackTopPointer.add(4).putInt(0);
		stackBottomPointer = stackTopPointer;
		stackTopPointer = stackBottomPointer.add(8);
		if(stackTopPointer.toInt() > stackMax.toInt()) {
			throw new StackOverflowError();
		}
	}

	public static void pop() {
		stackTopPointer = stackBottomPointer;
		stackBottomPointer = stackTopPointer.getAddress();
		Address cleanup = stackTopPointer.add(4).getAddress();
		while(cleanup.toInt() != 0) {
			WASMGCBufferAllocator.free(cleanup.getAddress());
			cleanup = cleanup.add(4).getAddress();
		}
		if(stackBottomPointer.toInt() == 0) {
			throw new IllegalStateException("MemoryStack underflow");
		}
	}

	public static Address malloc(int length) {
		if(length > MALLOC_THRESHOLD || (stackMax.toInt() - stackTopPointer.toInt()) < RESERVE_SIZE) {
			if(stackTopPointer.toInt() + 8 > stackMax.toInt()) {
				throw new StackOverflowError();
			}
			Address malloced = WASMGCBufferAllocator.malloc(length);
			Address cleanup = stackBottomPointer.add(4).getAddress();
			stackTopPointer.putAddress(malloced);
			stackTopPointer.add(4).putAddress(cleanup);
			stackBottomPointer.add(4).putAddress(stackTopPointer);
			stackTopPointer = stackTopPointer.add(8);
			return malloced;
		}else {
			Address ret = stackTopPointer;
			stackTopPointer = stackTopPointer.add((length + 3) & 0xFFFFFFFC);
			return ret;
		}
	}

	public static ByteBuffer mallocByteBuffer(int length) {
		return new DirectMallocByteBuffer(malloc(length), length, false);
	}

	public static IntBuffer mallocIntBuffer(int length) {
		return new DirectMallocIntBuffer(malloc(length << 2), length, false);
	}

	public static FloatBuffer mallocFloatBuffer(int length) {
		return new DirectMallocFloatBuffer(malloc(length << 2), length, false);
	}

	public static Address calloc(int length) {
		if(length > MALLOC_THRESHOLD || (stackMax.toInt() - stackTopPointer.toInt()) < RESERVE_SIZE) {
			if(stackTopPointer.toInt() + 8 > stackMax.toInt()) {
				throw new StackOverflowError();
			}
			Address malloced = WASMGCBufferAllocator.calloc(length);
			Address cleanup = stackBottomPointer.add(4).getAddress();
			stackTopPointer.putAddress(malloced);
			stackTopPointer.add(4).putAddress(cleanup);
			stackBottomPointer.add(4).putAddress(stackTopPointer);
			stackTopPointer = stackTopPointer.add(8);
			return malloced;
		}else {
			DirectMalloc.zmemset(stackTopPointer, length);
			Address ret = stackTopPointer;
			stackTopPointer = stackTopPointer.add((length + 3) & 0xFFFFFFFC);
			return ret;
		}
	}

	public static ByteBuffer callocByteBuffer(int length) {
		return new DirectMallocByteBuffer(calloc(length), length, false);
	}

	public static IntBuffer callocIntBuffer(int length) {
		return new DirectMallocIntBuffer(calloc(length << 2), length, false);
	}

	public static FloatBuffer callocFloatBuffer(int length) {
		return new DirectMallocFloatBuffer(calloc(length << 2), length, false);
	}

}
