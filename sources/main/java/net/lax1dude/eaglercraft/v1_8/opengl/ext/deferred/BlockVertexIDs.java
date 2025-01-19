package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import com.carrotsearch.hppc.ObjectIntHashMap;
import com.carrotsearch.hppc.ObjectIntMap;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.client.resources.IResource;
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
public class BlockVertexIDs implements IResourceManagerReloadListener {

	private static final Logger logger = LogManager.getLogger("BlockVertexIDsCSV");

	public static final ObjectIntMap<String> modelToID = new ObjectIntHashMap<>();

	public static int builtin_water_still_vertex_id = 0;
	public static int builtin_water_flow_vertex_id = 0;

	@Override
	public void onResourceManagerReload(IResourceManager var1) {
		try {
			IResource itemsCsv = var1.getResource(new ResourceLocation("eagler:glsl/deferred/vertex_ids.csv"));
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(itemsCsv.getInputStream(), StandardCharsets.UTF_8))) {
				modelToID.clear();
				String line;
				boolean firstLine = true;
				while((line = reader.readLine()) != null) {
					if((line = line.trim()).length() > 0) {
						if(firstLine) {
							firstLine = false;
							continue;
						}
						String[] split = line.split(",");
						if(split.length == 2) {
							try {
								int i = Integer.parseInt(split[1]);
								if(i <= 0 || i > 254) {
									logger.error("Error: {}: Only IDs 1 to 254 are configurable!", split[0]);
									throw new NumberFormatException();
								}
								i -= 127;
								modelToID.put(split[0], i);
								switch(split[0]) {
								case "eagler:builtin/water_still_vertex_id":
									builtin_water_still_vertex_id = i;
									break;
								case "eagler:builtin/water_flow_vertex_id":
									builtin_water_flow_vertex_id = i;
									break;
								default:
									break;
								}
								continue;
							}catch(NumberFormatException ex) {
							}
						}
						logger.error("Skipping bad vertex id entry: {}", line);
					}
				}
			}
		}catch(Throwable t) {
			logger.error("Could not load list of vertex ids!");
			logger.error(t);
		}
	}

}
