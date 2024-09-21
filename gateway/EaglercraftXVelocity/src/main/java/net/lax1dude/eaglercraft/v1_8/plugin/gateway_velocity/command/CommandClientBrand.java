package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.command;

import java.util.Optional;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPipeline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPlayerData;

/**
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
public class CommandClientBrand extends EaglerCommand {

	public CommandClientBrand() {
		super("client-brand", "eaglercraft.command.clientbrand", "clientbrand");
	}

	@Override
	public void execute(CommandSource var1, String[] var2) {
		if(var2.length == 1) {
			Optional<Player> player = EaglerXVelocity.proxy().getPlayer(var2[0]);
			if(player.isPresent()) {
				EaglerPlayerData playerData = EaglerPipeline.getEaglerHandle(player.get());
				if(playerData != null) {
					var1.sendMessage(Component.text("Eagler Client Brand: ", NamedTextColor.BLUE).append(Component.text(playerData.getEaglerBrandString(), NamedTextColor.WHITE)));
					var1.sendMessage(Component.text("Eagler Client Version: ", NamedTextColor.BLUE).append(Component.text(playerData.getEaglerVersionString(), NamedTextColor.WHITE)));
					var1.sendMessage(Component.text("Eagler Client UUID: ", NamedTextColor.BLUE).append(Component.text(playerData.getClientBrandUUID().toString(), NamedTextColor.WHITE)));
					var1.sendMessage(Component.text("Minecraft Client Brand: ", NamedTextColor.BLUE).append(Component.text(player.get().getClientBrand(), NamedTextColor.WHITE)));
				}else {
					var1.sendMessage(Component.text("That player is not using eaglercraft!", NamedTextColor.RED));
				}
			}else {
				var1.sendMessage(Component.text("That player was not found!", NamedTextColor.RED));
			}
			return;
		}
		if(var2.length == 2) {
			Optional<Player> player = EaglerXVelocity.proxy().getPlayer(var2[1]);
			if(player.isPresent()) {
				EaglerPlayerData playerData = EaglerPipeline.getEaglerHandle(player.get());
				if(playerData != null) {
					if("uuid".equalsIgnoreCase(var2[0])) {
						var1.sendMessage(Component.text("Eagler Client UUID: ", NamedTextColor.BLUE).append(Component.text(playerData.getClientBrandUUID().toString(), NamedTextColor.WHITE)));
						return;
					}else if("name".equalsIgnoreCase(var2[0])) {
						var1.sendMessage(Component.text("Eagler Client Brand: ", NamedTextColor.BLUE).append(Component.text(playerData.getEaglerBrandString(), NamedTextColor.WHITE)));
						var1.sendMessage(Component.text("Eagler Client Version: ", NamedTextColor.BLUE).append(Component.text(playerData.getEaglerVersionString(), NamedTextColor.WHITE)));
						return;
					}else if("mc".equalsIgnoreCase(var2[0])) {
						var1.sendMessage(Component.text("Minecraft Client Brand: ", NamedTextColor.BLUE).append(Component.text(player.get().getClientBrand(), NamedTextColor.WHITE)));
						return;
					}
				}else {
					var1.sendMessage(Component.text("That player is not using eaglercraft!", NamedTextColor.RED));
					return;
				}
			}else {
				var1.sendMessage(Component.text("That player was not found!", NamedTextColor.RED));
				return;
			}
		}
		var1.sendMessage(Component.text("Usage: /client-brand [uuid|name|mc] <username>", NamedTextColor.RED));
	}

}
