
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 5  @  4 : 5

~ import net.minecraft.nbt.NBTTagCompound;

> INSERT  27 : 33  @  27

+ 		this.addGameRule("loadSpawnChunks", "false", GameRules.ValueType.BOOLEAN_VALUE);
+ 		this.addGameRule("bedSpawnPoint", "true", GameRules.ValueType.BOOLEAN_VALUE);
+ 		this.addGameRule("clickToRide", "false", GameRules.ValueType.BOOLEAN_VALUE);
+ 		this.addGameRule("clickToSit", "true", GameRules.ValueType.BOOLEAN_VALUE);
+ 		this.addGameRule("colorCodes", "true", GameRules.ValueType.BOOLEAN_VALUE);
+ 		this.addGameRule("doSignEditing", "true", GameRules.ValueType.BOOLEAN_VALUE);

> EOF
