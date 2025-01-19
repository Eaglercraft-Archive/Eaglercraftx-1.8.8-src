
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  4 : 11  @  4

+ 
+ import com.carrotsearch.hppc.cursors.ObjectCursor;
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  6  @  6 : 7

> DELETE  2  @  2 : 3

> DELETE  11  @  11 : 12

> INSERT  32 : 39  @  32

+ 	public void handleTouchInput() throws IOException {
+ 		super.handleTouchInput();
+ 		if (this.displaySlot != null) {
+ 			this.displaySlot.handleTouchInput();
+ 		}
+ 	}
+ 

> CHANGE  39 : 40  @  39 : 40

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  111 : 112  @  111 : 112

~ 			if (!PointerInputAbstraction.getVCursorButtonDown(0)) {

> CHANGE  158 : 160  @  158 : 159

~ 			for (int m = 0, l = StatList.objectMineStats.size(); m < l; ++m) {
~ 				StatCrafting statcrafting = StatList.objectMineStats.get(m);

> CHANGE  133 : 135  @  133 : 134

~ 			for (int m = 0, l = StatList.itemStats.size(); m < l; ++m) {
~ 				StatCrafting statcrafting = StatList.itemStats.get(m);

> CHANGE  100 : 102  @  100 : 101

~ 			for (ObjectCursor<EntityList.EntityEggInfo> entitylist$entityegginfo_ : EntityList.entityEggs.values()) {
~ 				EntityList.EntityEggInfo entitylist$entityegginfo = entitylist$entityegginfo_.value;

> EOF
