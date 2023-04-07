package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrTokenizer;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * Copyright (c) 2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info) 
 * 
 */
public class ShaderSource {

	private static final Logger logger = LogManager.getLogger("ShaderSource");

	public static final ResourceLocation accel_particle_vsh = new ResourceLocation("eagler:glsl/deferred/accel_particle.vsh");
	public static final ResourceLocation accel_particle_gbuffer_fsh = new ResourceLocation("eagler:glsl/deferred/accel_particle_gbuffer.fsh");
	public static final ResourceLocation accel_particle_forward_fsh = new ResourceLocation("eagler:glsl/deferred/accel_particle_forward.fsh");
	public static final ResourceLocation deferred_core_vsh = new ResourceLocation("eagler:glsl/deferred/deferred_core.vsh");
	public static final ResourceLocation deferred_core_gbuffer_fsh = new ResourceLocation("eagler:glsl/deferred/deferred_core_gbuffer.fsh");
	public static final ResourceLocation deferred_shadow_vsh = new ResourceLocation("eagler:glsl/deferred/deferred_shadow.vsh");
	public static final ResourceLocation deferred_shadow_fsh = new ResourceLocation("eagler:glsl/deferred/deferred_shadow.fsh");
	public static final ResourceLocation deferred_local_vsh = new ResourceLocation("eagler:glsl/deferred/deferred_local.vsh");
	public static final ResourceLocation deferred_combine_fsh = new ResourceLocation("eagler:glsl/deferred/deferred_combine.fsh");
	public static final ResourceLocation deferred_fog_fsh = new ResourceLocation("eagler:glsl/deferred/deferred_fog.fsh");
	public static final ResourceLocation forward_core_vsh = new ResourceLocation("eagler:glsl/deferred/forward_core.vsh");
	public static final ResourceLocation forward_core_fsh = new ResourceLocation("eagler:glsl/deferred/forward_core.fsh");
	public static final ResourceLocation forward_glass_highlights_vsh = new ResourceLocation("eagler:glsl/deferred/forward_glass_highlights.vsh");
	public static final ResourceLocation forward_glass_highlights_fsh = new ResourceLocation("eagler:glsl/deferred/forward_glass_highlights.fsh");
	public static final ResourceLocation realistic_water_mask_vsh = new ResourceLocation("eagler:glsl/deferred/realistic_water_mask.vsh");
	public static final ResourceLocation realistic_water_mask_fsh = new ResourceLocation("eagler:glsl/deferred/realistic_water_mask.fsh");
	public static final ResourceLocation realistic_water_render_vsh = new ResourceLocation("eagler:glsl/deferred/realistic_water_render.vsh");
	public static final ResourceLocation realistic_water_render_fsh = new ResourceLocation("eagler:glsl/deferred/realistic_water_render.fsh");
	public static final ResourceLocation realistic_water_control_fsh = new ResourceLocation("eagler:glsl/deferred/realistic_water_control.fsh");
	public static final ResourceLocation realistic_water_normals_fsh = new ResourceLocation("eagler:glsl/deferred/realistic_water_normals.fsh");
	public static final ResourceLocation realistic_water_noise_fsh = new ResourceLocation("eagler:glsl/deferred/realistic_water_noise.fsh");
	public static final ResourceLocation gbuffer_debug_view_fsh = new ResourceLocation("eagler:glsl/deferred/gbuffer_debug_view.fsh");
	public static final ResourceLocation ssao_generate_fsh = new ResourceLocation("eagler:glsl/deferred/ssao_generate.fsh");
	public static final ResourceLocation lighting_sun_fsh = new ResourceLocation("eagler:glsl/deferred/lighting_sun.fsh");
	public static final ResourceLocation shadows_sun_fsh = new ResourceLocation("eagler:glsl/deferred/shadows_sun.fsh");
	public static final ResourceLocation light_shafts_sample_fsh = new ResourceLocation("eagler:glsl/deferred/light_shafts_sample.fsh");
	public static final ResourceLocation post_tonemap_fsh = new ResourceLocation("eagler:glsl/deferred/post_tonemap.fsh");
	public static final ResourceLocation post_bloom_bright_fsh = new ResourceLocation("eagler:glsl/deferred/post_bloom_bright.fsh");
	public static final ResourceLocation post_bloom_blur_fsh = new ResourceLocation("eagler:glsl/deferred/post_bloom_blur.fsh");
	public static final ResourceLocation post_lens_distort_fsh = new ResourceLocation("eagler:glsl/deferred/post_lens_distort.fsh");
	public static final ResourceLocation post_exposure_avg_fsh = new ResourceLocation("eagler:glsl/deferred/post_exposure_avg.fsh");
	public static final ResourceLocation post_exposure_final_fsh = new ResourceLocation("eagler:glsl/deferred/post_exposure_final.fsh");
	public static final ResourceLocation post_lens_streaks_vsh = new ResourceLocation("eagler:glsl/deferred/post_lens_streaks.vsh");
	public static final ResourceLocation post_lens_streaks_fsh = new ResourceLocation("eagler:glsl/deferred/post_lens_streaks.fsh");
	public static final ResourceLocation post_lens_ghosts_vsh = new ResourceLocation("eagler:glsl/deferred/post_lens_ghosts.vsh");
	public static final ResourceLocation post_lens_ghosts_fsh = new ResourceLocation("eagler:glsl/deferred/post_lens_ghosts.fsh");
	public static final ResourceLocation lens_sun_occlusion_fsh = new ResourceLocation("eagler:glsl/deferred/lens_sun_occlusion.fsh");
	public static final ResourceLocation skybox_atmosphere_fsh = new ResourceLocation("eagler:glsl/deferred/skybox_atmosphere.fsh");
	public static final ResourceLocation skybox_irradiance_fsh = new ResourceLocation("eagler:glsl/deferred/skybox_irradiance.fsh");
	public static final ResourceLocation skybox_render_vsh = new ResourceLocation("eagler:glsl/deferred/skybox_render.vsh");
	public static final ResourceLocation skybox_render_fsh = new ResourceLocation("eagler:glsl/deferred/skybox_render.fsh");
	public static final ResourceLocation skybox_render_end_vsh = new ResourceLocation("eagler:glsl/deferred/skybox_render_end.vsh");
	public static final ResourceLocation skybox_render_end_fsh = new ResourceLocation("eagler:glsl/deferred/skybox_render_end.fsh");
	public static final ResourceLocation moon_render_vsh = new ResourceLocation("eagler:glsl/deferred/moon_render.vsh");
	public static final ResourceLocation moon_render_fsh = new ResourceLocation("eagler:glsl/deferred/moon_render.fsh");
	public static final ResourceLocation clouds_noise3d_fsh = new ResourceLocation("eagler:glsl/deferred/clouds_noise3d.fsh");
	public static final ResourceLocation clouds_shapes_fsh = new ResourceLocation("eagler:glsl/deferred/clouds_shapes.fsh");
	public static final ResourceLocation clouds_shapes_vsh = new ResourceLocation("eagler:glsl/deferred/clouds_shapes.vsh");
	public static final ResourceLocation clouds_sample_fsh = new ResourceLocation("eagler:glsl/deferred/clouds_sample.fsh");
	public static final ResourceLocation clouds_sun_occlusion_fsh = new ResourceLocation("eagler:glsl/deferred/clouds_sun_occlusion.fsh");
	public static final ResourceLocation clouds_color_fsh = new ResourceLocation("eagler:glsl/deferred/clouds_color.fsh");
	public static final ResourceLocation lighting_mesh_vsh = new ResourceLocation("eagler:glsl/deferred/lighting_mesh.vsh");
	public static final ResourceLocation lighting_point_fsh = new ResourceLocation("eagler:glsl/deferred/lighting_point.fsh");
	public static final ResourceLocation reproject_control_fsh = new ResourceLocation("eagler:glsl/deferred/reproject_control.fsh");
	public static final ResourceLocation reproject_ssr_fsh = new ResourceLocation("eagler:glsl/deferred/reproject_ssr.fsh");
	public static final ResourceLocation post_fxaa_fsh = new ResourceLocation("eagler:glsl/deferred/post_fxaa.fsh");
	public static final ResourceLocation hand_depth_mask_fsh = new ResourceLocation("eagler:glsl/deferred/hand_depth_mask.fsh");

