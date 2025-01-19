package com.carrotsearch.hppc;

import com.carrotsearch.hppc.cursors.LongCursor;
import com.carrotsearch.hppc.procedures.LongProcedure;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Space-efficient index that enables fast rank/range search operations on a sorted sequence of
 * <code>long</code>.
 *
 * <p>Implementation of the PGM-Index described at <a
 * href="https://pgm.di.unipi.it/">https://pgm.di.unipi.it/</a>, based on the paper
 *
 * <pre>
 *   Paolo Ferragina and Giorgio Vinciguerra.
 *   The PGM-index: a fully-dynamic compressed learned index with provable worst-case bounds.
 *   PVLDB, 13(8): 1162-1175, 2020.
 * </pre>
 *
 * It provides {@code rank} and {@code range} search operations. {@code indexOf()} is faster than
 * B+Tree, and the index is much more compact. {@code contains()} is between 4x to 7x slower than
 * {@code IntHashSet#contains()}, but between 2.5x to 3x faster than {@link Arrays#binarySearch}.
 *
 * <p>Its compactness (40KB for 200MB of keys) makes it efficient for very large collections, the
 * index fitting easily in the L2 cache. The {@code epsilon} parameter should be set according to
 * the desired space-time trade-off. A smaller value makes the estimation more precise and the range
 * smaller but at the cost of increased space usage. In practice, {@code epsilon} 64 is a good sweet
 * spot.
 *
 * <p>Internally the index uses an optimal piecewise linear mapping from keys to their position in
 * the sorted order. This mapping is represented as a sequence of linear models (segments) which are
 * themselves recursively indexed by other piecewise linear mappings.
 */
@com.carrotsearch.hppc.Generated(date = "2024-06-04T15:20:17+0200", value = "KTypePgmIndex.java")
public class LongPgmIndex implements Accountable {

  /** Empty immutable LongPgmIndex. */
  public static final LongPgmIndex EMPTY = new LongEmptyPgmIndex();

  /**
   * Epsilon approximation range when searching the list of keys. Controls the size of the returned
   * search range, strictly greater than 0. It should be set according to the desired space-time
   * trade-off. A smaller value makes the estimation more precise and the range smaller but at the
   * cost of increased space usage.
   *
   * <p>With EPSILON=64 the benchmark with 200MB of keys shows that this PGM index requires only 2%
   * additional memory on average (40KB). It depends on the distribution of the keys. This epsilon
   * value is good even for 2MB of keys. With EPSILON=32: +5% speed, but 4x space (160KB).
   */
  public static final int EPSILON = 64;

  /**
   * Epsilon approximation range for the segments layers. Controls the size of the search range in
   * the hierarchical segment lists, strictly greater than 0.
   */
  public static final int EPSILON_RECURSIVE = 32;

  /** Size of a key, measured in {@link Integer#BYTES} because the key is stored in an int[]. */
  public static final int KEY_SIZE =
      RamUsageEstimator.primitiveSizes.get(long.class) / Integer.BYTES;

  /** 2x {@link #KEY_SIZE}. */
  public static final int DOUBLE_KEY_SIZE = KEY_SIZE * 2;

  /**
   * Data size of a segment, measured in {@link Integer#BYTES}, because segments are stored in an
   * int[].
   */
  public static final int SEGMENT_DATA_SIZE = KEY_SIZE * 3;

  /** Initial value of the exponential jump when scanning out of the epsilon range. */
  public static final int BEYOND_EPSILON_JUMP = 16;

  /**
   * The list of keys for which this index is built. It is sorted and may contain duplicate
   * elements.
   */
  public final LongArrayList keys;

  /** The size of the key set. That is, the number of distinct elements in {@link #keys}. */
  public final int size;

  /** The lowest key in {@link #keys}. */
  public final long firstKey;

  /** The highest key in {@link #keys}. */
  public final long lastKey;

  /** The epsilon range used to build this index. */
  public final int epsilon;

  /** The recursive epsilon range used to build this index. */
  public final int epsilonRecursive;

  /** The offsets in {@link #segmentData} of the first segment of each segment level. */
  public final int[] levelOffsets;

  /** The index data. It contains all the segments for all the levels. */
  public final int[] segmentData;

  private LongPgmIndex(
      LongArrayList keys,
      int size,
      int epsilon,
      int epsilonRecursive,
      int[] levelOffsets,
      int[] segmentData) {
    assert keys.size() > 0;
    assert size > 0 && size <= keys.size();
    assert epsilon > 0;
    assert epsilonRecursive > 0;
    this.keys = keys;
    this.size = size;
    firstKey = keys.get(0);
    lastKey = keys.get(keys.size() - 1);
    this.epsilon = epsilon;
    this.epsilonRecursive = epsilonRecursive;
    this.levelOffsets = levelOffsets;
    this.segmentData = segmentData;
  }

  /** Empty set constructor. */
  private LongPgmIndex() {
    keys = new LongArrayList(0);
    size = 0;
    firstKey = 0L;
    lastKey = 0L;
    epsilon = 0;
    epsilonRecursive = 0;
    levelOffsets = new int[0];
    segmentData = levelOffsets;
  }

  /** Returns the size of the key set. That is, the number of distinct elements in {@link #keys}. */
  public int size() {
    return size;
  }

  /** Returns whether this key set is empty. */
  public boolean isEmpty() {
    return size() == 0;
  }

  /** Returns whether this key set contains the given key. */
  public boolean contains(long key) {
    return indexOf(key) >= 0;
  }

  /**
   * Searches the specified key, and returns its index in the element list. If multiple elements are
   * equal to the specified key, there is no guarantee which one will be found.
   *
   * @return The index of the searched key if it is present; otherwise, {@code (-(<i>insertion
   *     point</i>) - 1)}. The <i>insertion point</i> is defined as the point at which the key would
   *     be inserted into the list: the index of the first element greater than the key, or {@link
   *     #keys}#{@code size()} if all the elements are less than the specified key. Note that this
   *     guarantees that the return value will be &gt;= 0 if and only if the key is found.
   */
  public int indexOf(long key) {
    if (key < firstKey) {
      return -1;
    }
    if (key > lastKey) {
      return -keys.size() - 1;
    }
    final int[] segmentData = this.segmentData;
    int segmentDataIndex = findSegment(key);
    int nextIntercept = (int) getIntercept(segmentDataIndex + SEGMENT_DATA_SIZE, segmentData);
    int index =
        Math.min(
            approximateIndex(key, segmentDataIndex, segmentData),
            Math.min(nextIntercept, keys.size() - 1));
    assert index >= 0 && index < keys.size();
    long k = keys.get(index);
    if (key < k) {
      // Scan sequentially before the approximated index, within epsilon range.
      final int fromIndex = Math.max(index - epsilon - 1, 0);
      while (--index >= fromIndex) {
        k = keys.get(index);
        if (key > k) {
          return -index - 2;
        }
        if (((key) == (k))) {
          return index;
        }
      }
      // Continue scanning out of the epsilon range.
      // This might happen in rare cases of precision error during the approximation
      // computation for longs (we don't have long double 128 bits in Java).
      // This might also happen in rare corner cases of large duplicate elements
      // sequence at the epsilon range boundary.
      index++;
      int jump = BEYOND_EPSILON_JUMP;
      do {
        int loIndex = Math.max(index - jump, 0);
        if (key >= keys.get(loIndex)) {
          return Arrays.binarySearch(keys.buffer, loIndex, index, key);
        }
        index = loIndex;
        jump <<= 1;
      } while (index > 0);
      return -1;
    } else if (((key) == (k))) {
      return index;
    } else {
      // Scan sequentially after the approximated index, within epsilon range.
      final int toIndex = Math.min(index + epsilon + 3, keys.size());
      while (++index < toIndex) {
        k = keys.get(index);
        if (key < k) {
          return -index - 1;
        }
        if (((key) == (k))) {
          return index;
        }
      }
      // Continue scanning out of the epsilon range.
      int jump = BEYOND_EPSILON_JUMP;
      do {
        int hiIndex = Math.min(index + jump, keys.size());
        if (key <= keys.get(hiIndex)) {
          return Arrays.binarySearch(keys.buffer, index, hiIndex, key);
        }
        index = hiIndex;
        jump <<= 1;
      } while (index < keys.size());
      return -keys.size() - 1;
    }
  }

  /**
   * Returns, for any value {@code x}, the number of keys in the sorted list which are smaller than
   * {@code x}. It is equal to {@link #indexOf} if {@code x} belongs to the list, or -{@link
   * #indexOf}-1 otherwise.
   *
   * <p>If multiple elements are equal to the specified key, there is no guarantee which one will be
   * found.
   *
   * @return The index of the searched key if it is present; otherwise, the {@code insertion point}.
   *     The <i>insertion point</i> is defined as the point at which the key would be inserted into
   *     the list: the index of the first element greater than the key, or {@link #keys}#{@code
   *     size()} if all the elements are less than the specified key. Note that this method always
   *     returns a value &gt;= 0.
   */
  public int rank(long x) {
    int index = indexOf(x);
    return index >= 0 ? index : -index - 1;
  }

  /**
   * Returns the number of keys in the list that are greater than or equal to {@code minKey}
   * (inclusive), and less than or equal to {@code maxKey} (inclusive).
   */
  public int rangeCardinality(long minKey, long maxKey) {
    int fromIndex = rank(minKey);
    int maxIndex = indexOf(maxKey);
    int toIndex = maxIndex >= 0 ? maxIndex + 1 : -maxIndex - 1;
    return Math.max(toIndex - fromIndex, 0);
  }

  /**
   * Returns an iterator over the keys in the list that are greater than or equal to {@code minKey}
   * (inclusive), and less than or equal to {@code maxKey} (inclusive).
   */
  public Iterator<LongCursor> rangeIterator(long minKey, long maxKey) {
    int fromIndex = rank(minKey);
    return new RangeIterator(keys, fromIndex, maxKey);
  }

  /**
   * Applies {@code procedure} to the keys in the list that are greater than or equal to {@code
   * minKey} (inclusive), and less than or equal to {@code maxKey} (inclusive).
   */
  public <T extends LongProcedure> T forEachInRange(T procedure, long minKey, long maxKey) {
    final long[] buffer = keys.buffer;
    long k;
    for (int i = rank(minKey), size = keys.size(); i < size && (k = buffer[i]) <= maxKey; i++) {
      procedure.apply(k);
    }
    return procedure;
  }

  /**
   * Estimates the allocated memory. It does not count the memory for the list of keys, only for the
   * index itself.
   */
  @Override
  public long ramBytesAllocated() {
    // int: size, epsilon, epsilonRecursive
    // long: firstKey, lastKey
    return RamUsageEstimator.NUM_BYTES_OBJECT_HEADER
        + 3 * Integer.BYTES
        + 2L * KEY_SIZE * Integer.BYTES
        // + keys.ramBytesAllocated()
        + RamUsageEstimator.shallowSizeOfArray(levelOffsets)
        + RamUsageEstimator.shallowSizeOfArray(segmentData);
  }

  /**
   * Estimates the bytes that are actually used. It does not count the memory for the list of keys,
   * only for the index itself.
   */
  @Override
  public long ramBytesUsed() {
    // int: size, epsilon, epsilonRecursive
    // long: firstKey, lastKey
    return RamUsageEstimator.NUM_BYTES_OBJECT_HEADER
        + 3 * Integer.BYTES
        + 2L * KEY_SIZE * Integer.BYTES
        // + keys.ramBytesUsed()
        + RamUsageEstimator.shallowSizeOfArray(levelOffsets)
        + RamUsageEstimator.shallowSizeOfArray(segmentData);
  }

  /**
   * Finds the segment responsible for a given key, that is, the rightmost segment having its first
   * key <= the searched key.
   *
   * @return the segment data index; or -1 if none.
   */
  private int findSegment(long key) {
    assert key >= firstKey && key <= lastKey;
    final int epsilonRecursive = this.epsilonRecursive;
    final int[] levelOffsets = this.levelOffsets;
    final int[] segmentData = this.segmentData;
    int level = levelOffsets.length - 1;
    int segmentDataIndex = levelOffsets[level] * SEGMENT_DATA_SIZE;
    while (--level >= 0) {
      int nextIntercept = (int) getIntercept(segmentDataIndex + SEGMENT_DATA_SIZE, segmentData);
      int index = Math.min(approximateIndex(key, segmentDataIndex, segmentData), nextIntercept);
      assert index >= 0 && index <= levelOffsets[level + 1] - levelOffsets[level] - 1;
      int sdIndex = (levelOffsets[level] + index) * SEGMENT_DATA_SIZE;
      if (getKey(sdIndex, segmentData) <= key) {
        // Scan sequentially segments after the approximated index, within the epsilon range.
        final int levelNumSegments = levelOffsets[level + 1] - levelOffsets[level] - 1;
        final int toIndex = Math.min(index + epsilonRecursive + 3, levelNumSegments);
        while (index++ < toIndex && getKey(sdIndex + SEGMENT_DATA_SIZE, segmentData) <= key) {
          sdIndex += SEGMENT_DATA_SIZE;
        }
      } else {
        // Scan sequentially segments before the approximated index, within the epsilon range.
        final int fromIndex = Math.max(index - epsilonRecursive - 1, 0);
        while (index-- > fromIndex) {
          sdIndex -= SEGMENT_DATA_SIZE;
          if (getKey(sdIndex, segmentData) <= key) {
            break;
          }
        }
      }
      segmentDataIndex = sdIndex;
    }
    assert segmentDataIndex >= 0;
    return segmentDataIndex;
  }

  private int approximateIndex(long key, int segmentDataIndex, int[] segmentData) {
    long intercept = getIntercept(segmentDataIndex, segmentData);
    long sKey = getKey(segmentDataIndex, segmentData);
    double slope = getSlope(segmentDataIndex, segmentData);
    int index = (int) (slope * ((double) key - sKey) + intercept);
    return Math.max(index, 0);
  }

  private static long getIntercept(int segmentDataIndex, int[] segmentData) {
    return PgmIndexUtil.getIntercept(segmentDataIndex, segmentData, KEY_SIZE);
  }

  private long getKey(int segmentDataIndex, int[] segmentData) {
    return PgmIndexUtil.getKey(segmentDataIndex + KEY_SIZE, segmentData, 0L);
  }

  private static double getSlope(int segmentDataIndex, int[] segmentData) {
    return PgmIndexUtil.getSlope(segmentDataIndex + DOUBLE_KEY_SIZE, segmentData, KEY_SIZE);
  }

  /** Empty immutable PGM Index. */
  private static class LongEmptyPgmIndex extends LongPgmIndex {

    private final Iterator<LongCursor> emptyIterator = new LongEmptyIterator();

    @Override
    public int indexOf(long key) {
      return -1;
    }

    @Override
    public Iterator<LongCursor> rangeIterator(long minKey, long maxKey) {
      return emptyIterator;
    }

    @Override
    public <T extends LongProcedure> T forEachInRange(T procedure, long minKey, long maxKey) {
      return procedure;
    }

    private static class LongEmptyIterator extends AbstractIterator<LongCursor> {
      @Override
      protected LongCursor fetch() {
        return done();
      }
    }
  }

  /** Iterator over a range of elements in a sorted array. */
  protected static class RangeIterator extends AbstractIterator<LongCursor> {
    private final long[] buffer;
    private final int size;
    private final LongCursor cursor;
    private final long maxKey;

    /** Range iterator from {@code fromIndex} (inclusive) to {@code maxKey} (inclusive). */
    protected RangeIterator(LongArrayList keys, int fromIndex, long maxKey) {
      this.buffer = keys.buffer;
      this.size = keys.size();
      this.cursor = new LongCursor();
      this.cursor.index = fromIndex;
      this.maxKey = maxKey;
    }

    @Override
    protected LongCursor fetch() {
      if (cursor.index >= size) {
        return done();
      }
      cursor.value = buffer[cursor.index++];
      if (cursor.value > maxKey) {
        cursor.index = size;
        return done();
      }
      return cursor;
    }
  }

  /** Builds a {@link LongPgmIndex} on a provided sorted list of keys. */
  public static class LongBuilder implements PlaModel.SegmentConsumer, Accountable {

    protected LongArrayList keys;
    protected int epsilon = EPSILON;
    protected int epsilonRecursive = EPSILON_RECURSIVE;
    protected PlaModel plam;
    protected int size;
    protected IntArrayList segmentData;
    protected int numSegments;

    /** Sets the sorted list of keys to build the index for; duplicate elements are allowed. */
    public LongBuilder setSortedKeys(LongArrayList keys) {
      this.keys = keys;
      return this;
    }

    /** Sets the sorted array of keys to build the index for; duplicate elements are allowed. */
    public LongBuilder setSortedKeys(long[] keys, int length) {
      LongArrayList keyList = new LongArrayList(0);
      keyList.buffer = keys;
      keyList.elementsCount = length;
      return setSortedKeys(keyList);
    }

    /** Sets the epsilon range to use when learning the segments for the list of keys. */
    public LongBuilder setEpsilon(int epsilon) {
      if (epsilon <= 0) {
        throw new IllegalArgumentException("epsilon must be > 0");
      }
      this.epsilon = epsilon;
      return this;
    }

    /**
     * Sets the recursive epsilon range to use when learning the segments for the segment levels.
     */
    public LongBuilder setEpsilonRecursive(int epsilonRecursive) {
      if (epsilonRecursive <= 0) {
        throw new IllegalArgumentException("epsilonRecursive must be > 0");
      }
      this.epsilonRecursive = epsilonRecursive;
      return this;
    }

    /** Builds the {@link LongPgmIndex}; or {@link #EMPTY} if there are no keys in the list. */
    public LongPgmIndex build() {
      if (keys == null || keys.size() == 0) {
        return (LongPgmIndex) EMPTY;
      }
      plam = new PlaModel(epsilon);

      int segmentsInitialCapacity =
          Math.min(
              Math.max(keys.size() / (2 * epsilon * epsilon) * SEGMENT_DATA_SIZE, 16), 1 << 19);
      segmentData = new IntArrayList(segmentsInitialCapacity);
      IntArrayList levelOffsets = new IntArrayList(16);

      int levelOffset = 0;
      levelOffsets.add(levelOffset);
      int levelNumSegments = buildFirstLevel();
      while (levelNumSegments > 1) {
        int nextLevelOffset = numSegments;
        levelOffsets.add(nextLevelOffset);
        levelNumSegments = buildUpperLevel(levelOffset, levelNumSegments);
        levelOffset = nextLevelOffset;
      }

      int[] segmentDataFinal = segmentData.toArray();
      int[] levelOffsetsFinal = levelOffsets.toArray();
      return new LongPgmIndex(
          keys, size, epsilon, epsilonRecursive, levelOffsetsFinal, segmentDataFinal);
    }

    private int buildFirstLevel() {
      assert numSegments == 0;
      int numKeys = keys.size();
      int size = 0;
      long key = keys.get(0);
      size++;
      plam.addKey(key, 0, this);
      for (int i = 1; i < numKeys; i++) {
        long nextKey = keys.get(i);
        if (!((nextKey) == (key))) {
          key = nextKey;
          plam.addKey(key, i, this);
          size++;
        }
      }
      plam.finish(this);
      addSentinelSegment(numKeys);
      this.size = size;
      return numSegments - 1;
    }

    private int buildUpperLevel(int levelOffset, int levelNumSegments) {
      plam.setEpsilon(epsilonRecursive);
      assert numSegments > 0;
      int initialNumSegments = numSegments;
      int segmentDataIndex = levelOffset * SEGMENT_DATA_SIZE;
      long key = getKey(segmentDataIndex, segmentData.buffer);
      plam.addKey(key, 0, this);
      for (int i = 1; i < levelNumSegments; i++) {
        segmentDataIndex += SEGMENT_DATA_SIZE;
        long nextKey = getKey(segmentDataIndex, segmentData.buffer);
        if (!((nextKey) == (key))) {
          key = nextKey;
          plam.addKey(key, i, this);
        }
      }
      plam.finish(this);
      addSentinelSegment(levelNumSegments);
      return numSegments - initialNumSegments - 1;
    }

    private long getKey(int segmentDataIndex, int[] segmentData) {
      return PgmIndexUtil.getKey(segmentDataIndex + KEY_SIZE, segmentData, 0L);
    }

    /**
     * Adds a sentinel segment that is used to give a limit for the position approximation, but does
     * not count in the number of segments per level.
     */
    private void addSentinelSegment(int endIndex) {
      // This sentinel segment is used in findSegment().
      accept(Double.MAX_VALUE, 0d, endIndex);
    }

    @Override
    public void accept(double firstKey, double slope, long intercept) {
      PgmIndexUtil.addIntercept(intercept, segmentData, KEY_SIZE);
      PgmIndexUtil.addKey((long) firstKey, segmentData);
      PgmIndexUtil.addSlope(slope, segmentData, KEY_SIZE);
      numSegments++;
      assert segmentData.size() == numSegments * SEGMENT_DATA_SIZE;
    }

    /**
     * Estimates the allocated memory. It does not count the memory for the list of keys, only for
     * the builder itself.
     */
    @Override
    public long ramBytesAllocated() {
      // int: epsilon, epsilonRecursive, size, numSegments
      return RamUsageEstimator.NUM_BYTES_OBJECT_HEADER
          + 4 * Integer.BYTES
          // + keys.ramBytesAllocated()
          + plam.ramBytesAllocated()
          + segmentData.ramBytesAllocated();
    }

    /**
     * Estimates the bytes that are actually used. It does not count the memory for the list of
     * keys, only for the builder itself.
     */
    @Override
    public long ramBytesUsed() {
      // int: epsilon, epsilonRecursive, size, numSegments
      return RamUsageEstimator.NUM_BYTES_OBJECT_HEADER
          + 4 * Integer.BYTES
          // + keys.ramBytesUsed()
          + plam.ramBytesUsed()
          + segmentData.ramBytesUsed();
    }
  }
}
