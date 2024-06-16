
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  3 : 4  @  3 : 4

~ import java.nio.charset.StandardCharsets;

> INSERT  1 : 2  @  1

+ import java.util.Arrays;

> CHANGE  2 : 29  @  2 : 4

~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.EagUtils;
~ import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ 
~ import com.google.common.base.Charsets;
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.crypto.SHA1Digest;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
~ import net.lax1dude.eaglercraft.v1_8.profile.GuiScreenEditProfile;
~ import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenDemoPlayWorldSelection;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenIntegratedServerBusy;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenIntegratedServerStartup;
~ import net.lax1dude.eaglercraft.v1_8.update.GuiUpdateCheckerOverlay;
~ import net.lax1dude.eaglercraft.v1_8.update.GuiUpdateVersionSlot;
~ import net.lax1dude.eaglercraft.v1_8.update.UpdateCertificate;
~ import net.lax1dude.eaglercraft.v1_8.update.UpdateService;

> CHANGE  1 : 2  @  1 : 13

~ import net.minecraft.client.audio.PositionedSoundRecord;

> DELETE  1  @  1 : 2

> DELETE  3  @  3 : 5

> DELETE  2  @  2 : 3

> DELETE  1  @  1 : 8

> DELETE  2  @  2 : 3

> CHANGE  1 : 2  @  1 : 2

~ 	private static final EaglercraftRandom RANDOM = new EaglercraftRandom();

> INSERT  1 : 5  @  1

+ 	private boolean isDefault;
+ 	private static final int lendef = 5987;
+ 	private static final byte[] sha1def = new byte[] { -107, 77, 108, 49, 11, -100, -8, -119, -1, -100, -85, -55, 18,
+ 			-69, -107, 113, -93, -101, -79, 32 };

> CHANGE  3 : 4  @  3 : 4

~ 	private static DynamicTexture viewportTexture = null;

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 3

> INSERT  3 : 6  @  3

+ 	private static final ResourceLocation minecraftTitleBlurFlag = new ResourceLocation(
+ 			"textures/gui/title/background/enable_blur.txt");
+ 	private static final ResourceLocation eaglerGuiTextures = new ResourceLocation("eagler:gui/eagler_gui.png");

> DELETE  7  @  7 : 9

> CHANGE  6 : 11  @  6 : 8

~ 	private static ResourceLocation backgroundTexture = null;
~ 	private GuiUpdateCheckerOverlay updateCheckerOverlay;
~ 	private GuiButton downloadOfflineButton;
~ 	private boolean enableBlur = true;
~ 	private boolean shouldReload = false;

> INSERT  1 : 3  @  1

+ 	private static GuiMainMenu instance = null;
+ 

> CHANGE  1 : 2  @  1 : 2

~ 		instance = this;

> INSERT  1 : 2  @  1

+ 		updateCheckerOverlay = new GuiUpdateCheckerOverlay(false, this);

> CHANGE  38 : 65  @  38 : 43

