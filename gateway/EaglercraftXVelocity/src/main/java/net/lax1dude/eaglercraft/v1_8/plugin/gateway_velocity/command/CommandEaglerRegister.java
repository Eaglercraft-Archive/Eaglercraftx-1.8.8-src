/*
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.auth.DefaultAuthSystem;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerAuthConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPipeline;

public class CommandEaglerRegister extends EaglerCommand {

	public CommandEaglerRegister(String name) {
		super(name, null);
	}

	@Override
	public void execute(CommandSource sender, String[] args) {
		if(sender instanceof ConnectedPlayer) {
			ConnectedPlayer player = (ConnectedPlayer)sender;
			if(args.length != 1) {
				player.sendMessage(Component.text("Use: /" + name + " <password>", NamedTextColor.RED));
				return;
			}
			EaglerAuthConfig authConf = EaglerXVelocity.getEagler().getConfig().getAuthConfig();
			if(authConf.isEnableAuthentication() && authConf.isUseBuiltInAuthentication()) {
				DefaultAuthSystem srv = EaglerXVelocity.getEagler().getAuthService();
				if(srv != null) {
					if(EaglerPipeline.getEaglerHandle(player) == null) {
						try {
							srv.processSetPassword(player, args[0]);
							sender.sendMessage(Component.text(authConf.getCommandSuccessText()));
						}catch(DefaultAuthSystem.TooManyRegisteredOnIPException ex) {
							String tooManyReg = authConf.getTooManyRegistrationsMessage();
							sender.sendMessage(Component.text(tooManyReg));
						}catch(DefaultAuthSystem.AuthException ex) {
							EaglerXVelocity.logger().error("Internal exception while processing password change from \"{}\"", player.getUsername(), ex);
							sender.sendMessage(Component.text("Internal error, check console logs"));
						}
					}else {
						player.sendMessage(Component.text(authConf.getNeedVanillaToRegisterMessage()));
					}
				}
			}
		}else {
			sender.sendMessage(Component.text("You must be a player to use this command!", NamedTextColor.RED));
		}
	}

}