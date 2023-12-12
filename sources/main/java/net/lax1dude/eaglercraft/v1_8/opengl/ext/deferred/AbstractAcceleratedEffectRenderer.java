package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import net.lax1dude.eaglercraft.v1_8.minecraft.IAcceleratedParticleEngine;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;

/**
 * Copyright (c) 2023 lax1dude. All Rights Reserved.
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
public abstract class AbstractAcceleratedEffectRenderer implements IAcceleratedParticleEngine {

	public float partialTicks;

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
		int color = ((int)(a * 255.0f) << 24) | ((int)(r * 255.0f) << 16) | ((int)(g * 255.0f) << 8) | (int)(b * 255.0f);
		drawParticle(posX, posY, posZ, particleIndexX, particleIndexY, lightMapData, texSize, particleSize, color);
	}

}
