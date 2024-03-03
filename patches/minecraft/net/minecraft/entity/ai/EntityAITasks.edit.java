
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  5  @  5 : 6

> CHANGE  1 : 3  @  1 : 3

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  84 : 86  @  84 : 86

~ 		for (int i = 0, l = this.executingTaskEntries.size(); i < l; ++i) {
~ 			this.executingTaskEntries.get(i).action.updateTask();

> CHANGE  11 : 13  @  11 : 12

~ 		for (int i = 0, l = this.taskEntries.size(); i < l; ++i) {
~ 			EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry = this.taskEntries.get(i);

> EOF
