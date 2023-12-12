
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 9  @  2 : 24

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.gui.GuiShaderConfig;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.gui.GuiShadersNotSupported;
~ import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
~ import net.lax1dude.eaglercraft.v1_8.vfs.SYS;

> DELETE  2  @  2 : 3

> INSERT  12 : 13  @  12

+ 	private GuiButton broadcastSettings;

> CHANGE  47 : 48  @  47 : 59

~ 				I18n.format("shaders.gui.optionsButton")));

> CHANGE  2 : 5  @  2 : 4

~ 		this.buttonList.add(broadcastSettings = new GuiButton(107, this.width / 2 + 5, this.height / 6 + 72 - 6, 150,
~ 				20, I18n.format(EagRuntime.getRecText(), new Object[0])));
~ 		broadcastSettings.enabled = EagRuntime.recSupported();

> CHANGE  8 : 10  @  8 : 9

~ 		GuiButton rp;
~ 		this.buttonList.add(rp = new GuiButton(105, this.width / 2 - 155, this.height / 6 + 144 - 6, 150, 20,

> CHANGE  1 : 5  @  1 : 3

~ 		GuiButton dbg;
~ 		this.buttonList.add(dbg = new GuiButton(104, this.width / 2 + 5, this.height / 6 + 144 - 6, 150, 20,
~ 				I18n.format("options.debugConsoleButton", new Object[0])));
~ 		dbg.enabled = EagRuntime.getPlatformType() != EnumPlatformType.DESKTOP;

> INSERT  2 : 4  @  2

+ 
+ 		rp.enabled = SYS.VFS != null;

> CHANGE  22 : 23  @  22 : 23

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  11 : 13  @  11

+ 				SingleplayerServerController
+ 						.setDifficulty(this.mc.theWorld.getWorldInfo().getDifficulty().getDifficultyId());

> CHANGE  21 : 27  @  21 : 22

~ 				if (EaglerDeferredPipeline.isSupported()) {
~ 					this.mc.displayGuiScreen(new GuiShaderConfig(this));
~ 				} else {
~ 					this.mc.displayGuiScreen(new GuiShadersNotSupported(this,
~ 							I18n.format(EaglerDeferredPipeline.getReasonUnsupported())));
~ 				}

> DELETE  22  @  22 : 27

> CHANGE  16 : 18  @  16 : 23

~ 				EagRuntime.toggleRec();
~ 				broadcastSettings.displayString = I18n.format(EagRuntime.getRecText(), new Object[0]);

> INSERT  2 : 5  @  2

+ 			if (parGuiButton.id == 104) {
+ 				EagRuntime.showDebugConsole();
+ 			}

> EOF
