/*
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.futures;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

public class Futures {

	private abstract static class ImmediateFuture<V> implements ListenableFuture<V> {

		private static final Logger log = LogManager.getLogger(ImmediateFuture.class.getName());

		@Override
		public void addListener(Runnable listener, Executor executor) {
			checkNotNull(listener, "Runnable was null.");
			checkNotNull(executor, "Executor was null.");
			try {
				executor.execute(listener);
			} catch (RuntimeException e) {
				log.error("RuntimeException while executing runnable " + listener + " with executor " + executor, e);
			}
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return false;
		}

		@Override
		public abstract V get() throws ExecutionException;

		@Override
		public V get(long timeout, TimeUnit unit) throws ExecutionException {
			checkNotNull(unit);
			return get();
		}

		@Override
		public boolean isCancelled() {
			return false;
		}

		@Override
		public boolean isDone() {
			return true;
		}
	}

	private static class ImmediateSuccessfulFuture<V> extends ImmediateFuture<V> {

		@Nullable
		private final V value;

		ImmediateSuccessfulFuture(@Nullable V value) {
			this.value = value;
		}

		@Override
		public V get() {
			return value;
		}
	}

	private static class ImmediateFailedFuture<V> extends ImmediateFuture<V> {

		private final Throwable thrown;

		ImmediateFailedFuture(Throwable thrown) {
			this.thrown = thrown;
		}

		@Override
		public V get() throws ExecutionException {
			throw new ExecutionException(thrown);
		}
	}

	private static class ImmediateCancelledFuture<V> extends ImmediateFuture<V> {

		private final CancellationException thrown;

		ImmediateCancelledFuture() {
			this.thrown = new CancellationException("Immediate cancelled future.");
		}

		@Override
		public boolean isCancelled() {
			return true;
		}

		@Override
		public V get() {
			throw new CancellationException("Task was cancelled.", thrown);
		}
	}

	public static <V> ListenableFuture<V> immediateFuture(@Nullable V value) {
		return new ImmediateSuccessfulFuture<V>(value);
	}

	public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
		checkNotNull(throwable);
		return new ImmediateFailedFuture<V>(throwable);
	}

	public static <V> ListenableFuture<V> immediateCancelledFuture() {
		return new ImmediateCancelledFuture<V>();
	}

}