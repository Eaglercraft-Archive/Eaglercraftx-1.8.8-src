
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import com.carrotsearch.hppc.ObjectContainer;
+ import com.carrotsearch.hppc.cursors.ObjectCursor;

> INSERT  4 : 5  @  4

+ 

> CHANGE  5 : 6  @  5 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> DELETE  1  @  1 : 9

> INSERT  10 : 11  @  10

+ 

> CHANGE  128 : 129  @  128 : 129

~ 						.getPlayerByUUID(EaglercraftUUID.fromString(username));

> CHANGE  31 : 32  @  31 : 32

~ 				EaglercraftUUID uuid = EaglercraftUUID.fromString(parString1);

> INSERT  317 : 333  @  317

+ 	public static List<String> getListOfStringsMatchingLastWord(String[] parArrayOfString,
+ 			ObjectContainer<String> parCollection) {
+ 		String s = parArrayOfString[parArrayOfString.length - 1];
+ 		ArrayList arraylist = Lists.newArrayList();
+ 		if (!parCollection.isEmpty()) {
+ 			for (ObjectCursor<String> s1_ : parCollection) {
+ 				String s1 = s1_.value;
+ 				if (doesStringStartWith(s, s1)) {
+ 					arraylist.add(s1);
+ 				}
+ 			}
+ 		}
+ 
+ 		return arraylist;
+ 	}
+ 

> EOF
