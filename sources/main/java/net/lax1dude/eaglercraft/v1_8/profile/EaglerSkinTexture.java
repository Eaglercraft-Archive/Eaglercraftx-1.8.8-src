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

import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

public class EaglerSkinTexture implements ITextureObject {

	private final int[] pixels;
	private final int width;
	private final int height;

	private int textureId = -1;

	public EaglerSkinTexture(int[] pixels, int width, int height) {
		if(pixels.length != width * height) {
			throw new IllegalArgumentException("Wrong data length " + pixels.length * 4 + "  for " + width + "x" + height + " texture");
		}
		this.pixels = pixels;
		this.width = width;
		this.height = height;
	}

	public EaglerSkinTexture(byte[] pixels, int width, int height) {
		if(pixels.length != width * height * 4) {
			throw new IllegalArgumentException("Wrong data length " + pixels.length + "  for " + width + "x" + height + " texture");
		}
		this.pixels = convertToInt(pixels);
		this.width = width;
		this.height = height;
	}

	public static int[] convertToInt(byte[] pixels) {
		int[] p = new int[pixels.length >> 2];
		for(int i = 0, j; i < p.length; ++i) {
			j = i << 2;
			p[i] = (((int) pixels[j] & 0xFF) << 24) | (((int) pixels[j + 1] & 0xFF) << 16)
					| (((int) pixels[j + 2] & 0xFF) << 8) | ((int) pixels[j + 3] & 0xFF);
		}
		return p;
	}

	public void copyPixelsIn(byte[] pixels) {
		copyPixelsIn(convertToInt(pixels));
	}

	public void copyPixelsIn(int[] pixels) {
		if(this.pixels.length != pixels.length) {
			throw new IllegalArgumentException("Tried to copy " + pixels.length + " pixels into a " + this.pixels.length + " pixel texture");
		}
		System.arraycopy(pixels, 0, this.pixels, 0, pixels.length);
		if(textureId != -1) {
			TextureUtil.uploadTextureImageSub(textureId, new ImageData(width, height, pixels, true), 0, 0, false, false);
		}
	}

	@Override
	public void loadTexture(IResourceManager var1) throws IOException {
		if(textureId == -1) {
			textureId = GlStateManager.generateTexture();
			TextureUtil.uploadTextureImageAllocate(textureId, new ImageData(width, height, pixels, true), false, false);
		}
	}

	@Override
	public int getGlTextureId() {
		return textureId;
	}

	@Override
	public void setBlurMipmap(boolean var1, boolean var2) {
		// no
	}

	@Override
	public void restoreLastBlurMipmap() {
		// no
	}
	
	public void free() {
		GlStateManager.deleteTexture(textureId);
		textureId = -1;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int[] getData() {
		return pixels;
	}

}