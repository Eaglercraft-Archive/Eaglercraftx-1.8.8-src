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

package net.lax1dude.eaglercraft.v1_8.sp;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.FileChooserResult;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketInstallSkinSPEAG;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;

public class SkullCommand {

	private final Minecraft mc;
	private boolean waitingForSelection = false;

	public SkullCommand(Minecraft mc) {
		this.mc = mc;
	}

	public void openFileChooser() {
		EagRuntime.displayFileChooser("image/png", "png");
		waitingForSelection = true;
	}

	public void tick() {
		if(waitingForSelection && EagRuntime.fileChooserHasResult()) {
			waitingForSelection = false;
			FileChooserResult fr = EagRuntime.getFileChooserResult();
			if(fr == null || mc.thePlayer == null || mc.thePlayer.sendQueue == null) {
				return;
			}
			ImageData loaded = ImageData.loadImageFile(fr.fileData, ImageData.getMimeFromType(fr.fileName));
			if(loaded == null) {
				mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("command.skull.error.invalid.format"));
				return;
			}
			if(loaded.width != 64 || loaded.height > 64) {
				mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("command.skull.error.invalid.skin", loaded.width, loaded.height));
				return;
			}
			byte[] rawSkin = new byte[loaded.pixels.length << 2];
			for(int i = 0, j, k; i < 4096; ++i) {
				j = i << 2;
				k = loaded.pixels[i];
				rawSkin[j] = (byte)(k >>> 24);
				rawSkin[j + 1] = (byte)(k >>> 16);
				rawSkin[j + 2] = (byte)(k >>> 8);
				rawSkin[j + 3] = (byte)(k & 0xFF);
			}
			mc.thePlayer.sendQueue.sendEaglerMessage(new CPacketInstallSkinSPEAG(rawSkin));
		}
	}

}