	private static final Map<ResourceLocation, String> sourceCache = new HashMap();

	private static boolean isHighP = false;

	public static String getSourceFor(ResourceLocation path) {
		return getSourceFor(path, 0);
	}

	private static String getSourceFor(ResourceLocation path, int lineNumberOffset) {
		String str = sourceCache.get(path);
		if(str == null) {
			try {
				str = loadSource(path, lineNumberOffset);
			}catch(IOException ex) {
				str = "";
				logger.error("Could not load shader source \"{}\"! {}", deferred_core_vsh, ex.toString());
				logger.error("This may cause a NullPointerException when enabling certain features");
				logger.error(ex);
			}
			sourceCache.put(path, str);
		}
		return str.length() == 0 ? null : str;
	}

	private static String loadSource(ResourceLocation resourceLocation, int fileID) throws IOException {
		StringBuilder ret = new StringBuilder();
		try(InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation).getInputStream()) {
			int lineCounter = 1;
			BufferedReader lineReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			String line;
			while((line = lineReader.readLine()) != null) {
				if(line.startsWith("#line ")) {
					String[] split = line.split("\\s+", 3);
					try {
						lineCounter = Integer.parseInt(split[1]);
					}catch(NumberFormatException ex) {
						throw new IOException("Invalid preprocessor directive: " + line, ex);
					}
					ret.append("#line ").append(lineCounter).append(' ').append(fileID).append('\n');
				}else if(line.startsWith("#EAGLER ")) {
					StrTokenizer tokenizer = new StrTokenizer(line.substring(8));
					tokenizer.setDelimiterChar(' ');
					tokenizer.setIgnoreEmptyTokens(true);
					tokenizer.setQuoteChar('\"');
					if(tokenizer.hasNext()) {
						String p1 = tokenizer.next();
						if(p1.equals("INCLUDE") && tokenizer.hasNext()) {
							String fileIDStr = tokenizer.next();
							if(tokenizer.hasNext() && fileIDStr.charAt(0) == '(' && fileIDStr.charAt(fileIDStr.length() - 1) == ')') {
								String includePath = tokenizer.next();
								if(!tokenizer.hasNext()) { // ignore if there are extra arguments
									int newFileId = -1;
									try {
										newFileId = Integer.parseInt(fileIDStr.substring(1, fileIDStr.length() - 1));
									}catch(NumberFormatException ex) {
									}
									if(newFileId != -1) {
										newFileId += fileID * 100;
										ret.append('\n').append("////////////////////////////////////////////////////////////////////").append('\n');
										ret.append("//" + line).append('\n');
										ret.append("#line 1 ").append(newFileId).append('\n');
										ret.append(getSourceFor(new ResourceLocation(includePath), newFileId)).append('\n');
										ret.append("////////////////////////////////////////////////////////////////////").append('\n');
										ret.append("#line ").append(lineCounter - 1).append(' ').append(fileID).append('\n').append('\n');
										++lineCounter;
										continue;
									}
								}
							}
						}
					}
					logger.error("Skipping invalid preprocessor directive: " + line);
					ret.append("// [INVALID]: " + line).append('\n');
				}else if(isHighP && line.startsWith("precision")) {
					ret.append(line.replace("lowp", "highp").replace("mediump", "highp")).append('\n');
				}else {
					ret.append(line).append('\n');
				}
				++lineCounter;
			}
		}
		return ret.toString();
	}

	public static void clearCache() {
		sourceCache.clear();
		logger.info("Cleared Cache");
	}

	public static void setHighP(boolean b) {
		isHighP = b;
	}

}
