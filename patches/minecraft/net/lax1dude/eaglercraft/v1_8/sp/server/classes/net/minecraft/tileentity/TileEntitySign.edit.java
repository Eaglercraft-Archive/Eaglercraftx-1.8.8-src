
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  6 : 7  @  6 : 7

~ import net.minecraft.nbt.NBTTagCompound;

> INSERT  14 : 16  @  14

+ import org.json.JSONException;
+ 

> CHANGE  80 : 81  @  80 : 81

~ 			} catch (JSONException var8) {

> INSERT  80 : 82  @  80

+ 		boolean didSomething = false;
+ 

> INSERT  5 : 6  @  5

+ 					didSomething = true;

> INSERT  6 : 11  @  6

+ 		if (!didSomething && MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
+ 				.getBoolean("doSignEditing")) {
+ 			playerIn.openEditSign(this);
+ 		}
+ 

> EOF
