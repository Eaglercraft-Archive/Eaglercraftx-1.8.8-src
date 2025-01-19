package com.carrotsearch.hppc.comparators;

/** Compares two <code>Object</code>, <code>byte</code> pairs. */
@com.carrotsearch.hppc.Generated(
    date = "2024-06-04T15:20:17+0200",
    value = "KTypeVTypeComparator.java")
public interface ObjectByteComparator<KType> {
  int compare(KType k1, byte v1, KType k2, byte v2);
}
