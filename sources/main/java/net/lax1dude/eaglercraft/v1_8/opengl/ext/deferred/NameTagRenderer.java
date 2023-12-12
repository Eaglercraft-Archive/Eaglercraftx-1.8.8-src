package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

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
public class NameTagRenderer {

	public static boolean doRenderNameTags = false;
	public static final NameTagRenderer[] nameTagsThisFrame = new NameTagRenderer[256];
	public static int nameTagsCount = 0;

	static {
		for(int i = 0; i < nameTagsThisFrame.length; ++i) {
			nameTagsThisFrame[i] = new NameTagRenderer();
		}
	}

	public Entity entityIn;
	public String str;
	public double x;
	public double y;
	public double z;
	public int maxDistance;
	public double dst2;

	public static void renderNameTag(Entity entityIn, String str, double x, double y, double z, int maxDistance) {
		if(!doRenderNameTags || nameTagsCount >= nameTagsThisFrame.length) {
			return;
		}
		NameTagRenderer n = nameTagsThisFrame[nameTagsCount++];
		n.entityIn = entityIn;
		n.str = str;
		n.x = x;
		n.y = y;
		n.z = z;
		n.dst2 = x * x + y * y + z * z;
		n.maxDistance = maxDistance;
	}

}
