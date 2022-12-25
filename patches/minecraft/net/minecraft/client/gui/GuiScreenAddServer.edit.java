
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 8

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;

> DELETE  6  @  10 : 11

> CHANGE  13 : 14  @  18 : 37

~ 	private GuiButton hideAddress;

> INSERT  27 : 28  @  50

+ 		int i = 80;

> CHANGE  29 : 32  @  51 : 52

~ 		GuiButton done;
~ 		GuiButton cancel;
~ 		this.buttonList.add(done = new GuiButton(0, this.width / 2 - 100, i + 96 + 12,

> CHANGE  33 : 34  @  53 : 54

~ 		this.buttonList.add(cancel = new GuiButton(1, this.width / 2 - 100, i + 120 + 12,

> CHANGE  35 : 42  @  55 : 56

~ 		if (EagRuntime.requireSSL()) {
~ 			done.yPosition = cancel.yPosition;
~ 			done.width = (done.width / 2) - 2;
~ 			cancel.width = (cancel.width / 2) - 2;
~ 			done.xPosition += cancel.width + 4;
~ 		}
~ 		this.buttonList.add(this.serverResourcePacks = new GuiButton(2, this.width / 2 - 100, i + 54,

> INSERT  44 : 47  @  58

+ 		this.buttonList.add(this.hideAddress = new GuiButton(3, this.width / 2 - 100, i + 78,
+ 				I18n.format("addServer.hideAddress", new Object[0]) + ": "
+ 						+ I18n.format(this.serverData.hideAddress ? "gui.yes" : "gui.no", new Object[0])));

> CHANGE  53 : 54  @  64 : 67

~ 		((GuiButton) this.buttonList.get(0)).enabled = this.serverIPField.getText().trim().length() > 0;

> CHANGE  60 : 61  @  73 : 74

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  62 : 67  @  75 : 76

~ 			if (parGuiButton.id == 3) {
~ 				this.serverData.hideAddress = !this.serverData.hideAddress;
~ 				this.hideAddress.displayString = I18n.format("addServer.hideAddress", new Object[0]) + ": "
~ 						+ I18n.format(this.serverData.hideAddress ? "gui.yes" : "gui.no", new Object[0]);
~ 			} else if (parGuiButton.id == 2) {

> CHANGE  75 : 77  @  84 : 86

~ 				this.serverData.serverName = this.serverNameField.getText().trim();
~ 				this.serverData.serverIP = this.serverIPField.getText().trim();

> CHANGE  83 : 84  @  92 : 93

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  95 : 96  @  104 : 106

~ 		((GuiButton) this.buttonList.get(0)).enabled = this.serverIPField.getText().trim().length() > 0;

> CHANGE  98 : 99  @  108 : 109

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> INSERT  112 : 118  @  122

+ 		if (EagRuntime.requireSSL()) {
+ 			this.drawCenteredString(this.fontRendererObj, I18n.format("addServer.SSLWarn1"), this.width / 2, 184,
+ 					0xccccff);
+ 			this.drawCenteredString(this.fontRendererObj, I18n.format("addServer.SSLWarn2"), this.width / 2, 196,
+ 					0xccccff);
+ 		}

> EOF
