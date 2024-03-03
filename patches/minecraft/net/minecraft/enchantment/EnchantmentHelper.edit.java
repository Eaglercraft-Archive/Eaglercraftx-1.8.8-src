
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  6 : 11  @  6 : 9

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ 

> CHANGE  13 : 14  @  13 : 14

~ 	private static final EaglercraftRandom enchantmentRand = new EaglercraftRandom();

> CHANGE  76 : 78  @  76 : 78

~ 			for (int k = 0; k < stacks.length; ++k) {
~ 				int j = getEnchantmentLevel(enchID, stacks[k]);

> CHANGE  26 : 28  @  26 : 28

~ 		for (int k = 0; k < stacks.length; ++k) {
~ 			applyEnchantmentModifier(modifier, stacks[k]);

> CHANGE  96 : 99  @  96 : 97

~ 		ItemStack[] stacks = parEntityLivingBase.getInventory();
~ 		for (int k = 0; k < stacks.length; ++k) {
~ 			ItemStack itemstack = stacks[k];

> CHANGE  8 : 10  @  8 : 9

~ 	public static int calcItemStackEnchantability(EaglercraftRandom parRandom, int parInt1, int parInt2,
~ 			ItemStack parItemStack) {

> CHANGE  14 : 16  @  14 : 16

~ 	public static ItemStack addRandomEnchantment(EaglercraftRandom parRandom, ItemStack parItemStack, int parInt1) {
~ 		List<EnchantmentData> list = buildEnchantmentList(parRandom, parItemStack, parInt1);

> CHANGE  6 : 8  @  6 : 7

~ 			for (int i = 0, l = list.size(); i < l; ++i) {
~ 				EnchantmentData enchantmentdata = list.get(i);

> CHANGE  11 : 13  @  11 : 12

~ 	public static List<EnchantmentData> buildEnchantmentList(EaglercraftRandom randomIn, ItemStack itemStackIn,
~ 			int parInt1) {

> CHANGE  14 : 15  @  14 : 15

~ 			ArrayList<EnchantmentData> arraylist = null;

> CHANGE  15 : 17  @  15 : 16

~ 							for (int m = 0, n = arraylist.size(); m < n; ++m) {
~ 								EnchantmentData enchantmentdata1 = arraylist.get(m);

> CHANGE  30 : 32  @  30 : 31

~ 		for (int j = 0; j < Enchantment.enchantmentsBookList.length; ++j) {
~ 			Enchantment enchantment = Enchantment.enchantmentsBookList[j];

> EOF
