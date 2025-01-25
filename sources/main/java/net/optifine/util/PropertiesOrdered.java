package net.optifine.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import net.lax1dude.eaglercraft.v1_8.EaglerProperties;

public class PropertiesOrdered extends EaglerProperties {
	private Set<Object> keysOrdered = new LinkedHashSet();

	public Object put(Object key, Object value) {
		this.keysOrdered.add(key);
		return super.put(key, value);
	}

	public Set<Object> keySet() {
		Set<Object> set = super.keySet();
		this.keysOrdered.retainAll(set);
		return Collections.<Object>unmodifiableSet(this.keysOrdered);
	}

	public Enumeration<Object> keys() {
		return Collections.<Object>enumeration(this.keySet());
	}
}
