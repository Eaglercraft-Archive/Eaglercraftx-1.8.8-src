package com.carrotsearch.hppc;

import static com.carrotsearch.hppc.Containers.*;

import com.carrotsearch.hppc.cursors.IntCursor;
import com.carrotsearch.hppc.predicates.IntPredicate;
import com.carrotsearch.hppc.procedures.IntProcedure;
import java.util.*;

/** An array-backed {@link IntDeque}. */
@com.carrotsearch.hppc.Generated(date = "2024-06-04T15:20:17+0200", value = "KTypeArrayDeque.java")
public class IntArrayDeque extends AbstractIntCollection
    implements IntDeque, Preallocable, Cloneable, Accountable {

  /** Reuse the same strategy instance. */
  private static final BoundedProportionalArraySizingStrategy DEFAULT_SIZING_STRATEGY =
      BoundedProportionalArraySizingStrategy.DEFAULT_INSTANCE;

  /** Internal array for storing elements of the deque. */
  public int[] buffer = IntArrayList.EMPTY_ARRAY;

  /**
   * The index of the element at the head of the deque or an arbitrary number equal to tail if the
   * deque is empty.
   */
  public int head;

  /** The index at which the next element would be added to the tail of the deque. */
  public int tail;

  /** Buffer resizing strategy. */
  protected final ArraySizingStrategy resizer;

  /** New instance with sane defaults. */
  public IntArrayDeque() {
    this(DEFAULT_EXPECTED_ELEMENTS);
  }

  /**
   * New instance with sane defaults.
   *
   * @param expectedElements The expected number of elements guaranteed not to cause buffer
   *     expansion (inclusive).
   */
  public IntArrayDeque(int expectedElements) {
    this(expectedElements, DEFAULT_SIZING_STRATEGY);
  }

  /**
   * New instance with sane defaults.
   *
   * @param expectedElements The expected number of elements guaranteed not to cause buffer
   *     expansion (inclusive).
   * @param resizer Underlying buffer sizing strategy.
   */
  public IntArrayDeque(int expectedElements, ArraySizingStrategy resizer) {
    assert resizer != null;
    this.resizer = resizer;
    ensureCapacity(expectedElements);
  }

  /**
   * Creates a new deque from elements of another container, appending elements at the end of the
   * deque in the iteration order.
   */
  public IntArrayDeque(IntContainer container) {
    this(container.size());
    addLast(container);
  }

  /** {@inheritDoc} */
  @Override
  public void addFirst(int e1) {
    int h = oneLeft(head, buffer.length);
    if (h == tail) {
      ensureBufferSpace(1);
      h = oneLeft(head, buffer.length);
    }
    buffer[head = h] = e1;
  }

  /**
   * Vararg-signature method for adding elements at the front of this deque.
   *
   * <p><b>This method is handy, but costly if used in tight loops (anonymous array passing)</b>
   *
   * @param elements The elements to add.
   */
  public final void addFirst(int... elements) {
    ensureBufferSpace(elements.length);
    for (int k : elements) {
      addFirst(k);
    }
  }

  /**
   * Inserts all elements from the given container to the front of this deque.
   *
   * @param container The container to iterate over.
   * @return Returns the number of elements actually added as a result of this call.
   */
  public int addFirst(IntContainer container) {
    int size = container.size();
    ensureBufferSpace(size);

    for (IntCursor cursor : container) {
      addFirst(cursor.value);
    }

    return size;
  }

  /**
   * Inserts all elements from the given iterable to the front of this deque.
   *
   * @param iterable The iterable to iterate over.
   * @return Returns the number of elements actually added as a result of this call.
   */
  public int addFirst(Iterable<? extends IntCursor> iterable) {
    int size = 0;
    for (IntCursor cursor : iterable) {
      addFirst(cursor.value);
      size++;
    }
    return size;
  }

  /** {@inheritDoc} */
  @Override
  public void addLast(int e1) {
    int t = oneRight(tail, buffer.length);
    if (head == t) {
      ensureBufferSpace(1);
      t = oneRight(tail, buffer.length);
    }
    buffer[tail] = e1;
    tail = t;
  }

  /**
   * Vararg-signature method for adding elements at the end of this deque.
   *
   * <p><b>This method is handy, but costly if used in tight loops (anonymous array passing)</b>
   *
   * @param elements The elements to iterate over.
   */
  public final void addLast(int... elements) {
    ensureBufferSpace(1);
    for (int k : elements) {
      addLast(k);
    }
  }

  /**
   * Inserts all elements from the given container to the end of this deque.
   *
   * @param container The container to iterate over.
   * @return Returns the number of elements actually added as a result of this call.
   */
  public int addLast(IntContainer container) {
    int size = container.size();
    ensureBufferSpace(size);

    for (IntCursor cursor : container) {
      addLast(cursor.value);
    }

    return size;
  }

  /**
   * Inserts all elements from the given iterable to the end of this deque.
   *
   * @param iterable The iterable to iterate over.
   * @return Returns the number of elements actually added as a result of this call.
   */
  public int addLast(Iterable<? extends IntCursor> iterable) {
    int size = 0;
    for (IntCursor cursor : iterable) {
      addLast(cursor.value);
      size++;
    }
    return size;
  }

  /** {@inheritDoc} */
  @Override
  public int removeFirst() {
    assert size() > 0 : "The deque is empty.";

    final int result = buffer[head];
    buffer[head] = 0;
    head = oneRight(head, buffer.length);
    return result;
  }

  /** {@inheritDoc} */
  @Override
  public int removeLast() {
    assert size() > 0 : "The deque is empty.";

    tail = oneLeft(tail, buffer.length);
    final int result = buffer[tail];
    buffer[tail] = 0;
    return result;
  }

  /** {@inheritDoc} */
  @Override
  public int getFirst() {
    assert size() > 0 : "The deque is empty.";

    return buffer[head];
  }

  /** {@inheritDoc} */
  @Override
  public int getLast() {
    assert size() > 0 : "The deque is empty.";

    return buffer[oneLeft(tail, buffer.length)];
  }

  /** {@inheritDoc} */
  @Override
  public int removeFirst(int e1) {
    final int index = bufferIndexOf(e1);
    if (index >= 0) removeAtBufferIndex(index);
    return index;
  }

  /**
   * Return the index of the first (counting from head) element equal to <code>e1</code>. The index
   * points to the {@link #buffer} array.
   *
   * @param e1 The element to look for.
   * @return Returns the index of the first element equal to <code>e1</code> or <code>-1</code> if
   *     not found.
   */
  public int bufferIndexOf(int e1) {
    final int last = tail;
    final int bufLen = buffer.length;
    for (int i = head; i != last; i = oneRight(i, bufLen)) {
      if (((e1) == (buffer[i]))) {
        return i;
      }
    }

    return -1;
  }

  /** {@inheritDoc} */
  @Override
  public int removeLast(int e1) {
    final int index = lastBufferIndexOf(e1);
    if (index >= 0) {
      removeAtBufferIndex(index);
    }
    return index;
  }

  /**
   * Return the index of the last (counting from tail) element equal to <code>e1</code>. The index
   * points to the {@link #buffer} array.
   *
   * @param e1 The element to look for.
   * @return Returns the index of the first element equal to <code>e1</code> or <code>-1</code> if
   *     not found.
   */
  public int lastBufferIndexOf(int e1) {
    final int bufLen = buffer.length;
    final int last = oneLeft(head, bufLen);
    for (int i = oneLeft(tail, bufLen); i != last; i = oneLeft(i, bufLen)) {
      if (((e1) == (buffer[i]))) return i;
    }

    return -1;
  }

  /** {@inheritDoc} */
  @Override
  public int removeAll(int e1) {
    int removed = 0;
    final int last = tail;
    final int bufLen = buffer.length;
    int from, to;
    for (from = to = head; from != last; from = oneRight(from, bufLen)) {
      if (((e1) == (buffer[from]))) {
        buffer[from] = 0;
        removed++;
        continue;
      }

      if (to != from) {
        buffer[to] = buffer[from];
        buffer[from] = 0;
      }

      to = oneRight(to, bufLen);
    }

    tail = to;
    return removed;
  }

  /**
   * Removes the element at <code>index</code> in the internal {#link {@link #buffer} array,
   * returning its value.
   *
   * @param index Index of the element to remove. The index must be located between {@link #head}
   *     and {@link #tail} in modulo {@link #buffer} arithmetic.
   */
  public void removeAtBufferIndex(int index) {
    assert (head <= tail ? index >= head && index < tail : index >= head || index < tail)
        : "Index out of range (head=" + head + ", tail=" + tail + ", index=" + index + ").";

    // Cache fields in locals (hopefully moved to registers).
    final int[] buffer = this.buffer;
    final int bufLen = buffer.length;
    final int lastIndex = bufLen - 1;
    final int head = this.head;
    final int tail = this.tail;

    final int leftChunk = Math.abs(index - head) % bufLen;
    final int rightChunk = Math.abs(tail - index) % bufLen;

    if (leftChunk < rightChunk) {
      if (index >= head) {
        System.arraycopy(buffer, head, buffer, head + 1, leftChunk);
      } else {
        System.arraycopy(buffer, 0, buffer, 1, index);
        buffer[0] = buffer[lastIndex];
        System.arraycopy(buffer, head, buffer, head + 1, lastIndex - head);
      }
      buffer[head] = 0;
      this.head = oneRight(head, bufLen);
    } else {
      if (index < tail) {
        System.arraycopy(buffer, index + 1, buffer, index, rightChunk);
      } else {
        System.arraycopy(buffer, index + 1, buffer, index, lastIndex - index);
        buffer[lastIndex] = buffer[0];
        System.arraycopy(buffer, 1, buffer, 0, tail);
      }
      buffer[tail] = 0;
      this.tail = oneLeft(tail, bufLen);
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  /** {@inheritDoc} */
  @Override
  public int size() {
    if (head <= tail) return tail - head;
    else return (tail - head + buffer.length);
  }

  /**
   * {@inheritDoc}
   *
   * <p>The internal array buffers are not released as a result of this call.
   *
   * @see #release()
   */
  @Override
  public void clear() {
    if (head < tail) {
      Arrays.fill(buffer, head, tail, 0);
    } else {
      Arrays.fill(buffer, 0, tail, 0);
      Arrays.fill(buffer, head, buffer.length, 0);
    }
    this.head = tail = 0;
  }

  /** Release internal buffers of this deque and reallocate with the default buffer. */
  public void release() {
    this.head = tail = 0;
    buffer = IntArrayList.EMPTY_ARRAY;
    ensureBufferSpace(0);
  }

  /**
   * Ensure this container can hold at least the given number of elements without resizing its
   * buffers.
   *
   * @param expectedElements The total number of elements, inclusive.
   */
  @Override
  public void ensureCapacity(int expectedElements) {
    ensureBufferSpace(expectedElements - size());
  }

  /**
   * Ensures the internal buffer has enough free slots to store <code>expectedAdditions</code>.
   * Increases internal buffer size if needed.
   */
  protected void ensureBufferSpace(int expectedAdditions) {
    final int bufferLen = buffer.length;
    final int elementsCount = size();

    if (elementsCount + expectedAdditions >= bufferLen) {
      final int emptySlot = 1; // deque invariant: always an empty slot.
      final int newSize = resizer.grow(bufferLen, elementsCount + emptySlot, expectedAdditions);
      assert newSize >= (elementsCount + expectedAdditions + emptySlot)
          : "Resizer failed to"
              + " return sensible new size: "
              + newSize
              + " <= "
              + (elementsCount + expectedAdditions);

      try {
        final int[] newBuffer = (new int[newSize]);
        if (bufferLen > 0) {
          toArray(newBuffer);
          tail = elementsCount;
          head = 0;
        }
        this.buffer = newBuffer;
      } catch (OutOfMemoryError e) {
        throw new BufferAllocationException(
            "Not enough memory to allocate new buffers: %,d -> %,d", e, bufferLen, newSize);
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public int[] toArray() {

    final int size = size();
    return toArray((new int[size]));
  }

  /**
   * Copies elements of this deque to an array. The content of the <code>target</code> array is
   * filled from index 0 (head of the queue) to index <code>size() - 1</code> (tail of the queue).
   *
   * @param target The target array must be large enough to hold all elements.
   * @return Returns the target argument for chaining.
   */
  public int[] toArray(int[] target) {
    assert target.length >= size() : "Target array must be >= " + size();

    if (head < tail) {
      // The contents is not wrapped around. Just copy.
      System.arraycopy(buffer, head, target, 0, size());
    } else if (head > tail) {
      // The contents is split. Merge elements from the following indexes:
      // [head...buffer.length - 1][0, tail - 1]
      final int rightCount = buffer.length - head;
      System.arraycopy(buffer, head, target, 0, rightCount);
      System.arraycopy(buffer, 0, target, rightCount, tail);
    }

    return target;
  }

  /**
   * Clone this object. The returned clone will reuse the same hash function and array resizing
   * strategy.
   */
  @Override
  public IntArrayDeque clone() {
    try {

      IntArrayDeque cloned = (IntArrayDeque) super.clone();
      cloned.buffer = buffer.clone();
      return cloned;
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  /** Move one index to the left, wrapping around buffer. */
  protected static int oneLeft(int index, int modulus) {
    if (index >= 1) {
      return index - 1;
    }
    return modulus - 1;
  }

  /** Move one index to the right, wrapping around buffer. */
  protected static int oneRight(int index, int modulus) {
    if (index + 1 == modulus) {
      return 0;
    }
    return index + 1;
  }

  @Override
  public long ramBytesAllocated() {
    // int: head, tail
    return RamUsageEstimator.NUM_BYTES_OBJECT_HEADER
        + Integer.BYTES * 2
        + resizer.ramBytesAllocated()
        + RamUsageEstimator.shallowSizeOfArray(buffer);
  }

  @Override
  public long ramBytesUsed() {
    // int: head, tail
    return RamUsageEstimator.NUM_BYTES_OBJECT_HEADER
        + Integer.BYTES * 2
        + resizer.ramBytesUsed()
        + RamUsageEstimator.shallowUsedSizeOfArray(buffer, size());
  }

  /** An iterator implementation for {@link ObjectArrayDeque#iterator}. */
  private final class ValueIterator extends AbstractIterator<IntCursor> {
    private final IntCursor cursor;
    private int remaining;

    public ValueIterator() {
      cursor = new IntCursor();
      cursor.index = oneLeft(head, buffer.length);
      this.remaining = size();
    }

    @Override
    protected IntCursor fetch() {
      if (remaining == 0) {
        return done();
      }

      remaining--;
      cursor.value = buffer[cursor.index = oneRight(cursor.index, buffer.length)];
      return cursor;
    }
  }

  /** An iterator implementation for {@link ObjectArrayDeque#descendingIterator()}. */
  private final class DescendingValueIterator extends AbstractIterator<IntCursor> {
    private final IntCursor cursor;
    private int remaining;

    public DescendingValueIterator() {
      cursor = new IntCursor();
      cursor.index = tail;
      this.remaining = size();
    }

    @Override
    protected IntCursor fetch() {
      if (remaining == 0) return done();

      remaining--;
      cursor.value = buffer[cursor.index = oneLeft(cursor.index, buffer.length)];
      return cursor;
    }
  }

  /**
   * Returns a cursor over the values of this deque (in head to tail order). The iterator is
   * implemented as a cursor and it returns <b>the same cursor instance</b> on every call to {@link
   * Iterator#next()} (to avoid boxing of primitive types). To read the current value (or index in
   * the deque's buffer) use the cursor's public fields. An example is shown below.
   *
   * <pre>
   * for (IntValueCursor c : intDeque) {
   *   System.out.println(&quot;buffer index=&quot; + c.index + &quot; value=&quot; + c.value);
   * }
   * </pre>
   */
  public Iterator<IntCursor> iterator() {
    return new ValueIterator();
  }

  /**
   * Returns a cursor over the values of this deque (in tail to head order). The iterator is
   * implemented as a cursor and it returns <b>the same cursor instance</b> on every call to {@link
   * Iterator#next()} (to avoid boxing of primitive types). To read the current value (or index in
   * the deque's buffer) use the cursor's public fields. An example is shown below.
   *
   * <pre>
   * for (Iterator&lt;IntCursor&gt; i = intDeque.descendingIterator(); i.hasNext();) {
   *   final IntCursor c = i.next();
   *   System.out.println(&quot;buffer index=&quot; + c.index + &quot; value=&quot; + c.value);
   * }
   * </pre>
   */
  public Iterator<IntCursor> descendingIterator() {
    return new DescendingValueIterator();
  }

  /** {@inheritDoc} */
  @Override
  public <T extends IntProcedure> T forEach(T procedure) {
    forEach(procedure, head, tail);
    return procedure;
  }

  /**
   * Applies <code>procedure</code> to a slice of the deque, <code>fromIndex</code>, inclusive, to
   * <code>toIndex</code>, exclusive.
   */
  private void forEach(IntProcedure procedure, int fromIndex, final int toIndex) {
    final int[] buffer = this.buffer;
    for (int i = fromIndex; i != toIndex; i = oneRight(i, buffer.length)) {
      procedure.apply(buffer[i]);
    }
  }

  /** {@inheritDoc} */
  @Override
  public <T extends IntPredicate> T forEach(T predicate) {
    int fromIndex = head;
    int toIndex = tail;

    final int[] buffer = this.buffer;
    for (int i = fromIndex; i != toIndex; i = oneRight(i, buffer.length)) {
      if (!predicate.apply(buffer[i])) {
        break;
      }
    }

    return predicate;
  }

  /** Applies <code>procedure</code> to all elements of this deque, tail to head. */
  @Override
  public <T extends IntProcedure> T descendingForEach(T procedure) {
    descendingForEach(procedure, head, tail);
    return procedure;
  }

  /**
   * Applies <code>procedure</code> to a slice of the deque, <code>toIndex</code>, exclusive, down
   * to <code>fromIndex</code>, inclusive.
   */
  private void descendingForEach(IntProcedure procedure, int fromIndex, final int toIndex) {
    if (fromIndex == toIndex) return;

    final int[] buffer = this.buffer;
    int i = toIndex;
    do {
      i = oneLeft(i, buffer.length);
      procedure.apply(buffer[i]);
    } while (i != fromIndex);
  }

  /** {@inheritDoc} */
  @Override
  public <T extends IntPredicate> T descendingForEach(T predicate) {
    descendingForEach(predicate, head, tail);
    return predicate;
  }

  /**
   * Applies <code>predicate</code> to a slice of the deque, <code>toIndex</code>, exclusive, down
   * to <code>fromIndex</code>, inclusive or until the predicate returns <code>false</code>.
   */
  private void descendingForEach(IntPredicate predicate, int fromIndex, final int toIndex) {
    if (fromIndex == toIndex) return;

    final int[] buffer = this.buffer;
    int i = toIndex;
    do {
      i = oneLeft(i, buffer.length);
      if (!predicate.apply(buffer[i])) {
        break;
      }
    } while (i != fromIndex);
  }

  /** {@inheritDoc} */
  @Override
  public int removeAll(IntPredicate predicate) {
    final int[] buffer = this.buffer;
    final int last = tail;
    final int bufLen = buffer.length;
    int removed = 0;
    int from, to;
    from = to = head;
    try {
      for (from = to = head; from != last; from = oneRight(from, bufLen)) {
        if (predicate.apply(buffer[from])) {
          buffer[from] = 0;
          removed++;
          continue;
        }

        if (to != from) {
          buffer[to] = buffer[from];
          buffer[from] = 0;
        }

        to = oneRight(to, bufLen);
      }
    } finally {
      // Keep the deque in consistent state even if the predicate throws an exception.
      for (; from != last; from = oneRight(from, bufLen)) {
        if (to != from) {
          buffer[to] = buffer[from];
          buffer[from] = 0;
        }

        to = oneRight(to, bufLen);
      }
      tail = to;
    }

    return removed;
  }

  /** {@inheritDoc} */
  @Override
  public boolean contains(int e) {
    int fromIndex = head;
    int toIndex = tail;

    final int[] buffer = this.buffer;
    for (int i = fromIndex; i != toIndex; i = oneRight(i, buffer.length)) {
      if (((e) == (buffer[i]))) {
        return true;
      }
    }

    return false;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    int h = 1;
    int fromIndex = head;
    int toIndex = tail;

    final int[] buffer = this.buffer;
    for (int i = fromIndex; i != toIndex; i = oneRight(i, buffer.length)) {
      h = 31 * h + BitMixer.mix(this.buffer[i]);
    }
    return h;
  }

  /**
   * Returns <code>true</code> only if the other object is an instance of the same class and with
   * the same elements.
   */
  @Override
  public boolean equals(Object obj) {
    return (this == obj)
        || (obj != null && getClass() == obj.getClass() && equalElements(getClass().cast(obj)));
  }

  /** Compare order-aligned elements against another {@link IntDeque}. */
  protected boolean equalElements(IntArrayDeque other) {
    int max = size();
    if (other.size() != max) {
      return false;
    }

    Iterator<IntCursor> i1 = this.iterator();
    Iterator<? extends IntCursor> i2 = other.iterator();

    while (i1.hasNext() && i2.hasNext()) {
      if (!((i1.next().value) == (i2.next().value))) {
        return false;
      }
    }

    return !i1.hasNext() && !i2.hasNext();
  }

  /** Create a new deque by pushing a variable number of arguments to the end of it. */
  public static IntArrayDeque from(int... elements) {
    final IntArrayDeque coll = new IntArrayDeque(elements.length);
    coll.addLast(elements);
    return coll;
  }
}
