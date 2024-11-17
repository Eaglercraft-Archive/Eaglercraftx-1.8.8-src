
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 10  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DynamicLightManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights.DynamicLightsStateManager;
~ import net.lax1dude.eaglercraft.v1_8.profanity_filter.ProfanityFilter;
~ 

> INSERT  1 : 2  @  1

+ 

> INSERT  8 : 10  @  8

+ import net.minecraft.client.Minecraft;
+ import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

> DELETE  6  @  6 : 9

> CHANGE  74 : 75  @  74 : 75

~ 	protected EaglercraftRandom rand;

> CHANGE  27 : 28  @  27 : 28

~ 	protected EaglercraftUUID entityUniqueID;

> CHANGE  21 : 22  @  21 : 22

~ 		this.rand = new EaglercraftRandom();

> DELETE  100  @  100 : 101

> DELETE  11  @  11 : 12

> DELETE  32  @  32 : 34

> DELETE  35  @  35 : 36

> DELETE  44  @  44 : 45

> CHANGE  66 : 68  @  66 : 67

~ 			List<AxisAlignedBB> list1 = this.worldObj.getCollidingBoundingBoxes(this,
~ 					this.getEntityBoundingBox().addCoord(x, y, z));

> CHANGE  2 : 4  @  2 : 4

