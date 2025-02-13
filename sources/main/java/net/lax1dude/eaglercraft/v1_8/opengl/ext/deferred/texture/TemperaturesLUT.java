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

import java.io.IOException;
import java.io.InputStream;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

public class TemperaturesLUT implements IResourceManagerReloadListener {

	private static final Logger logger = LogManager.getLogger("TemperaturesLUT");

	public static final float[][] colorTemperatureLUT = new float[390][3];

	@Override
	public void onResourceManagerReload(IResourceManager var1) {
		try {
			IResource res = var1.getResource(new ResourceLocation("eagler:glsl/deferred/temperatures.lut"));
			try(InputStream is = res.getInputStream()) {
				for(int i = 0; i < 390; ++i) {
					colorTemperatureLUT[i][0] = ((int)is.read() & 0xFF) * 0.0039216f;
					colorTemperatureLUT[i][0] *= colorTemperatureLUT[i][0];
					colorTemperatureLUT[i][1] = ((int)is.read() & 0xFF) * 0.0039216f;
					colorTemperatureLUT[i][1] *= colorTemperatureLUT[i][1];
					colorTemperatureLUT[i][2] = ((int)is.read() & 0xFF) * 0.0039216f;
					colorTemperatureLUT[i][2] *= colorTemperatureLUT[i][2];
				}
			}
		} catch (IOException e) {
			logger.error("Failed to load color temperature lookup table!");
			logger.error(e);
		}
	}

	public static float[] getColorTemperature(int kelvin) {
		if (kelvin < 1000) kelvin = 1000;
		if (kelvin > 39000) kelvin = 39000;
		int k = ((kelvin - 100) / 100);
		return colorTemperatureLUT[k];
	}

	public static void getColorTemperature(int kelvin, float[] ret) {
		if (kelvin < 1000) kelvin = 1000;
		if (kelvin > 39000) kelvin = 39000;
		int k = ((kelvin - 100) / 100);
		ret[0] = colorTemperatureLUT[k][0];
		ret[1] = colorTemperatureLUT[k][1];
		ret[2] = colorTemperatureLUT[k][2];
	}

}