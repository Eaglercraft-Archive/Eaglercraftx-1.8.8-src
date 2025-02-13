/*
 * Copyright (c) 2023 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import java.util.ArrayList;

public class ArrayListSerial<E> extends ArrayList<E> implements ListSerial<E> {

	protected int modCountEagler = 0;
	protected int mark = 0;

	public ArrayListSerial() {
		super();
	}

	public ArrayListSerial(int initialSize) {
		super(initialSize);
	}

	public E set(int index, E element) {
		++modCountEagler;
		return super.set(index, element);
	}

	public int getEaglerSerial() {
		return (modCount << 8) + modCountEagler;
	}

	public void eaglerIncrSerial() {
		++modCountEagler;
	}

	public void eaglerResetCheck() {
		mark = getEaglerSerial();
	}

	public boolean eaglerCheck() {
		return mark != getEaglerSerial();
	}

}