~ 
~ 		reloadResourceFlags();
~ 	}
~ 
~ 	private void reloadResourceFlags() {
~ 		if (Minecraft.getMinecraft().isDemo()) {
~ 			this.isDefault = false;
~ 		} else {
~ 			if (!EagRuntime.getConfiguration().isEnableMinceraft()) {
~ 				this.isDefault = false;
~ 			} else {
~ 				try {
~ 					byte[] bytes = EaglerInputStream.inputStreamToBytesQuiet(Minecraft.getMinecraft()
~ 							.getResourceManager().getResource(minecraftTitleTextures).getInputStream());
~ 					if (bytes != null && bytes.length == lendef) {
~ 						SHA1Digest sha1 = new SHA1Digest();
~ 						byte[] sha1out = new byte[20];
~ 						sha1.update(bytes, 0, bytes.length);
~ 						sha1.doFinal(sha1out, 0);
~ 						this.isDefault = Arrays.equals(sha1out, sha1def);
~ 					} else {
~ 						this.isDefault = false;
~ 					}
~ 				} catch (IOException e) {
~ 					this.isDefault = false;
~ 				}
~ 			}

> INSERT  2 : 21  @  2

+ 		this.enableBlur = true;
+ 
+ 		try {
+ 			byte[] bytes = EaglerInputStream.inputStreamToBytesQuiet(
+ 					Minecraft.getMinecraft().getResourceManager().getResource(minecraftTitleBlurFlag).getInputStream());
+ 			if (bytes != null) {
+ 				String[] blurCfg = EagUtils.linesArray(new String(bytes, StandardCharsets.UTF_8));
+ 				for (int i = 0; i < blurCfg.length; ++i) {
+ 					String s = blurCfg[i];
+ 					if (s.startsWith("enable_blur=")) {
+ 						s = s.substring(12).trim();
+ 						this.enableBlur = s.equals("1") || s.equals("true");
+ 						break;
+ 					}
+ 				}
+ 			}
+ 		} catch (IOException e) {
+ 			;
+ 		}

> INSERT  2 : 8  @  2

+ 	public static void doResourceReloadHack() {
+ 		if (instance != null) {
+ 			instance.shouldReload = true;
+ 		}
+ 	}
+ 

> INSERT  2 : 9  @  2

+ 		if (downloadOfflineButton != null) {
+ 			downloadOfflineButton.enabled = !UpdateService.shouldDisableDownloadButton();
+ 		}
+ 		if (shouldReload) {
+ 			reloadResourceFlags();
+ 			shouldReload = false;
+ 		}

> CHANGE  6 : 7  @  6 : 7

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  3 : 9  @  3 : 7

~ 		if (viewportTexture == null) {
~ 			viewportTexture = new DynamicTexture(256, 256);
~ 			backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", viewportTexture);
~ 		}
~ 		this.updateCheckerOverlay.setResolution(mc, width, height);
~ 		Calendar calendar = EagRuntime.getLocaleCalendar();

> DELETE  9  @  9 : 10

> INSERT  1 : 8  @  1

+ 
+ 		boolean isFork = !EaglercraftVersion.projectOriginAuthor.equalsIgnoreCase(EaglercraftVersion.projectForkVendor);
+ 
+ 		if (isFork && EaglercraftVersion.mainMenuStringF != null && EaglercraftVersion.mainMenuStringF.length() > 0) {
+ 			i += 11;
+ 		}
+ 

> CHANGE  8 : 11  @  8 : 10

~ 		this.buttonList.add(new GuiButton(4, this.width / 2 + 2, i + 72 + 12, 98, 20,
~ 				I18n.format("menu.editProfile", new Object[0])));
~ 

> CHANGE  1 : 6  @  1 : 2

~ 
~ 		if (isFork) {
~ 			this.openGLWarning1 = EaglercraftVersion.mainMenuStringE;
~ 			this.openGLWarning2 = EaglercraftVersion.mainMenuStringF;
~ 			boolean line2 = this.openGLWarning2 != null && this.openGLWarning2.length() > 0;

> CHANGE  4 : 5  @  4 : 5

~ 			this.field_92021_u = ((GuiButton) this.buttonList.get(0)).yPosition - (line2 ? 32 : 21);

> CHANGE  1 : 2  @  1 : 2

~ 			this.field_92019_w = this.field_92021_u + (line2 ? 24 : 11);

> CHANGE  10 : 23  @  10 : 12

~ 		if (EaglercraftVersion.mainMenuEnableGithubButton) {
~ 			this.buttonList.add(
~ 					new GuiButton(14, this.width / 2 - 100, parInt1 + parInt2 * 2, I18n.format("menu.forkOnGitlab")));
~ 		} else {
~ 			if (EagRuntime.getConfiguration().isEnableDownloadOfflineButton()
~ 					&& (EagRuntime.getConfiguration().getDownloadOfflineButtonLink() != null
~ 							|| (!EagRuntime.isOfflineDownloadURL() && UpdateService.supported()
~ 									&& UpdateService.getClientSignatureData() != null))) {
~ 				this.buttonList.add(downloadOfflineButton = new GuiButton(15, this.width / 2 - 100,
~ 						parInt1 + parInt2 * 2, I18n.format("update.downloadOffline")));
~ 				downloadOfflineButton.enabled = !UpdateService.shouldDisableDownloadButton();
~ 			}
~ 		}

> CHANGE  7 : 8  @  7 : 13

~ 		this.buttonResetDemo.enabled = this.mc.gameSettings.hasCreatedDemoWorld;

> CHANGE  2 : 3  @  2 : 3

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  9 : 10  @  9 : 10

~ 			this.mc.displayGuiScreen(new GuiScreenIntegratedServerStartup(this));

> CHANGE  6 : 8  @  6 : 8

~ 		if (parGuiButton.id == 4) {
~ 			this.mc.displayGuiScreen(new GuiScreenEditProfile(this));

> CHANGE  2 : 4  @  2 : 4

~ 		if (parGuiButton.id == 14) {
~ 			EagRuntime.openLink(EaglercraftVersion.projectForkURL);

> CHANGE  3 : 4  @  3 : 4

~ 			this.mc.displayGuiScreen(new GuiScreenDemoPlayWorldSelection(this));

> CHANGE  3 : 5  @  3 : 9

~ 			GuiYesNo guiyesno = GuiSelectWorld.func_152129_a(this, "Demo World", 12);
~ 			this.mc.displayGuiScreen(guiyesno);

> INSERT  2 : 12  @  2

+ 		if (parGuiButton.id == 15) {
+ 			if (EagRuntime.getConfiguration().isEnableDownloadOfflineButton()) {
+ 				String link = EagRuntime.getConfiguration().getDownloadOfflineButtonLink();
+ 				if (link != null) {
+ 					EagRuntime.openLink(link);
+ 				} else {
+ 					UpdateService.quine();
+ 				}
+ 			}
+ 		}

> DELETE  2  @  2 : 7

> INSERT  2 : 4  @  2

+ 			this.mc.gameSettings.hasCreatedDemoWorld = false;
+ 			this.mc.gameSettings.saveOptions();

> CHANGE  1 : 5  @  1 : 3

~ 			isaveformat.deleteWorldDirectory("Demo World");
~ 			this.mc.displayGuiScreen(new GuiScreenIntegratedServerBusy(this, "singleplayer.busy.deleting",
~ 					"singleplayer.failed.deleting", SingleplayerServerController::isReady));
~ 		} else {

> DELETE  1  @  1 : 14

> DELETE  1  @  1 : 2

> CHANGE  8 : 13  @  8 : 9

~ 		if (enableBlur) {
~ 			GlStateManager.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
~ 		} else {
~ 			GlStateManager.gluPerspective(85.0F, (float) width / (float) height, 0.05F, 10.0F);
~ 		}

> CHANGE  5 : 8  @  5 : 6

~ 		if (enableBlur) {
~ 			GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
~ 		}

> CHANGE  5 : 6  @  5 : 6

~ 		byte b0 = enableBlur ? (byte) 8 : (byte) 1;

> CHANGE  61 : 65  @  61 : 65

~ 		this.mc.getTextureManager().bindTexture(backgroundTexture);
~ 		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
~ 		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
~ 		EaglercraftGPU.glCopyTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);

> DELETE  30  @  30 : 31

> DELETE  9  @  9 : 10

> CHANGE  22 : 27  @  22 : 23

~ 		if (enableBlur) {
~ 			this.renderSkybox(i, j, f);
~ 		} else {
~ 			this.drawPanorama(i, j, f);
~ 		}

> DELETE  1  @  1 : 3

> CHANGE  7 : 12  @  7 : 8

~ 		boolean minc = (double) this.updateCounter < 1.0E-4D;
~ 		if (this.isDefault) {
~ 			minc = !minc;
~ 		}
~ 		if (minc) {

> CHANGE  4 : 5  @  4 : 5

~ 			this.drawTexturedModalRect(k + 154, b0 + 0, 0, 45, 155, 44);

> INSERT  5 : 18  @  5

+ 		boolean isForkLabel = ((this.openGLWarning1 != null && this.openGLWarning1.length() > 0)
+ 				|| (this.openGLWarning2 != null && this.openGLWarning2.length() > 0));
+ 
+ 		if (isForkLabel) {
+ 			drawRect(this.field_92022_t - 3, this.field_92021_u - 3, this.field_92020_v + 3, this.field_92019_w,
+ 					1428160512);
+ 			if (this.openGLWarning1 != null)
+ 				this.drawString(this.fontRendererObj, this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
+ 			if (this.openGLWarning2 != null)
+ 				this.drawString(this.fontRendererObj, this.openGLWarning2, (this.width - this.field_92024_r) / 2,
+ 						this.field_92021_u + 12, -1);
+ 		}
+ 

> CHANGE  2 : 3  @  2 : 3

~ 		GlStateManager.rotate(isForkLabel ? -12.0F : -20.0F, 0.0F, 0.0F, 1.0F);

> INSERT  3 : 6  @  3

+ 		if (isForkLabel) {
+ 			f1 *= 0.8f;
+ 		}

> CHANGE  3 : 5  @  3 : 4

~ 
~ 		String s = EaglercraftVersion.mainMenuStringA;

> CHANGE  1 : 2  @  1 : 2

~ 			s += " Demo";

> CHANGE  1 : 3  @  1 : 2

~ 		this.drawString(this.fontRendererObj, s, 2, this.height - 20, -1);
~ 		s = EaglercraftVersion.mainMenuStringB;

> CHANGE  1 : 3  @  1 : 2

~ 
~ 		String s1 = EaglercraftVersion.mainMenuStringC;

> INSERT  1 : 7  @  1

+ 				this.height - 20, -1);
+ 		s1 = EaglercraftVersion.mainMenuStringD;
+ 		if (this.mc.isDemo()) {
+ 			s1 = "Copyright Mojang AB. Do not distribute!";
+ 		}
+ 		this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2,

> CHANGE  1 : 50  @  1 : 7

~ 
~ 		if (!this.mc.isDemo()) {
~ 			GlStateManager.pushMatrix();
~ 			GlStateManager.scale(0.75f, 0.75f, 0.75f);
~ 			int www = 0;
~ 			int hhh = 0;
~ 			s1 = EaglercraftVersion.mainMenuStringG;
~ 			if (s1 != null) {
~ 				www = this.fontRendererObj.getStringWidth(s1);
~ 				hhh += 10;
~ 			}
~ 			s1 = EaglercraftVersion.mainMenuStringH;
~ 			if (s1 != null) {
~ 				www = Math.max(www, this.fontRendererObj.getStringWidth(s1));
~ 				hhh += 10;
~ 			}
~ 			if (www > 0) {
~ 				drawRect(0, 0, www + 6, hhh + 4, 0x55200000);
~ 				s1 = EaglercraftVersion.mainMenuStringG;
~ 				if (s1 != null) {
~ 					www = this.fontRendererObj.getStringWidth(s1);
~ 					this.drawString(this.fontRendererObj, s1, 3, 3, 0xFFFFFF99);
~ 				}
~ 				s1 = EaglercraftVersion.mainMenuStringH;
~ 				if (s1 != null) {
~ 					www = Math.max(www, this.fontRendererObj.getStringWidth(s1));
~ 					this.drawString(this.fontRendererObj, s1, 3, 13, 0xFFFFFF99);
~ 				}
~ 			}
~ 			if (EagRuntime.getConfiguration().isEnableSignatureBadge()) {
~ 				UpdateCertificate cert = UpdateService.getClientCertificate();
~ 				GlStateManager.scale(0.66667f, 0.66667f, 0.66667f);
~ 				if (cert != null) {
~ 					s1 = I18n.format("update.digitallySigned",
~ 							GuiUpdateVersionSlot.dateFmt.format(new Date(cert.sigTimestamp)));
~ 				} else {
~ 					s1 = I18n.format("update.signatureInvalid");
~ 				}
~ 				www = this.fontRendererObj.getStringWidth(s1) + 14;
~ 				drawRect((this.width * 2 - www) / 2, 0, (this.width * 2 - www) / 2 + www, 12, 0x33000000);
~ 				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
~ 				this.drawString(this.fontRendererObj, s1, (this.width * 2 - www) / 2 + 12, 2,
~ 						cert != null ? 0xFFFFFF99 : 0xFFFF5555);
~ 				GlStateManager.scale(0.6f, 0.6f, 0.6f);
~ 				mc.getTextureManager().bindTexture(eaglerGuiTextures);
~ 				drawTexturedModalRect((int) ((this.width * 2 - www) / 2 / 0.6f) + 2, 1, cert != null ? 32 : 16, 0, 16,
~ 						16);
~ 			}
~ 			GlStateManager.popMatrix();

> INSERT  2 : 19  @  2

+ 		String lbl = "CREDITS.txt";
+ 		int w = fontRendererObj.getStringWidth(lbl) * 3 / 4;
+ 
+ 		if (i >= (this.width - w - 4) && i <= this.width && j >= 0 && j <= 9) {
+ 			Mouse.showCursor(EnumCursorType.HAND);
+ 			drawRect((this.width - w - 4), 0, this.width, 10, 0x55000099);
+ 		} else {
+ 			drawRect((this.width - w - 4), 0, this.width, 10, 0x55200000);
+ 		}
+ 
+ 		GlStateManager.pushMatrix();
+ 		GlStateManager.translate((this.width - w - 2), 2.0f, 0.0f);
+ 		GlStateManager.scale(0.75f, 0.75f, 0.75f);
+ 		drawString(fontRendererObj, lbl, 0, 0, 16777215);
+ 		GlStateManager.popMatrix();
+ 
+ 		this.updateCheckerOverlay.drawScreen(i, j, f);

> CHANGE  3 : 15  @  3 : 11

~ 	protected void mouseClicked(int par1, int par2, int par3) {
~ 		if (par3 == 0) {
~ 			String lbl = "CREDITS.txt";
~ 			int w = fontRendererObj.getStringWidth(lbl) * 3 / 4;
~ 			if (par1 >= (this.width - w - 4) && par1 <= this.width && par2 >= 0 && par2 <= 10) {
~ 				String resStr = EagRuntime.getResourceString("/assets/eagler/CREDITS.txt");
~ 				if (resStr != null) {
~ 					EagRuntime.openCreditsPopup(resStr);
~ 				}
~ 				mc.getSoundHandler()
~ 						.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
~ 				return;

> DELETE  1  @  1 : 2

> INSERT  1 : 3  @  1

+ 		this.updateCheckerOverlay.mouseClicked(par1, par2, par3);
+ 		super.mouseClicked(par1, par2, par3);

> EOF
