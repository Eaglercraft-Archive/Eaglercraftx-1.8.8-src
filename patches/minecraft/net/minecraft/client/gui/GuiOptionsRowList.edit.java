
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 5  @  1

+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;

> DELETE  1  @  1 : 5

> CHANGE  72 : 77  @  72 : 73

~ 			if (var4 != 0 && var4 != 12345)
~ 				return false;
~ 			boolean touchMode = PointerInputAbstraction.isTouchMode();
~ 			if ((!touchMode || (this.field_148323_b.isSliderTouchEvents() == (var4 == 12345)))
~ 					&& this.field_148323_b.mousePressed(this.field_148325_a, i, j)) {

> CHANGE  8 : 11  @  8 : 9

~ 			} else if (this.field_148324_c != null
~ 					&& (!touchMode || (this.field_148324_c.isSliderTouchEvents() == (var4 == 12345)))
~ 					&& this.field_148324_c.mousePressed(this.field_148325_a, i, j)) {

> CHANGE  14 : 19  @  14 : 15

~ 			if (var4 != 0 && var4 != 12345)
~ 				return;
~ 			boolean touchMode = PointerInputAbstraction.isTouchMode();
~ 			if (this.field_148323_b != null
~ 					&& (!touchMode || (this.field_148323_b.isSliderTouchEvents() == (var4 == 12345)))) {

> CHANGE  3 : 5  @  3 : 4

~ 			if (this.field_148324_c != null
~ 					&& (!touchMode || (this.field_148324_c.isSliderTouchEvents() == (var4 == 12345)))) {

> INSERT  8 : 53  @  8

+ 
+ 	public GuiOptionButton getButtonFor(GameSettings.Options enumOption) {
+ 		for (Row r : field_148184_k) {
+ 			if (r.field_148323_b != null) {
+ 				if (r.field_148323_b instanceof GuiOptionButton) {
+ 					GuiOptionButton btn = (GuiOptionButton) r.field_148323_b;
+ 					if (btn.returnEnumOptions() == enumOption) {
+ 						return btn;
+ 					}
+ 				}
+ 			}
+ 			if (r.field_148324_c != null) {
+ 				if (r.field_148324_c instanceof GuiOptionButton) {
+ 					GuiOptionButton btn = (GuiOptionButton) r.field_148324_c;
+ 					if (btn.returnEnumOptions() == enumOption) {
+ 						return btn;
+ 					}
+ 				}
+ 			}
+ 		}
+ 		return null;
+ 	}
+ 
+ 	public GuiOptionSlider getSliderFor(GameSettings.Options enumOption) {
+ 		for (Row r : field_148184_k) {
+ 			if (r.field_148323_b != null) {
+ 				if (r.field_148323_b instanceof GuiOptionSlider) {
+ 					GuiOptionSlider btn = (GuiOptionSlider) r.field_148323_b;
+ 					if (btn.getEnumOptions() == enumOption) {
+ 						return btn;
+ 					}
+ 				}
+ 			}
+ 			if (r.field_148324_c != null) {
+ 				if (r.field_148324_c instanceof GuiOptionSlider) {
+ 					GuiOptionSlider btn = (GuiOptionSlider) r.field_148324_c;
+ 					if (btn.getEnumOptions() == enumOption) {
+ 						return btn;
+ 					}
+ 				}
+ 			}
+ 		}
+ 		return null;
+ 	}
+ 

> EOF
