
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  3 : 9  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ import com.google.common.collect.Sets;
~ 

> INSERT  12 : 13  @  12

+ import net.minecraft.util.EnumFacing;

> DELETE  3  @  3 : 4

> CHANGE  4 : 5  @  4 : 5

~ 	private final EaglercraftRandom explosionRNG;

> CHANGE  22 : 23  @  22 : 23

~ 		this.explosionRNG = new EaglercraftRandom();

> CHANGE  112 : 114  @  112 : 113

~ 			for (int i = 0, l = this.affectedBlockPositions.size(); i < l; ++i) {
~ 				BlockPos blockpos = this.affectedBlockPositions.get(i);

> CHANGE  36 : 41  @  36 : 39

~ 			BlockPos tmp = new BlockPos(0, 0, 0);
~ 			for (int i = 0, l = this.affectedBlockPositions.size(); i < l; ++i) {
~ 				BlockPos blockpos1 = this.affectedBlockPositions.get(i);
~ 				if (this.worldObj.getBlockState(blockpos1).getBlock().getMaterial() == Material.air && this.worldObj
~ 						.getBlockState(blockpos1.offsetEvenFaster(EnumFacing.DOWN, tmp)).getBlock().isFullBlock()

> EOF
