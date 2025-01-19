package com.carrotsearch.hppc.comparators;

/** Compares two <code>int</code> values. */
@com.carrotsearch.hppc.Generated(date = "2024-06-04T15:20:17+0200", value = "KTypeComparator.java")
public interface IntComparator {
  int compare(int a, int b);

  static <KType> IntComparator naturalOrder() {
    return Integer::compare;
  }
}
