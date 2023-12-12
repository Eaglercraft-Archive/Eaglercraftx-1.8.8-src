package net.lax1dude.eaglercraft.v1_8.mojang.authlib;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
