package com.carrotsearch.hppc;

import static com.carrotsearch.hppc.Containers.*;
import static com.carrotsearch.hppc.HashContainers.*;

import com.carrotsearch.hppc.cursors.*;
import com.carrotsearch.hppc.predicates.*;
import com.carrotsearch.hppc.procedures.*;
import java.util.*;

/**
 * A hash map of <code>long</code> to <code>byte</code>, implemented using open addressing with
 * linear probing for collision resolution.
 *
 * @see <a href="{@docRoot}/overview-summary.html#interfaces">HPPC interfaces diagram</a>
 */
@com.carrotsearch.hppc.Generated(
    date = "2024-06-04T15:20:17+0200",
    value = "KTypeVTypeHashMap.java")
public class LongByteHashMap implements LongByteMap, Preallocable, Cloneable, Accountable {

  /** The array holding keys. */
  public long[] keys;

  /** The array holding values. */
  public byte[] values;

  /**
   * The number of stored keys (assigned key slots), excluding the special "empty" key, if any (use
   * {@link #size()} instead).
   *
   * @see #size()
   */
  protected int assigned;

  /** Mask for slot scans in {@link #keys}. */
  protected int mask;

  /** Expand (rehash) {@link #keys} when {@link #assigned} hits this value. */
  protected int resizeAt;

  /** Special treatment for the "empty slot" key marker. */
  protected boolean hasEmptyKey;

  /** The load factor for {@link #keys}. */
  protected double loadFactor;

  /** Seed used to ensure the hash iteration order is different from an iteration to another. */
  protected int iterationSeed;

  /** New instance with sane defaults. */
  public LongByteHashMap() {
    this(DEFAULT_EXPECTED_ELEMENTS);
  }

