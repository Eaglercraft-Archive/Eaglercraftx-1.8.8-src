package net.lax1dude.eaglercraft.v1_8.recording;

import net.lax1dude.eaglercraft.v1_8.HString;
import net.lax1dude.eaglercraft.v1_8.internal.ScreenRecordParameters;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenGenericErrorMessage;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiSlider2;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

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
public class GuiScreenRecordingSettings extends GuiScreen {

	private static final Logger logger = LogManager.getLogger("GuiScreenRecordingSettings");

	protected final GuiScreen parent;

	protected GuiButton recordButton;
	protected GuiButton codecButton;
	protected GuiSlider2 videoResolutionSlider;
	protected GuiSlider2 videoFrameRateSlider;
	protected GuiSlider2 audioBitrateSlider;
	protected GuiSlider2 videoBitrateSlider;
	protected GuiSlider2 microphoneVolumeSlider;
	protected GuiSlider2 gameVolumeSlider;
	protected boolean dirty = false;

	public GuiScreenRecordingSettings(GuiScreen parent) {
		this.parent = parent;
	}

	public void initGui() {
		buttonList.clear();
		buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done")));
		buttonList.add(codecButton = new GuiButton(1, this.width / 2 + 65, this.height / 6 - 2, 75, 20, I18n.format("options.screenRecording.codecButton")));
		boolean isRecording = ScreenRecordingController.isRecording();
		buttonList.add(recordButton = new GuiButton(2, this.width / 2 + 15, this.height / 6 + 28, 125, 20,
				I18n.format(isRecording ? "options.screenRecording.stop" : "options.screenRecording.start")));
		buttonList.add(videoResolutionSlider = new GuiSlider2(3, this.width / 2 - 155, this.height / 6 + 64, 150, 20, (mc.gameSettings.screenRecordResolution - 1) / 3.999f, 1.0f) {
			@Override
			protected String updateDisplayString() {
				int i = (int)(sliderValue * 3.999f);
				return I18n.format("options.screenRecording.videoResolution") + ": x" + HString.format("%.2f", 1.0f / (int)Math.pow(2.0, i));
			}
			@Override
			protected void onChange() {
				mc.gameSettings.screenRecordResolution = 1 + (int)(sliderValue * 3.999f);
				dirty = true;
			}
		});
		buttonList.add(videoFrameRateSlider = new GuiSlider2(4, this.width / 2 + 5, this.height / 6 + 64, 150, 20, (Math.max(mc.gameSettings.screenRecordFPS, 9) - 9) / 51.999f, 1.0f) {
			@Override
			protected String updateDisplayString() {
				int i = (int)(sliderValue * 51.999f);
				return I18n.format("options.screenRecording.videoFPS") + ": " + (i <= 0 ? I18n.format("options.screenRecording.onVSync") : 9 + i);
			}
			@Override
			protected void onChange() {
				int i = (int)(sliderValue * 51.999f);
				mc.gameSettings.screenRecordFPS = i <= 0 ? -1 : 9 + i;
				dirty = true;
			}
		});
		buttonList.add(videoBitrateSlider = new GuiSlider2(5, this.width / 2 - 155, this.height / 6 + 98, 150, 20, MathHelper.sqrt_float(MathHelper.clamp_float((mc.gameSettings.screenRecordVideoBitrate - 250) / 19750.999f, 0.0f, 1.0f)), 1.0f) {
			@Override
			protected String updateDisplayString() {
				return I18n.format("options.screenRecording.videoBitrate") + ": " + (250 + (int)(sliderValue * sliderValue * 19750.999f)) + "kbps";
			}
			@Override
			protected void onChange() {
				mc.gameSettings.screenRecordVideoBitrate = 250 + (int)(sliderValue * sliderValue * 19750.999f);
				dirty = true;
			}
		});
		buttonList.add(audioBitrateSlider = new GuiSlider2(6, this.width / 2 + 5, this.height / 6 + 98, 150, 20, MathHelper.sqrt_float(MathHelper.clamp_float((mc.gameSettings.screenRecordAudioBitrate - 24) / 232.999f, 0.0f, 1.0f)), 1.0f) {
			@Override
			protected String updateDisplayString() {
				return I18n.format("options.screenRecording.audioBitrate") + ": " + (24 + (int)(sliderValue * sliderValue * 232.999f)) + "kbps";
			}
			@Override
			protected void onChange() {
				mc.gameSettings.screenRecordAudioBitrate = 24 + (int)(sliderValue * sliderValue * 232.999f);
				dirty = true;
			}
		});
		buttonList.add(gameVolumeSlider = new GuiSlider2(7, this.width / 2 - 155, this.height / 6 + 130, 150, 20, mc.gameSettings.screenRecordGameVolume, 1.0f) {
			@Override
			protected String updateDisplayString() {
				return I18n.format("options.screenRecording.gameVolume") + ": " + (int)(sliderValue * 100.999f) + "%";
			}
			@Override
			protected void onChange() {
				mc.gameSettings.screenRecordGameVolume = sliderValue;
				ScreenRecordingController.setGameVolume(sliderValue);
				dirty = true;
			}
		});
		buttonList.add(microphoneVolumeSlider = new GuiSlider2(8, this.width / 2 + 5, this.height / 6 + 130, 150, 20, mc.gameSettings.screenRecordMicVolume, 1.0f) {
			@Override
			protected String updateDisplayString() {
				return I18n.format("options.screenRecording.microphoneVolume") + ": " + (int)(sliderValue * 100.999f) + "%";
			}
			@Override
			protected void onChange() {
				mc.gameSettings.screenRecordMicVolume = sliderValue;
				ScreenRecordingController.setMicrophoneVolume(sliderValue);
				dirty = true;
			}
		});
		codecButton.enabled = !isRecording;
		videoResolutionSlider.enabled = !isRecording;
		videoFrameRateSlider.enabled = !isRecording;
		audioBitrateSlider.enabled = !isRecording;
		videoBitrateSlider.enabled = !isRecording;
		microphoneVolumeSlider.enabled = !ScreenRecordingController.isMicVolumeLocked();
	}

