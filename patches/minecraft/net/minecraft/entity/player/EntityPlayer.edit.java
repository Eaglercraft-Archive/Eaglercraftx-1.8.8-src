
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  2 : 8  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import com.google.common.base.Charsets;
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> INSERT  5 : 6  @  5

+ import net.minecraft.command.ICommandSender;

> DELETE  15  @  15 : 16

> DELETE  3  @  3 : 7

> DELETE  16  @  16 : 17

> DELETE  7  @  7 : 8

> CHANGE  20 : 21  @  20 : 21

~ public abstract class EntityPlayer extends EntityLivingBase implements ICommandSender {

> CHANGE  42 : 43  @  42 : 43

~ 		this.inventoryContainer = new ContainerPlayer(this.inventory, false, this);

> DELETE  49  @  49 : 53

> CHANGE  19 : 20  @  19 : 22

~ 				--this.itemInUseCount;

> DELETE  14  @  14 : 22

> DELETE  8  @  8 : 12

> DELETE  43  @  43 : 51

> CHANGE  100 : 113  @  100 : 119

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

> DELETE  36  @  36 : 39

> CHANGE  108 : 109  @  108 : 109

~ 		for (ScoreObjective scoreobjective : (Collection<ScoreObjective>) collection) {

> DELETE  211  @  211 : 215

> DELETE  201  @  201 : 210

> DELETE  82  @  82 : 86

> DELETE  1  @  1 : 26

> DELETE  36  @  36 : 39

> DELETE  39  @  39 : 42

> DELETE  292  @  292 : 296

> DELETE  1  @  1 : 2

> DELETE  18  @  18 : 22

> CHANGE  156 : 158  @  156 : 158

~ 	public static EaglercraftUUID getUUID(GameProfile profile) {
~ 		EaglercraftUUID uuid = profile.getId();

> CHANGE  7 : 9  @  7 : 9

~ 	public static EaglercraftUUID getOfflineUUID(String username) {
~ 		return EaglercraftUUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8));

> CHANGE  18 : 19  @  18 : 19

~ 		return true;

> EOF
