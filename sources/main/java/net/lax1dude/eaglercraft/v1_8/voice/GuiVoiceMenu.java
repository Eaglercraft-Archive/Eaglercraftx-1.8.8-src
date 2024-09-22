package net.lax1dude.eaglercraft.v1_8.voice;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.List;
import java.util.Set;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiSlider2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
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
public class GuiVoiceMenu extends Gui {

	public class AbortedException extends RuntimeException {
	}

	private static final ResourceLocation voiceGuiIcons = new ResourceLocation("eagler:gui/eagler_gui.png");

	protected final GuiScreen parent;

	protected Minecraft mc;
	protected FontRenderer fontRendererObj;
	protected int width;
	protected int height;

	protected int voiceButtonOFFposX;
	protected int voiceButtonOFFposY;
	protected int voiceButtonOFFposW;
	protected int voiceButtonOFFposH;

	protected int voiceButtonRADIUSposX;
	protected int voiceButtonRADIUSposY;
	protected int voiceButtonRADIUSposW;
	protected int voiceButtonRADIUSposH;

	protected int voiceButtonGLOBALposX;
	protected int voiceButtonGLOBALposY;
	protected int voiceButtonGLOBALposW;
	protected int voiceButtonGLOBALposH;

	protected int voiceScreenButtonOFFposX;
	protected int voiceScreenButtonOFFposY;
	protected int voiceScreenButtonOFFposW;
	protected int voiceScreenButtonOFFposH;

	protected int voiceScreenButtonRADIUSposX;
	protected int voiceScreenButtonRADIUSposY;
	protected int voiceScreenButtonRADIUSposW;
	protected int voiceScreenButtonRADIUSposH;

	protected int voiceScreenButtonGLOBALposX;
	protected int voiceScreenButtonGLOBALposY;
	protected int voiceScreenButtonGLOBALposW;
	protected int voiceScreenButtonGLOBALposH;

	protected int voiceScreenButtonChangeRadiusposX;
	protected int voiceScreenButtonChangeRadiusposY;
	protected int voiceScreenButtonChangeRadiusposW;
	protected int voiceScreenButtonChangeRadiusposH;
	
	protected int voiceScreenVolumeIndicatorX;
	protected int voiceScreenVolumeIndicatorY;
	protected int voiceScreenVolumeIndicatorW;
	protected int voiceScreenVolumeIndicatorH;

	protected boolean showSliderBlocks = false;
	protected boolean showSliderVolume = false;
	protected boolean showPTTKeyConfig = false;
	protected int showNewPTTKey = 0;
	protected GuiSlider2 sliderBlocks = null;
	protected GuiSlider2 sliderListenVolume = null;
	protected GuiSlider2 sliderSpeakVolume = null;

	protected GuiButton applyRadiusButton = null;
	protected GuiButton applyVolumeButton = null;
	protected GuiButton noticeContinueButton = null;
	protected GuiButton noticeCancelButton = null;

	protected static boolean showingCompatWarning = false;
	protected static boolean showCompatWarning = true;
	
	protected static boolean showingTrackingWarning = false;
	protected static boolean showTrackingWarning = true;
	
	protected static EnumVoiceChannelType continueChannel = null;
	
	public GuiVoiceMenu(GuiScreen parent) {
		this.parent = parent;
	}
	
	public void setResolution(Minecraft mc, int w, int h) {
		this.mc = mc;
		this.fontRendererObj = mc.fontRendererObj;
		this.width = w;
		this.height = h;
		initGui();
	}
	
	public void initGui() {
		this.sliderBlocks = new GuiSlider2(-1, (width - 150) / 2, height / 3 + 20, 150, 20, (VoiceClientController.getVoiceProximity() - 5) / 17.0f, 1.0f) {
			protected String updateDisplayString() {
				return (int)((sliderValue * 17.0f) + 5.0f) + " Blocks";
			}
		};
		sliderBlocks.displayString = "" + VoiceClientController.getVoiceProximity() + " Blocks";
		this.sliderListenVolume = new GuiSlider2(-1, (width - 150) / 2, height / 3 + 10, 150, 20, VoiceClientController.getVoiceListenVolume(), 1.0f);
		this.sliderSpeakVolume = new GuiSlider2(-1, (width - 150) / 2, height / 3 + 56, 150, 20, VoiceClientController.getVoiceSpeakVolume(), 1.0f);
		
		applyRadiusButton = new GuiButton(2, (width - 150) / 2, height / 3 + 49, 150, 20, I18n.format("voice.apply"));
		applyVolumeButton = new GuiButton(3, (width - 150) / 2, height / 3 + 90, 150, 20, I18n.format("voice.apply"));
		noticeContinueButton = new GuiButton(5, (width - 150) / 2, height / 3 + 60, 150, 20, I18n.format("voice.unsupportedWarning10"));
		noticeCancelButton = new GuiButton(6, (width - 150) / 2, height / 3 + 90, 150, 20, I18n.format("voice.unsupportedWarning11"));
		applyRadiusButton.visible = applyVolumeButton.visible = noticeContinueButton.visible = noticeCancelButton.visible = false;
	}
	
