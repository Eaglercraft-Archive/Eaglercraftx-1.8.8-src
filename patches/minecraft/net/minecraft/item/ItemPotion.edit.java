
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  5 : 11  @  5

+ import java.util.Set;
+ 
+ import com.google.common.collect.HashMultimap;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 

> DELETE  6  @  6 : 9

> CHANGE  62 : 63  @  62 : 63

~ 			List<PotionEffect> list = this.getEffects(itemstack);

> CHANGE  1 : 3  @  1 : 3

~ 				for (int i = 0, l = list.size(); i < l; ++i) {
~ 					entityplayer.addPotionEffect(new PotionEffect(list.get(i)));

> CHANGE  56 : 57  @  56 : 57

~ 		List<PotionEffect> list = this.getEffects(meta);

> CHANGE  1 : 3  @  1 : 3

~ 			for (int i = 0, l = list.size(); i < l; ++i) {
~ 				if (Potion.potionTypes[list.get(i).getPotionID()].isInstant()) {

> CHANGE  33 : 34  @  33 : 34

~ 			List<PotionEffect> list1 = Items.potionitem.getEffects(itemstack);

> CHANGE  2 : 4  @  2 : 3

~ 				for (int i = 0, l = list1.size(); i < l; ++i) {
~ 					PotionEffect potioneffect = list1.get(i);

> CHANGE  4 : 5  @  4 : 5

~ 						for (Entry entry : (Set<Entry<Object, Object>>) map.entrySet()) {

> CHANGE  33 : 34  @  33 : 34

~ 				for (Entry entry1 : (Set<Entry<Object, Object>>) hashmultimap.entries()) {

> EOF
