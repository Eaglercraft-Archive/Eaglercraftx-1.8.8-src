/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.util.PacketImageData;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.util.SkinPacketHelper;

public class ImageDataLoader {

	public static PacketImageData loadPacketImageData(File img) throws IOException {
		return loadPacketImageData(ImageIO.read(img), 255, 255);
	}

	public static PacketImageData loadPacketImageData(File img, int maxWidth, int maxHeight) throws IOException {
		return loadPacketImageData(ImageIO.read(img), maxWidth, maxHeight);
	}

	public static PacketImageData loadPacketImageData(BufferedImage img) {
		return loadPacketImageData(img, 255, 255);
	}

	public static PacketImageData loadPacketImageData(BufferedImage img, int maxWidth, int maxHeight) {
		int w = img.getWidth();
		int h = img.getHeight();
		if(w > maxWidth || h > maxHeight) {
			float aspectRatio = (float)w / (float)h;
			int nw, nh;
			if(aspectRatio >= 1.0f) {
				nw = (int)(maxWidth / aspectRatio);
				nh = maxHeight;
			}else {
				nw = maxWidth;
				nh = (int)(maxHeight * aspectRatio);
			}
			BufferedImage resized = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) resized.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g.setBackground(new Color(0, true));
			g.clearRect(0, 0, nw, nh);
			g.drawImage(img, 0, 0, nw, nh, 0, 0, w, h, null);
			g.dispose();
			img = resized;
		}
		int[] pixels = new int[w * h];
		img.getRGB(0, 0, w, h, pixels, 0, w);
		return new PacketImageData(w, h, pixels);
	}

	public static byte[] loadCustomSkin(File texture64x64) throws IOException {
		return SkinPacketHelper.loadCustomSkin(texture64x64);
	}

	public static byte[] loadCustomSkin(InputStream texture64x64) throws IOException {
		return SkinPacketHelper.loadCustomSkin(texture64x64);
	}

	public static byte[] loadCustomSkin(BufferedImage texture64x64) {
		return SkinPacketHelper.loadCustomSkin(texture64x64);
	}

	public static byte[] loadCustomCape(File textureNx64) throws IOException {
		return SkinPacketHelper.loadCustomCape(textureNx64);
	}

	public static byte[] loadCustomCape(InputStream textureNx64) throws IOException {
		return SkinPacketHelper.loadCustomCape(textureNx64);
	}

	public static byte[] loadCustomCape(BufferedImage textureNx64) {
		return SkinPacketHelper.loadCustomCape(textureNx64);
	}

}