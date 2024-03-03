
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 6  @  2 : 3

~ import java.util.Arrays;
~ import java.util.List;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  77 : 78  @  77 : 229

~ 	private static List<List<List<List<EntityVillager.ITradeList>>>> DEFAULT_TRADE_LIST_MAP = null;

> INSERT  1 : 206  @  1

+ 	public static void bootstrap() {
+ 		DEFAULT_TRADE_LIST_MAP = Arrays.asList(
+ 				Arrays.asList(Arrays.asList(Arrays.asList(
+ 						new EntityVillager.EmeraldForItems(Items.wheat, new EntityVillager.PriceInfo(18, 22)),
+ 						new EntityVillager.EmeraldForItems(Items.potato, new EntityVillager.PriceInfo(15, 19)),
+ 						new EntityVillager.EmeraldForItems(Items.carrot,
+ 								new EntityVillager.PriceInfo(15, 19)),
+ 						new EntityVillager.ListItemForEmeralds(Items.bread, new EntityVillager.PriceInfo(-4, -2))),
+ 						Arrays.asList(
+ 								new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.pumpkin),
+ 										new EntityVillager.PriceInfo(8, 13)),
+ 								new EntityVillager.ListItemForEmeralds(Items.pumpkin_pie,
+ 										new EntityVillager.PriceInfo(-3, -2))),
+ 						Arrays.asList(
+ 								new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.melon_block),
+ 										new EntityVillager.PriceInfo(7, 12)),
+ 								new EntityVillager.ListItemForEmeralds(Items.apple,
+ 										new EntityVillager.PriceInfo(-5, -7))),
+ 						Arrays.asList(
+ 								new EntityVillager.ListItemForEmeralds(Items.cookie,
+ 										new EntityVillager.PriceInfo(-6, -10)),
+ 								new EntityVillager.ListItemForEmeralds(Items.cake,
+ 										new EntityVillager.PriceInfo(1, 1)))),
+ 						Arrays.asList(Arrays.asList(
+ 								new EntityVillager.EmeraldForItems(Items.string, new EntityVillager.PriceInfo(15, 20)),
+ 								new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)),
+ 								new EntityVillager.ItemAndEmeraldToItem(Items.fish, new EntityVillager.PriceInfo(6, 6),
+ 										Items.cooked_fish, new EntityVillager.PriceInfo(6, 6))),
+ 								Arrays.asList(new EntityVillager.ListEnchantedItemForEmeralds(Items.fishing_rod,
+ 										new EntityVillager.PriceInfo(7, 8)))),
+ 						Arrays.asList(
+ 								Arrays.asList(
+ 										new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.wool),
+ 												new EntityVillager.PriceInfo(16, 22)),
+ 										new EntityVillager.ListItemForEmeralds(Items.shears,
+ 												new EntityVillager.PriceInfo(3, 4))),
+ 								Arrays.asList(
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 0),
+ 												new EntityVillager.PriceInfo(1, 2)),
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 1),
+ 												new EntityVillager.PriceInfo(1, 2)),
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 2),
+ 												new EntityVillager.PriceInfo(1, 2)),
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 3),
+ 												new EntityVillager.PriceInfo(1, 2)),
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 4),
+ 												new EntityVillager.PriceInfo(1, 2)),
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 5),
+ 												new EntityVillager.PriceInfo(1, 2)),
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 6),
+ 												new EntityVillager.PriceInfo(1, 2)),
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 7),
+ 												new EntityVillager.PriceInfo(1, 2)),
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 8),
+ 												new EntityVillager.PriceInfo(1, 2)),
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 9),
+ 												new EntityVillager.PriceInfo(1, 2)),
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 10),
+ 												new EntityVillager.PriceInfo(1, 2)),
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 11),
+ 												new EntityVillager.PriceInfo(1, 2)),
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 12),
+ 												new EntityVillager.PriceInfo(1, 2)),
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 13),
+ 												new EntityVillager.PriceInfo(1, 2)),
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 14),
+ 												new EntityVillager.PriceInfo(1, 2)),
+ 										new EntityVillager.ListItemForEmeralds(
+ 												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 15),
+ 												new EntityVillager.PriceInfo(1, 2)))),
+ 						Arrays.asList(Arrays.asList(
+ 								new EntityVillager.EmeraldForItems(Items.string, new EntityVillager.PriceInfo(15, 20)),
+ 								new EntityVillager.ListItemForEmeralds(Items.arrow,
+ 										new EntityVillager.PriceInfo(-12, -8))),
+ 								Arrays.asList(
+ 										new EntityVillager.ListItemForEmeralds(Items.bow,
+ 												new EntityVillager.PriceInfo(2, 3)),
+ 										new EntityVillager.ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.gravel),
+ 												new EntityVillager.PriceInfo(10, 10), Items.flint,
+ 												new EntityVillager.PriceInfo(6, 10))))),
+ 				Arrays.asList(Arrays.asList(
+ 						Arrays.asList(
+ 								new EntityVillager.EmeraldForItems(Items.paper, new EntityVillager.PriceInfo(24, 36)),
+ 								new EntityVillager.ListEnchantedBookForEmeralds()),
+ 						Arrays.asList(
+ 								new EntityVillager.EmeraldForItems(Items.book, new EntityVillager.PriceInfo(8, 10)),
+ 								new EntityVillager.ListItemForEmeralds(Items.compass,
+ 										new EntityVillager.PriceInfo(10, 12)),
+ 								new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.bookshelf),
+ 										new EntityVillager.PriceInfo(3, 4))),
+ 						Arrays.asList(
+ 								new EntityVillager.EmeraldForItems(Items.written_book,
+ 										new EntityVillager.PriceInfo(2, 2)),
+ 								new EntityVillager.ListItemForEmeralds(Items.clock,
+ 										new EntityVillager.PriceInfo(10, 12)),
+ 								new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.glass),
+ 										new EntityVillager.PriceInfo(-5, -3))),
+ 						Arrays.asList(new EntityVillager.ListEnchantedBookForEmeralds()),
+ 						Arrays.asList(new EntityVillager.ListEnchantedBookForEmeralds()),
+ 						Arrays.asList(new EntityVillager.ListItemForEmeralds(Items.name_tag,
+ 								new EntityVillager.PriceInfo(20, 22))))),
+ 				Arrays.asList(Arrays.asList(Arrays.asList(
+ 						new EntityVillager.EmeraldForItems(Items.rotten_flesh, new EntityVillager.PriceInfo(36, 40)),
+ 						new EntityVillager.EmeraldForItems(Items.gold_ingot, new EntityVillager.PriceInfo(8, 10))),
+ 						Arrays.asList(
+ 								new EntityVillager.ListItemForEmeralds(Items.redstone,
+ 										new EntityVillager.PriceInfo(-4, -1)),
+ 								new EntityVillager.ListItemForEmeralds(
+ 										new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()),
+ 										new EntityVillager.PriceInfo(-2, -1))),
+ 						Arrays.asList(
+ 								new EntityVillager.ListItemForEmeralds(Items.ender_eye,
+ 										new EntityVillager.PriceInfo(7, 11)),
+ 								new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.glowstone),
+ 										new EntityVillager.PriceInfo(-3, -1))),
+ 						Arrays.asList(new EntityVillager.ListItemForEmeralds(Items.experience_bottle,
+ 								new EntityVillager.PriceInfo(3, 11))))),
+ 				Arrays.asList(
+ 						Arrays.asList(Arrays.asList(
+ 								new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)),
+ 								new EntityVillager.ListItemForEmeralds(Items.iron_helmet,
+ 										new EntityVillager.PriceInfo(4, 6))),
+ 								Arrays.asList(
+ 										new EntityVillager.EmeraldForItems(Items.iron_ingot,
+ 												new EntityVillager.PriceInfo(7, 9)),
+ 										new EntityVillager.ListItemForEmeralds(Items.iron_chestplate,
+ 												new EntityVillager.PriceInfo(10, 14))),
+ 								Arrays.asList(
+ 										new EntityVillager.EmeraldForItems(Items.diamond,
+ 												new EntityVillager.PriceInfo(3, 4)),
+ 										new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_chestplate,
+ 												new EntityVillager.PriceInfo(16, 19))),
+ 								Arrays.asList(
+ 										new EntityVillager.ListItemForEmeralds(Items.chainmail_boots,
+ 												new EntityVillager.PriceInfo(5, 7)),
+ 										new EntityVillager.ListItemForEmeralds(Items.chainmail_leggings,
+ 												new EntityVillager.PriceInfo(9, 11)),
+ 										new EntityVillager.ListItemForEmeralds(Items.chainmail_helmet,
+ 												new EntityVillager.PriceInfo(5, 7)),
+ 										new EntityVillager.ListItemForEmeralds(Items.chainmail_chestplate,
+ 												new EntityVillager.PriceInfo(11, 15)))),
+ 						Arrays.asList(Arrays.asList(
+ 								new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)),
+ 								new EntityVillager.ListItemForEmeralds(Items.iron_axe,
+ 										new EntityVillager.PriceInfo(6, 8))),
+ 								Arrays.asList(
+ 										new EntityVillager.EmeraldForItems(Items.iron_ingot,
+ 												new EntityVillager.PriceInfo(7, 9)),
+ 										new EntityVillager.ListEnchantedItemForEmeralds(Items.iron_sword,
+ 												new EntityVillager.PriceInfo(9, 10))),
+ 								Arrays.asList(
+ 										new EntityVillager.EmeraldForItems(Items.diamond,
+ 												new EntityVillager.PriceInfo(3, 4)),
+ 										new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_sword,
+ 												new EntityVillager.PriceInfo(12, 15)),
+ 										new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_axe,
+ 												new EntityVillager.PriceInfo(9, 12)))),
+ 						Arrays.asList(Arrays.asList(
+ 								new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)),
+ 								new EntityVillager.ListEnchantedItemForEmeralds(Items.iron_shovel,
+ 										new EntityVillager.PriceInfo(5, 7))),
+ 								Arrays.asList(
+ 										new EntityVillager.EmeraldForItems(Items.iron_ingot,
+ 												new EntityVillager.PriceInfo(7, 9)),
+ 										new EntityVillager.ListEnchantedItemForEmeralds(Items.iron_pickaxe,
+ 												new EntityVillager.PriceInfo(9, 11))),
+ 								Arrays.asList(
+ 										new EntityVillager.EmeraldForItems(Items.diamond,
+ 												new EntityVillager.PriceInfo(3, 4)),
+ 										new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_pickaxe,
+ 												new EntityVillager.PriceInfo(12, 15))))),
+ 				Arrays.asList(Arrays.asList(Arrays.asList(
+ 						new EntityVillager.EmeraldForItems(Items.porkchop, new EntityVillager.PriceInfo(14, 18)),
+ 						new EntityVillager.EmeraldForItems(Items.chicken, new EntityVillager.PriceInfo(14, 18))),
+ 						Arrays.asList(
+ 								new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)),
+ 								new EntityVillager.ListItemForEmeralds(Items.cooked_porkchop,
+ 										new EntityVillager.PriceInfo(-7, -5)),
+ 								new EntityVillager.ListItemForEmeralds(Items.cooked_chicken,
+ 										new EntityVillager.PriceInfo(-8, -6)))),
+ 						Arrays.asList(Arrays.asList(
+ 								new EntityVillager.EmeraldForItems(Items.leather, new EntityVillager.PriceInfo(9, 12)),
+ 								new EntityVillager.ListItemForEmeralds(Items.leather_leggings,
+ 										new EntityVillager.PriceInfo(2, 4))),
+ 								Arrays.asList(new EntityVillager.ListEnchantedItemForEmeralds(Items.leather_chestplate,
+ 										new EntityVillager.PriceInfo(7, 12))),
+ 								Arrays.asList(new EntityVillager.ListItemForEmeralds(Items.saddle,
+ 										new EntityVillager.PriceInfo(8, 10))))));
+ 	}
+ 