	private void drawButtons(int mx, int my, float partialTicks) {
		applyRadiusButton.drawButton(mc, mx, my);
		applyVolumeButton.drawButton(mc, mx, my);
		noticeContinueButton.drawButton(mc, mx, my);
		noticeCancelButton.drawButton(mc, mx, my);
	}
	
	public void drawScreen(int mx, int my, float partialTicks) {
		String txt = I18n.format("voice.title");
		drawString(fontRendererObj, txt, width - 5 - fontRendererObj.getStringWidth(txt), 5, 0xFFCC22);
		
		applyRadiusButton.visible = showSliderBlocks;
		applyVolumeButton.visible = showSliderVolume;
		
		if(showSliderBlocks || showSliderVolume || showPTTKeyConfig) {
			
			drawRect(0, 0, this.width, this.height, 0xB0101010);
			
			if(showSliderBlocks) {
				
				drawRect(width / 2 - 86, height / 4 - 1, this.width / 2 + 86, height / 3 + 64 + height / 16, 0xFFDDDDDD);
				drawRect(width / 2 - 85, height / 4 + 0, this.width / 2 + 85, height / 3 + 63 + height / 16, 0xFF333333);
				
				drawCenteredString(this.fontRendererObj, I18n.format("voice.radiusTitle"), this.width / 2, height / 4 + 9, 16777215);
				drawString(this.fontRendererObj, I18n.format("voice.radiusLabel"), (this.width - 150) / 2 + 3, height / 3 + 6, 0xCCCCCC);
				sliderBlocks.drawButton(mc, mx, my);
				
			}else if(showSliderVolume) {
				
				drawRect(width / 2 - 86, height / 4 - 11, this.width / 2 + 86, height / 3 + 104 + height / 16, 0xFFDDDDDD);
				drawRect(width / 2 - 85, height / 4 - 10, this.width / 2 + 85, height / 3 + 103 + height / 16, 0xFF333333);
				
				drawCenteredString(this.fontRendererObj, I18n.format("voice.volumeTitle"), this.width / 2, height / 4 - 1, 16777215);
				drawString(this.fontRendererObj, I18n.format("voice.volumeListen"), (this.width - 150) / 2 + 3, height / 3 - 4, 0xCCCCCC);
				sliderListenVolume.drawButton(mc, mx, my);
				
				drawString(this.fontRendererObj, I18n.format("voice.volumeSpeak"), (this.width - 150) / 2 + 3, height / 3 + 42, 0xCCCCCC);
				sliderSpeakVolume.drawButton(mc, mx, my);
				
			}else if(showPTTKeyConfig) {
				
				drawRect(width / 2 - 86, height / 3 - 10, this.width / 2 + 86, height / 3 + 35, 0xFFDDDDDD);
				drawRect(width / 2 - 85, height / 3 - 9, this.width / 2 + 85, height / 3 + 34, 0xFF333333);
				
				if(showNewPTTKey > 0) {
					GlStateManager.pushMatrix();
					GlStateManager.translate(this.width / 2, height / 3 + 5, 0.0f);
					GlStateManager.scale(2.0f, 2.0f, 2.0f);
					drawCenteredString(this.fontRendererObj, Keyboard.getKeyName(mc.gameSettings.voicePTTKey), 0, 0, 0xFFCC11);
					GlStateManager.popMatrix();
				}else {
					drawCenteredString(this.fontRendererObj, I18n.format("voice.pttChangeDesc"), this.width / 2, height / 3 + 8, 16777215);
				}
			}
			
			drawButtons(mx, my, partialTicks);
			throw new AbortedException();
		}
		
		GlStateManager.pushMatrix();
		
		GlStateManager.translate(width - 6, 15, 0.0f);
		GlStateManager.scale(0.75f, 0.75f, 0.75f);
		
		if(!VoiceClientController.isClientSupported()) {
			txt = I18n.format("voice.titleVoiceUnavailable");
			drawString(fontRendererObj, txt, 1 - fontRendererObj.getStringWidth(txt), 6, 0xFF7777);
			txt = I18n.format("voice.titleVoiceBrowserError");
			drawString(fontRendererObj, txt, 1 - fontRendererObj.getStringWidth(txt), 19, 0xAA4444);
			GlStateManager.popMatrix();
			return;
		}
		
		if(!VoiceClientController.isServerSupported()) {
			txt = I18n.format("voice.titleNoVoice");
			drawString(fontRendererObj, txt, 1 - fontRendererObj.getStringWidth(txt), 5, 0xFF7777);
			GlStateManager.popMatrix();
			return;
		}

		int xo = 0;
		// this feature is optional
		//if(VoiceClientController.voiceRelayed()) {
		//	txt = I18n.format("voice.warning1");
		//	drawString(fontRendererObj, txt, 1 - fontRendererObj.getStringWidth(txt), 8, 0xBB9999);
		//	txt = I18n.format("voice.warning2");
		//	drawString(fontRendererObj, txt, 1 - fontRendererObj.getStringWidth(txt), 18, 0xBB9999);
		//	txt = I18n.format("voice.warning3");
		//	drawString(fontRendererObj, txt, 1 - fontRendererObj.getStringWidth(txt), 28, 0xBB9999);
		//	xo = 43;
		//	GlStateManager.translate(0.0f, xo, 0.0f);
		//}
		
		EnumVoiceChannelStatus status = VoiceClientController.getVoiceStatus();
		EnumVoiceChannelType channel = VoiceClientController.getVoiceChannel();
		
		boolean flag = false;
		
		if(channel == EnumVoiceChannelType.NONE) {
			flag = true;
		}else {
			if(status == EnumVoiceChannelStatus.CONNECTED) {
				
				if(channel == EnumVoiceChannelType.PROXIMITY) {
					txt = I18n.format("voice.connectedRadius").replace("$radius$", "" + VoiceClientController.getVoiceProximity()).replace("$f$", "");
					int w = fontRendererObj.getStringWidth(txt);
					int xx = width - 5 - (w * 3 / 4);
					int yy = 15 + (xo * 3 / 4);
					voiceScreenButtonChangeRadiusposX = xx;
					voiceScreenButtonChangeRadiusposY = yy;
					voiceScreenButtonChangeRadiusposW = width - 3 - xx;
					voiceScreenButtonChangeRadiusposH = 12;
					if(mx >= xx && my >= yy && mx < xx + voiceScreenButtonChangeRadiusposW && my < yy + 12) {
						txt = I18n.format("voice.connectedRadius").replace("$radius$", "" + VoiceClientController.getVoiceProximity())
								.replace("$f$", "" + EnumChatFormatting.UNDERLINE) + EnumChatFormatting.RESET;
					}
				}else {
					txt = I18n.format("voice.connectedGlobal");
				}
				
				voiceScreenVolumeIndicatorX = width - 15 - (104 * 3 / 4);
				voiceScreenVolumeIndicatorY = 15 + (xo * 3 / 4) + 30;
				voiceScreenVolumeIndicatorW = width - voiceScreenVolumeIndicatorX - 4;
				voiceScreenVolumeIndicatorH = 23;
				
				drawString(fontRendererObj, txt, 1 - fontRendererObj.getStringWidth(txt), 5, 0x66DD66);

				drawRect(-90, 42, 2, 52, 0xFFAAAAAA);
				drawRect(-89, 43, 1, 51, 0xFF222222);
				
				float vol = VoiceClientController.getVoiceListenVolume();
				drawRect(-89, 43, -89 + (int)(vol * 90), 51, 0xFF993322);
				
				for(float f = 0.07f; f < vol; f += 0.08f) {
					int ww = (int)(f * 90);
					drawRect(-89 + ww, 43, -89 + ww + 1, 51, 0xFF999999);
				}

				drawRect(-90, 57, 2, 67, 0xFFAAAAAA);
				drawRect(-89, 58, 1, 66, 0xFF222222);
				
				vol = VoiceClientController.getVoiceSpeakVolume();
				drawRect(-89, 58, -89 + (int)(vol * 90), 66, 0xFF993322);
				
				for(float f = 0.07f; f < vol; f += 0.08f) {
					int ww = (int)(f * 90);
					drawRect(-89 + ww, 58, -89 + ww + 1, 66, 0xFF999999);
				}
				
				mc.getTextureManager().bindTexture(voiceGuiIcons);
				GlStateManager.color(0.7f, 0.7f, 0.7f, 1.0f);
				
				GlStateManager.pushMatrix();
				GlStateManager.translate(-104.0f, 41.5f, 0.0f);
				GlStateManager.scale(0.7f, 0.7f, 0.7f);
				drawTexturedModalRect(0, 0, 64, 144, 16, 16);
				GlStateManager.popMatrix();
				
				GlStateManager.pushMatrix();
				GlStateManager.translate(-104.0f, 56.5f, 0.0f);
				GlStateManager.scale(0.7f, 0.7f, 0.7f);
				if((mc.currentScreen == null || !mc.currentScreen.blockPTTKey()) && Keyboard.isKeyDown(mc.gameSettings.voicePTTKey)) {
					GlStateManager.color(0.9f, 0.4f, 0.4f, 1.0f);
					drawTexturedModalRect(0, 0, 64, 64, 16, 16);
				}else {
					drawTexturedModalRect(0, 0, 64, 32, 16, 16);
				}
				GlStateManager.popMatrix();
				
				txt = I18n.format("voice.ptt", Keyboard.getKeyName(mc.gameSettings.voicePTTKey));
				drawString(fontRendererObj, txt, 1 - fontRendererObj.getStringWidth(txt) - 10, 76, 0x66DD66);

				mc.getTextureManager().bindTexture(voiceGuiIcons);
				GlStateManager.color(0.4f, 0.9f, 0.4f, 1.0f);
				GlStateManager.pushMatrix();
				GlStateManager.translate(-7.0f, 74.5f, 0.0f);
				GlStateManager.scale(0.35f, 0.35f, 0.35f);
				drawTexturedModalRect(0, 0, 32, 224, 32, 32);
				GlStateManager.popMatrix();
				
				txt = I18n.format("voice.playersListening");
				
				GlStateManager.pushMatrix();
				GlStateManager.translate(0.0f, 98.0f, 0.0f);
				GlStateManager.scale(1.2f, 1.2f, 1.2f);
				drawString(fontRendererObj, txt, -fontRendererObj.getStringWidth(txt), 0, 0xFF7777);
				GlStateManager.popMatrix();

				List<EaglercraftUUID> playersToRender = VoiceClientController.getVoiceRecent();
				
				if(playersToRender.size() > 0) {
					EaglercraftUUID uuid;
					Set<EaglercraftUUID> playersSpeaking = VoiceClientController.getVoiceSpeaking();
					Set<EaglercraftUUID> playersMuted = VoiceClientController.getVoiceMuted();
					for(int i = 0, l = playersToRender.size(); i < l; ++i) {
						uuid = playersToRender.get(i);
						txt = VoiceClientController.getVoiceUsername(uuid);
						
						boolean muted = playersMuted.contains(uuid);
						boolean speaking = !muted && playersSpeaking.contains(uuid);
						
						int mhy = voiceScreenVolumeIndicatorY + voiceScreenVolumeIndicatorH + 33 + i * 9;
						boolean hovered = mx >= voiceScreenVolumeIndicatorX - 3 && my >= mhy && mx < voiceScreenVolumeIndicatorX + voiceScreenVolumeIndicatorW + 2 && my < mhy + 9;
						float cm = hovered ? 1.5f : 1.0f;
						mc.getTextureManager().bindTexture(voiceGuiIcons);

						GlStateManager.pushMatrix();
						GlStateManager.translate(-100.0f, 115.0f + i * 12.0f, 0.0f);
						GlStateManager.scale(0.78f, 0.78f, 0.78f);
						
						if(muted) {
							GlStateManager.color(1.0f * cm, 0.2f * cm, 0.2f * cm, 1.0f);
							drawTexturedModalRect(0, 0, 64, 208, 16, 16);
						}else if(speaking) {
							GlStateManager.color(1.0f * cm, 1.0f * cm, 1.0f * cm, 1.0f);
							drawTexturedModalRect(0, 0, 64, 176, 16, 16);
						}else {
							GlStateManager.color(0.65f * cm, 0.65f * cm, 0.65f * cm, 1.0f);
							drawTexturedModalRect(0, 0, 64, 144, 16, 16);
						}
						
						GlStateManager.popMatrix();

						if(muted) {
							drawString(fontRendererObj, txt, -84, 117 + i * 12, attenuate(0xCC4444, cm));
						}else if(speaking) {
							drawString(fontRendererObj, txt, -84, 117 + i * 12, attenuate(0xCCCCCC, cm));
						}else {
							drawString(fontRendererObj, txt, -84, 117 + i * 12, attenuate(0x999999, cm));
						}
						
					}
				}else {
					txt = "(none)";
					drawString(fontRendererObj, txt, -fontRendererObj.getStringWidth(txt), 112, 0xAAAAAA);
				}
				
			}else if(status == EnumVoiceChannelStatus.CONNECTING) {
				float fadeTimer = MathHelper.sin((float)((EagRuntime.steadyTimeMillis() % 700l) * 0.0014d) * 3.14159f) * 0.35f + 0.3f;
				txt = I18n.format("voice.connecting");
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				drawString(fontRendererObj, txt, 1 - fontRendererObj.getStringWidth(txt), 5, (0xFFDD77 | ((int)(Math.pow(fadeTimer, 1.0d / 2.2d) * 255.0f) << 24)));
				GlStateManager.disableBlend();
			}else if(status == EnumVoiceChannelStatus.UNAVAILABLE) {
				txt = I18n.format("voice.unavailable");
				drawString(fontRendererObj, txt, 1 - fontRendererObj.getStringWidth(txt), 5, 0xFF3333);
			}else {
				flag = true;
			}
		}
		
		if(flag) {
			txt = I18n.format("voice.notConnected");
			drawString(fontRendererObj, txt, 1 - fontRendererObj.getStringWidth(txt), 5, 0xBB9999);
		}

		String OFFstring = I18n.format("voice.off");
		String RADIUSstring = I18n.format("voice.radius");
		String GLOBALstring = I18n.format("voice.global");

		int OFFwidth = fontRendererObj.getStringWidth(OFFstring);
		int RADIUSwidth = fontRendererObj.getStringWidth(RADIUSstring);
		int GLOBALwidth = fontRendererObj.getStringWidth(GLOBALstring);
		
		voiceButtonOFFposX = 0 - OFFwidth - 8 - RADIUSwidth - 8 - GLOBALwidth;
		voiceButtonOFFposY = 20;
		voiceButtonOFFposW = OFFwidth + 5;
		voiceButtonOFFposH = 15;

		voiceScreenButtonOFFposX = voiceButtonOFFposX * 3 / 4 + width - 6;
		voiceScreenButtonOFFposY = 15 + (voiceButtonOFFposY + xo) * 3 / 4;
		voiceScreenButtonOFFposW = voiceButtonOFFposW * 3 / 4;
		voiceScreenButtonOFFposH = voiceButtonOFFposH * 3 / 4;

		voiceButtonRADIUSposX = 0 - RADIUSwidth - 8 - GLOBALwidth;
		voiceButtonRADIUSposY = 20;
		voiceButtonRADIUSposW = RADIUSwidth + 5;
		voiceButtonRADIUSposH = 15;

		voiceScreenButtonRADIUSposX = voiceButtonRADIUSposX * 3 / 4 + width - 6;
		voiceScreenButtonRADIUSposY = 15 + (voiceButtonRADIUSposY + xo) * 3 / 4;
		voiceScreenButtonRADIUSposW = voiceButtonRADIUSposW * 3 / 4;
		voiceScreenButtonRADIUSposH = voiceButtonRADIUSposH * 3 / 4;

		voiceButtonGLOBALposX = 0 - GLOBALwidth;
		voiceButtonGLOBALposY = 20;
		voiceButtonGLOBALposW = GLOBALwidth + 5;
		voiceButtonGLOBALposH = 15;

		voiceScreenButtonGLOBALposX = voiceButtonGLOBALposX * 3 / 4 + width - 6;
		voiceScreenButtonGLOBALposY = 15 + (voiceButtonGLOBALposY + xo) * 3 / 4;
		voiceScreenButtonGLOBALposW = voiceButtonGLOBALposW * 3 / 4;
		voiceScreenButtonGLOBALposH = voiceButtonGLOBALposH * 3 / 4;
		
		if(channel == EnumVoiceChannelType.NONE) {
			drawOutline(voiceButtonOFFposX, voiceButtonOFFposY, voiceButtonOFFposW, voiceButtonOFFposH, 0xFFCCCCCC);
			drawRect(voiceButtonOFFposX + 1, voiceButtonOFFposY + 1, voiceButtonOFFposX + voiceButtonOFFposW - 2,
					voiceButtonOFFposY + voiceButtonOFFposH - 1, 0xFF222222);
		}else if(mx >= voiceScreenButtonOFFposX && my >= voiceScreenButtonOFFposY && mx < voiceScreenButtonOFFposX +
				voiceScreenButtonOFFposW && my < voiceScreenButtonOFFposY + voiceScreenButtonOFFposH) {
			drawOutline(voiceButtonOFFposX, voiceButtonOFFposY, voiceButtonOFFposW, voiceButtonOFFposH, 0xFF777777);
		}

		if(channel == EnumVoiceChannelType.PROXIMITY) {
			drawOutline(voiceButtonRADIUSposX, voiceButtonRADIUSposY, voiceButtonRADIUSposW, voiceButtonRADIUSposH, 0xFFCCCCCC);
			drawRect(voiceButtonRADIUSposX + 1, voiceButtonRADIUSposY + 1, voiceButtonRADIUSposX + voiceButtonRADIUSposW - 2,
					voiceButtonRADIUSposY + voiceButtonRADIUSposH - 1, 0xFF222222);
		}else if(mx >= voiceScreenButtonRADIUSposX && my >= voiceScreenButtonRADIUSposY && mx < voiceScreenButtonRADIUSposX +
				voiceScreenButtonRADIUSposW && my < voiceScreenButtonRADIUSposY + voiceScreenButtonRADIUSposH) {
			drawOutline(voiceButtonRADIUSposX, voiceButtonRADIUSposY, voiceButtonRADIUSposW, voiceButtonRADIUSposH, 0xFF777777);
		}

		if(channel == EnumVoiceChannelType.GLOBAL) {
			drawOutline(voiceButtonGLOBALposX, voiceButtonGLOBALposY, voiceButtonGLOBALposW, voiceButtonGLOBALposH, 0xFFCCCCCC);
			drawRect(voiceButtonGLOBALposX + 1, voiceButtonGLOBALposY + 1, voiceButtonGLOBALposX + voiceButtonGLOBALposW - 2,
					voiceButtonGLOBALposY + voiceButtonGLOBALposH - 1, 0xFF222222);
		}else if(mx >= voiceScreenButtonGLOBALposX && my >= voiceScreenButtonGLOBALposY && mx < voiceScreenButtonGLOBALposX +
				voiceScreenButtonGLOBALposW && my < voiceScreenButtonGLOBALposY + voiceScreenButtonGLOBALposH) {
			drawOutline(voiceButtonGLOBALposX, voiceButtonGLOBALposY, voiceButtonGLOBALposW, voiceButtonGLOBALposH, 0xFF777777);
		}

		int enabledColor = (status == EnumVoiceChannelStatus.CONNECTED || channel == EnumVoiceChannelType.NONE) ? 0x66DD66 : 0xDDCC66;
		int disabledColor = 0xDD4444;
		
		if(channel != EnumVoiceChannelType.NONE && status == EnumVoiceChannelStatus.UNAVAILABLE) {
			enabledColor = disabledColor;
		}
		
		drawString(fontRendererObj, OFFstring, 3 - OFFwidth - 8 - RADIUSwidth - 8 - GLOBALwidth, 24, channel == EnumVoiceChannelType.NONE ? enabledColor : disabledColor);
		drawString(fontRendererObj, RADIUSstring, 3 - RADIUSwidth - 8 - GLOBALwidth, 24, channel == EnumVoiceChannelType.PROXIMITY ? enabledColor : disabledColor);
		drawString(fontRendererObj, GLOBALstring, 3 - GLOBALwidth, 24, channel == EnumVoiceChannelType.GLOBAL ? enabledColor : disabledColor);
		
		GlStateManager.popMatrix();
		
		if(showingCompatWarning) {
			
			drawNotice(I18n.format("voice.unsupportedWarning1"), false, I18n.format("voice.unsupportedWarning2"), I18n.format("voice.unsupportedWarning3"),
					"", I18n.format("voice.unsupportedWarning4"), I18n.format("voice.unsupportedWarning5"), I18n.format("voice.unsupportedWarning6"),
					I18n.format("voice.unsupportedWarning7"), "", I18n.format("voice.unsupportedWarning8"), I18n.format("voice.unsupportedWarning9"));
			
			noticeContinueButton.visible = true;
			noticeCancelButton.visible = false;
		}else if(showingTrackingWarning) {
			
			drawNotice(I18n.format("voice.ipGrabWarning1"), true, I18n.format("voice.ipGrabWarning2"), I18n.format("voice.ipGrabWarning3"),
					I18n.format("voice.ipGrabWarning4"), "", I18n.format("voice.ipGrabWarning5"), I18n.format("voice.ipGrabWarning6"),
					I18n.format("voice.ipGrabWarning7"));
			
			noticeContinueButton.visible = true;
			noticeCancelButton.visible = true;
		}else {
			noticeContinueButton.visible = false;
			noticeCancelButton.visible = false;
		}
		
		drawButtons(mx, my, partialTicks);

		if(showingCompatWarning || showingTrackingWarning) {
			throw new AbortedException();
		}
	}
	
