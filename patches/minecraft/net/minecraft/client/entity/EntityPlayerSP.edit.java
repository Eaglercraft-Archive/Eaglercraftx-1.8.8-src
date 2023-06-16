
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  5  @  5 : 6

> DELETE  51  @  51 : 52

> INSERT  22 : 23  @  22

+ 	private StatFileWriter statWriter;

> CHANGE  1 : 2  @  1 : 2

~ 	public EntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statWriter) {

> DELETE  2  @  2 : 3

> INSERT  2 : 3  @  2

+ 		this.statWriter = statWriter;

> EOF
