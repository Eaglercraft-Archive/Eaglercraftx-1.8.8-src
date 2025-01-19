package com.carrotsearch.hppc.comparators;

/** Compares two <code>long</code> values. */
@com.carrotsearch.hppc.Generated(date = "2024-06-04T15:20:17+0200", value = "KTypeComparator.java")
public interface LongComparator {
  int compare(long a, long b);

  static <KType> LongComparator naturalOrder() {
    return Long::compare;
  }
}
