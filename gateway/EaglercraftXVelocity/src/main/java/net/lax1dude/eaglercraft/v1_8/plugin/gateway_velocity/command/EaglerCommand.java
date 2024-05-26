package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.proxy.command.VelocityCommandManager;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;

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
public abstract class EaglerCommand implements SimpleCommand {

	public final String name;
	public final String permission;
	public final String[] alias;

	public EaglerCommand(String name, String perm, String...alias) {
		this.name = name;
		this.permission = perm;
		this.alias = alias;
	}

	@Override
    public void execute(final Invocation invocation) {
		this.execute(invocation.source(), invocation.arguments());
	}

	@Override
    public boolean hasPermission(Invocation invocation) {
		if(permission != null) {
			return invocation.source().hasPermission(permission);
		}else {
			return true;
		}
    }

	protected abstract void execute(CommandSource invocation, String[] args);

	public static void register(EaglerXVelocity plugin, EaglerCommand cmd) {
		VelocityCommandManager cmdManager = EaglerXVelocity.proxy().getCommandManager();
		cmdManager.register(cmdManager.metaBuilder(cmd.name).aliases(cmd.alias).plugin(plugin).build(), cmd);
	}
}
