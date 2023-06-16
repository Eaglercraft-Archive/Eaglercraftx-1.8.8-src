
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> DELETE  4  @  4 : 16

> DELETE  6  @  6 : 11

> DELETE  3  @  3 : 4

> DELETE  3  @  3 : 4

> DELETE  11  @  11 : 12

> DELETE  5  @  5 : 17

> DELETE  21  @  21 : 25

> DELETE  24  @  24 : 28

> DELETE  16  @  16 : 17

> DELETE  9  @  9 : 13

> CHANGE  7 : 8  @  7 : 8

~ 		} else {

> DELETE  11  @  11 : 13

> CHANGE  50 : 55  @  50 : 68

~ 		this.field_175484_c = this.field_175482_b;
~ 		if (!this.isInWater()) {
~ 			this.field_175483_bk = 2.0F;
~ 			if (this.motionY > 0.0D && this.field_175480_bp && !this.isSilent()) {
~ 				this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.guardian.flop", 1.0F, 1.0F, false);

> CHANGE  2 : 7  @  2 : 8

~ 			this.field_175480_bp = this.motionY < 0.0D
~ 					&& this.worldObj.isBlockNormalCube((new BlockPos(this)).down(), false);
~ 		} else if (this.func_175472_n()) {
~ 			if (this.field_175483_bk < 0.5F) {
~ 				this.field_175483_bk = 4.0F;

> CHANGE  1 : 2  @  1 : 2

~ 				this.field_175483_bk += (0.5F - this.field_175483_bk) * 0.1F;

> INSERT  1 : 4  @  1

+ 		} else {
+ 			this.field_175483_bk += (0.125F - this.field_175483_bk) * 0.2F;
+ 		}

> CHANGE  1 : 10  @  1 : 3

~ 		this.field_175482_b += this.field_175483_bk;
~ 		this.field_175486_bm = this.field_175485_bl;
~ 		if (!this.isInWater()) {
~ 			this.field_175485_bl = this.rand.nextFloat();
~ 		} else if (this.func_175472_n()) {
~ 			this.field_175485_bl += (0.0F - this.field_175485_bl) * 0.25F;
~ 		} else {
~ 			this.field_175485_bl += (1.0F - this.field_175485_bl) * 0.06F;
~ 		}

> CHANGE  1 : 10  @  1 : 8

~ 		if (this.func_175472_n() && this.isInWater()) {
~ 			Vec3 vec3 = this.getLook(0.0F);
~ 
~ 			for (int i = 0; i < 2; ++i) {
~ 				this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE,
~ 						this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width - vec3.xCoord * 1.5D,
~ 						this.posY + this.rand.nextDouble() * (double) this.height - vec3.yCoord * 1.5D,
~ 						this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width - vec3.zCoord * 1.5D, 0.0D,
~ 						0.0D, 0.0D, new int[0]);

> INSERT  1 : 2  @  1

+ 		}

> CHANGE  1 : 5  @  1 : 5

~ 		if (this.hasTargetedEntity()) {
~ 			if (this.field_175479_bo < this.func_175464_ck()) {
~ 				++this.field_175479_bo;
~ 			}

> CHANGE  1 : 13  @  1 : 15

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

> CHANGE  1 : 6  @  1 : 7

~ 				while (d4 < d3) {
~ 					d4 += 1.8D - d5 + this.rand.nextDouble() * (1.7D - d5);
~ 					this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + d0 * d4,
~ 							this.posY + d1 * d4 + (double) this.getEyeHeight(), this.posZ + d2 * d4, 0.0D, 0.0D, 0.0D,
~ 							new int[0]);

> DELETE  34  @  34 : 67

> DELETE  48  @  48 : 49

> DELETE  27  @  27 : 163

> EOF
