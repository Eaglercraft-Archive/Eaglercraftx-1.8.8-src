package net.lax1dude.eaglercraft.v1_8.sp.server;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.command.CommandBase;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.command.CommandException;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.command.ICommandSender;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.ChatComponentTranslation;

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
public class ClientCommandDummy extends CommandBase {

	private final String commandName;
	private final int permissionLevel;
	private final String commandUsage;

	public ClientCommandDummy(String commandName, int permissionLevel, String commandUsage) {
		this.commandName = commandName;
		this.permissionLevel = permissionLevel;
		this.commandUsage = commandUsage;
	}

	@Override
	public String getCommandName() {
		return commandName;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return permissionLevel;
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return commandUsage;
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
		var1.addChatMessage(new ChatComponentTranslation("command.clientStub"));
	}

}
