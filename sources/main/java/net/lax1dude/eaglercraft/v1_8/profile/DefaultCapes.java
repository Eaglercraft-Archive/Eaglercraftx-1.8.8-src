/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.profile;

import net.minecraft.util.ResourceLocation;

public enum DefaultCapes {

	NO_CAPE(0, "No Cape", null),
	MINECON_2011(1, "Minecon 2011", new ResourceLocation("eagler:capes/01.minecon_2011.png")),
	MINECON_2012(2, "Minecon 2012", new ResourceLocation("eagler:capes/02.minecon_2012.png")),
	MINECON_2013(3, "Minecon 2013", new ResourceLocation("eagler:capes/03.minecon_2013.png")),
	MINECON_2015(4, "Minecon 2015", new ResourceLocation("eagler:capes/04.minecon_2015.png")),
	MINECON_2016(5, "Minecon 2016", new ResourceLocation("eagler:capes/05.minecon_2016.png")),
	MICROSOFT_ACCOUNT(6, "Microsoft Account", new ResourceLocation("eagler:capes/06.microsoft_account.png")),
	MAPMAKER(7, "Realms Mapmaker", new ResourceLocation("eagler:capes/07.mapmaker.png")),
	MOJANG_OLD(8, "Mojang Old", new ResourceLocation("eagler:capes/08.mojang_old.png")),
	MOJANG_NEW(9, "Mojang New", new ResourceLocation("eagler:capes/09.mojang_new.png")),
	JIRA_MOD(10, "Jira Moderator", new ResourceLocation("eagler:capes/10.jira_mod.png")),
	MOJANG_VERY_OLD(11, "Mojang Very Old", new ResourceLocation("eagler:capes/11.mojang_very_old.png")),
	SCROLLS(12, "Scrolls", new ResourceLocation("eagler:capes/12.scrolls.png")),
	COBALT(13, "Cobalt", new ResourceLocation("eagler:capes/13.cobalt.png")),
	TRANSLATOR(14, "Lang Translator", new ResourceLocation("eagler:capes/14.translator.png")),
	MILLIONTH_ACCOUNT(15, "Millionth Player", new ResourceLocation("eagler:capes/15.millionth_account.png")),
	PRISMARINE(16, "Prismarine", new ResourceLocation("eagler:capes/16.prismarine.png")),
	SNOWMAN(17, "Snowman", new ResourceLocation("eagler:capes/17.snowman.png")),
	SPADE(18, "Spade", new ResourceLocation("eagler:capes/18.spade.png")),
	BIRTHDAY(19, "Birthday", new ResourceLocation("eagler:capes/19.birthday.png")),
	DB(20, "dB", new ResourceLocation("eagler:capes/20.db.png")),
	_15TH_ANNIVERSARY(21, "15th Anniversary", new ResourceLocation("eagler:capes/21.15th_anniversary.png")),
	VANILLA(22, "Vanilla", new ResourceLocation("eagler:capes/22.vanilla.png")),
	TIKTOK(23, "TikTok", new ResourceLocation("eagler:capes/23.tiktok.png")),
	PURPLE_HEART(24, "Purple Heart", new ResourceLocation("eagler:capes/24.purple_heart.png")),
	CHERRY_BLOSSOM(25, "Cherry Blossom", new ResourceLocation("eagler:capes/25.cherry_blossom.png"));
	
	public static final DefaultCapes[] defaultCapesMap = new DefaultCapes[26];
	
	public final int id;
	public final String name;
	public final ResourceLocation location;
	
	private DefaultCapes(int id, String name, ResourceLocation location) {
		this.id = id;
		this.name = name;
		this.location = location;
	}
	
	public static DefaultCapes getCapeFromId(int id) {
		DefaultCapes e = null;
		if(id >= 0 && id < defaultCapesMap.length) {
			e = defaultCapesMap[id];
		}
		if(e != null) {
			return e;
		}else {
			return NO_CAPE;
		}
	}
	
	static {
		DefaultCapes[] capes = values();
		for(int i = 0; i < capes.length; ++i) {
			defaultCapesMap[capes[i].id] = capes[i];
		}
	}

}