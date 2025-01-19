
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 4

~ import java.util.List;
~ 

> CHANGE  68 : 71  @  68 : 69

~ 		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facings();
~ 		for (int i = 0; i < facings.length; ++i) {
~ 			EnumFacing enumfacing = facings[i];

> CHANGE  61 : 62  @  61 : 64

~ 		if (!worldIn.isRemote) {

> DELETE  67  @  67 : 68

> INSERT  1 : 2  @  1

+ 		return state;

> CHANGE  5 : 8  @  5 : 6

~ 		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 		for (int i = 0; i < facings.length; ++i) {
~ 			EnumFacing enumfacing1 = facings[i];

> CHANGE  80 : 83  @  80 : 81

~ 			EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 			for (int i = 0; i < facings.length; ++i) {
~ 				EnumFacing enumfacing = facings[i];

> CHANGE  30 : 31  @  30 : 33

~ 		{

> CHANGE  23 : 26  @  23 : 24

~ 				EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 				for (int i = 0; i < facings.length; ++i) {
~ 					EnumFacing enumfacing = facings[i];

> CHANGE  61 : 62  @  61 : 62

~ 		List<Entity> entityList = worldIn.getEntitiesWithinAABB(EntityOcelot.class,

> CHANGE  1 : 4  @  1 : 2

~ 						(double) (pos.getX() + 1), (double) (pos.getY() + 2), (double) (pos.getZ() + 1)));
~ 		for (int i = 0, l = entityList.size(); i < l; ++i) {
~ 			Entity entity = entityList.get(i);

> EOF
