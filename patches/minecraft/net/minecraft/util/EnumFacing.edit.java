
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import java.util.Iterator;
+ import java.util.Map;
+ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
+ 

> DELETE  3  @  3 : 9

> INSERT  9 : 11  @  9

+ 	public static final EnumFacing[] _VALUES = values();
+ 

> CHANGE  8 : 9  @  8 : 9

~ 	public static final EnumFacing[] HORIZONTALS = new EnumFacing[4];

> CHANGE  153 : 155  @  153 : 155

~ 	public static EnumFacing random(EaglercraftRandom rand) {
~ 		return _VALUES[rand.nextInt(_VALUES.length)];

> CHANGE  6 : 9  @  6 : 7

~ 		EnumFacing[] facings = _VALUES;
~ 		for (int i = 0; i < facings.length; ++i) {
~ 			EnumFacing enumfacing1 = facings[i];

> CHANGE  21 : 24  @  21 : 22

~ 		EnumFacing[] facings = EnumFacing._VALUES;
~ 		for (int i = 0; i < facings.length; ++i) {
~ 			EnumFacing enumfacing = facings[i];

> CHANGE  13 : 17  @  13 : 14

~ 		Plane.bootstrap();
~ 		EnumFacing[] facings = EnumFacing._VALUES;
~ 		for (int i = 0; i < facings.length; ++i) {
~ 			EnumFacing enumfacing = facings[i];

> CHANGE  55 : 58  @  55 : 57

~ 			EnumFacing.Axis[] axis = values();
~ 			for (int i = 0; i < axis.length; ++i) {
~ 				NAME_LOOKUP.put(axis[i].getName2().toLowerCase(), axis[i]);

> INSERT  8 : 10  @  8

+ 		public static final AxisDirection[] _VALUES = values();
+ 

> CHANGE  18 : 19  @  18 : 19

~ 		HORIZONTAL(new EnumFacing[4]), VERTICAL(new EnumFacing[2]);

> INSERT  1 : 7  @  1

+ 		public final EnumFacing[] facingsArray;
+ 
+ 		private Plane(EnumFacing[] facingsArray) {
+ 			this.facingsArray = facingsArray;
+ 		}
+ 

> CHANGE  1 : 2  @  1 : 9

~ 			return facingsArray;

> CHANGE  2 : 3  @  2 : 3

~ 		public EnumFacing random(EaglercraftRandom rand) {

> CHANGE  9 : 10  @  9 : 10

~ 			return Iterators.forArray(facingsArray);

> INSERT  1 : 10  @  1

+ 
+ 		private static void bootstrap() {
+ 			HORIZONTAL.facingsArray[0] = EnumFacing.NORTH;
+ 			HORIZONTAL.facingsArray[1] = EnumFacing.EAST;
+ 			HORIZONTAL.facingsArray[2] = EnumFacing.SOUTH;
+ 			HORIZONTAL.facingsArray[3] = EnumFacing.WEST;
+ 			VERTICAL.facingsArray[0] = EnumFacing.UP;
+ 			VERTICAL.facingsArray[1] = EnumFacing.DOWN;
+ 		}

> EOF
