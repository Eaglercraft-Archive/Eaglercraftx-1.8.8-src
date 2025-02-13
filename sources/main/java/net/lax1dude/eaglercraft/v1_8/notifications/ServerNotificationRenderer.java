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

package net.lax1dude.eaglercraft.v1_8.notifications;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IFramebufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.VertexFormat;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ExtGLEnums.*;

public class ServerNotificationRenderer {

	protected static final Logger logger = LogManager.getLogger("ServerNotificationRenderer");

	protected Minecraft mc;
	protected int width;
	protected int height;
	protected int scaleFactor;

	protected IFramebufferGL rendererFramebuffer;

	protected static final int BADGE_WIDTH = 160;
	protected static final int BADGE_HEIGHT = 64;

	private static final ResourceLocation eaglerGui = new ResourceLocation("eagler:gui/eagler_gui.png");

	public ServerNotificationRenderer() {
		
	}

	public void init() {
		destroy();
		rendererFramebuffer = _wglCreateFramebuffer();
	}

	public void setResolution(Minecraft mc, int w, int h, int scaleFactor) {
		this.mc = mc;
		this.width = w;
		this.height = h;
		this.scaleFactor = scaleFactor;
	}

	public boolean handleClicked(GuiScreen currentScreen, int posX, int posY) {
		if(mc.thePlayer == null) return false;
		ServerNotificationManager mgr = mc.thePlayer.sendQueue.getNotifManager();
		List<NotificationBadge> lst = mgr.getNotifBadgesToDisplay();
		if(!lst.isEmpty()) {
			int baseOffset = mc.guiAchievement.getHeight();
			boolean showX = (currentScreen instanceof GuiChat);
			if(showX) {
				baseOffset += 25; // exit button in chat screen;
			}
			long millis = EagRuntime.steadyTimeMillis();
			for(int i = 0, l = lst.size(); i < l; ++i) {
				NotificationBadge badge = lst.get(i);
				CachedNotifBadgeTexture tex = badge.currentCacheGLTexture;
				if(tex != null) {
					int baseX = width - tex.width;
					float texHeight = tex.height;
					float timeRemainingSec;
					long age = millis - badge.clientTimestamp;
					if(badge.hideAtMillis != -1l) {
						timeRemainingSec = (float)((double)(500l - (millis - badge.hideAtMillis)) * 0.001);
					}else {
						timeRemainingSec = (float)((double)((long)badge.hideAfterSec * 1000l - age) * 0.001);
					}
					timeRemainingSec = Math.min((float)(age * 0.001) + 0.001f, timeRemainingSec);
					float f = MathHelper.clamp_float(timeRemainingSec * 3.0F, 0.0F, 1.0F);
					f *= f;
					texHeight *= f;
					if(badge.hideAtMillis == -1l) {
						if(posX >= baseX && posX < width && posY >= baseOffset && posY < baseOffset + texHeight) {
							if(showX) {
								int xposX = baseX + tex.width - 21;
								int xposY = baseOffset + 5;
								if(posX >= xposX && posY >= xposY && posX < xposX + 16 && posY < xposY + 16) {
									badge.hideNotif();
									mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
									return true;
								}
							}
							if(tex.rootClickEvent != null) {
								if(currentScreen.handleComponentClick(tex.rootClickEvent)) {
									mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
									return true;
								}
							}
							List<ClickEventZone> cursorEvents = tex.cursorEvents;
							if(tex.hasClickEvents && cursorEvents != null) {
								for(int j = 0, m = cursorEvents.size(); j < m; ++j) {
									ClickEventZone evt = cursorEvents.get(j);
									if(evt.hasClickEvent) {
										int offsetPosX = baseX + evt.posX;
										int offsetPosY = baseOffset + evt.posY;
										if(posX >= offsetPosX && posY >= offsetPosY && posX < offsetPosX + evt.width && posY < offsetPosY + evt.height) {
											if(currentScreen.handleComponentClick(evt.chatComponent)) {
												mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
												return true;
											}
										}
									}
								}
							}
						}
					}
					baseOffset += texHeight;
				}
			}
		}
		return false;
	}

