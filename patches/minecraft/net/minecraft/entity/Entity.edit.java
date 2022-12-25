
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 6  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.HString;

> INSERT  7 : 8  @  6

+ 

> DELETE  15  @  13 : 16

> DELETE  19  @  20 : 23

> DELETE  22  @  26 : 27

> DELETE  30  @  35 : 36

> DELETE  44  @  50 : 51

> CHANGE  45 : 46  @  52 : 53

~ public abstract class Entity {

> CHANGE  90 : 91  @  97 : 98

~ 	protected EaglercraftRandom rand;

> CHANGE  118 : 119  @  125 : 127

~ 	protected EaglercraftUUID entityUniqueID;

> CHANGE  139 : 140  @  147 : 148

~ 		this.rand = new EaglercraftRandom();

> DELETE  143  @  151 : 152

> DELETE  202  @  211 : 214

> DELETE  247  @  259 : 274

> DELETE  248  @  275 : 297

> CHANGE  250 : 251  @  299 : 311

~ 		this.fire = 0;

> DELETE  252  @  312 : 316

> DELETE  261  @  325 : 329

> CHANGE  377 : 378  @  445 : 446

~ 			for (AxisAlignedBB axisalignedbb1 : (List<AxisAlignedBB>) list1) {

> CHANGE  384 : 385  @  452 : 453

~ 			for (AxisAlignedBB axisalignedbb2 : (List<AxisAlignedBB>) list1) {

> CHANGE  390 : 391  @  458 : 459

~ 			for (AxisAlignedBB axisalignedbb13 : (List<AxisAlignedBB>) list1) {

> CHANGE  408 : 409  @  476 : 477

~ 				for (AxisAlignedBB axisalignedbb6 : (List<AxisAlignedBB>) list) {

> CHANGE  415 : 416  @  483 : 484

~ 				for (AxisAlignedBB axisalignedbb7 : (List<AxisAlignedBB>) list) {

> CHANGE  422 : 423  @  490 : 491

~ 				for (AxisAlignedBB axisalignedbb8 : (List<AxisAlignedBB>) list) {

> CHANGE  430 : 431  @  498 : 499

~ 				for (AxisAlignedBB axisalignedbb9 : (List<AxisAlignedBB>) list) {

> CHANGE  437 : 438  @  505 : 506

~ 				for (AxisAlignedBB axisalignedbb10 : (List<AxisAlignedBB>) list) {

> CHANGE  444 : 445  @  512 : 513

~ 				for (AxisAlignedBB axisalignedbb11 : (List<AxisAlignedBB>) list) {

> CHANGE  463 : 464  @  531 : 532

~ 				for (AxisAlignedBB axisalignedbb12 : (List<AxisAlignedBB>) list) {

> DELETE  1059  @  1127 : 1128

> CHANGE  1114 : 1116  @  1183 : 1184

~ 				this.entityUniqueID = new EaglercraftUUID(tagCompund.getLong("UUIDMost"),
~ 						tagCompund.getLong("UUIDLeast"));

> CHANGE  1117 : 1118  @  1185 : 1186

~ 				this.entityUniqueID = EaglercraftUUID.fromString(tagCompund.getString("UUID"));

> DELETE  1127  @  1195 : 1196

> CHANGE  1343 : 1344  @  1412 : 1413

~ 			for (AxisAlignedBB axisalignedbb : (List<AxisAlignedBB>) list) {

> DELETE  1367  @  1436 : 1457

> CHANGE  1395 : 1396  @  1485 : 1486

~ 		boolean flag = this.worldObj != null;

> CHANGE  1579 : 1580  @  1669 : 1670

~ 		return HString.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]",

> DELETE  1605  @  1695 : 1729

> CHANGE  1635 : 1636  @  1759 : 1760

~ 				return EntityList.getEntityString(Entity.this) + " (" + Entity.this.getClass().getName() + ")";

> CHANGE  1644 : 1645  @  1768 : 1769

~ 		category.addCrashSection("Entity\'s Exact location", HString.format("%.2f, %.2f, %.2f",

> CHANGE  1649 : 1650  @  1773 : 1774

~ 		category.addCrashSection("Entity\'s Momentum", HString.format("%.2f, %.2f, %.2f", new Object[] {

> CHANGE  1667 : 1668  @  1791 : 1792

~ 	public EaglercraftUUID getUniqueID() {

> DELETE  1730  @  1854 : 1858

> DELETE  1781  @  1909 : 1921

> EOF