	private void drawNotice(String title, boolean showCancel, String... lines) {
		
		int widthAccum = 0;
		
		for(int i = 0; i < lines.length; ++i) {
			int w = fontRendererObj.getStringWidth(lines[i]);
			if(widthAccum < w) {
				widthAccum = w;
			}
		}
		
		int margin = 15;
		
		int x = (width - widthAccum) / 2;
		int y = (height - lines.length * 10 - 60 - margin) / 2;

		drawRect(x - margin - 1, y - margin - 1, x + widthAccum + margin + 1,
				y + lines.length * 10 + 49 + margin, 0xFFCCCCCC);
		drawRect(x - margin, y - margin, x + widthAccum + margin,
				y + lines.length * 10 + 48 + margin, 0xFF111111);
		
		drawCenteredString(fontRendererObj, EnumChatFormatting.BOLD + title, width / 2, y, 0xFF7766);
		
		for(int i = 0; i < lines.length; ++i) {
			drawString(fontRendererObj, lines[i], x, y + i * 10 + 18, 0xDDAAAA);
		}
		
		if(!showCancel) {
			noticeContinueButton.width = 150;
			noticeContinueButton.xPosition = (width - 150) / 2;
			noticeContinueButton.yPosition = y + lines.length * 10 + 29;
		}else {
			noticeContinueButton.width = widthAccum / 2 - 10;
			noticeContinueButton.xPosition = (width - widthAccum) / 2 + widthAccum / 2 + 3;
			noticeContinueButton.yPosition = y + lines.length * 10 + 28;
			noticeCancelButton.width = widthAccum / 2 - 10;
			noticeCancelButton.xPosition = (width - widthAccum) / 2 + 4;
			noticeCancelButton.yPosition = y + lines.length * 10 + 28;
		}
		
	}
	
