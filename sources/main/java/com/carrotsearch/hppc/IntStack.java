package com.carrotsearch.hppc;

import com.carrotsearch.hppc.cursors.IntCursor;

/**
 * A subclass of {@link IntArrayList} adding stack-related utility methods. The top of the stack is
 * at the <code>{@link #size()} - 1</code> element.
 */
@com.carrotsearch.hppc.Generated(date = "2024-06-04T15:20:17+0200", value = "KTypeStack.java")
public class IntStack extends IntArrayList {
  /** New instance with sane defaults. */
  public IntStack() {
    super();
  }

  /**
   * New instance with sane defaults.
   *
   * @param expectedElements The expected number of elements guaranteed not to cause buffer
   *     expansion (inclusive).
   */
  public IntStack(int expectedElements) {
    super(expectedElements);
  }

  /**
   * New instance with sane defaults.
   *
   * @param expectedElements The expected number of elements guaranteed not to cause buffer
   *     expansion (inclusive).
   * @param resizer Underlying buffer sizing strategy.
   */
  public IntStack(int expectedElements, ArraySizingStrategy resizer) {
    super(expectedElements, resizer);
  }

  /** Create a stack by pushing all elements of another container to it. */
  public IntStack(IntContainer container) {
    super(container);
  }

  /** Adds one int to the stack. */
  public void push(int e1) {
    ensureBufferSpace(1);
    buffer[elementsCount++] = e1;
  }

  /** Adds two ints to the stack. */
  public void push(int e1, int e2) {
    ensureBufferSpace(2);
    buffer[elementsCount++] = e1;
    buffer[elementsCount++] = e2;
  }

  /** Adds three ints to the stack. */
  public void push(int e1, int e2, int e3) {
    ensureBufferSpace(3);
    buffer[elementsCount++] = e1;
    buffer[elementsCount++] = e2;
    buffer[elementsCount++] = e3;
  }

  /** Adds four ints to the stack. */
  public void push(int e1, int e2, int e3, int e4) {
    ensureBufferSpace(4);
    buffer[elementsCount++] = e1;
    buffer[elementsCount++] = e2;
    buffer[elementsCount++] = e3;
    buffer[elementsCount++] = e4;
  }

  /** Add a range of array elements to the stack. */
  public void push(int[] elements, int start, int len) {
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
  public final void push(int... elements) {
    push(elements, 0, elements.length);
  }

  /** Pushes all elements from another container to the top of the stack. */
  public int pushAll(IntContainer container) {
    return addAll(container);
  }

  /** Pushes all elements from another iterable to the top of the stack. */
  public int pushAll(Iterable<? extends IntCursor> iterable) {
    return addAll(iterable);
  }

  /** Discard an arbitrary number of elements from the top of the stack. */
  public void discard(int count) {
    assert elementsCount >= count;

    elementsCount -= count;
  }

  /** Discard the top element from the stack. */
  public void discard() {
    assert elementsCount > 0;

    elementsCount--;
  }

  /** Remove the top element from the stack and return it. */
  public int pop() {
    return removeLast();
  }

  /** Peek at the top element on the stack. */
  public int peek() {
    assert elementsCount > 0;
    return buffer[elementsCount - 1];
  }

  /** Create a stack by pushing a variable number of arguments to it. */
  public static IntStack from(int... elements) {
    final IntStack stack = new IntStack(elements.length);
    stack.push(elements);
    return stack;
  }

  /** {@inheritDoc} */
  @Override
  public IntStack clone() {
    return (IntStack) super.clone();
  }
}
