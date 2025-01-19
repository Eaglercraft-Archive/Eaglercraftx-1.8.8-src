package com.carrotsearch.hppc;

import com.carrotsearch.hppc.cursors.ShortCursor;
import com.carrotsearch.hppc.predicates.ShortPredicate;
import java.util.Arrays;

/** Common superclass for collections. */
@com.carrotsearch.hppc.Generated(
    date = "2024-06-04T15:20:17+0200",
    value = "AbstractKTypeCollection.java")
abstract class AbstractShortCollection implements ShortCollection {
  /** Default implementation uses a predicate for removal. */
  @Override
  public int removeAll(final ShortLookupContainer c) {
    return this.removeAll(c::contains);
  }

  /** Default implementation uses a predicate for retaining. */
  @Override
  public int retainAll(final ShortLookupContainer c) {
    // We know c holds sub-types of short and we're not modifying c, so go unchecked.
    return this.removeAll(k -> !c.contains(k));
  }

  /**
   * Default implementation redirects to {@link #removeAll(ShortPredicate)} and negates the
   * predicate.
   */
  @Override
  public int retainAll(final ShortPredicate predicate) {
    return removeAll(value -> !predicate.apply(value));
  }

  /** Default implementation of copying to an array. */
  @Override
  public short[] toArray() {

    short[] array = (new short[size()]);
    int i = 0;
    for (ShortCursor c : this) {
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
