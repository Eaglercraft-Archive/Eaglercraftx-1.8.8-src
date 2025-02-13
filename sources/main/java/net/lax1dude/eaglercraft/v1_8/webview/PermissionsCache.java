/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.webview;

import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

public class PermissionsCache {

	public static class Permission {

		public final int perm;
		public final boolean choice;

		private Permission(int perm, boolean choice) {
			this.perm = perm;
			this.choice = choice;
		}

	}

	private static final Map<EaglercraftUUID,Permission> javaScriptAllowed = new HashMap<>();

	public static Permission getJavaScriptAllowed(EaglercraftUUID uuid, int flags) {
		synchronized(javaScriptAllowed) {
			if(uuid == null) {
				return null;
			}
			Permission p = javaScriptAllowed.get(uuid);
			if(p == null) {
				return null;
			}
			return (p.perm | flags) != p.perm ? null : p;
		}
	}

	public static void setJavaScriptAllowed(EaglercraftUUID uuid, int flags, boolean allowed) {
		synchronized(javaScriptAllowed) {
			if(uuid != null) javaScriptAllowed.put(uuid, new Permission(flags, allowed));
		}
	}

	public static void clearJavaScriptAllowed(EaglercraftUUID uuid) {
		synchronized(javaScriptAllowed) {
			if(uuid != null) javaScriptAllowed.remove(uuid);
		}
	}

}