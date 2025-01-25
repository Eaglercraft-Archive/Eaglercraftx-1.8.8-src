
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  15 : 17  @  15

+ 	private int alfheim$lightRefCount = -1;
+ 

> CHANGE  46 : 59  @  46 : 47

~ 		if (blockRefCount != 0)
~ 			return false;
~ 
~ 		// -1 indicates the lightRefCount needs to be re-calculated
~ 		if (alfheim$lightRefCount == -1) {
~ 			if (alfheim$checkLightArrayEqual(skylightArray, (byte) 255)
~ 					&& alfheim$checkLightArrayEqual(blocklightArray, (byte) 0))
~ 				alfheim$lightRefCount = 0; // Lighting is trivial, don't send to clients
~ 			else
~ 				alfheim$lightRefCount = 1; // Lighting is not trivial, send to clients
~ 		}
~ 
~ 		return alfheim$lightRefCount == 0;

> INSERT  12 : 13  @  12

+ 		alfheim$lightRefCount = -1;

> INSERT  8 : 9  @  8

+ 		alfheim$lightRefCount = -1;

> INSERT  44 : 45  @  44

+ 		alfheim$lightRefCount = -1;

> INSERT  4 : 5  @  4

+ 		alfheim$lightRefCount = -1;

> INSERT  1 : 12  @  1

+ 
+ 	private boolean alfheim$checkLightArrayEqual(final NibbleArray storage, final byte targetValue) {
+ 		if (storage == null)
+ 			return true;
+ 
+ 		for (final byte currentByte : storage.getData())
+ 			if (currentByte != targetValue)
+ 				return false;
+ 
+ 		return true;
+ 	}

> EOF
