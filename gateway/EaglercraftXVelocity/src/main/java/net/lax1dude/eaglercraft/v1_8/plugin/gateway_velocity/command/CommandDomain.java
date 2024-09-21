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
public class CommandDomain extends EaglerCommand {

	public CommandDomain() {
		super("domain", "eaglercraft.command.domain");
	}

	@Override
	public void execute(CommandSource var1, String[] var2) {
		if(var2.length != 1) {
			var1.sendMessage(Component.text("How to use: ", NamedTextColor.RED).append(Component.text("/domain <player>", NamedTextColor.WHITE)));
		}else {
			Optional<Player> playerOpt = EaglerXVelocity.proxy().getPlayer(var2[0]);
			if(playerOpt.isEmpty()) {
				var1.sendMessage(Component.text("That user is not online", NamedTextColor.RED));
				return;
			}
			EaglerPlayerData eagPlayer = EaglerPipeline.getEaglerHandle(playerOpt.get());
			if(eagPlayer == null) {
				var1.sendMessage(Component.text("That user is not using Eaglercraft", NamedTextColor.RED));
				return;
			}
			String origin = eagPlayer.getOrigin();
			if(origin != null) {
				var1.sendMessage(Component.text("Domain of " + var2[0] + " is '" + origin + "'", NamedTextColor.BLUE));
			}else {
				var1.sendMessage(Component.text("That user's browser did not send an origin header", NamedTextColor.RED));
			}
		}
	}

}
