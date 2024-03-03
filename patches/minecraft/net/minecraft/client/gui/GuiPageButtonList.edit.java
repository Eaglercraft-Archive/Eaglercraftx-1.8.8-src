
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import java.util.List;
+ 

> CHANGE  4 : 5  @  4 : 5

~ 

> DELETE  1  @  1 : 9

> CHANGE  22 : 24  @  22 : 23

~ 		for (int n = 0; n < this.field_178078_x.length; ++n) {
~ 			GuiPageButtonList.GuiListEntry[] aguipagebuttonlist$guilistentry = this.field_178078_x[n];

> CHANGE  86 : 91  @  86 : 90

~ 
~ 		GuiListEntry[] etr = this.field_178078_x[parInt1];
~ 		for (int i = 0; i < etr.length; ++i) {
~ 			if (etr[i] != null) {
~ 				this.func_178066_a((Gui) this.field_178073_v.lookup(etr[i].func_178935_b()), false);

> CHANGE  3 : 7  @  3 : 7

~ 		etr = this.field_178078_x[parInt2];
~ 		for (int i = 0; i < etr.length; ++i) {
~ 			if (etr[i] != null) {
~ 				this.func_178066_a((Gui) this.field_178073_v.lookup(etr[i].func_178935_b()), true);

> CHANGE  33 : 35  @  33 : 34

~ 		for (int i = 0, l = this.field_178074_u.size(); i < l; ++i) {
~ 			GuiPageButtonList.GuiEntry guipagebuttonlist$guientry = this.field_178074_u.get(i);

> CHANGE  108 : 110  @  108 : 110

~ 				for (int k = 0; k < astring.length; ++k) {
~ 					((GuiTextField) this.field_178072_w.get(j)).setText(astring[k]);

> EOF
