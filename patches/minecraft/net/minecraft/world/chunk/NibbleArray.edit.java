
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  29 : 31  @  29 : 31

~ 		int shift = (index & 1) << 2;
~ 		return data[index >> 1] >> shift & 15;

> CHANGE  3 : 6  @  3 : 10

~ 		int i = index >> 1;
~ 		int shift = (index & 1) << 2;
~ 		data[i] = (byte) (data[i] & ~(15 << shift) | (value & 15) << shift);

> EOF
