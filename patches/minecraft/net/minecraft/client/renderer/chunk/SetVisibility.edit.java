
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  4 : 5  @  4

+ 

> CHANGE  3 : 4  @  3 : 4

~ 	private static final int COUNT_FACES = EnumFacing._VALUES.length;

> CHANGE  7 : 11  @  7 : 10

~ 		EnumFacing[] facings = EnumFacing._VALUES;
~ 		for (int i = 0; i < facings.length; ++i) {
~ 			for (int j = 0; j < facings.length; ++j) {
~ 				this.setVisible(facings[i], facings[j], true);

> CHANGE  22 : 25  @  22 : 24

~ 		EnumFacing[] facings = EnumFacing._VALUES;
~ 		for (int i = 0; i < facings.length; ++i) {
~ 			stringbuilder.append(' ').append(facings[i].toString().toUpperCase().charAt(0));

> CHANGE  4 : 6  @  4 : 5

~ 		for (int i = 0; i < facings.length; ++i) {
~ 			EnumFacing enumfacing2 = facings[i];

> CHANGE  2 : 4  @  2 : 4

~ 			for (int j = 0; j < facings.length; ++j) {
~ 				if (enumfacing2 == facings[j]) {

> CHANGE  2 : 3  @  2 : 3

~ 					boolean flag = this.isVisible(enumfacing2, facings[j]);

> EOF
