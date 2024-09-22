
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import java.util.List;
+ 

> INSERT  1 : 3  @  1

+ 
+ import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;

> DELETE  18  @  18 : 19

> DELETE  12  @  12 : 13

> INSERT  99 : 107  @  99

+ 		return getNameImpl(false);
+ 	}
+ 
+ 	public String getNameProfanityFilter() {
+ 		return getNameImpl(true);
+ 	}
+ 
+ 	private String getNameImpl(boolean filter) {

> CHANGE  1 : 2  @  1 : 2

~ 			return filter ? this.getCustomNameTagProfanityFilter() : this.getCustomNameTag();

> CHANGE  280 : 284  @  280 : 282

~ 		List<Entity> lst = this.worldObj.getEntitiesInAABBexcluding(entityIn,
~ 				entityIn.getEntityBoundingBox().addCoord(distance, distance, distance), horseBreedingSelector);
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			Entity entity1 = lst.get(i);

> CHANGE  632 : 637  @  632 : 633

~ 		if (worldObj.isRemote && !SingleplayerServerController.isClientInEaglerSingleplayerOrLAN()) {
~ 			nbttagcompound.setString("OwnerUUID", this.getOwnerId());
~ 		} else {
~ 			nbttagcompound.setString("Owner", this.getOwnerId());
~ 		}

> CHANGE  37 : 41  @  37 : 39

~ 		if (worldObj.isRemote && !SingleplayerServerController.isClientInEaglerSingleplayerOrLAN()) {
~ 			if (nbttagcompound.hasKey("OwnerUUID", 8)) {
~ 				s = nbttagcompound.getString("OwnerUUID");
~ 			}

> CHANGE  1 : 4  @  1 : 3

~ 			if (nbttagcompound.hasKey("Owner", 8)) {
~ 				s = nbttagcompound.getString("Owner");
~ 			}

> EOF
