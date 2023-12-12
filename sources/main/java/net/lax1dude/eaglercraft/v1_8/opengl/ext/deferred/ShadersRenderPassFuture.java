package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
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
public abstract class ShadersRenderPassFuture {

	public static enum PassType {
		MAIN, SHADOW
	}

	protected float x;
	protected float y;
	protected float z;
	protected float partialTicks;

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public ShadersRenderPassFuture(float x, float y, float z, float partialTicks) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.partialTicks = partialTicks;
	}

	public ShadersRenderPassFuture(Entity e, float partialTicks) {
		this.x = (float)((e.posX - e.prevPosX) * partialTicks + e.prevPosX - TileEntityRendererDispatcher.staticPlayerX);
		this.y = (float)((e.posY - e.prevPosY) * partialTicks + e.prevPosY - TileEntityRendererDispatcher.staticPlayerY);
		this.z = (float)((e.posZ - e.prevPosZ) * partialTicks + e.prevPosZ - TileEntityRendererDispatcher.staticPlayerZ);
	}

	public ShadersRenderPassFuture(Entity e) {
		this(e, EaglerDeferredPipeline.instance.getPartialTicks());
	}

	public abstract void draw(PassType pass);

	private final float[] tmp = new float[1];

	public float[] tmpValue() {
		return tmp;
	}
}
