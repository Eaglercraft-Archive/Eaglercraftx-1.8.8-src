
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 6  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.HString;

> INSERT  1 : 2  @  1

+ 

> DELETE  7  @  7 : 10

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

> EOF
