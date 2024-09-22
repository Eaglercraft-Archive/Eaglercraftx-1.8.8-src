package net.lax1dude.eaglercraft.v1_8.internal;

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
public class GLObjectMap<T> {
	private Object[] values;
	private int size;
	private int insertIndex;
	public int allocatedObjects;
	
	public GLObjectMap(int initialSize) {
		this.values = new Object[initialSize];
		this.size = initialSize;
		this.insertIndex = 0;
		this.allocatedObjects = 0;
	}

	public int register(T obj) {
		int start = insertIndex;
		do {
			++insertIndex;
			if(insertIndex >= size) {
				insertIndex = 0;
			}
			if(insertIndex == start) {
				resize();
				return register(obj);
			}
		}while(values[insertIndex] != null);
		values[insertIndex] = obj;
		++allocatedObjects;
		return insertIndex + 1;
	}
	
	public T free(int obj) {
		--obj;
		if(obj >= size || obj < 0) return null;
		Object ret = values[obj];
		values[obj] = null;
		--allocatedObjects;
		return (T) ret;
	}
	
	public T get(int obj) {
		--obj;
		if(obj >= size || obj < 0) return null;
		return (T) values[obj];
	}
	
	public void set(int obj, T val) {
		values[obj] = val;
	}
	
	private void resize() {
		int oldSize = size;
		size += size / 2;
		Object[] oldValues = values;
		values = new Object[size];
		System.arraycopy(oldValues, 0, values, 0, oldSize);
	}

	public void clear() {
		if(allocatedObjects == 0) return;
		values = new Object[size];
		insertIndex = 0;
		allocatedObjects = 0;
	}
}
