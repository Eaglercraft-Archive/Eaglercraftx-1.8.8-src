
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 8  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DynamicLightManager;
~ 

> INSERT  1 : 2  @  1

+ 

> CHANGE  7 : 8  @  7 : 10

~ import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

> DELETE  4  @  4 : 7

> DELETE  3  @  3 : 4

> DELETE  8  @  8 : 9

> DELETE  14  @  14 : 15

> CHANGE  1 : 2  @  1 : 2

~ public abstract class Entity {

> CHANGE  44 : 45  @  44 : 45

~ 	protected EaglercraftRandom rand;

> CHANGE  27 : 28  @  27 : 29

~ 	protected EaglercraftUUID entityUniqueID;

> CHANGE  20 : 21  @  20 : 21

~ 		this.rand = new EaglercraftRandom();

> DELETE  3  @  3 : 4

> DELETE  59  @  59 : 62

> DELETE  45  @  45 : 60

> DELETE  1  @  1 : 23

> CHANGE  2 : 3  @  2 : 14

~ 		this.fire = 0;

> DELETE  1  @  1 : 5

> DELETE  9  @  9 : 13

> CHANGE  116 : 117  @  116 : 117

~ 			for (AxisAlignedBB axisalignedbb1 : (List<AxisAlignedBB>) list1) {

> CHANGE  6 : 7  @  6 : 7

~ 			for (AxisAlignedBB axisalignedbb2 : (List<AxisAlignedBB>) list1) {

> CHANGE  5 : 6  @  5 : 6

~ 			for (AxisAlignedBB axisalignedbb13 : (List<AxisAlignedBB>) list1) {

> CHANGE  17 : 18  @  17 : 18

~ 				for (AxisAlignedBB axisalignedbb6 : (List<AxisAlignedBB>) list) {

> CHANGE  6 : 7  @  6 : 7

~ 				for (AxisAlignedBB axisalignedbb7 : (List<AxisAlignedBB>) list) {

> CHANGE  6 : 7  @  6 : 7

~ 				for (AxisAlignedBB axisalignedbb8 : (List<AxisAlignedBB>) list) {

> CHANGE  7 : 8  @  7 : 8

~ 				for (AxisAlignedBB axisalignedbb9 : (List<AxisAlignedBB>) list) {

> CHANGE  6 : 7  @  6 : 7

~ 				for (AxisAlignedBB axisalignedbb10 : (List<AxisAlignedBB>) list) {

> CHANGE  6 : 7  @  6 : 7

~ 				for (AxisAlignedBB axisalignedbb11 : (List<AxisAlignedBB>) list) {

> CHANGE  18 : 19  @  18 : 19

~ 				for (AxisAlignedBB axisalignedbb12 : (List<AxisAlignedBB>) list) {

> DELETE  595  @  595 : 596

> CHANGE  55 : 57  @  55 : 56

~ 				this.entityUniqueID = new EaglercraftUUID(tagCompund.getLong("UUIDMost"),
~ 						tagCompund.getLong("UUIDLeast"));

> CHANGE  1 : 2  @  1 : 2

~ 				this.entityUniqueID = EaglercraftUUID.fromString(tagCompund.getString("UUID"));

> DELETE  9  @  9 : 10

> CHANGE  216 : 217  @  216 : 217

~ 			for (AxisAlignedBB axisalignedbb : (List<AxisAlignedBB>) list) {

> DELETE  23  @  23 : 44

> CHANGE  28 : 29  @  28 : 29

~ 		boolean flag = this.worldObj != null;

> CHANGE  183 : 184  @  183 : 184

~ 		return HString.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]",

> DELETE  25  @  25 : 59

> CHANGE  30 : 31  @  30 : 31

~ 				return EntityList.getEntityString(Entity.this) + " (" + Entity.this.getClass().getName() + ")";

> CHANGE  8 : 9  @  8 : 9

~ 		category.addCrashSection("Entity\'s Exact location", HString.format("%.2f, %.2f, %.2f",

> CHANGE  4 : 5  @  4 : 5

~ 		category.addCrashSection("Entity\'s Momentum", HString.format("%.2f, %.2f, %.2f", new Object[] {

> CHANGE  17 : 18  @  17 : 18

~ 	public EaglercraftUUID getUniqueID() {

> DELETE  62  @  62 : 66

> DELETE  51  @  51 : 63

> INSERT  22 : 48  @  22

+ 
+ 	public void renderDynamicLightsEagler(float partialTicks, boolean isInFrustum) {
+ 		double entityX = prevPosX + (posX - prevPosX) * (double) partialTicks;
+ 		double entityY = prevPosY + (posY - prevPosY) * (double) partialTicks;
+ 		double entityZ = prevPosZ + (posZ - prevPosZ) * (double) partialTicks;
+ 		double entityX2 = entityX - TileEntityRendererDispatcher.staticPlayerX;
+ 		double entityY2 = entityY - TileEntityRendererDispatcher.staticPlayerY;
+ 		double entityZ2 = entityZ - TileEntityRendererDispatcher.staticPlayerZ;
+ 		if (Math.sqrt(entityX2 * entityX2 + entityY2 * entityY2 + entityZ2 * entityZ2) < 48.0 * 48.0) {
+ 			renderDynamicLightsEaglerAt(entityX, entityY, entityZ, entityX2, entityY2, entityZ2, partialTicks,
+ 					isInFrustum);
+ 		}
+ 	}
+ 
+ 	protected void renderDynamicLightsEaglerAt(double entityX, double entityY, double entityZ, double renderX,
+ 			double renderY, double renderZ, float partialTicks, boolean isInFrustum) {
+ 		if (this.isBurning()) {
+ 			float size = Math.max(width, height);
+ 			if (size < 1.0f && !isInFrustum) {
+ 				return;
+ 			}
+ 			float mag = 5.0f * size;
+ 			DynamicLightManager.renderDynamicLight("entity_" + entityId + "_fire", entityX, entityY + height * 0.75,
+ 					entityZ, mag, 0.487f * mag, 0.1411f * mag, false);
+ 		}
+ 	}

> EOF
