
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  7  @  7 : 9

> CHANGE  7 : 10  @  7 : 8

~ 		EnumDyeColor[] colors = EnumDyeColor.META_LOOKUP;
~ 		for (int i = 0; i < colors.length; ++i) {
~ 			EnumDyeColor enumdyecolor = colors[i];

> CHANGE  104 : 107  @  104 : 106

~ 			TileEntityBanner.EnumBannerPattern[] patterns = TileEntityBanner.EnumBannerPattern._VALUES;
~ 			for (int m = 0; m < patterns.length; ++m) {
~ 				TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern = patterns[m];

> EOF
