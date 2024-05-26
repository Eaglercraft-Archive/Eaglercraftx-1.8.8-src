package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.auth.DefaultAuthSystem;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.auth.DefaultAuthSystem.AuthException;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerAuthConfig;

/**
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
public class CommandEaglerPurge extends EaglerCommand {

	public CommandEaglerPurge(String name) {
		super(name + "-purge", "eaglercraft.command.purge");
	}

	@Override
	public void execute(CommandSource var1, String[] var2) {
		if(var1 instanceof ConsoleCommandSource) {
			if(var2.length != 1) {
				var1.sendMessage(Component.text("Use /" + name + " <maxAge>", NamedTextColor.RED));
				return;
			}
			int mx;
			try {
				mx = Integer.parseInt(var2[0]);
			}catch(NumberFormatException ex) {
				var1.sendMessage(Component.text("'" + var2[0] + "' is not an integer!", NamedTextColor.RED));
				return;
			}
			EaglerAuthConfig authConf = EaglerXVelocity.getEagler().getConfig().getAuthConfig();
			if(authConf.isEnableAuthentication() && authConf.isUseBuiltInAuthentication()) {
				DefaultAuthSystem srv = EaglerXVelocity.getEagler().getAuthService();
				if(srv != null) {
					int cnt;
					try {
						EaglerXVelocity.logger().warn("Console is attempting to purge all accounts with {} days of inactivity", mx);
						cnt = srv.pruneUsers(System.currentTimeMillis() - mx * 86400000l);
					}catch(AuthException ex) {
						EaglerXVelocity.logger().error("Failed to purge accounts", ex);
						var1.sendMessage(Component.text("Failed to purge, check log! Reason: " + ex.getMessage(), NamedTextColor.AQUA));
						return;
					}
					EaglerXVelocity.logger().warn("Console purged {} accounts from auth database", cnt);
					var1.sendMessage(Component.text("Purged " + cnt + " old accounts from the database", NamedTextColor.AQUA));
				}
			}
		}else {
			var1.sendMessage(Component.text("This command can only be run from the console!", NamedTextColor.RED));
		}
	}

}
