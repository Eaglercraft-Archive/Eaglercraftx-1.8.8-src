
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  6  @  6 : 9

> INSERT  1 : 5  @  1

+ import java.util.List;
+ 
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ 

> CHANGE  24 : 25  @  24 : 25

~ 		if (minecraftserver != null && StringUtils.isNullOrEmpty(this.value)) {

> CHANGE  4 : 5  @  4 : 5

~ 				this.setValue(HString.format("%d", new Object[] { Integer.valueOf(score.getScorePoints()) }));

> CHANGE  13 : 16  @  13 : 15

~ 		List<IChatComponent> lst = this.getSiblings();
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			chatcomponentscore.appendSibling(lst.get(i).createCopy());

> EOF
