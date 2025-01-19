package com.carrotsearch.hppc;

import com.carrotsearch.hppc.cursors.IntCursor;
import com.carrotsearch.hppc.predicates.IntPredicate;
import java.util.Arrays;

/** Common superclass for collections. */
@com.carrotsearch.hppc.Generated(
    date = "2024-06-04T15:20:17+0200",
    value = "AbstractKTypeCollection.java")
abstract class AbstractIntCollection implements IntCollection {
  /** Default implementation uses a predicate for removal. */
  @Override
  public int removeAll(final IntLookupContainer c) {
    return this.removeAll(c::contains);
  }

  /** Default implementation uses a predicate for retaining. */
  @Override
  public int retainAll(final IntLookupContainer c) {
    // We know c holds sub-types of int and we're not modifying c, so go unchecked.
    return this.removeAll(k -> !c.contains(k));
  }

  /**
   * Default implementation redirects to {@link #removeAll(IntPredicate)} and negates the predicate.
   */
  @Override
  public int retainAll(final IntPredicate predicate) {
    return removeAll(value -> !predicate.apply(value));
  }

  /** Default implementation of copying to an array. */
  @Override
  public int[] toArray() {

    int[] array = (new int[size()]);
    int i = 0;
    for (IntCursor c : this) {
      array[i++] = c.value;
    }
    return array;
  }

  /** Convert the contents of this container to a human-friendly string. */
  @Override
  public String toString() {
    return Arrays.toString(this.toArray());
  }
}
