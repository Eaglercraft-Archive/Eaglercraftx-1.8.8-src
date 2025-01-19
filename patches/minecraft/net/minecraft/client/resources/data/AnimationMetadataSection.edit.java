
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  1 : 3  @  1 : 4

~ import com.carrotsearch.hppc.IntHashSet;
~ import com.carrotsearch.hppc.IntSet;

> CHANGE  54 : 56  @  54 : 56

~ 	public IntSet getFrameIndexSet() {
~ 		IntHashSet hashset = new IntHashSet();

> CHANGE  1 : 3  @  1 : 3

~ 		for (int i = 0, l = this.animationFrames.size(); i < l; ++i) {
~ 			hashset.add(this.animationFrames.get(i).getFrameIndex());

> EOF
