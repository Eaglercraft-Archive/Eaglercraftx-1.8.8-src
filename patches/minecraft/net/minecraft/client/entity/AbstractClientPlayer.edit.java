
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.profanity_filter.ProfanityFilter;
~ import net.lax1dude.eaglercraft.v1_8.profile.SkinModel;

> DELETE  2  @  2 : 6

> INSERT  4 : 5  @  4

+ import net.minecraft.event.ClickEvent;

> INSERT  1 : 4  @  1

+ import net.minecraft.scoreboard.ScorePlayerTeam;
+ import net.minecraft.util.ChatComponentText;
+ import net.minecraft.util.IChatComponent;

> DELETE  1  @  1 : 2

> INSERT  6 : 16  @  6

+ 	public long eaglerHighPolyAnimationTick = EagRuntime.steadyTimeMillis();
+ 	public float eaglerHighPolyAnimationFloat1 = 0.0f;
+ 	public float eaglerHighPolyAnimationFloat2 = 0.0f;
+ 	public float eaglerHighPolyAnimationFloat3 = 0.0f;
+ 	public float eaglerHighPolyAnimationFloat4 = 0.0f;
+ 	public float eaglerHighPolyAnimationFloat5 = 0.0f;
+ 	public float eaglerHighPolyAnimationFloat6 = 0.0f;
+ 	public EaglercraftUUID clientBrandUUIDCache = null;
+ 	private String nameProfanityFilter = null;
+ 

> DELETE  38  @  38 : 56

> INSERT  6 : 11  @  6

+ 	public SkinModel getEaglerSkinModel() {
+ 		NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
+ 		return networkplayerinfo == null ? SkinModel.STEVE : networkplayerinfo.getEaglerSkinModel();
+ 	}
+ 

> INSERT  27 : 49  @  27

+ 
+ 	public String getNameProfanityFilter() {
+ 		if (Minecraft.getMinecraft().isEnableProfanityFilter()) {
+ 			if (nameProfanityFilter == null) {
+ 				nameProfanityFilter = ProfanityFilter.getInstance()
+ 						.profanityFilterString(this.getGameProfile().getName());
+ 			}
+ 			return nameProfanityFilter;
+ 		} else {
+ 			return this.getGameProfile().getName();
+ 		}
+ 	}
+ 
+ 	public IChatComponent getDisplayNameProfanityFilter() {
+ 		ChatComponentText chatcomponenttext = new ChatComponentText(
+ 				ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getNameProfanityFilter()));
+ 		chatcomponenttext.getChatStyle()
+ 				.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + this.getName() + " "));
+ 		chatcomponenttext.getChatStyle().setChatHoverEvent(this.getHoverEvent());
+ 		chatcomponenttext.getChatStyle().setInsertion(this.getName());
+ 		return chatcomponenttext;
+ 	}

> EOF
