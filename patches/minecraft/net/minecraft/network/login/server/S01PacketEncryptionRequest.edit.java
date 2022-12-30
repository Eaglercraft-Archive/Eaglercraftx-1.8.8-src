
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> DELETE  4  @  4 : 5

> CHANGE  3 : 4  @  4 : 5

~ 	// private PublicKey publicKey;

> CHANGE  6 : 11  @  6 : 11

~ //	public S01PacketEncryptionRequest(String serverId, PublicKey key, byte[] verifyToken) {
~ //		this.hashedServerId = serverId;
~ //		this.publicKey = key;
~ //		this.verifyToken = verifyToken;
~ //	}

> CHANGE  8 : 11  @  8 : 9

~ 		// this.publicKey =
~ 		// CryptManager.decodePublicKey(parPacketBuffer.readByteArray());
~ 		parPacketBuffer.readByteArray(); // skip

> CHANGE  7 : 10  @  5 : 8

~ //		parPacketBuffer.writeString(this.hashedServerId);
~ //		parPacketBuffer.writeByteArray(this.publicKey.getEncoded());
~ //		parPacketBuffer.writeByteArray(this.verifyToken);

> CHANGE  13 : 16  @  13 : 16

~ //	public PublicKey getPublicKey() {
~ //		return this.publicKey;
~ //	}

> EOF
