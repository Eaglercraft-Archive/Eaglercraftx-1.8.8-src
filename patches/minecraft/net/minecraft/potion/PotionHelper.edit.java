
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> DELETE  3  @  3 : 7

> INSERT  1 : 9  @  1

+ import com.carrotsearch.hppc.IntIntHashMap;
+ import com.carrotsearch.hppc.IntIntMap;
+ import com.carrotsearch.hppc.IntObjectHashMap;
+ import com.carrotsearch.hppc.IntObjectMap;
+ import com.carrotsearch.hppc.ObjectContainer;
+ import com.carrotsearch.hppc.cursors.ObjectCursor;
+ import com.google.common.collect.Lists;
+ 

> CHANGE  15 : 18  @  15 : 18

~ 	private static final IntObjectMap<String> potionRequirements = new IntObjectHashMap<>();
~ 	private static final IntObjectMap<String> potionAmplifiers = new IntObjectHashMap<>();
~ 	private static final IntIntMap DATAVALUE_COLORS = new IntIntHashMap();

> INSERT  60 : 95  @  60

+ 	public static int calcPotionLiquidColor(ObjectContainer<PotionEffect> parCollection) {
+ 		int i = 3694022;
+ 		if (parCollection != null && !parCollection.isEmpty()) {
+ 			float f = 0.0F;
+ 			float f1 = 0.0F;
+ 			float f2 = 0.0F;
+ 			float f3 = 0.0F;
+ 
+ 			for (ObjectCursor<PotionEffect> potioneffect_ : parCollection) {
+ 				PotionEffect potioneffect = potioneffect_.value;
+ 				if (potioneffect.getIsShowParticles()) {
+ 					int j = Potion.potionTypes[potioneffect.getPotionID()].getLiquidColor();
+ 
+ 					for (int k = 0; k <= potioneffect.getAmplifier(); ++k) {
+ 						f += (float) (j >> 16 & 255) / 255.0F;
+ 						f1 += (float) (j >> 8 & 255) / 255.0F;
+ 						f2 += (float) (j >> 0 & 255) / 255.0F;
+ 						++f3;
+ 					}
+ 				}
+ 			}
+ 
+ 			if (f3 == 0.0F) {
+ 				return 0;
+ 			} else {
+ 				f = f / f3 * 255.0F;
+ 				f1 = f1 / f3 * 255.0F;
+ 				f2 = f2 / f3 * 255.0F;
+ 				return (int) f << 16 | (int) f1 << 8 | (int) f2;
+ 			}
+ 		} else {
+ 			return i;
+ 		}
+ 	}
+ 

> INSERT  10 : 20  @  10

+ 	public static boolean getAreAmbient(ObjectContainer<PotionEffect> potionEffects) {
+ 		for (ObjectCursor<PotionEffect> potioneffect : potionEffects) {
+ 			if (!potioneffect.value.getIsAmbient()) {
+ 				return false;
+ 			}
+ 		}
+ 
+ 		return true;
+ 	}
+ 

> DELETE  1  @  1 : 2

> CHANGE  1 : 4  @  1 : 3

~ 			int i = DATAVALUE_COLORS.getOrDefault(dataValue, -1);
~ 			if (i != -1) {
~ 				return i;

> CHANGE  1 : 3  @  1 : 3

~ 				i = calcPotionLiquidColor(getPotionEffects(dataValue, false));
~ 				DATAVALUE_COLORS.put(dataValue, i);

> CHANGE  3 : 4  @  3 : 4

~ 			return calcPotionLiquidColor(getPotionEffects(dataValue, true));

> CHANGE  168 : 170  @  168 : 169

~ 		for (int k = 0; k < Potion.potionTypes.length; ++k) {
~ 			Potion potion = Potion.potionTypes[k];

> CHANGE  1 : 2  @  1 : 2

~ 				String s = potionRequirements.get(potion.getId());

> CHANGE  4 : 5  @  4 : 5

~ 						String s1 = potionAmplifiers.get(potion.getId());

> EOF
