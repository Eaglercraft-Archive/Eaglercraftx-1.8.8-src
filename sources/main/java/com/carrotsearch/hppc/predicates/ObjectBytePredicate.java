package com.carrotsearch.hppc.predicates;

/** A predicate that applies to <code>Object</code>, <code>byte</code> pairs. */
@com.carrotsearch.hppc.Generated(
    date = "2024-06-04T15:20:16+0200",
    value = "KTypeVTypePredicate.java")
public interface ObjectBytePredicate<KType> {
  public boolean apply(KType key, byte value);
}
