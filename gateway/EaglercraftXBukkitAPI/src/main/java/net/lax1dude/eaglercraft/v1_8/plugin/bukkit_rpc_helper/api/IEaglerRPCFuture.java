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

package net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api;

import java.util.concurrent.Executor;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.impl.SameThreadExecutor;

public interface IEaglerRPCFuture<V> extends ListenableFuture<V> {

	/**
	 * Warning: Futures.addCallback is recommended!
	 */
	default void addListener(Runnable runnable) {
		addListener(runnable, SameThreadExecutor.SAME_THREAD_EXECUTOR);
	}

	default void addCallback(FutureCallback<V> runnable, Executor executor) {
		Futures.addCallback(this, runnable, executor);
	}

	default void addCallback(FutureCallback<V> runnable) {
		Futures.addCallback(this, runnable, SameThreadExecutor.SAME_THREAD_EXECUTOR);
	}

	void setExpiresMSFromNow(int millis);

	boolean hasExpired();

}