	public static int attenuate(int cin, float f) {
		return attenuate(cin, f, f, f, 1.0f);
	}
	
	public static int attenuate(int cin, float r, float g, float b, float a) {
		float var10 = (float) (cin >>> 24 & 255) / 255.0F;
		float var6 = (float) (cin >>> 16 & 255) / 255.0F;
		float var7 = (float) (cin >>> 8 & 255) / 255.0F;
		float var8 = (float) (cin & 255) / 255.0F;
		var10 *= a;
		var6 *= r;
		var7 *= g;
		var8 *= b;
		if(var10 > 1.0f) {
			var10 = 1.0f;
		}
		if(var6 > 1.0f) {
			var6 = 1.0f;
		}
		if(var7 > 1.0f) {
			var7 = 1.0f;
		}
		if(var8 > 1.0f) {
			var8 = 1.0f;
		}
		return (((int)(var10 * 255.0f) << 24) | ((int)(var6 * 255.0f) << 16) | ((int)(var7 * 255.0f) << 8) | (int)(var8 * 255.0f));
	}
	
	private void drawOutline(int x, int y, int w, int h, int color) {
		drawRect(x, y, x + w, y + 1, color);
		drawRect(x + w - 1, y + 1, x + w, y + h - 1, color);
		drawRect(x, y + h - 1, x + w, y + h, color);
		drawRect(x, y + 1, x + 1, y + h - 1, color);
	}
	
