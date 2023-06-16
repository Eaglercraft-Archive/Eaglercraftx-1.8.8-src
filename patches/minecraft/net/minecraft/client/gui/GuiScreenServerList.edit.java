
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 6

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;

> DELETE  2  @  2 : 3

> CHANGE  22 : 28  @  22 : 23

~ 		if (EagRuntime.requireSSL()) {
~ 			this.field_146302_g = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, this.height / 4 + 35,
~ 					200, 20);
~ 		} else {
~ 			this.field_146302_g = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, 116, 200, 20);
~ 		}

> CHANGE  3 : 4  @  3 : 5

~ 		((GuiButton) this.buttonList.get(0)).enabled = this.field_146302_g.getText().trim().length() > 0;

> CHANGE  8 : 9  @  8 : 9

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  4 : 5  @  4 : 5

~ 				this.field_146301_f.serverIP = this.field_146302_g.getText().trim();

> CHANGE  6 : 7  @  6 : 7

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  1 : 2  @  1 : 3

~ 			((GuiButton) this.buttonList.get(0)).enabled = this.field_146302_g.getText().trim().length() > 0;

> CHANGE  6 : 7  @  6 : 7

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  8 : 19  @  8 : 10

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
