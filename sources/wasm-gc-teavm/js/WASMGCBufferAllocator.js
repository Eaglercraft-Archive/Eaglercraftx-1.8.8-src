/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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

const WASMGCBufferAllocatorName = "WASMGCBufferAllocator";

/**
 * @param {number} addr
 * @param {number} length
 * @return {Int8Array}
 */
eagruntimeImpl.WASMGCBufferAllocator["getByteBufferView"] = function(addr, length) {
	return new Int8Array(heapArrayBuffer, addr, length);
}

/**
 * @param {number} addr
 * @param {number} length
 * @return {Uint8Array}
 */
eagruntimeImpl.WASMGCBufferAllocator["getUnsignedByteBufferView"] = function(addr, length) {
	return new Uint8Array(heapArrayBuffer, addr, length);
}

/**
 * @param {number} addr
 * @param {number} length
 * @return {Uint8ClampedArray}
 */
eagruntimeImpl.WASMGCBufferAllocator["getUnsignedClampedByteBufferView"] = function(addr, length) {
	return new Uint8ClampedArray(heapArrayBuffer, addr, length);
}

/**
 * @param {number} addr
 * @param {number} length
 * @return {Int16Array}
 */
eagruntimeImpl.WASMGCBufferAllocator["getShortBufferView"] = function(addr, length) {
	return new Int16Array(heapArrayBuffer, addr, length);
}

/**
 * @param {number} addr
 * @param {number} length
 * @return {Uint16Array}
 */
eagruntimeImpl.WASMGCBufferAllocator["getUnsignedShortBufferView"] = function(addr, length) {
	return new Uint16Array(heapArrayBuffer, addr, length);
}

/**
 * @param {number} addr
 * @param {number} length
 * @return {Int32Array}
 */
eagruntimeImpl.WASMGCBufferAllocator["getIntBufferView"] = function(addr, length) {
	return new Int32Array(heapArrayBuffer, addr, length);
}

/**
 * @param {number} addr
 * @param {number} length
 * @return {Float32Array}
 */
eagruntimeImpl.WASMGCBufferAllocator["getFloatBufferView"] = function(addr, length) {
	return new Float32Array(heapArrayBuffer, addr, length);
}

/**
 * @param {function(Int8Array,Uint8Array,Int16Array,Uint16Array,Int32Array,Uint32Array,Float32Array)} cb
 */
eagruntimeImpl.WASMGCBufferAllocator["setHeapViewCallback"] = function(cb) {
	heapResizeHandler = cb;
	callHeapViewCallback();
}
