
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> DELETE  3  @  3 : 4

> INSERT  1 : 2  @  1

+ import java.util.Arrays;

> CHANGE  2 : 19  @  2 : 4

~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
~ 
~ import com.google.common.base.Charsets;
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.crypto.MD5Digest;
~ import net.lax1dude.eaglercraft.v1_8.crypto.SHA1Digest;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
~ import net.lax1dude.eaglercraft.v1_8.profile.GuiScreenEditProfile;

> CHANGE  1 : 2  @  1 : 13

~ import net.minecraft.client.audio.PositionedSoundRecord;

> DELETE  1  @  1 : 2

> DELETE  3  @  3 : 5

> DELETE  2  @  2 : 11

> DELETE  2  @  2 : 3

> CHANGE  1 : 2  @  1 : 2

~ 	private static final EaglercraftRandom RANDOM = new EaglercraftRandom();

> INSERT  1 : 7  @  1

+ 	private boolean isDefault;
+ 	private static final int lendef = 5987;
+ 	private static final byte[] md5def = new byte[] { -61, -53, -36, 27, 24, 27, 103, -31, -58, -116, 113, -60, -67, -8,
+ 			-77, 30 };
+ 	private static final byte[] sha1def = new byte[] { -107, 77, 108, 49, 11, -100, -8, -119, -1, -100, -85, -55, 18,
+ 			-69, -107, 113, -93, -101, -79, 32 };

> CHANGE  3 : 4  @  3 : 4

~ 	private static DynamicTexture viewportTexture = null;

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 3

> DELETE  10  @  10 : 12

> CHANGE  6 : 7  @  6 : 8

~ 	private static ResourceLocation backgroundTexture = null;

> DELETE  2  @  2 : 3

> DELETE  39  @  39 : 45

> INSERT  1 : 21  @  1

+ 		MD5Digest md5 = new MD5Digest();
+ 		SHA1Digest sha1 = new SHA1Digest();
+ 		byte[] md5out = new byte[16];
+ 		byte[] sha1out = new byte[20];
+ 		try {
+ 			byte[] bytes = EaglerInputStream.inputStreamToBytesQuiet(
+ 					Minecraft.getMinecraft().getResourceManager().getResource(minecraftTitleTextures).getInputStream());
+ 			if (bytes != null) {
+ 				md5.update(bytes, 0, bytes.length);
+ 				sha1.update(bytes, 0, bytes.length);
+ 				md5.doFinal(md5out, 0);
+ 				sha1.doFinal(sha1out, 0);
+ 				this.isDefault = bytes.length == lendef && Arrays.equals(md5out, md5def)
+ 						&& Arrays.equals(sha1out, sha1def);
+ 			} else {
+ 				this.isDefault = false;
+ 			}
+ 		} catch (IOException e) {
+ 			this.isDefault = false;
+ 		}

> CHANGE  10 : 11  @  10 : 11

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  3 : 7  @  3 : 6

~ 		if (viewportTexture == null) {
~ 			viewportTexture = new DynamicTexture(256, 256);
~ 			backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", viewportTexture);
~ 		}

> DELETE  10  @  10 : 11

> CHANGE  1 : 6  @  1 : 5

~ 
~ 		boolean isFork = !EaglercraftVersion.projectOriginAuthor.equalsIgnoreCase(EaglercraftVersion.projectForkVendor);
~ 
~ 		if (isFork && EaglercraftVersion.mainMenuStringF != null && EaglercraftVersion.mainMenuStringF.length() > 0) {
~ 			i += 11;

> INSERT  2 : 4  @  2

+ 		this.addSingleplayerMultiplayerButtons(i, 24);
+ 

> CHANGE  2 : 5  @  2 : 4

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

> CHANGE  6 : 10  @  6 : 9

~ 		// this.buttonList
~ 		// .add(new GuiButton(1, this.width / 2 - 100, parInt1,
~ 		// I18n.format("menu.singleplayer", new Object[0])));
~ 		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, parInt1 + parInt2 * 0,

> CHANGE  1 : 5  @  1 : 3

~ 		GuiButton btn;
~ 		this.buttonList.add(btn = new GuiButton(14, this.width / 2 - 100, parInt1 + parInt2 * 1,
~ 				I18n.format("menu.forkOnGitlab", new Object[0])));
~ 		btn.enabled = EaglercraftVersion.mainMenuEnableGithubButton;

> CHANGE  2 : 3  @  2 : 16

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  9 : 10  @  9 : 10

~ 			logger.error("Singleplayer was removed dumbass");

> DELETE  6  @  6 : 10

> CHANGE  1 : 2  @  1 : 2

~ 			this.mc.displayGuiScreen(new GuiScreenEditProfile(this));

> CHANGE  2 : 4  @  2 : 4

~ 		if (parGuiButton.id == 14) {
~ 			EagRuntime.openLink(EaglercraftVersion.projectForkURL);

> DELETE  2  @  2 : 11

> DELETE  2  @  2 : 30

> CHANGE  6 : 7  @  6 : 7

~ 		GlStateManager.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);

> CHANGE  73 : 77  @  73 : 77

~ 		this.mc.getTextureManager().bindTexture(backgroundTexture);
~ 		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
~ 		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
~ 		EaglercraftGPU.glCopyTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);

> DELETE  30  @  30 : 31

> DELETE  9  @  9 : 10

> DELETE  24  @  24 : 26

> CHANGE  7 : 8  @  7 : 8

~ 		if (this.isDefault || (double) this.updateCounter < 1.0E-4D) {

> INSERT  10 : 23  @  10

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

> DELETE  3  @  3 : 7

> INSERT  1 : 4  @  1

+ 		String s = EaglercraftVersion.mainMenuStringA;
+ 		this.drawString(this.fontRendererObj, s, 2, this.height - 20, -1);
+ 		s = EaglercraftVersion.mainMenuStringB;

> CHANGE  1 : 3  @  1 : 2

~ 
~ 		String s1 = EaglercraftVersion.mainMenuStringC;

> INSERT  1 : 4  @  1

+ 				this.height - 20, -1);
+ 		s1 = EaglercraftVersion.mainMenuStringD;
+ 		this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2,

> CHANGE  1 : 9  @  1 : 7

~ 
~ 		String lbl = "CREDITS.txt";
~ 		int w = fontRendererObj.getStringWidth(lbl) * 3 / 4;
~ 
~ 		if (i >= (this.width - w - 4) && i <= this.width && j >= 0 && j <= 9) {
~ 			drawRect((this.width - w - 4), 0, this.width, 10, 0x55000099);
~ 		} else {
~ 			drawRect((this.width - w - 4), 0, this.width, 10, 0x55200000);

> INSERT  2 : 8  @  2

+ 		GlStateManager.pushMatrix();
+ 		GlStateManager.translate((this.width - w - 2), 2.0f, 0.0f);
+ 		GlStateManager.scale(0.75f, 0.75f, 0.75f);
+ 		drawString(fontRendererObj, lbl, 0, 0, 16777215);
+ 		GlStateManager.popMatrix();
+ 

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

> INSERT  1 : 2  @  1

+ 		super.mouseClicked(par1, par2, par3);

> EOF
