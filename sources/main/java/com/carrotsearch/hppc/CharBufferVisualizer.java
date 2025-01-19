package com.carrotsearch.hppc;

/**
 * Reused buffer visualization routines.
 *
 * @see CharSet#visualizeKeyDistribution(int)
 * @see CharVTypeMap#visualizeKeyDistribution(int)
 */
class CharBufferVisualizer {
  static String visualizeKeyDistribution(char[] buffer, int max, int characters) {
    final StringBuilder b = new StringBuilder();
    final char[] chars = ".123456789X".toCharArray();
    for (int i = 1, start = -1; i <= characters; i++) {
      int end = (int) ((long) i * max / characters);

      if (start + 1 <= end) {
        int taken = 0;
        int slots = 0;
        for (int slot = start + 1; slot <= end; slot++, slots++) {
          if (!((buffer[slot]) == 0)) {
            taken++;
          }
        }
        b.append(chars[Math.min(chars.length - 1, taken * chars.length / slots)]);
        start = end;
      }
    }
    while (b.length() < characters) {
      b.append(' ');
    }
    return b.toString();
  }
}