	public void renderOverlay(int mouseX, int mouseY) {
		if(mc.thePlayer == null) return;
		ServerNotificationManager mgr = mc.thePlayer.sendQueue.getNotifManager();
		List<NotificationBadge> lst = mgr.getNotifBadgesToDisplay();
		if(!lst.isEmpty()) {
			GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
			boolean showXButtons = false;
			int baseOffset = mc.guiAchievement.getHeight();
			if(mc.currentScreen != null) {
				if(mc.currentScreen instanceof GuiChat) {
					baseOffset += 25; // exit button in chat screen;
					showXButtons = true;
				}else if(mc.currentScreen instanceof GuiScreenNotifications) {
					return;
				}
			}
			long millis = EagRuntime.steadyTimeMillis();
			boolean isBlend = false;
			for(int i = 0, l = lst.size(); i < l; ++i) {
				NotificationBadge badge = lst.get(i);
				boolean isHiding = false;
				if(badge.hideAtMillis != -1l) {
					isHiding = true;
					if(millis - badge.hideAtMillis > 500l) {
						continue;
					}
				}
				CachedNotifBadgeTexture tex = badge.getGLTexture(this, scaleFactor, showXButtons);
				if(tex != null) {
					GlStateManager.bindTexture(tex.glTexture);
					float alphaTop = 1.0f;
					float alphaBottom = 1.0f;
					float timeRemainingSec;
					long age = millis - badge.clientTimestamp;
					if(isHiding) {
						timeRemainingSec = (float)((double)(500l - (millis - badge.hideAtMillis)) * 0.001);
					}else {
						timeRemainingSec = (float)((double)((long)badge.hideAfterSec * 1000l - age) * 0.001);
					}
					timeRemainingSec = Math.min((float)(age * 0.001) + 0.001f, timeRemainingSec);
					alphaTop *= MathHelper.clamp_float(timeRemainingSec * 3.0F, 0.0F, 1.0F);
					alphaTop *= alphaTop;
					alphaBottom *= MathHelper.clamp_float(timeRemainingSec * 2.0F, 0.0F, 1.0F);
					alphaBottom *= alphaBottom;
					if(alphaTop == 0.0F && alphaBottom == 0.0F) {
						continue;
					}
					boolean blend = alphaTop < 1.0f || alphaBottom < 1.0f;
					if(blend != isBlend) {
						if(blend) {
							GlStateManager.enableBlend();
							GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
						}else {
							GlStateManager.disableBlend();
						}
						isBlend = blend;
					}
					int px = width - tex.width;
					drawTexturedGradientFBRect(px, baseOffset, tex.width, tex.height,
							((int) (alphaTop * 255.0f) << 24) | 0xFFFFFF,
							((int) (alphaBottom * 255.0f) << 24) | 0xFFFFFF, 200.0f);
					if(showXButtons && tex.hasHoverEvents) {
						if(mouseX >= px && mouseY >= baseOffset && mouseX < px + tex.width && mouseY < baseOffset + tex.height) {
							List<ClickEventZone> cursorEvents = tex.cursorEvents;
							if(cursorEvents != null) {
								for(int j = 0, m = cursorEvents.size(); j < m; ++j) {
									ClickEventZone evt = cursorEvents.get(j);
									if(evt.hasHoverEvent) {
										int offsetPosX = px + evt.posX;
										int offsetPosY = baseOffset + evt.posY;
										if(mouseX >= offsetPosX && mouseY >= offsetPosY && mouseX < offsetPosX + evt.width
												&& mouseY < offsetPosY + evt.height) {
											if(isBlend) {
												GlStateManager.disableBlend();
												isBlend = false;
											}
											mc.currentScreen.handleComponentHover(evt.chatComponent, mouseX, mouseY);
										}
									}
								}
							}
						}
					}
					baseOffset += tex.height * alphaTop;
				}
			}
			if(isBlend) {
				GlStateManager.disableBlend();
			}
		}
	}

