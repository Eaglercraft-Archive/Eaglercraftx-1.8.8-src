
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  26 : 27  @  26 : 27

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom var4) {

> CHANGE  44 : 57  @  44 : 62

~ 					for (int j = 0; j < 128; ++j) {
~ 						double d0 = worldIn.rand.nextDouble();
~ 						float f = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
~ 						float f1 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
~ 						float f2 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
~ 						double d1 = (double) blockpos.getX() + (double) (pos.getX() - blockpos.getX()) * d0
~ 								+ (worldIn.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
~ 						double d2 = (double) blockpos.getY() + (double) (pos.getY() - blockpos.getY()) * d0
~ 								+ worldIn.rand.nextDouble() * 1.0D - 0.5D;
~ 						double d3 = (double) blockpos.getZ() + (double) (pos.getZ() - blockpos.getZ()) * d0
~ 								+ (worldIn.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
~ 						worldIn.spawnParticle(EnumParticleTypes.PORTAL, d1, d2, d3, (double) f, (double) f1,
~ 								(double) f2, new int[0]);

> DELETE  1  @  1 : 2

> EOF
