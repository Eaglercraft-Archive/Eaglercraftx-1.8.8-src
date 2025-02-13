/*
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

package net.lax1dude.eaglercraft.v1_8.minecraft;

import net.minecraft.entity.Entity;

public interface IAcceleratedParticleEngine {

	void begin(float partialTicks);

	void draw(float texCoordWidth, float texCoordHeight);

	void drawParticle(Entity entityIn, int particleIndexX, int particleIndexY,
			int lightMapData, int texSize, float particleSize, float r, float g, float b, float a);

	void drawParticle(Entity entityIn, int particleIndexX, int particleIndexY,
			int lightMapData, int texSize, float particleSize, int rgba);

	void drawParticle(float posX, float posY, float posZ, int particleIndexX, int particleIndexY,
			int lightMapData, int texSize, float particleSize, float r, float g, float b, float a);

	void drawParticle(float posX, float posY, float posZ, int particleIndexX, int particleIndexY,
			int lightMapData, int texSize, float particleSize, int rgba);

}