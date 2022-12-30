
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  2 : 8  @  5 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import com.google.common.base.Charsets;
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> INSERT  11 : 12  @  6

+ import net.minecraft.command.ICommandSender;

> DELETE  16  @  15 : 16

> DELETE  3  @  4 : 8

> DELETE  16  @  20 : 21

> DELETE  7  @  8 : 9

> CHANGE  20 : 21  @  21 : 22

~ public abstract class EntityPlayer extends EntityLivingBase implements ICommandSender {

> CHANGE  43 : 44  @  43 : 44

~ 		this.inventoryContainer = new ContainerPlayer(this.inventory, false, this);

> DELETE  50  @  50 : 54

> CHANGE  19 : 20  @  23 : 26

~ 				--this.itemInUseCount;

> DELETE  15  @  17 : 25

> DELETE  8  @  16 : 20

> DELETE  43  @  47 : 55

> CHANGE  100 : 113  @  108 : 127

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

> DELETE  49  @  55 : 58

> CHANGE  108 : 109  @  111 : 112

~ 		for (ScoreObjective scoreobjective : (Collection<ScoreObjective>) collection) {

> DELETE  212  @  212 : 216

> DELETE  201  @  205 : 214

> DELETE  82  @  91 : 95

> DELETE  1  @  5 : 30

> DELETE  36  @  61 : 64

> DELETE  39  @  42 : 45

> DELETE  292  @  295 : 299

> DELETE  1  @  5 : 6

> DELETE  18  @  19 : 23

> CHANGE  156 : 158  @  160 : 162

~ 	public static EaglercraftUUID getUUID(GameProfile profile) {
~ 		EaglercraftUUID uuid = profile.getId();

> CHANGE  9 : 11  @  9 : 11

~ 	public static EaglercraftUUID getOfflineUUID(String username) {
~ 		return EaglercraftUUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8));

> CHANGE  20 : 21  @  20 : 21

~ 		return true;

> EOF
