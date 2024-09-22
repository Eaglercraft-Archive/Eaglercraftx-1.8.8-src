package net.lax1dude.eaglercraft.v1_8.notifications;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketNotifBadgeShowV4EAG.EnumBadgePriority;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

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
public class GuiSlotNotifications extends GuiSlot {

	private static final ResourceLocation eaglerGui = new ResourceLocation("eagler:gui/eagler_gui.png");
	private static final ResourceLocation largeNotifBk = new ResourceLocation("eagler:gui/notif_bk_large.png");

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

	final GuiScreenNotifications parent;
	final List<NotifBadgeSlot> currentDisplayNotifs;

	int mouseX;
	int mouseY;

	protected static class NotifBadgeSlot {
		
		protected final NotificationBadge badge;
		protected final List<ClickEventZone> cursorEvents = new ArrayList<>();
		protected int currentScreenX = -69420;
		protected int currentScreenY = -69420;
		
		protected NotifBadgeSlot(NotificationBadge badge) {
			this.badge = badge;
		}
		
	}

	public GuiSlotNotifications(GuiScreenNotifications parent) {
		super(GuiScreenNotifications.getMinecraft(parent), parent.width, parent.height, 32, parent.height - 44, 68);
		this.parent = parent;
		this.currentDisplayNotifs = new ArrayList<>();
	}

	@Override
	protected int getSize() {
		return currentDisplayNotifs.size();
	}

