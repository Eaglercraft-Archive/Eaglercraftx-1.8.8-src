
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import java.io.IOException;
+ 

> CHANGE  2 : 6  @  2 : 15

~ 
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  3  @  3 : 5

> DELETE  1  @  1 : 4

> DELETE  3  @  3 : 4

> DELETE  12  @  12 : 14

> INSERT  1 : 2  @  1

+ 	private static long lastRefreshCommit = 0l;

> CHANGE  10 : 11  @  10 : 11

~ 			this.savedServerList = ServerList.getServerList();

> DELETE  1  @  1 : 10

> CHANGE  35 : 36  @  35 : 42

~ 		this.savedServerList.updateServerPing();

> DELETE  4  @  4 : 10

> CHANGE  2 : 3  @  2 : 3

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  36 : 41  @  36 : 37

~ 				long millis = System.currentTimeMillis();
~ 				if (millis - lastRefreshCommit > 700l) {
~ 					lastRefreshCommit = millis;
~ 					this.refreshServerList();
~ 				}

> CHANGE  5 : 6  @  5 : 6

~ 	public void refreshServerList() {

> CHANGE  14 : 19  @  14 : 16

~ 			long millis = System.currentTimeMillis();
~ 			if (millis - lastRefreshCommit > 700l) {
~ 				lastRefreshCommit = millis;
~ 				this.refreshServerList();
~ 			}

> CHANGE  15 : 20  @  15 : 17

~ 			long millis = System.currentTimeMillis();
~ 			if (millis - lastRefreshCommit > 700l) {
~ 				lastRefreshCommit = millis;
~ 				this.refreshServerList();
~ 			}

> CHANGE  10 : 15  @  10 : 12

~ 			long millis = System.currentTimeMillis();
~ 			if (millis - lastRefreshCommit > 700l) {
~ 				lastRefreshCommit = millis;
~ 				this.refreshServerList();
~ 			}

> DELETE  1  @  1 : 2

> CHANGE  2 : 3  @  2 : 3

~ 	protected void keyTyped(char parChar1, int parInt1) {

> DELETE  18  @  18 : 27

> CHANGE  11 : 12  @  11 : 12

~ 					} else if (i < this.serverListSelector.getSize() - 1) {

> DELETE  2  @  2 : 11

> DELETE  33  @  33 : 38

> DELETE  1  @  1 : 2

> CHANGE  13 : 14  @  13 : 15

~ 		if (guilistextended$iguilistentry != null) {

> DELETE  6  @  6 : 7

> DELETE  2  @  2 : 6

> CHANGE  4 : 5  @  4 : 5

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> EOF
