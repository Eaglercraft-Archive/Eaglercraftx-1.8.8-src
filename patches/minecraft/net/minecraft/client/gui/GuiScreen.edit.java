
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 11

> DELETE  1  @  1 : 3

> INSERT  2 : 3  @  2

+ import java.util.HashMap;

> INSERT  1 : 2  @  1

+ import java.util.Map;

> INSERT  1 : 24  @  1

+ 
+ import net.lax1dude.eaglercraft.v1_8.internal.EnumTouchEvent;
+ import org.apache.commons.lang3.StringUtils;
+ 
+ import com.google.common.base.Splitter;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Sets;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.EaglerXBungeeVersion;
+ import net.lax1dude.eaglercraft.v1_8.Keyboard;
+ import net.lax1dude.eaglercraft.v1_8.Mouse;
+ import net.lax1dude.eaglercraft.v1_8.PauseMenuCustomizeState;
+ import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
+ import net.lax1dude.eaglercraft.v1_8.Touch;
+ import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
+ import net.lax1dude.eaglercraft.v1_8.touch_gui.TouchControls;
+ import net.lax1dude.eaglercraft.v1_8.webview.GuiScreenServerInfo;

> CHANGE  1 : 2  @  1 : 9

~ import net.minecraft.client.gui.inventory.GuiContainer;

> DELETE  2  @  2 : 3

> INSERT  2 : 3  @  2

+ import net.minecraft.client.resources.I18n;

> CHANGE  13 : 14  @  13 : 19

~ import net.minecraft.util.ResourceLocation;

> CHANGE  13 : 14  @  13 : 14

~ 	protected GuiButton selectedButton;

> CHANGE  3 : 5  @  3 : 4

~ 	private String clickedLinkURI;
~ 	protected long showingCloseKey = 0;

> INSERT  1 : 5  @  1

+ 	protected int touchModeCursorPosX = -1;
+ 	protected int touchModeCursorPosY = -1;
+ 	private long lastTouchEvent;
+ 

> CHANGE  1 : 2  @  1 : 2