	public void mouseReleased(int par1, int par2, int par3) {
		if(par3 != 0 && par3 != 12345) return;
		boolean touchMode = PointerInputAbstraction.isTouchMode();
		if(!touchMode || par3 == 0) {
			applyRadiusButton.mouseReleased(par1, par2);
			applyVolumeButton.mouseReleased(par1, par2);
			noticeContinueButton.mouseReleased(par1, par2);
			noticeCancelButton.mouseReleased(par1, par2);
		}
		if(showSliderBlocks || showSliderVolume) {
			if(showSliderBlocks) {
				if(!touchMode || par3 == 12345) {
					sliderBlocks.mouseReleased(par1, par2);
				}
			}else if(showSliderVolume) {
				if(!touchMode || par3 == 12345) {
					sliderListenVolume.mouseReleased(par1, par2);
					sliderSpeakVolume.mouseReleased(par1, par2);
				}
			}
			throw new AbortedException();
		}
	}
	
	public void keyTyped(char par1, int par2) {
		if(showSliderBlocks || showSliderVolume || showPTTKeyConfig) {
			if(showPTTKeyConfig) {
				if(par2 == 1) {
					showPTTKeyConfig = false;
				}else {
					mc.gameSettings.voicePTTKey = par2;
					showNewPTTKey = 10;
				}
			}
			throw new AbortedException();
		}
	}
	
