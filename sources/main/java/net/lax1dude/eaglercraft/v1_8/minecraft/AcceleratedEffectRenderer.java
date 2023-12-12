package net.lax1dude.eaglercraft.v1_8.minecraft;

import net.lax1dude.eaglercraft.v1_8.opengl.InstancedParticleRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
public class AcceleratedEffectRenderer implements IAcceleratedParticleEngine {

	private float partialTicks;

	private float f1;
	private float f2;
	private float f3;
	private float f4;
	private float f5;

	@Override
	public void begin(float partialTicks) {
		this.partialTicks = partialTicks;
		InstancedParticleRenderer.begin();
		Entity et = Minecraft.getMinecraft().getRenderViewEntity();
		if(et != null) {
			f1 = MathHelper.cos(et.rotationYaw * 0.017453292F);
			f2 = MathHelper.sin(et.rotationYaw * 0.017453292F);
			f3 = -f2 * MathHelper.sin(et.rotationPitch * 0.017453292F);
			f4 = f1 * MathHelper.sin(et.rotationPitch * 0.017453292F);
			f5 = MathHelper.cos(et.rotationPitch * 0.017453292F);
		}
	}

	@Override
	public void draw(float texCoordWidth, float texCoordHeight) {
		InstancedParticleRenderer.render(texCoordWidth, texCoordHeight, 0.0625f, f1, f5, f2, f3, f4);
	}

	@Override
	public void drawParticle(Entity entityIn, int particleIndexX, int particleIndexY, int lightMapData,
			int texSize, float particleSize, float r, float g, float b, float a) {
		float xx = (float) (entityIn.prevPosX + (entityIn.posX - entityIn.prevPosX) * (double) partialTicks - EntityFX.interpPosX);
		float yy = (float) (entityIn.prevPosY + (entityIn.posY - entityIn.prevPosY) * (double) partialTicks - EntityFX.interpPosY);
		float zz = (float) (entityIn.prevPosZ + (entityIn.posZ - entityIn.prevPosZ) * (double) partialTicks - EntityFX.interpPosZ);
		drawParticle(xx, yy, zz, particleIndexX, particleIndexY, lightMapData, texSize, particleSize, r, g, b, a);
	}

	@Override
	public void drawParticle(Entity entityIn, int particleIndexX, int particleIndexY, int lightMapData,
			int texSize, float particleSize, int rgba) {
		float xx = (float) (entityIn.prevPosX + (entityIn.posX - entityIn.prevPosX) * (double) partialTicks - EntityFX.interpPosX);
		float yy = (float) (entityIn.prevPosY + (entityIn.posY - entityIn.prevPosY) * (double) partialTicks - EntityFX.interpPosY);
		float zz = (float) (entityIn.prevPosZ + (entityIn.posZ - entityIn.prevPosZ) * (double) partialTicks - EntityFX.interpPosZ);
		drawParticle(xx, yy, zz, particleIndexX, particleIndexY, lightMapData, texSize, particleSize, rgba);
	}

	@Override
	public void drawParticle(float posX, float posY, float posZ, int particleIndexX, int particleIndexY,
			int lightMapData, int texSize, float particleSize, float r, float g, float b, float a) {
		InstancedParticleRenderer.appendParticle(posX, posY, posZ, particleIndexX, particleIndexY, lightMapData & 0xFF,
				(lightMapData >> 16) & 0xFF, (int)(particleSize * 16.0f), texSize, r, g, b, a);
	}

	@Override
	public void drawParticle(float posX, float posY, float posZ, int particleIndexX, int particleIndexY,
			int lightMapData, int texSize, float particleSize, int rgba) {
		InstancedParticleRenderer.appendParticle(posX, posY, posZ, particleIndexX, particleIndexY, lightMapData & 0xFF,
				(lightMapData >> 16) & 0xFF, (int)(particleSize * 16.0f), texSize, rgba);
	}

}
