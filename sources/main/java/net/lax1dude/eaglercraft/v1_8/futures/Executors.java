package net.lax1dude.eaglercraft.v1_8.futures;

import java.util.concurrent.Callable;

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
public class Executors {

	public static <T> Callable<T> callable(Runnable task, T result) {
		if (task == null)
			throw new NullPointerException();
		return new RunnableAdapter<T>(task, result);
	}

	public static Callable<Object> callable(Runnable task) {
		if (task == null)
			throw new NullPointerException();
		return new RunnableAdapter<Object>(task, null);
	}

	static final class RunnableAdapter<T> implements Callable<T> {
		final Runnable task;
		final T result;

		RunnableAdapter(Runnable task, T result) {
			this.task = task;
			this.result = result;
		}

		public T call() {
			task.run();
			return result;
		}
	}

}
