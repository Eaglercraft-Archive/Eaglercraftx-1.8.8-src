/*
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.auth;

public class HashUtils {

	public static final byte[] EAGLER_SHA256_SALT_BASE = new byte[] { (byte) 117, (byte) 43, (byte) 1, (byte) 112,
			(byte) 75, (byte) 3, (byte) 188, (byte) 61, (byte) 121, (byte) 31, (byte) 34, (byte) 181, (byte) 234,
			(byte) 31, (byte) 247, (byte) 72, (byte) 12, (byte) 168, (byte) 138, (byte) 45, (byte) 143, (byte) 77,
			(byte) 118, (byte) 245, (byte) 187, (byte) 242, (byte) 188, (byte) 219, (byte) 160, (byte) 235, (byte) 235,
			(byte) 68 };

	public static final byte[] EAGLER_SHA256_SALT_SAVE = new byte[] { (byte) 49, (byte) 25, (byte) 39, (byte) 38,
			(byte) 253, (byte) 85, (byte) 70, (byte) 245, (byte) 71, (byte) 150, (byte) 253, (byte) 206, (byte) 4,
			(byte) 26, (byte) 198, (byte) 249, (byte) 145, (byte) 251, (byte) 232, (byte) 174, (byte) 186, (byte) 98,
			(byte) 27, (byte) 232, (byte) 55, (byte) 144, (byte) 83, (byte) 21, (byte) 36, (byte) 55, (byte) 170,
			(byte) 118 };

}