~ 			for (int i = 0, l = list1.size(); i < l; ++i) {
~ 				y = list1.get(i).calculateYOffset(this.getEntityBoundingBox(), y);

> CHANGE  5 : 7  @  5 : 7

~ 			for (int i = 0, l = list1.size(); i < l; ++i) {
~ 				x = list1.get(i).calculateXOffset(this.getEntityBoundingBox(), x);

> CHANGE  4 : 6  @  4 : 6

~ 			for (int i = 0, l = list1.size(); i < l; ++i) {
~ 				z = list1.get(i).calculateZOffset(this.getEntityBoundingBox(), z);

> CHANGE  10 : 11  @  10 : 11

~ 				List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this,

> CHANGE  5 : 7  @  5 : 7

~ 				for (int i = 0, l = list.size(); i < l; ++i) {
~ 					d9 = list.get(i).calculateYOffset(axisalignedbb5, d9);

> CHANGE  5 : 7  @  5 : 7

~ 				for (int i = 0, l = list.size(); i < l; ++i) {
~ 					d15 = list.get(i).calculateXOffset(axisalignedbb4, d15);

> CHANGE  5 : 7  @  5 : 7

~ 				for (int i = 0, l = list.size(); i < l; ++i) {
~ 					d16 = list.get(i).calculateZOffset(axisalignedbb4, d16);

> CHANGE  6 : 8  @  6 : 8

~ 				for (int i = 0, l = list.size(); i < l; ++i) {
~ 					d17 = list.get(i).calculateYOffset(axisalignedbb14, d17);

> CHANGE  5 : 7  @  5 : 7

~ 				for (int i = 0, l = list.size(); i < l; ++i) {
~ 					d18 = list.get(i).calculateXOffset(axisalignedbb14, d18);

> CHANGE  5 : 7  @  5 : 7

~ 				for (int i = 0, l = list.size(); i < l; ++i) {
~ 					d19 = list.get(i).calculateZOffset(axisalignedbb14, d19);

> CHANGE  17 : 19  @  17 : 19

~ 				for (int i = 0, l = list.size(); i < l; ++i) {
~ 					y = list.get(i).calculateYOffset(this.getEntityBoundingBox(), y);

> DELETE  11  @  11 : 13

> DELETE  93  @  93 : 95

> CHANGE  239 : 245  @  239 : 240

~ 		int i = 0;
~ 		if (DynamicLightsStateManager.isDynamicLightsRender()) {
~ 			i += (int) (getEaglerDynamicLightsValueSimple(var1) * 15.0f);
~ 		}
~ 		return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getCombinedLight(blockpos, -i)
~ 				: (i > 15 ? 240 : (i << 4));

> CHANGE  4 : 9  @  4 : 5

~ 		float f = this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getLightBrightness(blockpos) : 0.0F;
~ 		if (DynamicLightsStateManager.isDynamicLightsRender()) {
~ 			f = Math.min(f + getEaglerDynamicLightsValueSimple(var1), 1.0f);
~ 		}
~ 		return f;

> CHANGE  297 : 299  @  297 : 298

~ 				this.entityUniqueID = new EaglercraftUUID(tagCompund.getLong("UUIDMost"),
~ 						tagCompund.getLong("UUIDLeast"));

> CHANGE  1 : 2  @  1 : 2

~ 				this.entityUniqueID = EaglercraftUUID.fromString(tagCompund.getString("UUID"));

> CHANGE  42 : 44  @  42 : 44

~ 		for (int i = 0; i < numbers.length; ++i) {
~ 			nbttaglist.appendTag(new NBTTagDouble(numbers[i]));

> CHANGE  8 : 10  @  8 : 10

~ 		for (int i = 0; i < numbers.length; ++i) {
~ 			nbttaglist.appendTag(new NBTTagFloat(numbers[i]));

> CHANGE  33 : 34  @  33 : 35

~ 			BlockPos blockpos$mutableblockpos = new BlockPos(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

> CHANGE  137 : 138  @  137 : 138

~ 			for (AxisAlignedBB axisalignedbb : (List<AxisAlignedBB>) list) {

> INSERT  229 : 242  @  229

+ 	public String getNameProfanityFilter() {
+ 		if (this.hasCustomName()) {
+ 			return this.getCustomNameTagProfanityFilter();
+ 		} else {
+ 			String s = EntityList.getEntityString(this);
+ 			if (s == null) {
+ 				s = "generic";
+ 			}
+ 
+ 			return StatCollector.translateToLocal("entity." + s + ".name");
+ 		}
+ 	}
+ 

> CHANGE  27 : 28  @  27 : 28

~ 		return HString.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]",

> DELETE  26  @  26 : 27

> DELETE  12  @  12 : 13

> DELETE  1  @  1 : 2

> DELETE  12  @  12 : 13

> DELETE  2  @  2 : 3

> CHANGE  63 : 64  @  63 : 64

~ 	public EaglercraftUUID getUniqueID() {

> INSERT  14 : 21  @  14

+ 	public IChatComponent getDisplayNameProfanityFilter() {
+ 		ChatComponentText chatcomponenttext = new ChatComponentText(this.getNameProfanityFilter());
+ 		chatcomponenttext.getChatStyle().setChatHoverEvent(this.getHoverEvent());
+ 		chatcomponenttext.getChatStyle().setInsertion(this.getUniqueID().toString());
+ 		return chatcomponenttext;
+ 	}
+ 

> INSERT  8 : 28  @  8

+ 	private String lastNameTagForFilter = null;
+ 	private String lastFilteredNameTagForFilter = null;
+ 
+ 	public String getCustomNameTagProfanityFilter() {
+ 		if (Minecraft.getMinecraft().isEnableProfanityFilter()) {
+ 			String str = getCustomNameTag();
+ 			if (str != null) {
+ 				if (!str.equals(lastNameTagForFilter)) {
+ 					lastNameTagForFilter = str;
+ 					lastFilteredNameTagForFilter = ProfanityFilter.getInstance().profanityFilterString(str);
+ 				}
+ 				return lastFilteredNameTagForFilter;
+ 			} else {
+ 				return null;
+ 			}
+ 		} else {
+ 			return getCustomNameTag();
+ 		}
+ 	}
+ 

> INSERT  129 : 183  @  129

+ 
+ 	public void renderDynamicLightsEagler(float partialTicks, boolean isInFrustum) {
+ 		double entityX = prevPosX + (posX - prevPosX) * (double) partialTicks;
+ 		double entityY = prevPosY + (posY - prevPosY) * (double) partialTicks;
+ 		double entityZ = prevPosZ + (posZ - prevPosZ) * (double) partialTicks;
+ 		double entityX2 = entityX - TileEntityRendererDispatcher.staticPlayerX;
+ 		double entityY2 = entityY - TileEntityRendererDispatcher.staticPlayerY;
+ 		double entityZ2 = entityZ - TileEntityRendererDispatcher.staticPlayerZ;
+ 		if (entityX2 * entityX2 + entityY2 * entityY2
+ 				+ entityZ2 * entityZ2 < (isInFrustum ? (64.0 * 64.0) : (24.0 * 24.0))) {
+ 			renderDynamicLightsEaglerAt(entityX, entityY, entityZ, entityX2, entityY2, entityZ2, partialTicks,
+ 					isInFrustum);
+ 		}
+ 	}
+ 
+ 	protected void renderDynamicLightsEaglerAt(double entityX, double entityY, double entityZ, double renderX,
+ 			double renderY, double renderZ, float partialTicks, boolean isInFrustum) {
+ 		float size = Math.max(width, height);
+ 		if (size < 1.0f && !isInFrustum) {
+ 			return;
+ 		}
+ 		if (this.isBurning()) {
+ 			float mag = 5.0f * size;
+ 			DynamicLightManager.renderDynamicLight("entity_" + entityId + "_fire", entityX, entityY + height * 0.75,
+ 					entityZ, mag, 0.487f * mag, 0.1411f * mag, false);
+ 		}
+ 	}
+ 
+ 	public void renderDynamicLightsEaglerSimple(float partialTicks) {
+ 		double entityX = prevPosX + (posX - prevPosX) * (double) partialTicks;
+ 		double entityY = prevPosY + (posY - prevPosY) * (double) partialTicks;
+ 		double entityZ = prevPosZ + (posZ - prevPosZ) * (double) partialTicks;
+ 		renderDynamicLightsEaglerSimpleAt(entityX, entityY, entityZ, partialTicks);
+ 	}
+ 
+ 	protected void renderDynamicLightsEaglerSimpleAt(double entityX, double entityY, double entityZ,
+ 			float partialTicks) {
+ 		float renderBrightness = this.getEaglerDynamicLightsValueSimple(partialTicks);
+ 		if (renderBrightness > 0.1f) {
+ 			DynamicLightsStateManager.renderDynamicLight("entity_" + entityId + "_lightmap", entityX,
+ 					entityY + height * 0.85, entityZ, renderBrightness * 13.0f);
+ 		}
+ 	}
+ 
+ 	protected float getEaglerDynamicLightsValueSimple(float partialTicks) {
+ 		float size = Math.max(width, height);
+ 		if (size < 1.0f) {
+ 			return 0.0f;
+ 		}
+ 		if (this.isBurning()) {
+ 			return size / 2.0f;
+ 		}
+ 		return 0.0f;
+ 	}

> EOF
