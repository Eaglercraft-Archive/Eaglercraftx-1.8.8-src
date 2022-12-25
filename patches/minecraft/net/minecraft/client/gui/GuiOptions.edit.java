
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.vfs.SYS;

> DELETE  8  @  7 : 24

> DELETE  10  @  26 : 27

> INSERT  22 : 24  @  39

+ 	private GuiButton notSoSuperSecret;
+ 	private GuiButton broadcastSettings;

> CHANGE  70 : 72  @  85 : 87

~ 		this.buttonList.add(notSoSuperSecret = new GuiButton(8675309, this.width / 2 + 5, this.height / 6 + 48 - 6, 150,
~ 				20, "Super Secret Settings...") {

> CHANGE  85 : 88  @  100 : 102

~ 		this.buttonList.add(broadcastSettings = new GuiButton(107, this.width / 2 + 5, this.height / 6 + 72 - 6, 150,
~ 				20, I18n.format(EagRuntime.getRecText(), new Object[0])));
~ 		broadcastSettings.enabled = EagRuntime.recSupported();

> CHANGE  96 : 98  @  110 : 111

~ 		GuiButton rp;
~ 		this.buttonList.add(rp = new GuiButton(105, this.width / 2 - 155, this.height / 6 + 144 - 6, 150, 20,

> CHANGE  99 : 101  @  112 : 113

~ 		GuiButton b;
~ 		this.buttonList.add(b = new GuiButton(104, this.width / 2 + 5, this.height / 6 + 144 - 6, 150, 20,

> INSERT  102 : 103  @  114

+ 		b.enabled = false;

> INSERT  105 : 107  @  116

+ 
+ 		rp.enabled = SYS.VFS != null;

> CHANGE  129 : 130  @  138 : 139

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  162 : 163  @  171 : 172

~ 				notSoSuperSecret.displayString = "Nope!";

> DELETE  185  @  194 : 199

> CHANGE  201 : 203  @  215 : 222

~ 				EagRuntime.toggleRec();
~ 				broadcastSettings.displayString = I18n.format(EagRuntime.getRecText(), new Object[0]);

> DELETE  204  @  223 : 224

> EOF
