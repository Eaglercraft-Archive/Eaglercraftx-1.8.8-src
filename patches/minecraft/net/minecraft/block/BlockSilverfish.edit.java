
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  15 : 16  @  15 : 17

~ 	public static PropertyEnum<BlockSilverfish.EnumType> VARIANT;

> CHANGE  8 : 13  @  8 : 9

~ 	public static void bootstrapStates() {
~ 		VARIANT = PropertyEnum.<BlockSilverfish.EnumType>create("variant", BlockSilverfish.EnumType.class);
~ 	}
~ 
~ 	public int quantityDropped(EaglercraftRandom var1) {

> CHANGE  43 : 46  @  43 : 45

~ 		BlockSilverfish.EnumType[] types = BlockSilverfish.EnumType.META_LOOKUP;
~ 		for (int i = 0; i < types.length; ++i) {
~ 			list.add(new ItemStack(item, 1, types[i].getMetadata()));

> CHANGE  52 : 53  @  52 : 53

~ 		public static final BlockSilverfish.EnumType[] META_LOOKUP = new BlockSilverfish.EnumType[6];

> CHANGE  41 : 44  @  41 : 42

~ 			BlockSilverfish.EnumType[] types = BlockSilverfish.EnumType.META_LOOKUP;
~ 			for (int i = 0; i < types.length; ++i) {
~ 				BlockSilverfish.EnumType blocksilverfish$enumtype = types[i];

> CHANGE  9 : 12  @  9 : 11

~ 			BlockSilverfish.EnumType[] types = BlockSilverfish.EnumType.values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				META_LOOKUP[types[i].getMetadata()] = types[i];

> EOF
