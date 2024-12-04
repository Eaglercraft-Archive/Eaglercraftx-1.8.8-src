
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> DELETE  2  @  2 : 5

> CHANGE  8 : 10  @  8 : 11

~ 	public WorldServerMulti(MinecraftServer server, ISaveHandler saveHandlerIn, int dimensionId, WorldServer delegate) {
~ 		super(server, saveHandlerIn, new DerivedWorldInfo(delegate.getWorldInfo()), dimensionId);

> DELETE  32  @  32 : 35

> EOF
