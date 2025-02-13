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

package net.lax1dude.eaglercraft.v1_8.update;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.util.ResourceLocation;

public class GuiUpdateVersionSlot extends GuiSlot {

	private static final ResourceLocation eaglerGuiTex = new ResourceLocation("eagler:gui/eagler_gui.png");

	final List<UpdateCertificate> certList = new ArrayList<>();

	final GuiUpdateVersionList screen;

	public GuiUpdateVersionSlot(GuiUpdateVersionList screen) {
		super(GuiUpdateVersionList.getMinecraft(screen), screen.width, screen.height, 32, screen.height - 64, 37);
		this.screen = screen;
		this.refresh();
	}

	public void refresh() {
		certList.clear();
		Collection<UpdateCertificate> certs = UpdateService.getAvailableUpdates();
		synchronized(certs) {
			certList.addAll(certs);
		}
		certList.sort((c1, c2) -> {
			if(c1.bundleVersionInteger > c2.bundleVersionInteger) {
				return -1;
			}else if(c1.bundleVersionInteger == c2.bundleVersionInteger) {
				if(c1.sigTimestamp > c2.sigTimestamp) {
					return -1;
				}else if(c1.sigTimestamp == c2.sigTimestamp) {
					return 0;
				}
			}
			return 1;
		});
	}

	@Override
	protected int getSize() {
		return certList.size();
	}

	@Override
	protected void elementClicked(int var1, boolean var2, int var3, int var4) {
		screen.selected = var1;
		screen.updateButtons();
	}

	@Override
	protected boolean isSelected(int var1) {
		return var1 == screen.selected;
	}

	@Override
	protected void drawBackground() {
		screen.drawBackground(0);
	}

	public static final SimpleDateFormat dateFmt = new SimpleDateFormat("M/dd/yyyy");
	private static final char[] hexChars = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	@Override
	protected void drawSlot(int id, int xx, int yy, int width, int height, int ii) {
		if(id < certList.size()) {
			this.mc.getTextureManager().bindTexture(eaglerGuiTex);
			GlStateManager.pushMatrix();
			GlStateManager.translate(xx, yy, 0.0f);
			GlStateManager.pushMatrix();
			int iconSize = 33;
			GlStateManager.scale(iconSize * 0.01f, iconSize * 0.01f, iconSize * 0.01f);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR); // rip
			screen.drawTexturedModalRect(0, 0, 156, 0, 100, 100);
			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			GlStateManager.popMatrix();
			GlStateManager.translate(iconSize + 1, 0.0f, 0.0f);
			GlStateManager.scale(0.75f, 0.75f, 0.75f);
			UpdateCertificate cert = certList.get(id);
			screen.drawString(mc.fontRendererObj,
					EnumChatFormatting.WHITE + cert.bundleDisplayName + EnumChatFormatting.GRAY + " - "
							+ (cert.bundleVersionInteger > EaglercraftVersion.updateBundlePackageVersionInt
									? EnumChatFormatting.GREEN
									: (cert.bundleVersionInteger < EaglercraftVersion.updateBundlePackageVersionInt
											? EnumChatFormatting.RED
											: EnumChatFormatting.YELLOW))
							+ cert.bundleDisplayVersion + EnumChatFormatting.DARK_GRAY + " "
							+ cert.bundleVersionInteger + " " + EnumChatFormatting.GRAY
							+ dateFmt.format(new Date(cert.sigTimestamp)) + EnumChatFormatting.WHITE + " " + (cert.bundleDataLength / 1024) + " kB",
					2, 2, 0xFFFFFF);
			List<String> strs = (List<String>)mc.fontRendererObj.listFormattedStringToWidth(cert.bundleVersionComment, (int)((getListWidth() - iconSize - 6) * 1.25f));
			if(strs.size() > 0) {
				screen.drawString(mc.fontRendererObj, strs.get(0), 2, 13, 0x888888);
			}
			if(strs.size() > 1) {
				screen.drawString(mc.fontRendererObj, strs.get(1), 2, 24, 0x888888);
			}
			if(strs.size() > 2 && screen.mx > xx + iconSize && screen.my > yy + 8 && screen.mx < xx + getListWidth() - 5 && screen.my < yy + 25) {
				screen.tooltip = cert.bundleVersionComment;
			}
			char[] hexStr1 = new char[] { hexChars[(cert.bundleDataHash[0] >>> 4) & 0xF],
					hexChars[cert.bundleDataHash[1] & 0xF], hexChars[(cert.bundleDataHash[1] >>> 4) & 0xF],
					hexChars[cert.bundleDataHash[1] & 0xF], hexChars[(cert.bundleDataHash[2] >>> 4) & 0xF],
					hexChars[cert.bundleDataHash[2] & 0xF] };
			char[] hexStr2 = new char[] { hexChars[(cert.bundleDataHash[29] >>> 4) & 0xF],
					hexChars[cert.bundleDataHash[29] & 0xF], hexChars[(cert.bundleDataHash[30] >>> 4) & 0xF],
					hexChars[cert.bundleDataHash[30] & 0xF], hexChars[(cert.bundleDataHash[31] >>> 4) & 0xF],
					hexChars[cert.bundleDataHash[31] & 0xF] };
			screen.drawString(mc.fontRendererObj,
					"Author: " + EnumChatFormatting.GRAY + cert.bundleAuthorName + EnumChatFormatting.WHITE + "  Hash: "
							+ EnumChatFormatting.GRAY + "0x" + (new String(hexStr1)) + "......" + (new String(hexStr2)),
					2, 35, 0xFFFFFF);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public int getListWidth() {
		return 250;
	}
}