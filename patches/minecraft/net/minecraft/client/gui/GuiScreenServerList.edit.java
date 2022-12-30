
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 6

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;

> DELETE  4  @  6 : 7

> CHANGE  22 : 28  @  23 : 24

~ 		if (EagRuntime.requireSSL()) {
~ 			this.field_146302_g = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, this.height / 4 + 35,
~ 					200, 20);
~ 		} else {
~ 			this.field_146302_g = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, 116, 200, 20);
~ 		}

> CHANGE  9 : 10  @  4 : 6

~ 		((GuiButton) this.buttonList.get(0)).enabled = this.field_146302_g.getText().trim().length() > 0;

> CHANGE  9 : 10  @  10 : 11

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  5 : 6  @  5 : 6

~ 				this.field_146301_f.serverIP = this.field_146302_g.getText().trim();

> CHANGE  7 : 8  @  7 : 8

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  2 : 3  @  2 : 4

~ 			((GuiButton) this.buttonList.get(0)).enabled = this.field_146302_g.getText().trim().length() > 0;

> CHANGE  7 : 8  @  8 : 9

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  9 : 20  @  9 : 11

~ 		if (EagRuntime.requireSSL()) {
~ 			this.drawString(this.fontRendererObj, I18n.format("addServer.enterIp", new Object[0]), this.width / 2 - 100,
~ 					this.height / 4 + 19, 10526880);
~ 			this.drawCenteredString(this.fontRendererObj, I18n.format("addServer.SSLWarn1"), this.width / 2,
~ 					this.height / 4 + 30 + 37, 0xccccff);
~ 			this.drawCenteredString(this.fontRendererObj, I18n.format("addServer.SSLWarn2"), this.width / 2,
~ 					this.height / 4 + 30 + 49, 0xccccff);
~ 		} else {
~ 			this.drawString(this.fontRendererObj, I18n.format("addServer.enterIp", new Object[0]), this.width / 2 - 100,
~ 					100, 10526880);
~ 		}

> EOF
