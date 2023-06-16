package net.lax1dude.eaglercraft.v1_8.mojang.authlib;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
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
public class GameProfile {
	
	private final EaglercraftUUID id;

	private final String name;

	private final Multimap<String, Property> properties;

	private TexturesProperty textures = null;

	public GameProfile(EaglercraftUUID id, String name) {
		this(id, name, MultimapBuilder.hashKeys().arrayListValues().build());
	}

	public GameProfile(EaglercraftUUID id, String name, Multimap<String, Property> properties) {
		if (id == null && StringUtils.isBlank(name))
			throw new IllegalArgumentException("Name and ID cannot both be blank");
		this.id = id;
		this.name = name;
		this.properties = properties;
	}

	public EaglercraftUUID getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public boolean isComplete() {
		return (this.id != null && StringUtils.isNotBlank(getName()));
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		GameProfile that = (GameProfile) o;
		if ((this.id != null) ? !this.id.equals(that.id) : (that.id != null))
			return false;
		if ((this.name != null) ? !this.name.equals(that.name) : (that.name != null))
			return false;
		return true;
	}

	public int hashCode() {
		int result = (this.id != null) ? this.id.hashCode() : 0;
		result = 31 * result + ((this.name != null) ? this.name.hashCode() : 0);
		return result;
	}

	public String toString() {
		return (new ToStringBuilder(this)).append("id", this.id).append("name", this.name)
				.append("legacy", false).toString();
	}

	public boolean isLegacy() {
		return false;
	}

	public Multimap<String, Property> getProperties() {
		return properties;
	}

	public TexturesProperty getTextures() {
		if(textures == null) {
			textures = TexturesProperty.parseProfile(this);
		}
		return textures;
	}
}
