package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

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
public class PBRMaterialConstants implements IResourceManagerReloadListener {

	public static final Logger logger = LogManager.getLogger("PBRMaterialConstants");

	public final ResourceLocation resourceLocation;
	public final Map<String,Integer> spriteNameToMaterialConstants = new HashMap();

	public int defaultMaterial = 0x00000A77;

	public PBRMaterialConstants(ResourceLocation resourceLocation) {
		this.resourceLocation = resourceLocation;
	}

	@Override
	public void onResourceManagerReload(IResourceManager var1) {
		try(InputStream is = var1.getResource(resourceLocation).getInputStream()) {
			spriteNameToMaterialConstants.clear();
			BufferedReader bf = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			String line;
			boolean firstLine = true;
			while((line = bf.readLine()) != null) {
				if((line = line.trim()).length() == 0) {
					continue;
				}
				if(firstLine) {
					firstLine = false;
					continue;
				}
				String[] cols = line.split(",");
				if(cols.length == 4)  {
					try {
						int value = Integer.parseInt(cols[1]) | (Integer.parseInt(cols[2]) << 8) | (Integer.parseInt(cols[3]) << 16);
						if(cols[0].equals("default")) {
							defaultMaterial = value;
						}else {
							Integer v = spriteNameToMaterialConstants.get(cols[0]);
							if(v == null) {
								spriteNameToMaterialConstants.put(cols[0], value);
							}else if(v.intValue() != value) {
								logger.warn("Inconsistent material definition for sprite \"{}\": {}", cols[0], line);
							}
						}
						continue;
					}catch(NumberFormatException fmt) {
					}
				}
				logger.error("Skipping bad material constant entry: {}", line);
			}
		}catch(IOException ex) {
			logger.error("Could not load \"{}\"!", resourceLocation.toString());
			logger.error(ex);
		}
	}

}
