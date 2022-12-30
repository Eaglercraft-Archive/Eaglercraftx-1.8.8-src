
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 8

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;

> DELETE  4  @  8 : 9

> CHANGE  7 : 8  @  8 : 27

~ 	private GuiButton hideAddress;

> INSERT  14 : 15  @  32

+ 		int i = 80;

> CHANGE  2 : 5  @  1 : 2

~ 		GuiButton done;
~ 		GuiButton cancel;
~ 		this.buttonList.add(done = new GuiButton(0, this.width / 2 - 100, i + 96 + 12,

> CHANGE  4 : 5  @  2 : 3

~ 		this.buttonList.add(cancel = new GuiButton(1, this.width / 2 - 100, i + 120 + 12,

> CHANGE  2 : 9  @  2 : 3

~ 		if (EagRuntime.requireSSL()) {
~ 			done.yPosition = cancel.yPosition;
~ 			done.width = (done.width / 2) - 2;
~ 			cancel.width = (cancel.width / 2) - 2;
~ 			done.xPosition += cancel.width + 4;
~ 		}
~ 		this.buttonList.add(this.serverResourcePacks = new GuiButton(2, this.width / 2 - 100, i + 54,

> INSERT  9 : 12  @  3

+ 		this.buttonList.add(this.hideAddress = new GuiButton(3, this.width / 2 - 100, i + 78,
+ 				I18n.format("addServer.hideAddress", new Object[0]) + ": "
+ 						+ I18n.format(this.serverData.hideAddress ? "gui.yes" : "gui.no", new Object[0])));

> CHANGE  9 : 10  @  6 : 9

~ 		((GuiButton) this.buttonList.get(0)).enabled = this.serverIPField.getText().trim().length() > 0;

> CHANGE  7 : 8  @  9 : 10

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  2 : 7  @  2 : 3

~ 			if (parGuiButton.id == 3) {
~ 				this.serverData.hideAddress = !this.serverData.hideAddress;
~ 				this.hideAddress.displayString = I18n.format("addServer.hideAddress", new Object[0]) + ": "
~ 						+ I18n.format(this.serverData.hideAddress ? "gui.yes" : "gui.no", new Object[0]);
~ 			} else if (parGuiButton.id == 2) {

> CHANGE  13 : 15  @  9 : 11

~ 				this.serverData.serverName = this.serverNameField.getText().trim();
~ 				this.serverData.serverIP = this.serverIPField.getText().trim();

> CHANGE  8 : 9  @  8 : 9

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  12 : 13  @  12 : 14

~ 		((GuiButton) this.buttonList.get(0)).enabled = this.serverIPField.getText().trim().length() > 0;

> CHANGE  3 : 4  @  4 : 5

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> INSERT  14 : 20  @  14

+ 		if (EagRuntime.requireSSL()) {
+ 			this.drawCenteredString(this.fontRendererObj, I18n.format("addServer.SSLWarn1"), this.width / 2, 184,
+ 					0xccccff);
+ 			this.drawCenteredString(this.fontRendererObj, I18n.format("addServer.SSLWarn2"), this.width / 2, 196,
+ 					0xccccff);
+ 		}

> EOF
