
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 7  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ 

> INSERT  1 : 3  @  1

+ 
+ import net.lax1dude.eaglercraft.v1_8.sp.server.CrashReportHelper;

> CHANGE  10 : 12  @  10 : 12

~ import net.minecraft.crash.CrashReport;
~ import net.minecraft.crash.CrashReportCategory;

> CHANGE  13 : 17  @  13 : 17

~ import net.minecraft.nbt.NBTTagCompound;
~ import net.minecraft.nbt.NBTTagDouble;
~ import net.minecraft.nbt.NBTTagFloat;
~ import net.minecraft.nbt.NBTTagList;

> CHANGE  69 : 70  @  69 : 70

~ 	protected EaglercraftRandom rand;

> CHANGE  27 : 28  @  27 : 28

~ 	protected EaglercraftUUID entityUniqueID;

> CHANGE  21 : 22  @  21 : 22

~ 		this.rand = new EaglercraftRandom();

> CHANGE  63 : 64  @  63 : 64

~ 			if (this.width > f2 && !this.firstUpdate) {

> CHANGE  47 : 48  @  47 : 48

~ 		if (this.worldObj instanceof WorldServer) {

> CHANGE  39 : 40  @  39 : 42

~ 		if (this.fire > 0) {

> CHANGE  23 : 24  @  23 : 26

~ 		this.setFlag(0, this.fire > 0);

> CHANGE  117 : 118  @  117 : 118

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

> CHANGE  142 : 143  @  142 : 143

~ 							CrashReportHelper.addIntegratedServerBlockInfo(crashreportcategory, blockpos2, iblockstate);

> CHANGE  508 : 510  @  508 : 509

~ 				this.entityUniqueID = new EaglercraftUUID(tagCompund.getLong("UUIDMost"),
~ 						tagCompund.getLong("UUIDLeast"));

> CHANGE  1 : 2  @  1 : 2

~ 				this.entityUniqueID = EaglercraftUUID.fromString(tagCompund.getString("UUID"));

> CHANGE  226 : 227  @  226 : 227

~ 			for (AxisAlignedBB axisalignedbb : (List<AxisAlignedBB>) list) {

> CHANGE  23 : 24  @  23 : 24

~ 			if (!parBlockPos.equals(this.field_181016_an)) {

> CHANGE  48 : 49  @  48 : 50

~ 		return !this.isImmuneToFire && this.fire > 0;

> CHANGE  182 : 183  @  182 : 183

~ 		return HString.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]",

> CHANGE  25 : 26  @  25 : 26

~ 		if (!this.isDead) {

> CHANGE  72 : 73  @  72 : 73

~ 		category.addCrashSection("Entity\'s Exact location", HString.format("%.2f, %.2f, %.2f",

> CHANGE  4 : 5  @  4 : 5

~ 		category.addCrashSection("Entity\'s Momentum", HString.format("%.2f, %.2f, %.2f", new Object[] {

> CHANGE  17 : 18  @  17 : 18

~ 	public EaglercraftUUID getUniqueID() {

> EOF
