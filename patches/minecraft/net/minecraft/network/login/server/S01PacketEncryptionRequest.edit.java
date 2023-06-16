
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> DELETE  3  @  3 : 4

> CHANGE  3 : 4  @  3 : 4

~ 	// private PublicKey publicKey;

> CHANGE  5 : 10  @  5 : 10

~ //	public S01PacketEncryptionRequest(String serverId, PublicKey key, byte[] verifyToken) {
~ //		this.hashedServerId = serverId;
~ //		this.publicKey = key;
~ //		this.verifyToken = verifyToken;
~ //	}

> CHANGE  3 : 6  @  3 : 4

~ 		// this.publicKey =
~ 		// CryptManager.decodePublicKey(parPacketBuffer.readByteArray());
~ 		parPacketBuffer.readByteArray(); // skip

> CHANGE  4 : 7  @  4 : 7

~ //		parPacketBuffer.writeString(this.hashedServerId);
~ //		parPacketBuffer.writeByteArray(this.publicKey.getEncoded());
~ //		parPacketBuffer.writeByteArray(this.verifyToken);

> CHANGE  10 : 13  @  10 : 13

~ //	public PublicKey getPublicKey() {
~ //		return this.publicKey;
~ //	}

> EOF
