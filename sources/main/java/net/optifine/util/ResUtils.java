package net.optifine.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.IResourcePack;

public class ResUtils {

	public static List<String> collectPropertyFiles(IResourcePack rp, String prefix) {
		List<String> ret = new ArrayList<>();
		for (String str : rp.getEaglerFileIndex().getPropertiesFiles()) {
			if (str.startsWith(prefix)) {
				ret.add(str);
			}
		}
		return ret;
	}

	public static List<String> collectPropertyFiles(IResourcePack rp, String... prefixes) {
		List<String> ret = new ArrayList<>();
		for (String str : rp.getEaglerFileIndex().getPropertiesFiles()) {
			for (int i = 0; i < prefixes.length; ++i) {
				if (str.startsWith(prefixes[i])) {
					ret.add(str);
				}
			}
		}
		return ret;
	}

	public static List<String> collectPotionFiles(IResourcePack rp, String prefix) {
		List<String> ret = new ArrayList<>();
		for (String str : rp.getEaglerFileIndex().getCITPotionsFiles()) {
			if (str.startsWith(prefix)) {
				ret.add(str);
			}
		}
		return ret;
	}

	public static List<String> collectPotionFiles(IResourcePack rp, String... prefixes) {
		List<String> ret = new ArrayList<>();
		for (String str : rp.getEaglerFileIndex().getCITPotionsFiles()) {
			for (int i = 0; i < prefixes.length; ++i) {
				if (str.startsWith(prefixes[i])) {
					ret.add(str);
				}
			}
		}
		return ret;
	}

}
