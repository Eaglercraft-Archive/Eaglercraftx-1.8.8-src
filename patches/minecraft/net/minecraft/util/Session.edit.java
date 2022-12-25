
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 6  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;

> CHANGE  8 : 9  @  9 : 13

~ 	private GameProfile profile;

> CHANGE  10 : 11  @  14 : 20

~ 	private static final EaglercraftUUID offlineUUID;

> CHANGE  12 : 14  @  21 : 23

~ 	public Session() {
~ 		reset();

> CHANGE  16 : 18  @  25 : 27

~ 	public GameProfile getProfile() {
~ 		return profile;

> CHANGE  20 : 22  @  29 : 31

~ 	public void update(String serverUsername, EaglercraftUUID uuid) {
~ 		profile = new GameProfile(uuid, serverUsername);

> CHANGE  24 : 26  @  33 : 35

~ 	public void reset() {
~ 		update(EaglerProfile.getName(), offlineUUID);

> CHANGE  28 : 32  @  37 : 44

~ 	static {
~ 		byte[] bytes = new byte[16];
~ 		(new EaglercraftRandom()).nextBytes(bytes);
~ 		offlineUUID = new EaglercraftUUID(bytes);

> DELETE  34  @  46 : 71

> EOF
