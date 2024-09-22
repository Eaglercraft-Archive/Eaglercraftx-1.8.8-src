package net.lax1dude.eaglercraft.v1_8.touch_gui;

import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
import net.lax1dude.eaglercraft.v1_8.Touch;
import net.lax1dude.eaglercraft.v1_8.opengl.GameOverlayFramebuffer;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.Set;

import com.google.common.collect.Sets;

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
public class TouchOverlayRenderer {

	public static final ResourceLocation spriteSheet = new ResourceLocation("eagler:gui/touch_gui.png");

	static final int[] _fuck = new int[2];

	private GameOverlayFramebuffer overlayFramebuffer;
	private final Minecraft mc;
	private boolean invalid = false;
	private boolean invalidDeep = false;
	private int currentWidth = -1;
	private int currentHeight = -1;

	public TouchOverlayRenderer(Minecraft mc) {
		this.mc = mc;
		this.overlayFramebuffer = new GameOverlayFramebuffer(false);
		EnumTouchControl.currentLayout = null;
		EnumTouchControl.setLayoutState(this, EnumTouchLayoutState.IN_GUI);
	}

	public void invalidate() {
		invalid = true;
	}

	public void invalidateDeep() {
		invalid = true;
		invalidDeep = true;
	}

	public void render(int w, int h, ScaledResolution scaledResolution) {
		if(PointerInputAbstraction.isTouchMode()) {
			render0(w, h, scaledResolution);
			if(EnumTouchControl.KEYBOARD.visible) {
				int[] pos = EnumTouchControl.KEYBOARD.getLocation(scaledResolution, _fuck);
				int scale = scaledResolution.getScaleFactor();
				int size = EnumTouchControl.KEYBOARD.size * scale;
				Touch.touchSetOpenKeyboardZone(pos[0] * scale,
						(scaledResolution.getScaledHeight() - pos[1] - 1) * scale - size, size, size);
			}else {
				Touch.touchSetOpenKeyboardZone(0, 0, 0, 0);
			}
		}else {
			Touch.touchSetOpenKeyboardZone(0, 0, 0, 0);
		}
	}

	private void render0(int w, int h, ScaledResolution scaledResolution) {
		EnumTouchControl.setLayoutState(this, hashLayoutState());
		int sw = scaledResolution.getScaledWidth();
		int sh = scaledResolution.getScaledHeight();
		if(currentWidth != sw || currentHeight != sh) {
			invalidateDeep();
		}
		GlStateManager.disableDepth();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		GlStateManager.enableAlpha();
		GlStateManager.depthMask(false);
		if(invalid) {
			GlStateManager.pushMatrix();
			invalidDeep |= overlayFramebuffer.beginRender(sw, sh);
			GlStateManager.viewport(0, 0, sw, sh);
			if(invalidDeep) {
				currentWidth = sw;
				currentHeight = sh;
				GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
				GlStateManager.clear(GL_COLOR_BUFFER_BIT);
			}
			Set<EnumTouchControl> controls = Sets.newHashSet(EnumTouchControl._VALUES);
			for (TouchControlInput input : TouchControls.touchControls.values()) {
				controls.remove(input.control);
			}
			for (EnumTouchControl control : controls) {
				if(invalidDeep || control.invalid) {
					if(control.visible) {
						GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
						control.getRender().call(control, 0, 0, false, mc, scaledResolution);
					}
					control.invalid = false;
				}
			}
			for (TouchControlInput input : TouchControls.touchControls.values()) {
				EnumTouchControl control = input.control;
				if(invalidDeep || control.invalid) {
					if(control.visible) {
						GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
						control.getRender().call(control, input.x, input.y, true, mc, scaledResolution);
					}
					control.invalid = false;
				}
			}
			overlayFramebuffer.endRender();
			invalid = false;
			invalidDeep = false;
			GlStateManager.popMatrix();
			GlStateManager.viewport(0, 0, w, h);
		}
		GlStateManager.bindTexture(overlayFramebuffer.getTexture());
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableAlpha();
		GlStateManager.color(1.0f, 1.0f, 1.0f, MathHelper.clamp_float(mc.gameSettings.touchControlOpacity, 0.0f, 1.0f));
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(0.0D, (double) sh, 500.0D).tex(0.0D, 0.0D).endVertex();
		worldrenderer.pos((double) sw, (double) sh, 500.0D).tex(1.0D, 0.0D).endVertex();
		worldrenderer.pos((double) sw, 0.0D, 500.0D).tex(1.0D, 1.0D).endVertex();
		worldrenderer.pos(0.0D, 0.0D, 500.0D).tex(0.0D, 1.0D).endVertex();
		tessellator.draw();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
	}

	private EnumTouchLayoutState hashLayoutState() {
		if(mc.currentScreen != null) {
			return mc.currentScreen.showCopyPasteButtons() ? EnumTouchLayoutState.IN_GUI_TYPING
					: (mc.currentScreen.canCloseGui() ? EnumTouchLayoutState.IN_GUI
							: EnumTouchLayoutState.IN_GUI_NO_BACK);
		}
		EntityPlayerSP player = mc.thePlayer;
		if(player != null) {
			if(player.capabilities.isFlying) {
				 return showDiagButtons() ? EnumTouchLayoutState.IN_GAME_WALK_FLYING : EnumTouchLayoutState.IN_GAME_FLYING;
			}else {
				if(player.capabilities.allowFlying) {
					return showDiagButtons() ? EnumTouchLayoutState.IN_GAME_WALK_CAN_FLY : EnumTouchLayoutState.IN_GAME_CAN_FLY;
				}else {
					return showDiagButtons() ? EnumTouchLayoutState.IN_GAME_WALK : EnumTouchLayoutState.IN_GAME;
				}
			}
		}else {
			return showDiagButtons() ? EnumTouchLayoutState.IN_GAME_WALK : EnumTouchLayoutState.IN_GAME;
		}
	}

	private boolean showDiagButtons() {
		return TouchControls.isPressed(EnumTouchControl.DPAD_UP)
				|| TouchControls.isPressed(EnumTouchControl.DPAD_UP_LEFT)
				|| TouchControls.isPressed(EnumTouchControl.DPAD_UP_RIGHT);
	}

	protected static void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV, int scaleFac) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos((double) (xCoord + 0.0F), (double) (yCoord + (float) maxV * scaleFac), 0.0)
				.tex((double) ((float) (minU + 0) * f), (double) ((float) (minV + maxV) * f1)).endVertex();
		worldrenderer.pos((double) (xCoord + (float) maxU * scaleFac), (double) (yCoord + (float) maxV * scaleFac), 0.0)
				.tex((double) ((float) (minU + maxU) * f), (double) ((float) (minV + maxV) * f1)).endVertex();
		worldrenderer.pos((double) (xCoord + (float) maxU * scaleFac), (double) (yCoord + 0.0F), 0.0)
				.tex((double) ((float) (minU + maxU) * f), (double) ((float) (minV + 0) * f1)).endVertex();
		worldrenderer.pos((double) (xCoord + 0.0F), (double) (yCoord + 0.0F), 0.0)
				.tex((double) ((float) (minU + 0) * f), (double) ((float) (minV + 0) * f1)).endVertex();
		tessellator.draw();
	}

}
