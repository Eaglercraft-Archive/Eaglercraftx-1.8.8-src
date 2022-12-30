
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 6

~ 

> DELETE  4  @  6 : 7

> CHANGE  8 : 12  @  9 : 13

~ //	public C01PacketEncryptionResponse(SecretKey secretKey, PublicKey publicKey, byte[] verifyToken) {
~ //		this.secretKeyEncrypted = CryptManager.encryptData(publicKey, secretKey.getEncoded());
~ //		this.verifyTokenEncrypted = CryptManager.encryptData(publicKey, verifyToken);
~ //	}

> CHANGE  19 : 22  @  19 : 22

~ //	public SecretKey getSecretKey(PrivateKey key) {
~ //		return CryptManager.decryptSharedKey(key, this.secretKeyEncrypted);
~ //	}

> CHANGE  4 : 7  @  4 : 7

~ //	public byte[] getVerifyToken(PrivateKey key) {
~ //		return key == null ? this.verifyTokenEncrypted : CryptManager.decryptData(key, this.verifyTokenEncrypted);
~ //	}

> EOF
