
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  4 : 10  @  7 : 8

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import com.google.common.base.Charsets;
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> INSERT  15 : 16  @  13

+ import net.minecraft.command.ICommandSender;

> DELETE  31  @  28 : 29

> DELETE  34  @  32 : 36

> DELETE  50  @  52 : 53

> DELETE  57  @  60 : 61

> CHANGE  77 : 78  @  81 : 82

~ public abstract class EntityPlayer extends EntityLivingBase implements ICommandSender {

> CHANGE  120 : 121  @  124 : 125

~ 		this.inventoryContainer = new ContainerPlayer(this.inventory, false, this);

> DELETE  170  @  174 : 178

> CHANGE  189 : 190  @  197 : 200

~ 				--this.itemInUseCount;

> DELETE  204  @  214 : 222

> DELETE  212  @  230 : 234

> DELETE  255  @  277 : 285

> CHANGE  355 : 368  @  385 : 404

~ 		double d0 = this.posX;
~ 		double d1 = this.posY;
~ 		double d2 = this.posZ;
~ 		float f = this.rotationYaw;
~ 		float f1 = this.rotationPitch;
~ 		super.updateRidden();
~ 		this.prevCameraYaw = this.cameraYaw;
~ 		this.cameraYaw = 0.0F;
~ 		this.addMountedMovementStat(this.posX - d0, this.posY - d1, this.posZ - d2);
~ 		if (this.ridingEntity instanceof EntityPig) {
~ 			this.rotationPitch = f1;
~ 			this.rotationYaw = f;
~ 			this.renderYawOffset = ((EntityPig) this.ridingEntity).renderYawOffset;

> DELETE  404  @  440 : 443

> CHANGE  512 : 513  @  551 : 552

~ 		for (ScoreObjective scoreobjective : (Collection<ScoreObjective>) collection) {

> DELETE  724  @  763 : 767

> DELETE  925  @  968 : 977

> DELETE  1007  @  1059 : 1063

> DELETE  1008  @  1064 : 1089

> DELETE  1044  @  1125 : 1128

> DELETE  1083  @  1167 : 1170

> DELETE  1375  @  1462 : 1466

> DELETE  1376  @  1467 : 1468

> DELETE  1394  @  1486 : 1490

> CHANGE  1550 : 1552  @  1646 : 1648

~ 	public static EaglercraftUUID getUUID(GameProfile profile) {
~ 		EaglercraftUUID uuid = profile.getId();

> CHANGE  1559 : 1561  @  1655 : 1657

~ 	public static EaglercraftUUID getOfflineUUID(String username) {
~ 		return EaglercraftUUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8));

> CHANGE  1579 : 1580  @  1675 : 1676

~ 		return true;

> EOF
