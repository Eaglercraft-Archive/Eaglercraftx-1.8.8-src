/*
 * Copyright (c) 2022-2025 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package net.lax1dude.eaglercraft.v1_8.socket.protocol.handshake;

import java.nio.charset.StandardCharsets;

import net.lax1dude.eaglercraft.v1_8.crypto.SHA256Digest;
import net.lax1dude.eaglercraft.v1_8.socket.HandshakePacketTypes;

public class AuthTypes {

	public static byte[] applyEaglerSHA256(String password, byte[] salt) {
		SHA256Digest digest = new SHA256Digest();
		
		int passLen = password.length();
		
		digest.update((byte)((passLen >>> 8) & 0xFF));
		digest.update((byte)(passLen & 0xFF));
		
		for (int i = 0; i < passLen; ++i) {
			char codePoint = password.charAt(i);
			digest.update((byte)((codePoint >>> 8) & 0xFF));
			digest.update((byte)(codePoint & 0xFF));
		}
		
		digest.update(HandshakePacketTypes.EAGLER_SHA256_SALT_SAVE, 0, 32);
		
		byte[] hashed = new byte[32];
		digest.doFinal(hashed, 0);
		
		digest.reset();
		
		digest.update(hashed, 0, 32);
		digest.update(salt, 0, 32);
		digest.update(HandshakePacketTypes.EAGLER_SHA256_SALT_BASE, 0, 32);
		
		digest.doFinal(hashed, 0);
		
		digest.reset();
		
		digest.update(hashed, 0, 32);
		digest.update(salt, 32, 32);
		digest.update(HandshakePacketTypes.EAGLER_SHA256_SALT_BASE, 0, 32);
		
		digest.doFinal(hashed, 0);

		return hashed;
	}

	public static byte[] applyAuthMeSHA256(String password, byte[] salt) {
		SHA256Digest digest = new SHA256Digest();

		byte[] passwd = password.getBytes(StandardCharsets.UTF_8);
		digest.update(passwd, 0, passwd.length);

		byte[] hashed = new byte[32];
		digest.doFinal(hashed, 0);

		byte[] toHexAndSalt = new byte[64];
		for (int i = 0; i < 32; ++i) {
			toHexAndSalt[i << 1] = HEX[(hashed[i] >> 4) & 0xF];
			toHexAndSalt[(i << 1) + 1] = HEX[hashed[i] & 0xF];
		}

		digest.reset();
		digest.update(toHexAndSalt, 0, 64);
		digest.update(salt, 0, salt.length);

		digest.doFinal(hashed, 0);

		for (int i = 0; i < 32; ++i) {
			toHexAndSalt[i << 1] = HEX[(hashed[i] >> 4) & 0xF];
			toHexAndSalt[(i << 1) + 1] = HEX[hashed[i] & 0xF];
		}

		return toHexAndSalt;
	}

	private static final byte[] HEX = new byte[] {
		(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
		(byte) '8', (byte) '9', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
	};

}
