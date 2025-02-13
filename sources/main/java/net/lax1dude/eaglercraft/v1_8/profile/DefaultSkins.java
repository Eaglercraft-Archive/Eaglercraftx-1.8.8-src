/*
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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

public enum DefaultSkins {

	DEFAULT_STEVE(0, "Default Steve", new ResourceLocation("eagler:skins/01.default_steve.png"), SkinModel.STEVE),
	DEFAULT_ALEX(1, "Default Alex", new ResourceLocation("eagler:skins/02.default_alex.png"), SkinModel.ALEX),
	TENNIS_STEVE(2, "Tennis Steve", new ResourceLocation("eagler:skins/03.tennis_steve.png"), SkinModel.STEVE),
	TENNIS_ALEX(3, "Tennis Alex", new ResourceLocation("eagler:skins/04.tennis_alex.png"), SkinModel.ALEX),
	TUXEDO_STEVE(4, "Tuxedo Steve", new ResourceLocation("eagler:skins/05.tuxedo_steve.png"), SkinModel.STEVE),
	TUXEDO_ALEX(5, "Tuxedo Alex", new ResourceLocation("eagler:skins/06.tuxedo_alex.png"), SkinModel.ALEX),
	ATHLETE_STEVE(6, "Athlete Steve", new ResourceLocation("eagler:skins/07.athlete_steve.png"), SkinModel.STEVE),
	ATHLETE_ALEX(7, "Athlete Alex", new ResourceLocation("eagler:skins/08.athlete_alex.png"), SkinModel.ALEX),
	CYCLIST_STEVE(8, "Cyclist Steve", new ResourceLocation("eagler:skins/09.cyclist_steve.png"), SkinModel.STEVE),
	CYCLIST_ALEX(9, "Cyclist Alex", new ResourceLocation("eagler:skins/10.cyclist_alex.png"), SkinModel.ALEX),
	BOXER_STEVE(10, "Boxer Steve", new ResourceLocation("eagler:skins/11.boxer_steve.png"), SkinModel.STEVE),
	BOXER_ALEX(11, "Boxer Alex", new ResourceLocation("eagler:skins/12.boxer_alex.png"), SkinModel.ALEX),
	PRISONER_STEVE(12, "Prisoner Steve", new ResourceLocation("eagler:skins/13.prisoner_steve.png"), SkinModel.STEVE),
	PRISONER_ALEX(13, "Prisoner Alex", new ResourceLocation("eagler:skins/14.prisoner_alex.png"), SkinModel.ALEX),
	SCOTTISH_STEVE(14, "Scottish Steve", new ResourceLocation("eagler:skins/15.scottish_steve.png"), SkinModel.STEVE),
	SCOTTISH_ALEX(15, "Scottish Alex", new ResourceLocation("eagler:skins/16.scottish_alex.png"), SkinModel.ALEX),
	DEVELOPER_STEVE(16, "Developer Steve", new ResourceLocation("eagler:skins/17.developer_steve.png"), SkinModel.STEVE),
	DEVELOPER_ALEX(17, "Developer Alex", new ResourceLocation("eagler:skins/18.developer_alex.png"), SkinModel.ALEX),
	HEROBRINE(18, "Herobrine", new ResourceLocation("eagler:skins/19.herobrine.png"), SkinModel.ZOMBIE),
	NOTCH(19, "Notch", new ResourceLocation("eagler:skins/20.notch.png"), SkinModel.STEVE),
	CREEPER(20, "Creeper", new ResourceLocation("eagler:skins/21.creeper.png"), SkinModel.STEVE),
	ZOMBIE(21, "Zombie", new ResourceLocation("eagler:skins/22.zombie.png"), SkinModel.ZOMBIE),
	PIG(22, "Pig", new ResourceLocation("eagler:skins/23.pig.png"), SkinModel.STEVE),
	MOOSHROOM(23, "Mooshroom", new ResourceLocation("eagler:skins/24.mooshroom.png"), SkinModel.STEVE),
	LONG_ARMS(24, "Long Arms", new ResourceLocation("eagler:mesh/longarms.fallback.png"), SkinModel.LONG_ARMS),
	WEIRD_CLIMBER_DUDE(25, "Weird Climber Dude", new ResourceLocation("eagler:mesh/weirdclimber.fallback.png"), SkinModel.WEIRD_CLIMBER_DUDE),
	LAXATIVE_DUDE(26, "Laxative Dude", new ResourceLocation("eagler:mesh/laxativedude.fallback.png"), SkinModel.LAXATIVE_DUDE),
	BABY_CHARLES(27, "Baby Charles", new ResourceLocation("eagler:mesh/charles.fallback.png"), SkinModel.BABY_CHARLES),
	BABY_WINSTON(28, "Baby Winston", new ResourceLocation("eagler:mesh/winston.fallback.png"), SkinModel.BABY_WINSTON);
	
	public static final DefaultSkins[] defaultSkinsMap = new DefaultSkins[29];
	
	public final int id;
	public final String name;
	public final ResourceLocation location;
	public final SkinModel model;
	
	private DefaultSkins(int id, String name, ResourceLocation location, SkinModel model) {
		this.id = id;
		this.name = name;
		this.location = location;
		this.model = model;
	}
	
	public static DefaultSkins getSkinFromId(int id) {
		DefaultSkins e = null;
		if(id >= 0 && id < defaultSkinsMap.length) {
			e = defaultSkinsMap[id];
		}
		if(e != null) {
			return e;
		}else {
			return DEFAULT_STEVE;
		}
	}
	
	static {
		DefaultSkins[] skins = values();
		for(int i = 0; i < skins.length; ++i) {
			defaultSkinsMap[skins[i].id] = skins[i];
		}
	}

}