~ 		for (int k = 0, l = this.buttonList.size(); k < l; ++k) {

> CHANGE  3 : 4  @  3 : 4

~ 		for (int l = 0, m = this.labelList.size(); l < m; ++l) {

> INSERT  3 : 33  @  3

+ 		long millis = EagRuntime.steadyTimeMillis();
+ 		long closeKeyTimeout = millis - showingCloseKey;
+ 		if (closeKeyTimeout < 3000l) {
+ 			int alpha1 = 0xC0000000;
+ 			int alpha2 = 0xFF000000;
+ 			if (closeKeyTimeout > 2500l) {
+ 				float f = (float) (3000l - closeKeyTimeout) * 0.002f;
+ 				if (f < 0.03f)
+ 					f = 0.03f;
+ 				alpha1 = (int) (f * 192.0f) << 24;
+ 				alpha2 = (int) (f * 255.0f) << 24;
+ 			}
+ 			String str;
+ 			int k = getCloseKey();
+ 			if (k == KeyboardConstants.KEY_GRAVE) {
+ 				str = I18n.format("gui.exitKeyRetarded");
+ 			} else {
+ 				str = I18n.format("gui.exitKey", Keyboard.getKeyName(k));
+ 			}
+ 			int w = fontRendererObj.getStringWidth(str);
+ 			int x = (width - w - 4) / 2;
+ 			int y = 10;
+ 			drawRect(x, y, x + w + 4, y + 12, alpha1);
+ 			if (closeKeyTimeout > 2500l)
+ 				GlStateManager.enableBlend();
+ 			fontRendererObj.drawStringWithShadow(str, x + 2, y + 2, 0xFFAAAA | alpha2);
+ 			if (closeKeyTimeout > 2500l)
+ 				GlStateManager.disableBlend();
+ 		}
+ 

> CHANGE  2 : 16  @  2 : 4

~ 	protected int getCloseKey() {
~ 		if (this instanceof GuiContainer) {
~ 			return this.mc.gameSettings.keyBindInventory.getKeyCode();
~ 		} else {
~ 			return this.mc.gameSettings.keyBindClose.getKeyCode();
~ 		}
~ 	}
~ 
~ 	protected void keyTyped(char parChar1, int parInt1) {
~ 		if (!canCloseGui())
~ 			return;
~ 		if (((this.mc.theWorld == null || this.mc.thePlayer.getHealth() <= 0.0F) && parInt1 == 1)
~ 				|| parInt1 == this.mc.gameSettings.keyBindClose.getKeyCode() || (parInt1 == 1
~ 						&& (this.mc.gameSettings.keyBindClose.getKeyCode() == 0 || Keyboard.areKeysLocked()))) {

> INSERT  4 : 6  @  4

+ 		} else if (parInt1 == 1) {
+ 			showingCloseKey = EagRuntime.steadyTimeMillis();

> DELETE  1  @  1 : 2

> CHANGE  3 : 4  @  3 : 13

~ 		return EagRuntime.getClipboard();

> CHANGE  4 : 5  @  4 : 11

~ 			EagRuntime.setClipboard(copyText);

> CHANGE  4 : 6  @  4 : 5

~ 		renderToolTip0(itemstack, i, j, false);
~ 	}

> CHANGE  1 : 5  @  1 : 2

~ 	protected void renderToolTip0(ItemStack itemstack, int i, int j, boolean eagler) {
~ 		List list = itemstack.getTooltipProfanityFilter(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
~ 
~ 		for (int k = 0, l = list.size(); k < l; ++k) {

> CHANGE  7 : 8  @  7 : 8

~ 		this.drawHoveringText0(list, i, j, eagler);

> INSERT  7 : 11  @  7

+ 		drawHoveringText0(list, i, j, false);
+ 	}
+ 
+ 	protected void drawHoveringText0(List<String> list, int i, int j, boolean eagler) {

> CHANGE  7 : 9  @  7 : 9

~ 			for (int m = 0, n = list.size(); m < n; ++m) {
~ 				int l = this.fontRendererObj.getStringWidth(list.get(m));

> CHANGE  5 : 7  @  5 : 7

~ 			int j2 = i;
~ 			int k2 = j;

> CHANGE  5 : 8  @  5 : 8

~ 			if (!eagler) {
~ 				j2 += 12;
~ 				k2 -= 12;

> CHANGE  1 : 10  @  1 : 3

~ 				if (j2 + k > this.width) {
~ 					j2 -= 28 + k;
~ 				}
~ 
~ 				if (k2 + i1 + 6 > this.height) {
~ 					k2 = this.height - i1 - 6;
~ 				}
~ 			} else {
~ 				j2 -= (k + 3) >> 1;

> CHANGE  19 : 22  @  19 : 20

~ 				if (s1.length() > 0) {
~ 					this.fontRendererObj.drawStringWithShadow(s1, (float) j2, (float) k2, -1);
~ 				}

> CHANGE  16 : 17  @  16 : 17

~ 	public void handleComponentHover(IChatComponent parIChatComponent, int parInt1, int parInt2) {

> CHANGE  76 : 77  @  76 : 77

~ 	public boolean handleComponentClick(IChatComponent parIChatComponent) {

> INSERT  13 : 14  @  13

+ 					String uri = clickevent.getValue();

> CHANGE  1 : 6  @  1 : 22

~ 					if (this.mc.gameSettings.chatLinksPrompt) {
~ 						this.clickedLinkURI = uri;
~ 						this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, clickevent.getValue(), 31102009, false));
~ 					} else {
~ 						this.openWebLink(uri);

> CHANGE  2 : 3  @  2 : 4

~ 					// rip

> CHANGE  5 : 15  @  5 : 8

~ 					/*
~ 					 * ChatUserInfo chatuserinfo =
~ 					 * this.mc.getTwitchStream().func_152926_a(clickevent.getValue()); if
~ 					 * (chatuserinfo != null) { this.mc.displayGuiScreen(new
~ 					 * GuiTwitchUserMode(this.mc.getTwitchStream(), chatuserinfo)); } else { }
~ 					 */
~ 					LOGGER.error("Tried to handle twitch user but couldn\'t find them!");
~ 				} else if (clickevent.getAction() == ClickEvent.Action.EAGLER_PLUGIN_DOWNLOAD) {
~ 					if (EaglerXBungeeVersion.pluginFileEPK.equals(clickevent.getValue())) {
~ 						EaglerXBungeeVersion.startPluginDownload();

> CHANGE  1 : 3  @  1 : 2

~ 						LOGGER.error("Invalid plugin download from EPK was blocked: {}",
~ 								EaglerXBungeeVersion.pluginFileEPK);

> CHANGE  24 : 49  @  24 : 26

~ 	protected void touchStarted(int parInt1, int parInt2, int parInt3) {
~ 		if (shouldTouchGenerateMouseEvents()) {
~ 			this.mouseClicked(parInt1, parInt2, 12345);
~ 		}
~ 	}
~ 
~ 	protected void touchTapped(int parInt1, int parInt2, int parInt3) {
~ 		if (shouldTouchGenerateMouseEvents()) {
~ 			this.mouseClicked(parInt1, parInt2, 0);
~ 			this.mouseReleased(parInt1, parInt2, 0);
~ 		}
~ 	}
~ 
~ 	protected void touchMoved(int parInt1, int parInt2, int parInt3) {
~ 	}
~ 
~ 	protected void touchEndMove(int parInt1, int parInt2, int parInt3) {
~ 		if (shouldTouchGenerateMouseEvents()) {
~ 			this.mouseReleased(parInt1, parInt2, 12345);
~ 		}
~ 	}
~ 
~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
~ 		boolean touchMode = PointerInputAbstraction.isTouchMode();
~ 		if (parInt3 == 0 || parInt3 == 12345) {

> INSERT  2 : 4  @  2

+ 				if (touchMode && (parInt3 == 12345) != guibutton.isSliderTouchEvents())
+ 					continue;

> CHANGE  11 : 13  @  11 : 12

~ 		if (this.selectedButton != null && (k == 0 || k == 12345)
~ 				&& (!PointerInputAbstraction.isTouchMode() || (k == 12345) == selectedButton.isSliderTouchEvents())) {

> CHANGE  9 : 10  @  9 : 10

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  16 : 23  @  16

+ 		boolean noTouch = true;
+ 		while (Touch.next()) {
+ 			noTouch = false;
+ 			this.handleTouchInput();
+ 			TouchControls.handleInput();
+ 		}
+ 

> CHANGE  2 : 5  @  2 : 3

~ 				if (noTouch) {
~ 					this.handleMouseInput();
~ 				}

> INSERT  11 : 96  @  11

+ 	public final Map<Integer, int[]> touchStarts = new HashMap<>();
+ 
+ 	/**
+ 	 * Handles touch input.
+ 	 */
+ 	public void handleTouchInput() throws IOException {
+ 		EnumTouchEvent et = Touch.getEventType();
+ 		if (et == EnumTouchEvent.TOUCHSTART) {
+ 			PointerInputAbstraction.enterTouchModeHook();
+ 		}
+ 		float scaleFac = getEaglerScale();
+ 		for (int t = 0, c = Touch.getEventTouchPointCount(); t < c; ++t) {
+ 			int u = Touch.getEventTouchPointUID(t);
+ 			int i = Touch.getEventTouchX(t);
+ 			int j = Touch.getEventTouchY(t);
+ 			if (et == EnumTouchEvent.TOUCHSTART) {
+ 				if (TouchControls.handleTouchBegin(u, i, j)) {
+ 					continue;
+ 				}
+ 			} else if (et == EnumTouchEvent.TOUCHEND) {
+ 				if (TouchControls.handleTouchEnd(u, i, j)) {
+ 					continue;
+ 				}
+ 			}
+ 			i = applyEaglerScale(scaleFac, i * this.width / this.mc.displayWidth, this.width);
+ 			j = applyEaglerScale(scaleFac, this.height - j * this.height / this.mc.displayHeight - 1, this.height);
+ 			float rad = Touch.getEventTouchRadiusMixed(t);
+ 			float si = rad * this.width / this.mc.displayWidth / scaleFac;
+ 			if (si < 1.0f)
+ 				si = 1.0f;
+ 			float sj = rad * this.height / this.mc.displayHeight / scaleFac;
+ 			if (sj < 1.0f)
+ 				sj = 1.0f;
+ 			int[] ck = touchStarts.remove(u);
+ 			switch (et) {
+ 			case TOUCHSTART:
+ 				if (t == 0) {
+ 					touchModeCursorPosX = i;
+ 					touchModeCursorPosY = j;
+ 				}
+ 				lastTouchEvent = EagRuntime.steadyTimeMillis();
+ 				touchStarts.put(u, new int[] { i, j, 0 });
+ 				this.touchStarted(i, j, u);
+ 				break;
+ 			case TOUCHMOVE:
+ 				if (t == 0) {
+ 					touchModeCursorPosX = i;
+ 					touchModeCursorPosY = j;
+ 				}
+ 				if (ck != null && Math.abs(ck[0] - i) < si && Math.abs(ck[1] - j) < sj) {
+ 					touchStarts.put(u, ck);
+ 					break;
+ 				}
+ 				touchStarts.put(u, new int[] { i, j, (ck != null && isTouchDraggingStateLocked(u)) ? ck[2] : 1 });
+ 				this.touchMoved(i, j, u);
+ 				if (t == 0 && shouldTouchGenerateMouseEvents()) {
+ 					this.mouseClickMove(i, j, 0, EagRuntime.steadyTimeMillis() - lastTouchEvent);
+ 				}
+ 				break;
+ 			case TOUCHEND:
+ 				if (ck == null)
+ 					break;
+ 				if (t == 0) {
+ 					touchModeCursorPosX = -1;
+ 					touchModeCursorPosY = -1;
+ 				}
+ 				if (ck != null && ck[2] == 1) {
+ 					this.touchEndMove(i, j, u);
+ 				} else {
+ 					if (ck != null) {
+ 						i = ck[0];
+ 						j = ck[1];
+ 					}
+ 					this.touchTapped(i, j, u);
+ 				}
+ 				break;
+ 			}
+ 		}
+ 	}
+ 
+ 	public boolean isTouchPointDragging(int uid) {
+ 		int[] ret = touchStarts.get(uid);
+ 		return ret != null && ret[2] == 1;
+ 	}
+ 

> CHANGE  1 : 5  @  1 : 3

~ 		float f = getEaglerScale();
~ 		int i = applyEaglerScale(f, Mouse.getEventX() * this.width / this.mc.displayWidth, this.width);
~ 		int j = applyEaglerScale(f, this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1,
~ 				this.height);

> INSERT  2 : 3  @  2

+ 			PointerInputAbstraction.enterMouseModeHook();

> INSERT  39 : 43  @  39

+ 	protected boolean isPartOfPauseMenu() {
+ 		return false;
+ 	}
+ 

> CHANGE  2 : 53  @  2 : 3

~ 			boolean ingame = isPartOfPauseMenu();
~ 			ResourceLocation loc = (ingame && PauseMenuCustomizeState.icon_background_pause != null)
~ 					? PauseMenuCustomizeState.icon_background_pause
~ 					: PauseMenuCustomizeState.icon_background_all;
~ 			float aspect = (ingame && PauseMenuCustomizeState.icon_background_pause != null)
~ 					? 1.0f / PauseMenuCustomizeState.icon_background_pause_aspect
~ 					: 1.0f / PauseMenuCustomizeState.icon_background_all_aspect;
~ 			if (loc != null) {
~ 				GlStateManager.disableLighting();
~ 				GlStateManager.disableFog();
~ 				GlStateManager.enableBlend();
~ 				GlStateManager.disableAlpha();
~ 				GlStateManager.enableTexture2D();
~ 				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
~ 				Tessellator tessellator = Tessellator.getInstance();
~ 				WorldRenderer worldrenderer = tessellator.getWorldRenderer();
~ 				this.mc.getTextureManager().bindTexture(loc);
~ 				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
~ 				float f = 64.0F;
~ 				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
~ 				worldrenderer.pos(0.0D, (double) this.height, 0.0D).tex(0.0D, (double) ((float) this.height / f))
~ 						.color(64, 64, 64, 192).endVertex();
~ 				worldrenderer.pos((double) this.width, (double) this.height, 0.0D)
~ 						.tex((double) ((float) this.width / f * aspect), (double) ((float) this.height / f))
~ 						.color(64, 64, 64, 192).endVertex();
~ 				worldrenderer.pos((double) this.width, 0.0D, 0.0D)
~ 						.tex((double) ((float) this.width / f * aspect), (double) 0).color(64, 64, 64, 192).endVertex();
~ 				worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double) 0).color(64, 64, 64, 192).endVertex();
~ 				tessellator.draw();
~ 				GlStateManager.enableAlpha();
~ 			} else {
~ 				this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
~ 			}
~ 			if (!(this instanceof GuiScreenServerInfo)) {
~ 				loc = (ingame && PauseMenuCustomizeState.icon_watermark_pause != null)
~ 						? PauseMenuCustomizeState.icon_watermark_pause
~ 						: PauseMenuCustomizeState.icon_watermark_all;
~ 				aspect = (ingame && PauseMenuCustomizeState.icon_watermark_pause != null)
~ 						? PauseMenuCustomizeState.icon_watermark_pause_aspect
~ 						: PauseMenuCustomizeState.icon_watermark_all_aspect;
~ 				if (loc != null) {
~ 					GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
~ 					mc.getTextureManager().bindTexture(loc);
~ 					GlStateManager.pushMatrix();
~ 					GlStateManager.translate(8, height - 72, 0.0f);
~ 					float f2 = 64.0f / 256.0f;
~ 					GlStateManager.scale(f2 * aspect, f2, f2);
~ 					this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
~ 					GlStateManager.popMatrix();
~ 				}
~ 			}

> CHANGE  42 : 44  @  42 : 51

~ 	private void openWebLink(String parURI) {
~ 		EagRuntime.openLink(parURI);

> INSERT  34 : 75  @  34

+ 
+ 	public boolean shouldHangupIntegratedServer() {
+ 		return true;
+ 	}
+ 
+ 	public boolean blockPTTKey() {
+ 		return false;
+ 	}
+ 
+ 	public void fireInputEvent(EnumInputEvent event, String param) {
+ 
+ 	}
+ 
+ 	public boolean showCopyPasteButtons() {
+ 		return false;
+ 	}
+ 
+ 	public static int applyEaglerScale(float scaleFac, int coord, int screenDim) {
+ 		return (int) ((coord - (1.0f - scaleFac) * screenDim * 0.5f) / scaleFac);
+ 	}
+ 
+ 	public float getEaglerScale() {
+ 		return PointerInputAbstraction.isTouchMode() ? getTouchModeScale() : 1.0f;
+ 	}
+ 
+ 	protected float getTouchModeScale() {
+ 		return 1.0f;
+ 	}
+ 
+ 	public boolean canCloseGui() {
+ 		return true;
+ 	}
+ 
+ 	protected boolean isTouchDraggingStateLocked(int uid) {
+ 		return false;
+ 	}
+ 
+ 	protected boolean shouldTouchGenerateMouseEvents() {
+ 		return true;
+ 	}
+ 

> EOF
