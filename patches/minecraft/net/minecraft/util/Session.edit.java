
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 6  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;

> CHANGE  6 : 7  @  7 : 11

~ 	private GameProfile profile;

> CHANGE  2 : 3  @  5 : 11

~ 	private static final EaglercraftUUID offlineUUID;

> CHANGE  2 : 4  @  7 : 9

~ 	public Session() {
~ 		reset();

> CHANGE  4 : 6  @  4 : 6

~ 	public GameProfile getProfile() {
~ 		return profile;

> CHANGE  4 : 6  @  4 : 6

~ 	public void update(String serverUsername, EaglercraftUUID uuid) {
~ 		profile = new GameProfile(uuid, serverUsername);

> CHANGE  4 : 6  @  4 : 6

~ 	public void reset() {
~ 		update(EaglerProfile.getName(), offlineUUID);

> CHANGE  4 : 8  @  4 : 11

~ 	static {
~ 		byte[] bytes = new byte[16];
~ 		(new EaglercraftRandom()).nextBytes(bytes);
~ 		offlineUUID = new EaglercraftUUID(bytes);

> DELETE  6  @  9 : 34

> EOF
