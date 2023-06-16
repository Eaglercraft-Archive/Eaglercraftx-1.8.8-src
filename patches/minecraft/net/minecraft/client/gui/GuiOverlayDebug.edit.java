
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 4

~ import java.text.SimpleDateFormat;

> INSERT  1 : 3  @  1

+ import java.util.Calendar;
+ import java.util.Iterator;

> INSERT  1 : 2  @  1

+ import java.util.Locale;

> INSERT  1 : 13  @  1

+ import java.util.TimeZone;
+ 
+ import com.google.common.base.Strings;
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.Display;
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;

> CHANGE  5 : 8  @  5 : 10

~ import net.minecraft.client.renderer.RenderHelper;
~ import net.minecraft.client.renderer.entity.RenderManager;
~ import net.minecraft.client.resources.I18n;

> CHANGE  1 : 3  @  1 : 2

~ import net.minecraft.entity.EntityLivingBase;
~ import net.minecraft.potion.PotionEffect;

> CHANGE  8 : 9  @  8 : 9

~ import net.minecraft.world.biome.BiomeGenBase;

> DELETE  1  @  1 : 3

> INSERT  11 : 13  @  11

+ 		int ww = scaledResolutionIn.getScaledWidth();
+ 		int hh = scaledResolutionIn.getScaledHeight();

> CHANGE  1 : 20  @  1 : 7

