
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> CHANGE  1 : 2  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> INSERT  6 : 8  @  6

+ 	private byte[] skin;
+ 	private byte[] cape;

> CHANGE  4 : 5  @  4 : 5

~ 	public C00PacketLoginStart(GameProfile profileIn, byte[] skin, byte[] cape) {

> INSERT  1 : 3  @  1

+ 		this.skin = skin;
+ 		this.cape = cape;

> CHANGE  3 : 6  @  3 : 4

~ 		this.profile = new GameProfile((EaglercraftUUID) null, parPacketBuffer.readStringFromBuffer(16));
~ 		this.skin = parPacketBuffer.readByteArray();
~ 		this.cape = parPacketBuffer.readableBytes() > 0 ? parPacketBuffer.readByteArray() : null;

> INSERT  4 : 6  @  4

+ 		parPacketBuffer.writeByteArray(this.skin);
+ 		parPacketBuffer.writeByteArray(this.cape);

> INSERT  9 : 17  @  9

+ 
+ 	public byte[] getSkin() {
+ 		return this.skin;
+ 	}
+ 
+ 	public byte[] getCape() {
+ 		return this.cape;
+ 	}

> EOF
