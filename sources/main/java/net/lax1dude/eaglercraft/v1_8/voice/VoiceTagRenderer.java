package net.lax1dude.eaglercraft.v1_8.voice;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.HashSet;
import java.util.Set;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

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
public class VoiceTagRenderer {

	private static final ResourceLocation voiceGuiIcons = new ResourceLocation("eagler:gui/eagler_gui.png");

	private static final Set<EaglercraftUUID> voiceTagsDrawnThisFrame = new HashSet();

	public static void renderVoiceNameTag(Minecraft mc, EntityOtherPlayerMP player, int offset) {
		EaglercraftUUID uuid = player.getUniqueID();
		boolean mute = VoiceClientController.getVoiceMuted().contains(uuid);
		if((mute || VoiceClientController.getVoiceSpeaking().contains(uuid)) && voiceTagsDrawnThisFrame.add(uuid)) {
			GlStateManager.disableLighting();
			GlStateManager.disableTexture2D();
			GlStateManager.enableAlpha();
			GlStateManager.depthMask(false);
			GlStateManager.disableDepth();
			GlStateManager.enableBlend();

			GlStateManager.pushMatrix();
			GlStateManager.translate(-8.0f, -18.0f + offset, 0.0f);

			GlStateManager.scale(16.0f, 16.0f, 16.0f);

			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
			float a = 0.25F;
			worldrenderer.pos(-0.02, -0.02, 0.0).color(0.0F, 0.0F, 0.0F, a).endVertex();
			worldrenderer.pos(-0.02, 1.02, 0.0).color(0.0F, 0.0F, 0.0F, a).endVertex();
			worldrenderer.pos(1.02, 1.02, 0.0).color(0.0F, 0.0F, 0.0F, a).endVertex();
			worldrenderer.pos(1.02, -0.02, 0.0).color(0.0F, 0.0F, 0.0F, a).endVertex();
			tessellator.draw();

			GlStateManager.enableTexture2D();
			GlStateManager.enableAlpha();
			GlStateManager.alphaFunc(GL_GREATER, 0.02f);

			mc.getTextureManager().bindTexture(voiceGuiIcons);

			int u = 0;
			int v = mute ? 192 : 160;

			float var7 = 0.00390625F;
			float var8 = 0.00390625F;

			if(mute) {
				GlStateManager.color(0.9F, 0.3F, 0.3F, 0.125F);
			}else {
				GlStateManager.color(1.0F, 1.0F, 1.0F, 0.125F);
			}

			worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
			worldrenderer.pos(0, 1.0, 0).tex((double) ((float) (u + 0.2f) * var7), (double) ((float) (v + 32 - 0.2f) * var8)).endVertex();
			worldrenderer.pos(1.0, 1.0, 0).tex((double) ((float) (u + 32 - 0.2f) * var7), (double) ((float) (v + 32 - 0.2f) * var8)).endVertex();
			worldrenderer.pos(1.0, 0, 0).tex((double) ((float) (u + 32 - 0.2f) * var7), (double) ((float) (v + 0.2f) * var8)).endVertex();
			worldrenderer.pos(0, 0, 0).tex((double) ((float) (u + 0.2f) * var7), (double) ((float) (v + 0.2f) * var8)).endVertex();
			tessellator.draw();

			GlStateManager.alphaFunc(GL_GREATER, 0.1f);
			GlStateManager.enableDepth();
			GlStateManager.depthMask(true);

			if(mute) {
				GlStateManager.color(0.9F, 0.3F, 0.3F, 1.0F);
			}else {
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			}

			worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
			worldrenderer.pos(0, 1.0, 0).tex((double) ((float) (u + 0.2f) * var7), (double) ((float) (v + 32 - 0.2f) * var8)).endVertex();
			worldrenderer.pos(1.0, 1.0, 0).tex((double) ((float) (u + 32 - 0.2f) * var7), (double) ((float) (v + 32 - 0.2f) * var8)).endVertex();
			worldrenderer.pos(1.0, 0, 0).tex((double) ((float) (u + 32 - 0.2f) * var7), (double) ((float) (v + 0.2f) * var8)).endVertex();
			worldrenderer.pos(0, 0, 0).tex((double) ((float) (u + 0.2f) * var7), (double) ((float) (v + 0.2f) * var8)).endVertex();
			tessellator.draw();

			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			GlStateManager.popMatrix();
		}
	}

	public static void clearTagsDrawnSet() {
		voiceTagsDrawnThisFrame.clear();
	}

}