~ 		if (this.mc.gameSettings.showDebugInfo) {
~ 			GlStateManager.pushMatrix();
~ 			this.renderDebugInfoLeft();
~ 			this.renderDebugInfoRight(scaledResolutionIn);
~ 			GlStateManager.popMatrix();
~ 			if (this.mc.gameSettings.field_181657_aC) {
~ 				this.func_181554_e();
~ 			}
~ 		} else {
~ 			int i = 2;
~ 
~ 			if (this.mc.gameSettings.hudFps) {
~ 				drawFPS(2, i);
~ 				i += 9;
~ 			}
~ 
~ 			if (this.mc.gameSettings.hudCoords) {
~ 				drawXYZ(2, i);
~ 			}

> INSERT  2 : 26  @  2

+ 		if (this.mc.currentScreen == null || !(this.mc.currentScreen instanceof GuiChat)) {
+ 			if (this.mc.gameSettings.hudStats) {
+ 				drawStatsHUD(ww - 2, hh - 2);
+ 			}
+ 
+ 			if (this.mc.gameSettings.hudWorld) {
+ 				drawWorldHUD(2, hh - 2);
+ 			}
+ 		}
+ 
+ 		if (this.mc.gameSettings.hudCoords && this.mc.joinWorldTickCounter < 80) {
+ 			if (this.mc.joinWorldTickCounter > 70) {
+ 				GlStateManager.enableBlend();
+ 				GlStateManager.blendFunc(770, 771);
+ 			}
+ 			int i = this.mc.joinWorldTickCounter - 70;
+ 			if (i < 0)
+ 				i = 0;
+ 			drawHideHUD(ww / 2, hh - 70, (10 - i) * 0xFF / 10);
+ 			if (this.mc.joinWorldTickCounter > 70) {
+ 				GlStateManager.disableBlend();
+ 			}
+ 		}
+ 

> INSERT  3 : 142  @  3

+ 	private void drawFPS(int x, int y) {
+ 		this.fontRenderer.drawStringWithShadow(this.mc.renderGlobal.getDebugInfoShort(), x, y, 0xFFFFFF);
+ 	}
+ 
+ 	private void drawXYZ(int x, int y) {
+ 		Entity e = mc.getRenderViewEntity();
+ 		BlockPos blockpos = new BlockPos(e.posX, e.getEntityBoundingBox().minY, e.posZ);
+ 		this.fontRenderer.drawStringWithShadow(
+ 				"x: " + blockpos.getX() + ", y: " + blockpos.getY() + ", z: " + blockpos.getZ(), x, y, 0xFFFFFF);
+ 	}
+ 
+ 	private void drawStatsHUD(int x, int y) {
+ 		int i = 9;
+ 
+ 		String line = "Walk: " + EnumChatFormatting.YELLOW + HString.format("%.2f", mc.thePlayer.getAIMoveSpeed())
+ 				+ EnumChatFormatting.WHITE + " Flight: "
+ 				+ (mc.thePlayer.capabilities.allowFlying
+ 						? ("" + EnumChatFormatting.YELLOW + mc.thePlayer.capabilities.getFlySpeed())
+ 						: EnumChatFormatting.RED + "No");
+ 		int lw = fontRenderer.getStringWidth(line);
+ 		this.fontRenderer.drawStringWithShadow(line, x - lw, y - i, 0xFFFFFF);
+ 		i += 11;
+ 
+ 		line = "Food: " + EnumChatFormatting.YELLOW + mc.thePlayer.getFoodStats().getFoodLevel()
+ 				+ EnumChatFormatting.WHITE + ", Sat: " + EnumChatFormatting.YELLOW
+ 				+ HString.format("%.1f", mc.thePlayer.getFoodStats().getSaturationLevel());
+ 		lw = fontRenderer.getStringWidth(line);
+ 		this.fontRenderer.drawStringWithShadow(line, x - lw, y - i, 0xFFFFFF);
+ 		i += 11;
+ 
+ 		line = "Amr: " + EnumChatFormatting.YELLOW + mc.thePlayer.getTotalArmorValue() + EnumChatFormatting.WHITE
+ 				+ ", Health: " + EnumChatFormatting.RED + HString.format("%.1f", mc.thePlayer.getHealth());
+ 		lw = fontRenderer.getStringWidth(line);
+ 		this.fontRenderer.drawStringWithShadow(line, x - lw, y - i, 0xFFFFFF);
+ 		i += 11;
+ 
+ 		int xpc = mc.thePlayer.xpBarCap();
+ 		line = "XP: " + EnumChatFormatting.GREEN + MathHelper.floor_float(mc.thePlayer.experience * xpc)
+ 				+ EnumChatFormatting.WHITE + " / " + EnumChatFormatting.GREEN + xpc;
+ 		lw = fontRenderer.getStringWidth(line);
+ 		this.fontRenderer.drawStringWithShadow(line, x - lw, y - i, 0xFFFFFF);
+ 		i += 11;
+ 
+ 		Iterator<PotionEffect> potions = mc.thePlayer.getActivePotionEffects().iterator();
+ 		if (potions.hasNext()) {
+ 			while (potions.hasNext()) {
+ 				i += 11;
+ 				PotionEffect e = potions.next();
+ 				int t = e.getDuration() / 20;
+ 				int m = t / 60;
+ 				int s = t % 60;
+ 				int j = e.getAmplifier();
+ 				if (j > 0) {
+ 					line = I18n.format(e.getEffectName())
+ 							+ (j > 0 ? (" " + EnumChatFormatting.YELLOW + EnumChatFormatting.BOLD
+ 									+ I18n.format("potion.potency." + j) + EnumChatFormatting.RESET) : "")
+ 							+ " [" + EnumChatFormatting.YELLOW + HString.format("%02d:%02d", m, s)
+ 							+ EnumChatFormatting.RESET + "]";
+ 				} else {
+ 					line = I18n.format(e.getEffectName()) + " [" + EnumChatFormatting.YELLOW
+ 							+ HString.format("%02d:%02d", m, s) + EnumChatFormatting.RESET + "]";
+ 				}
+ 				lw = fontRenderer.getStringWidth(line);
+ 				this.fontRenderer.drawStringWithShadow(line, x - lw, y - i, 0xFFFFFF);
+ 			}
+ 		}
+ 
+ 	}
+ 
+ 	public static final int ticksAtMidnight = 18000;
+ 	public static final int ticksPerDay = 24000;
+ 	public static final int ticksPerHour = 1000;
+ 	public static final double ticksPerMinute = 1000d / 60d;
+ 	public static final double ticksPerSecond = 1000d / 60d / 60d;
+ 	private static final SimpleDateFormat SDFTwentyFour = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
+ 	private static final SimpleDateFormat SDFTwelve = new SimpleDateFormat("h:mm aa", Locale.ENGLISH);
+ 
+ 	private void drawWorldHUD(int x, int y) {
+ 		/*
+ 		 * Math was taken from: https://github.com/EssentialsX/Essentials/blob/
+ 		 * dc7fb919391d62de45e17b51ae1e6fe3e66d7ac6/Essentials/src/main/java/com/
+ 		 * earth2me/essentials/utils/DescParseTickFormat.java
+ 		 */
+ 		long totalTicks = mc.theWorld.getWorldTime();
+ 		long ticks = totalTicks;
+ 		ticks = ticks - ticksAtMidnight + ticksPerDay;
+ 		final long days = ticks / ticksPerDay;
+ 		ticks -= days * ticksPerDay;
+ 		final long hours = ticks / ticksPerHour;
+ 		ticks -= hours * ticksPerHour;
+ 		final long minutes = (long) Math.floor(ticks / ticksPerMinute);
+ 		final double dticks = ticks - minutes * ticksPerMinute;
+ 		final long seconds = (long) Math.floor(dticks / ticksPerSecond);
+ 
+ 		// TODO: why does desktop JRE not apply "GMT" correctly?
+ 		final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);
+ 
+ 		cal.setLenient(true);
+ 		cal.set(0, Calendar.JANUARY, 1, 0, 0, 0);
+ 		cal.add(Calendar.DAY_OF_YEAR, (int) days);
+ 		cal.add(Calendar.HOUR_OF_DAY, (int) hours);
+ 		cal.add(Calendar.MINUTE, (int) minutes);
+ 		cal.add(Calendar.SECOND, (int) seconds + 1);
+ 
+ 		String timeString = EnumChatFormatting.WHITE + "Day " + ((totalTicks + 30000l) / 24000l) + " ("
+ 				+ EnumChatFormatting.YELLOW
+ 				+ (this.mc.gameSettings.hud24h ? SDFTwentyFour : SDFTwelve).format(cal.getTime())
+ 				+ EnumChatFormatting.WHITE + ")";
+ 
+ 		Entity e = mc.getRenderViewEntity();
+ 		BlockPos blockpos = new BlockPos(e.posX, MathHelper.clamp_double(e.getEntityBoundingBox().minY, 0.0D, 254.0D),
+ 				e.posZ);
+ 		BiomeGenBase biome = mc.theWorld.getBiomeGenForCoords(blockpos);
+ 
+ 		Chunk c = mc.theWorld.getChunkFromBlockCoords(blockpos);
+ 		int blockLight = c.getLightFor(EnumSkyBlock.BLOCK, blockpos);
+ 		int skyLight = c.getLightFor(EnumSkyBlock.SKY, blockpos) - mc.theWorld.calculateSkylightSubtracted(1.0f);
+ 		int totalLight = Math.max(blockLight, skyLight);
+ 		EnumChatFormatting lightColor = blockLight < 8
+ 				? ((skyLight < 8 || !mc.theWorld.isDaytime()) ? EnumChatFormatting.RED : EnumChatFormatting.YELLOW)
+ 				: EnumChatFormatting.GREEN;
+ 		String lightString = "Light: " + lightColor + totalLight + EnumChatFormatting.WHITE;
+ 
+ 		float temp = biome.getFloatTemperature(blockpos);
+ 
+ 		String tempString = "Temp: "
+ 				+ ((blockLight > 11 || temp > 0.15f) ? EnumChatFormatting.YELLOW : EnumChatFormatting.AQUA)
+ 				+ HString.format("%.2f", temp) + EnumChatFormatting.WHITE;
+ 
+ 		this.fontRenderer.drawStringWithShadow(timeString, x, y - 30, 0xFFFFFF);
+ 		this.fontRenderer.drawStringWithShadow("Biome: " + EnumChatFormatting.AQUA + biome.biomeName, x, y - 19,
+ 				0xFFFFFF);
+ 		this.fontRenderer.drawStringWithShadow(lightString + " " + tempString, x, y - 8, 0xFFFFFF);
+ 	}
+ 
+ 	private void drawHideHUD(int x, int y, int fade) {
+ 		drawCenteredString(fontRenderer, I18n.format("options.hud.note"), x, y, 0xEECC00 | (fade << 24));
+ 	}
+ 

