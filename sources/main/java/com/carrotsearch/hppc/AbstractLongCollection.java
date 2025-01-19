package com.carrotsearch.hppc;

import com.carrotsearch.hppc.cursors.LongCursor;
import com.carrotsearch.hppc.predicates.LongPredicate;
import java.util.Arrays;

/** Common superclass for collections. */
@com.carrotsearch.hppc.Generated(
    date = "2024-06-04T15:20:17+0200",
    value = "AbstractKTypeCollection.java")
abstract class AbstractLongCollection implements LongCollection {
  /** Default implementation uses a predicate for removal. */
  @Override
  public int removeAll(final LongLookupContainer c) {
    return this.removeAll(c::contains);
  }

  /** Default implementation uses a predicate for retaining. */
  @Override
  public int retainAll(final LongLookupContainer c) {
    // We know c holds sub-types of long and we're not modifying c, so go unchecked.
    return this.removeAll(k -> !c.contains(k));
  }

  /**
   * Default implementation redirects to {@link #removeAll(LongPredicate)} and negates the
   * predicate.
   */
  @Override
  public int retainAll(final LongPredicate predicate) {
    return removeAll(value -> !predicate.apply(value));
  }

  /** Default implementation of copying to an array. */
  @Override
  public long[] toArray() {

    long[] array = (new long[size()]);
    int i = 0;
    for (LongCursor c : this) {
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
