
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  5  @  5 : 6

> DELETE  56  @  57 : 58

> INSERT  78 : 79  @  80

+ 	private StatFileWriter statWriter;

> CHANGE  80 : 81  @  81 : 82

~ 	public EntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statWriter) {

> DELETE  83  @  84 : 85

> INSERT  85 : 86  @  87

+ 		this.statWriter = statWriter;

> EOF
