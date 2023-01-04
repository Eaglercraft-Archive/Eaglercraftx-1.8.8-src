package net.lax1dude.eaglercraft.v1_8.futures;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info) 
 * 
 */
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
