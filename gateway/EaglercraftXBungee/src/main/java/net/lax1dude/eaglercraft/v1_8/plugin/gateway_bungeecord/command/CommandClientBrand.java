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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.command;

import io.netty.buffer.Unpooled;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.OverflowPacketException;
import net.md_5.bungee.protocol.packet.PluginMessage;

public class CommandClientBrand extends Command {

	public CommandClientBrand() {
		super("client-brand", "eaglercraft.command.clientbrand", "clientbrand");
	}

	@Override
	public void execute(CommandSender var1, String[] var2) {
		if(var2.length == 1) {
			ProxiedPlayer player = BungeeCord.getInstance().getPlayer(var2[0]);
			if(player != null) {
				if(player.getPendingConnection() instanceof EaglerInitialHandler) {
					EaglerInitialHandler handler = (EaglerInitialHandler)player.getPendingConnection();
					var1.sendMessage(new TextComponent(ChatColor.BLUE + "Eagler Client Brand: " + ChatColor.WHITE + handler.getEaglerBrandString()));
					var1.sendMessage(new TextComponent(ChatColor.BLUE + "Eagler Client Version: " + ChatColor.WHITE + handler.getEaglerVersionString()));
					var1.sendMessage(new TextComponent(ChatColor.BLUE + "Eagler Client UUID: " + ChatColor.WHITE + handler.getClientBrandUUID()));
					var1.sendMessage(new TextComponent(ChatColor.BLUE + "Minecraft Client Brand: " + ChatColor.WHITE + decodeMCBrand(handler.getBrandMessage())));
				}else {
					var1.sendMessage(new TextComponent(ChatColor.RED + "That player is not using eaglercraft!"));
				}
			}else {
				var1.sendMessage(new TextComponent(ChatColor.RED + "That player was not found!"));
			}
			return;
		}
		if(var2.length == 2) {
			ProxiedPlayer player = BungeeCord.getInstance().getPlayer(var2[1]);
			if(player != null) {
				if(player.getPendingConnection() instanceof EaglerInitialHandler) {
					EaglerInitialHandler handler = (EaglerInitialHandler)player.getPendingConnection();
					if("uuid".equalsIgnoreCase(var2[0])) {
						var1.sendMessage(new TextComponent(ChatColor.BLUE + "Eagler Client UUID: " + ChatColor.WHITE + handler.getClientBrandUUID()));
						return;
					}else if("name".equalsIgnoreCase(var2[0])) {
						var1.sendMessage(new TextComponent(ChatColor.BLUE + "Eagler Client Brand: " + ChatColor.WHITE + handler.getEaglerBrandString()));
						var1.sendMessage(new TextComponent(ChatColor.BLUE + "Eagler Client Version: " + ChatColor.WHITE + handler.getEaglerVersionString()));
						return;
					}else if("mc".equalsIgnoreCase(var2[0])) {
						var1.sendMessage(new TextComponent(ChatColor.BLUE + "Minecraft Client Brand: " + ChatColor.WHITE + decodeMCBrand(handler.getBrandMessage())));
						return;
					}
				}else {
					var1.sendMessage(new TextComponent(ChatColor.RED + "That player is not using eaglercraft!"));
					return;
				}
			}else {
				var1.sendMessage(new TextComponent(ChatColor.RED + "That player was not found!"));
				return;
			}
		}
		var1.sendMessage(new TextComponent(ChatColor.RED + "Usage: /client-brand [uuid|name|mc] <username>"));
	}

	private static String decodeMCBrand(PluginMessage pkt) {
		if(pkt == null) {
			return "null";
		}
		try {
			return DefinedPacket.readString(Unpooled.wrappedBuffer(pkt.getData()), 64);
		}catch(OverflowPacketException | IndexOutOfBoundsException ex) {
			return "null";
		}
	}
}