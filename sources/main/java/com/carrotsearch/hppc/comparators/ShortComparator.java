package com.carrotsearch.hppc.comparators;

/** Compares two <code>short</code> values. */
@com.carrotsearch.hppc.Generated(date = "2024-06-04T15:20:17+0200", value = "KTypeComparator.java")
public interface ShortComparator {
  int compare(short a, short b);

  static <KType> ShortComparator naturalOrder() {
    return Short::compare;
  }
}
