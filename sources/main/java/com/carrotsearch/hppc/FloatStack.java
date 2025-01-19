package com.carrotsearch.hppc;

import com.carrotsearch.hppc.cursors.FloatCursor;

/**
 * A subclass of {@link FloatArrayList} adding stack-related utility methods. The top of the stack
 * is at the <code>{@link #size()} - 1</code> element.
 */
@com.carrotsearch.hppc.Generated(date = "2024-06-04T15:20:17+0200", value = "KTypeStack.java")
public class FloatStack extends FloatArrayList {
  /** New instance with sane defaults. */
  public FloatStack() {
    super();
  }

  /**
   * New instance with sane defaults.
   *
   * @param expectedElements The expected number of elements guaranteed not to cause buffer
   *     expansion (inclusive).
   */
  public FloatStack(int expectedElements) {
    super(expectedElements);
  }

  /**
   * New instance with sane defaults.
   *
   * @param expectedElements The expected number of elements guaranteed not to cause buffer
   *     expansion (inclusive).
   * @param resizer Underlying buffer sizing strategy.
   */
  public FloatStack(int expectedElements, ArraySizingStrategy resizer) {
    super(expectedElements, resizer);
  }

  /** Create a stack by pushing all elements of another container to it. */
  public FloatStack(FloatContainer container) {
    super(container);
  }

  /** Adds one float to the stack. */
  public void push(float e1) {
    ensureBufferSpace(1);
    buffer[elementsCount++] = e1;
  }

  /** Adds two floats to the stack. */
  public void push(float e1, float e2) {
    ensureBufferSpace(2);
    buffer[elementsCount++] = e1;
    buffer[elementsCount++] = e2;
  }

  /** Adds three floats to the stack. */
  public void push(float e1, float e2, float e3) {
    ensureBufferSpace(3);
    buffer[elementsCount++] = e1;
    buffer[elementsCount++] = e2;
    buffer[elementsCount++] = e3;
  }

  /** Adds four floats to the stack. */
  public void push(float e1, float e2, float e3, float e4) {
    ensureBufferSpace(4);
    buffer[elementsCount++] = e1;
    buffer[elementsCount++] = e2;
    buffer[elementsCount++] = e3;
    buffer[elementsCount++] = e4;
  }

  /** Add a range of array elements to the stack. */
  public void push(float[] elements, int start, int len) {
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
  public final void push(float... elements) {
    push(elements, 0, elements.length);
  }

  /** Pushes all elements from another container to the top of the stack. */
  public int pushAll(FloatContainer container) {
    return addAll(container);
  }

  /** Pushes all elements from another iterable to the top of the stack. */
  public int pushAll(Iterable<? extends FloatCursor> iterable) {
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
  public float pop() {
    return removeLast();
  }

  /** Peek at the top element on the stack. */
  public float peek() {
    assert elementsCount > 0;
    return buffer[elementsCount - 1];
  }

  /** Create a stack by pushing a variable number of arguments to it. */
  public static FloatStack from(float... elements) {
    final FloatStack stack = new FloatStack(elements.length);
    stack.push(elements);
    return stack;
  }

  /** {@inheritDoc} */
  @Override
  public FloatStack clone() {
    return (FloatStack) super.clone();
  }
}
