package net.lax1dude.eaglercraft.v1_8.futures;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

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
public class ListenableFutureTask<V> extends FutureTask<V> implements ListenableFuture<V> {
	
	private final List<Runnable> listeners = new ArrayList();

	public ListenableFutureTask(Callable<V> callable) {
		super(callable);
	}

	@Override
	public void addListener(final Runnable listener, final Executor executor) {
		listeners.add(new Runnable() {

			@Override
			public void run() {
				executor.execute(listener); // so dumb
			}
			
		});
	}
	
	protected void done() {
		for(Runnable r : listeners) {
			try {
				r.run();
			}catch(Throwable t) {
				ListenableFuture.futureExceptionLogger.error("Exception caught running future listener!");
				ListenableFuture.futureExceptionLogger.error(t);
			}
		}
		listeners.clear();
	}

	public static <V> ListenableFutureTask<V> create(Callable<V> callableToSchedule) {
		return new ListenableFutureTask(callableToSchedule);
	}
	
}
