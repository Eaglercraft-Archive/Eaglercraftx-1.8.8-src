
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 8

> INSERT  1 : 2  @  1

+ import java.util.HashMap;

> CHANGE  3 : 4  @  3 : 6

~ 

> INSERT  2 : 9  @  2

+ import com.google.common.base.Joiner;
+ import com.google.common.base.Predicate;
+ import com.google.common.base.Predicates;
+ import com.google.common.collect.Lists;
+ 
+ import net.minecraft.block.state.BlockWorldState;
+ 

> CHANGE  3 : 4  @  3 : 4

~ 	private final Map<Character, Predicate<BlockWorldState>> symbolMap = new HashMap<>();

> CHANGE  4 : 5  @  4 : 5

~ 		this.symbolMap.put(' ', Predicates.alwaysTrue());

> CHANGE  3 : 4  @  3 : 4

~ 		if (!(aisle == null || aisle.length <= 0) && !StringUtils.isEmpty(aisle[0])) {

> CHANGE  9 : 11  @  9 : 10

~ 				for (int i = 0; i < aisle.length; ++i) {
~ 					String s = aisle[i];

> CHANGE  6 : 11  @  6 : 9

~ 					char[] achar = s.toCharArray();
~ 					for (int j = 0; j < achar.length; ++j) {
~ 						char c0 = achar[j];
~ 						if (!this.symbolMap.containsKey(c0)) {
~ 							this.symbolMap.put(c0, null);

> CHANGE  17 : 18  @  17 : 18

~ 		this.symbolMap.put(symbol, blockMatcher);

> CHANGE  9 : 10  @  9 : 11

~ 		Predicate[][][] apredicate = new Predicate[this.depth.size()][this.aisleHeight][this.rowWidth];

> CHANGE  4 : 5  @  4 : 6

~ 					apredicate[i][j][k] = this.symbolMap.get(((String[]) this.depth.get(i))[j].charAt(k));

> CHANGE  10 : 11  @  10 : 11

~ 		for (Entry<Character, Predicate<BlockWorldState>> entry : this.symbolMap.entrySet()) {

> EOF