> INSERT  39 : 46  @  39

+ 		if (!this.mc.gameSettings.showDebugInfo) {
+ 			BlockPos blockpos = new BlockPos(this.mc.getRenderViewEntity().posX,
+ 					this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
+ 			return Lists.newArrayList(new String[] { this.mc.renderGlobal.getDebugInfoShort(),
+ 					"x: " + blockpos.getX() + ", y: " + blockpos.getY() + ", z: " + blockpos.getZ() });
+ 		}
+ 

> CHANGE  10 : 11  @  10 : 11

~ 					HString.format("Chunk-relative: %d %d %d", new Object[] { Integer.valueOf(blockpos.getX() & 15),

> CHANGE  25 : 26  @  25 : 26

~ 					HString.format("XYZ: %.3f / %.5f / %.3f",

> CHANGE  3 : 4  @  3 : 4

~ 					HString.format("Block: %d %d %d",

> CHANGE  2 : 3  @  2 : 3

~ 					HString.format("Chunk: %d %d %d in %d %d %d",

> CHANGE  3 : 4  @  3 : 4

~ 					HString.format("Facing: %s (%s) (%.1f / %.1f)",

> CHANGE  5 : 6  @  5 : 6

~ 				arraylist.add("Biome: " + chunk.getBiome(blockpos).biomeName);

> CHANGE  4 : 5  @  4 : 14

~ 				arraylist.add(HString.format("Local Difficulty: %.2f (Day %d)",

> DELETE  4  @  4 : 8

> CHANGE  4 : 5  @  4 : 5

~ 				arraylist.add(HString.format("Looking at: %d %d %d", new Object[] { Integer.valueOf(blockpos1.getX()),

> CHANGE  8 : 36  @  8 : 25

~ 		ArrayList arraylist;
~ 		if (EagRuntime.getPlatformType() != EnumPlatformType.JAVASCRIPT) {
~ 			long i = EagRuntime.maxMemory();
~ 			long j = EagRuntime.totalMemory();
~ 			long k = EagRuntime.freeMemory();
~ 			long l = j - k;
~ 			arraylist = Lists.newArrayList(new String[] {
~ 					HString.format("Java: %s %dbit",
~ 							new Object[] { System.getProperty("java.version"),
~ 									Integer.valueOf(this.mc.isJava64bit() ? 64 : 32) }),
~ 					HString.format("Mem: % 2d%% %03d/%03dMB",
~ 							new Object[] { Long.valueOf(l * 100L / i), Long.valueOf(bytesToMb(l)),
~ 									Long.valueOf(bytesToMb(i)) }),
~ 					HString.format("Allocated: % 2d%% %03dMB",
~ 							new Object[] { Long.valueOf(j * 100L / i), Long.valueOf(bytesToMb(j)) }),
~ 					"", HString.format("CPU: %s", new Object[] { "eaglercraft" }), "",
~ 					HString.format("Display: %dx%d (%s)",
~ 							new Object[] { Integer.valueOf(Display.getWidth()), Integer.valueOf(Display.getHeight()),
~ 									EaglercraftGPU.glGetString(7936) }),
~ 					EaglercraftGPU.glGetString(7937), EaglercraftGPU.glGetString(7938) });
~ 		} else {
~ 			arraylist = Lists.newArrayList(
~ 					new String[] { "Java: TeaVM", "", HString.format("CPU: %s", new Object[] { "eaglercraft" }), "",
~ 							HString.format("Display: %dx%d (%s)",
~ 									new Object[] { Integer.valueOf(Display.getWidth()),
~ 											Integer.valueOf(Display.getHeight()), EaglercraftGPU.glGetString(7936) }),
~ 							EaglercraftGPU.glGetString(7937), EaglercraftGPU.glGetString(7938) });
~ 		}

> DELETE  8  @  8 : 12

> EOF
