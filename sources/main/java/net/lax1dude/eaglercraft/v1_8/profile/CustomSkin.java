/*
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

package net.lax1dude.eaglercraft.v1_8.profile;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class CustomSkin {

	public final String name;
	public final byte[] texture;
	public SkinModel model;

	private EaglerSkinTexture textureInstance;
	private ResourceLocation resourceLocation;

	private static int texId = 0;

	public CustomSkin(String name, byte[] texture, SkinModel model) {
		this.name = name;
		this.texture = texture;
		this.model = model;
		this.textureInstance = new EaglerSkinTexture(texture, model.width, model.height);
		this.resourceLocation = null;
	}
	
	public void load() {
		if(resourceLocation == null) {
			resourceLocation = new ResourceLocation("eagler:skins/custom/tex_" + texId++);
			Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, textureInstance);
		}
	}
	
	public ResourceLocation getResource() {
		return resourceLocation;
	}
	
	public void delete() {
		if(resourceLocation != null) {
			Minecraft.getMinecraft().getTextureManager().deleteTexture(resourceLocation);
			resourceLocation = null;
		}
	}

}