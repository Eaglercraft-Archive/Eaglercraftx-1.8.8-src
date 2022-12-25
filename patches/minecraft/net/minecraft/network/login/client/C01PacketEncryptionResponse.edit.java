
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 6

~ 

> DELETE  7  @  9 : 10

> CHANGE  15 : 19  @  18 : 22

~ //	public C01PacketEncryptionResponse(SecretKey secretKey, PublicKey publicKey, byte[] verifyToken) {
~ //		this.secretKeyEncrypted = CryptManager.encryptData(publicKey, secretKey.getEncoded());
~ //		this.verifyTokenEncrypted = CryptManager.encryptData(publicKey, verifyToken);
~ //	}

> CHANGE  34 : 37  @  37 : 40

~ //	public SecretKey getSecretKey(PrivateKey key) {
~ //		return CryptManager.decryptSharedKey(key, this.secretKeyEncrypted);
~ //	}

> CHANGE  38 : 41  @  41 : 44

~ //	public byte[] getVerifyToken(PrivateKey key) {
~ //		return key == null ? this.verifyTokenEncrypted : CryptManager.decryptData(key, this.verifyTokenEncrypted);
~ //	}

> EOF
