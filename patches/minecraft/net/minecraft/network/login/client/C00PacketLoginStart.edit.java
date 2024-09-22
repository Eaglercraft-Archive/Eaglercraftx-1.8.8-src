
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> CHANGE  1 : 2  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> INSERT  6 : 10  @  6

+ 	private byte[] skin;
+ 	private byte[] cape;
+ 	private byte[] protocols;
+ 	private EaglercraftUUID brandUUID;

> CHANGE  4 : 6  @  4 : 5

~ 	public C00PacketLoginStart(GameProfile profileIn, byte[] skin, byte[] cape, byte[] protocols,
~ 			EaglercraftUUID brandUUID) {

> INSERT  1 : 5  @  1

+ 		this.skin = skin;
+ 		this.cape = cape;
+ 		this.protocols = protocols;
+ 		this.brandUUID = brandUUID;

> CHANGE  3 : 8  @  3 : 4

~ 		this.profile = new GameProfile((EaglercraftUUID) null, parPacketBuffer.readStringFromBuffer(16));
~ 		this.skin = parPacketBuffer.readByteArray();
~ 		this.cape = parPacketBuffer.readableBytes() > 0 ? parPacketBuffer.readByteArray() : null;
~ 		this.protocols = parPacketBuffer.readableBytes() > 0 ? parPacketBuffer.readByteArray() : null;
~ 		this.brandUUID = parPacketBuffer.readableBytes() > 0 ? parPacketBuffer.readUuid() : null;

> INSERT  4 : 8  @  4

+ 		parPacketBuffer.writeByteArray(this.skin);
+ 		parPacketBuffer.writeByteArray(this.cape);
+ 		parPacketBuffer.writeByteArray(this.protocols);
+ 		parPacketBuffer.writeUuid(brandUUID);

> INSERT  9 : 25  @  9

+ 
+ 	public byte[] getSkin() {
+ 		return this.skin;
+ 	}
+ 
+ 	public byte[] getCape() {
+ 		return this.cape;
+ 	}
+ 
+ 	public byte[] getProtocols() {
+ 		return this.protocols;
+ 	}
+ 
+ 	public EaglercraftUUID getBrandUUID() {
+ 		return this.brandUUID;
+ 	}

> EOF
