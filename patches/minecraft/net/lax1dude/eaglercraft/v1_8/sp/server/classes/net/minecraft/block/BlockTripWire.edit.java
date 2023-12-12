
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  75 : 76  @  75 : 76

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  43 : 46  @  43 : 49

~ 		if (entityplayer.getCurrentEquippedItem() != null
~ 				&& entityplayer.getCurrentEquippedItem().getItem() == Items.shears) {
~ 			world.setBlockState(blockpos, iblockstate.withProperty(DISARMED, Boolean.valueOf(true)), 4);

> CHANGE  24 : 25  @  24 : 25

~ 		{

> CHANGE  6 : 7  @  6 : 7

~ 	public void randomTick(World var1, BlockPos var2, IBlockState var3, EaglercraftRandom var4) {

> CHANGE  2 : 4  @  2 : 4

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom var4) {
~ 		{

> CHANGE  15 : 16  @  15 : 16

~ 			for (Entity entity : (List<Entity>) list) {

> EOF
