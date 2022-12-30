
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 6  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.HString;

> INSERT  4 : 5  @  3

+ 

> DELETE  8  @  7 : 10

> DELETE  4  @  7 : 10

> DELETE  3  @  6 : 7

> DELETE  8  @  9 : 10

> DELETE  14  @  15 : 16

> CHANGE  1 : 2  @  2 : 3

~ public abstract class Entity {

> CHANGE  45 : 46  @  45 : 46

~ 	protected EaglercraftRandom rand;

> CHANGE  28 : 29  @  28 : 30

~ 	protected EaglercraftUUID entityUniqueID;

> CHANGE  21 : 22  @  22 : 23

~ 		this.rand = new EaglercraftRandom();

> DELETE  4  @  4 : 5

> DELETE  59  @  60 : 63

> DELETE  45  @  48 : 63

> DELETE  1  @  16 : 38

> CHANGE  2 : 3  @  24 : 36

~ 		this.fire = 0;

> DELETE  2  @  13 : 17

> DELETE  9  @  13 : 17

> CHANGE  116 : 117  @  120 : 121

~ 			for (AxisAlignedBB axisalignedbb1 : (List<AxisAlignedBB>) list1) {

> CHANGE  7 : 8  @  7 : 8

~ 			for (AxisAlignedBB axisalignedbb2 : (List<AxisAlignedBB>) list1) {

> CHANGE  6 : 7  @  6 : 7

~ 			for (AxisAlignedBB axisalignedbb13 : (List<AxisAlignedBB>) list1) {

> CHANGE  18 : 19  @  18 : 19

~ 				for (AxisAlignedBB axisalignedbb6 : (List<AxisAlignedBB>) list) {

> CHANGE  7 : 8  @  7 : 8

~ 				for (AxisAlignedBB axisalignedbb7 : (List<AxisAlignedBB>) list) {

> CHANGE  7 : 8  @  7 : 8

~ 				for (AxisAlignedBB axisalignedbb8 : (List<AxisAlignedBB>) list) {

> CHANGE  8 : 9  @  8 : 9

~ 				for (AxisAlignedBB axisalignedbb9 : (List<AxisAlignedBB>) list) {

> CHANGE  7 : 8  @  7 : 8

~ 				for (AxisAlignedBB axisalignedbb10 : (List<AxisAlignedBB>) list) {

> CHANGE  7 : 8  @  7 : 8

~ 				for (AxisAlignedBB axisalignedbb11 : (List<AxisAlignedBB>) list) {

> CHANGE  19 : 20  @  19 : 20

~ 				for (AxisAlignedBB axisalignedbb12 : (List<AxisAlignedBB>) list) {

> DELETE  596  @  596 : 597

> CHANGE  55 : 57  @  56 : 57

~ 				this.entityUniqueID = new EaglercraftUUID(tagCompund.getLong("UUIDMost"),
~ 						tagCompund.getLong("UUIDLeast"));

> CHANGE  3 : 4  @  2 : 3

~ 				this.entityUniqueID = EaglercraftUUID.fromString(tagCompund.getString("UUID"));

> DELETE  10  @  10 : 11

> CHANGE  216 : 217  @  217 : 218

~ 			for (AxisAlignedBB axisalignedbb : (List<AxisAlignedBB>) list) {

> DELETE  24  @  24 : 45

> CHANGE  28 : 29  @  49 : 50

~ 		boolean flag = this.worldObj != null;

> CHANGE  184 : 185  @  184 : 185

~ 		return HString.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]",

> DELETE  26  @  26 : 60

> CHANGE  30 : 31  @  64 : 65

~ 				return EntityList.getEntityString(Entity.this) + " (" + Entity.this.getClass().getName() + ")";

> CHANGE  9 : 10  @  9 : 10

~ 		category.addCrashSection("Entity\'s Exact location", HString.format("%.2f, %.2f, %.2f",

> CHANGE  5 : 6  @  5 : 6

~ 		category.addCrashSection("Entity\'s Momentum", HString.format("%.2f, %.2f, %.2f", new Object[] {

> CHANGE  18 : 19  @  18 : 19

~ 	public EaglercraftUUID getUniqueID() {

> DELETE  63  @  63 : 67

> DELETE  51  @  55 : 67

> EOF
