
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 8

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;

> DELETE  2  @  2 : 3

> CHANGE  7 : 8  @  7 : 26

~ 	private GuiButton hideAddress;

> INSERT  13 : 14  @  13

+ 		int i = 80;

> CHANGE  1 : 4  @  1 : 2

~ 		GuiButton done;
~ 		GuiButton cancel;
~ 		this.buttonList.add(done = new GuiButton(0, this.width / 2 - 100, i + 96 + 12,

> CHANGE  1 : 2  @  1 : 2

~ 		this.buttonList.add(cancel = new GuiButton(1, this.width / 2 - 100, i + 120 + 12,

> CHANGE  1 : 8  @  1 : 2

~ 		if (EagRuntime.requireSSL()) {
~ 			done.yPosition = cancel.yPosition;
~ 			done.width = (done.width / 2) - 2;
~ 			cancel.width = (cancel.width / 2) - 2;
~ 			done.xPosition += cancel.width + 4;
~ 		}
~ 		this.buttonList.add(this.serverResourcePacks = new GuiButton(2, this.width / 2 - 100, i + 54,

> INSERT  2 : 5  @  2

+ 		this.buttonList.add(this.hideAddress = new GuiButton(3, this.width / 2 - 100, i + 78,
+ 				I18n.format("addServer.hideAddress", new Object[0]) + ": "
+ 						+ I18n.format(this.serverData.hideAddress ? "gui.yes" : "gui.no", new Object[0])));

> CHANGE  6 : 7  @  6 : 9

~ 		((GuiButton) this.buttonList.get(0)).enabled = this.serverIPField.getText().trim().length() > 0;

> CHANGE  6 : 7  @  6 : 7

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  1 : 6  @  1 : 2

~ 			if (parGuiButton.id == 3) {
~ 				this.serverData.hideAddress = !this.serverData.hideAddress;
~ 				this.hideAddress.displayString = I18n.format("addServer.hideAddress", new Object[0]) + ": "
~ 						+ I18n.format(this.serverData.hideAddress ? "gui.yes" : "gui.no", new Object[0]);
~ 			} else if (parGuiButton.id == 2) {

> CHANGE  8 : 10  @  8 : 10

~ 				this.serverData.serverName = this.serverNameField.getText().trim();
~ 				this.serverData.serverIP = this.serverIPField.getText().trim();

> CHANGE  6 : 7  @  6 : 7

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  11 : 12  @  11 : 13

~ 		((GuiButton) this.buttonList.get(0)).enabled = this.serverIPField.getText().trim().length() > 0;

> CHANGE  2 : 3  @  2 : 3

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> INSERT  13 : 19  @  13

+ 		if (EagRuntime.requireSSL()) {
+ 			this.drawCenteredString(this.fontRendererObj, I18n.format("addServer.SSLWarn1"), this.width / 2, 184,
+ 					0xccccff);
+ 			this.drawCenteredString(this.fontRendererObj, I18n.format("addServer.SSLWarn2"), this.width / 2, 196,
+ 					0xccccff);
+ 		}

> EOF