	protected void actionPerformed(GuiButton parGuiButton) {
		if(parGuiButton.id == 0) {
			if(dirty) {
				mc.gameSettings.saveOptions();
				dirty = false;
			}
			mc.displayGuiScreen(parent);
		}else if(parGuiButton.id == 1) {
			mc.displayGuiScreen(new GuiScreenSelectCodec(this, mc.gameSettings.screenRecordCodec));
		}else if(parGuiButton.id == 2) {
			if(!ScreenRecordingController.isRecording()) {
				try {
					ScreenRecordingController.startRecording(new ScreenRecordParameters(mc.gameSettings.screenRecordCodec,
							mc.gameSettings.screenRecordResolution, mc.gameSettings.screenRecordVideoBitrate,
							mc.gameSettings.screenRecordAudioBitrate, mc.gameSettings.screenRecordFPS));
				}catch(Throwable t) {
					logger.error("Failed to begin screen recording!");
					logger.error(t);
					mc.displayGuiScreen(new GuiScreenGenericErrorMessage("options.screenRecording.failed", t.toString(), parent));
				}
			}else {
				ScreenRecordingController.endRecording();
			}
		}
	}

	public void drawScreen(int i, int j, float var3) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, I18n.format("options.screenRecording.title"), this.width / 2, 15, 16777215);
		if(mc.gameSettings.screenRecordCodec == null) {
			mc.gameSettings.screenRecordCodec = ScreenRecordingController.getDefaultCodec();
		}
		
		String codecString = mc.gameSettings.screenRecordCodec.name;
		int codecStringWidth = fontRendererObj.getStringWidth(codecString);
		drawString(fontRendererObj, codecString, this.width / 2 + 60 - codecStringWidth, this.height / 6 + 4, 0xFFFFFF);
		
		boolean isRecording = ScreenRecordingController.isRecording();
		codecButton.enabled = !isRecording;
		videoResolutionSlider.enabled = !isRecording;
		videoFrameRateSlider.enabled = !isRecording;
		audioBitrateSlider.enabled = !isRecording;
		videoBitrateSlider.enabled = !isRecording;
		microphoneVolumeSlider.enabled = !ScreenRecordingController.isMicVolumeLocked();
		recordButton.displayString = I18n.format(isRecording ? "options.screenRecording.stop" : "options.screenRecording.start");
		String statusString = I18n.format("options.screenRecording.status",
				(isRecording ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + I18n.format(isRecording ? "options.screenRecording.status.1" : "options.screenRecording.status.0"));
		int statusStringWidth = fontRendererObj.getStringWidth(statusString);
		drawString(fontRendererObj, statusString, this.width / 2 + 10 - statusStringWidth, this.height / 6 + 34, 0xFFFFFF);
		
		super.drawScreen(i, j, var3);
	}

	protected void handleCodecCallback(EnumScreenRecordingCodec codec) {
		EnumScreenRecordingCodec oldCodec = mc.gameSettings.screenRecordCodec;
		if(ScreenRecordingController.codecs.contains(codec)) {
			mc.gameSettings.screenRecordCodec = codec;
		}else {
			mc.gameSettings.screenRecordCodec = ScreenRecordingController.getDefaultCodec();
		}
		if(oldCodec != mc.gameSettings.screenRecordCodec) {
			dirty = true;
		}
	}

}
