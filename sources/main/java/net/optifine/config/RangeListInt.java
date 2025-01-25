package net.optifine.config;

public class RangeListInt {
	private RangeInt[] ranges = new RangeInt[0];

	public RangeListInt() {
	}

	public RangeListInt(RangeInt ri) {
		this.addRange(ri);
	}

	public void addRange(RangeInt ri) {
		RangeInt[] newRanges = new RangeInt[ranges.length + 1];
		System.arraycopy(ranges, 0, newRanges, 0, ranges.length);
		newRanges[ranges.length] = ri;
		this.ranges = newRanges;
	}

	public boolean isInRange(int val) {
		for (int i = 0; i < this.ranges.length; ++i) {
			RangeInt rangeint = this.ranges[i];

			if (rangeint.isInRange(val)) {
				return true;
			}
		}

		return false;
	}

	public int getCountRanges() {
		return this.ranges.length;
	}

	public RangeInt getRange(int i) {
		return this.ranges[i];
	}

	public String toString() {
		StringBuffer stringbuffer = new StringBuffer();
		stringbuffer.append("[");

		for (int i = 0; i < this.ranges.length; ++i) {
			RangeInt rangeint = this.ranges[i];

			if (i > 0) {
				stringbuffer.append(", ");
			}

			stringbuffer.append(rangeint.toString());
		}

		stringbuffer.append("]");
		return stringbuffer.toString();
	}
}
