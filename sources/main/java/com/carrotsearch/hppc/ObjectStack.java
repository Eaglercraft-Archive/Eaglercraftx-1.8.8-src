package com.carrotsearch.hppc;

import com.carrotsearch.hppc.cursors.ObjectCursor;
import java.util.Arrays;

/**
 * A subclass of {@link ObjectArrayList} adding stack-related utility methods. The top of the stack
 * is at the <code>{@link #size()} - 1</code> element.
 */
@SuppressWarnings("unchecked")
@com.carrotsearch.hppc.Generated(date = "2024-06-04T15:20:17+0200", value = "KTypeStack.java")
public class ObjectStack<KType> extends ObjectArrayList<KType> {
  /** New instance with sane defaults. */
  public ObjectStack() {
    super();
  }

  /**
   * New instance with sane defaults.
   *
   * @param expectedElements The expected number of elements guaranteed not to cause buffer
   *     expansion (inclusive).
   */
  public ObjectStack(int expectedElements) {
    super(expectedElements);
  }

  /**
   * New instance with sane defaults.
   *
   * @param expectedElements The expected number of elements guaranteed not to cause buffer
   *     expansion (inclusive).
   * @param resizer Underlying buffer sizing strategy.
   */
  public ObjectStack(int expectedElements, ArraySizingStrategy resizer) {
    super(expectedElements, resizer);
  }

  /** Create a stack by pushing all elements of another container to it. */
  public ObjectStack(ObjectContainer<KType> container) {
    super(container);
  }

  /** Adds one Object to the stack. */
  public void push(KType e1) {
    ensureBufferSpace(1);
    buffer[elementsCount++] = e1;
  }

  /** Adds two Objects to the stack. */
  public void push(KType e1, KType e2) {
    ensureBufferSpace(2);
    buffer[elementsCount++] = e1;
    buffer[elementsCount++] = e2;
  }

  /** Adds three Objects to the stack. */
  public void push(KType e1, KType e2, KType e3) {
    ensureBufferSpace(3);
    buffer[elementsCount++] = e1;
    buffer[elementsCount++] = e2;
    buffer[elementsCount++] = e3;
  }

  /** Adds four Objects to the stack. */
  public void push(KType e1, KType e2, KType e3, KType e4) {
    ensureBufferSpace(4);
    buffer[elementsCount++] = e1;
    buffer[elementsCount++] = e2;
    buffer[elementsCount++] = e3;
    buffer[elementsCount++] = e4;
  }

  /** Add a range of array elements to the stack. */
  public void push(KType[] elements, int start, int len) {
    assert start >= 0 && len >= 0;

    ensureBufferSpace(len);
    System.arraycopy(elements, start, buffer, elementsCount, len);
    elementsCount += len;
  }

  /**
   * Vararg-signature method for pushing elements at the top of the stack.
   *
   * <p><b>This method is handy, but costly if used in tight loops (anonymous array passing)</b>
   */
  @SafeVarargs
  public final void push(KType... elements) {
    push(elements, 0, elements.length);
  }

  /** Pushes all elements from another container to the top of the stack. */
  public int pushAll(ObjectContainer<? extends KType> container) {
    return addAll(container);
  }

  /** Pushes all elements from another iterable to the top of the stack. */
  public int pushAll(Iterable<? extends ObjectCursor<? extends KType>> iterable) {
    return addAll(iterable);
  }

  /** Discard an arbitrary number of elements from the top of the stack. */
  public void discard(int count) {
    assert elementsCount >= count;

    elementsCount -= count;

    Arrays.fill(buffer, elementsCount, elementsCount + count, null);
  }

  /** Discard the top element from the stack. */
  public void discard() {
    assert elementsCount > 0;

    elementsCount--;

    buffer[elementsCount] = null;
  }

  /** Remove the top element from the stack and return it. */
  public KType pop() {
    return removeLast();
  }

  /** Peek at the top element on the stack. */
  public KType peek() {
    assert elementsCount > 0;
    return (KType) buffer[elementsCount - 1];
  }

  /** Create a stack by pushing a variable number of arguments to it. */
  @SafeVarargs
  public static <KType> ObjectStack<KType> from(KType... elements) {
    final ObjectStack<KType> stack = new ObjectStack<KType>(elements.length);
    stack.push(elements);
    return stack;
  }

  /** {@inheritDoc} */
  @Override
  public ObjectStack<KType> clone() {
    return (ObjectStack<KType>) super.clone();
  }
}
