
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> DELETE  3  @  3 : 4

> CHANGE  2 : 10  @  2 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.carrotsearch.hppc.IntIntHashMap;
~ import com.carrotsearch.hppc.IntIntMap;
~ import com.carrotsearch.hppc.cursors.IntCursor;
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ 

> CHANGE  13 : 14  @  13 : 14

~ 	private static final EaglercraftRandom enchantmentRand = new EaglercraftRandom();

> CHANGE  26 : 28  @  26 : 28

~ 	public static IntIntMap getEnchantments(ItemStack stack) {
~ 		IntIntHashMap linkedhashmap = new IntIntHashMap();

> CHANGE  6 : 7  @  6 : 7

~ 				linkedhashmap.put(short1, short2);

> CHANGE  6 : 7  @  6 : 7

~ 	public static void setEnchantments(IntIntMap enchMap, ItemStack stack) {

> DELETE  1  @  1 : 2

> CHANGE  1 : 3  @  1 : 3

~ 		for (IntCursor cur : enchMap.keys()) {
~ 			int i = cur.value;

> CHANGE  29 : 31  @  29 : 31

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
