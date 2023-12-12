
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 4  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> INSERT  6 : 7  @  6

+ 	private byte[] skin;

> CHANGE  4 : 5  @  4 : 5

~ 	public C00PacketLoginStart(GameProfile profileIn, byte[] skin) {

> INSERT  1 : 2  @  1

+ 		this.skin = skin;

> CHANGE  3 : 5  @  3 : 4

~ 		this.profile = new GameProfile((EaglercraftUUID) null, parPacketBuffer.readStringFromBuffer(16));
~ 		this.skin = parPacketBuffer.readByteArray();

> INSERT  4 : 5  @  4

+ 		parPacketBuffer.writeByteArray(this.skin);

> EOF
