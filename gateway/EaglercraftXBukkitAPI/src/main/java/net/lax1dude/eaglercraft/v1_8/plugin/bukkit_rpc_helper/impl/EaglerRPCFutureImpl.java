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

package net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.impl;

import com.google.common.util.concurrent.AbstractFuture;

import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.IEaglerRPCFuture;

public class EaglerRPCFutureImpl<V> extends AbstractFuture<V> implements IEaglerRPCFuture<V> {

	private volatile long timeStart = -1l;
	private volatile int timeoutAfter = -1;

	@Override
	public void setExpiresMSFromNow(int millis) {
		if(millis > 0) {
			timeStart = System.nanoTime() / 1000000l;
			timeoutAfter = millis;
		}else {
			timeStart = -1l;
			timeoutAfter = -1;
		}
	}

	@Override
	public boolean hasExpired() {
		return (timeStart > 0l && timeoutAfter > 0) ? ((System.nanoTime() / 1000000l) - timeStart) > timeoutAfter : false;
	}

	public boolean hasExpiredBetter(long now) {
		return (timeStart > 0l && timeoutAfter > 0) ? (now - timeStart) > timeoutAfter : false;
	}

	public void fireCompleteInternal(V value) {
		this.set(value);
	}

	public void fireExceptionInternal(Throwable value) {
		this.setException(value);
	}

}