
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

> CHANGE  4 : 5  @  4 : 7

~ import net.minecraft.entity.player.EntityPlayerMP;

> INSERT  4 : 8  @  4

+ import java.util.List;
+ 
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

> CHANGE  16 : 19  @  16 : 17

~ 			List<EntityPlayerMP> players = minecraftserver.getConfigurationManager().func_181057_v();
~ 			for (int i = 0, l = players.size(); i < l; ++i) {
~ 				EntityPlayerMP entityplayer = players.get(i);

> CHANGE  3 : 4  @  3 : 9

~ 					entityplayer.addChatMessage(chatcomponenttranslation);

> EOF
