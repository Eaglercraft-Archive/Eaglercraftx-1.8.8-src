
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 6  @  4 : 5

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ 

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> CHANGE  42 : 44  @  42 : 44

~ import net.minecraft.nbt.NBTTagCompound;
~ import net.minecraft.nbt.NBTTagList;

> CHANGE  79 : 80  @  79 : 80

~ 		this.inventoryContainer = new ContainerPlayer(this.inventory, true, this);

> CHANGE  49 : 50  @  49 : 53

~ 		this.setEating(false);

> CHANGE  19 : 20  @  19 : 20

~ 				if (--this.itemInUseCount == 0) {

> CHANGE  17 : 18  @  17 : 18

~ 			{

> CHANGE  14 : 15  @  14 : 15

~ 		if (this.openContainer != null && !this.openContainer.canInteractWith(this)) {

> CHANGE  46 : 47  @  46 : 47

~ 		{

> CHANGE  107 : 108  @  107 : 108

~ 		if (this.isSneaking()) {

> CHANGE  54 : 55  @  54 : 57

~ 		iattributeinstance.setBaseValue((double) this.capabilities.getWalkSpeed());

> CHANGE  108 : 109  @  108 : 109

~ 		for (ScoreObjective scoreobjective : (Collection<ScoreObjective>) collection) {

> CHANGE  211 : 212  @  211 : 212

~ 				if (this.isPlayerSleeping()) {

> INSERT  125 : 135  @  125

+ 				} else if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
+ 						.getBoolean("clickToRide") && itemstack == null && parEntity instanceof EntityPlayer) {
+ 					EntityPlayer otherPlayer = (EntityPlayer) parEntity;
+ 					while (otherPlayer.riddenByEntity instanceof EntityPlayer) {
+ 						otherPlayer = (EntityPlayer) otherPlayer.riddenByEntity;
+ 					}
+ 					if (otherPlayer.riddenByEntity == null && otherPlayer != this) {
+ 						mountEntity(otherPlayer);
+ 						return true;
+ 					}

> CHANGE  170 : 171  @  170 : 171

~ 		{

> CHANGE  65 : 66  @  65 : 68

~ 		this.worldObj.updateAllPlayersSleepingFlag();

> CHANGE  39 : 40  @  39 : 40

~ 		if (flag1) {

> CHANGE  295 : 296  @  295 : 299

~ 			this.foodStats.addExhaustion(parFloat1);

> CHANGE  19 : 20  @  19 : 23

~ 			this.setEating(true);

> CHANGE  156 : 158  @  156 : 158

~ 	public static EaglercraftUUID getUUID(GameProfile profile) {
~ 		EaglercraftUUID uuid = profile.getId();

> CHANGE  7 : 9  @  7 : 9

~ 	public static EaglercraftUUID getOfflineUUID(String username) {
~ 		return EaglercraftUUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8));

> EOF