	@Override
	protected void elementClicked(int id, boolean doubleClk, int xx, int yy) {
		if(selectedElement != id) return; //workaround for vanilla bs
		if(id < currentDisplayNotifs.size()) {
			NotifBadgeSlot slot = currentDisplayNotifs.get(id);
			if(slot.currentScreenY != -69420) {
				int w = getListWidth();
				int localX = xx - slot.currentScreenX;
				int localY = yy - slot.currentScreenY;
				if(localX >= w - 22 && localX < w - 5 && localY >= 5 && localY < 21) {
					slot.badge.removeNotif();
					mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
					return;
				}
				IChatComponent cmp = slot.badge.bodyComponent;
				if(cmp != null) {
					if(doubleClk) {
						if (cmp.getChatStyle().getChatClickEvent() != null
								&& cmp.getChatStyle().getChatClickEvent().getAction().shouldAllowInChat()) {
							if(parent.handleComponentClick(cmp)) {
								mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
								return;
							}
						}
					}else {
						if(parent.selected != id) {
							parent.selected = id;
						}else {
							List<ClickEventZone> cursorEvents = slot.cursorEvents;
							if(cursorEvents != null && !cursorEvents.isEmpty()) {
								for(int j = 0, m = cursorEvents.size(); j < m; ++j) {
									ClickEventZone evt = cursorEvents.get(j);
									if(evt.hasClickEvent) {
										int offsetPosX = slot.currentScreenX + evt.posX;
										int offsetPosY = slot.currentScreenY + evt.posY;
										if(xx >= offsetPosX && yy >= offsetPosY && xx < offsetPosX + evt.width && yy < offsetPosY + evt.height) {
											if(parent.handleComponentClick(evt.chatComponent)) {
												mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
												return;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected boolean isSelected(int var1) {
		return var1 == parent.selected;
	}

	@Override
	protected void drawBackground() {
		parent.drawBackground(0);
	}

	@Override
	protected void drawSlot(int id, int xx, int yy, int width, int height, int ii) {
		if(id < currentDisplayNotifs.size()) {
			NotifBadgeSlot slot = currentDisplayNotifs.get(id);
			slot.currentScreenX = xx;
			slot.currentScreenY = yy;
			NotificationBadge bd = slot.badge;
			if(yy + 32 > this.top && yy + 32 < this.bottom) {
				bd.markRead();
			}
			GlStateManager.pushMatrix();
			GlStateManager.translate(xx, yy, 0.0f);
			mc.getTextureManager().bindTexture(largeNotifBk);
			int badgeWidth = getListWidth() - 4;
			int badgeHeight = getSlotHeight() - 4;
			float r = ((bd.backgroundColor >> 16) & 0xFF) * 0.00392156f;
			float g = ((bd.backgroundColor >> 8) & 0xFF) * 0.00392156f;
			float b = (bd.backgroundColor & 0xFF) * 0.00392156f;
			if(parent.selected != id) {
				r *= 0.85f;
				g *= 0.85f;
				b *= 0.85f;
			}
			GlStateManager.color(r, g, b, 1.0f);
			parent.drawTexturedModalRect(0, 0, 0, bd.unreadFlagRender ? 64 : 0, badgeWidth - 32, 64);
			parent.drawTexturedModalRect(badgeWidth - 32, 0, 224, bd.unreadFlagRender ? 64 : 0, 32, 64);
			mc.getTextureManager().bindTexture(eaglerGui);
			if(bd.priority == EnumBadgePriority.LOW) {
				parent.drawTexturedModalRect(badgeWidth - 21, badgeHeight - 21, 192, 176, 16, 16);
			}
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			
			switch(bd.priority) {
			default:
				break;
			case NORMAL:
				parent.drawTexturedModalRect(badgeWidth - 21, badgeHeight - 21, 208, 176, 16, 16);
				break;
			case HIGHER:
				parent.drawTexturedModalRect(badgeWidth - 21, badgeHeight - 21, 224, 176, 16, 16);
				break;
			case HIGHEST:
				parent.drawTexturedModalRect(badgeWidth - 21, badgeHeight - 21, 240, 176, 16, 16);
				break;
			}
			
			int bodyYOffset = 16;
					
			int leftPadding = 6;
			int rightPadding = 26;
			
			int mainIconSW = 32;
			boolean mainIconEn = bd.mainIcon != null && bd.mainIcon.isValid();
			if(mainIconEn) {
				int iw = bd.mainIcon.texture.getWidth();
				int ih = bd.mainIcon.texture.getHeight();
				float iaspect = (float)iw / (float)ih;
				mainIconSW = (int)(32 * iaspect);
				leftPadding += Math.min(mainIconSW, 64) + 3;
			}
			
			int textZoneWidth = badgeWidth - leftPadding - rightPadding;
			
			if(mainIconEn) {
				mc.getTextureManager().bindTexture(bd.mainIcon.resource);
				ServerNotificationRenderer.drawTexturedRect(6, bodyYOffset, mainIconSW, 32);
			}

			boolean titleIconEn = bd.titleIcon != null && bd.titleIcon.isValid();
			if(titleIconEn) {
				mc.getTextureManager().bindTexture(bd.titleIcon.resource);
				ServerNotificationRenderer.drawTexturedRect(6, 5, 8, 8);
			}
			
			String titleText = "";
			IChatComponent titleComponent = bd.getTitleProfanityFilter();
			if(titleComponent != null) {
				titleText = titleComponent.getFormattedText();
			}
			
			titleText += EnumChatFormatting.GRAY + (titleText.length() > 0 ? " @ " : "@ ")
					+ (bd.unreadFlagRender ? EnumChatFormatting.YELLOW : EnumChatFormatting.GRAY)
					+ formatAge(bd.serverTimestamp);

			GlStateManager.pushMatrix();
			GlStateManager.translate(6 + (titleIconEn ? 10 : 0), 6, 0.0f);
			GlStateManager.scale(0.75f, 0.75f, 0.75f);
			mc.fontRendererObj.drawStringWithShadow(titleText, 0, 0, bd.titleTxtColor);
			GlStateManager.popMatrix();
			
			String sourceText = null;
			IChatComponent sourceComponent = bd.getSourceProfanityFilter();
			if(sourceComponent != null) {
				sourceText = sourceComponent.getFormattedText();
				if(sourceText.length() == 0) {
					sourceText = null;
				}
			}
			
			List<IChatComponent> bodyLines = null;
			float bodyFontSize = (sourceText != null || titleIconEn) ? 0.75f : 1.0f;
			IChatComponent bodyComponent = bd.getBodyProfanityFilter();
			if(bodyComponent != null) {
				bodyLines = GuiUtilRenderComponents.func_178908_a(bodyComponent, (int) (textZoneWidth / bodyFontSize),
						mc.fontRendererObj, true, true);
				
				int maxHeight = badgeHeight - (sourceText != null ? 32 : 22);
				int maxLines = MathHelper.floor_float(maxHeight / (9 * bodyFontSize));
				if(bodyLines.size() > maxLines) {
					bodyLines = bodyLines.subList(0, maxLines);
					IChatComponent cmp = bodyLines.get(maxLines - 1);
					List<IChatComponent> siblings = cmp.getSiblings();
					IChatComponent dots = new ChatComponentText("...");
					if(siblings != null && siblings.size() > 0) {
						dots.setChatStyle(siblings.get(siblings.size() - 1).getChatStyle());
					}
					cmp.appendSibling(dots);
				}
			}
			
			slot.cursorEvents.clear();
			if(bodyLines != null && !bodyLines.isEmpty()) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(leftPadding, bodyYOffset, 0.0f);
				int l = bodyLines.size();
				GlStateManager.scale(bodyFontSize, bodyFontSize, bodyFontSize);
				IChatComponent toolTip = null;
				for(int i = 0; i < l; ++i) {
					int startXLocal = 0;
					int startXReal = leftPadding;
					for(IChatComponent comp : bodyLines.get(i)) {
						int w = mc.fontRendererObj.drawStringWithShadow(
								comp.getChatStyle().getFormattingCode() + comp.getUnformattedTextForChat(), startXLocal,
								i * 9, bd.bodyTxtColor) - startXLocal;
						ClickEvent clickEvent = comp.getChatStyle().getChatClickEvent();
						HoverEvent hoverEvent = toolTip == null ? comp.getChatStyle().getChatHoverEvent() : null;
						if(clickEvent != null && !clickEvent.getAction().shouldAllowInChat()) {
							clickEvent = null;
						}
						if(hoverEvent != null && !hoverEvent.getAction().shouldAllowInChat()) {
							hoverEvent = null;
						}
						if(clickEvent != null) {
							slot.cursorEvents.add(new ClickEventZone(startXReal + (int) (startXLocal * bodyFontSize),
									bodyYOffset + (int) (i * 9 * bodyFontSize), (int) (w * bodyFontSize),
									(int) (9 * bodyFontSize), comp, clickEvent != null, hoverEvent != null));
						}
						if(hoverEvent != null) {
							int px = xx + startXReal + (int) (startXLocal * bodyFontSize);
							int py = yy + bodyYOffset + (int) (i * 9 * bodyFontSize);
							if (mouseX >= px && mouseX < px + (int) (w * bodyFontSize) && mouseY >= py
									&& mouseY < py + (int) (9 * bodyFontSize)) {
								toolTip = comp;
							}
						}
						startXLocal += w;
					}
				}
				GlStateManager.popMatrix();
				if(toolTip != null) {
					parent.handleComponentHover(toolTip, mouseX - xx, mouseY - yy);
				}
			}
			
			if(sourceText != null) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(badgeWidth - 21, badgeHeight - 5, 0.0f);
				GlStateManager.scale(0.75f, 0.75f, 0.75f);
				mc.fontRendererObj.drawStringWithShadow(sourceText, -mc.fontRendererObj.getStringWidth(sourceText) - 4, -10, bd.sourceTxtColor);
				GlStateManager.popMatrix();
			}
			
			GlStateManager.popMatrix();
		}
	}

	private String formatAge(long serverTimestamp) {
		long cur = System.currentTimeMillis();
		long daysAgo = Math.round((cur - serverTimestamp) / 86400000.0);
		String ret = dateFormat.format(new Date(serverTimestamp));
		if(daysAgo > 0l) {
			ret += " (" + daysAgo + (daysAgo == 1l ? " day" : " days") + " ago)";
		}else if(daysAgo < 0l) {
			ret += " (in " + -daysAgo + (daysAgo == -1l ? " day" : " days") + ")";
		}
		return ret;
	}

	@Override
	public int getListWidth() {
		return 224;
	}

	@Override
	public void drawScreen(int mouseXIn, int mouseYIn, float parFloat1) {
		mouseX = mouseXIn;
		mouseY = mouseYIn;
		for(int i = 0, l = currentDisplayNotifs.size(); i < l; ++i) {
			NotifBadgeSlot slot = currentDisplayNotifs.get(i);
			slot.currentScreenX = -69420;
			slot.currentScreenY = -69420;
		}
		super.drawScreen(mouseXIn, mouseYIn, parFloat1);
	}
}
