package net.lax1dude.eaglercraft.v1_8.internal;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

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
public class PlatformAssets {
	
	static URL getDesktopResourceURL(String path) {
		File f = new File("resources", path);
		if(f.isFile()) {
			try {
				return f.toURI().toURL();
			} catch (MalformedURLException e) {
				return null;
			}
		}else {
			return null;
		}
	}
	
	public static final byte[] getResourceBytes(String path) {
		File loadFile = new File("resources", path);
		byte[] ret = new byte[(int) loadFile.length()];
		try(FileInputStream is = new FileInputStream(loadFile)) {
			int i, j = 0;
			while(j < ret.length && (i = is.read(ret, j, ret.length - j)) != -1) {
				j += i;
			}
			return ret;
		}catch(IOException ex) {
			return null;
		}
	}
	
	public static final ImageData loadImageFile(InputStream data) {
		try {
			BufferedImage img = ImageIO.read(data);
			if(img == null) {
				throw new IOException("Data is not a supported image format!");
			}
			int w = img.getWidth();
			int h = img.getHeight();
			boolean a = img.getColorModel().hasAlpha();
			int[] pixels = new int[w * h];
			img.getRGB(0, 0, w, h, pixels, 0, w);
			for(int i = 0; i < pixels.length; ++i) {
				int j = pixels[i];
				if(!a) {
					j = j | 0xFF000000;
				}
				pixels[i] = (j & 0xFF00FF00) | ((j & 0x00FF0000) >>> 16) |
						((j & 0x000000FF) << 16);
			}
			return new ImageData(w, h, pixels, a);
		}catch(IOException ex) {
			return null;
		}
	}
	
	public static final ImageData loadImageFile(byte[] data) {
		return loadImageFile(new EaglerInputStream(data));
	}
	
}
