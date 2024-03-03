
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  6 : 7  @  6 : 7

~ 	private static final EnumDifficulty[] difficultyEnums = new EnumDifficulty[4];

> CHANGE  21 : 24  @  21 : 23

~ 		EnumDifficulty[] types = values();
~ 		for (int i = 0; i < types.length; ++i) {
~ 			difficultyEnums[types[i].difficultyId] = types[i];

> EOF
