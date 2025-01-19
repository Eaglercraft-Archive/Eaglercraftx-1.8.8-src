
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 10  @  2

+ import java.util.ArrayList;
+ import java.util.List;
+ 
+ import org.apache.commons.lang3.StringUtils;
+ 
+ import com.carrotsearch.hppc.CharObjectHashMap;
+ import com.carrotsearch.hppc.CharObjectMap;
+ import com.carrotsearch.hppc.cursors.CharObjectCursor;

> CHANGE  4 : 5  @  4 : 10

~ 

> DELETE  1  @  1 : 4

> CHANGE  4 : 5  @  4 : 5

~ 	private final CharObjectMap<Predicate<BlockWorldState>> symbolMap = new CharObjectHashMap<>();

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

> CHANGE  10 : 13  @  10 : 13

~ 		for (CharObjectCursor<Predicate<BlockWorldState>> entry : this.symbolMap) {
~ 			if (entry.value == null) {
~ 				arraylist.add(entry.key);

> EOF
