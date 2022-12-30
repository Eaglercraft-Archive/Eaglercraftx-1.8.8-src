
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> DELETE  1  @  2 : 3

> CHANGE  3 : 13  @  4 : 5

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

> DELETE  12  @  3 : 6

> DELETE  1  @  4 : 5

> DELETE  4  @  5 : 8

> CHANGE  28 : 29  @  31 : 32

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  29 : 30  @  29 : 30

~ 				EaglercraftRandom random = new EaglercraftRandom(8124371L);

> CHANGE  5 : 6  @  5 : 6

~ 					for (s = s.replaceAll("PLAYERNAME", EaglerProfile.getName()); s

> CHANGE  23 : 24  @  23 : 24

~ 					s = s.replaceAll("PLAYERNAME", EaglerProfile.getName());

> EOF
