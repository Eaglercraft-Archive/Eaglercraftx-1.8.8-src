package net.lax1dude.eaglercraft.v1_8.voice;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.GL_GREATER;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.util.ResourceLocation;

/**
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class GuiVoiceOverlay extends Gui {

	public final Minecraft mc;
	public int width;
	public int height;
	
	private long pttTimer = 0l;
	
	public GuiVoiceOverlay(Minecraft mc) {
		this.mc = mc;
	}
	
	public void setResolution(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	private static final ResourceLocation voiceGuiIcons = new ResourceLocation("eagler:gui/eagler_gui.png");

	public void drawOverlay() {
		if(mc.theWorld != null && VoiceClientController.getVoiceStatus() == EnumVoiceChannelStatus.CONNECTED && VoiceClientController.getVoiceChannel() != EnumVoiceChannelType.NONE &&
				!(mc.currentScreen != null && (mc.currentScreen instanceof GuiIngameMenu))) {
			
			if(mc.currentScreen != null && mc.currentScreen.doesGuiPauseGame()) {
				return;
			}
			
			GlStateManager.disableLighting();
			GlStateManager.disableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.alphaFunc(GL_GREATER, 0.1F);
			GlStateManager.pushMatrix();
			
			if(mc.currentScreen == null || (mc.currentScreen instanceof GuiChat)) {
				GlStateManager.translate(width / 2 + 77, height - 56, 0.0f);
				if(mc.thePlayer == null || mc.thePlayer.capabilities.isCreativeMode) {
					GlStateManager.translate(0.0f, 16.0f, 0.0f);
				}
			}else {
				GlStateManager.translate(width / 2 + 10, 4, 0.0f);
			}

			GlStateManager.scale(0.75f, 0.75f, 0.75f);
			
			String txxt = "press '" + Keyboard.getKeyName(mc.gameSettings.voicePTTKey) + "'";
			drawString(mc.fontRendererObj, txxt, -3 - mc.fontRendererObj.getStringWidth(txxt), 9, 0xDDDDDD);

			GlStateManager.scale(0.66f, 0.66f, 0.66f);
			
			mc.getTextureManager().bindTexture(voiceGuiIcons);
			
			if((mc.currentScreen == null || !mc.currentScreen.blockPTTKey()) && Keyboard.isKeyDown(mc.gameSettings.voicePTTKey)) {
				long millis = System.currentTimeMillis();
				if(pttTimer == 0l) {
					pttTimer = millis;
				}
				GlStateManager.color(0.2f, 0.2f, 0.2f, 1.0f);
				drawTexturedModalRect(0, 0, 0, 64, 32, 32);
				GlStateManager.translate(-1.5f, -1.5f, 0.0f);
				if(millis - pttTimer < 1050l) {
					if((millis - pttTimer) % 300l < 150l) {
						GlStateManager.color(0.9f, 0.2f, 0.2f, 1.0f);
					}else {
						GlStateManager.color(0.9f, 0.7f, 0.7f, 1.0f);
					}
				}else {
					GlStateManager.color(0.9f, 0.3f, 0.3f, 1.0f);
				}
				drawTexturedModalRect(0, 0, 0, 64, 32, 32);
			}else {
				pttTimer = 0l;
				GlStateManager.color(0.2f, 0.2f, 0.2f, 1.0f);
				drawTexturedModalRect(0, 0, 0, 32, 32, 32);
				GlStateManager.translate(-1.5f, -1.5f, 0.0f);
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				drawTexturedModalRect(0, 0, 0, 32, 32, 32);
				GlStateManager.translate(-0.5f, -0.5f, 0.0f);
				drawTexturedModalRect(0, 0, 0, 32, 32, 32);
			}
			
			GlStateManager.popMatrix();
			
			if(VoiceClientController.getVoiceChannel() == EnumVoiceChannelType.PROXIMITY) {
				Set<EaglercraftUUID> listeners = VoiceClientController.getVoiceListening();
				if(listeners.size() > 0) {
					Set<EaglercraftUUID> speakers = VoiceClientController.getVoiceSpeaking();
					Set<EaglercraftUUID> muted = VoiceClientController.getVoiceMuted();
					
					List<EaglercraftUUID> listenerList = new ArrayList();
					listenerList.addAll(listeners);
					listenerList.removeAll(muted);
					
					while(listenerList.size() > 5) {
						boolean flag = false;
						for(int i = 0, l = listenerList.size(); i < l; ++i) {
							if(!speakers.contains(listenerList.get(i))) {
								listenerList.remove(i);
								flag = true;
								break;
							}
						}
						if(!flag) {
							break;
						}
					}
					
					int more = listenerList.size() - 5;
					
					int ww = width;
					int hh = height;
					
					if(mc.currentScreen != null && (mc.currentScreen instanceof GuiChat)) {
						hh -= 15;
					}
					
					List<String> listenerListStr = new ArrayList(Math.min(5, listenerList.size()));
					
					int left = 50;
					for(int i = 0, l = listenerList.size(); i < l && i < 5; ++i) {
						String txt = VoiceClientController.getVoiceUsername(listenerList.get(i));
						listenerListStr.add(txt);
						int j = mc.fontRendererObj.getStringWidth(txt) + 4;
						if(j > left) {
							left = j;
						}
					}
					
					if(more > 0) {
						GlStateManager.pushMatrix();
						GlStateManager.translate(ww - left + 3, hh - 10, left);
						GlStateManager.scale(0.75f, 0.75f, 0.75f);
						drawString(mc.fontRendererObj, "(" + more + " more)", 0, 0, 0xBBBBBB);
						GlStateManager.popMatrix();
						hh -= 9;
					}
					
					for(int i = 0, l = listenerList.size(); i < l && i < 5; ++i) {
						boolean speaking = speakers.contains(listenerList.get(i));
						float speakf = speaking ? 1.0f : 0.75f;
						
						drawString(mc.fontRendererObj, listenerListStr.get(i), ww - left, hh - 13 - i * 11, speaking ? 0xEEEEEE : 0xBBBBBB);
						
						mc.getTextureManager().bindTexture(voiceGuiIcons);
						
						GlStateManager.pushMatrix();
						GlStateManager.translate(ww - left - 14, hh - 14 - i * 11, 0.0f);
						
						GlStateManager.scale(0.75f, 0.75f, 0.75f);
						GlStateManager.color(speakf * 0.2f, speakf * 0.2f, speakf * 0.2f, 1.0f);
						drawTexturedModalRect(0, 0, 64, speaking ? 176 : 208, 16, 16);
						GlStateManager.translate(0.25f, 0.25f, 0.0f);
						drawTexturedModalRect(0, 0, 64, speaking ? 176 : 208, 16, 16);
						
						GlStateManager.translate(-1.25f, -1.25f, 0.0f);
						GlStateManager.color(speakf, speakf, speakf, 1.0f);
						drawTexturedModalRect(0, 0, 64, speaking ? 176 : 208, 16, 16);
						
						GlStateManager.popMatrix();
						
					}
					
				}
			}else if(VoiceClientController.getVoiceChannel() == EnumVoiceChannelType.GLOBAL) {
				Set<EaglercraftUUID> speakers = VoiceClientController.getVoiceSpeaking();
				Set<EaglercraftUUID> muted = VoiceClientController.getVoiceMuted();
				
				List<EaglercraftUUID> listenerList = new ArrayList();
				listenerList.addAll(speakers);
				listenerList.removeAll(muted);
				
				int more = listenerList.size() - 5;
				
				int ww = width;
				int hh = height;
				
				if(mc.currentScreen != null && (mc.currentScreen instanceof GuiChat)) {
					hh -= 15;
				}
				
				List<String> listenerListStr = new ArrayList(Math.min(5, listenerList.size()));
				
				int left = 50;
				for(int i = 0, l = listenerList.size(); i < l && i < 5; ++i) {
					String txt = VoiceClientController.getVoiceUsername(listenerList.get(i));
					listenerListStr.add(txt);
					int j = mc.fontRendererObj.getStringWidth(txt) + 4;
					if(j > left) {
						left = j;
					}
				}
				
				if(more > 0) {
					GlStateManager.pushMatrix();
					GlStateManager.translate(ww - left + 3, hh - 10, left);
					GlStateManager.scale(0.75f, 0.75f, 0.75f);
					drawString(mc.fontRendererObj, "(" + more + " more)", 0, 0, 0xBBBBBB);
					GlStateManager.popMatrix();
					hh -= 9;
				}
				
				for(int i = 0, l = listenerList.size(); i < l && i < 5; ++i) {
					drawString(mc.fontRendererObj, listenerListStr.get(i), ww - left, hh - 13 - i * 11, 0xEEEEEE);
					
					mc.getTextureManager().bindTexture(voiceGuiIcons);
					
					GlStateManager.pushMatrix();
					GlStateManager.translate(ww - left - 14, hh - 14 - i * 11, 0.0f);
					
					GlStateManager.scale(0.75f, 0.75f, 0.75f);
					GlStateManager.color(0.2f, 0.2f, 0.2f, 1.0f);
					drawTexturedModalRect(0, 0, 64, 176, 16, 16);
					GlStateManager.translate(0.25f, 0.25f, 0.0f);
					drawTexturedModalRect(0, 0, 64, 176, 16, 16);
					
					GlStateManager.translate(-1.25f, -1.25f, 0.0f);
					GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
					drawTexturedModalRect(0, 0, 64, 176, 16, 16);
					
					GlStateManager.popMatrix();
					
				}
			}
		}
	}

}
