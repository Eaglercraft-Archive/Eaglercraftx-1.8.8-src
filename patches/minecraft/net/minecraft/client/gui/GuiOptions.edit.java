
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.vfs.SYS;

> DELETE  6  @  5 : 22

> DELETE  2  @  19 : 20

> INSERT  12 : 14  @  13

+ 	private GuiButton notSoSuperSecret;
+ 	private GuiButton broadcastSettings;

> CHANGE  48 : 50  @  46 : 48

~ 		this.buttonList.add(notSoSuperSecret = new GuiButton(8675309, this.width / 2 + 5, this.height / 6 + 48 - 6, 150,
~ 				20, "Super Secret Settings...") {

> CHANGE  15 : 18  @  15 : 17

~ 		this.buttonList.add(broadcastSettings = new GuiButton(107, this.width / 2 + 5, this.height / 6 + 72 - 6, 150,
~ 				20, I18n.format(EagRuntime.getRecText(), new Object[0])));
~ 		broadcastSettings.enabled = EagRuntime.recSupported();

> CHANGE  11 : 13  @  10 : 11

~ 		GuiButton rp;
~ 		this.buttonList.add(rp = new GuiButton(105, this.width / 2 - 155, this.height / 6 + 144 - 6, 150, 20,

> CHANGE  3 : 5  @  2 : 3

~ 		GuiButton b;
~ 		this.buttonList.add(b = new GuiButton(104, this.width / 2 + 5, this.height / 6 + 144 - 6, 150, 20,

> INSERT  3 : 4  @  2

+ 		b.enabled = false;

> INSERT  3 : 5  @  2

+ 
+ 		rp.enabled = SYS.VFS != null;

> CHANGE  24 : 25  @  22 : 23

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  33 : 34  @  33 : 34

~ 				notSoSuperSecret.displayString = "Nope!";

> DELETE  23  @  23 : 28

> CHANGE  16 : 18  @  21 : 28

~ 				EagRuntime.toggleRec();
~ 				broadcastSettings.displayString = I18n.format(EagRuntime.getRecText(), new Object[0]);

> DELETE  3  @  8 : 9

> EOF
