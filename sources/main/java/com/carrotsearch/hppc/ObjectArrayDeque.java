package com.carrotsearch.hppc;

import static com.carrotsearch.hppc.Containers.*;

import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.carrotsearch.hppc.predicates.ObjectPredicate;
import com.carrotsearch.hppc.procedures.ObjectProcedure;
import java.util.*;

/** An array-backed {@link ObjectDeque}. */
@SuppressWarnings("unchecked")
@com.carrotsearch.hppc.Generated(date = "2024-06-04T15:20:17+0200", value = "KTypeArrayDeque.java")
public class ObjectArrayDeque<KType> extends AbstractObjectCollection<KType>
    implements ObjectDeque<KType>, Preallocable, Cloneable, Accountable {

  /** Reuse the same strategy instance. */
  private static final BoundedProportionalArraySizingStrategy DEFAULT_SIZING_STRATEGY =
      BoundedProportionalArraySizingStrategy.DEFAULT_INSTANCE;

  /** Internal array for storing elements of the deque. */
  public Object[] buffer = ObjectArrayList.EMPTY_ARRAY;

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
  public ObjectArrayDeque() {
    this(DEFAULT_EXPECTED_ELEMENTS);
  }

  /**
   * New instance with sane defaults.
   *
   * @param expectedElements The expected number of elements guaranteed not to cause buffer
   *     expansion (inclusive).
   */
  public ObjectArrayDeque(int expectedElements) {
    this(expectedElements, DEFAULT_SIZING_STRATEGY);
  }

  /**
   * New instance with sane defaults.
   *
   * @param expectedElements The expected number of elements guaranteed not to cause buffer
   *     expansion (inclusive).
   * @param resizer Underlying buffer sizing strategy.
   */
  public ObjectArrayDeque(int expectedElements, ArraySizingStrategy resizer) {
    assert resizer != null;
    this.resizer = resizer;
    ensureCapacity(expectedElements);
  }

  /**
   * Creates a new deque from elements of another container, appending elements at the end of the
   * deque in the iteration order.
   */
  public ObjectArrayDeque(ObjectContainer<? extends KType> container) {
    this(container.size());
    addLast(container);
  }

  /** {@inheritDoc} */
  @Override
  public void addFirst(KType e1) {
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
  @SafeVarargs
  public final void addFirst(KType... elements) {
    ensureBufferSpace(elements.length);
    for (KType k : elements) {
      addFirst(k);
    }
  }

  /**
   * Inserts all elements from the given container to the front of this deque.
   *
   * @param container The container to iterate over.
   * @return Returns the number of elements actually added as a result of this call.
   */
  public int addFirst(ObjectContainer<? extends KType> container) {
    int size = container.size();
    ensureBufferSpace(size);

    for (ObjectCursor<? extends KType> cursor : container) {
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
  public int addFirst(Iterable<? extends ObjectCursor<? extends KType>> iterable) {
    int size = 0;
    for (ObjectCursor<? extends KType> cursor : iterable) {
      addFirst(cursor.value);
      size++;
    }
    return size;
  }

  /** {@inheritDoc} */
  @Override
  public void addLast(KType e1) {
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
  @SafeVarargs
  public final void addLast(KType... elements) {
    ensureBufferSpace(1);
    for (KType k : elements) {
      addLast(k);
    }
  }

  /**
   * Inserts all elements from the given container to the end of this deque.
   *
   * @param container The container to iterate over.
   * @return Returns the number of elements actually added as a result of this call.
   */
  public int addLast(ObjectContainer<? extends KType> container) {
    int size = container.size();
    ensureBufferSpace(size);

    for (ObjectCursor<? extends KType> cursor : container) {
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
  public int addLast(Iterable<? extends ObjectCursor<? extends KType>> iterable) {
    int size = 0;
    for (ObjectCursor<? extends KType> cursor : iterable) {
      addLast(cursor.value);
      size++;
    }
    return size;
  }

  /** {@inheritDoc} */
  @Override
  public KType removeFirst() {
    assert size() > 0 : "The deque is empty.";

    final KType result = (KType) buffer[head];
    buffer[head] = null;
    head = oneRight(head, buffer.length);
    return result;
  }

  /** {@inheritDoc} */
  @Override
  public KType removeLast() {
    assert size() > 0 : "The deque is empty.";

    tail = oneLeft(tail, buffer.length);
    final KType result = (KType) buffer[tail];
    buffer[tail] = null;
    return result;
  }

  /** {@inheritDoc} */
  @Override
  public KType getFirst() {
    assert size() > 0 : "The deque is empty.";

    return (KType) buffer[head];
  }

  /** {@inheritDoc} */
  @Override
  public KType getLast() {
    assert size() > 0 : "The deque is empty.";

    return (KType) buffer[oneLeft(tail, buffer.length)];
  }

  /** {@inheritDoc} */
  @Override
  public int removeFirst(KType e1) {
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
  public int bufferIndexOf(KType e1) {
    final int last = tail;
    final int bufLen = buffer.length;
    for (int i = head; i != last; i = oneRight(i, bufLen)) {
      if (this.equals(e1, buffer[i])) {
        return i;
      }
    }

    return -1;
  }

  /** {@inheritDoc} */
  @Override
  public int removeLast(KType e1) {
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
  public int lastBufferIndexOf(KType e1) {
    final int bufLen = buffer.length;
    final int last = oneLeft(head, bufLen);
    for (int i = oneLeft(tail, bufLen); i != last; i = oneLeft(i, bufLen)) {
      if (this.equals(e1, buffer[i])) return i;
    }

    return -1;
  }

  /** {@inheritDoc} */
  @Override
  public int removeAll(KType e1) {
    int removed = 0;
    final int last = tail;
    final int bufLen = buffer.length;
    int from, to;
    for (from = to = head; from != last; from = oneRight(from, bufLen)) {
      if (this.equals(e1, buffer[from])) {
        buffer[from] = null;
        removed++;
        continue;
      }

      if (to != from) {
        buffer[to] = buffer[from];
        buffer[from] = null;
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
    final KType[] buffer = (KType[]) this.buffer;
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
      buffer[head] = null;
      this.head = oneRight(head, bufLen);
    } else {
      if (index < tail) {
        System.arraycopy(buffer, index + 1, buffer, index, rightChunk);
      } else {
        System.arraycopy(buffer, index + 1, buffer, index, lastIndex - index);
        buffer[lastIndex] = buffer[0];
        System.arraycopy(buffer, 1, buffer, 0, tail);
      }
      buffer[tail] = null;
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
      Arrays.fill(buffer, head, tail, null);
    } else {
      Arrays.fill(buffer, 0, tail, null);
      Arrays.fill(buffer, head, buffer.length, null);
    }
    this.head = tail = 0;
  }

  /** Release internal buffers of this deque and reallocate with the default buffer. */
  public void release() {
    this.head = tail = 0;
    buffer = ObjectArrayList.EMPTY_ARRAY;
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
        final KType[] newBuffer = ((KType[]) new Object[newSize]);
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
  public Object[] toArray() {

    final int size = size();
    return toArray(((KType[]) new Object[size]));
  }

  /**
   * Copies elements of this deque to an array. The content of the <code>target</code> array is
   * filled from index 0 (head of the queue) to index <code>size() - 1</code> (tail of the queue).
   *
   * @param target The target array must be large enough to hold all elements.
   * @return Returns the target argument for chaining.
   */
  public KType[] toArray(KType[] target) {
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
  public ObjectArrayDeque<KType> clone() {
    try {

      ObjectArrayDeque<KType> cloned = (ObjectArrayDeque<KType>) super.clone();
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
  private final class ValueIterator extends AbstractIterator<ObjectCursor<KType>> {
    private final ObjectCursor<KType> cursor;
    private int remaining;

    public ValueIterator() {
      cursor = new ObjectCursor<KType>();
      cursor.index = oneLeft(head, buffer.length);
      this.remaining = size();
    }

    @Override
    protected ObjectCursor<KType> fetch() {
      if (remaining == 0) {
        return done();
      }

      remaining--;
      cursor.value = (KType) buffer[cursor.index = oneRight(cursor.index, buffer.length)];
      return cursor;
    }
  }

  /** An iterator implementation for {@link ObjectArrayDeque#descendingIterator()}. */
  private final class DescendingValueIterator extends AbstractIterator<ObjectCursor<KType>> {
    private final ObjectCursor<KType> cursor;
    private int remaining;

    public DescendingValueIterator() {
      cursor = new ObjectCursor<KType>();
      cursor.index = tail;
      this.remaining = size();
    }

    @Override
    protected ObjectCursor<KType> fetch() {
      if (remaining == 0) return done();

      remaining--;
      cursor.value = (KType) buffer[cursor.index = oneLeft(cursor.index, buffer.length)];
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
  public Iterator<ObjectCursor<KType>> iterator() {
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
  public Iterator<ObjectCursor<KType>> descendingIterator() {
    return new DescendingValueIterator();
  }

  /** {@inheritDoc} */
  @Override
  public <T extends ObjectProcedure<? super KType>> T forEach(T procedure) {
    forEach(procedure, head, tail);
    return procedure;
  }

  /**
   * Applies <code>procedure</code> to a slice of the deque, <code>fromIndex</code>, inclusive, to
   * <code>toIndex</code>, exclusive.
   */
  private void forEach(ObjectProcedure<? super KType> procedure, int fromIndex, final int toIndex) {
    final KType[] buffer = (KType[]) this.buffer;
    for (int i = fromIndex; i != toIndex; i = oneRight(i, buffer.length)) {
      procedure.apply(buffer[i]);
    }
  }

  /** {@inheritDoc} */
  @Override
  public <T extends ObjectPredicate<? super KType>> T forEach(T predicate) {
    int fromIndex = head;
    int toIndex = tail;

    final KType[] buffer = (KType[]) this.buffer;
    for (int i = fromIndex; i != toIndex; i = oneRight(i, buffer.length)) {
      if (!predicate.apply(buffer[i])) {
        break;
      }
    }

    return predicate;
  }

  /** Applies <code>procedure</code> to all elements of this deque, tail to head. */
  @Override
  public <T extends ObjectProcedure<? super KType>> T descendingForEach(T procedure) {
    descendingForEach(procedure, head, tail);
    return procedure;
  }

  /**
   * Applies <code>procedure</code> to a slice of the deque, <code>toIndex</code>, exclusive, down
   * to <code>fromIndex</code>, inclusive.
   */
  private void descendingForEach(
      ObjectProcedure<? super KType> procedure, int fromIndex, final int toIndex) {
    if (fromIndex == toIndex) return;

    final KType[] buffer = (KType[]) this.buffer;
    int i = toIndex;
    do {
      i = oneLeft(i, buffer.length);
      procedure.apply(buffer[i]);
    } while (i != fromIndex);
  }

  /** {@inheritDoc} */
  @Override
  public <T extends ObjectPredicate<? super KType>> T descendingForEach(T predicate) {
    descendingForEach(predicate, head, tail);
    return predicate;
  }

  /**
   * Applies <code>predicate</code> to a slice of the deque, <code>toIndex</code>, exclusive, down
   * to <code>fromIndex</code>, inclusive or until the predicate returns <code>false</code>.
   */
  private void descendingForEach(
      ObjectPredicate<? super KType> predicate, int fromIndex, final int toIndex) {
    if (fromIndex == toIndex) return;

    final KType[] buffer = (KType[]) this.buffer;
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
  public int removeAll(ObjectPredicate<? super KType> predicate) {
    final KType[] buffer = (KType[]) this.buffer;
    final int last = tail;
    final int bufLen = buffer.length;
    int removed = 0;
    int from, to;
    from = to = head;
    try {
      for (from = to = head; from != last; from = oneRight(from, bufLen)) {
        if (predicate.apply(buffer[from])) {
          buffer[from] = null;
          removed++;
          continue;
        }

        if (to != from) {
          buffer[to] = buffer[from];
          buffer[from] = null;
        }

        to = oneRight(to, bufLen);
      }
    } finally {
      // Keep the deque in consistent state even if the predicate throws an exception.
      for (; from != last; from = oneRight(from, bufLen)) {
        if (to != from) {
          buffer[to] = buffer[from];
          buffer[from] = null;
        }

        to = oneRight(to, bufLen);
      }
      tail = to;
    }

    return removed;
  }

  /** {@inheritDoc} */
  @Override
  public boolean contains(KType e) {
    int fromIndex = head;
    int toIndex = tail;

    final KType[] buffer = (KType[]) this.buffer;
    for (int i = fromIndex; i != toIndex; i = oneRight(i, buffer.length)) {
      if (this.equals(e, buffer[i])) {
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

    final KType[] buffer = (KType[]) this.buffer;
    for (int i = fromIndex; i != toIndex; i = oneRight(i, buffer.length)) {
      h = 31 * h + BitMixer.mix(this.buffer[i]);
    }
    return h;
  }

  /**
   * Returns <code>true</code> only if the other object is an instance of the same class and with
   * the same elements. Equality comparison is performed with this object's {@link #equals(Object,
   * Object)} method.
   */
  @Override
  public boolean equals(Object obj) {
    return (this == obj)
        || (obj != null && getClass() == obj.getClass() && equalElements(getClass().cast(obj)));
  }

  /**
   * Compare order-aligned elements against another {@link ObjectDeque}. Equality comparison is
   * performed with this object's {@link #equals(Object, Object)} method.
   */
  protected boolean equalElements(ObjectArrayDeque<?> other) {
    int max = size();
    if (other.size() != max) {
      return false;
    }

    Iterator<ObjectCursor<KType>> i1 = this.iterator();
    Iterator<? extends ObjectCursor<?>> i2 = other.iterator();

    while (i1.hasNext() && i2.hasNext()) {
      if (!this.equals(i1.next().value, i2.next().value)) {
        return false;
      }
    }

    return !i1.hasNext() && !i2.hasNext();
  }

  /** Create a new deque by pushing a variable number of arguments to the end of it. */
  @SafeVarargs
  public static <KType> ObjectArrayDeque<KType> from(KType... elements) {
    final ObjectArrayDeque<KType> coll = new ObjectArrayDeque<KType>(elements.length);
    coll.addLast(elements);
    return coll;
  }
}
