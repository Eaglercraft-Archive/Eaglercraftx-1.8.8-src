package net.lax1dude.eaglercraft.v1_8.futures;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

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
public class FutureTask<V> implements RunnableFuture<V> {

	private boolean cancelled;
	private boolean completed;
	private V result;
	private Callable<V> callable;
	
	public FutureTask(Callable<V> callable) {
		this.callable = callable;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		if(!cancelled) {
			cancelled = true;
			if(!completed) {
				done();
			}
		}
		return true;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public boolean isDone() {
		return cancelled || completed;
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		if(!completed) {
			if(!cancelled) {
				try {
					result = callable.call();
				}catch(Throwable t) {
					throw new ExecutionException(t);
				}finally {
					completed = true;
					done();
				}
			}
		}
		return result;
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException {
		return get();
	}

	@Override
	public void run() {
		try {
			get();
		} catch (ExecutionException t) {
			throw t;
		} catch (Throwable t) {
			throw new ExecutionException(t);
		}
	}
	
	protected void done() {
	}
	
}