	public void mouseClicked(int mx, int my, int button) {
		if(button != 0 && button != 12345) return;
		boolean touchMode = PointerInputAbstraction.isTouchMode();
		if(showSliderBlocks || showSliderVolume || showPTTKeyConfig || showingCompatWarning || showingTrackingWarning) {
			if(showSliderBlocks) {
				if(!touchMode || button == 12345) {
					sliderBlocks.mousePressed(mc, mx, my);
				}
			}else if(showSliderVolume) {
				if(!touchMode || button == 12345) {
					sliderListenVolume.mousePressed(mc, mx, my);
					sliderSpeakVolume.mousePressed(mc, mx, my);
				}
			}
			if((!touchMode || button == 0) && applyRadiusButton.mousePressed(mc, mx, my)) actionPerformed(applyRadiusButton);
			if((!touchMode || button == 0) && applyVolumeButton.mousePressed(mc, mx, my)) actionPerformed(applyVolumeButton);
			if((!touchMode || button == 0) && noticeContinueButton.mousePressed(mc, mx, my)) actionPerformed(noticeContinueButton);
			if((!touchMode || button == 0) && noticeCancelButton.mousePressed(mc, mx, my)) actionPerformed(noticeCancelButton);
			throw new AbortedException();
		}
		
		EnumVoiceChannelStatus status = VoiceClientController.getVoiceStatus();
		EnumVoiceChannelType channel = VoiceClientController.getVoiceChannel();
		
		if(button == 0) {
			if(VoiceClientController.isSupported()) {
				if(mx >= voiceScreenButtonOFFposX && my >= voiceScreenButtonOFFposY && mx < voiceScreenButtonOFFposX +
						voiceScreenButtonOFFposW && my < voiceScreenButtonOFFposY + voiceScreenButtonOFFposH) {
					VoiceClientController.setVoiceChannel(EnumVoiceChannelType.NONE);
					this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
				}else if(mx >= voiceScreenButtonRADIUSposX && my >= voiceScreenButtonRADIUSposY && mx < voiceScreenButtonRADIUSposX +
						voiceScreenButtonRADIUSposW && my < voiceScreenButtonRADIUSposY + voiceScreenButtonRADIUSposH) {
					
					if(showCompatWarning) {
						continueChannel = EnumVoiceChannelType.PROXIMITY;
						showingCompatWarning = true;
					}else if(showTrackingWarning) {
						continueChannel = EnumVoiceChannelType.PROXIMITY;
						showingTrackingWarning = true;
					}else {
						VoiceClientController.setVoiceChannel(EnumVoiceChannelType.PROXIMITY);
					}
					
					this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
					
				}else if(mx >= voiceScreenButtonGLOBALposX && my >= voiceScreenButtonGLOBALposY && mx < voiceScreenButtonGLOBALposX +
						voiceScreenButtonGLOBALposW && my < voiceScreenButtonGLOBALposY + voiceScreenButtonGLOBALposH) {
					
					if(showCompatWarning) {
						continueChannel = EnumVoiceChannelType.GLOBAL;
						showingCompatWarning = true;
					}else if(showTrackingWarning) {
						continueChannel = EnumVoiceChannelType.GLOBAL;
						showingTrackingWarning = true;
					}else {
						VoiceClientController.setVoiceChannel(EnumVoiceChannelType.GLOBAL);
					}
					
					this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
					
					this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
				}else if(channel == EnumVoiceChannelType.PROXIMITY && status == EnumVoiceChannelStatus.CONNECTED && mx >= voiceScreenButtonChangeRadiusposX &&
						my >= voiceScreenButtonChangeRadiusposY && mx < voiceScreenButtonChangeRadiusposX + voiceScreenButtonChangeRadiusposW &&
						my < voiceScreenButtonChangeRadiusposY + voiceScreenButtonChangeRadiusposH) {
					showSliderBlocks = true;
					sliderBlocks.sliderValue = (VoiceClientController.getVoiceProximity() - 5) / 17.0f;
					this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
				}else if(status == EnumVoiceChannelStatus.CONNECTED && channel != EnumVoiceChannelType.NONE && mx >= voiceScreenVolumeIndicatorX &&
						my >= voiceScreenVolumeIndicatorY && mx < voiceScreenVolumeIndicatorX + voiceScreenVolumeIndicatorW &&
						my < voiceScreenVolumeIndicatorY + voiceScreenVolumeIndicatorH) {
					showSliderVolume = true;
					sliderListenVolume.sliderValue = VoiceClientController.getVoiceListenVolume();
					sliderSpeakVolume.sliderValue = VoiceClientController.getVoiceSpeakVolume();
					this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
				}else if(status == EnumVoiceChannelStatus.CONNECTED && channel != EnumVoiceChannelType.NONE && mx >= voiceScreenVolumeIndicatorX - 1 &&
						my >= voiceScreenVolumeIndicatorY + voiceScreenVolumeIndicatorH + 2 && mx < voiceScreenVolumeIndicatorX + voiceScreenVolumeIndicatorW + 2 &&
						my < voiceScreenVolumeIndicatorY + voiceScreenVolumeIndicatorH + 12) {
					showPTTKeyConfig = true;
					this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
				}else if(status == EnumVoiceChannelStatus.CONNECTED) {
					List<EaglercraftUUID> playersToRender = VoiceClientController.getVoiceRecent();
					if(playersToRender.size() > 0) {
						Set<EaglercraftUUID> playersMuted = VoiceClientController.getVoiceMuted();
						for(int i = 0, l = playersToRender.size(); i < l; ++i) {
							EaglercraftUUID uuid = playersToRender.get(i);
							String txt = VoiceClientController.getVoiceUsername(uuid);
							boolean muted = playersMuted.contains(uuid);
							int mhy = voiceScreenVolumeIndicatorY + voiceScreenVolumeIndicatorH + 33 + i * 9;
							if(mx >= voiceScreenVolumeIndicatorX - 3 && my >= mhy && mx < voiceScreenVolumeIndicatorX + voiceScreenVolumeIndicatorW + 2 && my < mhy + 9) {
								VoiceClientController.setVoiceMuted(uuid, !muted);
								this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
								break;
							}
						}
					}
				}
			}
		}
		
	}
	
