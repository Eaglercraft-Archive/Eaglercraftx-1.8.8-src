
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 7

> CHANGE  7 : 8  @  12 : 15

~ 

> INSERT  10 : 18  @  17

+ import com.google.common.base.Joiner;
+ import com.google.common.base.Predicate;
+ import com.google.common.base.Predicates;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 
+ import net.minecraft.block.state.BlockWorldState;
+ 

> CHANGE  30 : 31  @  29 : 30

~ 		if (aisle.length > 0 && !StringUtils.isEmpty(aisle[0])) {

> CHANGE  49 : 50  @  48 : 49

~ 							this.symbolMap.put(Character.valueOf(c0), (Predicate<BlockWorldState>) null);

> EOF
