
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 6  @  4 : 5

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ 

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> INSERT  5 : 6  @  5

+ import net.minecraft.command.ICommandSender;

> DELETE  19  @  19 : 23

> CHANGE  45 : 46  @  45 : 46

~ public abstract class EntityPlayer extends EntityLivingBase implements ICommandSender {

> CHANGE  458 : 459  @  458 : 459

~ 		Collection<ScoreObjective> collection = this.getWorldScoreboard()

> CHANGE  271 : 274  @  271 : 273

~ 		ItemStack[] stack = this.inventory.armorInventory;
~ 		for (int j = 0; j < stack.length; ++j) {
~ 			if (stack[j] != null) {

> INSERT  75 : 86  @  75

+ 				} else if (!this.worldObj.isRemote && MinecraftServer.getServer().worldServers[0].getWorldInfo()
+ 						.getGameRulesInstance().getBoolean("clickToRide") && itemstack == null
+ 						&& parEntity instanceof EntityPlayer) {
+ 					EntityPlayer otherPlayer = (EntityPlayer) parEntity;
+ 					while (otherPlayer.riddenByEntity instanceof EntityPlayer) {
+ 						otherPlayer = (EntityPlayer) otherPlayer.riddenByEntity;
+ 					}
+ 					if (otherPlayer.riddenByEntity == null && otherPlayer != this) {
+ 						mountEntity(otherPlayer);
+ 						return true;
+ 					}

> CHANGE  757 : 759  @  757 : 759

~ 	public static EaglercraftUUID getUUID(GameProfile profile) {
~ 		EaglercraftUUID uuid = profile.getId();

> CHANGE  7 : 9  @  7 : 9

~ 	public static EaglercraftUUID getOfflineUUID(String username) {
~ 		return EaglercraftUUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8));

> CHANGE  66 : 67  @  66 : 67

~ 		private static final EntityPlayer.EnumChatVisibility[] ID_LOOKUP = new EntityPlayer.EnumChatVisibility[3];

> CHANGE  21 : 24  @  21 : 23

~ 			EntityPlayer.EnumChatVisibility[] lst = values();
~ 			for (int i = 0; i < lst.length; ++i) {
~ 				ID_LOOKUP[lst[i].chatVisibility] = lst[i];

> EOF
