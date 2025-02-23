/*
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.minecraft;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.BitSet;

import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.BetterFrustum;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class EaglerCloudRenderer {

	private static final ResourceLocation locationCloudsPNG = new ResourceLocation("textures/environment/clouds.png");

	private final Minecraft mc;

	private int renderList = -1;

	private static final int RENDER_STATE_FAST = 0;
	private static final int RENDER_STATE_FANCY_BELOW = 1;
	private static final int RENDER_STATE_FANCY_INSIDE = 2;
	private static final int RENDER_STATE_FANCY_ABOVE = 3;

	private int currentRenderState = -1;

	private int[] renderListFancy = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1 };

	// 1.8 seems to use a different projection matrix for clouds
	private final Matrix4f fancyCloudProjView = new Matrix4f();
	private final BetterFrustum frustum = new BetterFrustum();

	private final BitSet visibleCloudParts = new BitSet();

	public EaglerCloudRenderer(Minecraft mc) {
		this.mc = mc;
	}

	public void renderClouds(float partialTicks, int pass) {
		if (!this.mc.theWorld.provider.isSurfaceWorld()) {
			return;
		}
		
		int c = mc.gameSettings.func_181147_e();
		if(c == 0) {
			return;
		}
		
		int newState;
		Entity rve = this.mc.getRenderViewEntity();
		float f = (float) (rve.lastTickPosY + (rve.posY - rve.lastTickPosY) * (double) partialTicks);
		float f3 = this.mc.theWorld.provider.getCloudHeight() - f + 0.33F;
		if(c == 2) {
			if (f3 > -5.0F) {
				if (f3 <= 5.0F) {
					newState = RENDER_STATE_FANCY_INSIDE;
				}else {
					newState = RENDER_STATE_FANCY_ABOVE;
				}
			}else {
				newState = RENDER_STATE_FANCY_BELOW;
			}
		}else {
			newState = RENDER_STATE_FAST;
		}
		
		if(newState != currentRenderState) {
			rebuild(newState);
			currentRenderState = newState;
		}
		
		mc.getTextureManager().bindTexture(locationCloudsPNG);
		
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		
		Vec3 vec3 = mc.theWorld.getCloudColour(partialTicks);
		float _f1 = (float) vec3.xCoord;
		float _f2 = (float) vec3.yCoord;
		float _f3 = (float) vec3.zCoord;
		if (pass != 2) {
			float _f4 = (_f1 * 30.0F + _f2 * 59.0F + _f3 * 11.0F) / 100.0F;
			float _f5 = (_f1 * 30.0F + _f2 * 70.0F) / 100.0F;
			float _f6 = (_f1 * 30.0F + _f3 * 70.0F) / 100.0F;
			_f1 = _f4;
			_f2 = _f5;
			_f3 = _f6;
		}
		
		if(newState != RENDER_STATE_FAST) {
			GlStateManager.enableCull();
			double d0 = this.mc.renderGlobal.getCloudCounter(partialTicks);
			double d1 = (rve.prevPosX + (rve.posX - rve.prevPosX) * (double) partialTicks + d0 * 0.029999999329447746D) / 12.0D;
			double d2 = (rve.prevPosZ + (rve.posZ - rve.prevPosZ) * (double) partialTicks) / 12.0D + 0.33000001311302185D;
			int i = MathHelper.floor_double(d1 / 2048.0D);
			int j = MathHelper.floor_double(d2 / 2048.0D);
			d1 = d1 - (double) (i * 2048);
			d2 = d2 - (double) (j * 2048);
			float f17 = (float) MathHelper.floor_double(d1) * 0.00390625F;
			float f18 = (float) MathHelper.floor_double(d2) * 0.00390625F;
			float f19 = (float) (d1 - (double) MathHelper.floor_double(d1));
			float f20 = (float) (d2 - (double) MathHelper.floor_double(d2));

			GlStateManager.pushMatrix();
			GlStateManager.scale(12.0F, 1.0F, 12.0F);

			Matrix4f.mul(GlStateManager.getProjectionReference(), GlStateManager.getModelViewReference(), fancyCloudProjView);
			frustum.set(fancyCloudProjView);

			visibleCloudParts.clear();

			for (int k = 0; k < 2; ++k) {
				if (k == 0) {
					GlStateManager.colorMask(false, false, false, false);
				} else {
					switch (pass) {
					case 0:
						GlStateManager.colorMask(false, true, true, true);
						break;
					case 1:
						GlStateManager.colorMask(true, false, false, true);
						break;
					case 2:
						GlStateManager.colorMask(true, true, true, true);
					}
				}

				int j1;
				for (int l = -3; l <= 3; ++l) {
					for (int i1 = -3; i1 <= 3; ++i1) {
						float f22 = (float) (l * 8);
						float f23 = (float) (i1 * 8);
						float f24 = f22 - f19;
						float f25 = f23 - f20;

						j1 = (l + 3) * 7 + i1 + 3;
						if(k == 0) {
							if(frustum.testAab(f24, f3, f25, (f24 + 8.0f), f3 + 4.0f, (f25 + 8.0f))) {
								visibleCloudParts.set(j1);
							}else {
								continue;
							}
						}else {
							if(!visibleCloudParts.get(j1)) {
								continue;
							}
						}

						GlStateManager.pushMatrix();
						GlStateManager.translate(f24, f3, f25);
						if(k != 0) {
							GlStateManager.color(_f1, _f2, _f3, 0.8f);
						}
						GlStateManager.matrixMode(GL_TEXTURE);
						GlStateManager.pushMatrix();
						GlStateManager.translate(f22 * 0.00390625F + f17, f23 * 0.00390625F + f18, 0.0f);

						int xx = 0;
						int yy = 0;
						if (l <= -1) {
							xx = -1;
						}else if (l >= 1) {
							xx = 1;
						}

						if (i1 <= -1) {
							yy = -1;
						}else if (i1 >= 1) {
							yy = 1;
						}
						
						GlStateManager.callList(renderListFancy[(yy + 1) * 3 + xx + 1]);
						
						GlStateManager.popMatrix();
						GlStateManager.matrixMode(GL_MODELVIEW);
						GlStateManager.popMatrix();
					}
				}
			}

			GlStateManager.popMatrix();
		}else {
			GlStateManager.disableCull();
			double d2 = this.mc.renderGlobal.getCloudCounter(partialTicks);
			double d0 = rve.prevPosX + (rve.posX - rve.prevPosX) * (double) partialTicks + d2 * 0.029999999329447746D;
			double d1 = rve.prevPosZ + (rve.posZ - rve.prevPosZ) * (double) partialTicks;
			int i = MathHelper.floor_double(d0 / 2048.0D);
			int j = MathHelper.floor_double(d1 / 2048.0D);
			d0 = d0 - (double) (i * 2048);
			d1 = d1 - (double) (j * 2048);
			float f8 = (float) (d0 * 4.8828125E-4D);
			float f9 = (float) (d1 * 4.8828125E-4D);
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0f, f3, 0.0f);
			GlStateManager.color(_f1, _f2, _f3, 0.8f);
			GlStateManager.matrixMode(GL_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate(f8, f9, 0.0f);
			GlStateManager.callList(renderList);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL_MODELVIEW);
			GlStateManager.popMatrix();
			GlStateManager.enableCull();
		}
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
	}

	private void rebuild(int newState) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		if(newState != RENDER_STATE_FAST) {
			if(renderList != -1) {
				EaglercraftGPU.glDeleteLists(renderList);
				renderList = -1;
			}
			for(int i = 0; i < renderListFancy.length; ++i) {
				if(renderListFancy[i] == -1) {
					renderListFancy[i] = EaglercraftGPU.glGenLists();
				}
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				generateFancyClouds(worldrenderer, i, newState != RENDER_STATE_FANCY_BELOW, newState != RENDER_STATE_FANCY_ABOVE);
				tessellator.uploadDisplayList(renderListFancy[i]);
			}
		}else {
			if(renderList == -1) {
				renderList = EaglercraftGPU.glGenLists();
			}
			for(int i = 0; i < renderListFancy.length; ++i) {
				if(renderListFancy[i] != -1) {
					EaglercraftGPU.glDeleteLists(renderListFancy[i]);
					renderListFancy[i] = -1;
				}
			}
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
			final double d = 4.8828125E-4;
			worldrenderer.pos(-256.0f, 0.0f, 256.0f).tex(-256.0f * d, 256.0f * d).endVertex();
			worldrenderer.pos(256.0f, 0.0f, 256.0f).tex(256.0f * d, 256.0f * d).endVertex();
			worldrenderer.pos(256.0f, 0.0f, -256.0f).tex(256.0f * d, -256.0f * d).endVertex();
			worldrenderer.pos(-256.0f, 0.0f, -256.0f).tex(-256.0f * d, -256.0f * d).endVertex();
			tessellator.uploadDisplayList(renderList);
		}
	}

	private static void generateFancyClouds(WorldRenderer worldrenderer, int mesh, boolean renderAbove, boolean renderBelow) {
		int xx = (mesh % 3) - 1;
		int yy = (mesh / 3) - 1;
		boolean center = xx == 0 && yy == 0 && renderAbove && renderBelow;

		if (renderAbove) {
			worldrenderer.pos(0.0f, 0.0f, 8.0f).tex(0.0f, 8.0f * 0.00390625F).color(0.7f, 0.7f, 0.7f, 1.0f).endVertex();
			worldrenderer.pos(0.0f, 0.0f, 0.0f).tex(0.0f, 0.0f).color(0.7f, 0.7f, 0.7f, 1.0f).endVertex();
			worldrenderer.pos(8.0f, 0.0f, 0.0f).tex(8.0f * 0.00390625f, 0.0f).color(0.7f, 0.7f, 0.7f, 1.0f).endVertex();
			worldrenderer.pos(8.0f, 0.0f, 8.0f).tex(8.0f * 0.00390625f, 8.0f * 0.00390625f).color(0.7f, 0.7f, 0.7f, 1.0f).endVertex();
			if(center) {
				worldrenderer.pos(0.0f, 0.0f, 8.0f).tex(0.0f, 8.0f * 0.00390625F).color(0.7f, 0.7f, 0.7f, 1.0f).endVertex();
				worldrenderer.pos(8.0f, 0.0f, 8.0f).tex(8.0f * 0.00390625f, 8.0f * 0.00390625f).color(0.7f, 0.7f, 0.7f, 1.0f).endVertex();
				worldrenderer.pos(8.0f, 0.0f, 0.0f).tex(8.0f * 0.00390625f, 0.0f).color(0.7f, 0.7f, 0.7f, 1.0f).endVertex();
				worldrenderer.pos(0.0f, 0.0f, 0.0f).tex(0.0f, 0.0f).color(0.7f, 0.7f, 0.7f, 1.0f).endVertex();
			}
		}

		if (renderBelow) {
			worldrenderer.pos(0.0f, 4.0f - 9.765625E-4f, 8.0f).tex(0.0f, 8.0f * 0.00390625f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
			worldrenderer.pos(8.0f, 4.0f - 9.765625E-4f, 8.0f).tex(8.0f * 0.00390625F, 8.0f * 0.00390625f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
			worldrenderer.pos(8.0f, 4.0f - 9.765625E-4f, 0.0f).tex(8.0f * 0.00390625f, 0.0f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
			worldrenderer.pos(0.0f, 4.0f - 9.765625E-4f, 0.0f).tex(0.0f, 0.0f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
			if(center) {
				worldrenderer.pos(0.0f, 4.0f - 9.765625E-4f, 8.0f).tex(0.0f, 8.0f * 0.00390625f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
				worldrenderer.pos(0.0f, 4.0f - 9.765625E-4f, 0.0f).tex(0.0f, 0.0f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
				worldrenderer.pos(8.0f, 4.0f - 9.765625E-4f, 0.0f).tex(8.0f * 0.00390625f, 0.0f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
				worldrenderer.pos(8.0f, 4.0f - 9.765625E-4f, 8.0f).tex(8.0f * 0.00390625F, 8.0f * 0.00390625f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
			}
		}

		if (xx != -1) {
			for (int j1 = 0; j1 < 8; ++j1) {
				worldrenderer.pos(j1, 0.0f, 8.0f).tex((j1 + 0.5f) * 0.00390625f, 8.0f * 0.00390625f)
						.color(0.9f, 0.9f, 0.9f, 1.0f).endVertex();
				worldrenderer.pos(j1, 4.0f, 8.0f).tex((j1 + 0.5f) * 0.00390625f, 8.0f * 0.00390625f)
						.color(0.9f, 0.9f, 0.9f, 1.0f).endVertex();
				worldrenderer.pos(j1, 4.0f, 0.0f).tex((j1 + 0.5f) * 0.00390625f, 0.0f).color(0.9f, 0.9f, 0.9f, 1.0f)
						.endVertex();
				worldrenderer.pos(j1, 0.0f, 0.0f).tex((j1 + 0.5f) * 0.00390625f, 0.0f).color(0.9f, 0.9f, 0.9f, 1.0f)
						.endVertex();
				if(center) {
					worldrenderer.pos(j1, 0.0f, 8.0f).tex((j1 + 0.5f) * 0.00390625f, 8.0f * 0.00390625f)
							.color(0.9f, 0.9f, 0.9f, 1.0f).endVertex();
					worldrenderer.pos(j1, 0.0f, 0.0f).tex((j1 + 0.5f) * 0.00390625f, 0.0f).color(0.9f, 0.9f, 0.9f, 1.0f)
							.endVertex();
					worldrenderer.pos(j1, 4.0f, 0.0f).tex((j1 + 0.5f) * 0.00390625f, 0.0f).color(0.9f, 0.9f, 0.9f, 1.0f)
							.endVertex();
					worldrenderer.pos(j1, 4.0f, 8.0f).tex((j1 + 0.5f) * 0.00390625f, 8.0f * 0.00390625f)
							.color(0.9f, 0.9f, 0.9f, 1.0f).endVertex();
				}
			}
		}

		if (xx != 1) {
			for (int k1 = 0; k1 < 8; ++k1) {
				worldrenderer.pos(k1 + 1.0f - 9.765625E-4f, 0.0f, 8.0f)
						.tex((k1 + 0.5f) * 0.00390625f, 8.0f * 0.00390625f).color(0.9f, 0.9f, 0.9f, 1.0f).endVertex();
				worldrenderer.pos(k1 + 1.0f - 9.765625E-4f, 0.0f, 0.0f).tex((k1 + 0.5f) * 0.00390625f, 0.0f)
						.color(0.9f, 0.9f, 0.9f, 1.0f).endVertex();
				worldrenderer.pos(k1 + 1.0f - 9.765625E-4f, 4.0f, 0.0f).tex((k1 + 0.5f) * 0.00390625f, 0.0f)
						.color(0.9f, 0.9f, 0.9f, 1.0f).endVertex();
				worldrenderer.pos(k1 + 1.0f - 9.765625E-4f, 4.0f, 8.0f)
						.tex((k1 + 0.5f) * 0.00390625f, 8.0f * 0.00390625f).color(0.9f, 0.9f, 0.9f, 1.0f).endVertex();
				if(center) {
					worldrenderer.pos(k1 + 1.0f - 9.765625E-4f, 0.0f, 8.0f)
							.tex((k1 + 0.5f) * 0.00390625f, 8.0f * 0.00390625f).color(0.9f, 0.9f, 0.9f, 1.0f).endVertex();
					worldrenderer.pos(k1 + 1.0f - 9.765625E-4f, 4.0f, 8.0f)
							.tex((k1 + 0.5f) * 0.00390625f, 8.0f * 0.00390625f).color(0.9f, 0.9f, 0.9f, 1.0f).endVertex();
					worldrenderer.pos(k1 + 1.0f - 9.765625E-4f, 4.0f, 0.0f).tex((k1 + 0.5f) * 0.00390625f, 0.0f)
							.color(0.9f, 0.9f, 0.9f, 1.0f).endVertex();
					worldrenderer.pos(k1 + 1.0f - 9.765625E-4f, 0.0f, 0.0f).tex((k1 + 0.5f) * 0.00390625f, 0.0f)
							.color(0.9f, 0.9f, 0.9f, 1.0f).endVertex();
				}
			}
		}

		if (yy != -1) {
			for (int l1 = 0; l1 < 8; ++l1) {
				worldrenderer.pos(0.0f, 4.0f, l1).tex(0.0f, (l1 + 0.5f) * 0.00390625f).color(0.8f, 0.8f, 0.8f, 1.0f)
						.endVertex();
				worldrenderer.pos(8.0f, 4.0f, l1).tex(8.0f * 0.00390625f, (l1 + 0.5f) * 0.00390625f)
						.color(0.8f, 0.8f, 0.8f, 1.0f).endVertex();
				worldrenderer.pos(8.0f, 0.0f, l1).tex(8.0f * 0.00390625f, (l1 + 0.5f) * 0.00390625f)
						.color(0.8f, 0.8f, 0.8f, 1.0f).endVertex();
				worldrenderer.pos(0.0f, 0.0f, l1).tex(0.0f, (l1 + 0.5f) * 0.00390625f).color(0.8f, 0.8f, 0.8f, 1.0f)
						.endVertex();
				if(center) {
					worldrenderer.pos(0.0f, 4.0f, l1).tex(0.0f, (l1 + 0.5f) * 0.00390625f).color(0.8f, 0.8f, 0.8f, 1.0f)
							.endVertex();
					worldrenderer.pos(0.0f, 0.0f, l1).tex(0.0f, (l1 + 0.5f) * 0.00390625f).color(0.8f, 0.8f, 0.8f, 1.0f)
							.endVertex();
					worldrenderer.pos(8.0f, 0.0f, l1).tex(8.0f * 0.00390625f, (l1 + 0.5f) * 0.00390625f)
							.color(0.8f, 0.8f, 0.8f, 1.0f).endVertex();
					worldrenderer.pos(8.0f, 4.0f, l1).tex(8.0f * 0.00390625f, (l1 + 0.5f) * 0.00390625f)
							.color(0.8f, 0.8f, 0.8f, 1.0f).endVertex();
				}
			}
		}

		if (yy != 1) {
			for (int i2 = 0; i2 < 8; ++i2) {
				worldrenderer.pos(0.0f, 4.0f, i2 + 1.0f - 9.765625E-4f).tex(0.0f, (i2 + 0.5f) * 0.00390625f)
						.color(0.8f, 0.8f, 0.8f, 1.0f).endVertex();
				worldrenderer.pos(0.0f, 0.0f, i2 + 1.0f - 9.765625E-4f).tex(0.0f, (i2 + 0.5f) * 0.00390625f)
						.color(0.8f, 0.8f, 0.8f, 1.0f).endVertex();
				worldrenderer.pos(8.0f, 0.0f, i2 + 1.0f - 9.765625E-4f)
						.tex(8.0f * 0.00390625f, (i2 + 0.5f) * 0.00390625f).color(0.8f, 0.8f, 0.8f, 1.0f).endVertex();
				worldrenderer.pos(8.0f, 4.0f, i2 + 1.0f - 9.765625E-4f)
						.tex(8.0f * 0.00390625f, (i2 + 0.5f) * 0.00390625f).color(0.8f, 0.8f, 0.8f, 1.0f).endVertex();
				if(center) {
					worldrenderer.pos(0.0f, 4.0f, i2 + 1.0f - 9.765625E-4f).tex(0.0f, (i2 + 0.5f) * 0.00390625f)
							.color(0.8f, 0.8f, 0.8f, 1.0f).endVertex();
					worldrenderer.pos(8.0f, 4.0f, i2 + 1.0f - 9.765625E-4f)
							.tex(8.0f * 0.00390625f, (i2 + 0.5f) * 0.00390625f).color(0.8f, 0.8f, 0.8f, 1.0f).endVertex();
					worldrenderer.pos(8.0f, 0.0f, i2 + 1.0f - 9.765625E-4f)
							.tex(8.0f * 0.00390625f, (i2 + 0.5f) * 0.00390625f).color(0.8f, 0.8f, 0.8f, 1.0f).endVertex();
					worldrenderer.pos(0.0f, 0.0f, i2 + 1.0f - 9.765625E-4f).tex(0.0f, (i2 + 0.5f) * 0.00390625f)
							.color(0.8f, 0.8f, 0.8f, 1.0f).endVertex();
				}
			}
		}
	}

}