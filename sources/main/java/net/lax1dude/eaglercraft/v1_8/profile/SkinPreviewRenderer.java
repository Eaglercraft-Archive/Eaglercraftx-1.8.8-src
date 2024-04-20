package net.lax1dude.eaglercraft.v1_8.profile;

import net.lax1dude.eaglercraft.v1_8.opengl.EaglerMeshLoader;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

/**
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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
public class SkinPreviewRenderer {

	private static ModelPlayer playerModelSteve = null;
	private static ModelPlayer playerModelAlex = null;
	private static ModelZombie playerModelZombie = null;
	
	public static void initialize() {
		playerModelSteve = new ModelPlayer(0.0f, false);
		playerModelSteve.isChild = false;
		playerModelAlex = new ModelPlayer(0.0f, true);
		playerModelAlex.isChild = false;
		playerModelZombie = new ModelZombie(0.0f, true);
		playerModelZombie.isChild = false;
	}

	public static void renderPreview(int x, int y, int mx, int my, SkinModel skinModel) {
		renderPreview(x, y, mx, my, false, skinModel, null, null);
	}

	public static void renderPreview(int x, int y, int mx, int my, boolean capeMode, SkinModel skinModel, ResourceLocation skinTexture, ResourceLocation capeTexture) {
		ModelBiped model;
		switch(skinModel) {
		case STEVE:
		default:
			model = playerModelSteve;
			break;
		case ALEX:
			model = playerModelAlex;
			break;
		case ZOMBIE:
			model = playerModelZombie;
			break;
		case LONG_ARMS:
		case WEIRD_CLIMBER_DUDE:
		case LAXATIVE_DUDE:
		case BABY_CHARLES:
		case BABY_WINSTON:
			if(skinModel.highPoly != null && Minecraft.getMinecraft().gameSettings.enableFNAWSkins) {
				renderHighPoly(x, y, mx, my, skinModel.highPoly);
				return;
			}
			model = playerModelSteve;
			break;
		}
		
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.disableCull();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y - 80.0f, 100.0f);
		GlStateManager.scale(50.0f, 50.0f, 50.0f);
		GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
		GlStateManager.scale(1.0f, -1.0f, 1.0f);
		
		RenderHelper.enableGUIStandardItemLighting();
		
		GlStateManager.translate(0.0f, 1.0f, 0.0f);
		if(capeMode) {
			GlStateManager.rotate(140.0f, 0.0f, 1.0f, 0.0f);
			mx = x - (x - mx) - 20;
			GlStateManager.rotate(((y - my) * -0.02f), 1.0f, 0.0f, 0.0f);
		}else {
			GlStateManager.rotate(((y - my) * -0.06f), 1.0f, 0.0f, 0.0f);
		}
		GlStateManager.rotate(((x - mx) * 0.06f), 0.0f, 1.0f, 0.0f);
		GlStateManager.translate(0.0f, -1.0f, 0.0f);
		
		if(skinTexture != null) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(skinTexture);
		}
		
		model.render(null, 0.0f, 0.0f, (float)(System.currentTimeMillis() % 2000000) / 50f, ((x - mx) * 0.06f), ((y - my) * -0.1f), 0.0625f);
		
		if(capeTexture != null && model instanceof ModelPlayer) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(capeTexture);
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 0.0F, 0.125F);
			GlStateManager.rotate(6.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			((ModelPlayer)model).renderCape(0.0625f);
			GlStateManager.popMatrix();
		}
		
		GlStateManager.popMatrix();
		GlStateManager.disableLighting();
	}

	private static void renderHighPoly(int x, int y, int mx, int my, HighPolySkin msh) {
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.disableCull();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y - 80.0f, 100.0f);
		GlStateManager.scale(50.0f, 50.0f, 50.0f);
		GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
		GlStateManager.scale(1.0f, -1.0f, 1.0f);
		
		RenderHelper.enableGUIStandardItemLighting();
		
		GlStateManager.translate(0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(((y - my) * -0.06f), 1.0f, 0.0f, 0.0f);
		GlStateManager.rotate(((x - mx) * 0.06f), 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.translate(0.0f, -0.6f, 0.0f);
		
		GlStateManager.scale(HighPolySkin.highPolyScale, HighPolySkin.highPolyScale, HighPolySkin.highPolyScale);
		Minecraft.getMinecraft().getTextureManager().bindTexture(msh.texture);
		
		if(msh.bodyModel != null) {
			EaglercraftGPU.drawHighPoly(EaglerMeshLoader.getEaglerMesh(msh.bodyModel));
		}
		
		if(msh.headModel != null) {
			EaglercraftGPU.drawHighPoly(EaglerMeshLoader.getEaglerMesh(msh.headModel));
		}
		
		if(msh.limbsModel != null && msh.limbsModel.length > 0) {
			for(int i = 0; i < msh.limbsModel.length; ++i) {
				float offset = 0.0f;
				if(msh.limbsOffset != null) {
					if(msh.limbsOffset.length == 1) {
						offset = msh.limbsOffset[0];
					}else {
						offset = msh.limbsOffset[i];
					}
				}
				if(offset != 0.0f || msh.limbsInitialRotation != 0.0f) {
					GlStateManager.pushMatrix();
					if(offset != 0.0f) {
						GlStateManager.translate(0.0f, offset, 0.0f);
					}
					if(msh.limbsInitialRotation != 0.0f) {
						GlStateManager.rotate(msh.limbsInitialRotation, 1.0f, 0.0f, 0.0f);
					}
				}
				
				EaglercraftGPU.drawHighPoly(EaglerMeshLoader.getEaglerMesh(msh.limbsModel[i]));
				
				if(offset != 0.0f || msh.limbsInitialRotation != 0.0f) {
					GlStateManager.popMatrix();
				}
			}
		}

		GlStateManager.popMatrix();
		GlStateManager.disableLighting();
	}

}
