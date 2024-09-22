
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  7 : 9  @  7

+ import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
+ 

> CHANGE  1 : 2  @  1 : 2

~ 	private static final long demoWorldSeed = EaglercraftVersion.demoWorldSeed;

> CHANGE  3 : 5  @  3 : 6

~ 	public DemoWorldServer(MinecraftServer server, ISaveHandler saveHandlerIn, WorldInfo worldInfoIn, int dimensionId) {
~ 		super(server, saveHandlerIn, worldInfoIn, dimensionId);

> EOF
