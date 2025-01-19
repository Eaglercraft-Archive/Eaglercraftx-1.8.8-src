
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  14  @  14 : 19

> INSERT  3 : 4  @  3

+ 

> CHANGE  147 : 148  @  147 : 148

~ 		List<Packet> list = this.func_96550_d(parScoreObjective);

> CHANGE  1 : 6  @  1 : 4

~ 		List<EntityPlayerMP> players = this.scoreboardMCServer.getConfigurationManager().func_181057_v();
~ 		for (int i = 0, l = players.size(); i < l; ++i) {
~ 			EntityPlayerMP entityplayermp = players.get(i);
~ 			for (int j = 0, m = list.size(); j < m; ++j) {
~ 				entityplayermp.playerNetServerHandler.sendPacket(list.get(j));

> CHANGE  20 : 21  @  20 : 21

~ 		List<Packet> list = this.func_96548_f(parScoreObjective);

> CHANGE  1 : 6  @  1 : 4

~ 		List<EntityPlayerMP> players = this.scoreboardMCServer.getConfigurationManager().func_181057_v();
~ 		for (int i = 0, l = players.size(); i < l; ++i) {
~ 			EntityPlayerMP entityplayermp = players.get(i);
~ 			for (int j = 0, m = list.size(); j < m; ++j) {
~ 				entityplayermp.playerNetServerHandler.sendPacket(list.get(j));

> EOF
