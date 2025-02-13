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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.shit;

import java.util.logging.Logger;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.md_5.bungee.api.ProxyServer;

public class CompatWarning {

	public static void displayCompatWarning() {
		String stfu = System.getProperty("eaglerxbungee.stfu");
		if("true".equalsIgnoreCase(stfu)) {
			return;
		}
		String[] compatWarnings = new String[] {
				":>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>",
				":>  ",
				":>      EAGLERCRAFTXBUNGEE WARNING:",
				":>  ",
				":>  This plugin wasn\'t tested to be \'working\'",
				":>  with ANY version of BungeeCord (and forks)",
				":>  apart from the versions listed below:",
				":>  ",
				":>  - BungeeCord: " + EaglerXBungee.NATIVE_BUNGEECORD_BUILD,
				":>  - " + EaglerXBungee.NATIVE_BUNGEECORD_BUILD_DL,
				":>  ",
				":>  - Waterfall: " + EaglerXBungee.NATIVE_WATERFALL_BUILD,
				":>  - " + EaglerXBungee.NATIVE_WATERFALL_BUILD_DL,
				":>  ",
				":>  This is not a Bukkit/Spigot plugin!",
				":>  ",
				":>  Use \"-Deaglerxbungee.stfu=true\" to hide",
				":>  ",
				":>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>"
		};
		
		try {
			Logger fuck = ProxyServer.getInstance().getLogger();
			for(int i = 0; i < compatWarnings.length; ++i) {
				fuck.warning(compatWarnings[i]);
			}
		}catch(Throwable t) {
			for(int i = 0; i < compatWarnings.length; ++i) {
				System.err.println(compatWarnings[i]);
			}
		}
	}

}