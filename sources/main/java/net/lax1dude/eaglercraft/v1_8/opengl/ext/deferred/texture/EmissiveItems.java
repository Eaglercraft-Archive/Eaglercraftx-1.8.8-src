package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
public class EmissiveItems implements IResourceManagerReloadListener {

	private static final Logger logger = LogManager.getLogger("EmissiveItemsCSV");

	private static final Map<String,float[]> entries = new HashMap();

	public static float[] getItemEmission(ItemStack itemStack) {
		return getItemEmission(itemStack.getItem(), itemStack.getItemDamage());
	}

	public static float[] getItemEmission(Item item, int damage) {
		return entries.get(Item.itemRegistry.getNameForObject(item).toString() + "#" + damage);
	}

	@Override
	public void onResourceManagerReload(IResourceManager var1) {
		try {
			IResource itemsCsv = var1.getResource(new ResourceLocation("eagler:glsl/deferred/emissive_items.csv"));
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(itemsCsv.getInputStream(), StandardCharsets.UTF_8))) {
				entries.clear();
				String line;
				boolean firstLine = true;
				while((line = reader.readLine()) != null) {
					if((line = line.trim()).length() > 0) {
						if(firstLine) {
							firstLine = false;
							continue;
						}
						String[] split = line.split(",");
						if(split.length == 6) {
							try {
								int dmg = Integer.parseInt(split[1]);
								float r = Float.parseFloat(split[2]);
								float g = Float.parseFloat(split[3]);
								float b = Float.parseFloat(split[4]);
								float i = Float.parseFloat(split[5]);
								r *= i;
								g *= i;
								b *= i;
								entries.put(split[0] + "#" + dmg, new float[] { r, g, b });
								continue;
							}catch(NumberFormatException ex) {
							}
						}
						logger.error("Skipping bad emissive item entry: {}", line);
					}
				}
			}
		}catch(Throwable t) {
			logger.error("Could not load list of emissive items!");
			logger.error(t);
		}
	}

}
