
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 6  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;

> CHANGE  2 : 3  @  2 : 6

~ 	private GameProfile profile;

> CHANGE  1 : 2  @  1 : 7

~ 	private static final EaglercraftUUID offlineUUID;

> CHANGE  1 : 3  @  1 : 3

~ 	public Session() {
~ 		reset();

> CHANGE  2 : 4  @  2 : 4

~ 	public GameProfile getProfile() {
~ 		return profile;

> CHANGE  2 : 4  @  2 : 4

~ 	public void update(String serverUsername, EaglercraftUUID uuid) {
~ 		profile = new GameProfile(uuid, serverUsername);

> CHANGE  2 : 4  @  2 : 4

~ 	public void reset() {
~ 		update(EaglerProfile.getName(), offlineUUID);

> CHANGE  2 : 6  @  2 : 9

~ 	static {
~ 		byte[] bytes = new byte[16];
~ 		(new EaglercraftRandom()).nextBytes(bytes);
~ 		offlineUUID = new EaglercraftUUID(bytes);

> DELETE  2  @  2 : 27

> EOF
