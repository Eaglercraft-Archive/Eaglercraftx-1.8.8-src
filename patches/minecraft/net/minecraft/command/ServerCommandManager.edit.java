
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 40

> DELETE  1  @  1 : 3

> DELETE  2  @  2 : 3

> DELETE  1  @  1 : 2

> DELETE  3  @  3 : 10

> DELETE  3  @  3 : 4

> DELETE  4  @  4 : 5

> DELETE  1  @  1 : 2

> INSERT  4 : 6  @  4

+ import net.lax1dude.eaglercraft.v1_8.sp.server.ClientCommandDummy;
+ 

> INSERT  1 : 2  @  1

+ 

> DELETE  19  @  19 : 20

> CHANGE  24 : 28  @  24 : 44

~ 		this.registerCommand(new CommandServerKick());
~ 		this.registerCommand(new CommandListPlayers());
~ 		this.registerCommand(new CommandSetPlayerTimeout());
~ 		this.registerCommand(new ClientCommandDummy("eagskull", 2, "command.skull.usage"));

> CHANGE  20 : 21  @  20 : 26

~ 					entityplayer.addChatMessage(chatcomponenttranslation);

> EOF