	protected CachedNotifBadgeTexture renderBadge(NotificationBadge badge, int scaleFactor, boolean showXButton) {
		int badgeWidth = BADGE_WIDTH;
		int badgeHeight = 10;
		
		int leftPadding = 6;
		int rightPadding = 26;
		
		int mainIconSW = 32;
		if(badge.mainIcon != null) {
			int iw = badge.mainIcon.texture.getWidth();
			int ih = badge.mainIcon.texture.getHeight();
			float iaspect = (float)iw / (float)ih;
			mainIconSW = (int)(32 * iaspect);
			leftPadding += Math.min(mainIconSW, 64) + 3;
		}
		
		int textZoneWidth = badgeWidth - leftPadding - rightPadding;
		int bodyYOffset = 5;
		
		String titleText = null;
		IChatComponent titleComponent = badge.getTitleProfanityFilter();
		if(titleComponent != null) {
			titleText = titleComponent.getFormattedText();
			if(titleText.length() > 0) {
				badgeHeight += 12;
				bodyYOffset += 12;
			}else {
				titleText = null;
			}
		}
		
		if(badge.titleIcon != null && titleText == null) {
			badgeHeight += 12;
			bodyYOffset += 12;
		}
		
		float bodyFontSize = 0.75f;
		List<IChatComponent> bodyLines = null;
		List<ClickEventZone> clickEvents = null;
		IChatComponent rootClickEvt = null;
		boolean hasClickEvents = false;
		boolean hasHoverEvents = false;
		
		int bodyHeight = 0;
		
		IChatComponent bodyComponent = badge.getBodyProfanityFilter();
		if(bodyComponent != null) {
			if (bodyComponent.getChatStyle().getChatClickEvent() != null
					&& bodyComponent.getChatStyle().getChatClickEvent().getAction().shouldAllowInChat()) {
				rootClickEvt = bodyComponent;
			}
			bodyLines = GuiUtilRenderComponents.func_178908_a(bodyComponent, (int) (textZoneWidth / bodyFontSize),
					mc.fontRendererObj, true, true);
			
			int maxHeight = BADGE_HEIGHT - 32;
			int maxLines = MathHelper.floor_float(maxHeight / (9 * bodyFontSize));
			if(bodyLines.size() > maxLines) {
				bodyLines = bodyLines.subList(0, maxLines);
				bodyComponent = bodyLines.get(maxLines - 1);
				List<IChatComponent> siblings = bodyComponent.getSiblings();
				IChatComponent dots = new ChatComponentText("...");
				if(siblings != null && siblings.size() > 0) {
					dots.setChatStyle(siblings.get(siblings.size() - 1).getChatStyle());
				}
				bodyComponent.appendSibling(dots);
			}
			bodyHeight = MathHelper.floor_float(bodyLines.size() * (9 * bodyFontSize));
		}

		String sourceText = null;
		IChatComponent sourceComponent = badge.getSourceProfanityFilter();
		if(sourceComponent != null) {
			sourceText = sourceComponent.getFormattedText();
			if(sourceText.length() == 0) {
				sourceText = null;
			}
		}
		
		if(badge.mainIcon != null) {
			bodyHeight = Math.max(sourceText != null ? 30 : 32, bodyHeight);
		}
		
		if(sourceText != null) {
			badgeHeight += 6;
		}
		
		badgeHeight += bodyHeight;
		
		badgeHeight = Math.max(badgeHeight, showXButton ? 42 : 26);
		
		if(badgeHeight > BADGE_HEIGHT) {
			logger.info("Warning: Badge {} was {} pixels too high!", badge.badgeUUID, BADGE_HEIGHT - badgeHeight);
			badgeHeight = BADGE_HEIGHT;
		}
		
		int glTex = GlStateManager.generateTexture();
		GlStateManager.bindTexture(glTex);
		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		EaglercraftGPU.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, badgeWidth * scaleFactor, badgeHeight * scaleFactor, 0, GL_RGBA,
				GL_UNSIGNED_BYTE, (ByteBuffer) null);
		_wglBindFramebuffer(_GL_FRAMEBUFFER, rendererFramebuffer);
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(glTex), 0);
		_wglDrawBuffers(_GL_COLOR_ATTACHMENT0);
		
		int[] oldViewport = new int[4];
		EaglercraftGPU.glGetInteger(GL_VIEWPORT, oldViewport);
		
		GlStateManager.viewport(0, 0, badgeWidth * scaleFactor, badgeHeight * scaleFactor);
		
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.enableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		
		GlStateManager.matrixMode(GL_PROJECTION);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0D, badgeWidth, badgeHeight, 0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.translate(0.0F, 0.0F, -2000.0F);

		Tessellator tess = Tessellator.getInstance();
		WorldRenderer worldRenderer = tess.getWorldRenderer();

		worldRenderer.begin(GL_QUADS, VertexFormat.POSITION_TEX_COLOR);
		
		mc.getTextureManager().bindTexture(eaglerGui);

		drawTexturedColoredRect(worldRenderer, 0, 0, 96, 192, 160, 8, (badge.backgroundColor >>> 16) & 0xFF,
				(badge.backgroundColor >>> 8) & 0xFF, badge.backgroundColor & 0xFF, 0xFF);
		
		drawTexturedColoredRect(worldRenderer, 0, 8, 96, 192 + (BADGE_HEIGHT - badgeHeight + 8), 160, (badgeHeight - 8),
				(badge.backgroundColor >>> 16) & 0xFF, (badge.backgroundColor >>> 8) & 0xFF,
				badge.backgroundColor & 0xFF, 0xFF);
		
		switch(badge.priority) {
		case LOW:
		default:
			drawTexturedColoredRect(worldRenderer, badgeWidth - 21, badgeHeight - 21, 192, 176, 16, 16, (badge.backgroundColor >>> 16) & 0xFF,
					(badge.backgroundColor >>> 8) & 0xFF, badge.backgroundColor & 0xFF, 0xFF);
			break;
		case NORMAL:
			drawTexturedColoredRect(worldRenderer, badgeWidth - 21, badgeHeight - 21, 208, 176, 16, 16, 0xFF, 0xFF, 0xFF, 0xFF);
			break;
		case HIGHER:
			drawTexturedColoredRect(worldRenderer, badgeWidth - 21, badgeHeight - 21, 224, 176, 16, 16, 0xFF, 0xFF, 0xFF, 0xFF);
			break;
		case HIGHEST:
			drawTexturedColoredRect(worldRenderer, badgeWidth - 21, badgeHeight - 21, 240, 176, 16, 16, 0xFF, 0xFF, 0xFF, 0xFF);
			break;
		}
		
		if(showXButton) {
			drawTexturedColoredRect(worldRenderer, badgeWidth - 21, 5, 80, 208, 16, 16, 0xFF, 0xFF, 0xFF, 0xFF);
		}
		
		tess.draw();

		if(badge.mainIcon != null) {
			mc.getTextureManager().bindTexture(badge.mainIcon.resource);
			drawTexturedRect(6, bodyYOffset, mainIconSW, 32);
		}

		if(badge.titleIcon != null) {
			mc.getTextureManager().bindTexture(badge.titleIcon.resource);
			drawTexturedRect(6, 5, 8, 8);
		}
		
		if(titleText != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(6 + (badge.titleIcon != null ? 10 : 0), 6, 0.0f);
			GlStateManager.scale(0.75f, 0.75f, 0.75f);
			mc.fontRendererObj.drawStringWithShadow(titleText, 0, 0, badge.titleTxtColor);
			GlStateManager.popMatrix();
		}
		
		if(bodyLines != null && !bodyLines.isEmpty()) {
			GlStateManager.pushMatrix();
			if(!showXButton && badge.mainIcon == null && titleText != null) {
				bodyYOffset -= 2;
			}
			GlStateManager.translate(leftPadding, bodyYOffset, 0.0f);
			int l = bodyLines.size();
			GlStateManager.scale(bodyFontSize, bodyFontSize, bodyFontSize);
			for(int i = 0; i < l; ++i) {
				int startXLocal = 0;
				int startXReal = leftPadding;
				for(IChatComponent comp : bodyLines.get(i)) {
					int w = mc.fontRendererObj.drawStringWithShadow(
							comp.getChatStyle().getFormattingCode() + comp.getUnformattedTextForChat(), startXLocal,
							i * 9, badge.bodyTxtColor) - startXLocal;
					ClickEvent clickEvent = comp.getChatStyle().getChatClickEvent();
					HoverEvent hoverEvent = comp.getChatStyle().getChatHoverEvent();
					if(clickEvent != null && !clickEvent.getAction().shouldAllowInChat()) {
						clickEvent = null;
					}
					if(hoverEvent != null && !hoverEvent.getAction().shouldAllowInChat()) {
						hoverEvent = null;
					}
					if(clickEvent != null || hoverEvent != null) {
						hasClickEvents |= clickEvent != null;
						hasHoverEvents |= hoverEvent != null;
						if(clickEvents == null) {
							clickEvents = new ArrayList<>();
						}
						clickEvents.add(new ClickEventZone(startXReal + (int) (startXLocal * bodyFontSize),
								bodyYOffset + (int) (i * 9 * bodyFontSize), (int) (w * bodyFontSize),
								(int) (9 * bodyFontSize), comp, clickEvent != null, hoverEvent != null));
					}
					startXLocal += w;
				}
			}
			GlStateManager.popMatrix();
		}
		
		if(sourceText != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(badgeWidth - 21, badgeHeight - 5, 0.0f);
			GlStateManager.scale(0.5f, 0.5f, 0.5f);
			mc.fontRendererObj.drawStringWithShadow(sourceText, -mc.fontRendererObj.getStringWidth(sourceText) - 4, -10, badge.sourceTxtColor);
			GlStateManager.popMatrix();
		}
		
		GlStateManager.matrixMode(GL_PROJECTION);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.popMatrix();

		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();

		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
		GlStateManager.viewport(oldViewport[0], oldViewport[1], oldViewport[2], oldViewport[3]);
		
		return new CachedNotifBadgeTexture(glTex, scaleFactor, badgeWidth, badgeHeight, clickEvents, rootClickEvt, hasClickEvents, hasHoverEvents);
	}

	static void drawTexturedColoredRect(WorldRenderer worldRenderer, float xCoord, float yCoord, int minU,
			int minV, int width, int height, int r, int g, int b, int a) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		worldRenderer.pos((double) (xCoord + 0.0F), (double) (yCoord + (float) height), 0.0).color(r, g, b, a)
				.tex((double) ((float) (minU + 0) * f), (double) ((float) (minV + height) * f1)).endVertex();
		worldRenderer.pos((double) (xCoord + (float) width), (double) (yCoord + (float) height), 0.0).color(r, g, b, a)
				.tex((double) ((float) (minU + width) * f), (double) ((float) (minV + height) * f1)).endVertex();
		worldRenderer.pos((double) (xCoord + (float) width), (double) (yCoord + 0.0F), 0.0).color(r, g, b, a)
				.tex((double) ((float) (minU + width) * f), (double) ((float) (minV + 0) * f1)).endVertex();
		worldRenderer.pos((double) (xCoord + 0.0F), (double) (yCoord + 0.0F), 0.0).color(r, g, b, a)
				.tex((double) ((float) (minU + 0) * f), (double) ((float) (minV + 0) * f1)).endVertex();
	}

	static void drawTexturedGradientFBRect(float xCoord, float yCoord, int width, int height, int rgbaTop, int rgbaBottom, float zIndex) {
		int topR = (rgbaTop >>> 16) & 0xFF;
		int topG = (rgbaTop >>> 8) & 0xFF;
		int topB = rgbaTop & 0xFF;
		int topA = (rgbaTop >>> 24) & 0xFF;
		int bottomR = (rgbaBottom >>> 16) & 0xFF;
		int bottomG = (rgbaBottom >>> 8) & 0xFF;
		int bottomB = rgbaBottom & 0xFF;
		int bottomA = (rgbaBottom >>> 24) & 0xFF;
		Tessellator tess = Tessellator.getInstance();
		WorldRenderer worldRenderer = tess.getWorldRenderer();
		worldRenderer.begin(GL_QUADS, VertexFormat.POSITION_TEX_COLOR);
		worldRenderer.pos((double) (xCoord + 0.0F), (double) (yCoord + (float) height), zIndex)
				.color(bottomR, bottomG, bottomB, bottomA).tex(0.0, 0.0).endVertex();
		worldRenderer.pos((double) (xCoord + (float) width), (double) (yCoord + (float) height), zIndex)
				.color(bottomR, bottomG, bottomB, bottomA).tex(1.0, 0.0).endVertex();
		worldRenderer.pos((double) (xCoord + (float) width), (double) (yCoord + 0.0F), zIndex)
				.color(topR, topG, topB, topA).tex(1.0, 1.0).endVertex();
		worldRenderer.pos((double) (xCoord + 0.0F), (double) (yCoord + 0.0F), zIndex).color(topR, topG, topB, topA)
				.tex(0.0, 1.0).endVertex();
		tess.draw();
	}

	static void drawTexturedRect(float xCoord, float yCoord, int width, int height) {
		Tessellator tess = Tessellator.getInstance();
		WorldRenderer worldRenderer = tess.getWorldRenderer();
		worldRenderer.begin(GL_QUADS, VertexFormat.POSITION_TEX);
		worldRenderer.pos((double) (xCoord + 0.0F), (double) (yCoord + (float) height), 0.0).tex(0.0, 1.0).endVertex();
		worldRenderer.pos((double) (xCoord + (float) width), (double) (yCoord + (float) height), 0.0).tex(1.0, 1.0).endVertex();
		worldRenderer.pos((double) (xCoord + (float) width), (double) (yCoord + 0.0F), 0.0).tex(1.0, 0.0).endVertex();
		worldRenderer.pos((double) (xCoord + 0.0F), (double) (yCoord + 0.0F), 0.0).tex(0.0, 0.0).endVertex();
		tess.draw();
	}

	public void destroy() {
		if(rendererFramebuffer != null) {
			_wglDeleteFramebuffer(rendererFramebuffer);
			rendererFramebuffer = null;
		}
	}

}