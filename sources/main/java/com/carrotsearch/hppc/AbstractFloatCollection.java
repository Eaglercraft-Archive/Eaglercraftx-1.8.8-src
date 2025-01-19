package com.carrotsearch.hppc;

import com.carrotsearch.hppc.cursors.FloatCursor;
import com.carrotsearch.hppc.predicates.FloatPredicate;
import java.util.Arrays;

/** Common superclass for collections. */
@com.carrotsearch.hppc.Generated(
    date = "2024-06-04T15:20:17+0200",
    value = "AbstractKTypeCollection.java")
abstract class AbstractFloatCollection implements FloatCollection {
  /** Default implementation uses a predicate for removal. */
  @Override
  public int removeAll(final FloatLookupContainer c) {
    return this.removeAll(c::contains);
  }

  /** Default implementation uses a predicate for retaining. */
  @Override
  public int retainAll(final FloatLookupContainer c) {
    // We know c holds sub-types of float and we're not modifying c, so go unchecked.
    return this.removeAll(k -> !c.contains(k));
  }

  /**
   * Default implementation redirects to {@link #removeAll(FloatPredicate)} and negates the
   * predicate.
   */
  @Override
  public int retainAll(final FloatPredicate predicate) {
    return removeAll(value -> !predicate.apply(value));
  }

  /** Default implementation of copying to an array. */
  @Override
  public float[] toArray() {

    float[] array = (new float[size()]);
    int i = 0;
    for (FloatCursor c : this) {
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
