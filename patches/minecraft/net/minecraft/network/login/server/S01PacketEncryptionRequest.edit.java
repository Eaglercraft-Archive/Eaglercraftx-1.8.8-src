
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> DELETE  7  @  7 : 8

> CHANGE  10 : 11  @  11 : 12

~ 	// private PublicKey publicKey;

> CHANGE  16 : 21  @  17 : 22

~ //	public S01PacketEncryptionRequest(String serverId, PublicKey key, byte[] verifyToken) {
~ //		this.hashedServerId = serverId;
~ //		this.publicKey = key;
~ //		this.verifyToken = verifyToken;
~ //	}

> CHANGE  24 : 27  @  25 : 26

~ 		// this.publicKey =
~ 		// CryptManager.decodePublicKey(parPacketBuffer.readByteArray());
~ 		parPacketBuffer.readByteArray(); // skip

> CHANGE  31 : 34  @  30 : 33

~ //		parPacketBuffer.writeString(this.hashedServerId);
~ //		parPacketBuffer.writeByteArray(this.publicKey.getEncoded());
~ //		parPacketBuffer.writeByteArray(this.verifyToken);

> CHANGE  44 : 47  @  43 : 46

~ //	public PublicKey getPublicKey() {
~ //		return this.publicKey;
~ //	}

> EOF
