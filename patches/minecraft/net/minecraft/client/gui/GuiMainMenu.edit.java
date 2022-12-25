
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> DELETE  5  @  6 : 7

> INSERT  6 : 7  @  8

+ import java.util.Arrays;

> CHANGE  9 : 26  @  10 : 12

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

> CHANGE  27 : 28  @  13 : 25

~ import net.minecraft.client.audio.PositionedSoundRecord;

> DELETE  29  @  26 : 27

> DELETE  32  @  30 : 32

> DELETE  34  @  34 : 43

> DELETE  36  @  45 : 46

> CHANGE  37 : 38  @  47 : 48

~ 	private static final EaglercraftRandom RANDOM = new EaglercraftRandom();

> INSERT  39 : 45  @  49

+ 	private boolean isDefault;
+ 	private static final int lendef = 5987;
+ 	private static final byte[] md5def = new byte[] { -61, -53, -36, 27, 24, 27, 103, -31, -58, -116, 113, -60, -67, -8,
+ 			-77, 30 };
+ 	private static final byte[] sha1def = new byte[] { -107, 77, 108, 49, 11, -100, -8, -119, -1, -100, -85, -55, 18,
+ 			-69, -107, 113, -93, -101, -79, 32 };

> CHANGE  48 : 49  @  52 : 53

~ 	private static DynamicTexture viewportTexture = null;

> DELETE  50  @  54 : 55

> DELETE  52  @  57 : 58

> DELETE  62  @  68 : 70

> CHANGE  68 : 69  @  76 : 78

~ 	private static ResourceLocation backgroundTexture = null;

> DELETE  71  @  80 : 81

> DELETE  110  @  120 : 126

> INSERT  111 : 131  @  127

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

> CHANGE  141 : 142  @  137 : 138

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  145 : 149  @  141 : 144

~ 		if (viewportTexture == null) {
~ 			viewportTexture = new DynamicTexture(256, 256);
~ 			backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", viewportTexture);
~ 		}

> DELETE  159  @  154 : 155

> CHANGE  160 : 165  @  156 : 160

~ 
~ 		boolean isFork = !EaglercraftVersion.projectOriginAuthor.equalsIgnoreCase(EaglercraftVersion.projectForkVendor);
~ 
~ 		if (isFork && EaglercraftVersion.mainMenuStringF != null && EaglercraftVersion.mainMenuStringF.length() > 0) {
~ 			i += 11;

> INSERT  167 : 169  @  162

+ 		this.addSingleplayerMultiplayerButtons(i, 24);
+ 

> CHANGE  171 : 174  @  164 : 166

~ 		this.buttonList.add(new GuiButton(4, this.width / 2 + 2, i + 72 + 12, 98, 20,
~ 				I18n.format("menu.editProfile", new Object[0])));
~ 

> CHANGE  175 : 180  @  167 : 168

~ 
~ 		if (isFork) {
~ 			this.openGLWarning1 = EaglercraftVersion.mainMenuStringE;
~ 			this.openGLWarning2 = EaglercraftVersion.mainMenuStringF;
~ 			boolean line2 = this.openGLWarning2 != null && this.openGLWarning2.length() > 0;

> CHANGE  184 : 185  @  172 : 173

~ 			this.field_92021_u = ((GuiButton) this.buttonList.get(0)).yPosition - (line2 ? 32 : 21);

> CHANGE  186 : 187  @  174 : 175

~ 			this.field_92019_w = this.field_92021_u + (line2 ? 24 : 11);

> CHANGE  193 : 197  @  181 : 184

~ 		// this.buttonList
~ 		// .add(new GuiButton(1, this.width / 2 - 100, parInt1,
~ 		// I18n.format("menu.singleplayer", new Object[0])));
~ 		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, parInt1 + parInt2 * 0,

> CHANGE  198 : 202  @  185 : 187

~ 		GuiButton btn;
~ 		this.buttonList.add(btn = new GuiButton(14, this.width / 2 - 100, parInt1 + parInt2 * 1,
~ 				I18n.format("menu.forkOnGitlab", new Object[0])));
~ 		btn.enabled = EaglercraftVersion.mainMenuEnableGithubButton;

> CHANGE  204 : 205  @  189 : 203

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  214 : 215  @  212 : 213

~ 			logger.error("Singleplayer was removed dumbass");

> DELETE  221  @  219 : 223

> CHANGE  222 : 223  @  224 : 225

~ 			this.mc.displayGuiScreen(new GuiScreenEditProfile(this));

> CHANGE  225 : 227  @  227 : 229

~ 		if (parGuiButton.id == 14) {
~ 			EagRuntime.openLink(EaglercraftVersion.projectForkURL);

> DELETE  229  @  231 : 240

> DELETE  231  @  242 : 270

> CHANGE  237 : 238  @  276 : 277

~ 		GlStateManager.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);

> CHANGE  311 : 315  @  350 : 354

~ 		this.mc.getTextureManager().bindTexture(backgroundTexture);
~ 		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
~ 		EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
~ 		EaglercraftGPU.glCopyTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);

> DELETE  345  @  384 : 385

> DELETE  354  @  394 : 395

> DELETE  378  @  419 : 421

> CHANGE  385 : 386  @  428 : 429

~ 		if (this.isDefault || (double) this.updateCounter < 1.0E-4D) {

> INSERT  396 : 409  @  439

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

> CHANGE  411 : 412  @  441 : 442

~ 		GlStateManager.rotate(isForkLabel ? -12.0F : -20.0F, 0.0F, 0.0F, 1.0F);

> INSERT  415 : 418  @  445

+ 		if (isForkLabel) {
+ 			f1 *= 0.8f;
+ 		}

> DELETE  421  @  448 : 452

> INSERT  422 : 425  @  453

+ 		String s = EaglercraftVersion.mainMenuStringA;
+ 		this.drawString(this.fontRendererObj, s, 2, this.height - 20, -1);
+ 		s = EaglercraftVersion.mainMenuStringB;

> CHANGE  426 : 428  @  454 : 455

~ 
~ 		String s1 = EaglercraftVersion.mainMenuStringC;

> INSERT  429 : 432  @  456

+ 				this.height - 20, -1);
+ 		s1 = EaglercraftVersion.mainMenuStringD;
+ 		this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2,

> CHANGE  433 : 441  @  457 : 463

~ 
~ 		String lbl = "CREDITS.txt";
~ 		int w = fontRendererObj.getStringWidth(lbl) * 3 / 4;
~ 
~ 		if (i >= (this.width - w - 4) && i <= this.width && j >= 0 && j <= 9) {
~ 			drawRect((this.width - w - 4), 0, this.width, 10, 0x55000099);
~ 		} else {
~ 			drawRect((this.width - w - 4), 0, this.width, 10, 0x55200000);

> INSERT  443 : 449  @  465

+ 		GlStateManager.pushMatrix();
+ 		GlStateManager.translate((this.width - w - 2), 2.0f, 0.0f);
+ 		GlStateManager.scale(0.75f, 0.75f, 0.75f);
+ 		drawString(fontRendererObj, lbl, 0, 0, 16777215);
+ 		GlStateManager.popMatrix();
+ 

> CHANGE  452 : 464  @  468 : 476

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

> DELETE  465  @  477 : 478

> INSERT  466 : 467  @  479

+ 		super.mouseClicked(par1, par2, par3);

> EOF
