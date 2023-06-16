
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 7

> CHANGE  5 : 6  @  5 : 8

~ 

> INSERT  2 : 10  @  2

+ import com.google.common.base.Joiner;
+ import com.google.common.base.Predicate;
+ import com.google.common.base.Predicates;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 
+ import net.minecraft.block.state.BlockWorldState;
+ 

> CHANGE  12 : 13  @  12 : 13

~ 		if (aisle.length > 0 && !StringUtils.isEmpty(aisle[0])) {

> CHANGE  18 : 19  @  18 : 19

~ 							this.symbolMap.put(Character.valueOf(c0), (Predicate<BlockWorldState>) null);

> EOF
