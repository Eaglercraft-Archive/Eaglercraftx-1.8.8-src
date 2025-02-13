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

package net.lax1dude.eaglercraft.v1_8.opengl;

import net.lax1dude.eaglercraft.v1_8.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

class SoftGLBufferArray implements IBufferArrayGL {

	Attrib[] attribs = new Attrib[4];
	int[] attribDivisors = null;
	int hasAttribDivisorMask = 0;
	int enabled = 0;
	int enabledCnt = -1;
	IBufferGL indexBuffer = null;

	SoftGLBufferArray() {
	}

	void setAttrib(IBufferGL buffer, int index, int size, int format, boolean normalized, int stride, int offset) {
		if(index >= attribs.length) {
			int newLen = attribs.length << 1;
			while(newLen <= index) {
				newLen <<= 1;
			}
			Attrib[] newAttrib = new Attrib[newLen];
			System.arraycopy(attribs, 0, newAttrib, 0, attribs.length);
			attribs = newAttrib;
		}
		attribs[index] = new Attrib(buffer, size, format, normalized, stride, offset);
	}

	void setAttribDivisor(int index, int divisor) {
		if(attribDivisors == null) {
			if(divisor != 0) {
				int newLen = 8;
				while(newLen <= index) {
					newLen <<= 1;
				}
				attribDivisors = new int[newLen];
			}
		}else if(index >= attribDivisors.length) {
			int newLen = attribDivisors.length << 1;
			while(newLen <= index) {
				newLen <<= 1;
			}
			int[] newDivisor = new int[newLen];
			System.arraycopy(attribDivisors, 0, newDivisor, 0, attribDivisors.length);
			attribDivisors = newDivisor;
		}
		if(attribDivisors != null) {
			attribDivisors[index] = divisor;
			if(divisor != 0) {
				hasAttribDivisorMask |= (1 << index);
			}else {
				hasAttribDivisorMask &= ~(1 << index);
			}
		}
	}

	void enableAttrib(int index, boolean en) {
		if(en) {
			enabled |= (1 << index);
		}else {
			enabled &= ~(1 << index);
		}
		enabledCnt = 32 - Integer.numberOfLeadingZeros(enabled);
	}

	void setIndexBuffer(IBufferGL buffer) {
		indexBuffer = buffer;
	}

	void transitionToState(SoftGLBufferState previousState, boolean elements) {
		int oldEnabled = previousState.oldEnabled;
		int oldEnabledCnt = previousState.oldEnabledCnt;
		int[] oldAttribDivisors = previousState.attribDivisors;
		int oldHasAttribDivisorMask = previousState.hasAttribDivisorMask;
		Attrib[] oldAttrs = previousState.attribs;
		boolean instancingCapable = EaglercraftGPU.checkInstancingCapable();
		int enCnt = enabledCnt;
		int en = enabled;
		Attrib[] attrs = attribs;
		int[] divs = attribDivisors;
		int hasDivs = hasAttribDivisorMask;
		if(oldEnabledCnt >= 0) {
			int enMax = Math.max(enCnt, oldEnabledCnt);
			for(int i = 0, ii; i < enMax; ++i) {
				ii = (1 << i);
				boolean old = i < oldEnabledCnt && (oldEnabled & ii) != 0;
				boolean _new = i < enCnt && (en & ii) != 0;
				if(_new) {
					if(!old) {
						_wglEnableVertexAttribArray(i);
					}
					Attrib attr = i < attrs.length ? attrs[i] : null;
					if(attr != null) {
						Attrib oldAttr = oldAttrs[i];
						if(oldAttr == null || !oldAttr.equalsExplicit(attr)) {
							EaglercraftGPU.bindGLArrayBuffer(attr.buffer);
							_wglVertexAttribPointer(i, attr.size, attr.format, attr.normalized, attr.stride, attr.offset);
							oldAttrs[i] = attr;
						}
					}else {
						oldAttrs[i] = null;
					}
					if(instancingCapable) {
						// instancing is uncommon
						if((hasDivs & ii) != 0) {
							int newDivisor = divs[i];
							if((oldHasAttribDivisorMask & ii) == 0 || newDivisor != oldAttribDivisors[i]) {
								_wglVertexAttribDivisor(i, newDivisor);
								oldAttribDivisors[i] = newDivisor;
								previousState.hasAttribDivisorMask |= ii;
							}
						}else {
							if((oldHasAttribDivisorMask & ii) != 0) {
								_wglVertexAttribDivisor(i, 0);
								oldAttribDivisors[i] = 0;
								previousState.hasAttribDivisorMask &= ~ii;
							}
						}
					}
				}else if(old) {
					_wglDisableVertexAttribArray(i);
				}
			}
		}else {
			// Bootstrap code for the emulator's first draw
			for(int i = 0; i < enCnt; ++i) {
				int ii = (1 << i);
				if((en & ii) != 0) {
					_wglEnableVertexAttribArray(i);
					Attrib attr = attrs[i];
					if(attr != null) {
						EaglercraftGPU.bindGLArrayBuffer(attr.buffer);
						_wglVertexAttribPointer(i, attr.size, attr.format, attr.normalized, attr.stride, attr.offset);
						oldAttrs[i] = attr;
					}else {
						oldAttrs[i] = null;
					}
					if(instancingCapable) {
						if((hasDivs & ii) != 0) {
							int newDivisor = divs[i];
							_wglVertexAttribDivisor(i, newDivisor);
							oldAttribDivisors[i] = newDivisor;
							previousState.hasAttribDivisorMask |= ii;
						}else {
							_wglVertexAttribDivisor(i, 0);
							oldAttribDivisors[i] = 0;
						}
					}
				}
			}
		}
		if(elements) {
			IBufferGL indexBufferL = indexBuffer;
			if(indexBufferL != null) {
				EaglercraftGPU.bindEmulatedVAOIndexBuffer(indexBufferL);
			}
		}
		previousState.oldEnabled = en & ((1 << enCnt) - 1);
		previousState.oldEnabledCnt = enCnt;
	}

	@Override
	public void free() {
	}

	static class Attrib {

		final IBufferGL buffer;
		final int size;
		final int format;
		final boolean normalized;
		final int stride;
		final int offset;
		final int hash;
		final int checkVal;

		Attrib(IBufferGL buffer, int size, int format, boolean normalized, int stride, int offset) {
			this.buffer = buffer;
			this.size = size;
			this.format = format;
			this.normalized = normalized;
			this.stride = stride;
			this.offset = offset;
			this.checkVal = ((size - 1) & 3) | (normalized ? 4 : 0) | (format << 4);
			this.hash = (((((31 + buffer.hashCode()) * 31 + size) * 31 + format) * 31 + (normalized ? 1 : 0)) * 31
					+ stride) * 31 + offset;
		}

		public int hashCode() {
			return hash;
		}

		public boolean equals(Object obj) {
			if(obj == this) return true;
			if(!(obj instanceof Attrib)) return false;
			Attrib o2 = (Attrib)obj;
			return o2.hash == hash && o2.buffer == buffer && o2.checkVal == checkVal && o2.stride == stride && o2.offset == offset;
		}

		public boolean equalsExplicit(Attrib o2) {
			return o2 == this || (o2.hash == hash && o2.buffer == buffer && o2.checkVal == checkVal && o2.stride == stride && o2.offset == offset);
		}

	}

}