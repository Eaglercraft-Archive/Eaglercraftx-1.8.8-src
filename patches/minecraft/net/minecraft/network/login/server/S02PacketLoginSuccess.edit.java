
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 4  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> INSERT  6 : 7  @  6

+ 	private int selectedProtocol = 3;

> CHANGE  4 : 5  @  4 : 5

~ 	public S02PacketLoginSuccess(GameProfile profileIn, int selectedProtocol) {

> INSERT  1 : 2  @  1

+ 		this.selectedProtocol = selectedProtocol;

> CHANGE  5 : 7  @  5 : 6

~ 		selectedProtocol = parPacketBuffer.readableBytes() > 0 ? parPacketBuffer.readShort() : 3;
~ 		EaglercraftUUID uuid = EaglercraftUUID.fromString(s);

> CHANGE  4 : 5  @  4 : 5

~ 		EaglercraftUUID uuid = this.profile.getId();

> INSERT  2 : 5  @  2

+ 		if (selectedProtocol != 3) {
+ 			parPacketBuffer.writeShort(selectedProtocol);
+ 		}

> INSERT  9 : 13  @  9

+ 
+ 	public int getSelectedProtocol() {
+ 		return selectedProtocol;
+ 	}

> EOF
