
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import java.util.List;
+ 

> CHANGE  4 : 7  @  4 : 5

~ 
~ import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;

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

> INSERT  31 : 46  @  31

+ 	public boolean isTextFieldFocused() {
+ 		for (GuiTextField txt : field_178072_w) {
+ 			if (txt.isFocused()) {
+ 				return true;
+ 			}
+ 		}
+ 		return false;
+ 	}
+ 
+ 	public void fireInputEvent(EnumInputEvent event, String param) {
+ 		for (GuiTextField txt : field_178072_w) {
+ 			txt.fireInputEvent(event, param);
+ 		}
+ 	}
+ 

> CHANGE  93 : 100  @  93 : 95

~ 			if (k != 0 && k != 12345)
~ 				return false;
~ 			boolean touchMode = PointerInputAbstraction.isTouchMode();
~ 			boolean flag = this.field_178029_b != null && (!touchMode || stupidCheck(this.field_178029_b, k))
~ 					&& this.func_178026_a(this.field_178029_b, i, j, k);
~ 			boolean flag1 = this.field_178030_c != null && (!touchMode || stupidCheck(this.field_178030_c, k))
~ 					&& this.func_178026_a(this.field_178030_c, i, j, k);

> INSERT  3 : 11  @  3

+ 		private static boolean stupidCheck(Gui gui, int k) {
+ 			if (gui instanceof GuiButton) {
+ 				return ((GuiButton) gui).isSliderTouchEvents() == (k == 12345);
+ 			} else {
+ 				return k != 12345;
+ 			}
+ 		}
+ 

> CHANGE  32 : 39  @  32 : 34

~ 			if (k != 0 && k != 12345)
~ 				return;
~ 			boolean touchMode = PointerInputAbstraction.isTouchMode();
~ 			if (!touchMode || stupidCheck(field_178029_b, k))
~ 				this.func_178016_b(this.field_178029_b, i, j, k);
~ 			if (!touchMode || stupidCheck(field_178030_c, k))
~ 				this.func_178016_b(this.field_178030_c, i, j, k);

> EOF
