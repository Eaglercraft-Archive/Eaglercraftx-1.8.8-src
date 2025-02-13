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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerVelocityConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerRateLimiter;

public class CommandRatelimit extends EaglerCommand {

	public CommandRatelimit() {
		super("ratelimit", "eaglercraft.command.ratelimit");
	}

	@Override
	public void execute(CommandSource sender, String[] args) {
		if((args.length != 1 && args.length != 2) || !args[0].equalsIgnoreCase("reset")) {
			sender.sendMessage(Component.text("Usage: /ratelimit reset [ip|login|motd|query]", NamedTextColor.RED)); //TODO: allow reset ratelimit on specific listeners
		}else {
			int resetNum = 0;
			if(args.length == 2) {
				if(args[1].equalsIgnoreCase("ip")) {
					resetNum = 1;
				}else if(args[1].equalsIgnoreCase("login")) {
					resetNum = 2;
				}else if(args[1].equalsIgnoreCase("motd")) {
					resetNum = 3;
				}else if(args[1].equalsIgnoreCase("query")) {
					resetNum = 4;
				}else {
					sender.sendMessage(Component.text("Unknown ratelimit '" + args[1] + "'!", NamedTextColor.RED));
					return;
				}
			}
			EaglerVelocityConfig conf = EaglerXVelocity.getEagler().getConfig();
			for(EaglerListenerConfig listener : conf.getServerListeners()) {
				if(resetNum == 0 || resetNum == 1) {
					EaglerRateLimiter limiter = listener.getRatelimitIp();
					if(limiter != null) {
						limiter.reset();
					}
				}
				if(resetNum == 0 || resetNum == 2) {
					EaglerRateLimiter limiter = listener.getRatelimitLogin();
					if(limiter != null) {
						limiter.reset();
					}
				}
				if(resetNum == 0 || resetNum == 3) {
					EaglerRateLimiter limiter = listener.getRatelimitMOTD();
					if(limiter != null) {
						limiter.reset();
					}
				}
				if(resetNum == 0 || resetNum == 4) {
					EaglerRateLimiter limiter = listener.getRatelimitQuery();
					if(limiter != null) {
						limiter.reset();
					}
				}
			}
			sender.sendMessage(Component.text("Ratelimits reset."));
		}
	}

}