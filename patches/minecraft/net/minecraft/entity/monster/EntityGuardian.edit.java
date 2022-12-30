
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> DELETE  4  @  5 : 17

> DELETE  6  @  18 : 23

> DELETE  3  @  8 : 9

> DELETE  3  @  4 : 5

> DELETE  11  @  12 : 13

> DELETE  5  @  6 : 18

> DELETE  21  @  33 : 37

> DELETE  24  @  28 : 32

> DELETE  16  @  20 : 21

> DELETE  9  @  10 : 14

> CHANGE  7 : 8  @  11 : 12

~ 		} else {

> DELETE  12  @  12 : 14

> CHANGE  50 : 55  @  52 : 70

~ 		this.field_175484_c = this.field_175482_b;
~ 		if (!this.isInWater()) {
~ 			this.field_175483_bk = 2.0F;
~ 			if (this.motionY > 0.0D && this.field_175480_bp && !this.isSilent()) {
~ 				this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.guardian.flop", 1.0F, 1.0F, false);

> CHANGE  7 : 12  @  20 : 26

~ 			this.field_175480_bp = this.motionY < 0.0D
~ 					&& this.worldObj.isBlockNormalCube((new BlockPos(this)).down(), false);
~ 		} else if (this.func_175472_n()) {
~ 			if (this.field_175483_bk < 0.5F) {
~ 				this.field_175483_bk = 4.0F;

> CHANGE  6 : 7  @  7 : 8

~ 				this.field_175483_bk += (0.5F - this.field_175483_bk) * 0.1F;

> INSERT  2 : 5  @  2

+ 		} else {
+ 			this.field_175483_bk += (0.125F - this.field_175483_bk) * 0.2F;
+ 		}

> CHANGE  4 : 13  @  1 : 3

~ 		this.field_175482_b += this.field_175483_bk;
~ 		this.field_175486_bm = this.field_175485_bl;
~ 		if (!this.isInWater()) {
~ 			this.field_175485_bl = this.rand.nextFloat();
~ 		} else if (this.func_175472_n()) {
~ 			this.field_175485_bl += (0.0F - this.field_175485_bl) * 0.25F;
~ 		} else {
~ 			this.field_175485_bl += (1.0F - this.field_175485_bl) * 0.06F;
~ 		}

> CHANGE  10 : 19  @  3 : 10

~ 		if (this.func_175472_n() && this.isInWater()) {
~ 			Vec3 vec3 = this.getLook(0.0F);
~ 
~ 			for (int i = 0; i < 2; ++i) {
~ 				this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE,
~ 						this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width - vec3.xCoord * 1.5D,
~ 						this.posY + this.rand.nextDouble() * (double) this.height - vec3.yCoord * 1.5D,
~ 						this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width - vec3.zCoord * 1.5D, 0.0D,
~ 						0.0D, 0.0D, new int[0]);

> INSERT  10 : 11  @  8

+ 		}

> CHANGE  2 : 6  @  1 : 5

~ 		if (this.hasTargetedEntity()) {
~ 			if (this.field_175479_bo < this.func_175464_ck()) {
~ 				++this.field_175479_bo;
~ 			}

> CHANGE  5 : 17  @  5 : 19

~ 			EntityLivingBase entitylivingbase = this.getTargetedEntity();
~ 			if (entitylivingbase != null) {
~ 				double d5 = (double) this.func_175477_p(0.0F);
~ 				double d0 = entitylivingbase.posX - this.posX;
~ 				double d1 = entitylivingbase.posY + (double) (entitylivingbase.height * 0.5F)
~ 						- (this.posY + (double) this.getEyeHeight());
~ 				double d2 = entitylivingbase.posZ - this.posZ;
~ 				double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
~ 				d0 = d0 / d3;
~ 				d1 = d1 / d3;
~ 				d2 = d2 / d3;
~ 				double d4 = this.rand.nextDouble();

> CHANGE  13 : 18  @  15 : 21

~ 				while (d4 < d3) {
~ 					d4 += 1.8D - d5 + this.rand.nextDouble() * (1.7D - d5);
~ 					this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + d0 * d4,
~ 							this.posY + d1 * d4 + (double) this.getEyeHeight(), this.posZ + d2 * d4, 0.0D, 0.0D, 0.0D,
~ 							new int[0]);

> DELETE  39  @  40 : 73

> DELETE  48  @  81 : 82

> DELETE  27  @  28 : 164

> EOF
