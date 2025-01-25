package dev.redstudio.alfheim.utils;

import com.carrotsearch.hppc.LongArrayDeque;
import com.carrotsearch.hppc.LongHashSet;

/**
 * A queue implementation for long values that are deduplicated on addition.
 * <p>
 * This is achieved by storing the values in a {@link LongOpenHashSet} and a
 * {@link LongArrayFIFOQueue}.
 *
 * @author Luna Lage (Desoroxxx)
 * @since 1.3
 */
public final class DeduplicatedLongQueue {

	// TODO: Fully Implement my own implementation to get rid of the downsides of
	// reduce etc...

	private final LongArrayDeque queue;
	private LongHashSet set;

	/**
	 * Creates a new deduplicated queue with the given capacity.
	 *
	 * @param capacity The capacity of the deduplicated queue
	 */
	public DeduplicatedLongQueue(final int capacity) {
		set = new LongHashSet(capacity);
		queue = new LongArrayDeque(capacity);
	}

	/**
	 * Adds a value to the queue.
	 *
	 * @param value The value to add to the queue
	 */
	public void enqueue(final long value) {
		if (set.add(value))
			queue.addLast(value);
	}

	/**
	 * Removes and returns the first value in the queue.
	 *
	 * @return The first value in the queue
	 */
	public long dequeue() {
		return queue.removeFirst();
	}

	/**
	 * Returns whether the queue is empty.
	 *
	 * @return {@code true} if the queue is empty, {@code false} otherwise
	 */
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	/**
	 * Creates a new deduplication set.
	 */
	public void newDeduplicationSet() {
		int i = queue.size();
		if(i < 4) {
			i = 4;
		}
		if((set.keys.length * 3 / 2) > i) {
			set = new LongHashSet(i);
		}else {
			set.clear();
		}
	}
}
