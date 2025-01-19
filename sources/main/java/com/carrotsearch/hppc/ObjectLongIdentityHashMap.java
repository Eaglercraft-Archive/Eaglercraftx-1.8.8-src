package com.carrotsearch.hppc;

import static com.carrotsearch.hppc.Containers.*;
import static com.carrotsearch.hppc.HashContainers.*;

/** An identity hash map of <code>Object</code> to <code>long</code>. */
@com.carrotsearch.hppc.Generated(
    date = "2024-06-04T15:20:16+0200",
    value = "KTypeVTypeIdentityHashMap.java")
public class ObjectLongIdentityHashMap<KType> extends ObjectLongHashMap<KType> {
  /** New instance with sane defaults. */
  public ObjectLongIdentityHashMap() {
    this(DEFAULT_EXPECTED_ELEMENTS);
  }

  /**
   * New instance with sane defaults.
   *
   * @param expectedElements The expected number of elements guaranteed not to cause buffer
   *     expansion (inclusive).
   */
  public ObjectLongIdentityHashMap(int expectedElements) {
    this(expectedElements, DEFAULT_LOAD_FACTOR);
  }

  /**
   * New instance with the provided defaults.
   *
   * @param expectedElements The expected number of elements guaranteed not to cause a rehash
   *     (inclusive).
   * @param loadFactor The load factor for internal buffers. Insane load factors (zero, full
   *     capacity) are rejected by {@link #verifyLoadFactor(double)}.
   */
  public ObjectLongIdentityHashMap(int expectedElements, double loadFactor) {
    super(expectedElements, loadFactor);
  }

  /** Create a hash map from all key-value pairs of another container. */
  public ObjectLongIdentityHashMap(ObjectLongAssociativeContainer<? extends KType> container) {
    this(container.size());
    putAll(container);
  }

  @Override
  public int hashKey(KType key) {
    assert !((key) == null); // Handled as a special case (empty slot marker).
    return BitMixer.mixPhi(System.identityHashCode(key));
  }

  @Override
  public boolean equals(Object v1, Object v2) {
    return v1 == v2;
  }

  @SuppressWarnings("unchecked")

  /** Creates a hash map from two index-aligned arrays of key-value pairs. */
  public static <KType> ObjectLongIdentityHashMap<KType> from(KType[] keys, long[] values) {
    if (keys.length != values.length) {
      throw new IllegalArgumentException(
          "Arrays of keys and values must have an identical length.");
    }

    ObjectLongIdentityHashMap<KType> map = new ObjectLongIdentityHashMap<>(keys.length);
    for (int i = 0; i < keys.length; i++) {
      map.put(keys[i], values[i]);
    }

    return map;
  }
}