	private void actionPerformed(GuiButton btn) {
		if(btn.id == 2) {
			showSliderBlocks = false;
			VoiceClientController.setVoiceProximity(mc.gameSettings.voiceListenRadius = (int)((sliderBlocks.sliderValue * 17.0f) + 5.0f));
			mc.gameSettings.saveOptions();
		}else if(btn.id == 3) {
			showSliderVolume = false;
			VoiceClientController.setVoiceListenVolume(mc.gameSettings.voiceListenVolume = sliderListenVolume.sliderValue);
			VoiceClientController.setVoiceSpeakVolume(mc.gameSettings.voiceSpeakVolume = sliderSpeakVolume.sliderValue);
			mc.gameSettings.saveOptions();
		}else if(btn.id == 4) {
			showPTTKeyConfig = false;
			mc.gameSettings.saveOptions();
		}else if(btn.id == 5) {
			if(showingCompatWarning) {
				showingCompatWarning = false;
				showCompatWarning = false;
				if(showTrackingWarning) {
					showingTrackingWarning = true;
				}else {
					VoiceClientController.setVoiceChannel(continueChannel);
				}
			}else if(showingTrackingWarning) {
				showingTrackingWarning = false;
				showTrackingWarning = false;
				VoiceClientController.setVoiceChannel(continueChannel);
			}
		}else if(btn.id == 6) {
			if(showingTrackingWarning) {
				showingTrackingWarning = false;
				VoiceClientController.setVoiceChannel(EnumVoiceChannelType.NONE);
			}
		}
	}
	
	public void updateScreen() {
		if(showNewPTTKey > 0) {
			--showNewPTTKey;
			if(showNewPTTKey == 0) {
				showPTTKeyConfig = false;
				mc.gameSettings.saveOptions();
			}
		}
	}

	public boolean isBlockingInput() {
		return showSliderBlocks || showSliderVolume || showPTTKeyConfig || showingCompatWarning || showingTrackingWarning;
	}

}
