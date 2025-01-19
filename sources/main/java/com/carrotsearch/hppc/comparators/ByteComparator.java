package com.carrotsearch.hppc.comparators;

/** Compares two <code>byte</code> values. */
@com.carrotsearch.hppc.Generated(date = "2024-06-04T15:20:17+0200", value = "KTypeComparator.java")
public interface ByteComparator {
  int compare(byte a, byte b);

  static <KType> ByteComparator naturalOrder() {
    return Byte::compare;
  }
}
