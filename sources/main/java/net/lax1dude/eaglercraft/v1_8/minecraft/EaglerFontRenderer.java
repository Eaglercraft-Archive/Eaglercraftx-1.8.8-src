package net.lax1dude.eaglercraft.v1_8.minecraft;

import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.InstancedFontRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info) 
 * 
 */
public class EaglerFontRenderer extends FontRenderer {

	private final int[] temporaryCodepointArray = new int[6553];

	public EaglerFontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn,
			boolean unicode) {
		super(gameSettingsIn, location, textureManagerIn, unicode);
	}

	public int drawString(String text, float x, float y, int color, boolean dropShadow) {
		if (text == null || text.length() == 0) {
			this.posX = x + (dropShadow ? 1 : 0);
			this.posY = y;
		} else {
			if(this.unicodeFlag || !decodeASCIICodepointsAndValidate(text)) {
				return super.drawString(text, x, y, color, dropShadow);
			}
			this.resetStyles();
			if ((color & 0xFC000000) == 0) {
				color |= 0xFF000000;
			}
			this.red = (float) (color >> 16 & 255) / 255.0F;
			this.blue = (float) (color >> 8 & 255) / 255.0F;
			this.green = (float) (color & 255) / 255.0F;
			this.alpha = (float) (color >> 24 & 255) / 255.0F;
			this.posX = x;
			this.posY = y;
			this.textColor = color;
			this.renderStringAtPos0(text, dropShadow);
		}
		return (int) this.posX;
	}

	protected void renderStringAtPos(String parString1, boolean parFlag) {
		if(parString1 == null) return;
		if(this.unicodeFlag || !decodeASCIICodepointsAndValidate(parString1)) {
			super.renderStringAtPos(parString1, parFlag);
		}else {
			renderStringAtPos0(parString1, false);
		}
	}

	private void renderStringAtPos0(String parString1, boolean parFlag) {
		renderEngine.bindTexture(locationFontTexture);
		InstancedFontRenderer.begin();
		
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		
		boolean hasStrike = false;
		
		for (int i = 0; i < parString1.length(); ++i) {
			char c0 = parString1.charAt(i);
			if (c0 == 167 && i + 1 < parString1.length()) {
				int i1 = "0123456789abcdefklmnor".indexOf(Character.toLowerCase(parString1.charAt(i + 1)));
				if (i1 < 16) {
					this.randomStyle = false;
					this.boldStyle = false;
					this.strikethroughStyle = false;
					this.underlineStyle = false;
					this.italicStyle = false;
					if (i1 < 0 || i1 > 15) {
						i1 = 15;
					}
					int j1 = this.colorCode[i1];
					this.textColor = j1 | (this.textColor & 0xFF000000);
				} else if (i1 == 16) {
					this.randomStyle = true;
				} else if (i1 == 17) {
					this.boldStyle = true;
				} else if (i1 == 18) {
					this.strikethroughStyle = true;
				} else if (i1 == 19) {
					this.underlineStyle = true;
				} else if (i1 == 20) {
					this.italicStyle = true;
				} else if (i1 == 21) {
					this.randomStyle = false;
					this.boldStyle = false;
					this.strikethroughStyle = false;
					this.underlineStyle = false;
					this.italicStyle = false;
					this.textColor = ((int) (this.alpha * 255.0f) << 24) | ((int) (this.red * 255.0f) << 16)
							| ((int) (this.green * 255.0f) << 8) | (int) (this.blue * 255.0f);
				}

				++i;
			} else {
				int j = temporaryCodepointArray[i];
				
				if (this.randomStyle && j != -1) {
					int k = this.getCharWidth(c0);
					String chars = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";

					char c1;
					while (true) {
						j = this.fontRandom.nextInt(chars.length());
						c1 = chars.charAt(j);
						if (k == this.getCharWidth(c1)) {
							break;
						}
					}

					c0 = c1;
				}

				float f = this.appendCharToBuffer(j, this.textColor, this.boldStyle, this.italicStyle);

				if (this.strikethroughStyle) {
					hasStrike = true;
					worldrenderer.pos((double) this.posX, (double) (this.posY + (float) (this.FONT_HEIGHT / 2)), 0.0D)
							.endVertex();
					worldrenderer
							.pos((double) (this.posX + f), (double) (this.posY + (float) (this.FONT_HEIGHT / 2)), 0.0D)
							.endVertex();
					worldrenderer.pos((double) (this.posX + f),
							(double) (this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();
					worldrenderer
							.pos((double) this.posX, (double) (this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F), 0.0D)
							.endVertex();
					worldrenderer.putColor4(this.textColor);
				}

				if (this.underlineStyle) {
					hasStrike = true;
					int l = this.underlineStyle ? -1 : 0;
					worldrenderer.pos((double) (this.posX + (float) l),
							(double) (this.posY + (float) this.FONT_HEIGHT), 0.0D).endVertex();
					worldrenderer.pos((double) (this.posX + f), (double) (this.posY + (float) this.FONT_HEIGHT), 0.0D)
							.endVertex();
					worldrenderer
							.pos((double) (this.posX + f), (double) (this.posY + (float) this.FONT_HEIGHT - 1.0F), 0.0D)
							.endVertex();
					worldrenderer.pos((double) (this.posX + (float) l),
							(double) (this.posY + (float) this.FONT_HEIGHT - 1.0F), 0.0D).endVertex();
					worldrenderer.putColor4(this.textColor);
				}

				this.posX += (float) ((int) f);
			}
		}
		
		float texScale = 0.0625f;
		
		if(!hasStrike) {
			worldrenderer.finishDrawing();
		}
		
		if(parFlag) {
			if(hasStrike) {
				GlStateManager.color(0.25f, 0.25f, 0.25f, 1.0f);
				GlStateManager.translate(1.0f, 1.0f, 0.0f);
				tessellator.draw();
				GlStateManager.translate(-1.0f, -1.0f, 0.0f);
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				InstancedFontRenderer.render(8, 8, texScale, texScale, true);
				EaglercraftGPU.renderAgain();
			}else {
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				InstancedFontRenderer.render(8, 8, texScale, texScale, true);
			}
		}else {
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			if(hasStrike) {
				tessellator.draw();
			}
			InstancedFontRenderer.render(8, 8, texScale, texScale, false);
		}
		
		if(parFlag) {
			this.posX += 1.0f;
		}
	}

	private float appendCharToBuffer(int parInt1, int color, boolean boldStyle, boolean italicStyle) {
		if (parInt1 == 32) {
			return 4.0f;
		}else {
			int i = parInt1 % 16;
			int j = parInt1 / 16;
			float w = this.charWidth[parInt1];
			if(boldStyle) {
				InstancedFontRenderer.appendBoldQuad((int)this.posX, (int)this.posY, i, j, color, italicStyle);
				++w;
			}else {
				InstancedFontRenderer.appendQuad((int)this.posX, (int)this.posY, i, j, color, italicStyle);
			}
			return w;
		}
	}

	private boolean decodeASCIICodepointsAndValidate(String str) {
		for(int i = 0, l = str.length(); i < l; ++i) {
			int j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000\u00a7"
				.indexOf(str.charAt(i));
			if(j != -1) {
				temporaryCodepointArray[i] = j;
			}else {
				return false;
			}
		}
		return true;
	}
}
