
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import java.io.IOException;
+ 

> CHANGE  6 : 10  @  4 : 17

~ 
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  13  @  20 : 22

> DELETE  14  @  23 : 26

> DELETE  17  @  29 : 30

> DELETE  29  @  42 : 44

> INSERT  30 : 31  @  45

+ 	private static long lastRefreshCommit = 0l;

> CHANGE  41 : 42  @  55 : 56

~ 			this.savedServerList = ServerList.getServerList();

> DELETE  43  @  57 : 66

> CHANGE  78 : 79  @  101 : 108

~ 		this.savedServerList.updateServerPing();

> DELETE  83  @  112 : 118

> CHANGE  85 : 86  @  120 : 121

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  122 : 127  @  157 : 158

~ 				long millis = System.currentTimeMillis();
~ 				if (millis - lastRefreshCommit > 700l) {
~ 					lastRefreshCommit = millis;
~ 					this.refreshServerList();
~ 				}

> CHANGE  132 : 133  @  163 : 164

~ 	public void refreshServerList() {

> CHANGE  147 : 152  @  178 : 180

~ 			long millis = System.currentTimeMillis();
~ 			if (millis - lastRefreshCommit > 700l) {
~ 				lastRefreshCommit = millis;
~ 				this.refreshServerList();
~ 			}

> CHANGE  167 : 172  @  195 : 197

~ 			long millis = System.currentTimeMillis();
~ 			if (millis - lastRefreshCommit > 700l) {
~ 				lastRefreshCommit = millis;
~ 				this.refreshServerList();
~ 			}

> CHANGE  182 : 187  @  207 : 209

~ 			long millis = System.currentTimeMillis();
~ 			if (millis - lastRefreshCommit > 700l) {
~ 				lastRefreshCommit = millis;
~ 				this.refreshServerList();
~ 			}

> DELETE  188  @  210 : 211

> CHANGE  190 : 191  @  213 : 214

~ 	protected void keyTyped(char parChar1, int parInt1) {

> DELETE  209  @  232 : 241

> CHANGE  220 : 221  @  252 : 253

~ 					} else if (i < this.serverListSelector.getSize() - 1) {

> DELETE  223  @  255 : 264

> DELETE  256  @  297 : 302

> DELETE  257  @  303 : 304

> CHANGE  270 : 271  @  317 : 319

~ 		if (guilistextended$iguilistentry != null) {

> DELETE  277  @  325 : 326

> DELETE  279  @  328 : 332

> CHANGE  283 : 284  @  336 : 337

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> EOF
