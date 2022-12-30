
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  5  @  5 : 6

> DELETE  51  @  52 : 53

> INSERT  22 : 23  @  23

+ 	private StatFileWriter statWriter;

> CHANGE  2 : 3  @  1 : 2

~ 	public EntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statWriter) {

> DELETE  3  @  3 : 4

> INSERT  2 : 3  @  3

+ 		this.statWriter = statWriter;

> EOF
