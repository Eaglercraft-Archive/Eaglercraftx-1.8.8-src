/*
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

package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TextureCompassPBRImpl extends EaglerTextureAtlasSpritePBR {
	public double currentAngle;
	public double angleDelta;

	public TextureCompassPBRImpl(String spriteName) {
		super(spriteName);
	}

	public void updateAnimationPBR() {
		Minecraft minecraft = Minecraft.getMinecraft();
		if (minecraft.theWorld != null && minecraft.thePlayer != null) {
			this.updateCompassPBR(minecraft.theWorld, minecraft.thePlayer.posX, minecraft.thePlayer.posZ,
					(double) minecraft.thePlayer.rotationYaw, false);
		} else {
			this.updateCompassPBR((World) null, 0.0, 0.0, 0.0, true);
		}
	}

	public void updateCompassPBR(World worldIn, double playerX, double playerY, double playerZ, boolean noWorld) {
		if (!this.frameTextureDataPBR[0].isEmpty()) {
			double d0 = 0.0;
			if (worldIn != null && !noWorld) {
				BlockPos blockpos = worldIn.getSpawnPoint();
				double d1 = (double) blockpos.getX() - playerX;
				double d2 = (double) blockpos.getZ() - playerY;
				playerZ = playerZ % 360.0;
				d0 = -((playerZ - 90.0) * Math.PI / 180.0 - Math.atan2(d2, d1));
				if (!worldIn.provider.isSurfaceWorld()) {
					d0 = Math.random() * Math.PI * 2.0;
				}
			}

			double d3;
			for (d3 = d0 - this.currentAngle; d3 < -Math.PI; d3 += Math.PI * 2.0) {
				;
			}

			while (d3 >= Math.PI) {
				d3 -= Math.PI * 2.0;
			}

			d3 = MathHelper.clamp_double(d3, -1.0, 1.0);
			this.angleDelta += d3 * 0.1;
			this.angleDelta *= 0.8;
			this.currentAngle += this.angleDelta;

			int i, frameCount = this.frameTextureDataPBR[0].size();
			for (i = (int) ((this.currentAngle / Math.PI * 0.5 + 1.0) * frameCount)
					% frameCount; i < 0; i = (i + frameCount) % frameCount) {
				;
			}

			if (i != this.frameCounter) {
				this.frameCounter = i;
				currentAnimUpdater = (mapWidth, mapHeight, mapLevel) -> {
					animationCachePBR[0].copyFrameToTex2D(this.frameCounter, mapLevel, this.originX >> mapLevel,
							this.originY >> mapLevel, this.width >> mapLevel, this.height >> mapLevel, mapWidth,
							mapHeight);
				};
				if(!dontAnimateNormals || !dontAnimateMaterial) {
					currentAnimUpdaterPBR = (mapWidth, mapHeight, mapLevel) -> {
						if (!dontAnimateNormals)
							animationCachePBR[1].copyFrameToTex2D(this.frameCounter, mapLevel, this.originX >> mapLevel,
									this.originY >> mapLevel, this.width >> mapLevel, this.height >> mapLevel, mapWidth,
									mapHeight);
						if (!dontAnimateMaterial)
							animationCachePBR[2].copyFrameToTex2D(this.frameCounter, mapLevel, this.originX >> mapLevel,
									(this.originY >> mapLevel) + (mapHeight >> 1), this.width >> mapLevel,
									this.height >> mapLevel, mapWidth, mapHeight);
					};
				}else {
					currentAnimUpdaterPBR = null;
				}
			}else {
				currentAnimUpdater = null;
				currentAnimUpdaterPBR = null;
			}

		}else {
			currentAnimUpdater = null;
			currentAnimUpdaterPBR = null;
		}
	}

}