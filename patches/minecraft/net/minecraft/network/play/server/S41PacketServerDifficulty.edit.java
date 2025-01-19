
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 4  @  3

+ 

> CHANGE  22 : 25  @  22 : 23

~ 		int i = parPacketBuffer.readUnsignedByte();
~ 		this.difficulty = EnumDifficulty.getDifficultyEnum(i & 3);
~ 		this.difficultyLocked = (i & 4) != 0;

> CHANGE  3 : 4  @  3 : 4

~ 		parPacketBuffer.writeByte(this.difficulty.getDifficultyId() | (this.difficultyLocked ? 4 : 0));

> EOF