> CHANGE  75 : 77  @  75 : 76

~ 					for (int i = 0, l = this.buyingList.size(); i < l; ++i) {
~ 						MerchantRecipe merchantrecipe = this.buyingList.get(i);

> CHANGE  49 : 54  @  49 : 50

~ 			try {
~ 				nbttagcompound.setTag("Offers", this.buyingList.getRecipiesAsTags());
~ 			} catch (Throwable t) {
~ 				this.buyingList = null; // workaround
~ 			}

> CHANGE  211 : 213  @  211 : 212

~ 		List<List<List<EntityVillager.ITradeList>>> aentityvillager$itradelist = DEFAULT_TRADE_LIST_MAP
~ 				.get(this.getProfession());

> CHANGE  3 : 4  @  3 : 4

~ 			this.careerId = this.rand.nextInt(aentityvillager$itradelist.size()) + 1;

> CHANGE  9 : 12  @  9 : 12

~ 		List<List<EntityVillager.ITradeList>> aentityvillager$itradelist1 = aentityvillager$itradelist.get(i);
~ 		if (j >= 0 && j < aentityvillager$itradelist1.size()) {
~ 			List<EntityVillager.ITradeList> aentityvillager$itradelist2 = aentityvillager$itradelist1.get(j);

> CHANGE  1 : 3  @  1 : 3

~ 			for (int k = 0, l = aentityvillager$itradelist2.size(); k < l; ++k) {
~ 				aentityvillager$itradelist2.get(k).modifyMerchantRecipeList(this.buyingList, this.rand);

> CHANGE  236 : 237  @  236 : 237

~ 		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, EaglercraftRandom random) {

> CHANGE  10 : 11  @  10 : 11

~ 		void modifyMerchantRecipeList(MerchantRecipeList var1, EaglercraftRandom var2);

> CHANGE  16 : 17  @  16 : 17

~ 		public void modifyMerchantRecipeList(MerchantRecipeList merchantrecipelist, EaglercraftRandom random) {

> CHANGE  18 : 19  @  18 : 19

~ 		public void modifyMerchantRecipeList(MerchantRecipeList merchantrecipelist, EaglercraftRandom random) {

> CHANGE  23 : 24  @  23 : 24

~ 		public void modifyMerchantRecipeList(MerchantRecipeList merchantrecipelist, EaglercraftRandom random) {

> CHANGE  26 : 27  @  26 : 27

~ 		public void modifyMerchantRecipeList(MerchantRecipeList merchantrecipelist, EaglercraftRandom random) {

> CHANGE  24 : 25  @  24 : 25

~ 		public int getPrice(EaglercraftRandom rand) {

> EOF
