
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> DELETE  3  @  4 : 5

> CHANGE  6 : 16  @  8 : 9

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.base.Charsets;
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
~ import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;

> DELETE  18  @  11 : 14

> DELETE  19  @  15 : 16

> DELETE  23  @  20 : 23

> CHANGE  51 : 52  @  51 : 52

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  80 : 81  @  80 : 81

~ 				EaglercraftRandom random = new EaglercraftRandom(8124371L);

> CHANGE  85 : 86  @  85 : 86

~ 					for (s = s.replaceAll("PLAYERNAME", EaglerProfile.getName()); s

> CHANGE  108 : 109  @  108 : 109

~ 					s = s.replaceAll("PLAYERNAME", EaglerProfile.getName());

> EOF
