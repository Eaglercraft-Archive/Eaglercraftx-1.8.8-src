package com.carrotsearch.hppc.cursors;

/** A cursor over entries of an associative container (int keys and long values). */
@com.carrotsearch.hppc.Generated(date = "2024-06-04T15:20:17+0200", value = "KTypeVTypeCursor.java")
public final class IntLongCursor {
  /**
   * The current key and value's index in the container this cursor belongs to. The meaning of this
   * index is defined by the container (usually it will be an index in the underlying storage
   * buffer).
   */
  public int index;

  /** The current key. */
  public int key;

  /** The current value. */
  public long value;

  @Override
  public String toString() {
    return "[cursor, index: " + index + ", key: " + key + ", value: " + value + "]";
  }
}