  /**
   * New instance with sane defaults.
   *
   * @param expectedElements The expected number of elements guaranteed not to cause buffer
   *     expansion (inclusive).
   */
  public LongByteHashMap(int expectedElements) {
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
  public LongByteHashMap(int expectedElements, double loadFactor) {
    this.loadFactor = verifyLoadFactor(loadFactor);
    iterationSeed = HashContainers.nextIterationSeed();
    ensureCapacity(expectedElements);
  }

  /** Create a hash map from all key-value pairs of another container. */
  public LongByteHashMap(LongByteAssociativeContainer container) {
    this(container.size());
    putAll(container);
  }

  /** {@inheritDoc} */
  @Override
  public byte put(long key, byte value) {
    assert assigned < mask + 1;

    final int mask = this.mask;
    if (((key) == 0)) {
      byte previousValue = hasEmptyKey ? values[mask + 1] : ((byte) 0);
      hasEmptyKey = true;
      values[mask + 1] = value;
      return previousValue;
    } else {
      final long[] keys = this.keys;
      int slot = hashKey(key) & mask;

      long existing;
      while (!((existing = keys[slot]) == 0)) {
        if (((key) == (existing))) {
          final byte previousValue = values[slot];
          values[slot] = value;
          return previousValue;
        }
        slot = (slot + 1) & mask;
      }

      if (assigned == resizeAt) {
        allocateThenInsertThenRehash(slot, key, value);
      } else {
        keys[slot] = key;
        values[slot] = value;
      }

      assigned++;
      return ((byte) 0);
    }
  }

  /** {@inheritDoc} */
  @Override
  public int putAll(LongByteAssociativeContainer container) {
    final int count = size();
    for (LongByteCursor c : container) {
      put(c.key, c.value);
    }
    return size() - count;
  }

  /** Puts all key/value pairs from a given iterable into this map. */
  @Override
  public int putAll(Iterable<? extends LongByteCursor> iterable) {
    final int count = size();
    for (LongByteCursor c : iterable) {
      put(c.key, c.value);
    }
    return size() - count;
  }

  /**
   * If <code>key</code> exists, <code>putValue</code> is inserted into the map, otherwise any
   * existing value is incremented by <code>additionValue</code>.
   *
   * @param key The key of the value to adjust.
   * @param putValue The value to put if <code>key</code> does not exist.
   * @param incrementValue The value to add to the existing value if <code>key</code> exists.
   * @return Returns the current value associated with <code>key</code> (after changes).
   */
  @Override
  public byte putOrAdd(long key, byte putValue, byte incrementValue) {
    assert assigned < mask + 1;

    int keyIndex = indexOf(key);
    if (indexExists(keyIndex)) {
      putValue = ((byte) ((values[keyIndex]) + (incrementValue)));
      indexReplace(keyIndex, putValue);
    } else {
      indexInsert(keyIndex, key, putValue);
    }
    return putValue;
  }

  /**
   * Adds <code>incrementValue</code> to any existing value for the given <code>key</code> or
   * inserts <code>incrementValue</code> if <code>key</code> did not previously exist.
   *
   * @param key The key of the value to adjust.
   * @param incrementValue The value to put or add to the existing value if <code>key</code> exists.
   * @return Returns the current value associated with <code>key</code> (after changes).
   */
  @Override
  public byte addTo(long key, byte incrementValue) {
    return putOrAdd(key, incrementValue, incrementValue);
  }

  /** {@inheritDoc} */
  @Override
  public byte remove(long key) {
    final int mask = this.mask;
    if (((key) == 0)) {
      if (!hasEmptyKey) {
        return ((byte) 0);
      }
      hasEmptyKey = false;
      byte previousValue = values[mask + 1];
      values[mask + 1] = ((byte) 0);
      return previousValue;
    } else {
      final long[] keys = this.keys;
      int slot = hashKey(key) & mask;

      long existing;
      while (!((existing = keys[slot]) == 0)) {
        if (((key) == (existing))) {
          final byte previousValue = values[slot];
          shiftConflictingKeys(slot);
          return previousValue;
        }
        slot = (slot + 1) & mask;
      }

      return ((byte) 0);
    }
  }

  /** {@inheritDoc} */
  @Override
  public int removeAll(LongContainer other) {
    final int before = size();

    // Try to iterate over the smaller set of values or
    // over the container that isn't implementing
    // efficient contains() lookup.

    if (other.size() >= size() && other instanceof LongLookupContainer) {
      if (hasEmptyKey && other.contains(0L)) {
        hasEmptyKey = false;
        values[mask + 1] = ((byte) 0);
      }

      final long[] keys = this.keys;
      for (int slot = 0, max = this.mask; slot <= max; ) {
        long existing;
        if (!((existing = keys[slot]) == 0) && other.contains(existing)) {
          // Shift, do not increment slot.
          shiftConflictingKeys(slot);
        } else {
          slot++;
        }
      }
    } else {
      for (LongCursor c : other) {
        remove(c.value);
      }
    }

    return before - size();
  }

  /** {@inheritDoc} */
  @Override
  public int removeAll(LongBytePredicate predicate) {
    final int before = size();

    final int mask = this.mask;

    if (hasEmptyKey) {
      if (predicate.apply(0L, values[mask + 1])) {
        hasEmptyKey = false;
        values[mask + 1] = ((byte) 0);
      }
    }

    final long[] keys = this.keys;
    final byte[] values = this.values;
    for (int slot = 0; slot <= mask; ) {
      long existing;
      if (!((existing = keys[slot]) == 0) && predicate.apply(existing, values[slot])) {
        // Shift, do not increment slot.
        shiftConflictingKeys(slot);
      } else {
        slot++;
      }
    }

    return before - size();
  }

  /** {@inheritDoc} */
  @Override
  public int removeAll(LongPredicate predicate) {
    final int before = size();

    if (hasEmptyKey) {
      if (predicate.apply(0L)) {
        hasEmptyKey = false;
        values[mask + 1] = ((byte) 0);
      }
    }

    final long[] keys = this.keys;
    for (int slot = 0, max = this.mask; slot <= max; ) {
      long existing;
      if (!((existing = keys[slot]) == 0) && predicate.apply(existing)) {
        // Shift, do not increment slot.
        shiftConflictingKeys(slot);
      } else {
        slot++;
      }
    }

    return before - size();
  }

  /** {@inheritDoc} */
  @Override
  public byte get(long key) {
    if (((key) == 0)) {
      return hasEmptyKey ? values[mask + 1] : ((byte) 0);
    } else {
      final long[] keys = this.keys;
      final int mask = this.mask;
      int slot = hashKey(key) & mask;

      long existing;
      while (!((existing = keys[slot]) == 0)) {
        if (((key) == (existing))) {
          return values[slot];
        }
        slot = (slot + 1) & mask;
      }

      return ((byte) 0);
    }
  }

  /** {@inheritDoc} */
  @Override
  public byte getOrDefault(long key, byte defaultValue) {
    if (((key) == 0)) {
      return hasEmptyKey ? values[mask + 1] : defaultValue;
    } else {
      final long[] keys = this.keys;
      final int mask = this.mask;
      int slot = hashKey(key) & mask;

      long existing;
      while (!((existing = keys[slot]) == 0)) {
        if (((key) == (existing))) {
          return values[slot];
        }
        slot = (slot + 1) & mask;
      }

      return defaultValue;
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean containsKey(long key) {
    if (((key) == 0)) {
      return hasEmptyKey;
    } else {
      final long[] keys = this.keys;
      final int mask = this.mask;
      int slot = hashKey(key) & mask;

      long existing;
      while (!((existing = keys[slot]) == 0)) {
        if (((key) == (existing))) {
          return true;
        }
        slot = (slot + 1) & mask;
      }

      return false;
    }
  }

  /** {@inheritDoc} */
  @Override
  public int indexOf(long key) {
    final int mask = this.mask;
    if (((key) == 0)) {
      return hasEmptyKey ? mask + 1 : ~(mask + 1);
    } else {
      final long[] keys = this.keys;
      int slot = hashKey(key) & mask;

      long existing;
      while (!((existing = keys[slot]) == 0)) {
        if (((key) == (existing))) {
          return slot;
        }
        slot = (slot + 1) & mask;
      }

      return ~slot;
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean indexExists(int index) {
    assert index < 0 || (index >= 0 && index <= mask) || (index == mask + 1 && hasEmptyKey);

    return index >= 0;
  }

  /** {@inheritDoc} */
  @Override
  public byte indexGet(int index) {
    assert index >= 0 : "The index must point at an existing key.";
    assert index <= mask || (index == mask + 1 && hasEmptyKey);

    return values[index];
  }

  /** {@inheritDoc} */
  @Override
  public byte indexReplace(int index, byte newValue) {
    assert index >= 0 : "The index must point at an existing key.";
    assert index <= mask || (index == mask + 1 && hasEmptyKey);

    byte previousValue = values[index];
    values[index] = newValue;
    return previousValue;
  }

  /** {@inheritDoc} */
  @Override
  public void indexInsert(int index, long key, byte value) {
    assert index < 0 : "The index must not point at an existing key.";

    index = ~index;
    if (((key) == 0)) {
      assert index == mask + 1;
      values[index] = value;
      hasEmptyKey = true;
    } else {
      assert ((keys[index]) == 0);

      if (assigned == resizeAt) {
        allocateThenInsertThenRehash(index, key, value);
      } else {
        keys[index] = key;
        values[index] = value;
      }

      assigned++;
    }
  }

  /** {@inheritDoc} */
  @Override
  public byte indexRemove(int index) {
    assert index >= 0 : "The index must point at an existing key.";
    assert index <= mask || (index == mask + 1 && hasEmptyKey);

    byte previousValue = values[index];
    if (index > mask) {
      assert index == mask + 1;
      hasEmptyKey = false;
      values[index] = ((byte) 0);
    } else {
      shiftConflictingKeys(index);
    }
    return previousValue;
  }

  /** {@inheritDoc} */
  @Override
  public void clear() {
    assigned = 0;
    hasEmptyKey = false;

    Arrays.fill(keys, 0L);
  }

  /** {@inheritDoc} */
  @Override
  public void release() {
    assigned = 0;
    hasEmptyKey = false;

    keys = null;
    values = null;
    ensureCapacity(Containers.DEFAULT_EXPECTED_ELEMENTS);
  }

  /** {@inheritDoc} */
  @Override
  public int size() {
    return assigned + (hasEmptyKey ? 1 : 0);
  }

  /** {@inheritDoc} */
  public boolean isEmpty() {
    return size() == 0;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    int h = hasEmptyKey ? 0xDEADBEEF : 0;
    for (LongByteCursor c : this) {
      h += BitMixer.mix(c.key) + BitMixer.mix(c.value);
    }
    return h;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object obj) {
    return (this == obj)
        || (obj != null && getClass() == obj.getClass() && equalElements(getClass().cast(obj)));
  }

  /** Return true if all keys of some other container exist in this container. */
  protected boolean equalElements(LongByteHashMap other) {
    if (other.size() != size()) {
      return false;
    }

    for (LongByteCursor c : other) {
      long key = c.key;
      if (!containsKey(key) || !((c.value) == (get(key)))) {
        return false;
      }
    }

    return true;
  }

  /**
   * Ensure this container can hold at least the given number of keys (entries) without resizing its
   * buffers.
   *
   * @param expectedElements The total number of keys, inclusive.
   */
  @Override
  public void ensureCapacity(int expectedElements) {
    if (expectedElements > resizeAt || keys == null) {
      final long[] prevKeys = this.keys;
      final byte[] prevValues = this.values;
      allocateBuffers(minBufferSize(expectedElements, loadFactor));
      if (prevKeys != null && !isEmpty()) {
        rehash(prevKeys, prevValues);
      }
    }
  }

  @Override
  public long ramBytesAllocated() {
    // int: iterationSeed, assigned, mask, resizeAt
    // double: loadFactor
    // boolean: hasEmptyKey
    return RamUsageEstimator.NUM_BYTES_OBJECT_HEADER
        + 4 * Integer.BYTES
        + Double.BYTES
        + 1
        + RamUsageEstimator.shallowSizeOfArray(keys)
        + RamUsageEstimator.shallowSizeOfArray(values);
  }

  @Override
  public long ramBytesUsed() {
    // int: iterationSeed, assigned, mask, resizeAt
    // double: loadFactor
    // boolean: hasEmptyKey
    return RamUsageEstimator.NUM_BYTES_OBJECT_HEADER
        + 4 * Integer.BYTES
        + Double.BYTES
        + 1
        + RamUsageEstimator.shallowUsedSizeOfArray(keys, size())
        + RamUsageEstimator.shallowUsedSizeOfArray(values, size());
  }

  /**
   * Provides the next iteration seed used to build the iteration starting slot and offset
   * increment. This method does not need to be synchronized, what matters is that each thread gets
   * a sequence of varying seeds.
   */
  protected int nextIterationSeed() {
    return iterationSeed = BitMixer.mixPhi(iterationSeed);
  }

  /** An iterator implementation for {@link #iterator}. */
  private final class EntryIterator extends AbstractIterator<LongByteCursor> {
    private final LongByteCursor cursor;
    private final int increment;
    private int index;
    private int slot;

    public EntryIterator() {
      cursor = new LongByteCursor();
      int seed = nextIterationSeed();
      increment = iterationIncrement(seed);
      slot = seed & mask;
    }

    @Override
    protected LongByteCursor fetch() {
      final int mask = LongByteHashMap.this.mask;
      while (index <= mask) {
        long existing;
        index++;
        slot = (slot + increment) & mask;
        if (!((existing = keys[slot]) == 0)) {
          cursor.index = slot;
          cursor.key = existing;
          cursor.value = values[slot];
          return cursor;
        }
      }

      if (index == mask + 1 && hasEmptyKey) {
        cursor.index = index;
        cursor.key = 0L;
        cursor.value = values[index++];
        return cursor;
      }

      return done();
    }
  }

  /** {@inheritDoc} */
  @Override
  public Iterator<LongByteCursor> iterator() {
    return new EntryIterator();
  }

  /** {@inheritDoc} */
  @Override
  public <T extends LongByteProcedure> T forEach(T procedure) {
    final long[] keys = this.keys;
    final byte[] values = this.values;

    if (hasEmptyKey) {
      procedure.apply(0L, values[mask + 1]);
    }

    int seed = nextIterationSeed();
    int inc = iterationIncrement(seed);
    for (int i = 0, mask = this.mask, slot = seed & mask;
        i <= mask;
        i++, slot = (slot + inc) & mask) {
      if (!((keys[slot]) == 0)) {
        procedure.apply(keys[slot], values[slot]);
      }
    }

    return procedure;
  }

  /** {@inheritDoc} */
  @Override
  public <T extends LongBytePredicate> T forEach(T predicate) {
    final long[] keys = this.keys;
    final byte[] values = this.values;

    if (hasEmptyKey) {
      if (!predicate.apply(0L, values[mask + 1])) {
        return predicate;
      }
    }

    int seed = nextIterationSeed();
    int inc = iterationIncrement(seed);
    for (int i = 0, mask = this.mask, slot = seed & mask;
        i <= mask;
        i++, slot = (slot + inc) & mask) {
      if (!((keys[slot]) == 0)) {
        if (!predicate.apply(keys[slot], values[slot])) {
          break;
        }
      }
    }

    return predicate;
  }

  /**
   * Returns a specialized view of the keys of this associated container. The view additionally
   * implements {@link ObjectLookupContainer}.
   */
  public KeysContainer keys() {
    return new KeysContainer();
  }

  /** A view of the keys inside this hash map. */
  public final class KeysContainer extends AbstractLongCollection implements LongLookupContainer {
    private final LongByteHashMap owner = LongByteHashMap.this;

    @Override
    public boolean contains(long e) {
      return owner.containsKey(e);
    }

    @Override
    public <T extends LongProcedure> T forEach(final T procedure) {
      owner.forEach((LongByteProcedure) (k, v) -> procedure.apply(k));
      return procedure;
    }

    @Override
    public <T extends LongPredicate> T forEach(final T predicate) {
      owner.forEach((LongBytePredicate) (key, value) -> predicate.apply(key));
      return predicate;
    }

    @Override
    public boolean isEmpty() {
      return owner.isEmpty();
    }

    @Override
    public Iterator<LongCursor> iterator() {
      return new KeysIterator();
    }

    @Override
    public int size() {
      return owner.size();
    }

    @Override
    public void clear() {
      owner.clear();
    }

    @Override
    public void release() {
      owner.release();
    }

    @Override
    public int removeAll(LongPredicate predicate) {
      return owner.removeAll(predicate);
    }

    @Override
    public int removeAll(final long e) {
      if (owner.containsKey(e)) {
        owner.remove(e);
        return 1;
      } else {
        return 0;
      }
    }
  }
  ;

  /** An iterator over the set of assigned keys. */
  private final class KeysIterator extends AbstractIterator<LongCursor> {
    private final LongCursor cursor;
    private final int increment;
    private int index;
    private int slot;

    public KeysIterator() {
      cursor = new LongCursor();
      int seed = nextIterationSeed();
      increment = iterationIncrement(seed);
      slot = seed & mask;
    }

    @Override
    protected LongCursor fetch() {
      final int mask = LongByteHashMap.this.mask;
      while (index <= mask) {
        long existing;
        index++;
        slot = (slot + increment) & mask;
        if (!((existing = keys[slot]) == 0)) {
          cursor.index = slot;
          cursor.value = existing;
          return cursor;
        }
      }

      if (index == mask + 1 && hasEmptyKey) {
        cursor.index = index++;
        cursor.value = 0L;
        return cursor;
      }

      return done();
    }
  }

  /**
   * @return Returns a container with all values stored in this map.
   */
  @Override
  public ByteCollection values() {
    return new ValuesContainer();
  }

  /** A view over the set of values of this map. */
  private final class ValuesContainer extends AbstractByteCollection {
    private final LongByteHashMap owner = LongByteHashMap.this;

    @Override
    public int size() {
      return owner.size();
    }

    @Override
    public boolean isEmpty() {
      return owner.isEmpty();
    }

    @Override
    public boolean contains(byte value) {
      for (LongByteCursor c : owner) {
        if (((value) == (c.value))) {
          return true;
        }
      }
      return false;
    }

    @Override
    public <T extends ByteProcedure> T forEach(T procedure) {
      for (LongByteCursor c : owner) {
        procedure.apply(c.value);
      }
      return procedure;
    }

    @Override
    public <T extends BytePredicate> T forEach(T predicate) {
      for (LongByteCursor c : owner) {
        if (!predicate.apply(c.value)) {
          break;
        }
      }
      return predicate;
    }

    @Override
    public Iterator<ByteCursor> iterator() {
      return new ValuesIterator();
    }

    @Override
    public int removeAll(final byte e) {
      return owner.removeAll((key, value) -> ((e) == (value)));
    }

    @Override
    public int removeAll(final BytePredicate predicate) {
      return owner.removeAll((key, value) -> predicate.apply(value));
    }

    @Override
    public void clear() {
      owner.clear();
    }

    @Override
    public void release() {
      owner.release();
    }
  }

  /** An iterator over the set of assigned values. */
  private final class ValuesIterator extends AbstractIterator<ByteCursor> {
    private final ByteCursor cursor;
    private final int increment;
    private int index;
    private int slot;

    public ValuesIterator() {
      cursor = new ByteCursor();
      int seed = nextIterationSeed();
      increment = iterationIncrement(seed);
      slot = seed & mask;
    }

    @Override
    protected ByteCursor fetch() {
      final int mask = LongByteHashMap.this.mask;
      while (index <= mask) {
        index++;
        slot = (slot + increment) & mask;
        if (!((keys[slot]) == 0)) {
          cursor.index = slot;
          cursor.value = values[slot];
          return cursor;
        }
      }

      if (index == mask + 1 && hasEmptyKey) {
        cursor.index = index;
        cursor.value = values[index++];
        return cursor;
      }

      return done();
    }
  }

  /** {@inheritDoc} */
  @Override
  public LongByteHashMap clone() {
    try {

      LongByteHashMap cloned = (LongByteHashMap) super.clone();
      cloned.keys = keys.clone();
      cloned.values = values.clone();
      cloned.hasEmptyKey = hasEmptyKey;
      cloned.iterationSeed = HashContainers.nextIterationSeed();
      return cloned;
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  /** Convert the contents of this map to a human-friendly string. */
  @Override
  public String toString() {
    final StringBuilder buffer = new StringBuilder();
    buffer.append("[");

    boolean first = true;
    for (LongByteCursor cursor : this) {
      if (!first) {
        buffer.append(", ");
      }
      buffer.append(cursor.key);
      buffer.append("=>");
      buffer.append(cursor.value);
      first = false;
    }
    buffer.append("]");
    return buffer.toString();
  }

  @Override
  public String visualizeKeyDistribution(int characters) {
    return LongBufferVisualizer.visualizeKeyDistribution(keys, mask, characters);
  }

  /** Creates a hash map from two index-aligned arrays of key-value pairs. */
  public static LongByteHashMap from(long[] keys, byte[] values) {
    if (keys.length != values.length) {
      throw new IllegalArgumentException(
          "Arrays of keys and values must have an identical length.");
    }

    LongByteHashMap map = new LongByteHashMap(keys.length);
    for (int i = 0; i < keys.length; i++) {
      map.put(keys[i], values[i]);
    }

    return map;
  }

  /**
   * Returns a hash code for the given key.
   *
   * <p>The output from this function should evenly distribute keys across the entire integer range.
   */
  protected int hashKey(long key) {
    assert !((key) == 0); // Handled as a special case (empty slot marker).
    return BitMixer.mixPhi(key);
  }

  /**
   * Validate load factor range and return it. Override and suppress if you need insane load
   * factors.
   */
  protected double verifyLoadFactor(double loadFactor) {
    checkLoadFactor(loadFactor, MIN_LOAD_FACTOR, MAX_LOAD_FACTOR);
    return loadFactor;
  }

  /** Rehash from old buffers to new buffers. */
  protected void rehash(long[] fromKeys, byte[] fromValues) {
    assert fromKeys.length == fromValues.length
        && HashContainers.checkPowerOfTwo(fromKeys.length - 1);

    // Rehash all stored key/value pairs into the new buffers.
    final long[] keys = this.keys;
    final byte[] values = this.values;
    final int mask = this.mask;
    long existing;

    // Copy the zero element's slot, then rehash everything else.
    int from = fromKeys.length - 1;
    keys[keys.length - 1] = fromKeys[from];
    values[values.length - 1] = fromValues[from];
    while (--from >= 0) {
      if (!((existing = fromKeys[from]) == 0)) {
        int slot = hashKey(existing) & mask;
        while (!((keys[slot]) == 0)) {
          slot = (slot + 1) & mask;
        }
        keys[slot] = existing;
        values[slot] = fromValues[from];
      }
    }
  }

  /**
   * Allocate new internal buffers. This method attempts to allocate and assign internal buffers
   * atomically (either allocations succeed or not).
   */
  protected void allocateBuffers(int arraySize) {
    assert Integer.bitCount(arraySize) == 1;

    // Ensure no change is done if we hit an OOM.
    long[] prevKeys = this.keys;
    byte[] prevValues = this.values;
    try {
      int emptyElementSlot = 1;
      this.keys = (new long[arraySize + emptyElementSlot]);
      this.values = (new byte[arraySize + emptyElementSlot]);
    } catch (OutOfMemoryError e) {
      this.keys = prevKeys;
      this.values = prevValues;
      throw new BufferAllocationException(
          "Not enough memory to allocate buffers for rehashing: %,d -> %,d",
          e, this.mask + 1, arraySize);
    }

    this.resizeAt = expandAtCount(arraySize, loadFactor);
    this.mask = arraySize - 1;
  }

  /**
   * This method is invoked when there is a new key/ value pair to be inserted into the buffers but
   * there is not enough empty slots to do so.
   *
   * <p>New buffers are allocated. If this succeeds, we know we can proceed with rehashing so we
   * assign the pending element to the previous buffer (possibly violating the invariant of having
   * at least one empty slot) and rehash all keys, substituting new buffers at the end.
   */
  protected void allocateThenInsertThenRehash(int slot, long pendingKey, byte pendingValue) {
    assert assigned == resizeAt && ((keys[slot]) == 0) && !((pendingKey) == 0);

    // Try to allocate new buffers first. If we OOM, we leave in a consistent state.
    final long[] prevKeys = this.keys;
    final byte[] prevValues = this.values;
    allocateBuffers(nextBufferSize(mask + 1, size(), loadFactor));
    assert this.keys.length > prevKeys.length;

    // We have succeeded at allocating new data so insert the pending key/value at
    // the free slot in the old arrays before rehashing.
    prevKeys[slot] = pendingKey;
    prevValues[slot] = pendingValue;

    // Rehash old keys, including the pending key.
    rehash(prevKeys, prevValues);
  }

  /**
   * Shift all the slot-conflicting keys and values allocated to (and including) <code>slot</code>.
   */
  protected void shiftConflictingKeys(int gapSlot) {
    final long[] keys = this.keys;
    final byte[] values = this.values;
    final int mask = this.mask;

    // Perform shifts of conflicting keys to fill in the gap.
    int distance = 0;
    while (true) {
      final int slot = (gapSlot + (++distance)) & mask;
      final long existing = keys[slot];
      if (((existing) == 0)) {
        break;
      }

      final int idealSlot = hashKey(existing);
      final int shift = (slot - idealSlot) & mask;
      if (shift >= distance) {
        // Entry at this position was originally at or before the gap slot.
        // Move the conflict-shifted entry to the gap's position and repeat the procedure
        // for any entries to the right of the current position, treating it
        // as the new gap.
        keys[gapSlot] = existing;
        values[gapSlot] = values[slot];
        gapSlot = slot;
        distance = 0;
      }
    }

    // Mark the last found gap slot without a conflict as empty.
    keys[gapSlot] = 0L;
    values[gapSlot] = ((byte) 0);
    assigned--;
  }
}
