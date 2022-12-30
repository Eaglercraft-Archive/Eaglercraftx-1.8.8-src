
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import java.io.IOException;
+ 

> CHANGE  4 : 8  @  2 : 15

~ 
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  7  @  16 : 18

> DELETE  1  @  3 : 6

> DELETE  3  @  6 : 7

> DELETE  12  @  13 : 15

> INSERT  1 : 2  @  3

+ 	private static long lastRefreshCommit = 0l;

> CHANGE  11 : 12  @  10 : 11

~ 			this.savedServerList = ServerList.getServerList();

> DELETE  2  @  2 : 11

> CHANGE  35 : 36  @  44 : 51

~ 		this.savedServerList.updateServerPing();

> DELETE  5  @  11 : 17

> CHANGE  2 : 3  @  8 : 9

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  37 : 42  @  37 : 38

~ 				long millis = System.currentTimeMillis();
~ 				if (millis - lastRefreshCommit > 700l) {
~ 					lastRefreshCommit = millis;
~ 					this.refreshServerList();
~ 				}

> CHANGE  10 : 11  @  6 : 7

~ 	public void refreshServerList() {

> CHANGE  15 : 20  @  15 : 17

~ 			long millis = System.currentTimeMillis();
~ 			if (millis - lastRefreshCommit > 700l) {
~ 				lastRefreshCommit = millis;
~ 				this.refreshServerList();
~ 			}

> CHANGE  20 : 25  @  17 : 19

~ 			long millis = System.currentTimeMillis();
~ 			if (millis - lastRefreshCommit > 700l) {
~ 				lastRefreshCommit = millis;
~ 				this.refreshServerList();
~ 			}

> CHANGE  15 : 20  @  12 : 14

~ 			long millis = System.currentTimeMillis();
~ 			if (millis - lastRefreshCommit > 700l) {
~ 				lastRefreshCommit = millis;
~ 				this.refreshServerList();
~ 			}

> DELETE  6  @  3 : 4

> CHANGE  2 : 3  @  3 : 4

~ 	protected void keyTyped(char parChar1, int parInt1) {

> DELETE  19  @  19 : 28

> CHANGE  11 : 12  @  20 : 21

~ 					} else if (i < this.serverListSelector.getSize() - 1) {

> DELETE  3  @  3 : 12

> DELETE  33  @  42 : 47

> DELETE  1  @  6 : 7

> CHANGE  13 : 14  @  14 : 16

~ 		if (guilistextended$iguilistentry != null) {

> DELETE  7  @  8 : 9

> DELETE  2  @  3 : 7

> CHANGE  4 : 5  @  8 : 9

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> EOF
