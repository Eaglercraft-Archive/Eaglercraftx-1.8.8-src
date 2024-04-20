
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.profile.SkinModel;

> DELETE  2  @  2 : 6

> DELETE  6  @  6 : 7

> INSERT  6 : 14  @  6

+ 	public long eaglerHighPolyAnimationTick = System.currentTimeMillis();
+ 	public float eaglerHighPolyAnimationFloat1 = 0.0f;
+ 	public float eaglerHighPolyAnimationFloat2 = 0.0f;
+ 	public float eaglerHighPolyAnimationFloat3 = 0.0f;
+ 	public float eaglerHighPolyAnimationFloat4 = 0.0f;
+ 	public float eaglerHighPolyAnimationFloat5 = 0.0f;
+ 	public float eaglerHighPolyAnimationFloat6 = 0.0f;
+ 

> DELETE  38  @  38 : 56

> INSERT  6 : 11  @  6

+ 	public SkinModel getEaglerSkinModel() {
+ 		NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
+ 		return networkplayerinfo == null ? SkinModel.STEVE : networkplayerinfo.getEaglerSkinModel();
+ 	}
+ 

> EOF
