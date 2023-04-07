package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

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
import net.minecraft.util.ResourceLocation;

public class BlockVertexIDs implements IResourceManagerReloadListener {

	private static final Logger logger = LogManager.getLogger("BlockVertexIDsCSV");

	public static final Map<String,Integer> modelToID = new HashMap();

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
